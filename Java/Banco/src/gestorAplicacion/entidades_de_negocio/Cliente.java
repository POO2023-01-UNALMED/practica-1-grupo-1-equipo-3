//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.entidades_de_negocio;

import java.io.Serial;
import java.util.Arrays;

import gestorAplicacion.infraestructura.*;
import gestorAplicacion.tarjetas.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Cliente implements Serializable{
	@Serial
	private static final long serialVersionUID = 1L;

	public final String NOMBRE;
	private int Id;
	private ArrayList<TarjetaDebito> tarjetasDebito = new ArrayList<>();
	private ArrayList<TarjetaCredito> tarjetasCredito = new ArrayList<>();
	private ArrayList<Factura> facturas = new ArrayList<>();
	private int bonoActual = 0;
	
	public Cliente(String nombre, int noDeIdentificacion) {
		this.NOMBRE = nombre;
		this.Id = noDeIdentificacion;
		Banco.agregarCliente(this);
	}
	
	
	//Metodos de la instancia
	
	@Override
	public String toString(){
		return "\n\nNombre: %s\nIdentificacion: %s\nNúmero de Tarjetas de Debito: %s\nNúmero de Tarjetas de Credito: %s\nBono Actual: %s".formatted(NOMBRE, Id, tarjetasDebito.size(), tarjetasCredito.size(), bonoActual);
	}

	public ArrayList<TarjetaDebito>  getTarjetasDebito() {
		return tarjetasDebito;
	}

	public void agregarTarjetasDebito(TarjetaDebito... tarjetasDebito) {//Agregar varios tarjetas una por una Ej. (tarjeta1, tarjeta2, etc)
		this.tarjetasDebito.addAll(Arrays.asList(tarjetasDebito));
	}

	public ArrayList<TarjetaCredito> getTarjetasCredito() {
		return tarjetasCredito;
	}

	public ArrayList<Tarjeta> getTarjetas() {
		ArrayList<Tarjeta> retorno = new ArrayList<>();
		retorno.addAll(tarjetasDebito);
		retorno.addAll(tarjetasCredito);
		return retorno;
	}

	public void agregarTarjetasCredito(TarjetaCredito... tarjetasCredito) {//Agregar varios tarjetas una por una Ej. (tarjeta1, tarjeta2, etc)
		this.tarjetasCredito.addAll(Arrays.asList(tarjetasCredito));
	}

	public ArrayList<Tarjeta> seleccionarTarjeta(Divisa divisa, boolean retirar){
		ArrayList<Tarjeta> retorno = new ArrayList<>();
		for(TarjetaDebito t : tarjetasDebito){
			if(t.getDivisa().equals(divisa)) retorno.add(t);
		}
		if(!retirar){
			for(TarjetaCredito t : tarjetasCredito){
				if(t.getDivisa().equals(divisa)) retorno.add(t);
			}
		}
		return retorno;
	}

	public ArrayList<Factura> getFactura() {
		return facturas;
	}

	public void agregarFactura(Factura factura) {
		this.facturas.add(factura);
	}

	public int getBonoActual() {
		return bonoActual;
	}

	public void setBonoActual(int bonoActual) {
		this.bonoActual = bonoActual;
	}
	
	//Metodos de las intancias
	
	public ArrayList<Factura> listarFacturas(){//Este metodo solo retorna facturas pendientes
		ArrayList<Factura> retorno = new ArrayList<>();
		for(Factura f : this.facturas){
			if(f.isFacturaVencida() && f.isFacturaPagada()){
				retorno.add(f);
			}
		}
		for(Factura f : this.facturas){
			if(!f.isFacturaVencida() && f.isFacturaPagada()){
				retorno.add(f);
			}
		}
		return retorno;
	}
	
	//Retorna las tarjetas de credito y debito que sean compatibles con la divisa de la factura
	//Ademas que no tengan fondos en 0 o creditoMaximo alcanzado
	//La tarjeta para ser listada también debe tener un estado "Activo"
	public ArrayList<Tarjeta> listarTarjetas(Factura factura){//Listar tarjetas para pagar factura
		ArrayList<Tarjeta> tarjetasDisponibles = new ArrayList<>();
		for(TarjetaDebito tarjeta : tarjetasDebito){
			if(!tarjeta.isActiva())
				continue;
			if(!tarjeta.tieneSaldo())
				continue;
			if(tarjeta.getDivisa().equals(factura.getDIVISA())){
				tarjetasDisponibles.add(tarjeta);
			}
		}
		for(TarjetaCredito tarjeta : tarjetasCredito){
			if(!tarjeta.isActiva())
				continue;
			if(!tarjeta.tieneSaldo())
				continue;
			if(tarjeta.getDivisa().equals(factura.getDIVISA())){
				tarjetasDisponibles.add(tarjeta);
			}
		}
		return tarjetasDisponibles;
	}
	
	public ArrayList<Tarjeta> listarTarjetas(ArrayList<Divisa> divisas){//Listar tarjetas para cambiar divisas. Recibe un arrayList
		Divisa divisaOrigen = divisas.get(0);
		Divisa divisaDestino = divisas.get(1);
		
		ArrayList<Tarjeta> tarjetasDisponibles = new ArrayList<>();
		
		for(TarjetaDebito tarjeta : tarjetasDebito){
			if(!tarjeta.isActiva())
				continue;
			if(!tarjeta.tieneSaldo())
				continue;
			if(tarjeta.getDivisa().equals(divisaOrigen) || tarjeta.getDivisa().equals(divisaDestino)){
				tarjetasDisponibles.add(tarjeta);
			}
		}
		
		for(TarjetaCredito tarjeta : tarjetasCredito){
			if(!tarjeta.isActiva())
				continue;
			if(!tarjeta.tieneSaldo())
				continue;
			if(tarjeta.getDivisa().equals(divisaOrigen) || tarjeta.getDivisa().equals(divisaDestino)){
				tarjetasDisponibles.add(tarjeta);
			}
		}
		
		return tarjetasDisponibles;
	}
	
	//El usuario debe tener tarjetas con la divisa de origen que escoja
	//Dichas tarjetas deben tener saldo o credito, y estar activas
	public ArrayList<Divisa> escogerDivisas(Divisa origen, Divisa destino) {//Retorna un ArrayList
		ArrayList<Divisa> divisas = new ArrayList<>();
		divisas.add(origen);
		divisas.add(destino);
		
		//Verificar que el cliente tenga una tarjeta de origen con la divisa que escogió
		for(TarjetaDebito tarjetaDebito: this.getTarjetasDebito()) {
			if(!tarjetaDebito.isActiva())
				continue;
			if(!tarjetaDebito.tieneSaldo())
				continue;
			if(!tarjetaDebito.getDivisa().equals(origen))
				continue;
			//Si existe al menos una tarjeta que cumpla con lo requerido, retornamos.
			return divisas;
		}
		
		//Si no existen aun tarjetas con dicha divisa (ya que no se retornó) se recorren las tarjetas de credito
		for(TarjetaCredito tarjetaCredito: this.getTarjetasCredito()) {
			if(!tarjetaCredito.isActiva())
				continue;
			if(!tarjetaCredito.tieneSaldo())
				continue;
			if(!tarjetaCredito.getDivisa().equals(origen))
				continue;
			return divisas;
		}
		
		//Si no se retornó anteriormente, es porque el cliente no tiene tarjetas con dicha divisa
		return null;
	}

	public ArrayList<Transaccion> verPeticiones(){
		ArrayList<Transaccion> retorno = new ArrayList<>();
		for(Transaccion t : Transaccion.getTransacciones()){
			if(t.getClienteObjetivo() != null){		//Verifica si la transacción tiene un cliente objetivo. sin esta verificación, la siguiente línea podría soltar un error
				if(t.getClienteObjetivo().equals(this) && t.getMensaje() != null && t.isPendiente()){
					retorno.add(t);
				}
			}
			
		}
		return retorno;
	}

	public ArrayList<Canal> listarCanales(ArrayList<Divisa> divisas) {//recibe un arrayList
		Divisa divisaOrigen = divisas.get(0);
		Divisa divisaDestino = divisas.get(1);
		
		ArrayList<Canal> canales = new ArrayList<>();
		for(Canal canal: Banco.getCanales()) {
			if(canal.tieneDivisa(divisaOrigen))
				continue;
			if(canal.tieneDivisa(divisaDestino))
				continue;
			if(canal.tieneFondosDeDivisa(divisaDestino))
				continue;
			canales.add(canal);
		}
		
		//ordenar por menos impuestos de menor a mayor
		return Banco.ordenarCanalesPorImpuestos(canales);
	}
	
	public ArrayList<Transaccion> revisarHistorialCreditos(){
		ArrayList<Transaccion> retorno = new ArrayList<>();
		for(Transaccion t : Transaccion.getTransacciones()){
			if(t.getClienteOrigen().equals(this) && t.getTarjetaOrigen() instanceof TarjetaCredito && !t.isRechazado() && !t.isPendiente()){
				retorno.add(t);
			}
		}
		return retorno;
	}
}
