package src.gestorAplicacion.cine;
import java.util.Objects;

public class Asiento {
	private String tipo;
	private Boolean disponibilidad;
	private String noSilla;
	private int precio;
	public Asiento(String tipo, String noSilla) {
		this.tipo = tipo;
		this.disponibilidad = true;
		this.noSilla = noSilla;
		if (Objects.equals(this.tipo, "general")){
			this.precio = 14000;
		} else {
			this.precio = 20000;
		}
	}

	public Boolean getDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(Boolean disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	public String getNoSilla(){
		return noSilla;
	}
}
