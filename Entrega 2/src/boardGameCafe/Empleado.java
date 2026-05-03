package boardGameCafe;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Empleado extends Usuario   implements  Serializable{
	private static final long serialVersionUID = 1L;
	
    private List<JuegoMesa> juegosConocidos;

    public Empleado(String nombre, String documentoIdentidad, int edad,
         String login, String password) {
        
        super(nombre, documentoIdentidad, login, password);
        this.juegosConocidos = new ArrayList<>();
    }

    public void adicionarJuegoConocido(JuegoMesa juego) {
        if (!juegosConocidos.contains(juego)) {
            juegosConocidos.add(juego);
        }
    }
    
  public List<JuegoMesa> getJuegosConocidos() {
            return juegosConocidos;
  }

}