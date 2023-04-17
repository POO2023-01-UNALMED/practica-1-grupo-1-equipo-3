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
		Pelicula pelicula = new Pelicula(60, "peli", "yo", "Genero", "clasificación", "formato");
		String fechaString = "2022-04-15 20:00";
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date fecha = formato.parse(fechaString);
		Sala sala = new Sala(1, 13, 13);
		Funcion funcion = new Funcion(pelicula, fecha, sala);
		Funcion.funciones.add(funcion);
		System.out.println(Funcion.funciones);
		Sala.asientosDisponibles(sala);

		Reserva.reservar(cliente, funcion, 169);
		Reserva.reservar(cliente, funcion, 10);
		Reserva.reservar(cliente2, funcion, 1);
		System.out.println(Reserva.getReservas());
		Sala.asientosDisponibles(sala);

		System.out.println(Reserva.clienteYaReservo(cliente));
	}
}
