package boardGameCafe;
import java.time.LocalDateTime;
import java.util.Map;

public class PrestamoEmpleado extends Prestamo {

    private Empleado empleado;
    private Map<String, Turno> turnos;

    public PrestamoEmpleado(String idPrestamo, JuegoMesa juego, LocalDateTime inicio, LocalDateTime entrega,
                            Empleado empleado, Map<String, Turno> turnos) {
        super(idPrestamo,juego, inicio, entrega);
        this.empleado = empleado;
        this.turnos = turnos;
    }

    @Override
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
