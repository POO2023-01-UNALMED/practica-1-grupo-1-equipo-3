package Banco;

public class TarjetaDebito extends Tarjeta{
	private double saldo;

	public TarjetaDebito(int noTarjeta, Divisa divisa, double saldo){
		super(noTarjeta, divisa);
		this.saldo = saldo;
	}
	
	public String verTarjeta() {
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
}
