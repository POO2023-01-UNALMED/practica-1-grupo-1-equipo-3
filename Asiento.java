public class Asiento {
    private String tipo;
    private Boolean disponibilidad;
    private String noSilla;

    public Asiento(String tipo, Boolean disponibilidad, String noSilla) {
        this.tipo = tipo;
        this.disponibilidad = disponibilidad;
        this.noSilla = noSilla;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getNoSilla() {
        return noSilla;
    }

    public void setNoSilla(String noSilla) {
        this.noSilla = noSilla;
    }

    public int getGeneral() {
        return 20000;
    }

    public int getPreferencial() {
        return 14000;
    }
}
