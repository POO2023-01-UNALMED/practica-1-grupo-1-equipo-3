package gestorAplicacion.infraestructura;

import java.util.Scanner;

import gestorAplicacion.entidades_de_negocio.Cliente;
import gestorAplicacion.tarjetas.Tarjeta;
import gestorAplicacion.tarjetas.TarjetaDebito;

import java.util.ArrayList;


public class Banco {
	public static void main(String[] args) {
	}
	
	
	
	public static ArrayList<Cliente> otrosUsuarios(Cliente usuario) {
		ArrayList<Cliente> otrosUsuarios = new ArrayList<Cliente>(); //Usa esta lista para guardar todos los clientes que no sean el que llama la función (usuario)
		for(Cliente c: Cliente.getClientes()) {
			if(c != usuario) {
				otrosUsuarios.add(c);
			}
		}

		return otrosUsuarios;
	}
	public static ArrayList<TarjetaDebito> tarjetasDebito(Cliente c) {
		ArrayList<TarjetaDebito> tarjetas = new ArrayList<TarjetaDebito>(); //Usa esta lista para guardar todas las tarjetas de débito
		for(Tarjeta t: c.getTarjetas()) {
			if(t instanceof TarjetaDebito) {
				tarjetas.add(((TarjetaDebito)t));
			}
		}
		return tarjetas;
	}
	
	public static boolean numeroExistente(int num) { // Evalua si un número de tarjeta corresponde a algúna tarjeta de algún cliente
		boolean valor = false;
		for(Tarjeta t : Tarjeta.getTarjetas()) {
			if(num == t.getNoTarjeta()) {
				valor = true;
			}
		}
		return valor;
	}

}
