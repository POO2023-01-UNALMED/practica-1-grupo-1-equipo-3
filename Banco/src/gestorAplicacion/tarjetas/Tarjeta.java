package gestorAplicacion.tarjetas;
import java.util.ArrayList;

import gestorAplicacion.entidades_de_negocio.Divisa;



public abstract class Tarjeta {
	private int noTarjeta;
	private Divisa divisa;
	private static ArrayList<Tarjeta> tarjetas = new ArrayList<Tarjeta>();
	
	public Tarjeta(int noTarjeta, Divisa divisa) {
		this.noTarjeta = noTarjeta;
		this.divisa = divisa;
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
	
	public abstract String verTarjeta();
	public abstract boolean transaccion(double cantidad, TarjetaDebito t); //Función que se encarga de hacer transacciones: Si la transacción es exitosa, devuelve verdadero
}
