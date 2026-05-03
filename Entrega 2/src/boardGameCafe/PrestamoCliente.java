package boardGameCafe;
import java.time.LocalDateTime;
import java.io.Serializable;

public class PrestamoCliente extends Prestamo implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private Cliente cliente;
    
    //cambiar el constuctor en lase controladora
    public PrestamoCliente(String idPrestamo,JuegoMesa juego, LocalDateTime inicio, LocalDateTime entrega, Cliente cliente) {
        super(idPrestamo,juego, inicio, entrega);
        this.cliente = cliente;
    }

    @Override
    public boolean sonAptos(Mesa mesa) {
         //verifica que hay clientes en la mesa 
    	if (mesa == null || mesa.getClienteActual() == null) 
    		return false;

        // por mesa solo puede haber max 2 juegos
        if (mesa.getPrestamoActicos().size() >= 2) 
        	return false;
        // verificar la cantidad de perssonas que pueden jugar
        int personas = mesa.getCantidadPersonas();
        if (personas < juego.getMinJugadores() || personas > juego.getMaxJugadores()) {
            return false;
        }
        // validacion de la edad
        if (mesa.isHayMenores() && juego.isSoloAdultos()) {
            return false;
        }

        return true;
    }
    public Cliente getCliente() {
        return cliente;
        
    }
}

   