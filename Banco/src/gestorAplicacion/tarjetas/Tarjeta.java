//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.tarjetas;

import java.io.Serializable;
import java.util.ArrayList;

import gestorAplicacion.entidades_de_negocio.*;

public abstract class Tarjeta implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected int noTarjeta;
	protected Divisa divisa;
	protected String estado;
	protected int transaccionesRechazadas;
	protected static ArrayList<Tarjeta> tarjetas = new ArrayList<>();
	
	public Tarjeta(int noTarjeta, Divisa divisa) {
		this.noTarjeta = noTarjeta;
		this.divisa = divisa;
		estado = "ACTIVA";
		transaccionesRechazadas = 0;
		tarjetas.add(this);
	}
	
	//Metodos de las instancias
	public int getNoTarjeta() {
		return noTarjeta;
	}
	
	public Divisa getDivisa() {
		return divisa;
	}
	
	public int getTransaccionesRechazadas() {
		return transaccionesRechazadas;
	}
	
	public void anadirTransaccionRechazada() {
		if (transaccionesRechazadas >= 3) 
			estado = "BLOQUEADA";
		transaccionesRechazadas++;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public boolean isActiva() {
		return getEstado().equalsIgnoreCase("ACTIVA");
	}
	
	public static ArrayList<Tarjeta> getTarjetas(){
		return tarjetas;
	}
	
	//Metodos asbtractos
	
	//Función que se encarga de hacer transacciones. Si la transacción es exitosa, devuelve verdadero
	public abstract boolean transaccion(double cantidad, TarjetaDebito t);

	public abstract boolean deshacerTransaccion(double cantidad, Tarjeta t); //Sirve para deshacer una transacción a un cliente determinado
	
	//Determina si una clase tiene fondos (en tarjetas debito) o si aun tiene credito (en tarjetas debito)
	public abstract boolean tieneSaldo();
	
	//Determina si la tarjeta tiene los fondos necesarios para cubrir una transaccion
	public abstract boolean puedeTransferir(double monto); 
	
	public abstract void sacarDinero(double monto);
	public abstract void introducirDinero(double monto);

	//Metodos de la clase
	public static ArrayList<Tarjeta> TarjetasBloqueadas(Cliente cliente){
		ArrayList<Tarjeta> retorno = new ArrayList<>();
		for(Tarjeta t : cliente.getTarjetasCredito()){
			if(!t.isActiva()){
				retorno.add(t);
			}
		}
		for(Tarjeta t : cliente.getTarjetasDebito()){
			if(!t.isActiva()){
				retorno.add(t);
			}
		}
		return retorno;
	}
	
	public static ArrayList<Tarjeta> TarjetasNoBloqueadas(Cliente cliente){
		ArrayList<Tarjeta> retorno = new ArrayList<>();
		for(Tarjeta t : cliente.getTarjetasCredito()){
			if(t.isActiva()){
				retorno.add(t);
			}
		}
		for(Tarjeta t : cliente.getTarjetasDebito()){
			if(t.isActiva()){
				retorno.add(t);
			}
		}
		return retorno;
	}

}
