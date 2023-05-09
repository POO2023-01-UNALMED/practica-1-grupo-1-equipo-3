package gestorAplicacion.tarjetas;
import java.util.ArrayList;

import gestorAplicacion.entidades_de_negocio.*;



public abstract class Tarjeta {
	protected int noTarjeta;
	protected Divisa divisa;
	protected String estado;
	protected int transaccionesRechazadas;
	protected static ArrayList<Tarjeta> tarjetas = new ArrayList<Tarjeta>();
	
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
		if(!getEstado().equalsIgnoreCase("ACTIVA")) return false;
		return true;
	}
	
	public static ArrayList<Tarjeta> getTarjetas(){
		return tarjetas;
	}
	
	//Metodos asbtractos
	
	//Función que se encarga de hacer transacciones. Si la transacción es exitosa, devuelve verdadero
	public abstract boolean transaccion(double cantidad, TarjetaDebito t);
	
	//Determina si una clase tiene fondos (en tarjetas debito) o si aun tiene credito (en tarjetas debito)
	public abstract boolean tieneSaldo();
	
	//Determina si la tarjeta tiene los fondos necesarios para cubrir una transaccion
	public abstract boolean puedeTransferir(double monto); 
	
	public abstract void sacarDinero(double monto);
	public abstract void introducirDinero(double monto);

	//Metodos de la clase
	public static ArrayList<Tarjeta> TarjetasBloqueadas(Cliente cliente){
		ArrayList<Tarjeta> retorno = new ArrayList<Tarjeta>();
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
		ArrayList<Tarjeta> retorno = new ArrayList<Tarjeta>();
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
