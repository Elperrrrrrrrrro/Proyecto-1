package boardGameCafe.logic;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.time.LocalDateTime;
public class PrestamoClienteTest {
	@Test
	public void testSonAptosCorrecto() {
	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");
	    Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);
	    mesa.setClienteActual(cliente);
	    JuegoMesa juego = new JuegoMesa( "J1", "Catan", 2010, "Devir", 2, 4, "Estrategia",false, "nuevo", false, 50000
	    );

	    PrestamoCliente prestamo = new PrestamoCliente("P1",juego, LocalDateTime.now(), null, cliente
	    );

	    assertTrue(prestamo.sonAptos(mesa));
	}
	@Test
	public void testSonAptosSinCliente() {
	    Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);
	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");
	    JuegoMesa juego = new JuegoMesa( "J1", "Catan", 2010, "Devir", 2, 4, "Estrategia", false, "nuevo", false, 50000
	    );

	    PrestamoCliente prestamo = new PrestamoCliente(
	            "P1", juego, LocalDateTime.now(), null, cliente
	    );
	    assertFalse(prestamo.sonAptos(mesa));
	}
	@Test
	public void testSonAptosMaxPrestamos() {
	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");
	    Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);
	    mesa.setClienteActual(cliente);
	    JuegoMesa juego = new JuegoMesa("J1", "Catan", 2010, "Devir",2, 4, "Estrategia", false, "nuevo", false, 50);

	    PrestamoCliente p1 = new PrestamoCliente("P1", juego, LocalDateTime.now(), null, cliente);
	    PrestamoCliente p2 = new PrestamoCliente("P2", juego, LocalDateTime.now(), null, cliente);
	    PrestamoCliente p3 = new PrestamoCliente("P3", juego, LocalDateTime.now(), null, cliente);

	    mesa.AgregarPrestamo(p1);
	    mesa.AgregarPrestamo(p2);

	    // ya hay 2 y se pueden maximos dos juegos por mesa asi q el tercero debe fallar
	    assertFalse(p3.sonAptos(mesa));
	}
	@Test
	public void testSonAptosMenoresConJuegoAdultos() {
	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");
	    Mesa mesa = new Mesa("1", 4, true, new ArrayList<>(), false); // hay menores
	    mesa.setClienteActual(cliente);
	    JuegoMesa juego = new JuegoMesa("J1", "Juego Adulto", 2010, "Devir",2, 4, "Estrategia",
	            true, // solo adultos 
	            "nuevo", false, 50000
	    );
	    PrestamoCliente prestamo = new PrestamoCliente(
	            "P1", juego, LocalDateTime.now(), null, cliente
	    );
	    assertFalse(prestamo.sonAptos(mesa));
	}
	@Test
	public void testSonAptosRangoJugadores() {
       Cliente cliente = new Cliente("Juan", "123", "juan", "1234");
	    Mesa mesa = new Mesa("1", 1, false, new ArrayList<>(), false); // solo 1 persona en la mesa
	    mesa.setClienteActual(cliente);

	    JuegoMesa juego = new JuegoMesa(
	            "J1", "Catan", 2010, "Devir",
	            2, 4, "Estrategia", // mínimo 2, máximo 4 jugadores
	            false, "nuevo", false, 50000
	    );

	    PrestamoCliente prestamo = new PrestamoCliente(
	            "P1", juego, LocalDateTime.now(), null, cliente
	    );

	    assertFalse(prestamo.sonAptos(mesa));
	}

}
