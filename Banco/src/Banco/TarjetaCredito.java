package Banco;

public class TarjetaCredito extends Tarjeta {
	private double creditoMaximo; // Es el límite de dinero que se puede prestar mediante esta tarjeta
	private double credito; //Es la cantidad de dinero que el usuario está debiendo en este momento
	private double interes;
	
	public TarjetaCredito(int noTarjeta, Divisa divisa, float creditoMaximo, float interes) {
		super(noTarjeta, divisa);
		this.creditoMaximo = creditoMaximo;
		this.credito = 0;
		this.interes = interes;
	}
	
	public String verTarjeta() {
		return "Tarjeta de crédito con número" + super.getNoTarjeta() + ", límite: " + creditoMaximo + " y credito " + credito + " " + super.getDivisa().getMoneda() + " con una taza de interés de " + interes;
	}

	public boolean transaccion(double cantidad, TarjetaDebito t) {
		if(creditoMaximo - credito >= cantidad && t.getDivisa().equals(super.getDivisa())) {
			credito += cantidad;
			t.setSaldo(t.getSaldo() + cantidad);
			return true;
		} else {
			return false;
		}
	}
}
