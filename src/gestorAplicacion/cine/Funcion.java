package src.gestorAplicacion.cine;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class Funcion {
	private Sala sala;
	private Pelicula pelicula;
	private Date momento;
	private int precioSinSilla = 0;
	private static ArrayList<Funcion> funciones = new ArrayList<Funcion>();

	public Funcion(Pelicula pelicula, Date momento, Sala sala) {
		this.pelicula = pelicula;
		this.momento = momento;
		this.sala = sala;
		if (Objects.equals(sala.getFormato(), "3D")){
			this.precioSinSilla += 5000;
		} else if (Objects.equals(sala.getFormato(), "4D")) {
			this.precioSinSilla += 10000;
		} else if (Objects.equals(sala.getFormato(), "IMAX")) {
			this.precioSinSilla += 7000;
		}
		int hora = momento.getHours();
		if (hora <= 10 || hora >= 20){
			this.precioSinSilla += 5000;
		}
		funciones.add(this);
	}

	public static String verFunciones(){
		String mensaje = "";
		for(Funcion f: funciones){
			SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
			mensaje += funciones.indexOf(f)+1 + ". En la sala " + f.sala.getNoDeSala() + " el día " + formatoFecha.format(f.momento) + " a las " + formatoHora.format(f.momento) + " tenemos la película " + f.pelicula.getNombre() + "\n";
		}
		return mensaje;
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

	public int getPrecioSinSilla() {
		return precioSinSilla;
	}

	public void setPrecioSinSilla(int precioSinSilla) {
		this.precioSinSilla = precioSinSilla;
	}
}