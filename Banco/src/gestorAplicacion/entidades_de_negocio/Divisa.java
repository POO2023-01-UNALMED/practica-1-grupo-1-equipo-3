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

	private Divisa(double valor) {
		this.valor = valor;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}
	
	public static ArrayList<Divisa> getDivisas(){
		ArrayList retorno = new ArrayList<Divisa>();
		retorno.add(Divisa.DOLAR);
		retorno.add(Divisa.EURO);
		retorno.add(Divisa.RUBLO_RUSO);
		retorno.add(Divisa.YEN_JAPONES);
		retorno.add(Divisa.PESO_COLOMBIANO);
		return retorno;
	}
	
	
//	public String getMoneda() {
//		return moneda;
//	}
//	public double getValor() {
//		return valor;
//	}
//	public static Divisa[] getDivisas() {
//		return divisas;
//	}

}
