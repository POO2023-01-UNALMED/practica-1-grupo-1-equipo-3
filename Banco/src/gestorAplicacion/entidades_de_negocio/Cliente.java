package gestorAplicacion.entidades_de_negocio;

import java.util.Scanner;

import gestorAplicacion.infraestructura.*;
import gestorAplicacion.entidades_de_negocio.Factura;
import gestorAplicacion.tarjetas.*;

import java.util.ArrayList;

public class Cliente {
	private String nombre;
	private int Id;
	private ArrayList<TarjetaDebito> tarjetasDebito = new ArrayList<TarjetaDebito>();
	private ArrayList<TarjetaCredito> tarjetasCredito = new ArrayList<TarjetaCredito>();
	private ArrayList<Factura> facturas = new ArrayList<Factura>();
	private int bonoActual = 0;
	
	public Cliente(String nombre, int noDeIdentificacion) {
		this.nombre = nombre;
		this.Id = noDeIdentificacion;
		Banco.agregarCliente(this);
	}
	
	
	//Metodos de la instancia
	
	@Override
	public String toString(){
		return "\n\nNombre: %s\nIdentificacion: %s\nNúmero de Tarjetas de Debito: %s\nNúmero de Tarjetas de Credito: %s\nBono Actual: %s".formatted(nombre, Id, tarjetasDebito.size(), tarjetasCredito.size(), bonoActual);
	}

	//Getters & Setters
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getNoDeIdentificacion() {
		return Id;
	}

	public void setNoDeIdentificacion(int noDeIdentificacion) {
		this.Id = noDeIdentificacion;
	}

	public ArrayList<TarjetaDebito> getTarjetasDebito() {
		return tarjetasDebito;
	}

	public void agregarTarjetasDebito(TarjetaDebito tarjetaDebito) {
		this.tarjetasDebito.add(tarjetaDebito);
	}
	
	public void eliminarTarjetasDebito(TarjetaDebito tarjetaDebito) {
		this.tarjetasDebito.remove(tarjetaDebito);
	}

	public ArrayList<TarjetaCredito> getTarjetasCredito() {
		return tarjetasCredito;
	}

	public void agregarTarjetasCredito(TarjetaCredito tarjetaCredito) {
		this.tarjetasCredito.add(tarjetaCredito);
	}
	
	public void eliminarTarjetasCredito(TarjetaCredito tarjetaCredito) {
		this.tarjetasCredito.add(tarjetaCredito);
	}

	public ArrayList<Factura> getFactura() {
		return facturas;
	}

	public void agregarFactura(Factura factura) {
		this.facturas.add(factura);
	}
	
	public void eliminarFactura(Factura factura) {
		this.facturas.remove(factura);
	}

	public int getBonoActual() {
		return bonoActual;
	}

	public void setBonoActual(int bonoActual) {
		this.bonoActual = bonoActual;
	}
	
	
	//Metodos de las instancias
	
//	public void sesion() {
//		Scanner scanner = new Scanner(System.in);
//		while(true) {
//			System.out.println("1. Para hacer una transferencia\n2. Obtener una tarjeta de débito\n3. Para solicitar una tarjeta de crédito\n4. Para ver las tarjetas disponibles\n5. Para salir");
//			String entrada1 = scanner.nextLine();
//			if(entrada1.equals("1")) { // inicia proceso para hacer una transacción
//				ArrayList<Cliente> otrosUsuarios = Banco.otrosUsuarios(this);
//				
//				System.out.println("Decida desde cual tarjeta hacer la transaccion"); //Determina tarjetaOrigen
//				for(Tarjeta t : tarjetas) {
//					System.out.println(tarjetas.indexOf(t)+1 + " " + t.verTarjeta());
//				}
//				int entrada2 = scanner.nextInt()-1;
//				scanner.nextLine();
//				Tarjeta tarjetaOrigen = tarjetas.get(entrada2);
//				
//				System.out.println("Escoja el usuario al que quiere hacer la transacción\n"); //Determina clienteObjetivo
//				for(Cliente c : otrosUsuarios) {
//					System.out.println(otrosUsuarios.indexOf(c)+1 + " " + c.getNombre());
//				}
//				int entrada3 = scanner.nextInt()-1;
//				scanner.nextLine();
//				Cliente clienteObjetivo = otrosUsuarios.get(entrada3);
//				ArrayList<TarjetaDebito> Tarjetas = Banco.tarjetasDebito(clienteObjetivo);
//				
//				System.out.println("Escoga la tarjeta a la cual desea transferir"); // Determina tarjetaObjetivo
//				for(TarjetaDebito t : Tarjetas) {
//					System.out.println(Tarjetas.indexOf(t)+1 + " " + t.getNoTarjeta() + " " + t.getDivisa().getMoneda());
//				}
//				int entrada4 = scanner.nextInt() - 1;
//				scanner.nextLine();
//				TarjetaDebito tarjetaObjetivo = Tarjetas.get(entrada4);
//				
//				System.out.println("Ingrese la cantidad total de dinero a transferir");
//				double cantidad = scanner.nextDouble();
//				scanner.nextLine();
//				Transaccion transaccion = new Transaccion(this, clienteObjetivo, tarjetaOrigen, tarjetaObjetivo, cantidad);
//				if(transaccion.isRechazado()) {
//					System.out.println("La transacción fue rechazada");
//				}else {
//					System.out.println("La transacción fue exitosa");
//				}
//			} else if (entrada1.equals("2")) { // inicia proceso para obtener nueva tarjeta de debito
//				int noTarjeta; 
//				while(true) {
//					noTarjeta = (int) Math.floor(Math.random()*(999999999 - 100000000 + 1) + 100000000); // genera un número aleatorio entre 999999999 y 100000000. Este será el número de la tarjeta, a menos de que ya está siendo usado (lo cual es improbable)
//					if(!Banco.numeroExistente(noTarjeta)) {
//						break;
//					}
//				}
//				System.out.println("El número de su nueva tarjeta sera " + noTarjeta);
//				System.out.println("Ingrese la divisa");
//				for (int i = 0; i < Divisa.getDivisas().length; i++) {
//					  System.out.println(i+1 + " " + Divisa.getDivisas()[i].getMoneda());
//				}
//				int entrada2 = scanner.nextInt()-1;
//				scanner.nextLine();
//				System.out.println("Añadiendo la tarjeta a las tarjetas del usuario...");
//				this.tarjetas.add(new TarjetaDebito(noTarjeta, Divisa.getDivisas()[entrada2], 0)); //añade la tarjeta solicitada a las tarjetas del usuario
//				
//			}else if (entrada1.equals("3")) {
//				
//			}else if (entrada1.equals("4")) {
//				for(Tarjeta t : this.tarjetas) {
//					System.out.println(t.verTarjeta());
//				}
//			}else if(entrada1.equals("5")) {
//				break;
//			}
//			
//		}
//		//scanner.close();
//	}
	
		
}
