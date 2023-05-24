package gestorAplicacion.tarjetas;

public interface BorrarTarjeta {
    int erroresMax = 3;
    String borrar();
    int getErroresActuales();
    void setErroresActuales(int erroresActuales);
    boolean tarjetaABorrar();
    void anadirError();
}
