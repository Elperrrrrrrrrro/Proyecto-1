package boardGameCafe.logic;
import java.io.Serializable;

public class Cocinero extends Empleado implements Serializable{
	
	public Cocinero(String nombre, String documentoIdentidad, int edad, String login, String password) {
		super(nombre, documentoIdentidad, edad, login, password);
	}

}
