package gestorAplicacion.entidades_de_negocio;

import java.util.ArrayList;
import java.util.Scanner;

import gestorAplicacion.infraestructura.Banco;
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
				System.out.println("1. Para ver facturas\n2. Para ver las tarjetas disponibles\n3. Para pagar una factura\n4. Para salir");
				String entrada2 = scanner.nextLine();
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
					break;
				}
			}
			
		}
		
		scanner.close();
	}

	public static void setup(){ //Función que inicializa algunos objetos para comenzar a experimentar
		Cliente cliente1 = new Cliente("Dario", 1);
		Cliente cliente2 = new Cliente("Esteban", 2);
		Cliente cliente3 = new Cliente("Marta", 3);

		TarjetaDebito tarjetafac = new TarjetaDebito(666, Divisa.getDivisas()[0], 10);
		TarjetaDebito tarjeta1 = new TarjetaDebito(1, Divisa.getDivisas()[0], 1000);
		TarjetaDebito tarjeta2 = new TarjetaDebito(1, Divisa.getDivisas()[0], 10);
		TarjetaCredito tarjetaCredito = new TarjetaCredito(2145, Divisa.getDivisas()[0], 3000, (float) 1.5);
		cliente1.agregarTarjetasDebito(tarjeta1, tarjeta2);
		cliente1.agregarTarjetasCredito(tarjetaCredito);

		Factura factura1 = new Factura(cliente1, 100.0, 5, tarjetafac);
		Factura factura2 = new Factura(cliente1, 90, 8, tarjetafac);

	}

}
