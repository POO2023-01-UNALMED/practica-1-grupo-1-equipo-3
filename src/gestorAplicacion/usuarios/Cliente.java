package src.gestorAplicacion.usuarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import src.gestorAplicacion.cine.Funcion;
import src.gestorAplicacion.cine.Sala;

public class Cliente extends Usuario {
	private int edad;
	private static List<Cliente> clientes = new ArrayList<>();

	Scanner input = new Scanner(System.in);

	public Cliente(String gmail, String contrasena, String nombre, long noDoc, int edad) {
		super(gmail, contrasena, nombre, noDoc);
		this.edad = edad;
	}

	public static void registrarse(Cliente cliente){
		for (Cliente c: Cliente.clientes) {
			if(Objects.equals(c.Gmail, cliente.Gmail)){
				System.out.println("El correo electrónico " + c.Gmail + " ya está registrado.");
				return;
			}
			if(c.NoDoc==cliente.NoDoc){
				System.out.println("El numero de documento " + c.NoDoc + " ya está registrado.");
				return;
			}
		}
		Cliente.clientes.add(cliente);
		Usuario.getUsuarios().add(cliente);
		System.out.println("Se ha registrado exitosamente");
	}



	public static List<Cliente> getClientes() {
		return clientes;
	}
	public void interfazUsuario(){
		//Aquí va lo que el usuario ve al iniciar sessión
		while(true){
			System.out.println("1. Para ver las funciones y hacer reservas\n2. Para ver las reservas sin pagar\n3. Para salir");
			int eleccion1 = input.nextInt();
			input.nextLine();

			if(eleccion1 == 1){
				System.out.println(Funcion.verFunciones());
				System.out.println(Funcion.getFunciones().size()+2 + ". Para volver\n");

				int eleccion2 = input.nextInt();
				input.nextLine();
				if(eleccion2 <= Funcion.getFunciones().size() +1){
					System.out.println("Los asientos disponibles para esta función son:");
					Sala.asientosDisponibles(Funcion.getFunciones().get(eleccion1).getSala());
					System.out.println("Escoga el asiento que quisiera reservar");
					String asiento = input.nextLine();
					input.nextInt();
				}

			} else if(eleccion1 == 2){
				break;
			} else if(eleccion1 == 3){
				break;
			}
		}
	}
}
