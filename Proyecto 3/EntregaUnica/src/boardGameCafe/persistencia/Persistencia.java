package boardGameCafe.persistencia;
import java.io.*;
import boardGameCafe.system.SistemaBoardGameCafe;

public class Persistencia {

		    private static final String CARPETA = "datos";
		    private static final String ARCHIVO = CARPETA + File.separator + "sistema.dat";

		    public static void guardarSistema(SistemaBoardGameCafe sistema) {
		    	
		    	File directorio = new File(CARPETA);
		        if (!directorio.exists()) {
		            directorio.mkdirs();
		        }
		    	
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
