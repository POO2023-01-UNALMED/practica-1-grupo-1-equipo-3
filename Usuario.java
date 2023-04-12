public abstract class Usuario {
    public String Gmail;
    protected String Contrasena;
    protected String nombre;
    protected int NoDoc;

    public Usuario(String gmail, String contrasena, String nombre, int noDoc) {
        Gmail = gmail;
        Contrasena = contrasena;
        this.nombre = nombre;
        NoDoc = noDoc;
    }
    public abstract boolean iniciarSesion(String correo, String contrasena);
}