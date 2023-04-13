package Cinema.src;
public class Pelicula {
	private int duracion;
	private String nombre;
	private String director;
	private String genero;
	private String clasificacion;
	private String formato;
	public Pelicula(int duracion, String nombre, String director, String genero, String clasificacion, String formato) {
		this.duracion = duracion;
		this.nombre = nombre;
		this.director = director;
		this.genero = genero;
		this.clasificacion = clasificacion;
		this.formato = formato;
	}
	public String getNombre() {
		return nombre;
	}
}
