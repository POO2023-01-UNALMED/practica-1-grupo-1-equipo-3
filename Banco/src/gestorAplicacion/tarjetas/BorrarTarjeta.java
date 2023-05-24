package gestorAplicacion.tarjetas;

public interface BorrarTarjeta {
    int erroresMax = 3;
    public String borrar();
    public int getErroresActuales();
    public void setErroresActuales(int erroresActuales);
    public boolean tarjetaABorrar();
    public void anadirError();
}
