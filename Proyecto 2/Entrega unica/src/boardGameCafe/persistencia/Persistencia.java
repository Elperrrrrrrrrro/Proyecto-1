package boardGameCafe;
import java.io.*;
public class Persistencia {

		    private static final String ARCHIVO = "sistema.dat";

		    public static void guardarSistema(SistemaBoardGameCafe sistema) {
		        try (ObjectOutputStream out = new ObjectOutputStream(
		                new FileOutputStream(ARCHIVO))) {

		            out.writeObject(sistema);

		            System.out.println(" Sistema guardado");

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }

		    public static SistemaBoardGameCafe cargarSistema() {
		        try (ObjectInputStream in = new ObjectInputStream(
		                new FileInputStream(ARCHIVO))) {

		            return (SistemaBoardGameCafe) in.readObject();

		        } catch (Exception e) {
		            System.out.println("Sistema nuevo creado");
		            return new SistemaBoardGameCafe();
		        }
		    }
}
