package Cinema.src;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Reserva {
	private Cliente cliente;
	private Funcion funcion;
	private Asiento asiento;
	private int numeroSala;
	private Pelicula pelicula;
	private Date fechaFuncion;
	private static HashMap<Integer, Reserva> reservas = new HashMap<>();
	private static int ultimaReserva = 0;
	private int id;

	public Reserva(Cliente cliente, Funcion funcion, Asiento asiento) {
		this.cliente = cliente;
		this.funcion = funcion;
		this.asiento = asiento;
		this.numeroSala = funcion.getSala().getNoDeSala();
		this.pelicula = funcion.getPelicula();
		this.fechaFuncion = funcion.getMomento();
		this.id = ultimaReserva++;
	}

	public static void reservar(Cliente cliente, Funcion funcion, int numeroDeSilla){
		funcion.getSala().getAsientos()[numeroDeSilla-1].setDisponibilidad(false);
		Reserva reserva = new Reserva(cliente, funcion, funcion.getSala().getAsientos()[numeroDeSilla-1]);
		reservas.put(reserva.getId(), reserva);
	}

	public static List<Reserva> clienteYaReservo(Cliente cliente) {
		List<Reserva> reservasCliente = new ArrayList<>();
		for (Reserva reserva : reservas.values()) {
			if (reserva.getCliente().equals(cliente)) {
				reservasCliente.add(reserva);
			}
		}
		return reservasCliente;
	}



	public static HashMap<Integer, Reserva> getReservas() {
		return reservas;
	}

	public int getId() {
		return id;
	}

	public Cliente getCliente() {
		return cliente;
	}
}