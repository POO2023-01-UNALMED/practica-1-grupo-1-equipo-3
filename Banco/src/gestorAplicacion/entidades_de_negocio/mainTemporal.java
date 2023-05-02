package gestorAplicacion.entidades_de_negocio;

import java.util.ArrayList;
import java.util.Scanner;

import gestorAplicacion.infraestructura.Banco;

public class mainTemporal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Cliente cliente = new Cliente("Marta", 123456);
		Cliente cliente1 = new Cliente("Dario", 111);
		Cliente cliente2 = new Cliente("Esteban", 222);
		
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		clientes.add(cliente);
		clientes.add(cliente1);
		Banco.eliminarClientes(clientes);
		System.out.println(Banco.getClientes());
//		Scanner scanner = new Scanner(System.in);
//		Cliente cliente2 = new Cliente("Esteban", 222);
//		Cliente cliente3 = new Cliente("Marta", 333);
//		
//		while(true) {
//			System.out.println("Elija el usuario:");
//			for(Cliente c : Cliente.getClientes()) {
//				System.out.println(Cliente.getClientes().indexOf(c)+1 + ". " + c.getNombre());
//			}
//			System.out.println(Cliente.getClientes().size()+1 + ". Para salir");
//			int respuesta1 = scanner.nextInt();
//			scanner.nextLine();
//			if(respuesta1 == Cliente.getClientes().size() + 1) {
//				break;
//			}
//			Cliente.getClientes().get(respuesta1-1).sesion();
//		}
//		
//		scanner.close();
	}

}
