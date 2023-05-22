//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.infraestructura;

import gestorAplicacion.entidades_de_negocio.*;
import gestorAplicacion.tarjetas.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class Banco {
	
	private static ArrayList<Cliente> clientes = new ArrayList<>();
	private static ArrayList<Canal> canales = new ArrayList<>();
	
	//Metodos de la clase
	
	public static ArrayList<Canal> ordenarCanalesPorImpuestos(List<Canal> canales) {//De menor a mayor
	    canales.sort(Comparator.comparingDouble(Canal::getImpuesto));
	    return new ArrayList<>(canales);
	}
	
	//Getters & Setters
	/*Los getters y los setters contienen metodos que permiten, por ejemplo
	 * Eliminar un solo usuario del arrayList o eliminar varios (metodos separados)
	 */
	public static ArrayList<Cliente> getClientes() {
		return clientes;
	}
	
	public static void agregarCliente(Cliente cliente) {//Agregar un solo cliente
		Banco.clientes.add(cliente);
	}
	
	
	public static void agregarClientes(ArrayList<Cliente> clientes) {//Agregar varios clientes de un arrayList
		Banco.clientes.addAll(clientes);
	}
	
	public static void agregarClientes(Cliente... clientes) {//Agregar varios clientes de un array
		Collections.addAll(Banco.clientes, clientes);
	}
	
	public static void eliminarCliente(Cliente cliente) {//Eliminar un solo cliente
		Banco.clientes.remove(cliente);
	}
	
	public static void eliminarClientes(ArrayList<Cliente> clientes) {//Eliminar varios clientes de un ArrayList
		for(Cliente clientePorEliminar: clientes) {
			Banco.clientes.remove(clientePorEliminar);
		}			
	}
	
	public static void eliminarClientes(Cliente[] clientes) {//Eliminar varios clientes de un Array normal
		for(Cliente clientePorEliminar: clientes) {
			Banco.clientes.remove(clientePorEliminar);
		}			
	}
	
	public static ArrayList<Canal> getCanales() {
		return canales;
	}
	
	public static void agregarCanal(Canal canal) {//Agregar un solo canal
		Banco.canales.add(canal);
	}
	
	
	public static void agregarCanales(Canal... canales) {//Agregar varios canales de un array normal
		Collections.addAll(Banco.canales, canales);
	}
	
	public static void agregarCanales(ArrayList<Canal> canales) {//Agregar varios canales de un arrayList
		Banco.canales.addAll(canales);
	}
	
	
	public static void eliminarCanal(Canal canal) {
		Banco.canales.remove(canal);
	}
	
	public static void eliminarCanales(Canal... canales) {//Eliminar varios canales de un Array normal
		for(Canal canalPorEliminar: canales) {
			Banco.canales.remove(canalPorEliminar);
		}		
	}
	
	public static void eliminarCanales(ArrayList<Canal> canales) {//Eliminar varios canales de un ArrayList
		for(Canal canalPorEliminar: canales) {
			Banco.canales.remove(canalPorEliminar);
		}			
	}
	
	public static int calcularPuntaje(ArrayList<Transaccion> trans){
		int puntaje = 0;
		for(Transaccion t : trans){
			puntaje += 2 *t.getCantidad()*t.getDivisa().getValor();
		}
		return puntaje;
	}
	
	public static boolean numeroExistente(int num) { // Evalua si un número de tarjeta corresponde a algúna tarjeta de algún cliente
		boolean valor = false;
		for(Tarjeta t : Tarjeta.getTarjetas()) {
			if (num == t.getNoTarjeta()) {
				valor = true;
				break;
			}
		}
		return valor;
	}

	public static Transaccion generarPeticion(Transaccion transaccion, String mensaje){
		transaccion.setRetornable(false);
		return new Transaccion(transaccion, mensaje);
	}
	
	/**
	 * Esta funcion es con el fin de usarse en los toString y las impresiones en consola
	 * @param numero a formatear
	 * @return el numero formateado de acuerdo a las reglas de formato de Colombia. 
	 * Ejm: 1000000.25 = 1.000.000,25
	 * */
	public static String formatearNumero(double numero) {
		Locale esLocale = new Locale("es", "CO");
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(esLocale);
        df.applyPattern("#,##0.00");
        return df.format(numero);
    }
}
