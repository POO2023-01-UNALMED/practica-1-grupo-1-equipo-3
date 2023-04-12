public class Pelicula {
    private int duracion;
    private String nombre;
    private String director;
    private String genero;
    private String clasificacion;
    private int id;
    private String formato;

    public Pelicula(int duracion, String nombre, String director, String genero, String clasificacion, int id, String formato) {
        this.duracion = duracion;
        this.nombre = nombre;
        this.director = director;
        this.genero = genero;
        this.clasificacion = clasificacion;
        this.id = id;
        this.formato = formato;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }
}
