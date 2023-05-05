package gestorAplicacion.infraestructura;

import java.util.Scanner;

import gestorAplicacion.entidades_de_negocio.*;
import gestorAplicacion.tarjetas.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class Banco {
	
	private static ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private static ArrayList<Canal> canales = new ArrayList<Canal>();
	
	//Metodos de la clase
	
	public static ArrayList<Canal> ordenarCanalesPorImpuestos(List<Canal> canales) {//De menor a mayor
	    Collections.sort(canales, new Comparator<Canal>() {
	        public int compare(Canal canal1, Canal canal2) {
	            return Double.compare(canal1.getImpuesto(), canal2.getImpuesto());
	        }
	    });
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
		for(Cliente cliente: clientes) {
			Banco.clientes.add(cliente);
		}
	}
	
	public static void agregarClientes(Cliente... clientes) {//Agregar varios clientes de un array
		for(Cliente cliente: clientes) {
			Banco.clientes.add(cliente);
		}
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
		for(Canal canal: canales) {
			Banco.canales.add(canal);
		}
	}
	
	public static void agregarCanales(ArrayList<Canal> canales) {//Agregar varios canales de un arrayList
		for(Canal canal: canales) {
			Banco.canales.add(canal);
		}
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
	
	
//	public static ArrayList<Cliente> otrosUsuarios(Cliente usuario) {
//		ArrayList<Cliente> otrosUsuarios = new ArrayList<Cliente>(); //Usa esta lista para guardar todos los clientes que no sean el que llama la función (usuario)
//		for(Cliente c: Cliente.getClientes()) {
//			if(c != usuario) {
//				otrosUsuarios.add(c);
//			}
//		}
//
//		return otrosUsuarios;
//	}
//	public static ArrayList<TarjetaDebito> tarjetasDebito(Cliente c) {
//		ArrayList<TarjetaDebito> tarjetas = new ArrayList<TarjetaDebito>(); //Usa esta lista para guardar todas las tarjetas de débito
//		for(Tarjeta t: c.getTarjetas()) {
//			if(t instanceof TarjetaDebito) {
//				tarjetas.add(((TarjetaDebito)t));
//			}
//		}
//		return tarjetas;
//	}
//	
//	public static boolean numeroExistente(int num) { // Evalua si un número de tarjeta corresponde a algúna tarjeta de algún cliente
//		boolean valor = false;
//		for(Tarjeta t : Tarjeta.getTarjetas()) {
//			if(num == t.getNoTarjeta()) {
//				valor = true;
//			}
//		}
//		return valor;
//	}

}
