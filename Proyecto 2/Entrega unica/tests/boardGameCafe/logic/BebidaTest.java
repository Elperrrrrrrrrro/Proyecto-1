package boardGameCafe.logic;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.time.LocalDateTime;
public class BebidaTest {
	
	@Test
	public void testEsAptoCorrecto() {
	    Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);
	    Bebida bebida = new Bebida("Jugo", "B1",false, false,5000, "C1", "desc"
	    );
	    assertTrue(bebida.esApto(mesa));
	}
	
	@Test
	public void testEsAptoMenoresConAlcohol() {
	    Mesa mesa = new Mesa("1", 4, true, new ArrayList<>(), false); //hay menores en la mesa
	    Bebida bebida = new Bebida("Cerveza", "B1",
	            true,  // alcohólica 
	            false,
	            8000, "C1", "desc"
	    );
	    assertFalse(bebida.esApto(mesa));
	}
	
	@Test
	public void testEsAptoCalienteConJuegoAccion() {
	    Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);
	    JuegoMesa juego = new JuegoMesa("J1", "JuegoAccion", 2010, "X",2, 4, "Accion", false, "nuevo", false, 50000
	    );
	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");
	    Prestamo prestamo = new PrestamoCliente(
	            "P1", juego, LocalDateTime.now(), null, cliente
	    );
	    mesa.AgregarPrestamo(prestamo);
	    Bebida bebida = new Bebida("Cafe", "B1",false,
	            true, // caliente 
	            4000, "C1", "desc"
	    );
	    assertFalse(bebida.esApto(mesa));
	}	
	
	@Test
	public void testEsAptoCalienteSinAccion() {
	    Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);
	    JuegoMesa juego = new JuegoMesa(
	            "J1", "Catan", 2010, "X",
	            2, 4, "Estrategia", 
	            false, "nuevo", false, 50000
	    );
	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");
	    Prestamo prestamo = new PrestamoCliente(
	            "P1", juego, LocalDateTime.now(), null, cliente
	    );
	    mesa.AgregarPrestamo(prestamo);
	    Bebida bebida = new Bebida("Cafe", "B1",false,
	            true, // caliente
	            4000, "C1", "desc"
	    );
	    assertTrue(bebida.esApto(mesa));
	}

}
