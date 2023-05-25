//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

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
				case 4 -> solicitarTarjetaCredito();
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
			System.out.println(Banco.getClientes().indexOf(cliente) + 1 + ". " + cliente.NOMBRE);
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

	/**
	 * Realiza el proceso de pago de una factura para un cliente.
	 */
	static void pagarFactura() {
		// Seleccionar cliente
		Cliente clienteEscogido = elejirUsuario();

		// Verificar si se seleccionó un cliente válido
		if (clienteEscogido == null)
			return;

		// Verificar si el cliente tiene facturas pendientes
		if (clienteEscogido.listarFacturas().isEmpty()) {
			System.out.println("No hay facturas por pagar\n");
			pagarFactura(); // Permite al usuario escoger otro cliente
			return; // Salir del bucle de recursión
		} else {
			// Mostrar las facturas disponibles para pagar
			System.out.println("Escoge la factura que deseas pagar:\n");
			for (Factura factura : clienteEscogido.listarFacturas()) {
				System.out.println(clienteEscogido.listarFacturas().indexOf(factura) + 1 + ". " + factura);
			}
		}

		// Obtener la opción de factura seleccionada por el usuario
		int opcion = readInt() - 1;
		Factura factura = clienteEscogido.listarFacturas().get(opcion);

		// Mostrar las tarjetas disponibles para pagar
		System.out.println("Escoge la tarjeta que quieres utilizar para pagar:\n");
		ArrayList<Tarjeta> tarjetasPosibles = clienteEscogido.listarTarjetas(factura);

		for (Tarjeta tarjeta : tarjetasPosibles) {
			System.out.println(tarjetasPosibles.indexOf(tarjeta) + 1 + ". " + tarjeta);
		}

		// Obtener la opción de tarjeta seleccionada por el usuario
		int opcion2 = readInt() - 1;
		Tarjeta tarjeta = tarjetasPosibles.get(opcion2);

		// Obtener el monto a transferir
		System.out.println("Escoge la cantidad de dinero que deseas transferir:\n");
		double monto = readDouble();

		// Generar la transacción
		Transaccion transaccion = factura.generarTransaccion(monto, tarjeta);

		// Verificar si la transacción fue rechazada
		if (transaccion.isRechazado()) {
			System.out.println("¡La transacción fue rechazada!");

			// Notificar las posibles razones del rechazo
			if (tarjeta.puedeTransferir(monto))
				System.out.println("Parece que el monto que ingresaste supera los fondos de tu tarjeta");

			if (tarjeta.getDivisa().equals(factura.getDIVISA()))
				System.out.println("La divisa de la tarjeta escogida no corresponde con la de la factura");

			tarjeta.anadirError();

			// Verificar si se debe eliminar la tarjeta
			if (tarjeta.tarjetaABorrar()) {
				System.out.println(tarjeta.borrar());
			}

			return;
		}

		System.out.println("La transacción ha sido generada: " + transaccion);

		// Confirmar si se desea continuar con el proceso de pago
		System.out.println("¿Estás seguro que deseas continuar con el proceso?\n(Si / No)");
		String opcion3 = readString();

		// Validar la respuesta del usuario
		while (!opcion3.equalsIgnoreCase("Si") && !opcion3.equalsIgnoreCase("No")) {
			if (opcion3.equalsIgnoreCase("No")) {
				break;
			}

			if (opcion3.equalsIgnoreCase("Si")) {
				break;
			}

			if (!opcion3.equalsIgnoreCase("") && !opcion3.equalsIgnoreCase("Si") && !opcion3.equalsIgnoreCase("No")) {
				System.out.println("Digita un valor válido");
			}

			opcion3 = readString();
		}

		// Cancelar el pago si el usuario decide no continuar
		if (opcion3.equalsIgnoreCase("No")) {
			System.out.println("¡El pago fue cancelado!");
			return;
		}

		// Realizar el pago de la factura
		Factura facturaNueva = transaccion.pagarFactura();

		// Reemplazar la factura anterior con la factura nueva
		int indiceFacturaAnterior = clienteEscogido.getFactura().indexOf(factura);
		clienteEscogido.getFactura().set(indiceFacturaAnterior, facturaNueva);

		// Restablecer los errores de la tarjeta a cero
		tarjeta.setErroresActuales(0);

		System.out.println("¡El pago fue exitoso!\n");
		System.out.println(transaccion);
	}


	/**
	 * Realiza el proceso de cambio de divisas para un cliente.
	 */
	static void cambiarDivisa() {
		// Seleccionar cliente
		Cliente clienteEscogido = elejirUsuario();

		// Verificar si se seleccionó un cliente válido
		if (clienteEscogido == null)
			return;

		System.out.println("Escoge las divisas que deseas cambiar");

		// Escoger la divisa de origen
		System.out.println("Escoge la divisa de origen:\n");
		for (Divisa divisa : Divisa.values()) {
			System.out.println(divisa.ordinal() + 1 + ". " + divisa);
		}

		int opcion = readInt(); // Obtener el número de la divisa de origen seleccionada

		// Validar la opción seleccionada
		while (opcion <= 0 || opcion > Divisa.values().length) {
			System.out.println("Escoge un valor válido:");
			opcion = readInt();
		}

		opcion -= 1;
		Divisa divisaOrigen = Divisa.values()[opcion]; // Almacenar la referencia de la divisa de origen seleccionada

		// Escoger la divisa de destino
		System.out.println("Escoge la divisa de destino:\n");
		for (Divisa divisa : Divisa.values()) {
			System.out.println(divisa.ordinal() + 1 + ". " + divisa);
		}

		opcion = readInt(); // Obtener el número de la divisa de destino seleccionada

		// Validar la opción seleccionada
		while (opcion <= 0 || opcion > Divisa.values().length) {
			System.out.println("Escoge un valor válido:");
			opcion = readInt();
		}

		opcion -= 1;
		Divisa divisaDestino = Divisa.values()[opcion]; // Almacenar la referencia de la divisa de destino seleccionada

		// Verificar si se seleccionó la misma divisa de origen y destino
		if (divisaOrigen.equals(divisaDestino)) {
			System.out.println("Debes escoger dos divisas distintas para hacer la conversión\n");
			return;
		}

		// Verificar si el cliente tiene tarjetas correspondientes a las divisas seleccionadas
		ArrayList<Divisa> divisas = clienteEscogido.escogerDivisas(divisaOrigen, divisaDestino);

		if (Objects.isNull(divisas)) {
			System.out.println("No tienes tarjetas que cumplan con la divisa que escogiste\n");
			return;
		}

		ArrayList<Tarjeta> tarjetas = clienteEscogido.listarTarjetas(divisas);
		ArrayList<Tarjeta> tarjetasEscogidas = new ArrayList<>();

		// Escoger la tarjeta de origen
		System.out.println("Escoge la tarjeta con la divisa de origen: " + divisaOrigen + "\n");
		for (Tarjeta tarjeta : tarjetas) {
			System.out.println(tarjetas.indexOf(tarjeta) + 1 + ". " + tarjeta);
		}

		opcion = readInt() - 1; // Obtener la opción de tarjeta de origen seleccionada
		tarjetasEscogidas.add(tarjetas.get(opcion));

		// Verificar que la tarjeta de origen seleccionada tenga la divisa correspondiente
		while (!Divisa.verificarOrden(divisas, tarjetasEscogidas.get(0).getDivisa(), "Origen")) {
			System.out.println("Debes escoger una tarjeta de origen con una divisa acorde a la divisa que quieres cambiar");
			System.out.println("Escoge una opción válida:");
			opcion = readInt() - 1;
			tarjetasEscogidas.add(0, tarjetas.get(opcion));
		}

		boolean disponibleDebito = false;//Servirá para comprobar que el usuario si tenga tarjetas de debito con la divisaDestino

		// Escoger la tarjeta de destino
		System.out.println("Escoja la tarjeta con la divisa de Destino:\n");
		for (Tarjeta tarjeta : tarjetas) {
			if (tarjeta instanceof TarjetaCredito) // La divisa de destino solo puede ser una tarjeta de débito
				continue;
			if(!tarjeta.getDivisa().equals(divisaDestino))
				continue;
			System.out.println(tarjetas.indexOf(tarjeta) + 1 + ". " + tarjeta);
			disponibleDebito = true;
		}
		
		if(!disponibleDebito) {
			System.out.println("Parece que no tienes tarjetas de debito acorde a la divisa de destino que escogiste...");
			return;
		}
		opcion = readInt() - 1; // Obtener la opción de tarjeta de destino seleccionada
		tarjetasEscogidas.add(tarjetas.get(opcion));

		// Verificar que la tarjeta de destino seleccionada tenga la divisa correspondiente
		while (Divisa.verificarOrden(divisas, tarjetasEscogidas.get(1).getDivisa(), "Destino")) {
			System.out.println("Debes escoger una tarjeta de destino con una divisa acorde a la divisa que quieres obtener");
			System.out.println("Escoge una opción válida:");
			opcion = readInt() - 1;
			tarjetasEscogidas.add(1, tarjetas.get(opcion));
		}

		// Listar los canales disponibles para el cliente
		ArrayList<Canal> listaCanales = clienteEscogido.listarCanales(divisas);
		System.out.println("Escoge el canal donde realizará el proceso");
		for (Canal canal : listaCanales) {
			System.out.println(listaCanales.indexOf(canal) + 1 + ". " + canal);
		}

		opcion = readInt() - 1; // Obtener la opción de canal seleccionada
		Canal canalEscogido = listaCanales.get(opcion);

		double montoInicial;

		System.out.println("Por favor digita el monto a convertir:\n");

		// Solicitar y validar el monto a convertir
		while (true) {
			montoInicial = readDouble();
			if (montoInicial <= 0) {
				System.out.println("El monto debe ser mayor a 0");
				continue;
			}
			break;
		}

		// Realizar la conversión de divisas
		ArrayList<Double> conversion = Divisa.convertirDivisas(divisas, canalEscogido, montoInicial);

		// Crear la transacción
		Transaccion transaccion = Transaccion.crearTransaccion(divisas, montoInicial, conversion, canalEscogido, tarjetasEscogidas, clienteEscogido);

		// Finalizar la conversión a través del canal seleccionado
		transaccion = canalEscogido.finalizarConversion(transaccion, montoInicial);

		System.out.println(transaccion);
	}


	static void retirarOrDepositar() {
		// Función para realizar operaciones de retiro o depósito.

		// Obtener el cliente seleccionado
		Cliente clienteEscogido = elejirUsuario();

		// Si no se seleccionó ningún cliente, finalizar la función.
		if(clienteEscogido == null)
			return;

		// Mostrar opciones al usuario
		System.out.println("¿Qué acción deseas realizar?");
		System.out.println("1. Retirar");
		System.out.println("2. Depositar");

		// Leer la opción seleccionada por el usuario
		int opcionString = readInt();

		// Variable para indicar si se realizará un retiro (true) o un depósito (false)
		boolean retirar = false;

		// Bucle para validar la opción seleccionada
		do {
			switch (opcionString) {
				case 1 -> retirar = true;  // Se seleccionó "Retirar"
				case 2 -> {}  // Se seleccionó "Depositar"
				case 0 -> opcionString = readInt();  // Se seleccionó "0" para volver a ingresar la opción
				default -> {
					// Opción inválida, solicitar una nueva opción
					System.out.println("Escoge un valor válido");
					opcionString = readInt();
				}
			}
		} while (opcionString != 1 && opcionString != 2);

		// Verificar si se realizará un retiro o un depósito
		String proceso = retirar ? "Retiro" : "Depósito";

		// Obtener las divisas disponibles para el cliente
		ArrayList<Divisa> divisas = Banco.seleccionarDivisa(clienteEscogido);

		// Verificar si el cliente tiene alguna divisa disponible
		if (divisas.isEmpty()) {
			System.out.println("No tienes ninguna divisa que puedas utilizar en esta transacción");
			return;
		}

		// Mostrar las opciones de divisas disponibles al cliente
		System.out.println("Por favor, escoge la divisa con la que quieres realizar el " + proceso.toLowerCase() + ":");
		for (Divisa d : divisas) {
			System.out.println(divisas.indexOf(d) + 1 + ". " + d);
		}

		// Leer la elección de la divisa
		int eleccion_divisa = readInt();
		while (eleccion_divisa <= 0 || eleccion_divisa > divisas.size()) {
			System.out.println("Escoge un valor válido:");
			eleccion_divisa = readInt();
		}
		eleccion_divisa -= 1;

		// Obtener la divisa seleccionada
		Divisa divisa_escogida = divisas.get(eleccion_divisa);

		// Obtener las tarjetas disponibles para la divisa y la operación seleccionada
		ArrayList<Tarjeta> tarjetas = clienteEscogido.seleccionarTarjeta(divisa_escogida, retirar);

		// Verificar si el cliente tiene alguna tarjeta disponible
		if (tarjetas.isEmpty()) {
			System.out.println("No tienes ninguna tarjeta que puedas utilizar en esta transacción");
			return;
		}

		// Mostrar las opciones de tarjetas disponibles al cliente
		System.out.println("Escoge la tarjeta con la cual deseas hacer la operación");
		for (Tarjeta t : tarjetas) {
			if (!retirar && t instanceof TarjetaCredito)
				continue;
			System.out.println(tarjetas.indexOf(t) + 1 + ". " + t);
		}

		// Leer la elección de la tarjeta
		int eleccion_tarjeta = readInt();

		// Validar la elección de la tarjeta
		while (eleccion_tarjeta <= 0 || eleccion_tarjeta > tarjetas.size()) {
			System.out.println("Escoge un valor válido:");
			eleccion_tarjeta = readInt();
		}
		eleccion_tarjeta -= 1;

		// Obtener la tarjeta seleccionada
		Tarjeta tarjeta = tarjetas.get(eleccion_tarjeta);

		// Obtener los canales disponibles para la divisa y la operación seleccionada
		ArrayList<Canal> canales = Canal.seleccionarCanal(divisa_escogida, retirar);

		// Verificar si hay algún canal disponible
		if (canales.isEmpty()) {
			System.out.println("No hay ningún canal que puedas utilizar en esta transacción");
			return;
		}

		// Mostrar las opciones de canales disponibles al cliente
		System.out.println("Por favor, escoge el canal con el cual deseas hacer la operación\n");
		for (Canal c : canales) {
			System.out.print(canales.indexOf(c) + 1 + ". " + c + "\n");
		}

		// Leer la elección del canal
		int eleccion_canal = readInt();

		// Validar la elección del canal
		while (eleccion_canal <= 0 || eleccion_canal > canales.size()) {
			System.out.println("Escoge un valor válido:");
			eleccion_tarjeta = readInt();
		}
		eleccion_canal -= 1;

		// Obtener el canal seleccionado
		Canal canal = canales.get(eleccion_canal);

		// Solicitar el monto de la operación al cliente
		System.out.println("Ingresa de cuanto será el " + proceso.toLowerCase() + ":");
		double monto = readDouble();

		// Crear la transacción inicial
		Transaccion transaccionInicial = Transaccion.crearTransaccion(clienteEscogido, tarjeta, monto, canal, retirar);

		// Mostrar información de la transacción inicial generada
		System.out.println("La transacción ha sido generada: \n" +
				"Proceso: " + proceso +
				"\n" + transaccionInicial + "\n");

		// Verificar si la transacción inicial fue rechazada
		if (transaccionInicial.isRechazado()) {
			if (!tarjeta.puedeTransferir(monto))
				System.out.println("La tarjeta no puede transferir el monto necesario");
			if (canal.getFondos(divisa_escogida) < monto)
				System.out.println("El canal no tiene suficientes fondos para hacer el " + proceso.toLowerCase());
			return;
		}

		// Solicitar confirmación al cliente
		System.out.println("\n¿Estás seguro que deseas continuar con el proceso?\n(Si / No)");
		String opcion = readString();

		// Validar la opción seleccionada por el cliente
		while (!opcion.equalsIgnoreCase("Si") && !opcion.equalsIgnoreCase("No")) {
			if (opcion.equalsIgnoreCase("No"))
				break;

			if (opcion.equalsIgnoreCase("Si"))
				break;

			if (!opcion.equalsIgnoreCase("") && !opcion.equalsIgnoreCase("Si") && !opcion.equalsIgnoreCase("No"))
				System.out.println("Digita un valor válido");

			opcion = readString();
		}

		// Si el cliente no desea continuar, finalizar el proceso
		if (opcion.equalsIgnoreCase("No")) {
			System.out.println("¡El " + proceso.toLowerCase() + " fue cancelado!");
			return;
		}

		// Finalizar la transacción
		Transaccion transaccionFinal = Canal.finalizarTransaccion(transaccionInicial, retirar);

		// Mostrar mensaje de éxito y la información de la transacción final
		System.out.println("\n" + proceso + " realizado con éxito");
		System.out.println(transaccionFinal);
	}


	static void solicitarTarjetaCredito() {
		// Seleccionar un cliente
		Cliente clienteEscogido = elejirUsuario();

		// Verificar si el cliente es nulo
		if(clienteEscogido == null)
			return;

		System.out.println("Viendo transferencias del usuario...");

		// Obtener el historial de transferencias del cliente
		ArrayList<Transaccion> historial = clienteEscogido.revisarHistorialCreditos();

		// Calcular el puntaje tentativo del cliente en base al historial de transferencias
		int puntajeTentativo = Banco.calcularPuntaje(historial);

		// Obtener las tarjetas de crédito bloqueadas del cliente
		ArrayList<Tarjeta> TarjetasBloqueadas = Tarjeta.TarjetasBloqueadas(clienteEscogido);

		// Obtener las tarjetas de crédito activas (no bloqueadas) del cliente
		ArrayList<Tarjeta> TarjetasActivas = Tarjeta.TarjetasNoBloqueadas(clienteEscogido);

		// Modificar el puntaje definitivo del cliente con base en las tarjetas bloqueadas y activas
		int puntajeDefinitivo = Factura.modificarPuntaje(TarjetasBloqueadas, TarjetasActivas, clienteEscogido, puntajeTentativo);

		System.out.println("Escoge la divisa que quieres para tu tarjeta de crédito:");

		// Mostrar las divisas disponibles
		for (Divisa divisa : Divisa.values()) {
			System.out.println(divisa.ordinal() + 1 + ". " + divisa);
		}

		int opcion = readInt() - 1;
		Divisa divisa = Divisa.values()[opcion];

		// Obtener las tarjetas de crédito disponibles en base al puntaje definitivo y la divisa seleccionada
		ArrayList<TarjetaCredito> tarjetasDisponibles = TarjetaCredito.tarjetasDisponibles(puntajeDefinitivo, divisa);

		int i = 0;
		System.out.printf("Tu puntaje total es: %s.\n Por favor, escoge la tarjeta de crédito que deseas:\n%n", puntajeDefinitivo);

		// Mostrar las tarjetas de crédito disponibles con sus respectivos puntos y bonos
		for (TarjetaCredito t : tarjetasDisponibles) {
			System.out.printf("%s. %sPuntos: %s\n%n", i + 1, t.toString(), i * 50);
			i++;
		}

		System.out.printf("%s. [ Salir ]%n", tarjetasDisponibles.size() + 1);
		opcion = readInt() - 1;
		int bono = puntajeDefinitivo - opcion * 50;

		// Verificar si se seleccionó la opción "Salir"
		if (opcion == tarjetasDisponibles.size())
			return;

		// Añadir la tarjeta de crédito seleccionada al cliente con el bono correspondiente
		TarjetaCredito.anadirTarjetaCredito(tarjetasDisponibles.get(opcion), clienteEscogido, bono);
	}

	static void deshacerTransaccion() {
		// Elección del cliente para deshacer la transacción
		Cliente clienteEscogido = elejirUsuario();

		// Verificar si se seleccionó un cliente válido
		if (clienteEscogido == null)
			return;

		// Mostrar opciones de criterio de búsqueda de transacciones
		System.out.println("Escoge mediante qué criterio deseas encontrar la transacción\n1. Divisa\n2. Cliente que recibió la transacción\n3. Para filtrar por tarjetas");

		// Leer la opción seleccionada por el usuario
		String criterioEscogido;
		do {
			criterioEscogido = readString();
		} while (!criterioEscogido.equals("1") && !criterioEscogido.equals("2") && !criterioEscogido.equals("3"));

		// Lista de transacciones que el cliente podría deshacer
		ArrayList<Transaccion> transacciones = new ArrayList<>();

		// Filtrar transacciones según el criterio seleccionado
		switch (criterioEscogido) {
			case "1" -> { // Filtrar por divisa
				System.out.println("Por favor, escoga la divisa");
				for (Divisa divisa : Divisa.values()) {
					System.out.println(divisa.ordinal() + 1 + ". " + divisa);
				}
				int eleccion_divisa = readInt() - 1;
				Divisa divisaCriterio = Divisa.values()[eleccion_divisa];
				transacciones = Transaccion.encontrarTransacciones(clienteEscogido, divisaCriterio);
			}
			case "2" -> { // Filtrar por cliente que recibió la transacción
				System.out.println("Por favor, escoga el cliente");
				for (Cliente c : Banco.getClientes()) {
					System.out.println(Banco.getClientes().indexOf(c) + 1 + " " + c.NOMBRE);
				}
				int eleccion_cliente = readInt() - 1;
				Cliente clienteCriterio = Banco.getClientes().get(eleccion_cliente);
				transacciones = Transaccion.encontrarTransacciones(clienteEscogido, clienteCriterio);
			}
			case "3" -> { // Filtrar por tarjeta
				System.out.println("Por favor, escoga la tarjeta");
				for (Tarjeta t : clienteEscogido.getTarjetas()) {
					System.out.println(clienteEscogido.getTarjetas().indexOf(t) + 1 + " " + t);
				}
				int eleccion_tarjeta = readInt() - 1;
				Tarjeta tarjetaCriterio = clienteEscogido.getTarjetas().get(eleccion_tarjeta);
				transacciones = Transaccion.encontrarTransacciones(clienteEscogido, tarjetaCriterio);
			}
		}

		// Verificar si no se encontraron transacciones que cumplan el criterio
		if (transacciones.isEmpty()) {
			System.out.println("No tienes ninguna transacción que corresponda al criterio especificado");
			readString();
			return;
		}

		// Mostrar las transacciones encontradas
		System.out.println("Estas son las transacciones que puede deshacer:");
		for (Transaccion t : transacciones) {
			System.out.println(transacciones.indexOf(t) + 1 + ". " + t);
		}

		// Seleccionar la transacción a deshacer
		int eleccion_transaccion = readInt() - 1;
		Transaccion transaccion = transacciones.get(eleccion_transaccion);

		// Ingresar un mensaje para el cliente que recibió la transacción
		System.out.println("Por favor, ingrese un mensaje para el cliente que recibió la transacción");
		readString();
		String mensaje = readString();

		// Generar una petición para deshacer la transacción
		Banco.generarPeticion(transaccion, mensaje);
	}


	static void hacerTransferencia() {
		// Elección del cliente que realizará la transferencia
		Cliente clienteEscogido = elejirUsuario();

		// Verificar si se seleccionó un cliente válido
		if (clienteEscogido == null)
			return;

		// Mostrar las tarjetas de débito disponibles del cliente
		System.out.println("Estas son las tarjetas débito que tienes disponibles:\n");
		for (TarjetaDebito tarjeta : clienteEscogido.getTarjetasDebito()) {
			System.out.println(clienteEscogido.getTarjetasDebito().indexOf(tarjeta) + 1 + ". " + tarjeta);
		}

		// Elección de la tarjeta de débito de origen
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

		// Mostrar opciones de clientes objetivo para la transferencia
		Cliente clienteObjetivo;
		while (true) {
			System.out.println("Elija el usuario al que le desea hacer la transaccion:");
			for (Cliente c : Banco.getClientes()) {
				System.out.println(Banco.getClientes().indexOf(c) + 1 + ". " + c.NOMBRE);
			}
			int eleccion_cliente_objetivo = readInt();
			if (eleccion_cliente_objetivo > 0 && eleccion_cliente_objetivo <= Banco.getClientes().size()) {
				clienteObjetivo = Banco.getClientes().get(eleccion_cliente_objetivo - 1);
				break;
			}
		}

		// Filtrar las tarjetas de débito del cliente objetivo
		ArrayList<TarjetaDebito> tarjetasObjetivo = new ArrayList<>();
		for (TarjetaDebito t : clienteObjetivo.getTarjetasDebito()) {
			if (!t.equals(tarjeta_de_origen)) {
				tarjetasObjetivo.add(t);
			}
		}

		// Mostrar opciones de tarjetas objetivo para la transferencia
		System.out.println("Escoge a qué tarjeta del destinatario transferir:");
		int eleccion_de_tarjeta_debito_objetivo;
		TarjetaDebito tarjeta_Objetivo;
		for (TarjetaDebito tarjeta : tarjetasObjetivo) {
			System.out.println(tarjetasObjetivo.indexOf(tarjeta) + 1 + ". Número de tarjeta: " + tarjeta);
		}

		// Verificar si el cliente objetivo tiene tarjetas válidas para la transferencia
		if (tarjetasObjetivo.isEmpty()) {
			System.out.println("El cliente objetivo no tiene tarjetas válidas para esta operación");
			return;
		}

		// Elección de la tarjeta de débito objetivo
		while (true) {
			eleccion_de_tarjeta_debito_objetivo = readInt();

			if (eleccion_de_tarjeta_debito_objetivo > 0 && eleccion_de_tarjeta_debito_objetivo <= tarjetasObjetivo.size()) {
				tarjeta_Objetivo = tarjetasObjetivo.get(eleccion_de_tarjeta_debito_objetivo - 1);
				break;
			} else {
				System.out.println("Por favor, elige una opción válida");
			}
		}

		// Ingresar el monto de la transferencia
		System.out.println("Ingrese el monto que desea transferir (En la divisa de su tarjeta escogida):");
		double monto = readDouble();

		// Crear una nueva transacción con los datos proporcionados
		Transaccion transaccion = new Transaccion(clienteObjetivo, clienteEscogido, tarjeta_de_origen, tarjeta_Objetivo, monto);

		// Verificar si la transacción fue rechazada o aprobada
		if (!transaccion.isRechazado() && monto > tarjeta_de_origen.getSaldo()) {
			System.out.println("La transacción ha fallado porque no hay suficiente dinero en la cuenta");
			// Se puede agregar código adicional para manejar transacciones rechazadas
		} else if (transaccion.isRechazado()) {
			if (!tarjeta_de_origen.getDivisa().equals(tarjeta_Objetivo.getDivisa())) {
				System.out.println("Error: las tarjetas no tienen la misma divisa");
			}
			if (!tarjeta_de_origen.puedeTransferir(monto)) {
				System.out.println("La tarjeta escogida no puede transferir esta cantidad de dinero");
			}
			tarjeta_de_origen.anadirError();
			if (tarjeta_de_origen.tarjetaABorrar()) {
				System.out.println(tarjeta_de_origen.borrar());
			}
			System.out.println("La transacción ha sido rechazada");
		} else if (!transaccion.isRechazado()) {
			System.out.println("Transacción realizada");
			tarjeta_de_origen.setErroresActuales(0);
		}
	}


	static void verPeticiones() {
		// Elección del cliente para ver las peticiones
		Cliente clienteEscogido = elejirUsuario();

		// Verificar si se seleccionó un cliente válido
		if (clienteEscogido == null)
			return;

		// Obtener las transacciones pendientes del cliente
		ArrayList<Transaccion> transacciones = clienteEscogido.verPeticiones();

		// Mostrar las transacciones pendientes al cliente
		System.out.println("Estas son las siguientes peticiones que has recibido:");
		for (Transaccion t : transacciones) {
			System.out.println(transacciones.indexOf(t) + 1 + " " + t);
		}

		// Pedir al cliente que elija una transacción para deshacer
		System.out.printf("Escoge una de estas transacciones para deshacer, o presione %s para salir%n", (clienteEscogido.verPeticiones().size() + 1));
		int eleccion_transaccion = readInt() - 1;

		// Verificar si se eligió la opción para salir
		if (eleccion_transaccion == transacciones.size()) {
			return;
		}

		// Obtener la transacción seleccionada
		Transaccion transaccion = transacciones.get(eleccion_transaccion);

		// Pedir al cliente que conceda o niegue la petición
		System.out.println("Quiere conceder o negar esta petición (Si / No)");
		boolean aceptar;

		while (true) {
			String respuesta = readString();

			if (respuesta.equalsIgnoreCase("Si")) {
				aceptar = true;
				break;
			}

			if (respuesta.equalsIgnoreCase("No")) {
				aceptar = false;
				break;
			}

			System.out.println("Por favor, responde adecuadamente");
		}

		// Completar la transacción y generar una nueva transacción actualizada
		Transaccion transNueva = Transaccion.completarTransaccion(transaccion, aceptar);

		// Reemplazar la transacción anterior con la nueva transacción en la lista de transacciones
		Transaccion.getTransacciones().set(Transaccion.getTransacciones().indexOf(transaccion), transNueva);
	}

	static void verFacturas() {
		// Elección del cliente para ver las facturas
		Cliente clienteEscogido = elejirUsuario();

		// Verificar si se seleccionó un cliente válido
		if (clienteEscogido == null)
			return;

		// Verificar si el cliente no tiene facturas por pagar
		if (clienteEscogido.listarFacturas().isEmpty()) {
			System.out.println("No hay facturas por pagar");
		} else {
			// Mostrar las facturas pendientes del cliente
			for (Factura f : clienteEscogido.getFactura()) {
				System.out.println(f);
			}
		}
	}


	static void verTarjetasDisponibles() {
		// Elección del cliente para ver las tarjetas disponibles
		Cliente clienteEscogido = elejirUsuario();

		// Verificar si se seleccionó un cliente válido
		if (clienteEscogido == null)
			return;

		// Mostrar las tarjetas de crédito del cliente
		for (Tarjeta t : clienteEscogido.getTarjetasCredito()) {
			System.out.println(t);
		}

		// Mostrar las tarjetas de débito del cliente
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
		Cliente cliente1 = new Cliente("Dario Gomez", 1);
		Cliente cliente2 = new Cliente("Esteban Betancur", 2);
		Cliente cliente3 = new Cliente("Marta Martínez", 3);
		Cliente cliente4 = new Cliente("Sandra Lopez", 4);
		Cliente cliente5 = new Cliente("Yasuri Yamile", 5);
		
		//TARJETAS DEBITO
		TarjetaDebito tarjetaDebito1 = new TarjetaDebito(123456, Divisa.DOLAR, 3000);
		TarjetaDebito tarjetaDebito2 = new TarjetaDebito(234567, Divisa.EURO, 500);
		TarjetaDebito tarjetaDebito3 = new TarjetaDebito(345678, Divisa.RUBLO_RUSO, 10000);
		TarjetaDebito tarjetaDebito4 = new TarjetaDebito(456789, Divisa.YEN_JAPONES, 100000);
		TarjetaDebito tarjetaDebito5 = new TarjetaDebito(567890, Divisa.PESO_COLOMBIANO, 5000000);
		TarjetaDebito tarjetaDebito6 = new TarjetaDebito(678901, Divisa.DOLAR, 200);
		TarjetaDebito tarjetaDebito7 = new TarjetaDebito(789012, Divisa.EURO, 1500);
		TarjetaDebito tarjetaDebito8 = new TarjetaDebito(890123, Divisa.RUBLO_RUSO, 5000);
		TarjetaDebito tarjetaDebito9 = new TarjetaDebito(901234, Divisa.YEN_JAPONES, 50000);
		TarjetaDebito tarjetaDebito10 = new TarjetaDebito(101112, Divisa.PESO_COLOMBIANO, 10000000);
		TarjetaDebito tarjetaDebito11 = new TarjetaDebito(111213, Divisa.LIBRA_ESTERLINA, 100);
		TarjetaDebito tarjetaDebito12 = new TarjetaDebito(121314, Divisa.DOLAR, 1000);
		TarjetaDebito tarjetaDebito13 = new TarjetaDebito(131415, Divisa.EURO, 2000);
		TarjetaDebito tarjetaDebito14 = new TarjetaDebito(141516, Divisa.RUBLO_RUSO, 7000);
		TarjetaDebito tarjetaDebito15 = new TarjetaDebito(151617, Divisa.YEN_JAPONES, 80000);
		TarjetaDebito tarjetaDebito16 = new TarjetaDebito(161718, Divisa.PESO_COLOMBIANO, 2000000);
		TarjetaDebito tarjetaDebito17 = new TarjetaDebito(171819, Divisa.LIBRA_ESTERLINA, 50);
		TarjetaDebito tarjetaDebito18 = new TarjetaDebito(181920, Divisa.DOLAR, 400);
		TarjetaDebito tarjetaDebito19 = new TarjetaDebito(192021, Divisa.EURO, 2500);
		TarjetaDebito tarjetaDebito20 = new TarjetaDebito(202122, Divisa.RUBLO_RUSO, 8000);
		TarjetaDebito tarjetaDebito21 = new TarjetaDebito(212223, Divisa.YEN_JAPONES, 90000);
		TarjetaDebito tarjetaDebito22 = new TarjetaDebito(222324, Divisa.PESO_COLOMBIANO, 3000000);
		TarjetaDebito tarjetaDebito23 = new TarjetaDebito(232425, Divisa.LIBRA_ESTERLINA, 70);
		TarjetaDebito tarjetaDebito24 = new TarjetaDebito(242526, Divisa.DOLAR, 800);
		TarjetaDebito tarjetaDebito25 = new TarjetaDebito(252627, Divisa.EURO, 3000);
		TarjetaDebito tarjetaDebito26 = new TarjetaDebito(262728, Divisa.RUBLO_RUSO, 6000);
		TarjetaDebito tarjetaDebito27 = new TarjetaDebito(272829, Divisa.YEN_JAPONES, 70000);
		TarjetaDebito tarjetaDebito28 = new TarjetaDebito(283930, Divisa.PESO_COLOMBIANO, 4000000);
		TarjetaDebito tarjetaDebito29 = new TarjetaDebito(293031, Divisa.LIBRA_ESTERLINA, 90);
		TarjetaDebito tarjetaDebito30 = new TarjetaDebito(303132, Divisa.DOLAR, 600);
		TarjetaDebito tarjetaDebito31 = new TarjetaDebito(987654, Divisa.DOLAR, 5000);
		TarjetaDebito tarjetaDebito32 = new TarjetaDebito(8765432, Divisa.EURO, 1000);
		TarjetaDebito tarjetaDebito33 = new TarjetaDebito(7654321, Divisa.RUBLO_RUSO, 25000);
		TarjetaDebito tarjetaDebito34 = new TarjetaDebito(6543210, Divisa.YEN_JAPONES, 100000);
		TarjetaDebito tarjetaDebito35 = new TarjetaDebito(5432109, Divisa.PESO_COLOMBIANO, 5000000);
		TarjetaDebito tarjetaDebito36 = new TarjetaDebito(4321098, Divisa.DOLAR, 300);
		TarjetaDebito tarjetaDebito37 = new TarjetaDebito(3210987, Divisa.EURO, 1500000);
		TarjetaDebito tarjetaDebito38 = new TarjetaDebito(2109876, Divisa.RUBLO_RUSO, 20000);
		TarjetaDebito tarjetaDebito39 = new TarjetaDebito(1098765, Divisa.YEN_JAPONES, 75000);
		TarjetaDebito tarjetaDebito40 = new TarjetaDebito(98765432, Divisa.PESO_COLOMBIANO, 3500000);
		TarjetaDebito tarjetaDebito41 = new TarjetaDebito(246813, Divisa.DOLAR, 8000);
		TarjetaDebito tarjetaDebito42 = new TarjetaDebito(1357924, Divisa.EURO, 500);
		TarjetaDebito tarjetaDebito43 = new TarjetaDebito(9876543, Divisa.RUBLO_RUSO, 30000);
		TarjetaDebito tarjetaDebito44 = new TarjetaDebito(8765432, Divisa.YEN_JAPONES, 50000);
		TarjetaDebito tarjetaDebito45 = new TarjetaDebito(7654321, Divisa.PESO_COLOMBIANO, 10000000);
		TarjetaDebito tarjetaDebito46 = new TarjetaDebito(6543210, Divisa.DOLAR, 200);
		TarjetaDebito tarjetaDebito47 = new TarjetaDebito(5432109, Divisa.EURO, 100000);
		TarjetaDebito tarjetaDebito48 = new TarjetaDebito(123456789, Divisa.LIBRA_ESTERLINA, 5000);
		TarjetaDebito tarjetaDebito49 = new TarjetaDebito(345678901, Divisa.LIBRA_ESTERLINA, 10000);
		TarjetaDebito tarjetaDebito50 = new TarjetaDebito(456789012, Divisa.LIBRA_ESTERLINA, 20000);


		
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
		TarjetaCredito tarjetaCredito13 = new TarjetaCredito(74857485, Divisa.DOLAR, 6000, 1.2);
		TarjetaCredito tarjetaCredito14 = new TarjetaCredito(38475895, Divisa.EURO, 2500, 0.5);
		TarjetaCredito tarjetaCredito15 = new TarjetaCredito(98347598, Divisa.RUBLO_RUSO, 80000, 0.6);
		TarjetaCredito tarjetaCredito16 = new TarjetaCredito(57649827, Divisa.YEN_JAPONES, 70000, 0.3);
		TarjetaCredito tarjetaCredito17 = new TarjetaCredito(123456789, Divisa.PESO_COLOMBIANO, 1500000, 2.5);
		TarjetaCredito tarjetaCredito18 = new TarjetaCredito(43789563, Divisa.LIBRA_ESTERLINA, 3000, 0.8);
		TarjetaCredito tarjetaCredito19 = new TarjetaCredito(79253485, Divisa.DOLAR, 4000, 1.0);
		TarjetaCredito tarjetaCredito20 = new TarjetaCredito(21394785, Divisa.EURO, 1500, 0.3);
		TarjetaCredito tarjetaCredito21 = new TarjetaCredito(19384756, Divisa.RUBLO_RUSO, 60000, 0.7);
		TarjetaCredito tarjetaCredito22 = new TarjetaCredito(54879324, Divisa.YEN_JAPONES, 50000, 0.5);
		TarjetaCredito tarjetaCredito23 = new TarjetaCredito(28573649, Divisa.PESO_COLOMBIANO, 2000000, 3.0);
		TarjetaCredito tarjetaCredito24 = new TarjetaCredito(64329875, Divisa.LIBRA_ESTERLINA, 4000, 1.2);
		TarjetaCredito tarjetaCredito25 = new TarjetaCredito(78345364, Divisa.DOLAR, 4500, 0.9);
		TarjetaCredito tarjetaCredito26 = new TarjetaCredito(37825375, Divisa.EURO, 1800, 0.4);
		TarjetaCredito tarjetaCredito27 = new TarjetaCredito(23758634, Divisa.RUBLO_RUSO, 50000, 0.6);
		TarjetaCredito tarjetaCredito28 = new TarjetaCredito(23849570, Divisa.YEN_JAPONES, 60000, 0.4);
		TarjetaCredito tarjetaCredito29 = new TarjetaCredito(95834575, Divisa.PESO_COLOMBIANO, 3000000, 4.0);
		TarjetaCredito tarjetaCredito30 = new TarjetaCredito(58743634, Divisa.LIBRA_ESTERLINA, 5000, 1.5);
		
		cliente1.agregarTarjetasDebito(tarjetaDebito1, tarjetaDebito2, tarjetaDebito3, tarjetaDebito5, tarjetaDebito11, tarjetaDebito30, tarjetaDebito33, tarjetaDebito39);
		cliente1.agregarTarjetasCredito(tarjetaCredito1, tarjetaCredito2, tarjetaCredito7, tarjetaCredito9, tarjetaCredito11, tarjetaCredito17, tarjetaCredito22);

		cliente2.agregarTarjetasDebito(tarjetaDebito6, tarjetaDebito31, tarjetaDebito32, tarjetaDebito36, tarjetaDebito38, tarjetaDebito45, tarjetaDebito49);
		cliente2.agregarTarjetasCredito(tarjetaCredito3, tarjetaCredito5, tarjetaCredito10, tarjetaCredito20, tarjetaCredito28, tarjetaCredito26);

		cliente3.agregarTarjetasDebito(tarjetaDebito4, tarjetaDebito7, tarjetaDebito9, tarjetaDebito34, tarjetaDebito37, tarjetaDebito42, tarjetaDebito43);
		cliente3.agregarTarjetasCredito(tarjetaCredito6, tarjetaCredito8, tarjetaCredito12, tarjetaCredito18, tarjetaCredito21);

		cliente4.agregarTarjetasDebito(tarjetaDebito8, tarjetaDebito10, tarjetaDebito40, tarjetaDebito35, tarjetaDebito41, tarjetaDebito47);
		cliente4.agregarTarjetasCredito(tarjetaCredito4, tarjetaCredito13, tarjetaCredito15, tarjetaCredito23,	tarjetaCredito27);

		cliente5.agregarTarjetasDebito(tarjetaDebito12, tarjetaDebito44, tarjetaDebito46, tarjetaDebito48, tarjetaDebito50);
		cliente5.agregarTarjetasCredito(tarjetaCredito14, tarjetaCredito16, tarjetaCredito24, tarjetaCredito30, tarjetaCredito29);

		// FACTURAS
		Factura factura1 = new Factura(cliente1, 1000000.0, 12, tarjetaDebito16);
		Factura factura2 = new Factura(cliente2, 9000000.0, 36, tarjetaDebito28);
		Factura factura3 = new Factura(cliente3, 56.0, 4, tarjetaDebito29);
		Factura factura4 = new Factura(cliente4, 16000, 8, tarjetaDebito20);
		Factura factura5 = new Factura(cliente5, 800000.0, 8, tarjetaDebito22);
		Factura factura6 = new Factura(cliente1, 24440, 6, tarjetaDebito27);
		Factura factura7 = new Factura(cliente2, 150.0, 2, tarjetaDebito24);
		Factura factura10 = new Factura(cliente5, 20000.0, 14, tarjetaDebito26);
		Factura factura11 = new Factura(cliente1, 200.0, 7, tarjetaDebito25);
		Factura factura12 = new Factura(cliente2, 150.0, 8, tarjetaDebito23);
		Factura factura13 = new Factura(cliente3, 180.0, 4, tarjetaDebito17);
		Factura factura14 = new Factura(cliente4, 120.0, 3, tarjetaDebito18);
		Factura factura15 = new Factura(cliente5, 90.0, 2, tarjetaDebito19);

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
	
