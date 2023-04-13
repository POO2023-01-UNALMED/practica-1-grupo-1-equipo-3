import java.util.Scanner;
import java.util.*;

public class cinema {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		//Definiendo valores para ensallar
		Pelicula pelicula = new Pelicula(60, "peli", "yo", "Genero", "clasificación", "formato");
		DateTime mom = new DateTime(4, 4, 5, 5);
		Sala sala = new Sala(1, 10);
		Funcion.funciones.add(new Funcion(pelicula, mom, sala, 1300));

		Cliente.registrarse("usuario", "111", "Usuario Por Defecto", 0, 0);
		while(true) {
			System.out.println("Ingrese 1 para iniciar sesión, ingrese 2 para crear usuario y x para salir");
			String entrada = input.next();
			if(entrada.equals("1")) {
				System.out.println("Por favor, ingrese su correo");
				String correo = input.next();
				System.out.println("Por favor, ingrese su contraseña");
				String contrasena = input.next();
				if(Usuario.iniciarSecion(correo, contrasena) == false) {
					System.out.println("Usuario o contraseña incorrecta");
				} else {
					Usuario usuario = Usuario.encontrarUsuario(correo);	 //Esta variable guarda el usuario que está usando el sistema
					if(!usuario.admin()) {
						while(true) {
							System.out.println("Bienvenido cliente");
							System.out.println("Presione 1 para ver nuestra programación");
							System.out.println("Presione 2 para hacer una reserva");
							System.out.println("Presione 3 para verificar su reserva");
							System.out.println("Presione x para salir");
							String entra = input.next();
							if(entra.equals("1")) {
								for(int i = 0; i< Funcion.funciones.size(); i++) {
									System.out.println("Función número " + (i+1)+": " + Funcion.funciones.get(i));
								}
							}else if(entra.equals("2")) {
								System.out.println("Por favor, ingrese el numero de la funcion que quiere ver");
								int numPeli = input.nextInt()-1;
								if(numPeli>Funcion.funciones.size()) {
									System.out.println("El numero ingresado no corresponde a ninguna función");
								}else {
									System.out.println("Los asientos disponibles son los siguientes:");
									String stringAsientos = "";
									for(Asiento a : Funcion.funciones.get(numPeli).getSala().asientos) {
										if(a.isDisponible()) {
											stringAsientos += a.noSilla() + " ";
										}
									}
									System.out.println(stringAsientos);
									System.out.println("Ingrese el numero del asiento que desea reservar");
									int numAsiento = input.nextInt()-1;
									usuario.reservar(Funcion.funciones.get(numPeli), numAsiento);
								}
							}else if(entra.equals("3")) {
								
							}else if(entra.equals("x")) {
								break;
							}
						}
					}
				}
			} else if(entrada.equals("2")) {
				System.out.println("ingrese su correo");
				String correo = input.next();
				System.out.println("Ingrese su contraseña");
				String contrasena = input.next();
				System.out.println("Ingrese su nombre de usuario");
				String nombreUsuario = input.next();
				System.out.println("Ingrese su número de documento");
				int noDoc = input.nextInt();
				System.out.println("Ingrese su edad");
				int edad = input.nextInt();
				Cliente.registrarse(correo, contrasena, nombreUsuario, noDoc, edad);
			} else if (entrada.equals("x")) {
				break;
			}
		}
		input.close();
	}
}
