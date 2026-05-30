package boardGameCafe.logic;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;
public class TurnoTest {
	@Test
	public void testAdicionarEmpleado() {
	    Turno turno = new Turno("Lunes");
	    Empleado empleado = new Empleado("Juan", "123", 25, "juan", "123");
	    turno.adicionarEmpleado(empleado);
	    assertEquals(1, turno.getEmpleadosAsignados().size());
	}
	@Test
	public void testValidarMinimoOperativoMenosDe3() {
	    Turno turno = new Turno("Lunes");
	    turno.adicionarEmpleado(new Mesero("M1", "1", 20, "m1", "123",new ArrayList<>()));
	    turno.adicionarEmpleado(new Cocinero("C1", "2", 30, "c1", "123"));
	    // hay solo 2 empleados
	    assertFalse(turno.validarMinimoOperativo());
	}
	
	@Test
	public void testValidarMinimoOperativoSinRoles() {
	    Turno turno = new Turno("Lunes");
	    turno.adicionarEmpleado(new Mesero("M1", "1", 20, "m1", "123",new ArrayList<>()));
	    turno.adicionarEmpleado(new Mesero("M2", "2", 22, "m2", "123",new ArrayList<>()));
	    turno.adicionarEmpleado(new Mesero("M3", "3", 23, "m3", "123",new ArrayList<>()));
	    // no hay cocinero asi q no se puede
	    assertFalse(turno.validarMinimoOperativo());
	}
	@Test
	public void testValidarMinimoOperativoCorrecto() {
	    Turno turno = new Turno("Lunes");
	    turno.adicionarEmpleado(new Mesero("M1", "1", 20, "m1", "123",new ArrayList<>()));
	    turno.adicionarEmpleado(new Mesero("M2", "2", 22, "m2", "123",new ArrayList<>()));
	    turno.adicionarEmpleado(new Cocinero("C1", "3", 30, "c1", "123"));
	    //  2 meseros y 1 cocinero
	    assertTrue(turno.validarMinimoOperativo());
	}
}
