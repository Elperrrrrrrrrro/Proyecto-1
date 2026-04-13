package boardGameCafe;
import java.util.ArrayList;
import java.util.List;
public class Cliente {
    private String nombre;
    private String documento;
    private int puntosFidelidad;
    private String login;
    private String password;
    private List<juegoMesa> juegosFavoritos;

    public Cliente(String nombre, String documento, String login, String password) {
        this.nombre = nombre;
        this.documento = documento;
        this.login = login;
        this.password = password;
        this.puntosFidelidad = 0;
        this.juegosFavoritos = new ArrayList<>();
    }

    public void agregarPuntosFidelidad(double cantidad) {
        this.puntosFidelidad += (int) cantidad;
    }

    public void adicionarJuegoFavorito(juegoMesa juego) {
        if (!juegosFavoritos.contains(juego)) {
            juegosFavoritos.add(juego);
        }
    }

    // Getters
    public String getNombre() {
    	return nombre; 
    	}
    public String getDocumento() { 
    	return documento; 
    	}
    public int getPuntosFidelidad() {
    	return puntosFidelidad; 
    	}
    public String getLogin() { 
    	return login;
    	}
    public List<juegoMesa> getJuegosFavoritos() {
        return juegosFavoritos;
    }
}