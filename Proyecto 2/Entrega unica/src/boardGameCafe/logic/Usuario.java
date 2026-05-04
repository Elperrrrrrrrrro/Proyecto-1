package boardGameCafe.logic;
import java.io.Serializable;
public abstract class Usuario implements Serializable{
	private static final long serialVersionUID = 1L;
    private String Nombre;
    private String DocumentoIdentidad;
    private String Login;
    private String Password;

public Usuario(String Nombre, String DocumentoIdentidad,
     String Login, String Password){
        this.Nombre = Nombre;
    	this.DocumentoIdentidad = DocumentoIdentidad;
    	this.Login = Login;
    	this.Password = Password;

}

public String getNombre() {
	return Nombre;
}

public String getDocumentoIdentidad() {
	return DocumentoIdentidad;
}

public String getLogin() {
	return Login;
}

public String getPassword() {
	return Password;
}
}
