package gestorAplicacion.tarjetas;

import gestorAplicacion.entidades_de_negocio.Divisa;

public class TarjetaDebito extends Tarjeta{
	private double saldo;

	public TarjetaDebito(int noTarjeta, Divisa divisa, double saldo){
		super(noTarjeta, divisa);
		this.saldo = saldo;
	}
	
	public String toString() {
		return "Tarjeta de débito con número " + super.getNoTarjeta() + " y saldo " + saldo + " " + super.getDivisa().getMoneda();
	}
	
	public boolean transaccion(double cantidad, TarjetaDebito t) {
		if(saldo >= cantidad && t.getDivisa().equals(super.getDivisa())) {
			this.saldo -= cantidad;
			t.setSaldo(t.getSaldo() + cantidad);
			return true;
		} else {
			return false;
		}
	}
	
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public boolean poderTransferir(double monto){
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
