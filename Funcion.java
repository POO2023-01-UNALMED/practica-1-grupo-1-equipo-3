import java.util.Date;

public class Funcion {
    private Pelicula pelicula;
    private Date hora;
    private double multiplicadorTipoPelicula;
    private double multiplicadorHoraFuncion;

    public Funcion(Pelicula pelicula, Date hora, double multiplicadorTipoPelicula, double multiplicadorHoraFuncion) {
        this.pelicula = pelicula;
        this.hora = hora;
        this.multiplicadorTipoPelicula = multiplicadorTipoPelicula;
        this.multiplicadorHoraFuncion = multiplicadorHoraFuncion;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
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
