//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.entidades_de_negocio;

import java.io.Serializable;
import java.util.ArrayList;

import gestorAplicacion.infraestructura.Canal;

public enum Divisa implements Serializable {
	LIBRA_ESTERLINA(1.25),        // Valor de conversión de la libra esterlina a dólares
	EURO(1.10),                  // Valor de conversión del euro a dólares
	DOLAR(1.0),                  // Valor de conversión del dólar a sí mismo (1.0)
	RUBLO_RUSO(0.013),           // Valor de conversión del rublo ruso a dólares
	YEN_JAPONES(0.0074),         // Valor de conversión del yen japonés a dólares
	PESO_COLOMBIANO(0.00022);    // Valor de conversión del peso colombiano a dólares

	private static final long serialVersionUID = 1L;
	private final double valor;  // Valor de conversión de la divisa

	Divisa(double valor) {
		this.valor = valor;
	}

	public double getValor() {
		return valor;
	}

	/**
	 * Verifica si una divisa está en la posición correcta en una lista de divisas,
	 * dependiendo del orden especificado.
	 *
	 * @param divisas La lista de divisas a comparar. Recibe un ArrayList.
	 * @param divisa La divisa a comparar.
	 * @param orden El orden a verificar, puede ser "Origen" o "Destino".
	 * @return Verdadero si la divisa está en la posición correcta, falso en caso contrario.
	 */
	public static boolean verificarOrden(ArrayList<Divisa> divisas, Divisa divisa, String orden) {
		if (orden.equalsIgnoreCase("Origen")) {
			int index = divisas.indexOf(divisa);
			return divisas.get(index) == divisas.get(0);
		} else if (orden.equalsIgnoreCase("Destino")) {
			int index = divisas.indexOf(divisa);
			return divisas.get(index) == divisas.get(0);
		}
		return false;
	}

	/**
	 * Realiza la conversión de la divisa de origen a la divisa de destino,
	 * aplicando los impuestos del canal correspondiente.
	 *
	 * @param divisas La lista de divisas a comparar. Recibe un ArrayList.
	 * @param canal El canal del cual se obtendrá la tasa de impuesto.
	 * @param monto El monto a convertir.
	 * @return Un ArrayList de doubles, donde el primer valor es la conversión y el segundo valor es el monto de impuestos
	 *         que se descontará al canal.
	 */
	public static ArrayList<Double> convertirDivisas(ArrayList<Divisa> divisas, Canal canal, double monto) {
		Divisa divisaOrigen = divisas.get(0);
		Divisa divisaDestino = divisas.get(1);

		ArrayList<Double> montos = new ArrayList<>();
		double montoFinal = monto;

		// Se calcula el valor del impuesto a descontar del monto
		double impuesto = monto * (canal.getImpuesto() / 100);
		montoFinal -= impuesto;

		// Se realiza la conversión de la divisa de origen a dólar
		montoFinal = montoFinal * divisaOrigen.getValor();
		if (divisaDestino.equals(Divisa.DOLAR)) {
			// Se redondea el monto final a un máximo de dos decimales, pero solo cuando se retorna DOLAR
			montoFinal = Math.round(montoFinal * 100.0) / 100.0;
			montos.add(montoFinal);
			montos.add(impuesto);
			return montos;
		}

		// Se realiza la conversión de la divisa en dólares a la divisa de destino
		montoFinal = Math.round((montoFinal / divisaDestino.getValor()) * 100.0) / 100.0;
		montos.add(montoFinal);
		montos.add(impuesto);

		return montos;
	}
}

