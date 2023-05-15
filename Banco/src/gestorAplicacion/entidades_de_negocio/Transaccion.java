package gestorAplicacion.entidades_de_negocio;

import java.util.ArrayList;

import gestorAplicacion.infraestructura.Banco;
import gestorAplicacion.infraestructura.Canal;
import gestorAplicacion.tarjetas.Tarjeta;
import gestorAplicacion.tarjetas.TarjetaDebito;

public class Transaccion {
	private Cliente clienteObjetivo;
	private Cliente clienteOrigen;
	private Tarjeta tarjetaOrigen;
	private TarjetaDebito tarjetaObjetivo;
	private Canal canalObjetivo; //Cuando una transaccion requiera retornar el valor de un impuesto al banco (se retorna a un canal) 
	private double cantidad;
	private boolean rechazado;
	private boolean pendiente;
	private Factura factura;
	private Divisa divisa;
	private static ArrayList<Transaccion> transacciones = new ArrayList<>();

	
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
	
	public Transaccion(Cliente clienteOrigen, Tarjeta tarjetaOrigen, TarjetaDebito tarjetaObjetivo, double cantidad, Canal canalObjetivo, boolean rechazado) {
		this.clienteOrigen = clienteOrigen;
		this.tarjetaOrigen = tarjetaOrigen;
		this.tarjetaObjetivo = tarjetaObjetivo;
		this.cantidad = cantidad;
		this.rechazado = rechazado;
		this.canalObjetivo = canalObjetivo;
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
			return "Transacción Rechazada\nTotal: " + Banco.formatearNumero(cantidad) + "\nTarjeta de origen: " + tarjetaOrigen.getNoTarjeta() + "\nTarjeta de destino: " + tarjetaObjetivo.getNoTarjeta() + "\nProveniente de: " + clienteOrigen.getNombre();
		} else if (pendiente){
			return "Transacción Pendiente\nAprobada por un total de: " + Banco.formatearNumero(cantidad) + "\nTarjeta de origen: " + tarjetaOrigen.getNoTarjeta() + "\nTarjeta de destino: " + tarjetaObjetivo.getNoTarjeta() + "\nProveniente de: " + clienteOrigen.getNombre();
		} else {
			return "Transacción Aprobada\nTotal: " + Banco.formatearNumero(cantidad) + "\nTarjeta de origen: " + tarjetaOrigen.getNoTarjeta() + "\nTarjeta de destino: " + tarjetaObjetivo.getNoTarjeta() + "\nProveniente de: " + clienteOrigen.getNombre();
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
	
	//Transaccion que se genera al cambiar Divisas
	public static Transaccion crearTransaccion(ArrayList<Divisa> divisas, ArrayList<Double> montos, Canal canal, ArrayList<Tarjeta> tarjetas, Cliente cliente) {
		Divisa divisaOrigen = divisas.get(0);
		Divisa divisaDestino = divisas.get(1);
		Double montoFinal = montos.get(0);
		Double impuestoRetorno = montos.get(1);//Monto que se pagará al canal
		Tarjeta tarjetaOrigen = tarjetas.get(0);
		TarjetaDebito tarjetaDestino = (TarjetaDebito) tarjetas.get(0);
		
		boolean validez = true;
		if(!(canal.getFondos(divisaDestino) >= montoFinal) || !(tarjetaOrigen.puedeTransferir(montoFinal)))
			validez = false;
		
		Transaccion transaccion = new Transaccion(cliente, tarjetaOrigen, tarjetaDestino, montoFinal, canal, validez);
		transaccion.pendiente = false;
		return transaccion;
	}
}
