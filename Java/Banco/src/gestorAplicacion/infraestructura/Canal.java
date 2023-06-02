//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.infraestructura;

import gestorAplicacion.entidades_de_negocio.Divisa;
import gestorAplicacion.entidades_de_negocio.Transaccion;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;

public class Canal implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private final String tipoCanal;

	/**
	 * Tipos de Canales:
	 * - Cajero Automático: Solo tienen una divisa disponible. Estos canales no pueden cambiar divisas. Solo se puede retirar dinero de ellos.
	 * - Sucursal Física: Tienen todas las divisas disponibles. Pueden cambiar todas las divisas.
	 * - Sucursal en Línea: Tienen todas las divisas disponibles. Pueden cambiar todas las divisas.
	 * - Corresponsal Bancario: Solo tienen 3 divisas, dos de ellas son las monedas internacionales más importantes (DÓLAR y EURO) y una tercera que puede ser cualquier otra.
	 *   Por lo tanto, solo se pueden cambiar esas 3 divisas en estos canales.
	 */
	private final float impuesto;
	private final EnumMap<Divisa, Double> fondosPorDivisa = new EnumMap<>(Divisa.class);

	/**
	 * Constructor de la clase Canal.
	 *
	 * @param tipoCanal El tipo de canal (Cajero Automático, Sucursal Física, Sucursal en Línea, Corresponsal Bancario).
	 * @param impuesto  La tasa de impuestos asociada al canal.
	 * @param fondos    Los fondos disponibles en el canal para cada divisa en el orden en que están declaradas las divisas.
	 */
	public Canal(String tipoCanal, float impuesto, double... fondos) {
		this(tipoCanal, impuesto);

		for (int i = 0; i < Divisa.values().length && i < fondos.length; i++) {
			Divisa divisa = Divisa.values()[i];
			double fondo = fondos[i];
			this.fondosPorDivisa.put(divisa, fondo);
		}
	}

	/**
	 * Constructor de la clase Canal sin fondos inicializados.
	 *
	 * @param tipoCanal El tipo de canal (Cajero Automático, Sucursal Física, Sucursal en Línea, Corresponsal Bancario).
	 * @param impuesto  La tasa de impuestos asociada al canal.
	 */
	public Canal(String tipoCanal, float impuesto) {
		this.tipoCanal = tipoCanal;
		this.impuesto = impuesto;

		Banco.agregarCanal(this);
	}

	// Getters y Setters
	
	public String getTipoCanal() {
		return tipoCanal;
	}	

	/**
	 * Establece los fondos disponibles para una divisa específica en el canal.
	 *
	 * @param divisa La divisa para la cual se establecerán los fondos.
	 * @param monto  El monto de fondos disponibles para la divisa.
	 */
	public void setFondos(Divisa divisa, double monto) {
		this.fondosPorDivisa.put(divisa, monto);
	}
	

	/**
	 * Obtiene los fondos disponibles para una divisa específica en el canal.
	 *
	 * @param divisa La divisa para la cual se obtendrán los fondos.
	 * @return El monto de fondos disponibles para la divisa.
	 */
	public double getFondos(Divisa divisa) {
		return this.fondosPorDivisa.get(divisa);
	}

	/**
	 * Obtiene la tasa de impuestos asociada al canal.
	 *
	 * @return La tasa de impuestos del canal.
	 */
	public double getImpuesto() {
		return impuesto;
	}

	// Métodos de las instancias

	/**
	 * Devuelve una representación en forma de cadena del objeto Canal.
	 *
	 * @return La representación en forma de cadena del objeto Canal.
	 */
	@Override
	public String toString() {
		return "Canal: %s #%s\nTasa de impuestos: %s\n".formatted(tipoCanal, Banco.getCanales().indexOf(this) + 1, impuesto);
	}

	/**
	 * Verifica si el canal tiene una divisa específica.
	 *
	 * @param divisa La divisa a verificar.
	 * @return true si el canal tiene la divisa, false en caso contrario.
	 */
	public boolean tieneDivisa(Divisa divisa) {
		return !fondosPorDivisa.containsKey(divisa);
	}

	/**
	 * Verifica si el canal tiene fondos disponibles para una divisa específica.
	 *
	 * @param divisa La divisa a verificar.
	 * @return true si el canal tiene fondos disponibles para la divisa, false en caso contrario.
	 */
	public boolean tieneFondosDeDivisa(Divisa divisa) {
		return !(fondosPorDivisa.get(divisa) > 0.0);
	}

	/**
	 * Finaliza la conversión de una transacción.
	 * Realiza los cambios necesarios en las tarjetas y fondos del canal.
	 *
	 * @param transaccion     La transacción a finalizar.
	 * @param montoInicial    El monto inicial de la transacción.
	 * @return La transacción finalizada.
	 */
	public Transaccion finalizarConversion(Transaccion transaccion, double montoInicial) {
		if (transaccion.isRechazado())
			transaccion.getTarjetaOrigen().anadirTransaccionRechazada();

		if (!transaccion.isPendiente())
			return null;

		Divisa divisaOrigen = transaccion.getDivisa();
		double fondosOrigen = transaccion.getCanal().getFondos(divisaOrigen);

		Divisa divisaDestino = transaccion.getTarjetaObjetivo().getDivisa();
		double fondosDestino = transaccion.getCanal().getFondos(divisaDestino);

		transaccion.getTarjetaOrigen().sacarDinero(montoInicial); // Sacando dinero de la tarjeta de origen
		transaccion.getCanal().setFondos(divisaOrigen, fondosOrigen + montoInicial); // Ingresando el dinero de la divisa de origen al canal
		transaccion.getCanal().setFondos(divisaDestino, fondosDestino - transaccion.getCantidad()); // Sacando dinero de la divisa de destino del canal
		transaccion.getTarjetaObjetivo().introducirDinero(transaccion.getCantidad()); // Ingresando la divisa de destino a la tarjeta de destino
		transaccion.setPendiente(false);

		return transaccion;
	}

	/**
	 * Selecciona los canales apropiados para una transacción en función de la divisa y la operación (retirar o depositar dinero).
	 *
	 * @param divisa   La divisa de la transacción.
	 * @param retirar  true si la operación es retirar dinero, false si es depositar dinero.
	 * @return Una lista de los canales apropiados para la transacción.
	 */
	public static ArrayList<Canal> seleccionarCanal(Divisa divisa, boolean retirar) {
		ArrayList<Canal> retorno = new ArrayList<>();
		if (retirar) {
			for (Canal canal : Banco.getCanales()) {
				if (!canal.tieneDivisa(divisa)) {
					retorno.add(canal);
				}
			}
		} else {
			for (Canal canal : Banco.getCanales()) {
				if (!canal.tieneDivisa(divisa)) {
					if(canal.getTipoCanal().equals("Cajero"))//No se puede depositar en un cajero
						continue;
					if (!canal.tieneFondosDeDivisa(divisa)) { // Estos chequeos deben hacerse uno después del otro, de otra manera, existe la posibilidad de que el programa lance un error en caso de que el canal no tenga la divisa
						retorno.add(canal);
					}
				}
			}
		}
		return retorno;
	}
	
	public static Transaccion finalizarTransaccion(Transaccion transaccion, boolean retirar){
		if (transaccion.isRechazado())
			transaccion.getTarjetaOrigen().anadirTransaccionRechazada();

		if (!transaccion.isPendiente())
			return null;
		
		if(retirar) {
			//Se saca dinero de la tarjeta y del canal
			transaccion.getTarjetaOrigen().sacarDinero(transaccion.getCantidad()); // Sacando dinero de la tarjeta
			transaccion.getCanal().setFondos(transaccion.getDivisa(), transaccion.getCanal().getFondos(transaccion.getDivisa()) - transaccion.getCantidad()); // Sacando dinero de la divisa del canal
		}
		else {
			//se deposita dinero al canal y a la tarjeta 
			transaccion.getTarjetaObjetivo().introducirDinero(transaccion.getCantidad());
			transaccion.getCanal().setFondos(transaccion.getDivisa(), transaccion.getCanal().getFondos(transaccion.getDivisa()) + transaccion.getCantidad()); // Ingresando el dinero de la divisa de origen al canal
		}
		transaccion.setPendiente(false);
		
		return new Transaccion(transaccion, retirar);
	}	
}
