public class Funcion {
    private Pelicula pelicula;
    private String hora;
    private double multiplicadorTipoPelicula;
    private double multiplicadorHoraFuncion;

    public Funcion(Pelicula pelicula, String fechaYHora){
        this.pelicula = pelicula;
        this.hora = fechaYHora;
    }

    public double getMultiplicadorTipoPelicula() {
        return multiplicadorTipoPelicula;
    }

    public void setMultiplicadorTipoPelicula(double multiplicadorTipoPelicula) {
        this.multiplicadorTipoPelicula = multiplicadorTipoPelicula;
    }

    public double getMultiplicadorHoraFuncion() {
        return multiplicadorHoraFuncion;
    }

    public void setMultiplicadorHoraFuncion(double multiplicadorHoraFuncion) {
        this.multiplicadorHoraFuncion = multiplicadorHoraFuncion;
    }
}