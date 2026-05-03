package boardGameCafe.logic;

public class TorneoCompetitivo extends Torneo{
    private double costo;
    private double premio;

    public TorneoCompetitivo(String diaSemana, java.util.ArrayList<Usuario> participantes, 
                             JuegoMesa juego, int numeroPartisipantes, double premio,double costo) {
        super(diaSemana, participantes, juego, numeroPartisipantes );
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
