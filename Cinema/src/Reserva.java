package Cinema.src;
import java.util.ArrayList;

public class Reserva {
	private Cliente cliente;
	private Funcion funcion;
	private Asiento asiento;
	private Sala sala;
	static ArrayList<Reserva>  reservas = new ArrayList<Reserva>();
	public Reserva(Cliente cliente, Funcion funcion, Asiento asiento, Sala sala) {
		this.cliente = cliente;
		this.funcion = funcion;
		this.asiento = asiento;
		this.sala = sala;
	}
}

