package Cinema.src;
import java.util.ArrayList;
import java.util.Date;


public class Funcion {
	private Pelicula pelicula;
	private Date momento;
	private double multiplicadorTipoPelicula;
	private double multiplicadorHoraFuncion;
	static ArrayList<Funcion> funciones = new ArrayList<Funcion>();
	
	public Funcion(Pelicula pelicula, Date momento) {
		this.pelicula = pelicula;
		this.momento = momento;
	}
}
