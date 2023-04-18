package src.gestorAplicacion.usuarios;
public abstract class Usuario {
	public String Gmail;
	protected String Contrasena;
	protected String nombre;
	protected long NoDoc;

	public Usuario(String gmail, String contrasena, String nombre, long noDoc) {
		this.Gmail = gmail;
		this.Contrasena = contrasena;
		this.nombre = nombre;
		this.NoDoc = noDoc;
	}
}