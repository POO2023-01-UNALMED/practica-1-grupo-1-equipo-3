package gestorAplicacion.entidades_de_negocio;

import java.util.ArrayList;

public class Transaccion {
	private Cliente clienteOrigen;
	private Cliente clienteObjetivo;
	private Tarjeta tarjetaOrigen;
	private TarjetaDebito tarjetaObjetivo;
	private double cantidad;
	private boolean rechazado;
	private int periodo; //Almacena la cantidad de tiempo que pasa antes de que se vuelva a hacer la transacción. Si la transacción se hace solo una vez, su valor es cero.
	private static ArrayList<Transaccion> transacciones = new ArrayList<Transaccion>();

	
	public Transaccion(Cliente clienteOrigen, Cliente clienteObjetivo, Tarjeta tarjetaOrigen, TarjetaDebito tarjetaObjetivo, double cantidad) {
		this.clienteOrigen = clienteOrigen;
		this.clienteObjetivo = clienteObjetivo;
		this.tarjetaOrigen = tarjetaOrigen;
		this.tarjetaObjetivo = tarjetaObjetivo;
		this.cantidad = cantidad;
		rechazado = !tarjetaOrigen.transaccion(cantidad, tarjetaObjetivo);
		this.periodo = 0;
		transacciones.add(this);
	}
	
	public boolean isRechazado() {
		return rechazado;
	}
}
