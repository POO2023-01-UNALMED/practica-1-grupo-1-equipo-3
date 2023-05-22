package uiMain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import gestorAplicacion.entidades_de_negocio.Cliente;
import gestorAplicacion.entidades_de_negocio.Divisa;
import gestorAplicacion.entidades_de_negocio.Factura;
import gestorAplicacion.entidades_de_negocio.Transaccion;
import gestorAplicacion.infraestructura.Banco;
import gestorAplicacion.infraestructura.Canal;
import gestorAplicacion.tarjetas.TarjetaDebito;
import gestorAplicacion.tarjetas.Tarjeta;
import gestorAplicacion.tarjetas.TarjetaCredito;

public class mainTemporal implements Serializable{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner scanner = new Scanner(System.in);
		setup();
		
		while(true) {
			System.out.println("Elija el usuario:");
			for(Cliente c : Banco.getClientes()) {
				System.out.println(Banco.getClientes().indexOf(c)+1 + ". " + c.nombre);
			}
			System.out.println(Banco.getClientes().size()+1 + ". Para salir");
			int respuesta1 = scanner.nextInt();
			scanner.nextLine();
			if(respuesta1 == Banco.getClientes().size() + 1) {
				break;
			}
			Cliente clienteActual; //Es el cliente escogido por el usuario
			if(respuesta1 > 0 && respuesta1 <= Banco.getClientes().size()){
				clienteActual = Banco.getClientes().get(respuesta1-1);
			} else{
				continue; //Si el valor ingresado no corresponde a un cliente, el programa vuelve al principio
			}
			label:
			while(true) { // el loop principal que se ejecuta para cada cliente
				System.out.println("1. Para ver facturas\n2. Para ver las tarjetas disponibles\n3. Para pagar una factura\n4. Cambiar Divisas\n5. Solicitar una tarjeta de crédito\n6. Para hacer una transferencia\n7. Para retirar dinero\n8. Para deshacer una transacción\n9. Para ver peticiones\n10. Para salir");
				String entrada2 = scanner.nextLine();//Se lee la elección del usuario
				switch (entrada2) {
					case "1":
						if (clienteActual.listarFacturas().isEmpty()) {
							System.out.println("No hay facturas por pagar");
						} else {
							for (Factura f : clienteActual.getFactura()) {
								System.out.println(f);
							}
						}
						break;
					case "2":
						for (Tarjeta t : clienteActual.getTarjetasCredito()) {
							System.out.println(t);
						}
						for (Tarjeta t : clienteActual.getTarjetasDebito()) {
							System.out.println(t);
						}
						break;
					case "3": {
						if (clienteActual.listarFacturas().isEmpty()) {
							System.out.println("No hay facturas por pagar");
							continue;
						} else {
							System.out.println("Escoja la factura que desea pagar");
							for (Factura f : clienteActual.listarFacturas()) {
								System.out.println(clienteActual.listarFacturas().indexOf(f) + 1 + ". " + f);
							}
						}
						int entrada3 = scanner.nextInt() - 1;
						Factura factura = clienteActual.listarFacturas().get(entrada3);
						System.out.println("Ingrese la tarjeta que quiere utilizar");
						ArrayList<Tarjeta> tarjetasPosibles = clienteActual.listarTarjetas(factura);
						for (Tarjeta t : tarjetasPosibles) {
							System.out.println(tarjetasPosibles.indexOf(t) + 1 + ". " + t);
						}
						int entrada4 = scanner.nextInt() - 1;
						Tarjeta tarjeta = tarjetasPosibles.get(entrada4);
						System.out.println("Ingrese la cantidad de dinero que desea transferir");
						double monto = scanner.nextDouble();
						scanner.nextLine();
						Transaccion transaccion = factura.generarTransaccion(monto, tarjeta);
						if (transaccion.isRechazado()) { //En caso de que la transacción fue rechazada, se notifica al usuario, y se vuelve al principio
							System.out.println("La transacción fue rechazada");
							continue;
						}
						System.out.println("La transacción ha sido generada. Es la siguiente: " + transaccion);
						String entrada5;
						do {
							System.out.println("Quiere continuar con el proceso?\n1. Si\n2. No");
							entrada5 = scanner.nextLine();
						} while (!entrada5.equals("2") && !entrada5.equals("1"));
						Factura facturaNueva = transaccion.pagarFactura();
						clienteActual.getFactura().set(clienteActual.getFactura().indexOf(factura), facturaNueva); //Remplaza la factura anterior con la factura nueva


						break;
					}
					case "4": {
						System.out.println("Escoja las divisas que desea cambiar");
						System.out.println("Escoja la divisa de origen:\n");

						for (Divisa divisa : Divisa.values()) {//Recorre un array de las divisas
							System.out.println(divisa.ordinal() + 1 + ". " + divisa);
						}
						int entrada3 = scanner.nextInt() - 1;//Obtiene el numero de la divisa escogida

						Divisa divisaOrigen = Divisa.values()[entrada3]; //Almacena la referencia de la divisa escogida


						System.out.println("Escoja la divisa de destino:\n");
						for (Divisa divisa : Divisa.values()) {
							System.out.println(divisa.ordinal() + 1 + ". " + divisa);
						}
						int entrada4 = scanner.nextInt() - 1;
						Divisa divisaDestino = Divisa.values()[entrada4];
						
						//Si el cliente escoge la misma divisa de Origen y de destino
						if(divisaOrigen.equals(divisaDestino)) {
							System.out.println("Debes escoger dos divisas distintas para hacer la conversión\n");
							break label;
						}
						System.out.println(divisaOrigen.name());
						
						//Si el cliente no tiene ninguna tarjeta de cada divisa respectivamente
						if (Objects.isNull(clienteActual.escogerDivisas(divisaOrigen, divisaDestino))) {
							System.out.println("No tiene tarjetas que cumplan con la divisa de origen\n");
						} else {
							ArrayList<Divisa> divisas = clienteActual.escogerDivisas(divisaOrigen, divisaDestino);
							ArrayList<Tarjeta> tarjetas = clienteActual.listarTarjetas(divisas);

							ArrayList<Tarjeta> tarjetasEscogidas = new ArrayList<>();

							System.out.println("Escoja la tarjeta con la divisa de origen:\n");
							for (Tarjeta tarjeta : tarjetas) {
								System.out.println(tarjetas.indexOf(tarjeta) + 1 + ". " + tarjeta);
							}

							int entrada5 = scanner.nextInt() - 1;
							tarjetasEscogidas.add(tarjetas.get(entrada5));

							//La tarjeta de origen que escoja el usuario debe corresponder con la divisa de origen (el orden debe ser el mismo)
							if (!Divisa.verificarOrden(divisas, tarjetasEscogidas.get(0).getDivisa(), "Origen")) {
								System.out.println("Debes escoger una tarjeta de origen con una divisa acorde a la divisa que quieres cambiar\n");
								break label;
							}

							System.out.println("Escoja la tarjeta con la divisa de Destino:\n");
							for (Tarjeta tarjeta : tarjetas) {
								if (tarjeta instanceof TarjetaCredito)//La divisa de destino solo puede ser una tarjeta debito
									continue;
								System.out.println(tarjetas.indexOf(tarjeta) + 1 + ". " + tarjeta);
							}

							int entrada6 = scanner.nextInt() - 1;
							tarjetasEscogidas.add(tarjetas.get(entrada6));

							if (Divisa.verificarOrden(divisas, tarjetasEscogidas.get(1).getDivisa(), "Destino")) {
								System.out.println("Debes escoger una tarjeta de destino con una divisa acorde a la divisa que quieres obtener\n");
								break label;
							}//Eventualmente hay que volver esta parte un ciclo en uiMain, para que le permita escojer al usuario nuevamente otra tarjeta


							ArrayList<Canal> listaCanales = clienteActual.listarCanales(divisas);
							System.out.println("Escoja el canal donde realizará el proceso");
							for (Canal canal : listaCanales) {
								System.out.println(listaCanales.indexOf(canal) + 1 + ". " + canal);
							}

							int entrada7 = scanner.nextInt() - 1;
							Canal canalEscogido = listaCanales.get(entrada7);
							
							double montoInicial;
							
							System.out.println("Por favor digita el monto a convertir:\n");

							while (true) {
								montoInicial = scanner.nextDouble();
								if (montoInicial <= 0) {
									System.out.println("El monto debe ser mayor a 0");
									continue;
								}
								break;
							}
							
							ArrayList<Double> conversion = Divisa.convertirDivisas(divisas, canalEscogido, montoInicial); 
							Transaccion transaccion = Transaccion.crearTransaccion(divisas, montoInicial, conversion, canalEscogido, tarjetasEscogidas, clienteActual);
							
							transaccion = canalEscogido.finalizarConversion(transaccion, montoInicial);
							System.out.println(transaccion);

						}
						break label;
					}
					case "5": {
						System.out.println("Viendo transferencias del usuario...");
						ArrayList<Transaccion> historial = clienteActual.revisarHistorialCreditos();
						int puntajeTentativo = Banco.calcularPuntaje(historial);
						ArrayList<Tarjeta> TarjetasBloqueadas = Tarjeta.TarjetasBloqueadas(clienteActual);
						ArrayList<Tarjeta> TarjetasActivas = Tarjeta.TarjetasNoBloqueadas(clienteActual);
						int puntajeDefinitivo = Factura.modificarPuntaje(TarjetasBloqueadas, TarjetasActivas, clienteActual, puntajeTentativo);
						System.out.println("Escoga la divisa que quiere para su tarjeta de crédito:");
						for (Divisa divisa : Divisa.values()) {
							System.out.println(divisa.ordinal() + 1 + ". " + divisa);
						}
						int entrada3 = scanner.nextInt() - 1;
						Divisa divisa = Divisa.values()[entrada3];
						ArrayList<TarjetaCredito> tarjetasDisponibles = TarjetaCredito.tarjetasDisponibles(puntajeDefinitivo, divisa);
						int i = 0;
						System.out.printf("Su puntaje total es: %s. Por favor, escoga la tarjeta de crédito que desea:\n%n", puntajeDefinitivo);
						for (TarjetaCredito t : tarjetasDisponibles) {
							System.out.printf("%s. %sPuntos: %s\n%n", i + 1, t.toString(), i * 50);
							i++;
						}
						System.out.printf("%s. Para salir%n", tarjetasDisponibles.size() + 1);
						int entrada4 = scanner.nextInt() - 1;
						int bono = puntajeDefinitivo - entrada4 * 50;
						if (entrada4 == tarjetasDisponibles.size()) continue;
						else
							TarjetaCredito.anadirTarjetaCredito(tarjetasDisponibles.get(entrada4), clienteActual, bono);

						scanner.nextLine();
						break;
					}
					case "6": {
						System.out.println("Estas son las tarjetas débito que tienes disponibles:\n");
						for (TarjetaDebito tarjeta : clienteActual.getTarjetasDebito()) {
							System.out.println("Numero de tarjeta: " + tarjeta.getNoTarjeta());
							System.out.println("Divisa de la tarjeta: " + tarjeta.getDivisa());
							System.out.println("Saldo de la tarjeta: " + tarjeta.getSaldo());
							System.out.println(tarjeta.getEstado());
							System.out.println();
						}
						int eleccion_de_tarjeta_debito;
						TarjetaDebito tarjeta_de_origen;

						while (true) {
							eleccion_de_tarjeta_debito = scanner.nextInt();

							if (eleccion_de_tarjeta_debito > 0 && eleccion_de_tarjeta_debito <= clienteActual.getTarjetasDebito().size()) {
								tarjeta_de_origen = clienteActual.getTarjetasDebito().get(eleccion_de_tarjeta_debito - 1);
								break;
							} else {
								System.out.println("Por favor, elige un número válido de tarjeta.");
							}
						}
						System.out.println(tarjeta_de_origen);
						Cliente clienteObjetivo;
						while (true) {
							System.out.println("Elija el usuario al que le desea hacer la transaccion:");
							for (Cliente c : Banco.getClientes()) {
								System.out.println(Banco.getClientes().indexOf(c) + 1 + ". " + c.nombre);
							}
							int eleccion_cliente_objetivo = scanner.nextInt();
							if (eleccion_cliente_objetivo > 0 && eleccion_cliente_objetivo <= Banco.getClientes().size()) {
								clienteObjetivo = Banco.getClientes().get(eleccion_cliente_objetivo - 1);
								break;
							}
						}
						ArrayList<TarjetaDebito> tarjetasObjetivo = new ArrayList<>(); //tarjetasObjetivo guarda las tarjetas a las cuales se puede hacer una transacción
						for(TarjetaDebito t : clienteObjetivo.getTarjetasDebito()){
							if(!t.equals(tarjeta_de_origen)){
								tarjetasObjetivo.add(t);
							}
						}
						int eleccion_de_tarjeta_debito_objetivo;
						TarjetaDebito tarjeta_Objetivo;
						for (TarjetaDebito tarjeta : tarjetasObjetivo) { //Mostrando las opciones para transferir del cliente objetivo.
							System.out.println("Numero de tarjeta: " + tarjeta.getNoTarjeta());
							System.out.println("Divisa de la tarjeta: " + tarjeta.getDivisa());
							System.out.println("Saldo de la tarjeta: " + tarjeta.getSaldo());
							System.out.println(tarjeta.getEstado());
							System.out.println();
						}

						if(tarjetasObjetivo.isEmpty()){ // Si el cliente objetivo no tiene tarjetas que puedan ser utilizadas en este contexto, la transacción se cancela
							System.out.println("El cliente objetivo no tiene tarjetas válidas para esta operación");
							scanner.nextLine();
							continue;
						}

						while (true) {
							eleccion_de_tarjeta_debito_objetivo = scanner.nextInt();

							if (eleccion_de_tarjeta_debito_objetivo > 0 && eleccion_de_tarjeta_debito_objetivo <= tarjetasObjetivo.size()) {
								tarjeta_Objetivo = tarjetasObjetivo.get(eleccion_de_tarjeta_debito_objetivo - 1);
								break;
							} else {
								System.out.println("Por favor, elige un número válido de tarjeta.");
							}
						}
						System.out.println("Ingrese el monto que desea tranferir (En la divisa de su tarjeta escogida):");
						double monto = scanner.nextDouble();
						Transaccion transaccion = new Transaccion(clienteObjetivo, clienteActual, tarjeta_de_origen, tarjeta_Objetivo, monto); //Genera un nuevo objeto de transacción. En caso de que no sea rechazada la transacción, el constructor mismo hace los cambios de las tarjetas
						if (!transaccion.isRechazado() && monto>tarjeta_de_origen.getSaldo()){
							System.out.println("la transaccion ha fallado porque no hay suficiente dinero en la cuenta");
							// aca se puede agregar codigo para las transacciones rechazadas
						}else if(transaccion.isRechazado()) {
							if(!tarjeta_de_origen.getDivisa().equals(tarjeta_Objetivo.getDivisa())){
								System.out.println("Error: las tarjetas no tiene la misma divisa");
							}
							if(!tarjeta_de_origen.puedeTransferir(monto)){
								System.out.println("La tarjeta escogida no puede transferir esta cantidad de dinero");
							}
							System.out.println("La transacción ha sido rechazada");
						}else if(!transaccion.isRechazado()){
							System.out.println("Transacción realizada");
						}
						scanner.nextLine();
						break;
					}
					case "7": {
						System.out.println("Estas son las tarjetas débito que tiene disponibles:\n");
						for (TarjetaDebito tarjeta : clienteActual.getTarjetasDebito()) {
							System.out.println("Numero de tarjeta: " + tarjeta.getNoTarjeta());
							System.out.println("Divisa de la tarjeta: " + tarjeta.getDivisa());
							System.out.println("Saldo de la tarjeta: " + tarjeta.getSaldo());
							System.out.println(tarjeta.getEstado());
							System.out.println();
						}
						int eleccion_de_tarjeta_debito;
						TarjetaDebito tarjeta_de_origen;

						while (true) {
							eleccion_de_tarjeta_debito = scanner.nextInt();

							if (eleccion_de_tarjeta_debito > 0 && eleccion_de_tarjeta_debito <= clienteActual.getTarjetasDebito().size()) {
								tarjeta_de_origen = clienteActual.getTarjetasDebito().get(eleccion_de_tarjeta_debito - 1);
								break;
							} else {
								System.out.println("Por favor, elige un número válido de tarjeta.");
							}
						}
						System.out.println(tarjeta_de_origen);
					}
					case "8":{
						System.out.println("Escoga mediante qué criterio desea encontrar la transacción\n1. Divisa\n2. Cliente que recibió la transacción\n3. Para filtrar por tarjetas");
						String criterioEscogido;
						do {
							criterioEscogido = scanner.nextLine();
						} while (!criterioEscogido.equals("1") && !criterioEscogido.equals("2") && !criterioEscogido.equals("3"));
						ArrayList<Transaccion> transacciones= new ArrayList<>(); //Almacena las transacciones que el cliente podría deshacer
						switch (criterioEscogido) {
							case "1" -> {        //Se filtran las transacciones por Divisa
								System.out.println("Por favor, escoga la divisa");
								for (Divisa divisa : Divisa.values()) {//Recorre un array de las divisas
									System.out.println(divisa.ordinal() + 1 + ". " + divisa);
								}
								int eleccion_divisa = scanner.nextInt() - 1;//Obtiene el numero de la divisa escogida
								Divisa divisaCriterio = Divisa.values()[eleccion_divisa]; //Almacena la divisa escogida
								System.out.println(divisaCriterio);
								transacciones = Transaccion.encontrarTransacciones(clienteActual, divisaCriterio);
							}
							case "2" -> {        //Se filtran las transacciones por cliente
								System.out.println("Por favor, escoga el cliente");
								for (Cliente c : Banco.getClientes()) {
									System.out.println(Banco.getClientes().indexOf(c) + 1 + " " + c.nombre);
								}
								int eleccion_cliente = scanner.nextInt() - 1;//Obtiene el numero de la divisa escogida
								Cliente clienteCriterio = Banco.getClientes().get(eleccion_cliente); //Almacena la divisa escogida
								transacciones = Transaccion.encontrarTransacciones(clienteActual, clienteCriterio);
							}
							case "3" -> {        //Se filtran las transacciones por tarjeta
								System.out.println("Por favor, escoga la tarjeta");
								for (Tarjeta t : clienteActual.getTarjetas()) {
									System.out.println(clienteActual.getTarjetas().indexOf(t) + 1 + " " + t);
								}
								int eleccion_tarjeta = scanner.nextInt() - 1; //Obtiene el numero de la tarjeta escogida
								Tarjeta tarjetaCriterio = clienteActual.getTarjetas().get(eleccion_tarjeta);  //Almacena la tarjeta escogida
								transacciones = Transaccion.encontrarTransacciones(clienteActual, tarjetaCriterio);
								System.out.println(tarjetaCriterio);
							}
						}
						if(transacciones.isEmpty()){
							System.out.println("Usted no tiene ninguna transacción que corresponda al criterio especificado");
							scanner.nextLine();
							break;
						}

						System.out.println("Estas son las transacciones que puede deshacer:");
						for(Transaccion t : transacciones){
							System.out.println(transacciones.indexOf(t)+1 + " " + t);
						}
						int eleccion_transaccion = scanner.nextInt()-1;
						Transaccion transaccion = transacciones.get(eleccion_transaccion);
						System.out.println("Por favor, ingrese un mensaje para el cliente que recibió la transacción");
						scanner.nextLine();
						String mensaje = scanner.nextLine();
						Banco.generarPeticion(transaccion, mensaje);
						break;
					}
					case "9":{
						ArrayList<Transaccion> transacciones = clienteActual.verPeticiones();
						System.out.println("Estas son las siguientes peticiones que usted ha recibido");
						for(Transaccion t : transacciones){
							System.out.println(transacciones.indexOf(t)+1 + " " + t);
						}
						System.out.printf("Escoga una de estas transacciones para deshacer, o presione %s para salir%n", (clienteActual.verPeticiones().size()+1));
						int eleccion_transaccion = scanner.nextInt()-1;
						if(eleccion_transaccion == transacciones.size()){
							scanner.nextLine();
							continue;
						}
						Transaccion transaccion = transacciones.get(eleccion_transaccion);
						System.out.println("Quiere conceder o negar esta petición (y/n)");
						boolean acceptar;
						while(true){
							scanner.nextLine();
							String respuesta = scanner.nextLine();
							if(respuesta.equalsIgnoreCase("y")){
								acceptar = true;
								break;
							}
							if(respuesta.equalsIgnoreCase("n")){
								acceptar = false;
								break;
							}
							System.out.println("Porfavor responda y o n");
						}
						Transaccion transNueva = Transaccion.completarTransaccion(transaccion, acceptar);
						Transaccion.getTransacciones().set(Transaccion.getTransacciones().indexOf(transaccion), transNueva); //Reemplaza la transacción anterior con la nueva transacción
						break;
					}
					case "10":
						break label;
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
		//Para la serializacion hay que volver los montos a valores reales, como millones en el caso del peso colombiano, para que algunas funcionalidades tengan mas sentido
		TarjetaDebito tarjetaDebito1 = new TarjetaDebito(123456, Divisa.DOLAR, 3000);
		TarjetaDebito tarjetaDebito2 = new TarjetaDebito(234567, Divisa.EURO, 0);
		TarjetaDebito tarjetaDebito3 = new TarjetaDebito(345678, Divisa.RUBLO_RUSO, 20000);
		TarjetaDebito tarjetaDebito4 = new TarjetaDebito(456789, Divisa.YEN_JAPONES, 90700);
		TarjetaDebito tarjetaDebito5 = new TarjetaDebito(567890, Divisa.PESO_COLOMBIANO, 8000000);
		TarjetaDebito tarjetaDebito6 = new TarjetaDebito(678901, Divisa.DOLAR, 700);
		TarjetaDebito tarjetaDebito7 = new TarjetaDebito(789012, Divisa.EURO, 2400500);
		TarjetaDebito tarjetaDebito8 = new TarjetaDebito(890123, Divisa.RUBLO_RUSO, 13700);
		TarjetaDebito tarjetaDebito9 = new TarjetaDebito(901234, Divisa.YEN_JAPONES, 48000);
		TarjetaDebito tarjetaDebito10 = new TarjetaDebito(123456789, Divisa.PESO_COLOMBIANO, 2000000);
		TarjetaDebito tarjetaDebito11 = new TarjetaDebito(4684648, Divisa.LIBRA_ESTERLINA,1000);
		TarjetaDebito tarjetaDebito12 = new TarjetaDebito(5448843, Divisa.LIBRA_ESTERLINA,960);
		
		//TARJETAS CREDITO
		TarjetaCredito tarjetaCredito1 = new TarjetaCredito(987456, Divisa.DOLAR, 3000, 1.5);
		TarjetaCredito tarjetaCredito2 = new TarjetaCredito(876543, Divisa.EURO, 2000, 2.0);
		TarjetaCredito tarjetaCredito3 = new TarjetaCredito(765432, Divisa.RUBLO_RUSO, 105000, 0.8);
		TarjetaCredito tarjetaCredito4 = new TarjetaCredito(654321, Divisa.YEN_JAPONES, 108050, 0.2);
		TarjetaCredito tarjetaCredito5 = new TarjetaCredito(543210, Divisa.PESO_COLOMBIANO, 6000000, 1.25);
		TarjetaCredito tarjetaCredito6 = new TarjetaCredito(432109, Divisa.DOLAR, 5000, 0.75);
		TarjetaCredito tarjetaCredito7 = new TarjetaCredito(321098, Divisa.EURO, 1750, 0.1);
		TarjetaCredito tarjetaCredito8 = new TarjetaCredito(210987, Divisa.RUBLO_RUSO, 70000, 0.3);
		TarjetaCredito tarjetaCredito9 = new TarjetaCredito(109876, Divisa.YEN_JAPONES, 40000, 0.99);
		TarjetaCredito tarjetaCredito10 = new TarjetaCredito(987654321, Divisa.PESO_COLOMBIANO, 1000000, 5.0);
		TarjetaCredito tarjetaCredito11 = new TarjetaCredito(41341395, Divisa.LIBRA_ESTERLINA, 5000, 1.0);
		TarjetaCredito tarjetaCredito12 = new TarjetaCredito(15641687, Divisa.LIBRA_ESTERLINA, 2000, 0.075);
		
		TarjetaDebito tarjetafac = new TarjetaDebito(666, Divisa.DOLAR, 10);

		cliente1.agregarTarjetasDebito(tarjetaDebito1, tarjetaDebito2, tarjetaDebito3, tarjetaDebito5, tarjetaDebito11);
		cliente1.agregarTarjetasCredito(tarjetaCredito1, tarjetaCredito2, tarjetaCredito7, tarjetaCredito9, tarjetaCredito11);
		cliente2.agregarTarjetDebito(tarjetaDebito6);

		TarjetaDebito tarjeta1 = new TarjetaDebito(1, Divisa.DOLAR, 1000);
		TarjetaDebito tarjeta2 = new TarjetaDebito(1, Divisa.EURO, 10);
		cliente1.agregarTarjetasDebito(tarjeta1, tarjeta2);

		Factura factura1 = new Factura(cliente1, 100.0, 5, tarjetafac);
		Factura factura2 = new Factura(cliente1, 90, 8, tarjetafac);
		
		//CANALES
		//Sucursal fisica
		Canal sucursalFisica1 = new Canal("Sucursal Fisica",(float)2.0, 22500, 12370, 8000, 70000, 200000, 30000000);
		
		//Cajero Automático
		Canal cajero1 = new Canal("Cajero",(float)0.5);
		cajero1.setFondos(Divisa.PESO_COLOMBIANO, 12000000);
		
		//Corresponal Bancario
		Canal corresponsal1 = new Canal("Corresponsal Bancario", (float)1.0);
		corresponsal1.setFondos(Divisa.DOLAR, 20000);
		corresponsal1.setFondos(Divisa.EURO, 10000);
		corresponsal1.setFondos(Divisa.PESO_COLOMBIANO, 40000000);
		
		//Canal tipo Cajero Automático
		Canal cajero2 = new Canal("Cajero", (float) 1.0);
		cajero2.setFondos(Divisa.DOLAR, 8000);

		//Canal tipo Sucursal Física
		Canal sucursalFisica2 = new Canal("Sucursal Física", (float) 1.5, 4000.0, 6000.0, 2000.0, 150000.0, 8000000.0, 6700000.0);

		//Canal tipo Sucursal en Línea
		Canal sucursalVirtual1 = new Canal("Sucursal en Línea", (float) 2.5, 5100.0, 8900.0, 0.0, 120000.0, 370000.0, 80545000.0);

		//Canal tipo Corresponsal Bancario
		Canal corresponsal2 = new Canal("Corresponsal Bancario", (float) 0.8);
		corresponsal2.setFondos(Divisa.DOLAR, 15000.0);
		corresponsal2.setFondos(Divisa.EURO, 8000.0);
		corresponsal2.setFondos(Divisa.RUBLO_RUSO, 350000.0);
	}

}
