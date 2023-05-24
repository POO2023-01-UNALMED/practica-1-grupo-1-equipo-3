package baseDatos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import gestorAplicacion.infraestructura.Banco;

public class Serializador {
	private static File rutaTemp = new File("Banco\\src\\baseDatos\\temp");
	
	public static void serializar(Banco banco) {
		FileOutputStream fos;
		ObjectOutputStream oos;
		File[] docs = rutaTemp.listFiles();
		PrintWriter pw;
		
//		try {
//            // Crear archivo de clientes
//            File archivoClientes = new File(rutaTemp, "clientes.dat");
//            archivoClientes.createNewFile();
//
//            // Crear archivo de canales
//            File archivoCanales = new File(rutaTemp, "canales.dat");
//            archivoCanales.createNewFile();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
		
		for(File file: docs) {
			try {
				pw = new PrintWriter(file);
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
		}
		
		for(File file: docs) {
			if(file.getAbsolutePath().contains("clientes")) {
				try {
					fos = new FileOutputStream(file);
					oos = new ObjectOutputStream(fos);
					oos.writeObject(banco.getClientes());
					
				}catch(FileNotFoundException e) {
					e.printStackTrace();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}else if (file.getAbsolutePath().contains("canales")) {
				try {
					fos = new FileOutputStream(file);
					oos = new ObjectOutputStream(fos);
					oos.writeObject(banco.getCanales());
					
				}catch(FileNotFoundException e) {
					e.printStackTrace();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
