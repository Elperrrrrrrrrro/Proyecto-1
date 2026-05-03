package boardGameCafe;
import java.io.Serializable;
public class Administrador extends Usuario  implements  Serializable {

	private static final long serialVersionUID = 1L;

	public Administrador(String nombre, String documentoIdentidad, String login, String password) {
		super(nombre, documentoIdentidad, login, password);
		
	}
	
}
