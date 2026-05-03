package boardGameCafe.logic ;
import java.util.*;

public class TorneoAmistoso extends Torneo {
    private double PorsentajeDedescuento;


    public TorneoAmistoso(String diaSemana, ArrayList<Usuario> participantes,
         JuegoMesa juego, int numeroPartisipantes, 
         double PorsentajeDedescuento) {


        super(diaSemana, participantes, juego, numeroPartisipantes);
        this.PorsentajeDedescuento = PorsentajeDedescuento;
    }

    public double getPorsentajeDedescuento() {
        return PorsentajeDedescuento;
    }

}
