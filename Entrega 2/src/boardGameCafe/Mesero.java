package boardGameCafe;

import java.util.List;

public class Mesero extends Empleado {
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
