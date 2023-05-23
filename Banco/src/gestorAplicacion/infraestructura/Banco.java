//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.infraestructura;

import baseDatos.Deserializador;
import gestorAplicacion.entidades_de_negocio.Cliente;
import gestorAplicacion.entidades_de_negocio.Divisa;
import gestorAplicacion.entidades_de_negocio.Transaccion;
import gestorAplicacion.tarjetas.Tarjeta;

import java.io.Serial;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class Banco implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private static List<Cliente> clientes = new ArrayList<>();
	private static List<Canal> canales = new ArrayList<>();

	public Banco() {
		Deserializador.deserializar(this);
	}

	// Métodos de la clase

	/**
	 * Ordena una lista de canales por impuestos de menor a mayor.
	 *
	 * @param canales Lista de canales a ordenar.
	 * @return ArrayList ordenado de canales por impuestos.
	 */
	public static ArrayList<Canal> ordenarCanalesPorImpuestos(List<Canal> canales) {
		canales.sort(Comparator.comparingDouble(Canal::getImpuesto));
		return new ArrayList<>(canales);
	}

	// Getters & Setters

	/**
	 * Obtiene la lista de clientes del banco.
	 *
	 * @return ArrayList de clientes.
	 */
	public static ArrayList<Cliente> getClientes() {
		return (ArrayList<Cliente>) clientes;
	}

	/**
	 * Establece la lista de clientes del banco.
	 *
	 * @param clientes ArrayList de clientes a establecer.
	 */
	public static void setClientes(ArrayList<Cliente> clientes) {
		Banco.clientes = clientes;
	}

	/**
	 * Agrega un cliente a la lista de clientes del banco.
	 *
	 * @param cliente Cliente a agregar.
	 */
	public static void agregarCliente(Cliente cliente) {
		Banco.clientes.add(cliente);
	}

	/**
	 * Obtiene la lista de canales del banco.
	 *
	 * @return ArrayList de canales.
	 */
	public static ArrayList<Canal> getCanales() {
		return (ArrayList<Canal>) canales;
	}

	/**
	 * Establece la lista de canales del banco.
	 *
	 * @param canales ArrayList de canales a establecer.
	 */
	public static void setCanales(ArrayList<Canal> canales) {
		Banco.canales = canales;
	}

	/**
	 * Agrega un canal a la lista de canales del banco.
	 *
	 * @param canal Canal a agregar.
	 */
	public static void agregarCanal(Canal canal) {
		Banco.canales.add(canal);
	}

	/**
	 * Calcula el puntaje de un cliente basado en las transacciones realizadas.
	 *
	 * @param trans ArrayList de transacciones del cliente.
	 * @return Puntaje calculado.
	 */
	public static int calcularPuntaje(ArrayList<Transaccion> trans) {
		int puntaje = 0;
		for (Transaccion t : trans) {
			puntaje += 2 * t.getCantidad() * t.getDivisa().getValor();
		}
		return puntaje;
	}

	/**
	 * Selecciona las divisas utilizadas por un cliente.
	 *
	 * @param cliente Cliente del cual seleccionar las divisas.
	 * @return ArrayList de divisas seleccionadas.
	 */
	public static ArrayList<Divisa> seleccionarDivisa(Cliente cliente) {
		ArrayList<Divisa> retorno = new ArrayList<>();
		for (Divisa d : Divisa.values()) {
			for (Tarjeta t : cliente.getTarjetas()) {
				if (t.getDivisa().equals(d)) {
					retorno.add(d);
					break;
				}
			}
		}
		return retorno;
	}

	/**
	 * Verifica si un número de tarjeta existe en alguna tarjeta de algún cliente.
	 *
	 * @param num Número de tarjeta a verificar.
	 * @return true si el número de tarjeta existe, false en caso contrario.
	 */
	public static boolean numeroExistente(int num) {
		boolean valor = false;
		for (Tarjeta t : Tarjeta.getTarjetas()) {
			if (num == t.getNoTarjeta()) {
				valor = true;
				break;
			}
		}
		return valor;
	}

	/**
	 * Genera una petición de transacción y la guarda en una instancia de Transaccion.
	 *
	 * @param transaccion Transacción a generar la petición.
	 * @param mensaje     Mensaje asociado a la petición.
	 */
	public static void generarPeticion(Transaccion transaccion, String mensaje) {
		transaccion.setRetornable(false);
		new Transaccion(transaccion, mensaje);
	}

	/**
	 * Formatea un número de acuerdo a las reglas de formato de Colombia.
	 *
	 * @param numero Número a formatear.
	 * @return El número formateado de acuerdo a las reglas de formato de Colombia.
	 *         Ejemplo: 1000000.25 = 1.000.000,25
	 */
	public static String formatearNumero(double numero) {
		Locale esLocale = new Locale("es", "CO");
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(esLocale);
		df.applyPattern("#,##0.00");
		return df.format(numero);
	}
}
