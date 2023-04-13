package Cinema.src;
public abstract class Usuario {
	public String Gmail;
	protected String Contrasena;
	protected String nombre;
	protected int NoDoc;

	public Usuario(String gmail, String contrasena, String nombre, int noDoc) {
		this.Gmail = gmail;
		this.Contrasena = contrasena;
		this.nombre = nombre;
		this.NoDoc = noDoc;
	}
	public abstract boolean iniciarSesion(String correo, String contrasena);
}