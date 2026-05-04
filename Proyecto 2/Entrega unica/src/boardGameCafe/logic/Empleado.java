package boardGameCafe.logic;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Empleado extends Usuario   implements  Serializable{
	private static final long serialVersionUID = 1L;


    public Empleado(String nombre, String documentoIdentidad, int edad,
         String login, String password) {
        
        super(nombre, documentoIdentidad, login, password);
    }


	public boolean puedeComprar(boolean enTurno, boolean hayClientes) {
		return !enTurno || !hayClientes;
	}

}