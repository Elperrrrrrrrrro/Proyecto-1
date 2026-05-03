package boardGameCafe.logic;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class Cliente extends Usuario   implements Serializable {

    private int puntosFidelidad;


    private List<JuegoMesa> juegosFavoritos;

    public Cliente(String nombre, String documento, String login, String password) {

        super(nombre, documento, login, password);
        this.puntosFidelidad = 0;
        this.juegosFavoritos = new ArrayList<>();
    }

    public void agregarPuntosFidelidad(double cantidad) {
        this.puntosFidelidad += (int) cantidad;
    }

    public void adicionarJuegoFavorito(JuegoMesa juego) {
        if (!juegosFavoritos.contains(juego)) {
            juegosFavoritos.add(juego);
        }
    }

    // Getters

    public int getPuntosFidelidad() {
    	return puntosFidelidad; 
    	}

    public List<JuegoMesa> getJuegosFavoritos() {
        return juegosFavoritos;
    }
}