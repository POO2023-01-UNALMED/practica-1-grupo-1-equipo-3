//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.entidades_de_negocio;

import java.util.ArrayList;

import gestorAplicacion.infraestructura.Banco;
import gestorAplicacion.tarjetas.*;

public class Factura {
	private Cliente cliente;
	private Divisa divisa; //Las facturas deben ser pagada en una determinada divisa
	private double total;
	private double valorPagado;
	private int transfeRestantes; //El tope maximo de transferencias antes de que la factura venza
	private TarjetaDebito tarjetaDestino;
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

	public Factura(Cliente cliente, double total, double valorPagado, int transfeRestantes, TarjetaDebito tarjetaDestino, boolean facturaPagada, boolean facturaVencida) { //Se utiliza en el contexto de pagarFactura (método de Transaccion)
		this.cliente = cliente;
		this.divisa = tarjetaDestino.getDivisa();
		this.total = total;
		this.valorPagado = valorPagado;
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

	public double getTotal(){
		return total;
	}

	public Divisa getDivisa(){
		return divisa;
	}
	
	public String toString(){
		String retorno;
		if(facturaPagada && !facturaVencida){
			retorno = "Factura pagada ANTES de vencer\nTarjeta objetivo: " + tarjetaDestino.getNoTarjeta() + "\n";
		} else if (facturaPagada){
			retorno = "Factura pagada DESPUÉS de vencer\nTarjeta objetivo: " + tarjetaDestino.getNoTarjeta() + "\n";
		}else if(facturaVencida){
			retorno = "Factura vencida por pagar.1\nTarjeta objetivo: " + tarjetaDestino.getNoTarjeta() + " faltan " + Banco.formatearNumero((total-valorPagado)) + " " + divisa.name() + " por pagar" + "\n";
		} else {
			retorno = "Factura no vencida por pagar.\nTarjeta objetivo: " + tarjetaDestino.getNoTarjeta() + " faltan " + Banco.formatearNumero(total-valorPagado) + " " + divisa.name() + " por pagar en " + transfeRestantes + " transferencias" + "\n";
		}
		return retorno;
	}

	public Transaccion generarTransaccion(double monto, Tarjeta tarjetaOrigen){
		boolean validez = tarjetaOrigen.puedeTransferir(monto) && tarjetaOrigen.getDivisa().equals(this.tarjetaDestino.getDivisa());

		return new Transaccion(cliente, tarjetaOrigen, tarjetaDestino, monto, this, !validez);
	}
	
	static int modificarPuntaje(ArrayList<Tarjeta> tarjetasBloqueadas, ArrayList<Tarjeta> tarjetasActivas, Cliente cliente, int puntaje){
		for(Factura f : cliente.getFactura()){
			if(f.facturaVencida){
				puntaje -= 50;
			}
			if(f.facturaPagada && !f.facturaVencida){
				puntaje += 0.5 * f.total*f.divisa.getValor();
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
