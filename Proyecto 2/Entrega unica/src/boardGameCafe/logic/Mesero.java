package boardGameCafe.logic;
import java.io.Serializable;
import java.util.List;

public class Mesero extends Empleado implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<JuegoMesa> juegosConocidos;
	
	public Mesero(String nombre, String documentoIdentidad, int edad, String login, String password, List<JuegoMesa> juegosConocidos) {
		super(nombre, documentoIdentidad, edad, login, password);
		this.juegosConocidos = juegosConocidos;
	}
	
	public List<JuegoMesa> getJuegosConocidos() {
		return juegosConocidos;
	}
	public void addJuegoConocido(JuegoMesa juego) {
		this.juegosConocidos.add(juego);
	}
}
