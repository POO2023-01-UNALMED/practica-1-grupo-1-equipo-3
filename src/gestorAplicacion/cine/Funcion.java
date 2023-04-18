package Cinema.src;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class Funcion {
	private Sala sala;
	private Pelicula pelicula;
	private Date momento;
	private int precioSinSilla;
	private static ArrayList<Funcion> funciones = new ArrayList<Funcion>();
	private String formato;

	public Funcion(Pelicula pelicula, Date momento, Sala sala, String formato) {
		this.pelicula = pelicula;
		this.momento = momento;
		this.sala = sala;
		this.formato = formato;
		if (Objects.equals(this.formato, "3D")){
			this.precioSinSilla += 5000;
		} else if (Objects.equals(this.formato, "4D")) {
			this.precioSinSilla += 10000;
		} else if (Objects.equals(this.formato, "IMAX")) {
			this.precioSinSilla += 7000;
		}
		int hora = momento.getHours();
		if (hora < 10 || hora > 20){
			this.precioSinSilla += 5000;
		}
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

	public static ArrayList<Funcion> getFunciones() {
		return funciones;
	}
}
