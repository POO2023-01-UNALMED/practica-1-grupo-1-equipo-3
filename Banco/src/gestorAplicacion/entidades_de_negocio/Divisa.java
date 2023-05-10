package gestorAplicacion.entidades_de_negocio;

import java.util.ArrayList;

public enum Divisa {
	EURO(1.11),
	DOLAR(1.0),
	RUBLO_RUSO(0.012),
	YEN_JAPONES(0.0075),
	PESO_COLOMBIANO(0.00022);
	
	
	
	private double valor;
	//private static Divisa[] divisas = {new Divisa("dolar", 1), new Divisa("euro", 1.11), new Divisa("peso colombiano", 0.00022), new Divisa("yen japonés", 0.0075), new Divisa("rublo ruso", 0.012)};

	Divisa(double valor) {
		this.valor = valor;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}
	
	public static ArrayList<Divisa> getDivisas(){
		ArrayList<Divisa> retorno = new ArrayList<>();
		retorno.add(Divisa.DOLAR);
		retorno.add(Divisa.EURO);
		retorno.add(Divisa.RUBLO_RUSO);
		retorno.add(Divisa.YEN_JAPONES);
		retorno.add(Divisa.PESO_COLOMBIANO);
		return retorno;
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
	
	//Recibe un array normal
	public static boolean verificarOrden(Divisa[] divisas, Divisa divisa, String orden) {
		ArrayList<Divisa> listaDivisa = new ArrayList<>();
		listaDivisa.add(divisas[0]);//divisa de origen
		listaDivisa.add(divisas[1]);//divisa de destino
		
		if(orden.equalsIgnoreCase("Origen")) {
			int index = listaDivisa.indexOf(divisa);
			return listaDivisa.get(index) == listaDivisa.get(0);
		}
		
		else if(orden.equalsIgnoreCase("Destino")) {
			int index = listaDivisa.indexOf(divisa);
			return listaDivisa.get(index) == listaDivisa.get(0);
		}
		
		return false;
	}

}
