package uiMain;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import baseDatos.Serializador;
import gestorAplicacion.entidades_de_negocio.Cliente;
import gestorAplicacion.entidades_de_negocio.Divisa;
import gestorAplicacion.entidades_de_negocio.Factura;
import gestorAplicacion.entidades_de_negocio.Transaccion;
import gestorAplicacion.infraestructura.Banco;
import gestorAplicacion.infraestructura.Canal;
import gestorAplicacion.tarjetas.TarjetaDebito;
import gestorAplicacion.tarjetas.Tarjeta;
import gestorAplicacion.tarjetas.TarjetaCredito;

public class Main implements Serializable{
	@Serial
	private static final long serialVersionUID = 1L;
	static Scanner scanner = new Scanner(System.in);
	
	/**
	 * Captura la entrada del usuario de tipo Int 
	 * */
	static int readInt() {
	    return scanner.nextInt();
	}
	
	/**
	 * Captura la entrada del usuario de tipo Double 
	 * */
	static Double readDouble() {
	    return scanner.nextDouble();
	}
	
	/**
	 * Captura la entrada del usuario de tipo String
	 * */
	static String readString() {
	    return scanner.nextLine();
	}

	public static void main(String[] args) {
		Banco banco = new Banco();
//		setup();
		int opcion;
		do {
			System.out.println("\nBienvenido al Banco Nacho");//Si se les ocurre un nombre mejor no duden en cambiarlo
			System.out.println("---------------------------------");
			System.out.println("¿Qué acción deseas realizar?");
			System.out.println("1. Pagar Factura");
			System.out.println("2. Cambiar Divisas");
			System.out.println("3. Retirar o Depositar Dinero");
			System.out.println("4. Solicitar una Tarjeta de Crédito");
			System.out.println("5. Deshacer Transaccion");
			System.out.println("6. Ver peticiones");
			System.out.println("--------- Otras opciones --------");
			System.out.println("7. Ver Facturas");
			System.out.println("8. Ver Tarjetas Disponibles");
			System.out.println("9. Hacer una Transferencia");
			System.out.println("0. Salir");
			
			opcion = readInt();

			switch (opcion) {
				case 1 -> pagarFactura();
				case 2 -> cambiarDivisa();
				case 3 -> retirarOrDepositar();
				case 4 -> solicitarTarjetaCredito(banco);
				case 5 -> deshacerTransaccion();
				case 6 -> verPeticiones();
				case 7 -> verFacturas();
				case 8 -> verTarjetasDisponibles();
				case 9 -> hacerTransferencia();
				case 0 -> salir(banco);
				default -> System.out.println("Digita una opción válida\n");
			}
		}while (opcion != 0);
	}	
	
	/**
	 * Permite escoger el usuario sobre el cual se realizará la funcionalidad
	 * */
	static Cliente elejirUsuario() {
		System.out.println("Elige el usuario:");
		
		//Se listan los clientes del banco
		for(Cliente cliente : Banco.getClientes()) {
			System.out.println(Banco.getClientes().indexOf(cliente) + 1 + ". " + cliente.nombre);
		}
		System.out.println(Banco.getClientes().size() + 1 + ". [ Atras ]");
		
		int opcion = readInt();
		Cliente clienteEscogido = null;
		
		do {
			if(opcion > 0 && opcion <= Banco.getClientes().size()){
				clienteEscogido = Banco.getClientes().get(opcion - 1);
				break;
			} else if (opcion == Banco.getClientes().size() + 1){
				return null;
			}
			else {
				System.out.println("Digita una opción válida:");
				opcion = readInt();
			}
		}while(opcion != Banco.getClientes().size() + 1);
		
		return clienteEscogido;
	}
		
	static void pagarFactura(){
		Cliente clienteEscogido = elejirUsuario();
		
		if(clienteEscogido == null)
			return;
		
		if (clienteEscogido.listarFacturas().isEmpty()) {
			System.out.println("No hay facturas por pagar\n");
			pagarFactura();//Se le permite al usuario escoger otro cliente
			return;//return para asegurarnos de salir del bucle de recursión
		} else {
			System.out.println("Escoge la factura que deseas pagar:\n");
			for (Factura factura : clienteEscogido.listarFacturas()) {
				System.out.println(clienteEscogido.listarFacturas().indexOf(factura) + 1 + ". " + factura);
			}
		}
		
		int opcion = readInt() - 1;
		Factura factura = clienteEscogido.listarFacturas().get(opcion);//el usuario escoge la factura por pagar
		
		System.out.println("Escoge la tarjeta que quiere utilizar para pagar:\n");
		ArrayList<Tarjeta> tarjetasPosibles = clienteEscogido.listarTarjetas(factura);
		
		for (Tarjeta tarjeta : tarjetasPosibles) {
			System.out.println(tarjetasPosibles.indexOf(tarjeta) + 1 + ". " + tarjeta);
		}
		
		int opcion2 = readInt() - 1;
		Tarjeta tarjeta = tarjetasPosibles.get(opcion2);//Se obtiene la tarjeta con la que el usuario quiere pagar
		
		System.out.println("Escoge la cantidad de dinero que desea transferir:\n");
		double monto = readDouble();
		
		Transaccion transaccion = factura.generarTransaccion(monto, tarjeta);
		if (transaccion.isRechazado()) { //En caso de que la transacción fue rechazada, se notifica al usuario, y se vuelve al principio
			System.out.println("¡La transacción fue rechazada!");
			if(tarjeta.puedeTransferir(monto))
				System.out.println("Parece que el monto que ingresaste supera los fondos de tu tarjeta");
			if(tarjeta.getDivisa().equals(factura.getDIVISA()))
				System.out.println("La divisa de la tarjeta escogida no corresponde con la de la factura");
			tarjeta.anadirError();
			if(tarjeta.tarjetaABorrar()){
				System.out.println(tarjeta.borrar());
			}
			return;
		}
		
		System.out.println("La transacción ha sido generada: " + transaccion);
		
		System.out.println("¿Estás seguro que deseas continuar con el proceso?\n(Si / No)");
		String opcion3 = readString();
		
		while (!opcion3.equalsIgnoreCase("Si") && !opcion3.equalsIgnoreCase("No")) {
			if(opcion3.equalsIgnoreCase("No"))
				break;
			
			if(opcion3.equalsIgnoreCase("Si"))
				break;
			
			if(!opcion3.equalsIgnoreCase("") && !opcion3.equalsIgnoreCase("Si") && !opcion3.equalsIgnoreCase("No"))
				System.out.println("Digita un valor válido");
			
			opcion3 = readString();
		}
		
		if(opcion3.equalsIgnoreCase("No")) {
			System.out.println("¡El pago fue cancelado!");
			return;
		}
			
		Factura facturaNueva = transaccion.pagarFactura();
		clienteEscogido.getFactura().set(clienteEscogido.getFactura().indexOf(factura), facturaNueva); //Remplaza la factura anterior con la factura nueva
		tarjeta.setErroresActuales(0);//en caso de que no se rechaza la transacción, los errores de la tarjeta vuelven a 0
		System.out.println("¡El pago fue exitoso!\n");
		System.out.println(transaccion);
	}
	
	static void cambiarDivisa() {
		Cliente clienteEscogido = elejirUsuario();
		
		if(clienteEscogido == null)
			return;
		
		System.out.println("Escoge las divisas que deseas cambiar");
		System.out.println("Escoge la divisa de origen:\n");
		
		for (Divisa divisa : Divisa.values()) {//Recorre un array de las divisas
			System.out.println(divisa.ordinal() + 1 + ". " + divisa);
		}
		
		int opcion = readInt();//Obtiene el numero de la divisa escogida
		
		//mientras el valor sea diferente del de las divisas listadas
		while (opcion <= 0 || opcion > Divisa.values().length) {
			System.out.println("Escoge un valor válido:");
			opcion = readInt();
		}
		opcion -= 1;
		Divisa divisaOrigen = Divisa.values()[opcion]; //Almacena la referencia de la divisa escogida
		
		System.out.println("Escoge la divisa de destino:\n");
		for (Divisa divisa : Divisa.values()) {
			System.out.println(divisa.ordinal() + 1 + ". " + divisa);
		}
		
		opcion = readInt();//Escogiendo divisa destino

		while (opcion <= 0 || opcion > Divisa.values().length) {
			System.out.println("Escoge un valor válido:");
			opcion = readInt();
		}
		opcion -= 1;
		Divisa divisaDestino = Divisa.values()[opcion];
		
		//Si el cliente escoge la misma divisa de Origen y de destino
		if(divisaOrigen.equals(divisaDestino)) {
			System.out.println("Debes escoger dos divisas distintas para hacer la conversión\n");
			return;
		}
		
		//Si el cliente no tiene ninguna tarjeta de cada divisa respectivamente
		if(Objects.isNull(clienteEscogido.escogerDivisas(divisaOrigen, divisaDestino))) {
			System.out.println("No tienes tarjetas que cumplan con la divisa que escogiste\n");
			return;
		} 
		
		ArrayList<Divisa> divisas = clienteEscogido.escogerDivisas(divisaOrigen, divisaDestino);
		ArrayList<Tarjeta> tarjetas = clienteEscogido.listarTarjetas(divisas);

		ArrayList<Tarjeta> tarjetasEscogidas = new ArrayList<>();

		System.out.println("Escoge la tarjeta con la divisa de origen: " + divisaOrigen + "\n");
		for (Tarjeta tarjeta : tarjetas) {
			System.out.println(tarjetas.indexOf(tarjeta) + 1 + ". " + tarjeta);
		}
		
		opcion = readInt() - 1;//Escogiendo tarjeta de origen
		tarjetasEscogidas.add(tarjetas.get(opcion));
		
		//La tarjeta de origen que escoja el usuario debe corresponder con la divisa de origen (el orden debe ser el mismo)
		while(!Divisa.verificarOrden(divisas, tarjetasEscogidas.get(0).getDivisa(), "Origen")) {
			System.out.println("Debes escoger una tarjeta de origen con una divisa acorde a la divisa que quieres cambiar");
			System.out.println("Escoge una opción válida:");
			opcion = readInt() - 1;
			tarjetasEscogidas.add(0, tarjetas.get(opcion));
		}
		
		System.out.println("Escoja la tarjeta con la divisa de Destino:\n");
		for (Tarjeta tarjeta : tarjetas) {
			if (tarjeta instanceof TarjetaCredito)//La divisa de destino solo puede ser una tarjeta debito
				continue;
			System.out.println(tarjetas.indexOf(tarjeta) + 1 + ". " + tarjeta);
		}

		opcion = readInt() - 1;
		tarjetasEscogidas.add(tarjetas.get(opcion));
		
		while(Divisa.verificarOrden(divisas, tarjetasEscogidas.get(1).getDivisa(), "Destino")) {
			System.out.println("Debes escoger una tarjeta de destino con una divisa acorde a la divisa que quieres obtener");
			System.out.println("Escoge una opción válida:");
			opcion = readInt() - 1;
			tarjetasEscogidas.add(1, tarjetas.get(opcion));
		}

		ArrayList<Canal> listaCanales = clienteEscogido.listarCanales(divisas);
		System.out.println("Escoge el canal donde realizará el proceso");
		for (Canal canal : listaCanales) {
			System.out.println(listaCanales.indexOf(canal) + 1 + ". " + canal);
		}

		opcion = readInt() - 1;
		Canal canalEscogido = listaCanales.get(opcion);
		
		double montoInicial;
		
		System.out.println("Por favor digita el monto a convertir:\n");

		while (true) {
			montoInicial = readDouble();
			if (montoInicial <= 0) {
				System.out.println("El monto debe ser mayor a 0");
				continue;
			}
			break;
		}
			
		ArrayList<Double> conversion = Divisa.convertirDivisas(divisas, canalEscogido, montoInicial); 
		Transaccion transaccion = Transaccion.crearTransaccion(divisas, montoInicial, conversion, canalEscogido, tarjetasEscogidas, clienteEscogido);
		
		transaccion = canalEscogido.finalizarConversion(transaccion, montoInicial);
		System.out.println(transaccion);
	}
	
	static void retirarOrDepositar() {
		Cliente clienteEscogido = elejirUsuario();
			
		if(clienteEscogido == null)
			return;
		
		System.out.println("¿Qué acción deseas realizar?");
		System.out.println("1. Retirar");
		System.out.println("2. Depositar");
		
		int opcionString = 0;
		boolean retirar = false;
		
		do {
			switch (opcionString) {
				case 1 -> retirar = true;
				case 2 -> retirar = false;
				case 0 -> opcionString = readInt();
				default -> {
					System.out.println("Escoge un valor válido");
					opcionString = readInt();
				}
			}
		}while(opcionString != 1 && opcionString != 2);
		
		ArrayList<Divisa> divisas = Banco.seleccionarDivisa(clienteEscogido);
		if(divisas.isEmpty()){
			System.out.println("No tienes ningúna divisa que puedas utilizar en esta transacción");
			return;
		}
		
		System.out.println("Por favor, escoge la divisa que deseas retirar:");
		
		for(Divisa d : divisas){
			System.out.println(divisas.indexOf(d)+1 + ". " + d);
		}
		
		int eleccion_divisa = readInt();
		while (eleccion_divisa <= 0 || eleccion_divisa > divisas.size()) {
			System.out.println("Escoge un valor válido:");
			eleccion_divisa = readInt();
		}
		eleccion_divisa -= 1;
		
		Divisa divisa_escogida = divisas.get(eleccion_divisa);
		ArrayList<Tarjeta> tarjetas = clienteEscogido.seleccionarTarjeta(divisa_escogida, retirar);
		if(tarjetas.isEmpty()){
			System.out.println("No tienes ningúna tarjeta que puedas utilizar en esta transacción");
			return;
		}
		
		System.out.println("Escoge la tarjeta con la cual deseas hacer la operación");
		for(Tarjeta t : tarjetas){
			System.out.println(tarjetas.indexOf(t)+1 + ". " + t);
		}
		
		int eleccion_tarjeta = readInt();

		while (eleccion_tarjeta <= 0 || eleccion_tarjeta > tarjetas.size()) {
			System.out.println("Escoge un valor válido:");
			eleccion_tarjeta = readInt();
		}	
		eleccion_tarjeta -= 1;
		Tarjeta tarjeta = tarjetas.get(eleccion_tarjeta);
		ArrayList<Canal> canales = Canal.seleccionarCanal(divisa_escogida, retirar);
		
		if(canales.isEmpty()){
			System.out.println("No hay ningún canal que puedas utilizar en esta transacción");
			return;
		}
		
		System.out.println("Por favor, escoge el canal con el cual deseas hacer la operación");
		for(Canal c : canales){
			System.out.print(canales.indexOf(c)+1 + ". " + c);
			System.out.println("Este canal tiene: " + c.getFondos(divisa_escogida) + " " + divisa_escogida + "\n");
		}
		
		int eleccion_canal = readInt();

		while (eleccion_canal <= 0 || eleccion_canal > canales.size()) {
			System.out.println("Escoge un valor válido:");
			eleccion_tarjeta = readInt();
		}	
		eleccion_canal -= 1;
		
		Canal canal = canales.get(eleccion_canal);
		
		System.out.println("Ingresa cuanto quieres retirar/depositar");
		double monto = readDouble();
		Transaccion transaccionInicial = canal.generarTransaccion(tarjeta, monto, clienteEscogido, retirar);
		Transaccion transaccionFinal = Transaccion.finalizarTransaccion(transaccionInicial, retirar);
		if(transaccionFinal.isRechazado()){
			if(!tarjeta.puedeTransferir(monto)) System.out.println("La tarjeta no puede transferir el monto necesario");
			if(canal.getFondos(divisa_escogida) >= monto) System.out.println("El canal no tiene suficientes fondos para hacer la transacción");
			System.out.println("La transacción ha sido rechazada");
		} else{
			System.out.println("Operación realizada con éxito");
//			System.out.println(transaccionFinal);
		}
	}
	
	static void solicitarTarjetaCredito(Banco banco) {
		Cliente clienteEscogido = elejirUsuario();
		
		if(clienteEscogido == null)
			return;
		
		System.out.println("Viendo transferencias del usuario...");
		
		ArrayList<Transaccion> historial = clienteEscogido.revisarHistorialCreditos();
		int puntajeTentativo = banco.calcularPuntaje(historial);
		ArrayList<Tarjeta> TarjetasBloqueadas = Tarjeta.TarjetasBloqueadas(clienteEscogido);
		ArrayList<Tarjeta> TarjetasActivas = Tarjeta.TarjetasNoBloqueadas(clienteEscogido);
		int puntajeDefinitivo = Factura.modificarPuntaje(TarjetasBloqueadas, TarjetasActivas, clienteEscogido, puntajeTentativo);
		
		System.out.println("Escoge la divisa que quieres para tu tarjeta de crédito:");
		for (Divisa divisa : Divisa.values()) {
			System.out.println(divisa.ordinal() + 1 + ". " + divisa);
		}
		
		int opcion = readInt() - 1;
		Divisa divisa = Divisa.values()[opcion];
		ArrayList<TarjetaCredito> tarjetasDisponibles = TarjetaCredito.tarjetasDisponibles(puntajeDefinitivo, divisa);
		
		int i = 0;
		System.out.printf("Tu puntaje total es: %s.\n Por favor, escoge la tarjeta de crédito que deseas:\n%n", puntajeDefinitivo);
		for (TarjetaCredito t : tarjetasDisponibles) {
			System.out.printf("%s. %sPuntos: %s\n%n", i + 1, t.toString(), i * 50);
			i++;
		}
		
		System.out.printf("%s. [ Salir ]%n", tarjetasDisponibles.size() + 1);
		opcion = readInt() - 1;
		int bono = puntajeDefinitivo - opcion * 50;
		
		if (opcion == tarjetasDisponibles.size()) 
			return;
		
		TarjetaCredito.anadirTarjetaCredito(tarjetasDisponibles.get(opcion), clienteEscogido, bono);

		scanner.nextLine();
	}
	
	static void deshacerTransaccion() {
		Cliente clienteEscogido = elejirUsuario();
		
		if(clienteEscogido == null)
			return;
		
		System.out.println("Escoge mediante qué criterio deseas encontrar la transacción\n1. Divisa\n2. Cliente que recibió la transacción\n3. Para filtrar por tarjetas");
		String criterioEscogido;
		do {
			criterioEscogido = readString();
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
				transacciones = Transaccion.encontrarTransacciones(clienteEscogido, divisaCriterio);
			}
			case "2" -> {        //Se filtran las transacciones por cliente
				System.out.println("Por favor, escoga el cliente");
				for (Cliente c : Banco.getClientes()) {
					System.out.println(Banco.getClientes().indexOf(c) + 1 + " " + c.nombre);
				}
				int eleccion_cliente = scanner.nextInt() - 1;//Obtiene el numero de la divisa escogida
				Cliente clienteCriterio = Banco.getClientes().get(eleccion_cliente); //Almacena la divisa escogida
				transacciones = Transaccion.encontrarTransacciones(clienteEscogido, clienteCriterio);
			}
			case "3" -> {        //Se filtran las transacciones por tarjeta
				System.out.println("Por favor, escoga la tarjeta");
				for (Tarjeta t : clienteEscogido.getTarjetas()) {
					System.out.println(clienteEscogido.getTarjetas().indexOf(t) + 1 + " " + t);
				}
				int eleccion_tarjeta = scanner.nextInt() - 1; //Obtiene el numero de la tarjeta escogida
				Tarjeta tarjetaCriterio = clienteEscogido.getTarjetas().get(eleccion_tarjeta);  //Almacena la tarjeta escogida
				transacciones = Transaccion.encontrarTransacciones(clienteEscogido, tarjetaCriterio);
				System.out.println(tarjetaCriterio);
			}
		}
		if(transacciones.isEmpty()){
			System.out.println("No tienes ninguna transacción que corresponda al criterio especificado");
			scanner.nextLine();
			return;
		}

		System.out.println("Estas son las transacciones que puede deshacer:");
		for(Transaccion t : transacciones){
			System.out.println(transacciones.indexOf(t)+1 + " " + t);
		}
		int eleccion_transaccion = scanner.nextInt()-1;
		Transaccion transaccion = transacciones.get(eleccion_transaccion);
		System.out.println("Por favor, ingrese un mensaje para el cliente que recibió la transacción");
		scanner.nextLine();
		String mensaje = readString();
		Banco.generarPeticion(transaccion, mensaje);
	}
	
	static void hacerTransferencia() {
		Cliente clienteEscogido = elejirUsuario();
		
		if(clienteEscogido == null)
			return;
		
		System.out.println("Estas son las tarjetas débito que tienes disponibles:\n");
		for (TarjetaDebito tarjeta : clienteEscogido.getTarjetasDebito()) {
			System.out.println(clienteEscogido.getTarjetasDebito().indexOf(tarjeta) + 1 + ". Numero de tarjeta: " + tarjeta.getNoTarjeta());
			System.out.println("Divisa de la tarjeta: " + tarjeta.getDivisa());
			System.out.println("Saldo de la tarjeta: " + tarjeta.getSaldo());
			System.out.println(tarjeta.getEstado());
			System.out.println();
		}
		int eleccion_de_tarjeta_debito;
		TarjetaDebito tarjeta_de_origen;

		while (true) {
			eleccion_de_tarjeta_debito = readInt();

			if (eleccion_de_tarjeta_debito > 0 && eleccion_de_tarjeta_debito <= clienteEscogido.getTarjetasDebito().size()) {
				tarjeta_de_origen = clienteEscogido.getTarjetasDebito().get(eleccion_de_tarjeta_debito - 1);
				break;
			}
			System.out.println("Por favor, elige un número válido de tarjeta.");
			
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
			System.out.println(tarjetasObjetivo.indexOf(tarjeta) + 1 + ". Numero de tarjeta: " + tarjeta.getNoTarjeta());
			System.out.println("Divisa de la tarjeta: " + tarjeta.getDivisa());
			System.out.println("Saldo de la tarjeta: " + tarjeta.getSaldo());
			System.out.println(tarjeta.getEstado());
			System.out.println();
		}

		if(tarjetasObjetivo.isEmpty()){ // Si el cliente objetivo no tiene tarjetas que puedan ser utilizadas en este contexto, la transacción se cancela
			System.out.println("El cliente objetivo no tiene tarjetas válidas para esta operación");
			return;
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
		double monto = readDouble();
		Transaccion transaccion = new Transaccion(clienteObjetivo, clienteEscogido, tarjeta_de_origen, tarjeta_Objetivo, monto); //Genera un nuevo objeto de transacción. En caso de que no sea rechazada la transacción, el constructor mismo hace los cambios de las tarjetas
		if (!transaccion.isRechazado() && monto>tarjeta_de_origen.getSaldo())
			System.out.println("la transaccion ha fallado porque no hay suficiente dinero en la cuenta");
			// aca se puede agregar codigo para las transacciones rechazadas
		else if(transaccion.isRechazado()) {
			if(!tarjeta_de_origen.getDivisa().equals(tarjeta_Objetivo.getDivisa())){
				System.out.println("Error: las tarjetas no tiene la misma divisa");
			}
			if(!tarjeta_de_origen.puedeTransferir(monto)){
				System.out.println("La tarjeta escogida no puede transferir esta cantidad de dinero");
			}
			tarjeta_de_origen.anadirError();
			if(tarjeta_de_origen.tarjetaABorrar()){
				System.out.println(tarjeta_de_origen.borrar());
			}
			System.out.println("La transacción ha sido rechazada");
		}else if(!transaccion.isRechazado()){
			System.out.println("Transacción realizada");
			tarjeta_de_origen.setErroresActuales(0);
		}
	}
	
	static void verPeticiones() {
		Cliente clienteEscogido = elejirUsuario();
		
		if(clienteEscogido == null)
			return;
		
		ArrayList<Transaccion> transacciones = clienteEscogido.verPeticiones();
		System.out.println("Estas son las siguientes peticiones que has recibido");
		for(Transaccion t : transacciones){
			System.out.println(transacciones.indexOf(t)+1 + " " + t);
		}
		System.out.printf("Escoga una de estas transacciones para deshacer, o presione %s para salir%n", (clienteEscogido.verPeticiones().size()+1));
		int eleccion_transaccion = readInt() - 1;
		if(eleccion_transaccion == transacciones.size()){
			return;
		}
		Transaccion transaccion = transacciones.get(eleccion_transaccion);
		System.out.println("Quiere conceder o negar esta petición (Si / No)");
		boolean acceptar;
		while(true){
			scanner.nextLine();
			String respuesta = scanner.nextLine();
			if(respuesta.equalsIgnoreCase("Si")){
				acceptar = true;
				break;
			}
			if(respuesta.equalsIgnoreCase("No")){
				acceptar = false;
				break;
			}
			System.out.println("Porfavor responde adecuadamente");
		}
		Transaccion transNueva = Transaccion.completarTransaccion(transaccion, acceptar);
		Transaccion.getTransacciones().set(Transaccion.getTransacciones().indexOf(transaccion), transNueva); //Reemplaza la transacción anterior con la nueva transacción
	}
	static void verFacturas() {
		Cliente clienteEscogido = elejirUsuario();
				
		if(clienteEscogido == null)
			return;
		
		if (clienteEscogido.listarFacturas().isEmpty()) {
			System.out.println("No hay facturas por pagar");
		} else {
			for (Factura f : clienteEscogido.getFactura()) {
				System.out.println(f);
			}
		}
	}
	
	static void verTarjetasDisponibles() {
		Cliente clienteEscogido = elejirUsuario();
		
		if(clienteEscogido == null)
			return;
		
		for (Tarjeta t : clienteEscogido.getTarjetasCredito()) {
			System.out.println(t);
		}
		for (Tarjeta t : clienteEscogido.getTarjetasDebito()) {
			System.out.println(t);
		}
	}
	
	static void salir(Banco banco) {
		System.out.println("Gracias por haber usado nuestro sistema");
	    Serializador.serializar(banco);
	}	

		public static void setup(){ //Función que inicializa algunos objetos para comenzar a experimentar
			//CLIENTES
			Cliente cliente1 = new Cliente("Dario", 1);
			Cliente cliente2 = new Cliente("Esteban", 2);
			Cliente cliente3 = new Cliente("Marta", 3);
			Cliente cliente4 = new Cliente("Sandra", 4);
			
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
	
