package Cinema.src;

import java.util.Arrays;

public class Sala {
	private Funcion funcion;
	private int noDeSala;
	private Asiento[] asientos;

	private final int noFilas;
	private final int noColumnas;

	public Sala(Funcion funcion, int noDeSala, int noFilas, int noColumnas) {
		this.funcion = funcion;
		this.noDeSala = noDeSala;
		this.noFilas = noFilas;
		this.noColumnas = noColumnas;
		this.asientos = new Asiento[noFilas*noColumnas]; // Total de asientos en la sala
		int numeroSilla = 1;
		for (int i = 0; i < this.noColumnas; i++) { // recorre por columna
			for (int j = 0; j < this.noFilas; j++) { // recorre por fila
				String tipo;
				if (j < 10) { // filas A-J son generales
					tipo = "general";
				} else { // filas K-O son preferenciales
					tipo = "preferencial";
				}
				String letra = Character.toString((char)('A' + i)); // obtiene la letra correspondiente a la columna
				asientos[i*this.getNoFilas() + j] = new Asiento(tipo, letra+numeroSilla); // crea el objeto Asiento con los valores correspondientes
				numeroSilla++; // aumenta el número de silla
			}
			numeroSilla = 1; // reinicia el número de silla al final de cada columna
		}
	}

	public void asientosDisponibles(Sala sala){
		for (int i = 0; i < sala.getAsientos().length; i++) {
			if (i % sala.getNoFilas() == 0 && i > 0) { // imprimir una nueva línea después de cada fila completa de asientos
				System.out.println();
			}
			if (sala.getAsientos()[i].getDisponibilidad()){
				System.out.print(sala.getAsientos()[i].getNoSilla() + " ");
			}
		}
		System.out.println();
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

	public int getNoFilas() {
		return noFilas;
	}

	public int getNoColumnas() {
		return noColumnas;
	}


}