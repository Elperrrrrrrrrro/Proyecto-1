package boardGameCafe.logic;
import java.util.ArrayList;

public class Torneo {
    private String diaSemana;
    private ArrayList<Usuario> participantes;
    private JuegoMesa juego;
    private String ID;
    private int numeroPartisipantes;

        public Torneo(String diaSemana, ArrayList<Usuario> participantes, JuegoMesa juego, int numeroPartisipantes, String ID ) {
        this.diaSemana = diaSemana;
        this.participantes = participantes;
        this.juego = juego;
        this.numeroPartisipantes = numeroPartisipantes;
        this.ID = ID;
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
    public String getID() {
        return ID;
    }

    public void agregarParticipante(Usuario participante) {
        participantes.add(participante);
    }

}
