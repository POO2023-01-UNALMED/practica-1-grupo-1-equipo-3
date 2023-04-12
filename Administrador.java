import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Administrador extends Usuario{
    protected static List<Administrador> administradores = new ArrayList<>();

    public Administrador(String gmail, String contrasena, String nombre, int noDoc) {
        super(gmail, contrasena, nombre, noDoc);
    }

    @Override
    public boolean iniciarSesion(String correo, String contrasena) {
        for (Administrador admin : administradores) {
            if (Objects.equals(admin.Gmail, correo) && Objects.equals(admin.Contrasena, contrasena)) {
                System.out.println("Bienvenido, " + admin.nombre + "!");
                return true;
            }
        }
        System.out.println("Correo electrónico o contraseña incorrectos.");
        return false;
    }
}