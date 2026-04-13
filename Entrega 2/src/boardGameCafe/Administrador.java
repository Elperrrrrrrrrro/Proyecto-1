package boardGameCafe;
import java.io.Serializable;
public class Administrador implements Serializable {
	private String nombre;
	private String documentoIdentidad;
	private String login;
	private String password;
	
	public Administrador(String nombre, String documentoIdentidad, String login, String password) {
		this.nombre = nombre;
		this.documentoIdentidad = documentoIdentidad;
		this.login = login;
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDocumentoIdentidad() {
		return documentoIdentidad;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setDocumentoIdentidad(String documentoIdentidad) {
		this.documentoIdentidad = documentoIdentidad;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
