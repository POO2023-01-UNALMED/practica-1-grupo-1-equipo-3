package gestorAplicacion.tarjetas;

import gestorAplicacion.entidades_de_negocio.Divisa;

public class TarjetaCredito extends Tarjeta {
	private double creditoMaximo; // Es el límite de dinero que se puede prestar mediante esta tarjeta
	private double credito; //Es la cantidad de dinero que el usuario está debiendo en este momento
	private float interes;
	
	public TarjetaCredito(int noTarjeta, Divisa divisa, double creditoMaximo, float interes) {
		super(noTarjeta, divisa);
		this.creditoMaximo = creditoMaximo;
		this.credito = 0;
		this.interes = interes;
	}
	
	public String toString() {
		return "Tipo de tarjeta: Crédito\nNúmero de tarjeta: %s\nLímite: %s %s\nCrédito: %s %s\nTaza de interés: %s\n".formatted(noTarjeta, creditoMaximo, divisa.name(), credito, divisa.name(), interes);
	}

	public boolean transaccion(double cantidad, TarjetaDebito t) {
		if(creditoMaximo - credito >= cantidad && t.getDivisa().equals(getDivisa())) {
			credito += cantidad;
			t.setSaldo(t.getSaldo() + cantidad);
			return true;
		} else {
			return false;
		}
	}

	public boolean puedeTransferir(double monto){
		if((creditoMaximo-credito)>=monto){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean tieneSaldo() {
		if (credito == creditoMaximo)
			return false;
		return true;
	}

	public void sacarDinero(double monto){
		if(this.credito+monto <= this.creditoMaximo){
			this.credito += monto;
		}
	}
	public void introducirDinero(double monto){
		if(this.credito - monto >= 0){
			this.credito -= monto;
		}
	}

}
