package src.gestorAplicacion.usuarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Usuario {
	public String Gmail;
	protected String Contrasena;
	protected String nombre;
	protected long NoDoc;
	private static List<Usuario> usuarios = new ArrayList<>();

	public Usuario(String gmail, String contrasena, String nombre, long noDoc) {
		this.Gmail = gmail;
		this.Contrasena = contrasena;
		this.nombre = nombre;
		this.NoDoc = noDoc;
	}
	public static Usuario iniciarSesion(String correo, String contrasena) {
		Usuario u = null;
		for (Usuario usuario : usuarios) {
			if (usuario.Gmail.equals(correo) && usuario.Contrasena.equals(contrasena)) {
				System.out.println("Bienvenido, " + usuario.nombre + "!");
				u = usuario;
			}
		}
		if(u == null){
			System.out.println("Correo electrónico o contraseña incorrectos.");
		}
		return u;
	}

	public static List<Usuario> getUsuarios() {
		return usuarios;
	}

	public abstract void interfazUsuario(); //Aquí va lo que el usuario ve al iniciar sessión

}