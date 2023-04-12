import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cliente extends Usuario{
    private int edad;
    protected static List<Cliente> clientes = new ArrayList<>();

    public Cliente(String gmail, String contrasena, String nombre, int noDoc, int edad) {
        super(gmail, contrasena, nombre, noDoc);
        this.edad = edad;
    }

    public static void registrarse(Cliente cliente){
        for (Cliente c: Cliente.clientes) {
            if(Objects.equals(c.Gmail, cliente.Gmail)){
                System.out.println("El correo electrónico " + c.Gmail + " ya está registrado.");
                return;
            }
            if(c.NoDoc==cliente.NoDoc){
                System.out.println("El numero de documento " + c.NoDoc + " ya está registrado.");
                return;
            }
        }
        Cliente.clientes.add(cliente);
    }

    @Override
    public boolean iniciarSesion(String correo, String contrasena) {
        for (Cliente cliente : clientes) {
            if (Objects.equals(cliente.Gmail, correo) && Objects.equals(cliente.Contrasena, contrasena)) {
                System.out.println("Bienvenido, " + cliente.nombre + "!");
                return true;
            }
        }
        System.out.println("Correo electrónico o contraseña incorrectos.");
        return false;
    }
}