package Banco;

import java.util.ArrayList;


public class Divisa {
	private String moneda;
	private double valor;
	private static Divisa[] divisas = {new Divisa("dolar", 1), new Divisa("euro", 1.11), new Divisa("peso colombiano", 0.00022), new Divisa("yen japonés", 0.0075), new Divisa("rublo ruso", 0.012)};

	
	public Divisa(String moneda, double valor) {
		this.moneda = moneda;
		this.valor = valor;
	}
	
	public String getMoneda() {
		return moneda;
	}
	public double getValor() {
		return valor;
	}
	public static Divisa[] getDivisas() {
		return divisas;
	}

}
