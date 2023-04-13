import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ArrayList;

public abstract class Usuario {
	private String gmail;
	private String contrasena;
	private String nombre;
	int noDoc;
	static ArrayList<Usuario>  usuarios = new ArrayList<Usuario>();
	public Usuario(String gmail, String contrasena, String nombre, int noDoc) {
		this.gmail = gmail;
		this.contrasena = contrasena;
		this.nombre = nombre;
		this.noDoc = noDoc;
	}
	public static boolean iniciarSecion(String gmail, String contrasena) {
		boolean iniciar = false;
		for(int i = 0; i< usuarios.size(); i++) {
			if(usuarios.get(i).gmail.equals(gmail) && usuarios.get(i).contrasena.equals(contrasena)) {
				iniciar = true;
			}
		}
		return iniciar;
	}
	public static Usuario encontrarUsuario(String gmail) {
		Usuario usuario = null;
		for(int i = 0; i< usuarios.size(); i++) {
			if(usuarios.get(i).gmail.equals(gmail)) {
				usuario = usuarios.get(i);
			}
		}
		return usuario;
	}
	public abstract boolean admin();
	public abstract void reservar(Funcion funcion, int silla);
	public String getGmail() {
		return gmail;
	}
	
}
