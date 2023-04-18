package Cinema.src;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class cinema {
	public static void main(String[] args) throws ParseException {
		Cliente cliente = new Cliente("carlos@gmail.com", "1234", "Carlos Guarin", 123456, 20);
		Cliente cliente2 = new Cliente("carlosasdf@gmail.com", "1234", "Carlos Guarin", 1234567, 20);
		Cliente cliente3 = new Cliente("carlos1@gmail.com", "1234", "Carlos Guarin", 123456, 20);
		Cliente cliente4 = new Cliente("carlosasdf@gmail.com", "1234", "Carlos Guarin", 12345678, 20);
		Cliente.registrarse(cliente);
		Cliente.registrarse(cliente2);
		Cliente.registrarse(cliente3);
		Cliente.registrarse(cliente4);

		// Iniciar sesión con credenciales correctas
		cliente.iniciarSesion("carlos@gmail.com", "1234"); // debería imprimir "Bienvenido, Carlos Guarin!"

		// Iniciar sesión con credenciales incorrectas
		cliente.iniciarSesion("juan@gmail.com", "1234"); // debería imprimir "Correo electrónico o contraseña incorrectos."

		Administrador.administradores.add(new Administrador("admin1@gmail.com", "password1", "Admin 1", 111111));
		Administrador.administradores.add(new Administrador("admin2@gmail.com", "password2", "Admin 2", 222222));
		Administrador.administradores.add(new Administrador("admin3@gmail.com", "password3", "Admin 3", 333333));

		System.out.println(Cliente.getClientes());
		System.out.println(Administrador.administradores);

		// crear una nueva función
		Pelicula pelicula = new Pelicula(60, "peli", "yo", "Genero", 0);
		String fechaString = "2022-04-15 20:00";
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date fecha = formato.parse(fechaString);
		Sala sala = new Sala(1, 13, 13);
		Funcion funcion = new Funcion(pelicula, fecha, sala, "2D");
		Funcion.getFunciones().add(funcion);
		System.out.println(Funcion.getFunciones());
		Sala.asientosDisponibles(sala);

		Reserva.reservar(cliente, funcion, 169);
		Reserva.reservar(cliente, funcion, 10);
		Reserva.reservar(cliente2, funcion, 1);
		System.out.println(Reserva.getReservas());
		Sala.asientosDisponibles(sala);

		System.out.println(Reserva.clienteYaReservo(cliente));
		//Creacion del input
		Scanner input = new Scanner(System.in);

		//Creacion de 5 salas
		Sala sala1 = new Sala(4, 8, 10);
		Sala sala2 = new Sala(2, 10, 10);
		Sala sala4 = new Sala(1, 10, 13);
		Sala sala3 = new Sala(3, 12, 12);
		Sala sala5 = new Sala(5, 15, 15);

		//Creacion de 5 peliculas
		Pelicula Super_Mario_Bros = new Pelicula(92,"Super Mario Bros", "Aaron Horvath, Michael Jelenic", "Acción, Comedia", 0);
		Pelicula John_Wick_4 = new Pelicula(169, "John Wick 4", "Chad Stahelski","Acción, Crimen, Suspenso", 12 );
		Pelicula Evil_Dead_El_Despertar = new Pelicula(96, "Evil Dead: El Despertar", "Lee Cronin", "Terror", 18);
		Pelicula El_Exorcista_del_Papa = new Pelicula(105, "El Exorcista del Papa", "Julius Avery", "Terror, Thriller", 15);
		Pelicula Dragones_Honor_entre_ladrones = new Pelicula(134, "Dragones: Honor entre ladrones", "John Francis Daley, Jonathan Goldstein", "Acción, Aventura, Drama", 7);

		//Funciones
		Funcion funcion1 = new Funcion(Super_Mario_Bros, new Date(123, Calendar.APRIL, 25, 8, 0), sala1, "2D");
		Funcion funcion2 = new Funcion(Super_Mario_Bros, new Date(123, Calendar.APRIL, 25, 8, 0), sala2, "3D");
		Funcion funcion3= new Funcion(Super_Mario_Bros, new Date(123, Calendar.APRIL, 25, 8, 0), sala3, "4D");
		Funcion funcion4 = new Funcion(Super_Mario_Bros, new Date(123, Calendar.APRIL, 25, 8, 0), sala5, "IMAX");

		Funcion funcion5 = new Funcion(John_Wick_4,new Date(123, Calendar.APRIL, 25, 10, 0),sala1, "2D");
		Funcion funcion6 = new Funcion(John_Wick_4,new Date(123, Calendar.APRIL, 25, 10, 0),sala2, "3D");
		Funcion funcion7 = new Funcion(John_Wick_4,new Date(123, Calendar.APRIL, 25, 10, 0),sala4, "4D");
		Funcion funcion8 = new Funcion(John_Wick_4,new Date(123, Calendar.APRIL, 25, 10, 0),sala5, "IMAX");

		Funcion funcion9 = new Funcion(Evil_Dead_El_Despertar,new Date(123, Calendar.APRIL, 25, 13, 0),sala1, "2D");
		Funcion funcion10 = new Funcion(Evil_Dead_El_Despertar,new Date(123, Calendar.APRIL, 25, 13, 0),sala3, "3D");
		Funcion funcion11 = new Funcion(Evil_Dead_El_Despertar,new Date(123, Calendar.APRIL, 25, 13, 0),sala4, "4D");
		Funcion funcion12 = new Funcion(Evil_Dead_El_Despertar,new Date(123, Calendar.APRIL, 25, 13, 0),sala5, "IMAX");

		Funcion funcion13 = new Funcion(El_Exorcista_del_Papa,new Date(123, Calendar.APRIL, 25, 15, 0),sala1, "2D");
		Funcion funcion14 = new Funcion(El_Exorcista_del_Papa,new Date(123, Calendar.APRIL, 25, 15, 0),sala3, "3D");
		Funcion funcion15 = new Funcion(El_Exorcista_del_Papa,new Date(123, Calendar.APRIL, 25, 15, 0),sala4, "4D");
		Funcion funcion16 = new Funcion(El_Exorcista_del_Papa,new Date(123, Calendar.APRIL, 25, 15, 0),sala5, "IMAX");

		Funcion funcion17 = new Funcion(El_Exorcista_del_Papa,new Date(123, Calendar.APRIL, 25, 17, 0),sala1, "2D");
		Funcion funcion18 = new Funcion(El_Exorcista_del_Papa,new Date(123, Calendar.APRIL, 25, 17, 0),sala2, "3D");
		Funcion funcion19 = new Funcion(El_Exorcista_del_Papa,new Date(123, Calendar.APRIL, 25, 17, 0),sala3, "4D");
		Funcion funcion20 = new Funcion(El_Exorcista_del_Papa,new Date(123, Calendar.APRIL, 25, 17, 0),sala4, "IMAX");

		Funcion funcion21 = new Funcion(Dragones_Honor_entre_ladrones,new Date(123, Calendar.APRIL, 25, 19, 0),sala1, "2D");
		Funcion funcion22 = new Funcion(Dragones_Honor_entre_ladrones,new Date(123, Calendar.APRIL, 25, 19, 0),sala1, "3D");
		Funcion funcion23 = new Funcion(Dragones_Honor_entre_ladrones,new Date(123, Calendar.APRIL, 25, 19, 0),sala1, "4D");
		Funcion funcion24 = new Funcion(Dragones_Honor_entre_ladrones,new Date(123, Calendar.APRIL, 25, 19, 0),sala1, "IMAX");


	}
}
