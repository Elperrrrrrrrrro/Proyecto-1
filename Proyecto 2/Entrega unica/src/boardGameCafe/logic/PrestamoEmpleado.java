package boardGameCafe.logic;
import java.time.LocalDateTime;
import java.util.Map;
import java.io.Serializable;

public class PrestamoEmpleado extends Prestamo implements Serializable{
	private static final long serialVersionUID = 1L;

    private Empleado empleado;
    private Map<String, Turno> turnos;

    public PrestamoEmpleado(String idPrestamo, JuegoMesa juego, LocalDateTime inicio, LocalDateTime entrega,
                            Empleado empleado, Map<String, Turno> turnos) {
        super(idPrestamo,juego, inicio, entrega);
        this.empleado = empleado;
        this.turnos = turnos;
    }


    public boolean sonAptos(Mesa mesa) {
        // 1. ver si el juego esta disponible
        if (juego.isPrestado()) {
            return false;
        }
        // ver que no este en turno
        for (Turno t : turnos.values()) {
            if (t.getEmpleadosAsignados().contains(empleado)) {
                return false;
            }
        }

        return true;
    }
}
