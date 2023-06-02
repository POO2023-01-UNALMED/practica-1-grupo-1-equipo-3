package gestorAplicacion.tarjetas;

public interface BorrarTarjeta {
    int erroresMax = 3;
    String borrar();
    void setErroresActuales(int erroresActuales);
    boolean tarjetaABorrar();
    void anadirError();
}
