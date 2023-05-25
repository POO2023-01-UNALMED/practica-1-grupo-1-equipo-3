//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.entidades_de_negocio;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import gestorAplicacion.infraestructura.Banco;
import gestorAplicacion.infraestructura.Canal;
import gestorAplicacion.tarjetas.Tarjeta;
import gestorAplicacion.tarjetas.TarjetaDebito;

public class Transaccion implements Serializable{
	@Serial
	private static final long serialVersionUID = 1L;
	
	private Cliente clienteObjetivo;
	private Cliente clienteOrigen;
	private Tarjeta tarjetaOrigen;
	private TarjetaDebito tarjetaObjetivo;
	private Canal canal; //Cuando una transaccion requiera retornar el valor de un impuesto al banco (se retorna a un canal) 
	private double cantidad;
	private double impuesto;//Algunas transaccion pueden descontar un pequeño impuesto
	private boolean rechazado;
	private boolean pendiente;
	private boolean retornable;
	private Factura factura;
	private Divisa divisa;
	private String mensaje; //Se utiliza únicamente en la funcionalidad deshacer transacción
	private static ArrayList<Transaccion> transacciones = new ArrayList<>();

	
	public Transaccion(Cliente clienteObjetivo, Cliente clienteOrigen, Tarjeta tarjetaOrigen, TarjetaDebito tarjetaObjetivo, double cantidad) {
		this.clienteObjetivo = clienteObjetivo;
		this.clienteOrigen = clienteOrigen;
		this.tarjetaOrigen = tarjetaOrigen;
		this.tarjetaObjetivo = tarjetaObjetivo;
		this.cantidad = cantidad;
		rechazado = !tarjetaOrigen.transaccion(cantidad, tarjetaObjetivo);
		pendiente = false;
		retornable = true;
		divisa = tarjetaOrigen.getDivisa();
		transacciones.add(this);
	}

	public Transaccion(Cliente cliente, Tarjeta tarjeta, double cantidad, Canal canal, boolean retirar){ // Se utiliza en la funcionalidad "retirar o depositar dinero"
		if(retirar){
			this.clienteOrigen = cliente;
			this.tarjetaOrigen = tarjeta;
		}else{
			this.clienteObjetivo = cliente;
			this.tarjetaObjetivo = (TarjetaDebito)tarjeta;
		}
		this.cantidad = cantidad;
		this.canal = canal;
		divisa = tarjeta.getDivisa();
		pendiente = true;
		retornable = false;
	}

	public Transaccion(Cliente clienteOrigen, Tarjeta tarjetaOrigen, TarjetaDebito tarjetaObjetivo, double cantidad, boolean rechazado){
		this.clienteOrigen=clienteOrigen;
		this.tarjetaOrigen = tarjetaOrigen;
		this.tarjetaObjetivo = tarjetaObjetivo;
		this.cantidad = cantidad;
		this.rechazado = rechazado;
		divisa = tarjetaOrigen.getDivisa();
	}

	public Transaccion(Cliente clienteOrigen, Tarjeta tarjetaOrigen, TarjetaDebito tarjetaObjetivo, double cantidad, Factura factura, boolean rechazado) {
		this(clienteOrigen, tarjetaOrigen, tarjetaObjetivo, cantidad, rechazado);
		this.factura = factura;
		pendiente = true;
		retornable = true;
		transacciones.add(this);
	}
	
	public Transaccion(Cliente clienteOrigen, Tarjeta tarjetaOrigen, TarjetaDebito tarjetaObjetivo, double cantidad, double impuesto, Canal canalObjetivo, boolean rechazado) {
		this(clienteOrigen, tarjetaOrigen, tarjetaObjetivo, cantidad, rechazado);
		this.impuesto = impuesto;
		this.canal = canalObjetivo;
		pendiente = true;
		retornable = true;
		transacciones.add(this);
	}

	public Transaccion(Transaccion transaccion, String mensaje){ //genera una petición para que la vea el otro cliente
		this.clienteOrigen = transaccion.clienteOrigen;
		this.clienteObjetivo = transaccion.clienteObjetivo;
		this.tarjetaOrigen = transaccion.tarjetaOrigen;
		this.tarjetaObjetivo = transaccion.tarjetaObjetivo;
		this.cantidad = transaccion.cantidad;
		this.mensaje = mensaje;
		this.divisa = tarjetaOrigen.getDivisa();
		pendiente = true;
		rechazado = false;
		retornable = false;
		transacciones.add(this);
	}

	public Transaccion(Transaccion transaccion, boolean retirar){
		if(retirar){
			this.clienteOrigen = transaccion.clienteOrigen;
			this.tarjetaOrigen = transaccion.tarjetaOrigen;
			this.cantidad = transaccion.cantidad;
			this.canal = transaccion.canal;
			this.divisa = tarjetaOrigen.getDivisa();
			rechazado = !(tarjetaOrigen.puedeTransferir(cantidad) && canal.getFondos(tarjetaOrigen.getDivisa()) >= cantidad); //En caso de que el cliente quiera retirar, es necesario chequear estas dos condiciones
			if(!rechazado){
				tarjetaOrigen.sacarDinero(cantidad);
				canal.setFondos(divisa, canal.getFondos(divisa)-cantidad);
			}
		}else{
			this.clienteObjetivo = transaccion.clienteObjetivo;
			this.tarjetaObjetivo = transaccion.tarjetaObjetivo;
			this.cantidad = transaccion.cantidad;
			this.canal = transaccion.canal;
			divisa = tarjetaObjetivo.getDivisa();
			rechazado = false;
			tarjetaObjetivo.introducirDinero(cantidad);
			canal.setFondos(divisa, canal.getFondos(divisa)+cantidad);
		}
		pendiente = false;
		retornable = false;
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

	public String getMensaje(){
		return mensaje;
	}

	public boolean isRetornable(){
		return retornable;
	}

	public double getCantidad(){
		return cantidad;
	}

	public Cliente getClienteObjetivo() {
		return clienteObjetivo;
	}

	public TarjetaDebito getTarjetaObjetivo() {
		return tarjetaObjetivo;
	}

	public Canal getCanal() {
		return canal;
	}

	public void setRetornable(boolean retornable){
		this.retornable = retornable;
	}

	public void setPendiente(boolean pendiente) {
		this.pendiente = pendiente;
	}

	public void setDivisa(Divisa divisa) {
		this.divisa = divisa;
	}

	public static ArrayList<Transaccion> getTransacciones(){
		return transacciones;
	}

	public static ArrayList<Transaccion> encontrarTransacciones(Cliente clienteOrigen, Divisa divisa){ //Funciones que permiten filtrar las transacciones según un criterio dado
		ArrayList<Transaccion> retorno = new ArrayList<>();
		for(Transaccion t : transacciones){
			if(t.divisa.equals(divisa) && t.clienteOrigen.equals(clienteOrigen) && t.clienteObjetivo != null && t.isRetornable() && !t.isPendiente() && !t.isRechazado()){
				retorno.add(t);
			}
		}
		return retorno;
	}

	public static ArrayList<Transaccion> encontrarTransacciones(Cliente clienteOrigen, Cliente clienteObjetivo){ //Funciones que permiten filtrar las transacciones según un criterio dado
		ArrayList<Transaccion> retorno = new ArrayList<>();
		for(Transaccion t : transacciones){
			if(t.clienteOrigen.equals(clienteOrigen) && t.clienteObjetivo.equals(clienteObjetivo) && t.isRetornable() && !t.isPendiente() && !t.isRechazado()){
				retorno.add(t);
			}
		}
		return retorno;
	}

	public static ArrayList<Transaccion> encontrarTransacciones(Cliente clienteOrigen, Tarjeta tarjeta){ //Funciones que permiten filtrar las transacciones según un criterio dado
		ArrayList<Transaccion> retorno = new ArrayList<>();
		for(Transaccion t : transacciones){
			if(t.clienteOrigen.equals(clienteOrigen) && t.tarjetaOrigen.equals(tarjeta) && t.clienteObjetivo != null && t.isRetornable()&& !t.isPendiente() && !t.isRechazado()){
				retorno.add(t);
			}
		}
		return retorno;
	}

	public Tarjeta getTarjetaOrigen(){
		return tarjetaOrigen;
	}

	public String toString(){
		if(mensaje != null && pendiente){ //Es el toString en caso de que la transacción sea una petición por parte de otro usuario
			return "El cliente " + clienteOrigen.NOMBRE + " quisiera deshacer una transacción por " + Banco.formatearNumero(cantidad) + " " + tarjetaOrigen.getDivisa() + " recibidos por la tarjeta #" + tarjetaObjetivo.getNoTarjeta() + "\nSu mensaje es: " + mensaje;
		}
		if(rechazado){
			return "Transacción Rechazada\nTotal: " + Banco.formatearNumero(cantidad) + "\nTarjeta de origen: #" + tarjetaOrigen.getNoTarjeta() + "\nTarjeta de destino: #" + tarjetaObjetivo.getNoTarjeta() + "\nProveniente de: " + clienteOrigen.NOMBRE + "\n";
		} else if (pendiente){
			return "Transacción Pendiente\nAprobada por un total de: " + Banco.formatearNumero(cantidad) + "\nTarjeta de origen: #" + tarjetaOrigen.getNoTarjeta() + "\nTarjeta de destino: #" + tarjetaObjetivo.getNoTarjeta() + "\nProveniente de: " + clienteOrigen.NOMBRE + "\n";
		} else {
			if (impuesto != 0) {
				return "Transacción aprobada con éxito\nTotal: " + Banco.formatearNumero(cantidad) + "\nImpuesto: " + impuesto + "\nTarjeta de origen: #" + tarjetaOrigen.getNoTarjeta() + "\nTarjeta de destino: #" + tarjetaObjetivo.getNoTarjeta() + "\nProveniente de: " + clienteOrigen.NOMBRE + "\n";
			}
			else {
				return "Transacción aprobada con éxito\nTotal: " + Banco.formatearNumero(cantidad) + "\nTarjeta de origen: #" + tarjetaOrigen.getNoTarjeta() + "\nTarjeta de destino: #" + tarjetaObjetivo.getNoTarjeta() + "\nProveniente de: " + clienteOrigen.NOMBRE + "\n";				
			}
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
			double montoPagado = factura.getTOTAL() - factura.getPendiente() + cantidad;
			boolean pagado = factura.getPendiente() == cantidad;
			tarjetaOrigen.sacarDinero(cantidad);
			tarjetaObjetivo.introducirDinero(cantidad);
			return new Factura(clienteOrigen, factura.getTOTAL(), montoPagado, factura.getTransfeRestantes()-1, tarjetaObjetivo, pagado, vencido);

		}
		return null;
	}
	
	//Transaccion que se genera al cambiar Divisas
	public static Transaccion crearTransaccion(ArrayList<Divisa> divisas, double montoInicial, ArrayList<Double> montosFinales, Canal canal, ArrayList<Tarjeta> tarjetas, Cliente cliente) {
		Divisa divisaDestino = divisas.get(1);
		Double montoFinal = montosFinales.get(0);
		Double impuestoRetorno = montosFinales.get(1);//Monto que se pagará al canal
		Tarjeta tarjetaOrigen = tarjetas.get(0);
		TarjetaDebito tarjetaDestino = (TarjetaDebito) tarjetas.get(1);
		
		boolean rechazado = montoFinal > canal.getFondos(divisaDestino) || !(tarjetaOrigen.puedeTransferir(montoInicial));

		Transaccion transaccion = new Transaccion(cliente, tarjetaOrigen, tarjetaDestino, montoFinal, impuestoRetorno, canal, rechazado);
		transaccion.pendiente = !rechazado;
		return transaccion;
	}

	public static Transaccion completarTransaccion(Transaccion transaccion, Boolean respuesta){
		if(!respuesta){
			transaccion.rechazado = true;
			transaccion.pendiente = false;
		}else{
			transaccion.rechazado = false;
			transaccion.pendiente = false;
			transaccion.tarjetaObjetivo.deshacerTransaccion(transaccion.cantidad, transaccion.tarjetaOrigen); // En caso de que el cliente diga que sí, esta función deshace la transaccion.
		}
		return transaccion;
	}
	public static Transaccion finalizarTransaccion(Transaccion transaccion, boolean retirar){
		return new Transaccion(transaccion, retirar);
	}
}
