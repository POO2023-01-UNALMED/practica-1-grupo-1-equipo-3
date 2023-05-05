package gestorAplicacion.entidades_de_negocio;

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
