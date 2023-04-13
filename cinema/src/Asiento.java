
public class Asiento {
	private boolean disponibilidad;
	private int noSilla;
	public Asiento(int noSilla) {
		this.disponibilidad = true;
		this.noSilla = noSilla;
	}
	public void setDisponibilidad(boolean d) {
		this.disponibilidad = d;
	}
	public boolean isDisponible() {
		return disponibilidad;
	}
	public int noSilla() {
		return noSilla;
	}
}
