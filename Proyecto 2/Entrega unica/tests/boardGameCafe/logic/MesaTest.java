package boardGameCafe.logic;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.time.LocalDateTime;

public class MesaTest {
	@Test
	public void testAgregarAlPedido() {

	    Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);

	    Pasteleria pastel = new Pasteleria(
	            "Torta",
	            "C1",
	            8000,
	            "P1",
	            "Chocolate",
	            new ArrayList<>()
	    );

	    mesa.agregarAlPedido(pastel);

	    assertEquals(1, mesa.getPedidoActual().size());
	}
	
	@Test
	public void testAgregarPrestamo() {

	    Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);
	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");
	    JuegoMesa juego = new JuegoMesa(
	            "J1", "Catan", 2010, "Devir",
	            2, 4, "Estrategia",
	            false, "nuevo", false, 50000
	    );

	    PrestamoCliente prestamo = new PrestamoCliente(
	            "P1",
	            juego,
	            LocalDateTime.now(),
	            null,
	            cliente
	    );

	    mesa.AgregarPrestamo(prestamo);
	    assertEquals(1, mesa.getPrestamoActicos().size());
	}
	
	@Test
	public void testLiberarMesa() {
	    Mesa mesa = new Mesa("1", 4, true, new ArrayList<>(), true);
	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");
	    mesa.setClienteActual(cliente);
	    // agregar el producto
	    Pasteleria pastel = new Pasteleria(
	            "Torta",
	            "C1",
	            8000,
	            "P1",
	            "Chocolate",
	            new ArrayList<>()
	    );
	    mesa.agregarAlPedido(pastel);
	    // juego y hacer el préstamo
	    JuegoMesa juego = new JuegoMesa("J1", "Catan", 2010, "Devir", 2, 4, "Estrategia",false, "nuevo", false, 50000
	    );
	    juego.setPrestado(true);

	    PrestamoCliente prestamo = new PrestamoCliente("P1",juego,LocalDateTime.now(),null,cliente
	    );
	    mesa.AgregarPrestamo(prestamo);
	    //borrar lo anterior con liberar mesa
	    mesa.liberarMesa();
	    
	    assertNull(mesa.getClienteActual());
	    assertEquals(0, mesa.getCantidadPersonas());
	    assertEquals(0, mesa.getPedidoActual().size());
	    assertEquals(0, mesa.getPrestamoActicos().size());
	    assertFalse(mesa.isHayMenores());
	    assertFalse(mesa.isHayInfantes());

	    assertFalse(juego.isPrestado());
	}

}
