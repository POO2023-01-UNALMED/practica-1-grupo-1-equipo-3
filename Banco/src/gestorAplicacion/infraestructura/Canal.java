package gestorAplicacion.infraestructura;

import java.util.EnumMap;

import gestorAplicacion.entidades_de_negocio.*;
public class Canal {
	private String tipoCanal;
	/*	Tipos de Canales:
	 *	- Cajero Automatico: Solo tienen una divisa disponible. Estos canales no pueden cambiar divisas. Solo se puede retirar de ellos
	 *	- Sucursal Fisica: Tienen todas las divisas disponibles. Pueden cambiar todas las divisas.
	 *	- Sucursal en Linea: Tienen todas las divisas disponibles. Pueden cambiar todas las divisas. 
	 *	- Corresponsal Bancario: Solo tienen 3 divisas, dos de ellas son las monedas internacionales mas importantes (DOLAR y EURO) y una tercera que puede ser cualquier otra.
	 *	Por lo que solo se pueden cambiar esas 3 divisas en estos canales
	 * */
	private float impuesto;
	private EnumMap<Divisa, Double> fondosPorDivisa = new EnumMap<>(Divisa.class); 

	//Los fondos deben ser proporcionados en el orden en que estan declaradas las divisas
    public Canal(String tipoCanal, float impuesto, double... fondos) {
    	this.tipoCanal = tipoCanal;
    	this.impuesto = impuesto;
    	
    	for(int i = 0; i < Divisa.values().length && i < fondos.length; i++) {
    		Divisa divisa = Divisa.values()[i];
    		double fondo = fondos[i];
    		this.fondosPorDivisa.put(divisa, fondo);
    	}
    	
    	Banco.agregarCanal(this);
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
		return "Canal: %s\nTasa de impuestos: %s\n".formatted(tipoCanal,impuesto);
	}
	
    public boolean tieneDivisa(Divisa divisa) {
        return fondosPorDivisa.containsKey(divisa);
    }
    
    public boolean tieneFondosDeDivisa(Divisa divisa) {
    	if(fondosPorDivisa.get(divisa) > 0.0) 
    		return true;
        return false;
    }
}
