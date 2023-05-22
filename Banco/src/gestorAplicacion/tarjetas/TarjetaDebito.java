//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.tarjetas;

import java.io.Serializable;

import gestorAplicacion.entidades_de_negocio.Divisa;
import gestorAplicacion.infraestructura.Banco;
import gestorAplicacion.entidades_de_negocio.Factura;;

public class TarjetaDebito extends Tarjeta implements Serializable{
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
		return "Tipo de tarjeta: Débito\nNúmero de tarjeta: %s\nSaldo: %s %s\n".formatted(getNoTarjeta(), Banco.formatearNumero(saldo), divisa.name());
	}
	
	//

	public boolean transaccion(double cantidad, TarjetaDebito tarjeta) {
		if(saldo >= cantidad && tarjeta.getDivisa().equals(getDivisa())) {
			this.saldo -= cantidad;
			tarjeta.setSaldo(tarjeta.getSaldo() + cantidad);
			return true;
		} else {
			return false;
		}
	}

	public boolean deshacerTransaccion(double cantidad, Tarjeta t){
		if(saldo >= cantidad && (t instanceof TarjetaDebito)) {	//Deshacer transacción en caso de que el origen de la transacción fué una tarjeta de débito
			this.saldo -= Math.floor(100*cantidad*t.divisa.getValor()/this.divisa.getValor())/100;
			((TarjetaDebito)t).setSaldo(((TarjetaDebito)t).getSaldo() + cantidad);
			return true;
		}else if (saldo >= cantidad && t instanceof TarjetaCredito && ((TarjetaCredito)t).getEspacio() >= cantidad){ //Deshacer transacción en caso de que el origen de la transacción fué una tarjeta de crédito
			this.saldo -= Math.floor(100*cantidad*t.divisa.getValor()/this.divisa.getValor())/100;
			((TarjetaCredito)t).setCredito(cantidad);
			return true;
		} else {
			return false;
		}
	}

	
	public boolean tieneSaldo() {
		return saldo != 0.0;
	}

	public boolean puedeTransferir(double monto){
		return monto <= saldo;
	}
	
	public void sacarDinero(double monto){
		if(saldo >= monto){
			saldo -= monto;
		}
	}
	public void introducirDinero(double monto){
		saldo += monto;
	}


}
