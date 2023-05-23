//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.entidades_de_negocio;

import java.io.Serializable;
import java.util.ArrayList;

import gestorAplicacion.infraestructura.Banco;
import gestorAplicacion.tarjetas.*;

public class Factura implements Serializable{
	private static final long serialVersionUID = 1L;

	private final Cliente CLIENTE;
	private final Divisa DIVISA; //Las facturas deben ser pagada en una determinada divisa
	private final double TOTAL;
	private double valorPagado;
	private int transfeRestantes; //El tope maximo de transferencias antes de que la factura venza
	private final TarjetaDebito TARJETADESTINO;
	private boolean facturaVencida;
	private boolean facturaPagada;

	public Factura(Cliente cliente, double total, int transfeRestantes, TarjetaDebito tarjetaDestino) {
		this.CLIENTE = cliente;
		this.DIVISA = tarjetaDestino.getDivisa();
		this.TOTAL = total;
		this.valorPagado = 0;
		this.transfeRestantes = transfeRestantes;
		this.TARJETADESTINO = tarjetaDestino;
		this.facturaVencida = false;
		this.facturaPagada = false;
		cliente.agregarFactura(this);
	}

	public Factura(Cliente cliente, double total, double valorPagado, int transfeRestantes, TarjetaDebito tarjetaDestino, boolean facturaPagada, boolean facturaVencida) { //Se utiliza en el contexto de pagarFactura (método de Transaccion)
		this.CLIENTE = cliente;
		this.DIVISA = tarjetaDestino.getDivisa();
		this.TOTAL = total;
		this.valorPagado = valorPagado;
		this.transfeRestantes = transfeRestantes;
		this.TARJETADESTINO = tarjetaDestino;
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
		return !facturaPagada;
	}

	public double getPendiente(){
		return TOTAL-valorPagado;
	}

	public double getTOTAL(){
		return TOTAL;
	}

	public Divisa getDIVISA(){
		return DIVISA;
	}
	
	public String toString(){
		String retorno;
		if(facturaPagada && !facturaVencida){
			retorno = "Factura pagada ANTES de vencer\nTarjeta objetivo: " + TARJETADESTINO.getNoTarjeta() + "\n";
		} else if (facturaPagada){
			retorno = "Factura pagada DESPUÉS de vencer\nTarjeta objetivo: " + TARJETADESTINO.getNoTarjeta() + "\n";
		}else if(facturaVencida){
			retorno = "Factura vencida por pagar.1\nTarjeta objetivo: " + TARJETADESTINO.getNoTarjeta() + " faltan " + Banco.formatearNumero((TOTAL-valorPagado)) + " " + DIVISA.name() + " por pagar" + "\n";
		} else {
			retorno = "Factura no vencida por pagar.\nTarjeta objetivo: " + TARJETADESTINO.getNoTarjeta() + " faltan " + Banco.formatearNumero(TOTAL-valorPagado) + " " + DIVISA.name() + " por pagar en " + transfeRestantes + " transferencias" + "\n";
		}
		return retorno;
	}

	public Transaccion generarTransaccion(double monto, Tarjeta tarjetaOrigen){
		boolean validez = tarjetaOrigen.puedeTransferir(monto) && tarjetaOrigen.getDivisa().equals(this.TARJETADESTINO.getDivisa());

		return new Transaccion(CLIENTE, tarjetaOrigen, TARJETADESTINO, monto, this, !validez);
	}
	
	public static int modificarPuntaje(ArrayList<Tarjeta> tarjetasBloqueadas, ArrayList<Tarjeta> tarjetasActivas, Cliente cliente, int puntaje){
		for(Factura f : cliente.getFactura()){
			if(f.facturaVencida){
				puntaje -= 50;
			}
			if(f.facturaPagada && !f.facturaVencida){
				puntaje += 0.5 * f.TOTAL*f.DIVISA.getValor();
			}
		}
		for(Tarjeta t : tarjetasActivas){
			if(t instanceof TarjetaDebito){
				puntaje += (int) (0.1*(((TarjetaDebito) t).getSaldo()*t.getDivisa().getValor()));
			}
		}
		puntaje -= 100 *(tarjetasBloqueadas.size())/(tarjetasActivas.size() + tarjetasBloqueadas.size());
		puntaje += cliente.getBonoActual();
		return puntaje;
	}

	public void setValorPagado(double pagado){
		this.valorPagado = pagado;
	}

	public double getValorPagado(){
		return this.valorPagado;
	}
	
}
