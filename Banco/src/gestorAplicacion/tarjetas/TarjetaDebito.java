package gestorAplicacion.tarjetas;

import gestorAplicacion.entidades_de_negocio.Divisa;

public class TarjetaDebito extends Tarjeta{
	private double saldo;

	public TarjetaDebito(int noTarjeta, Divisa divisa, double saldo){
		super(noTarjeta, divisa);
		this.saldo = saldo;
	}
	
	//Getters y Setters
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	//Metodos de las instancias
	public String toString() {
		return "Tipo de tarjeta: Débito\nNúmero de tarjeta: %s\nSaldo: %s %s\n".formatted(getNoTarjeta(), saldo, getDivisa().getMoneda());
	}
	
	//

	public boolean transaccion(double cantidad, TarjetaDebito t) {
		if(saldo >= cantidad && t.getDivisa().equals(getDivisa())) {
			this.saldo -= cantidad;
			t.setSaldo(t.getSaldo() + cantidad);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean tieneSaldo() {
		if (saldo == 0.0)
			return false;
		return true;
	}

	public boolean puedeTransferir(double monto){
		if(monto <= saldo){
			return true;
		} else {
			return false;
		}
	}
	
	public void sacarDinero(double monto){
		if(saldo >= monto){
			saldo -= monto;
		}
	}
}
