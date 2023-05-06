package gestorAplicacion.entidades_de_negocio;

import java.util.ArrayList;

import gestorAplicacion.tarjetas.Tarjeta;
import gestorAplicacion.tarjetas.TarjetaDebito;

public class Transaccion {
	private Cliente clienteObjetivo;
	private Cliente clienteOrigen;
	private Tarjeta tarjetaOrigen;
	private TarjetaDebito tarjetaObjetivo;
	private double cantidad;
	private boolean rechazado;
	private boolean pendiente;
	private Factura factura;
	private Divisa divisa;
	private static ArrayList<Transaccion> transacciones = new ArrayList<Transaccion>();

	
	public Transaccion(Cliente clienteObjetivo, Cliente clienteOrigen, Tarjeta tarjetaOrigen, TarjetaDebito tarjetaObjetivo, double cantidad) {
		this.clienteObjetivo = clienteObjetivo;
		this.clienteOrigen = clienteOrigen;
		this.tarjetaOrigen = tarjetaOrigen;
		this.tarjetaObjetivo = tarjetaObjetivo;
		this.cantidad = cantidad;
		rechazado = !tarjetaOrigen.transaccion(cantidad, tarjetaObjetivo);
		pendiente = false;
		divisa = tarjetaOrigen.getDivisa();
		transacciones.add(this);
	}

	public Transaccion(Cliente clienteOrigen, Tarjeta tarjetaOrigen, TarjetaDebito tarjetaObjetivo, double cantidad, Factura factura, boolean rechazado) {
		this.clienteOrigen = clienteOrigen;
		this.tarjetaOrigen = tarjetaOrigen;
		this.tarjetaObjetivo = tarjetaObjetivo;
		this.cantidad = cantidad;
		this.rechazado = rechazado;
		this.factura = factura;
		pendiente = true;
		divisa = tarjetaOrigen.getDivisa();
		transacciones.add(this);
	}
	
	public boolean isRechazado() {
		return rechazado;
	}

	public boolean isPendiente(){
		return pendiente;
	}

	public Divisa getDivisa(){
		return divisa;
	}

	public Cliente getClienteOrigen(){
		return clienteOrigen;
	}

	public double getCantidad(){
		return cantidad;
	}

	public static ArrayList<Transaccion> getTransacciones(){
		return transacciones;
	}

	public Tarjeta getTarjetaOrigen(){
		return tarjetaOrigen;
	}

	public String toString(){
		if(rechazado){
			return "Transacción rechazada por un monto de " + cantidad + " desde la tarjeta " + tarjetaOrigen.getNoTarjeta() + " Hacia la tarjeta " + tarjetaObjetivo.getNoTarjeta() + " por parte de " + clienteOrigen.getNombre();
		} else if (pendiente){
			return "Transacción pendiente aprobada por un monto de " + cantidad + " desde la tarjeta " + tarjetaOrigen.getNoTarjeta() + " Hacia la tarjeta " + tarjetaObjetivo.getNoTarjeta() + " por parte de " + clienteOrigen.getNombre();
		} else {
			return "Transacción aprobada por un monto de " + cantidad + " desde la tarjeta " + tarjetaOrigen.getNoTarjeta() + " Hacia la tarjeta " + tarjetaObjetivo.getNoTarjeta() + " por parte de " + clienteOrigen.getNombre();
		}
	}

	public Factura pagarFactura(){
		if(rechazado) tarjetaOrigen.anadirTransaccionRechazada();
		
		if(this.pendiente && !this.rechazado){
			boolean vencido;
			if(factura.getPendiente() - cantidad <= 0){
				cantidad += factura.getPendiente()- cantidad;//ajusta el monto a pagar para que sea igual al monto pendiente de la factura.
				vencido = factura.isFacturaVencida();
			}else{
				vencido = !(factura.getTransfeRestantes() > 1);
			}
			pendiente = false;
			double montoPagado = factura.getTotal() - factura.getPendiente() + cantidad;
			boolean pagado = factura.getPendiente() == cantidad;
			tarjetaOrigen.sacarDinero(cantidad);
			tarjetaObjetivo.introducirDinero(cantidad);
			return new Factura(clienteOrigen, factura.getTotal(), montoPagado, factura.getTransfeRestantes()-1, tarjetaObjetivo, pagado, vencido);

		}
		return null;
	}

}
