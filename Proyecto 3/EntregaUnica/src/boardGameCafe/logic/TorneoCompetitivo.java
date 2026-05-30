package boardGameCafe.logic;
import java.io.Serializable;
public class TorneoCompetitivo extends Torneo implements Serializable{
	private static final long serialVersionUID = 1L;
    private double costo;
    private double premio;

    public TorneoCompetitivo(String diaSemana, java.util.ArrayList<Usuario> participantes, 
                             JuegoMesa juego, int numeroPartisipantes, double premio,double costo, String ID) {
        super(diaSemana, participantes, juego, numeroPartisipantes ,ID);
        this.premio = premio;
        this.costo = costo;
    }

    public double getPremio() {
        return premio;
    }

    public double getCosto(){
        return costo;
    }

}
