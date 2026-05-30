package boardGameCafe.logic;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.time.LocalDateTime;
public class PrestamoEmpleadoTest {
	@Test
	public void testSonAptosEmpleado() {

	    Empleado empleado = new Empleado("Juan", "123", 25, "juan", "123");

	    JuegoMesa juego = new JuegoMesa(
	            "J1", "Catan", 2010, "Devir",
	            2, 4, "Estrategia",
	            false, "nuevo", false, 50000
	    );
	    Map<String, Turno> turnos = new HashMap<>(); 
	    PrestamoEmpleado prestamo = new PrestamoEmpleado( "P1", juego, LocalDateTime.now(),null,empleado,turnos
	    );
	    Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);
	    assertTrue(prestamo.sonAptos(mesa));
	}
	@Test
	public void testSonAptosJuegoPrestado() {
	    Empleado empleado = new Empleado("Juan", "123", 25, "juan", "123");
	    JuegoMesa juego = new JuegoMesa(
	            "J1", "Catan", 2010, "Devir",
	            2, 4, "Estrategia",
	            false, "nuevo", false, 50000
	    );
	    juego.setPrestado(true); // ya está prestado
	    Map<String, Turno> turnos = new HashMap<>();
	    PrestamoEmpleado prestamo = new PrestamoEmpleado("P1", juego, LocalDateTime.now(),null,empleado,turnos
	    );
	    Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);
	    assertFalse(prestamo.sonAptos(mesa));
	}
	@Test
	public void testSonAptosEmpleadoEnTurno() {
	    Empleado empleado = new Empleado("Juan", "123", 25, "juan", "123");
	    JuegoMesa juego = new JuegoMesa(
	            "J1", "Catan", 2010, "Devir",
	            2, 4, "Estrategia",
	            false, "nuevo", false, 50000
	    );
	    Turno turno = new Turno("Lunes"); 
	    turno.getEmpleadosAsignados().add(empleado);

	    Map<String, Turno> turnos = new HashMap<>();
	    turnos.put("Lunes", turno);
	    PrestamoEmpleado prestamo = new PrestamoEmpleado("P1",juego,LocalDateTime.now(),null,empleado,turnos
	    );

	    Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);
	    assertFalse(prestamo.sonAptos(mesa));
	}

}
