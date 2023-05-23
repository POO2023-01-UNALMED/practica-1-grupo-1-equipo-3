//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.tarjetas;

import gestorAplicacion.entidades_de_negocio.Divisa;
import gestorAplicacion.infraestructura.Banco;
import gestorAplicacion.entidades_de_negocio.Cliente;

import java.io.Serial;


public class TarjetaDebito extends Tarjeta{
	@Serial
	private static final long serialVersionUID = 1L;
	private double saldo;

	public TarjetaDebito(int noTarjeta, Divisa divisa, double saldo) {
		super(noTarjeta, divisa);
		this.saldo = saldo;
	}

	// Getters y Setters
	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	// Métodos de instancia

	/**
	 * Devuelve una representación en forma de cadena de la tarjeta de débito.
	 * El formato incluye el tipo de tarjeta, el número de tarjeta y el saldo.
	 */
	public String toString() {
		return "Tipo de tarjeta: Débito\nNúmero de tarjeta: %s\nSaldo: %s %s\n"
				.formatted(getNoTarjeta(), Banco.formatearNumero(saldo), divisa.name());
	}

	/**
	 * Realiza una transacción de dinero desde esta tarjeta de débito a otra tarjeta de débito.
	 * Se verifica si el saldo es suficiente y si las divisas de ambas tarjetas coinciden.
	 * Si la transacción es exitosa, se actualizan los saldos y se devuelve true;
	 * de lo contrario, se devuelve false.
	 */
	public boolean transaccion(double cantidad, TarjetaDebito tarjeta) {
		if (saldo >= cantidad && tarjeta.getDivisa().equals(getDivisa())) {
			this.saldo -= cantidad;
			tarjeta.setSaldo(tarjeta.getSaldo() + cantidad);
			return true;
		} else {
			return false;
		}
	}

	public String borrar(){
		for(Cliente c : Banco.getClientes()){ //En caso de que queramos borrar, necesitamos quitar la tarjeta de todas los clientes que la tienen
			if(c.getTarjetasDebito().contains(this)){
				c.getTarjetasDebito().remove(this);
			}
		}
		return "La tarjeta de crédito #" + noTarjeta + " será borrada, ya que tiene demasiadas transacciones rechazadas"; 
	}

	/**
	 * Deshace una transacción anteriormente realizada desde esta tarjeta de débito a otra tarjeta.
	 * Se verifica si el saldo es suficiente y si el tipo de tarjeta coincide.
	 * Si la deshacer transacción es exitosa, se actualizan los saldos y se devuelve true;
	 * de lo contrario, se devuelve false.
	 */
	public void deshacerTransaccion(double cantidad, Tarjeta t) {
		if (saldo >= cantidad && (t instanceof TarjetaDebito)) {
			this.saldo -= Math.floor(100 * cantidad * t.divisa.getValor() / this.divisa.getValor()) / 100;
			((TarjetaDebito) t).setSaldo(((TarjetaDebito) t).getSaldo() + cantidad);
		} else if (saldo >= cantidad && t instanceof TarjetaCredito && ((TarjetaCredito) t).getEspacio() >= cantidad) {
			this.saldo -= Math.floor(100 * cantidad * t.divisa.getValor() / this.divisa.getValor()) / 100;
			((TarjetaCredito) t).setCredito(cantidad);
		} else {
		}
	}

	/**
	 * Verifica si la tarjeta de débito tiene saldo disponible.
	 * Devuelve true si el saldo es diferente de cero; de lo contrario, devuelve false.
	 */
	public boolean tieneSaldo() {
		return saldo != 0.0;
	}

	/**
	 * Verifica si la tarjeta de débito puede transferir una determinada cantidad de dinero.
	 * Devuelve true si el monto es menor o igual al saldo disponible; de lo contrario, devuelve false.
	 */
	public boolean puedeTransferir(double monto) {
		return monto <= saldo;
	}

	/**
	 * Retira dinero de la tarjeta de débito.
	 * Actualiza el saldo restando el monto especificado.
	 */
	public void sacarDinero(double monto) {
		if (saldo >= monto) {
			saldo -= monto;
		}
	}

	/**
	 * Ingresa dinero a la tarjeta de débito.
	 * Actualiza el saldo sumando el monto especificado.
	 */
	public void introducirDinero(double monto) {
		saldo += monto;
	}
}
