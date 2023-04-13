
public class Cliente extends Usuario {
	int edad;
	public Cliente(String gmail, String contrasena, String nombre, int noDoc, int edad) {
		super(gmail, contrasena, nombre, noDoc);
		this.edad = edad;
	}
	static void registrarse(String gmail, String contrasena, String nombre, int noDoc, int edad) {
		boolean usado = false;
		for(int i = 0; i< usuarios.size(); i++) {
			if(Usuario.usuarios.get(i).getGmail().equals(gmail)) {
				System.out.println("Este correo ya está siendo usado");
				
			}
		}
		Usuario.usuarios.add(new Cliente(gmail, contrasena, nombre, noDoc, edad));
	}
	
	public void reservar(Funcion funcion, int silla) {
		if(funcion.getSala().asientos.get(silla).isDisponible()) {
			Reserva.reservas.add(new Reserva(this, funcion, funcion.getSala().asientos.get(silla), funcion.getSala()));
			funcion.getSala().asientos.get(silla).setDisponibilidad(false);
		} else {
			System.out.println("No se pudo hacer la reserva, debido a que la silla ya está reservada");
		}
	}
	@Override
	public boolean admin() {
		return false;
	}
	
}
