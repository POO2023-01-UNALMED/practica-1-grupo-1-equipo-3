//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.entidades_de_negocio;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import gestorAplicacion.infraestructura.Banco;
import gestorAplicacion.tarjetas.*;

public class Factura implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private final Cliente CLIENTE;           // Cliente asociado a la factura
	private final Divisa DIVISA;             // Divisa en la que se debe pagar la factura
	private final double TOTAL;              // Monto total de la factura
	private double valorPagado;              // Monto pagado de la factura
	private int transfeRestantes;            // Número máximo de transferencias restantes antes de que la factura venza
	private final TarjetaDebito TARJETADESTINO;   // Tarjeta de débito asociada a la factura
	private boolean facturaVencida;          // Indica si la factura está vencida
	private boolean facturaPagada;           // Indica si la factura ha sido pagada

	// Constructor para crear una nueva factura
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

	// Constructor utilizado en el contexto de pagarFactura (método de Transaccion)
	public Factura(Cliente cliente, double total, double valorPagado, int transfeRestantes, TarjetaDebito tarjetaDestino, boolean facturaPagada, boolean facturaVencida) {
		this.CLIENTE = cliente;
		this.DIVISA = tarjetaDestino.getDivisa();
		this.TOTAL = total;
		this.valorPagado = valorPagado;
		this.transfeRestantes = transfeRestantes;
		this.TARJETADESTINO = tarjetaDestino;
		this.facturaVencida = facturaVencida;
		this.facturaPagada = facturaPagada;
	}

	// Devuelve el número de transferencias restantes antes de que la factura venza
	public int getTransfeRestantes() {
		return transfeRestantes;
	}

	// Verifica si la factura está vencida
	public boolean isFacturaVencida() {
		return facturaVencida;
	}

	// Verifica si la factura ha sido pagada
	public boolean isFacturaPagada() {
		return !facturaPagada;
	}

	// Devuelve el monto pendiente por pagar de la factura
	public double getPendiente() {
		return TOTAL - valorPagado;
	}

	// Devuelve el monto total de la factura
	public double getTOTAL() {
		return TOTAL;
	}

	// Devuelve la divisa en la que se debe pagar la factura
	public Divisa getDIVISA() {
		return DIVISA;
	}

	// Devuelve una representación en forma de cadena de la factura
	public String toString() {
		String retorno;
		if (facturaPagada && !facturaVencida) {
			retorno = "Factura pagada ANTES de vencer\nTarjeta objetivo: " + TARJETADESTINO.getNoTarjeta() + "\n";
		} else if (facturaPagada) {
			retorno = "Factura pagada DESPUÉS de vencer\nTarjeta objetivo: " + TARJETADESTINO.getNoTarjeta() + "\n";
		} else if (facturaVencida) {
			retorno = "Factura vencida por pagar.1\nTarjeta objetivo: " + TARJETADESTINO.getNoTarjeta() + " faltan " + Banco.formatearNumero((TOTAL - valorPagado)) + " " + DIVISA.name() + " por pagar" + "\n";
		} else {
			retorno = "Factura no vencida por pagar.\nTarjeta objetivo: " + TARJETADESTINO.getNoTarjeta() + " faltan " + Banco.formatearNumero(TOTAL - valorPagado) + " " + DIVISA.name() + " por pagar en " + transfeRestantes + " transferencias" + "\n";
		}
		return retorno;
	}

	// Genera una transacción para pagar la factura
	public Transaccion generarTransaccion(double monto, Tarjeta tarjetaOrigen) {
		// Verifica si la tarjeta de origen puede transferir el monto y si tiene la misma divisa que la tarjeta destino
		boolean validez = tarjetaOrigen.puedeTransferir(monto) && tarjetaOrigen.getDivisa().equals(this.TARJETADESTINO.getDivisa());

		return new Transaccion(CLIENTE, tarjetaOrigen, TARJETADESTINO, monto, this, !validez);
	}

	// Modifica el puntaje de un cliente según el estado de sus facturas y tarjetas
	public static int modificarPuntaje(ArrayList<Tarjeta> tarjetasBloqueadas, ArrayList<Tarjeta> tarjetasActivas, Cliente cliente, int puntaje) {
		for (Factura f : cliente.getFactura()) {
			if (f.facturaVencida) {
				puntaje -= 50;
			}
			if (f.facturaPagada && !f.facturaVencida) {
				puntaje += 0.5 * f.TOTAL * f.DIVISA.getValor();
			}
		}
		for (Tarjeta t : tarjetasActivas) {
			if (t instanceof TarjetaDebito) {
				puntaje += (int) (0.1 * (((TarjetaDebito) t).getSaldo() * t.getDivisa().getValor()));
			}
		}
		puntaje -= 100 * (tarjetasBloqueadas.size()) / (tarjetasActivas.size() + tarjetasBloqueadas.size());
		puntaje += cliente.getBonoActual();
		return puntaje;
	}
}

