package src.gestorAplicacion.cine;

import java.util.Arrays;
import java.util.Objects;

public class Sala {
	private Funcion funcion;
	private int noDeSala;
	private Asiento[] asientos;

	private final int noFilas;
	private final int noColumnas;
	private String formato = "2D";

	public Sala(int noDeSala, int noFilas, int noColumnas) {
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

	public int getNoDeSala() {
		return noDeSala;
	}

	public Asiento[] getAsientos() {
		return asientos;
	}

	public int getNoFilas() {
		return noFilas;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getFormato() {
		return formato;
	}
}