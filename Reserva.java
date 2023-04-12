public class Reserva {
    private Cliente cliente;
    private Funcion funcion;
    private Asiento asiento;
    private Sala sala;

    public Reserva(Cliente cliente, Funcion funcion, Asiento asiento, Sala sala) {
        this.cliente = cliente;
        this.funcion = funcion;
        this.asiento = asiento;
        this.sala = sala;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }
}
