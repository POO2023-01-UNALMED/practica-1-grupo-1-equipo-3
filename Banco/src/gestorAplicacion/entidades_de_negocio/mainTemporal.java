package gestorAplicacion.entidades_de_negocio;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import gestorAplicacion.infraestructura.Banco;
import gestorAplicacion.infraestructura.Canal;
import gestorAplicacion.tarjetas.TarjetaDebito;
import gestorAplicacion.tarjetas.Tarjeta;
import gestorAplicacion.tarjetas.TarjetaCredito;

public class mainTemporal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner scanner = new Scanner(System.in);
		setup();
		
		while(true) {
			System.out.println("Elija el usuario:");
			for(Cliente c : Banco.getClientes()) {
				System.out.println(Banco.getClientes().indexOf(c)+1 + ". " + c.getNombre());
			}
			System.out.println(Banco.getClientes().size()+1 + ". Para salir");
			int respuesta1 = scanner.nextInt();
			scanner.nextLine();
			if(respuesta1 == Banco.getClientes().size() + 1) {
				break;
			}
			Cliente clienteActual; //Es el cliente escogido por el usuario
			if(respuesta1 >= 0 && respuesta1 <= Banco.getClientes().size()){
				clienteActual = Banco.getClientes().get(respuesta1-1);
			} else{
				continue; //Si el valor ingresado no corresponde a un cliente, el programa vuelve al principio
			}
			while(true) { // el loop principal que se ejecuta para cada cliente
				System.out.println("1. Para ver facturas\n2. Para ver las tarjetas disponibles\n3. Para pagar una factura\n4. Cambiar Divisas\n5. Solicitar una tarjeta de crédito\n6. Salir");
				String entrada2 = scanner.nextLine();//Se lee la elección del usuario
				if(entrada2.equals("1")){
					if(clienteActual.listarFacturas().isEmpty()){
						System.out.println("No hay facturas por pagar");
					} else {
						for(Factura f : clienteActual.getFactura()){
							System.out.println(f);
						}
					}
				}else if (entrada2.equals("2")){
					for(Tarjeta t : clienteActual.getTarjetasCredito()){
						System.out.println(t);
					}
					for(Tarjeta t : clienteActual.getTarjetasDebito()){
						System.out.println(t);
					}
				}else if(entrada2.equals("3")){
					if(clienteActual.listarFacturas().isEmpty()){
						System.out.println("No hay facturas por pagar");
						continue;
					} else {
						System.out.println("Escoja la factura que desea pagar");
						for(Factura f : clienteActual.listarFacturas()){
							System.out.println(clienteActual.listarFacturas().indexOf(f)+1 + ". " + f);
						}
					}
					int entrada3 = scanner.nextInt()-1;
					Factura factura = clienteActual.listarFacturas().get(entrada3);
					System.out.println("Ingrese la tarjeta que quiere utilizar");
					ArrayList<Tarjeta> tarjetasPosibles = clienteActual.listarTarjetas(factura);
					for(Tarjeta t : tarjetasPosibles){
						System.out.println(tarjetasPosibles.indexOf(t)+1 + ". " + t);
					}
					int entrada4 = scanner.nextInt()-1;
					Tarjeta tarjeta = tarjetasPosibles.get(entrada4);
					System.out.println("Ingrese la cantidad de dinero que desea transferir");
					double monto = scanner.nextDouble();
					scanner.nextLine();
					Transaccion transaccion = factura.generarTransaccion(monto, tarjeta);
					if(transaccion.isRechazado()){ //En caso de que la transacción fue rechazada, se notifica al usuario, y se vuelve al principio
						System.out.println("La transacción fue rechazada");
						continue;
					}
					System.out.println("La transacción ha sido generada. Es la siguiente: " + transaccion);
					String entrada5;
					while(true){
						System.out.println("Quiere continuar con el proceso?\n1. Si\n2. No");
						entrada5 = scanner.nextLine();
						if(entrada5.equals("2") || entrada5.equals("1")){
							break;
						}
					}
					if(entrada2.equals("2")){
						continue;
					}
					Factura facturaNueva = transaccion.pagarFactura();
					clienteActual.getFactura().set(clienteActual.getFactura().indexOf(factura), facturaNueva); //Remplaza la factura anterior con la factura nueva
					
				}else if(entrada2.equals("4")){
					System.out.println("Escoja las divisas que desea cambiar");
					System.out.println("Escoja la divisa de origen:");
					
					for(Divisa divisa : Divisa.values()){//Recorre un array de las divisas
						System.out.println(divisa.ordinal() + 1 + ". " + divisa);
					}
					int entrada3 = scanner.nextInt() - 1;//Obtiene el numero de la divisa escogida
					Divisa divisaOrigen = Divisa.values()[entrada3]; //Almacena la referencia de la divisa escogida
					
					System.out.println("Escoja la divisa de destino:");
					for(Divisa divisa : Divisa.values()){
						System.out.println(divisa.ordinal() + 1 + ". " + divisa);
					}
					int entrada4 = scanner.nextInt() - 1;
					Divisa divisaDestino = Divisa.values()[entrada4];
					
					//Si el valor de retorno es null
					if(Objects.isNull(clienteActual.escogerDivisas(divisaOrigen, divisaDestino))) {
						System.out.println("No tiene tarjetas que cumplan con la divisa de origen");
						break;
					}
					else {
						ArrayList<Divisa> divisas = clienteActual.escogerDivisas(divisaOrigen, divisaDestino);
						ArrayList<Tarjeta> tarjetas = clienteActual.listarTarjetas(divisas);
						
						ArrayList<Tarjeta> tarjetasEscogidas = new ArrayList<Tarjeta>();
						
						System.out.println("Escoja la tarjeta con la divisa de origen:\n");
						for(Tarjeta tarjeta: tarjetas) {
							System.out.println(tarjetas.indexOf(tarjeta) + 1 + ". " + tarjeta);
						}
						
						int entrada5 = scanner.nextInt() - 1;
						tarjetasEscogidas.add(tarjetas.get(entrada5));
						
						//La tarjeta de origen que escoja el usuario debe corresponder con la divisa de origen (el orden debe ser el mismo) 
						if(!Divisa.verificarOrden(divisas, tarjetasEscogidas.get(0).getDivisa(), "Origen")) {
							System.out.println("Debes escoger una tarjeta de origen con una divisa acorde a la divisa que quieres cambiar\n");
							break;
						}
						
						System.out.println("Escoja la tarjeta con la divisa de Destino:\n");
						for(Tarjeta tarjeta: tarjetas) {
							if(tarjeta instanceof TarjetaCredito)//La divisa de destino solo puede ser una tarjeta debito 
								continue;
							System.out.println(tarjetas.indexOf(tarjeta) + 1 + ". " + tarjeta);
						}
						
						int entrada6 = scanner.nextInt() - 1;
						tarjetasEscogidas.add(tarjetas.get(entrada6));
						
						if(Divisa.verificarOrden(divisas, tarjetasEscogidas.get(1).getDivisa(), "Destino")) {
							System.out.println("Debes escoger una tarjeta de destino con una divisa acorde a la divisa que quieres obtener\n");
							break;
						}//Eventualmente hay que volver esta parte un ciclo, para que le permita escojer al usuario nuevamente otra tarjeta
						
						
						ArrayList<Canal> listaCanales = clienteActual.listarCanales(divisas);
						System.out.println("Escoja el canal donde realizará el proceso");
						for(Canal canal: listaCanales) {
							System.out.println(listaCanales.indexOf(canal) + 1 + ". " + canal);
						}
						
						int entrada7 = scanner.nextInt() - 1;
						Canal canalEscogido = listaCanales.get(entrada7);
						System.out.println(canalEscogido);
						
						break;
					}
				}else if(entrada2.equals("5")){
					System.out.println("Viendo transferencias del usuario...");
					ArrayList<Transaccion> historial = clienteActual.revisarHistorialCreditos();
					int puntajeTentativo = Banco.calcularPuntaje(historial);
					ArrayList<Tarjeta> TarjetasBloqueadas = Tarjeta.TarjetasBloqueadas(clienteActual);
					ArrayList<Tarjeta> TarjetasActivas = Tarjeta.TarjetasNoBloqueadas(clienteActual);
					int puntajeDefinitivo = Factura.modificarPuntaje(TarjetasBloqueadas, TarjetasActivas, clienteActual, puntajeTentativo);
					System.out.println("Escoga la divisa que quiere para su tarjeta de crédito:");
					for(Divisa d : Divisa.getDivisas()){
						System.out.println(Divisa.getDivisas().indexOf(d)+1 + ". " + d.name());
					}
					int entrada3 = scanner.nextInt();
					Divisa divisa = Divisa.getDivisas().get(entrada3);

					
				} else if(entrada2.equals("6")){
					break;
				}
			}
			
		}
		
		scanner.close();
	}

	public static void setup(){ //Función que inicializa algunos objetos para comenzar a experimentar
		//CLIENTES
		Cliente cliente1 = new Cliente("Dario", 1);
		Cliente cliente2 = new Cliente("Esteban", 2);
		Cliente cliente3 = new Cliente("Marta", 3);
		
		//TARJETAS DEBITO
		TarjetaDebito tarjetaDebito1 = new TarjetaDebito(123456, Divisa.DOLAR, 3000);
		TarjetaDebito tarjetaDebito2 = new TarjetaDebito(234567, Divisa.EURO, 0);
		TarjetaDebito tarjetaDebito3 = new TarjetaDebito(345678, Divisa.RUBLO_RUSO, 200);
		TarjetaDebito tarjetaDebito4 = new TarjetaDebito(456789, Divisa.YEN_JAPONES, 970);
		TarjetaDebito tarjetaDebito5 = new TarjetaDebito(567890, Divisa.PESO_COLOMBIANO, 8000);
		TarjetaDebito tarjetaDebito6 = new TarjetaDebito(678901, Divisa.DOLAR, 7000);
		TarjetaDebito tarjetaDebito7 = new TarjetaDebito(789012, Divisa.EURO, 2450);
		TarjetaDebito tarjetaDebito8 = new TarjetaDebito(890123, Divisa.RUBLO_RUSO, 1370);
		TarjetaDebito tarjetaDebito9 = new TarjetaDebito(901234, Divisa.YEN_JAPONES, 480);
		TarjetaDebito tarjetaDebito10 = new TarjetaDebito(123456789, Divisa.PESO_COLOMBIANO, 2000);
		
		//TARJETAS CREDITO
		TarjetaCredito tarjetaCredito1 = new TarjetaCredito(987456, Divisa.DOLAR, 3000, 1.5);
		TarjetaCredito tarjetaCredito2 = new TarjetaCredito(876543, Divisa.EURO, 2000, 2.0);
		TarjetaCredito tarjetaCredito3 = new TarjetaCredito(765432, Divisa.RUBLO_RUSO, 1500, 0.8);
		TarjetaCredito tarjetaCredito4 = new TarjetaCredito(654321, Divisa.YEN_JAPONES, 1850, 0.2);
		TarjetaCredito tarjetaCredito5 = new TarjetaCredito(543210, Divisa.PESO_COLOMBIANO, 6000, 1.25);
		TarjetaCredito tarjetaCredito6 = new TarjetaCredito(432109, Divisa.DOLAR, 500, 0.75);
		TarjetaCredito tarjetaCredito7 = new TarjetaCredito(321098, Divisa.EURO, 1750, 0.1);
		TarjetaCredito tarjetaCredito8 = new TarjetaCredito(210987, Divisa.RUBLO_RUSO, 700, 0.3);
		TarjetaCredito tarjetaCredito9 = new TarjetaCredito(109876, Divisa.YEN_JAPONES, 300, 0.99);
		TarjetaCredito tarjetaCredito10 = new TarjetaCredito(987654321, Divisa.PESO_COLOMBIANO, 1000, 5.0);
		
		TarjetaDebito tarjetafac = new TarjetaDebito(666, Divisa.DOLAR, 10);

		cliente1.agregarTarjetasDebito(tarjetaDebito1, tarjetaDebito2, tarjetaDebito3, tarjetaDebito5);
		cliente1.agregarTarjetasCredito(tarjetaCredito1, tarjetaCredito2, tarjetaCredito7, tarjetaCredito9);

		TarjetaDebito tarjeta1 = new TarjetaDebito(1, Divisa.DOLAR, 1000);
		TarjetaDebito tarjeta2 = new TarjetaDebito(1, Divisa.EURO, 10);
		cliente1.agregarTarjetasDebito(tarjeta1, tarjeta2);

		Factura factura1 = new Factura(cliente1, 100.0, 5, tarjetafac);
		Factura factura2 = new Factura(cliente1, 90, 8, tarjetafac);
		
		//CANALES
		
		//Sucursal fisica
		Canal sucursalFisica1 = new Canal("Sucursal Fisica",(float)2.0, 22500, 12370, 800, 700, 30000);
		
		//Cajero Automático
		Canal cajero1 = new Canal("Cajero",(float)0.5);
		cajero1.setFondos(Divisa.PESO_COLOMBIANO, 12000.0);
		
		//Corresponal Bancario
		Canal corresponsal1 = new Canal("Corresponsal Bancario", (float)1.0);
		corresponsal1.setFondos(Divisa.DOLAR, 20000.0);
		corresponsal1.setFondos(Divisa.EURO, 10000.0);
		corresponsal1.setFondos(Divisa.PESO_COLOMBIANO, 40000.0);
		
		//Canal tipo Cajero Automático
		Canal cajero2 = new Canal("Cajero", (float) 1.0);
		cajero2.setFondos(Divisa.DOLAR, 8000.0);

		//Canal tipo Sucursal Física
		Canal sucursalFisica2 = new Canal("Sucursal Física", (float) 1.5, 4000.0, 6000.0, 2000.0, 1500.0, 8000.0);

		//Canal tipo Sucursal en Línea
		Canal sucursalVirtual1 = new Canal("Sucursal en Línea", (float) 2.5, 5100.0, 8900.0, 0.0, 1200.0, 3700.0);

		//Canal tipo Corresponsal Bancario
		Canal corresponsal2 = new Canal("Corresponsal Bancario", (float) 0.8);
		corresponsal2.setFondos(Divisa.DOLAR, 15000.0);
		corresponsal2.setFondos(Divisa.EURO, 8000.0);
		corresponsal2.setFondos(Divisa.RUBLO_RUSO, 35000.0);
		
	}

}
