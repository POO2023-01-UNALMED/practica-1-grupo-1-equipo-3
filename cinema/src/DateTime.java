
public class DateTime {
	private int mes;
	private int dia;
	private int hora;
	private int minutos;
	public DateTime(int mes, int dia, int hora, int minutos) {
		this.mes = mes;
		this.dia = dia;
		this.hora = hora;
		this.minutos = minutos;
	}
	public int getMes() {
		return mes;
	}
	public int getDia() {
		return dia;
	}
	public int gethora() {
		return hora;
	}
	public int getminutos() {
		return minutos;
	}

	public String dia() {
		return dia + "/" + mes;
	}
	public String tiempo() {
		return hora + ":" + minutos;
	}
	
}
