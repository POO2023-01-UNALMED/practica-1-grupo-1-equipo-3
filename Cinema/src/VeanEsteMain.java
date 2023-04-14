package Cinema.src;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VeanEsteMain {
    public static void main(String[] args) throws ParseException {
        Cliente cliente = new Cliente("carlos@gmail.com", "1234", "Carlos Guarin", 123456, 20);
        Cliente cliente2 = new Cliente("carlosasdf@gmail.com", "1234", "Carlos Guarin", 1234567, 20);
        Pelicula pelicula = new Pelicula(60, "peli", "yo", "Genero", "clasificación", "formato");
        String fechaString = "2022-04-15 20:00";
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date fecha = formato.parse(fechaString);
        Sala sala = new Sala(1, 13, 13);
        Funcion funcion = new Funcion(pelicula, fecha, sala);

        Reserva.reservar(cliente, funcion, 169);
        Reserva.reservar(cliente, funcion, 10);
        Reserva.reservar(cliente2, funcion, 1);
        System.out.println(Reserva.getReservas());
    }
}
