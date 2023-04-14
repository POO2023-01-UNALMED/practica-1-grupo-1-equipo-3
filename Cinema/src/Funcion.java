package Cinema.src;
import java.util.ArrayList;
import java.util.Date;


public class Funcion {
	private Sala sala;
	private Pelicula pelicula;
	private Date momento;
	private double multiplicadorTipoPelicula;
	private double multiplicadorHoraFuncion;
	static ArrayList<Funcion> funciones = new ArrayList<Funcion>();
	
	public Funcion(Pelicula pelicula, Date momento, Sala sala) {
		this.pelicula = pelicula;
		this.momento = momento;
		this.sala = sala;
	}

	public Sala getSala() {
		return sala;
	}

	public Pelicula getPelicula() {
		return pelicula;
	}

	public Date getMomento() {
		return momento;
	}
}
