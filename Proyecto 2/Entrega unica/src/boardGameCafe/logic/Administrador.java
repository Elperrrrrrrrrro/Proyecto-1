package boardGameCafe;
import java.io.Serializable;
public class Administrador extends Usuario  implements  Serializable {




	
	public Administrador(String nombre, String documentoIdentidad, String login, String password) {
		super(nombre, documentoIdentidad, login, password);
		
	}
	
}
