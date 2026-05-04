package boardGameCafe.logic ;
import java.util.*;
import java.io.Serializable;
public class TorneoAmistoso extends Torneo implements Serializable{
	private static final long serialVersionUID = 1L;
    private double PorsentajeDedescuento;


    public TorneoAmistoso(String diaSemana, ArrayList<Usuario> participantes,
         JuegoMesa juego, int numeroPartisipantes, 
         double PorsentajeDedescuento,String ID) {


        super(diaSemana, participantes, juego, numeroPartisipantes, ID);
        this.PorsentajeDedescuento = PorsentajeDedescuento;
    }

    public double getPorsentajeDedescuento() {
        return PorsentajeDedescuento;
    }

}
