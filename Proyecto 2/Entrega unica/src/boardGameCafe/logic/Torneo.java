package boardGameCafe.logic;
import java.util.ArrayList;
import java.io.Serializable;
public class Torneo implements Serializable {
	private static final long serialVersionUID = 1L;
    private String diaSemana;
    private ArrayList<Usuario> participantes;
    private JuegoMesa juego;
    private String id;
    private int numeroPartisipantes;

        public Torneo(String diaSemana, ArrayList<Usuario> participantes, JuegoMesa juego, int numeroPartisipantes, String id ) {
        this.diaSemana = diaSemana;
        this.participantes = participantes;
        this.juego = juego;
        this.numeroPartisipantes = numeroPartisipantes;
        this.id = id;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public ArrayList<Usuario> getParticipantes() {
        return participantes;
    }

    public JuegoMesa getJuego() {
        return juego;
    }

    public int getNumeroPartisipantes() {
        return numeroPartisipantes;
    }
    public String getId() {
        return id;
    }

    public void agregarParticipante(Usuario participante) {
        participantes.add(participante);
    }

}
