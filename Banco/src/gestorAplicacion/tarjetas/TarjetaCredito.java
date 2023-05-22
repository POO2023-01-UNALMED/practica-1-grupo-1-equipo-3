//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.tarjetas;

import gestorAplicacion.infraestructura.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gestorAplicacion.entidades_de_negocio.Divisa;
import gestorAplicacion.entidades_de_negocio.Cliente;

public class TarjetaCredito extends Tarjeta implements Serializable {
	private final double CREDITOMAXIMO; // Límite de crédito máximo de la tarjeta
	private double credito; // Cantidad de crédito utilizada actualmente
	private final double INTERES; // Tasa de interés aplicada al crédito

	public TarjetaCredito(int noTarjeta, Divisa divisa, double creditoMaximo, Double interes) {
		super(noTarjeta, divisa);
		this.CREDITOMAXIMO = creditoMaximo;
		this.credito = 0;
		this.INTERES = interes;
	}

	/**
	 * Método toString() para obtener una representación en cadena de la tarjeta de crédito.
	 * @return Cadena con la información de la tarjeta de crédito.
	 */
	public String toString() {
		return "Tipo de tarjeta: Crédito\n" +
				"Número de tarjeta: %s\n" +
				"Límite: %s %s\n" +
				"Crédito: %s %s\n" +
				"Tasa de interés: %s\n"
						.formatted(noTarjeta, Banco.formatearNumero(CREDITOMAXIMO), divisa.name(), Banco.formatearNumero(credito), divisa.name(), INTERES);
	}

	/**
	 * Realiza una transacción desde una tarjeta de débito a la tarjeta de crédito.
	 * @param cantidad Cantidad a transferir.
	 * @param t Tarjeta de débito origen.
	 * @return true si la transacción es exitosa, false en caso contrario.
	 */
	public boolean transaccion(double cantidad, TarjetaDebito t) {
		if (CREDITOMAXIMO - credito >= cantidad && t.getDivisa().equals(getDivisa())) {
			credito += cantidad;
			t.setSaldo(t.getSaldo() + cantidad);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Deshace una transacción realizada desde la tarjeta actual a otra tarjeta.
	 * @param cantidad Cantidad a deshacer.
	 * @param t Tarjeta destino.
	 * @return true si la deshacer la transacción es exitoso, false en caso contrario.
	 */
	public boolean deshacerTransaccion(double cantidad, Tarjeta t) {
		if (CREDITOMAXIMO - credito >= cantidad && (t instanceof TarjetaDebito)) {
			// Deshacer una transacción desde una tarjeta de débito
			this.credito += Math.floor(100 * cantidad * t.divisa.getValor() / this.divisa.getValor()) / 100;
			((TarjetaDebito) t).setSaldo(((TarjetaDebito) t).getSaldo() + cantidad);
			return true;
		} else if (CREDITOMAXIMO - credito >= cantidad && t instanceof TarjetaCredito && ((TarjetaCredito) t).getEspacio() >= cantidad) {
			// Deshacer una transacción desde otra tarjeta de crédito
			this.credito += Math.floor(100 * cantidad * t.divisa.getValor() / this.divisa.getValor()) / 100;
			((TarjetaCredito) t).setCredito(cantidad);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Verifica si la tarjeta de crédito tiene suficiente espacio disponible para realizar una transferencia.
	 * @param monto Monto de la transferencia.
	 * @return true si se puede realizar la transferencia, false en caso contrario.
	 */
	public boolean puedeTransferir(double monto) {
		return (CREDITOMAXIMO - credito) >= monto;
	}

	/**
	 * Verifica si la tarjeta de crédito tiene saldo disponible (crédito no utilizado).
	 * @return true si tiene saldo disponible, false en caso contrario.
	 */
	public boolean tieneSaldo() {
		return credito != CREDITOMAXIMO;
	}

	/**
	 * Retira dinero de la tarjeta de crédito.
	 * @param monto Monto a retirar.
	 */
	public void sacarDinero(double monto) {
		if (this.credito + monto <= this.CREDITOMAXIMO) {
			this.credito += monto;
		}
	}

	/**
	 * Ingresa dinero a la tarjeta de crédito.
	 * @param monto Monto a ingresar.
	 */
	public void introducirDinero(double monto) {
		if (this.credito - monto >= 0) {
			this.credito -= monto;
		}
	}

	/**
	 * Genera una lista de tarjetas de crédito disponibles según el puntaje y divisa especificados.
	 * @param puntaje Puntaje del cliente.
	 * @param divisa Divisa de las tarjetas.
	 * @return Lista de tarjetas de crédito disponibles.
	 */
	public static ArrayList<TarjetaCredito> tarjetasDisponibles(int puntaje, Divisa divisa) {
		Map<Integer, Double> reqCredMax = new HashMap<>();
		Map<Integer, Double> reqInteres = new HashMap<>();
		reqCredMax.put(0, 100.0);
		reqInteres.put(0, 10.0);
		reqCredMax.put(50, 500.0);
		reqInteres.put(50, 7.0);
		reqCredMax.put(100, 1000.0);
		reqInteres.put(100, 5.0);
		reqCredMax.put(150, 2000.0);
		reqInteres.put(150, 4.0);
		reqCredMax.put(200, 3000.0);
		reqInteres.put(200, 3.0);

		ArrayList<TarjetaCredito> tarjetas = new ArrayList<>();
		int noTarjeta;
		do {
			noTarjeta = (int) Math.floor(Math.random() * (999999999 - 100000000 + 1) + 100000000);
			// Genera un número de tarjeta aleatorio entre 999999999 y 100000000.
			// Verifica que el número de tarjeta generado no esté siendo utilizado (improbable pero necesario).
		} while (Banco.numeroExistente(noTarjeta));

		for (int i : reqCredMax.keySet()) {
			if (i > puntaje)
				break;
			else
				tarjetas.add(new TarjetaCredito(noTarjeta, divisa, Math.floor(100 * reqCredMax.get(i) / divisa.getValor()) / 100, reqInteres.get(i)));
			// Agrega una tarjeta de crédito a la lista con el número de tarjeta, límite de crédito y tasa de interés correspondientes.
		}

		return tarjetas;
	}

	/**
	 * Obtiene el espacio disponible en la tarjeta de crédito (crédito restante).
	 * @return Espacio disponible en la tarjeta de crédito.
	 */
	public double getEspacio() {
		return CREDITOMAXIMO - credito;
	}

	/**
	 * Establece el crédito utilizado en la tarjeta de crédito.
	 * @param credito Crédito utilizado a establecer.
	 */
	public void setCredito(double credito) {
		if (credito >= 0) {
			this.credito = credito;
		}
	}

	/**
	 * Añade una tarjeta de crédito a un cliente y actualiza el bono actual del cliente.
	 * @param tarjeta Tarjeta de crédito a añadir.
	 * @param cliente Cliente al que se añade la tarjeta.
	 * @param bono Bono actual del cliente.
	 */
	public static void anadirTarjetaCredito(TarjetaCredito tarjeta, Cliente cliente, int bono) {
		cliente.getTarjetasCredito().add(tarjeta);
		cliente.setBonoActual(bono);
	}
}
