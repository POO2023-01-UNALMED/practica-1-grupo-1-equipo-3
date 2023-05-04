package gestorAplicacion.entidades_de_negocio;

import java.util.ArrayList;

import gestorAplicacion.tarjetas.*;

public class Factura {
	private Cliente cliente;
	private Divisa divisa; //Las facturas deben ser pagada en una determinada divisa
	private double total;
	private double valorPagado;
	private int transfeRestantes; //El tope maximo de transferencias antes de que la factura venza
	private TarjetaDebito tarjetaDestino;
	private ArrayList<Transaccion> transfeActuales = new ArrayList<Transaccion>();
	private boolean facturaVencida;
	private boolean facturaPagada;

	public Factura(Cliente cliente, double total, int transfeRestantes, TarjetaDebito tarjetaDestino) {
		this.cliente = cliente;
		this.divisa = tarjetaDestino.getDivisa();
		this.total = total;
		this.valorPagado = 0;
		this.transfeRestantes = transfeRestantes;
		this.tarjetaDestino = tarjetaDestino;
		this.facturaVencida = false;
		this.facturaPagada = false;
		cliente.agregarFactura(this);
	}

	public Factura(double total, int transfeRestantes, TarjetaDebito tarjetaDestino, boolean facturaPagada, boolean facturaVencida) { //crea una factura sin cliente
		this.divisa = tarjetaDestino.getDivisa();
		this.total = total;
		this.valorPagado = 0;
		this.transfeRestantes = transfeRestantes;
		this.tarjetaDestino = tarjetaDestino;
		this.facturaVencida = facturaVencida;
		this.facturaPagada = facturaPagada;
	}

	public int getTransfeRestantes(){
		return transfeRestantes;
	}
	public boolean isFacturaVencida(){
		return facturaVencida;
	}
	public boolean isFacturaPagada(){
		return facturaPagada;
	}

	public double getPendiente(){
		return total-valorPagado;
	}

	public Divisa getDivisa(){
		return divisa;
	}
	
	public String toString(){
		String retorno = "";
		if(facturaPagada && !facturaVencida){
			retorno = "Factura pagada antes de vencer.\nTarjeta objetivo es: " + tarjetaDestino.getNoTarjeta() + "\n";
		} else if (facturaPagada && facturaVencida){
			retorno = "Factura pagada después de vencer.\nTarjeta objetivo es: " + tarjetaDestino.getNoTarjeta() + "\n";
		}else if(facturaVencida){
			retorno = "Factura vencida por pagar.1\nTarjeta objetivo es: " + tarjetaDestino.getNoTarjeta() + " faltan " + (total-valorPagado) + " " + divisa.getMoneda() + " por pagar en " + transfeRestantes + " transferencias" + "\n";
		} else {
			retorno = "Factura no vencida por pagar.\nTarjeta objetivo es: " + tarjetaDestino.getNoTarjeta() + " faltan " + (total-valorPagado) + " " + divisa.getMoneda() + " por pagar en " + transfeRestantes + " transferencias" + "\n";
		}
		return retorno;
	}

	public Transaccion generarTransaccion(double monto, Tarjeta tarjetaOrigen){
		boolean validez = false;
		if(tarjetaOrigen.puedeTransferir(monto) && tarjetaOrigen.getDivisa().equals(this.tarjetaDestino.getDivisa())){
			validez = true;
		}
		
		return new Transaccion(cliente, tarjetaOrigen, tarjetaDestino, monto, this, !validez);
	}
	
	
}
