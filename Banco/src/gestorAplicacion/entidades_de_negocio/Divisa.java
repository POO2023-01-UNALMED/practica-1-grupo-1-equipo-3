//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.entidades_de_negocio;

import java.io.Serializable;
import java.util.ArrayList;

import gestorAplicacion.infraestructura.Canal;

public enum Divisa implements Serializable{
	LIBRA_ESTERLINA(1.25),
	EURO(1.10),
	DOLAR(1.0),
	RUBLO_RUSO(0.013),
	YEN_JAPONES(0.0074),
	PESO_COLOMBIANO(0.00022);
	
	private static final long serialVersionUID = 1L;
	private double valor;

	Divisa(double valor) {
		this.valor = valor;
	}

	public double getValor() {
		return valor;
	}

	/**
	 * verifica si una divisa está en la posición correcta en una lista de divisas, dependiendo del orden especificado
	 * 
	 * @param divisas, la lista de divisas a comparar. Recibe un arrayList
	 * @param divisa, la divisa a comparar
	 * @param orden, el orden a verificar, puede ser "Origen" o "Destino"
	 * @return Un valor verdadero o falso que determina si la divisa si se encontraba en la posición correcta o no
	 */
	public static boolean verificarOrden(ArrayList<Divisa> divisas, Divisa divisa, String orden) {
		           if(orden.equalsIgnoreCase("Origen")) {
			int index = divisas.indexOf(divisa);
			return divisas.get(index) == divisas.get(0);
		}
		
		else if(orden.equalsIgnoreCase("Destino")) {
			int index = divisas.indexOf(divisa);
			return divisas.get(index) == divisas.get(0);
		}
		
		return false;
	}


	/**
	 * Realiza la conversión de la divisa de origen a la divisa de destino, aplicandole los impuestos del canal correspondiente
	 * 
	 * @param divisas, la lista de divisas a comparar. Recibe un arrayList
	 * @param canal, de este se obtendrá la tasa de impuesto
	 * @param monto, monto a convertir
	 * @return ArrayList de doubles, el primer valor es la conversión, el segundo el monto de impuestos que será dispuesto al canal
	 */
	public static ArrayList<Double> convertirDivisas(ArrayList<Divisa> divisas, Canal canal, double monto) {
		Divisa divisaOrigen = divisas.get(0);
		Divisa divisaDestino = divisas.get(1);
		
		ArrayList<Double> montos = new ArrayList<>();
		double montoFinal = monto;
		
		//valor que se retornara para el canal cuando se efectue la transaccion
		double impuesto = monto * (canal.getImpuesto() / 100);
		montoFinal -= impuesto;
		
		//convertimos la divisa de origen a dolar
		montoFinal = montoFinal * divisaOrigen.getValor();
		if (divisaDestino.equals(Divisa.DOLAR)) {
			//redondea a un maximo de dos decimales, pero solo cuando se va a retornar DOLAR
			montoFinal = Math.round(montoFinal * 100.0) / 100.0;
			montos.add(montoFinal);
			montos.add(impuesto);
			return montos;			
		} 
		
		//convertimos la divisa ya en dolares, a la divisa de destino
		montoFinal = Math.round((montoFinal / divisaDestino.getValor()) * 100.0) / 100.0;
		montos.add(montoFinal);
		montos.add(impuesto);

		return montos;
	}

}
