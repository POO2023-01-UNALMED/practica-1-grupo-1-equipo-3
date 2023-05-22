//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.tarjetas;

import java.io.Serializable;
import java.util.ArrayList;

import gestorAplicacion.entidades_de_negocio.*;


public abstract class Tarjeta implements Serializable {
	protected final int noTarjeta; // Número de tarjeta
	protected final Divisa divisa; // Divisa asociada a la tarjeta
	protected String estado; // Estado de la tarjeta (ACTIVA, BLOQUEADA, etc.)
	protected int transaccionesRechazadas; // Cantidad de transacciones rechazadas realizadas con la tarjeta
	protected static ArrayList<Tarjeta> tarjetas = new ArrayList<>(); // Lista de todas las tarjetas creadas

	// Constructor de la clase Tarjeta
	public Tarjeta(int noTarjeta, Divisa divisa) {
		this.noTarjeta = noTarjeta;
		this.divisa = divisa;
		estado = "ACTIVA";
		transaccionesRechazadas = 0;
		tarjetas.add(this); // Agregar la tarjeta actual a la lista de tarjetas
	}

	// Métodos de instancia para obtener información de la tarjeta

	/**
	 * Obtiene el número de la tarjeta.
	 * @return Número de tarjeta.
	 */
	public int getNoTarjeta() {
		return noTarjeta;
	}

	/**
	 * Obtiene la divisa asociada a la tarjeta.
	 * @return Divisa de la tarjeta.
	 */
	public Divisa getDivisa() {
		return divisa;
	}

	/**
	 * Obtiene la cantidad de transacciones rechazadas realizadas con la tarjeta.
	 * @return Cantidad de transacciones rechazadas.
	 */
	public int getTransaccionesRechazadas() {
		return transaccionesRechazadas;
	}

	/**
	 * Añade una transacción rechazada y actualiza el estado de la tarjeta si es necesario.
	 */
	public void anadirTransaccionRechazada() {
		if (transaccionesRechazadas >= 3)
			estado = "BLOQUEADA";
		transaccionesRechazadas++;
	}

	/**
	 * Obtiene el estado actual de la tarjeta.
	 * @return Estado de la tarjeta.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Verifica si la tarjeta está activa.
	 * @return true si la tarjeta está activa, false de lo contrario.
	 */
	public boolean isActiva() {
		return getEstado().equalsIgnoreCase("ACTIVA");
	}

	// Método estático para obtener la lista de todas las tarjetas creadas
	public static ArrayList<Tarjeta> getTarjetas() {
		return tarjetas;
	}

	// Métodos abstractos para realizar transacciones y otras operaciones

	/**
	 * Realiza una transacción con la tarjeta.
	 * @param cantidad Cantidad de dinero de la transacción.
	 * @param t Tarjeta de débito asociada.
	 * @return true si la transacción fue exitosa, false de lo contrario.
	 */
	public abstract boolean transaccion(double cantidad, TarjetaDebito t);

	/**
	 * Deshace una transacción realizada con la tarjeta.
	 * @param cantidad Cantidad de dinero de la transacción a deshacer.
	 * @param t Tarjeta asociada a la transacción.
	 * @return true si la transacción se deshizo correctamente, false de lo contrario.
	 */
	public abstract boolean deshacerTransaccion(double cantidad, Tarjeta t);

	/**
	 * Verifica si la tarjeta tiene saldo disponible (para tarjetas de débito) o crédito disponible (para tarjetas de crédito).
	 * @return true si la tarjeta tiene saldo o crédito disponible, false de lo contrario.
	 */
	public abstract boolean tieneSaldo();

	/**
	 * Verifica si la tarjeta tiene los fondos suficientes para realizar una transferencia de dinero.
	 * @param monto Monto de la transferencia.
	 * @return true si la tarjeta tiene los fondos suficientes, false de lo contrario.
	 */
	public abstract boolean puedeTransferir(double monto);

	/**
	 * Retira dinero de la tarjeta.
	 * @param monto Monto a retirar.
	 */
	public abstract void sacarDinero(double monto);

	/**
	 * Deposita dinero en la tarjeta.
	 * @param monto Monto a depositar.
	 */
	public abstract void introducirDinero(double monto);

	// Métodos de la clase Tarjeta

	/**
	 * Obtiene una lista de todas las tarjetas bloqueadas de un cliente.
	 * @param cliente Cliente asociado a las tarjetas.
	 * @return Lista de tarjetas bloqueadas del cliente.
	 */
	public static ArrayList<Tarjeta> TarjetasBloqueadas(Cliente cliente) {
		ArrayList<Tarjeta> retorno = new ArrayList<>();
		for (Tarjeta t : cliente.getTarjetasCredito()) {
			if (!t.isActiva()) {
				retorno.add(t);
			}
		}
		for (Tarjeta t : cliente.getTarjetasDebito()) {
			if (!t.isActiva()) {
				retorno.add(t);
			}
		}
		return retorno;
	}

	/**
	 * Obtiene una lista de todas las tarjetas no bloqueadas de un cliente.
	 * @param cliente Cliente asociado a las tarjetas.
	 * @return Lista de tarjetas no bloqueadas del cliente.
	 */
	public static ArrayList<Tarjeta> TarjetasNoBloqueadas(Cliente cliente) {
		ArrayList<Tarjeta> retorno = new ArrayList<>();
		for (Tarjeta t : cliente.getTarjetasCredito()) {
			if (t.isActiva()) {
				retorno.add(t);
			}
		}
		for (Tarjeta t : cliente.getTarjetasDebito()) {
			if (t.isActiva()) {
				retorno.add(t);
			}
		}
		return retorno;
	}
}

