package gestorAplicacion.infraestructura;

import java.util.EnumMap;

import gestorAplicacion.entidades_de_negocio.*;
public class Canal {
	private String tipoCanal;
	/*	Tipos de Canales:
	 *	- Cajero Automatico: Solo tienen una divisa disponible
	 *	- Sucursal Fisica: Tienen todas las divisas disponibles
	 *	- Sucursal en Linea: Tienen todas las divisas disponibles
	 *	- Corresponsal Bancario: Solo tiene divisa del pais de origen(Peso colombiano) y las divisas internacionales mas importantes
	 *	(Dolar y Euro)
	 * */
	private double impuesto;
	private EnumMap<Divisa, Double> fondosPorDivisa = new EnumMap<>(Divisa.class); 

	//Los fondos deben ser proporcionados en el orden en que estan declaradas las divisas
	//Los tipos de canales que no tienen algunas divisas, deben inicializar sus fondos respectivos en 0.0
    public Canal(String tipoCanal, double... fondos) {
    	this.tipoCanal = tipoCanal;
    	for(int i = 0; i < Divisa.values().length; i++) {
    		Divisa divisa = Divisa.values()[i];
    		double fondo = fondos[i];
    		this.fondosPorDivisa.put(divisa, fondo);
    	}
    }
    
    //Inicializar los fondos en un valor arbitrario
    public Canal(String tipoCanal) {
    	this.tipoCanal = tipoCanal;
    	for (Divisa divisa : Divisa.values()) {
            this.fondosPorDivisa.put(divisa, 5000.0);
        }
    }
    
    public void setFondos(Divisa divisa, double monto) {
        this.fondosPorDivisa.put(divisa, monto);
    }
    
    public double getFondos(Divisa divisa) {
        return this.fondosPorDivisa.get(divisa);
    }
    
    public EnumMap<Divisa, Double> getFondos() {
        return fondosPorDivisa;
    }
}
