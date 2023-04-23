package src.gestorAplicacion.cine;

public class Pelicula {
	private int duracion;
	private String nombre;
	private String director;
	private String genero;
	private int clasificacion;
	public Pelicula(int duracion, String nombre, String director, String genero, int clasificacion) {
		this.duracion = duracion;
		this.nombre = nombre;
		this.director = director;
		this.genero = genero;
		this.clasificacion = clasificacion;
	}

	public String getNombre(){
		return nombre;
	}
}
