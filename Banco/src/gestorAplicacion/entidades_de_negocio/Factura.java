package gestorAplicacion.entidades_de_negocio;

import java.util.ArrayList;

import gestorAplicacion.tarjetas.*;

public class Factura {
	private Cliente cliente;
	private Divisa divisa; //Las facturas deben ser pagada en una determinada divisa
	private double total;
	private double valorPagado;
	private int transfeTotales; //El tope maximo de transferencias antes de que la factura venza
	private TarjetaDebito tarjetaDestino;
	private ArrayList<Transaccion> transfeActuales = new ArrayList<Transaccion>();
	private boolean facturaVencida;
	private boolean facturaPagada;
	public Factura(Cliente cliente, Divisa divisa, double total, double valorPagado, int transfeTotales,
			TarjetaDebito tarjetaDestino, ArrayList<Transaccion> transfeActuales, boolean facturaVencida,
			boolean facturaPagada) {
		this.cliente = cliente;
		this.divisa = divisa;
		this.total = total;
		this.valorPagado = valorPagado;
		this.transfeTotales = transfeTotales;
		this.tarjetaDestino = tarjetaDestino;
		this.transfeActuales = transfeActuales;
		this.facturaVencida = facturaVencida;
		this.facturaPagada = facturaPagada;
	}
	
	
	
	
	
}
