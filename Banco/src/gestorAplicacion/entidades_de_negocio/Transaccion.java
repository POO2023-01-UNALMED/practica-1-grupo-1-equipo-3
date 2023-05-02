package gestorAplicacion.entidades_de_negocio;

import java.util.ArrayList;

import gestorAplicacion.tarjetas.Tarjeta;
import gestorAplicacion.tarjetas.TarjetaDebito;

public class Transaccion {
	private Cliente clienteObjetivo;
	private Tarjeta tarjetaOrigen;
	private TarjetaDebito tarjetaObjetivo;
	private double cantidad;
	private boolean rechazado;
	private boolean pendiente;
	private Factura factura;
	private static ArrayList<Transaccion> transacciones = new ArrayList<Transaccion>();

	
	public Transaccion(Cliente clienteObjetivo, Tarjeta tarjetaOrigen, TarjetaDebito tarjetaObjetivo, double cantidad) {
		this.clienteObjetivo = clienteObjetivo;
		this.tarjetaOrigen = tarjetaOrigen;
		this.tarjetaObjetivo = tarjetaObjetivo;
		this.cantidad = cantidad;
		rechazado = !tarjetaOrigen.transaccion(cantidad, tarjetaObjetivo);
		pendiente = false;
		transacciones.add(this);
	}

	public Transaccion(Cliente clienteObjetivo, Tarjeta tarjetaOrigen, TarjetaDebito tarjetaObjetivo, double cantidad, Factura factura, boolean rechazado) {
		this.clienteObjetivo = clienteObjetivo;
		this.tarjetaOrigen = tarjetaOrigen;
		this.tarjetaObjetivo = tarjetaObjetivo;
		this.cantidad = cantidad;
		this.rechazado = rechazado;
		this.factura = factura;
		pendiente = true;
		transacciones.add(this);
	}
	
	public boolean isRechazado() {
		return rechazado;
	}

	public String toString(){
		if(rechazado){
			return "Transacción rechazada por un monto de " + cantidad + " desde la tarjeta " + tarjetaOrigen.getNoTarjeta() + " Hacia la tarjeta " + tarjetaObjetivo.getNoTarjeta();
		} else if (pendiente){
			return "Transacción pendiente aprobada por un monto de " + cantidad + " desde la tarjeta " + tarjetaOrigen.getNoTarjeta() + " Hacia la tarjeta " + tarjetaObjetivo.getNoTarjeta();
		} else {
			return "Transacción aprobada por un monto de " + cantidad + " desde la tarjeta " + tarjetaOrigen.getNoTarjeta() + " Hacia la tarjeta " + tarjetaObjetivo.getNoTarjeta();
		}
	}

	public Factura pagarFactura(){
		if(this.pendiente && !this.rechazado){
			if(factura.getPendiente()-cantidad < 0){
				cantidad += factura.getPendiente()-cantidad;
			}
			double pendiente = factura.getPendiente()-cantidad;
			boolean pagado = factura.getPendiente() == cantidad;
			boolean vencido = !(factura.getTransfeRestantes() >= 1);
			this.tarjetaOrigen.sacarDinero(cantidad);
			return new Factura(pendiente, factura.getTransfeRestantes()-1, tarjetaObjetivo, pagado, vencido);

		}
		return null;
	}

}
