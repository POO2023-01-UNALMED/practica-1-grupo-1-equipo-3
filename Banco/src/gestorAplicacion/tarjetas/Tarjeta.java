package gestorAplicacion.tarjetas;
import java.util.ArrayList;

import gestorAplicacion.entidades_de_negocio.Divisa;



public abstract class Tarjeta {
	private int noTarjeta;
	private Divisa divisa;
	private int transaccionesRechazadas;
	private static ArrayList<Tarjeta> tarjetas = new ArrayList<Tarjeta>();
	
	public Tarjeta(int noTarjeta, Divisa divisa) {
		this.noTarjeta = noTarjeta;
		this.divisa = divisa;
		transaccionesRechazadas = 0;
		tarjetas.add(this);
	}
	
	public int getNoTarjeta() {
		return noTarjeta;
	}
	
	public Divisa getDivisa() {
		return divisa;
	}
	public static ArrayList<Tarjeta> getTarjetas(){
		return tarjetas;
	}
	
	public abstract boolean transaccion(double cantidad, TarjetaDebito t); //Función que se encarga de hacer transacciones: Si la transacción es exitosa, devuelve verdadero
	public abstract boolean poderTransferir(double monto); //Función que determina si la tarjeta puede o no puede transferir cierta cantidad de dinero
	public abstract void sacarDinero(double monto);
}
