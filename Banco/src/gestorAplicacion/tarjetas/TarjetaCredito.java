//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.tarjetas;

import gestorAplicacion.infraestructura.*;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import gestorAplicacion.entidades_de_negocio.Divisa;
import gestorAplicacion.entidades_de_negocio.Cliente;
import gestorAplicacion.entidades_de_negocio.Factura;

public class TarjetaCredito extends Tarjeta {
	private double creditoMaximo; // Es el límite de dinero que se puede prestar mediante esta tarjeta
	private double credito; //Es la cantidad de dinero que el usuario está debiendo en este momento
	private double interes;
	


	public TarjetaCredito(int noTarjeta, Divisa divisa, double creditoMaximo, Double interes) {
		super(noTarjeta, divisa);
		this.creditoMaximo = creditoMaximo;
		this.credito = 0;
		this.interes = interes;
	}
	
	public String toString() {
		return "Tipo de tarjeta: Crédito\nNúmero de tarjeta: %s\nLímite: %s %s\nCrédito: %s %s\nTaza de interés: %s\n".formatted(noTarjeta, Banco.formatearNumero(creditoMaximo), divisa.name(), Banco.formatearNumero(credito), divisa.name(), interes);
	}

	public boolean transaccion(double cantidad, TarjetaDebito t) {
		if(creditoMaximo - credito >= cantidad && t.getDivisa().equals(getDivisa())) {
			credito += cantidad;
			t.setSaldo(t.getSaldo() + cantidad);
			return true;
		} else {
			return false;
		}
	}

	public boolean deshacerTransaccion(double cantidad, Tarjeta t){
		if(creditoMaximo-credito >= cantidad && (t instanceof TarjetaDebito)) {	//Deshacer transacción en caso de que el origen de la transacción fué una tarjeta de débito
			this.credito += Math.floor(100*cantidad*t.divisa.getValor()/this.divisa.getValor())/100;
			((TarjetaDebito)t).setSaldo(((TarjetaDebito)t).getSaldo() + cantidad);
			return true;
		}else if (creditoMaximo-credito >= cantidad && t instanceof TarjetaCredito && ((TarjetaCredito)t).getEspacio() >= cantidad){ //Deshacer transacción en caso de que el origen de la transacción fué una tarjeta de crédito
			this.credito += Math.floor(100*cantidad*t.divisa.getValor()/this.divisa.getValor())/100;
			((TarjetaCredito)t).setCredito(cantidad);
			return true;
		} else {
			return false;
		}
	}

	public boolean puedeTransferir(double monto){
		return (creditoMaximo - credito) >= monto;
	}
	
	public boolean tieneSaldo() {
		return credito != creditoMaximo;
	}

	public void sacarDinero(double monto){
		if(this.credito+monto <= this.creditoMaximo){
			this.credito += monto;
		}
	}
	
	public void introducirDinero(double monto){
		if(this.credito - monto >= 0){
			this.credito -= monto;
		}
	}

	public static ArrayList<TarjetaCredito> tarjetasDisponibles(int puntaje, Divisa divisa){
		Map<Integer, Double> reqCredMax = new HashMap<Integer, Double>();
		Map<Integer, Double> reqInteres = new HashMap<Integer, Double>();
		reqCredMax.put(0, 100.0);
		reqInteres.put(0, 10.0);
		reqCredMax.put(50, 500.0);
		reqInteres.put(50, 7.0);
		reqCredMax.put(100, 1000.0);
		reqInteres.put(100, 5.0);
		reqCredMax.put(150, 2000.0);
		reqInteres.put(150, 4.0);
		reqCredMax.put(200, 3000.0);
		reqInteres.put(200, 3.0);
		ArrayList<TarjetaCredito> Tarjetas = new ArrayList<TarjetaCredito>();
		int noTarjeta;
		do {
			noTarjeta = (int) Math.floor(Math.random() * (999999999 - 100000000 + 1) + 100000000); // genera un número aleatorio entre 999999999 y 100000000. Este será el número de la tarjeta, a menos de que ya está siendo usado (lo cual es improbable)
		} while (Banco.numeroExistente(noTarjeta));
		for(int i : reqCredMax.keySet()){
			if(i > puntaje) break;
			else Tarjetas.add(new TarjetaCredito(noTarjeta, divisa, Math.floor(100*reqCredMax.get(i)/divisa.getValor())/100, reqInteres.get(i)));
		}
		return Tarjetas;
	}

	public double getEspacio(){
		return creditoMaximo - credito;
	}

	public void setCredito(double credito){
		if(credito >= 0){
			this.credito = credito;
		}
	}

	public static void anadirTarjetaCredito(TarjetaCredito tarjeta, Cliente cliente, int bono){
		cliente.getTarjetasCredito().add(tarjeta);
		cliente.setBonoActual(bono);
	}

}
