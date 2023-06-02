//Autores:
//Jose Miguel Pulgarin A.
//Carlos Guarin
//Dario Alexander Penagos V.

package baseDatos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import gestorAplicacion.entidades_de_negocio.Cliente;
import gestorAplicacion.infraestructura.Banco;
import gestorAplicacion.infraestructura.Canal;

public class Deserializador {
    private static File rutaTemp = new File("Banco\\src\\baseDatos\\temp");

    public static void deserializar(Banco banco){
    	File[] docs = rutaTemp.listFiles();
        FileInputStream fis;
        ObjectInputStream ois;

        for (File file: docs){
        	if (file.getAbsolutePath().contains("clientes")) {
                try {
                    fis = new FileInputStream(file);
                    ois = new ObjectInputStream(fis);
                    
                    banco.setClientes((ArrayList<Cliente>) ois.readObject());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }//catch (ClassNotFoundException e){e.printStackTrace();}
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                                }
            } else if (file.getAbsolutePath().contains("canales")){
                try {
                    fis = new FileInputStream(file);
                    ois = new ObjectInputStream(fis);
                    
                    banco.setCanales((ArrayList<Canal>) ois.readObject());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}