public class Sala {
    private Funcion funcion;
    private int noDeSala;
    private Asiento[] asientos;

    public Sala(Funcion funcion, int noDeSala, Asiento[] asientos) {
        this.funcion = funcion;
        this.noDeSala = noDeSala;
        this.asientos = asientos;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public int getNoDeSala() {
        return noDeSala;
    }

    public void setNoDeSala(int noDeSala) {
        this.noDeSala = noDeSala;
    }

    public Asiento[] getAsientos() {
        return asientos;
    }

    public void setAsientos(Asiento[] asientos) {
        this.asientos = asientos;
    }
}
