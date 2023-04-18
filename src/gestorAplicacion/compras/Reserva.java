package Cinema.src;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Reserva {
	private Cliente cliente;
	private Funcion funcion;
	private Asiento asiento;
	private int numeroSala;
	private Pelicula pelicula;
	private Date fechaFuncion;
	private static HashMap<Cliente, Reserva> reservas = new HashMap<>();

	public Reserva(Cliente cliente, Funcion funcion, Asiento asiento) {
		this.cliente = cliente;
		this.funcion = funcion;
		this.asiento = asiento;
		this.numeroSala = funcion.getSala().getNoDeSala();
		this.pelicula = funcion.getPelicula();
		this.fechaFuncion = funcion.getMomento();
	}

	public static void reservar(Cliente cliente, Funcion funcion, int numeroDeSilla){
		funcion.getSala().getAsientos()[numeroDeSilla-1].setDisponibilidad(false);
		Reserva reserva = new Reserva(cliente, funcion, funcion.getSala().getAsientos()[numeroDeSilla-1]);
		reservas.put(cliente, reserva);
	}

	public static HashMap<Cliente, Reserva> getReservas() {
		return reservas;
	}
}