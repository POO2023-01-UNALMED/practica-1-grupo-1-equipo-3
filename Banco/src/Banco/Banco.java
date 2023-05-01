package Banco;

import java.util.Scanner;
import java.util.ArrayList;


public class Banco {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Cliente cliente1 = new Cliente("Dario", 111);
		Cliente cliente2 = new Cliente("Esteban", 222);
		Cliente cliente3 = new Cliente("Marta", 333);
		
		while(true) {
			System.out.println("Elija el usuario:");
			for(Cliente c : Cliente.getClientes()) {
				System.out.println(Cliente.getClientes().indexOf(c)+1 + ". " + c.getNombre());
			}
			System.out.println(Cliente.getClientes().size()+1 + ". Para salir");
			int respuesta1 = scanner.nextInt();
			scanner.nextLine();
			if(respuesta1 == Cliente.getClientes().size() + 1) {
				break;
			}
			Cliente.getClientes().get(respuesta1-1).sesion();
		}
		
		scanner.close();
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
