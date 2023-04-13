import java.util.ArrayList;


public class Funcion {
	private Pelicula pelicula;
	private DateTime momento;
	private int precio;
	private Sala sala;
	static ArrayList<Funcion> funciones = new ArrayList<Funcion>();
	
	public Funcion(Pelicula pelicula, DateTime momento, Sala sala, int precio) {
		this.pelicula = pelicula;
		this.momento = momento;
		this.sala = sala;
		this.precio = precio;
	}
	
	public String toString() {
		return "El día "+getMomento().dia() + " a las "+getMomento().tiempo() + " tenemos la película " + pelicula.getNombre();
	}
	
	public Pelicula getPelicula() {
		return pelicula;
	}
	public DateTime getMomento() {
		return momento;
	}
	public int getPrecio() {
		return precio;
	}
	public Sala getSala() {
		return sala;
	}
}
