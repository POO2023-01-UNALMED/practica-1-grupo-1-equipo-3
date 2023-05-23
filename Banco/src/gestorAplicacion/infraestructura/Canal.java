//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package gestorAplicacion.infraestructura;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import gestorAplicacion.entidades_de_negocio.*;

public class Canal implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final String tipoCanal;
	
	/*	Tipos de Canales:
	 *	- Cajero Automatico: Solo tienen una divisa disponible. Estos canales no pueden cambiar divisas. Solo se puede retirar de ellos
	 *	- Sucursal Fisica: Tienen todas las divisas disponibles. Pueden cambiar todas las divisas.
	 *	- Sucursal en Linea: Tienen todas las divisas disponibles. Pueden cambiar todas las divisas. 
	 *	- Corresponsal Bancario: Solo tienen 3 divisas, dos de ellas son las monedas internacionales mas importantes (DOLAR y EURO) y una tercera que puede ser cualquier otra.
	 *	Por lo que solo se pueden cambiar esas 3 divisas en estos canales
	 * */
	private float impuesto;
	private int contador;//Este atributo solo es para asignarle un numero a los canales, para rastrearlos mejor. Ej: Sucursal Fisica #2
	private EnumMap<Divisa, Double> fondosPorDivisa = new EnumMap<>(Divisa.class);

	//Los fondos deben ser proporcionados en el orden en que estan declaradas las divisas
    public Canal(String tipoCanal, float impuesto, double... fondos) {
    	this(tipoCanal, impuesto);
    	
    	for(int i = 0; i < Divisa.values().length && i < fondos.length; i++) {
    		Divisa divisa = Divisa.values()[i];
    		double fondo = fondos[i];
    		this.fondosPorDivisa.put(divisa, fondo);
    	}
    }
    
    //Constructor para configurar solo el tipo de canal y los impuestos, los fondos seran configurados uno por uno
    //Esto con la idea de inicializar canales que solo tengan algunas divisas
    public Canal(String tipoCanal, float impuesto) {
    	this.tipoCanal = tipoCanal;
    	this.impuesto = impuesto;
    	
    	Banco.agregarCanal(this);
    }
    
    //Getters y Setters
    public void setFondos(Divisa divisa, double monto) {
        this.fondosPorDivisa.put(divisa, monto);
    }
    
    public double getFondos(Divisa divisa) {
        return this.fondosPorDivisa.get(divisa);
    }
    
    public EnumMap<Divisa, Double> getFondos() {
        return fondosPorDivisa;
    }
    
    
    public double getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(float impuesto) {
		this.impuesto = impuesto;
	}

	//Metodos de las instancias
	
	@Override
	public String toString() {
		return "Canal: %s #%s\nTasa de impuestos: %s\n".formatted(tipoCanal,Banco.getCanales().indexOf(this) + 1,impuesto);
	}
	
    public boolean tieneDivisa(Divisa divisa) {
        return !fondosPorDivisa.containsKey(divisa);
    }
    
    public boolean tieneFondosDeDivisa(Divisa divisa) {
		return !(fondosPorDivisa.get(divisa) > 0.0);
	}
    
    public Transaccion finalizarConversion(Transaccion transaccion, double montoInicial){
		if(transaccion.isRechazado()) 
			transaccion.getTarjetaOrigen().anadirTransaccionRechazada();
		
		if(!transaccion.isPendiente()) 
			return null;
		
		Divisa divisaOrigen = transaccion.getDivisa();
		double fondosOrigen = transaccion.getCanalObjetivo().getFondos(divisaOrigen);
		
		Divisa divisaDestino = transaccion.getTarjetaObjetivo().getDivisa();
		double fondosDestino = transaccion.getCanalObjetivo().getFondos(divisaDestino);
		
		transaccion.getTarjetaOrigen().sacarDinero(montoInicial);//Sacando dinero de la tarjeta de origen
		transaccion.getCanalObjetivo().setFondos(divisaOrigen, fondosOrigen + montoInicial);//Ingresando el dinero de la divisa de origen al canal
		transaccion.getCanalObjetivo().setFondos(divisaDestino, fondosDestino - transaccion.getCantidad());//Sacando dinero de la divisa de destino del canal
		transaccion.getTarjetaObjetivo().introducirDinero(transaccion.getCantidad());//Ingresando la divisa de destino a la tarjeta de destino
		transaccion.setPendiente(false);
				
		return transaccion;
	}

	public static ArrayList<Canal> seleccionarCanal(Divisa divisa, boolean retirar){ //Encuentra los canales apropiados para la transaccion, en el contexto de la funcionalidad "retirar o depositar dinero"
		ArrayList<Canal> retorno = new ArrayList<Canal>();
		if(retirar) retorno.addAll(Banco.getCanales());
		else{
			for(Canal c : Banco.getCanales()){
				if(c.tieneDivisa(divisa)){
					if(c.tieneFondosDeDivisa(divisa)){  //Estos chequeos deben hacerce uno después del otro, de otra manera, existe la posibilidad de que el programa lanze un error en caso de que el canal no tenga la divisa
						retorno.add(c);
					}
				}
			}
		}
		return retorno;
	}
}
