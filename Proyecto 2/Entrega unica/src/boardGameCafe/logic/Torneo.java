package boardGameCafe.logic;
import java.util.ArrayList;

public class Torneo {
    private String diaSemana;
    private ArrayList<Usuario> participantes;
    private JuegoMesa juego;
    private int numeroPartisipantes;

        public Torneo(String diaSemana, ArrayList<Usuario> participantes, JuegoMesa juego, int numeroPartisipantes) {
        this.diaSemana = diaSemana;
        this.participantes = participantes;
        this.juego = juego;
        this.numeroPartisipantes = numeroPartisipantes;
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


}
