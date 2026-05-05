package boardGameCafe.logic;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class ClienteTest {
	@Test
	public void testAgregarPuntosFidelidadAcumulados() {

	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");
	    // puntos iniciales
	    cliente.agregarPuntosFidelidad(50);
	    // agregar más puntos
	    cliente.agregarPuntosFidelidad(30);
	    // debe acumular los puntos sin remmplazar los que ya estaban
	    assertEquals(80, cliente.getPuntosFidelidad());
	}
	@Test
	public void testUsarPuntosFidelidad() {

	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");

	    cliente.agregarPuntosFidelidad(100);

	    double descuento = cliente.usarPuntosFidelidad(50);

	    assertEquals(50, descuento);
	    assertEquals(50, cliente.getPuntosFidelidad());
	}
	@Test
	public void testUsarPuntosInsuficientes() {

	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");

	    cliente.agregarPuntosFidelidad(30);

	    double descuento = cliente.usarPuntosFidelidad(100);

	    assertEquals(30, descuento);
	    assertEquals(0, cliente.getPuntosFidelidad());
	}
	@Test
	public void testUsarPuntosSinPuntos() {

	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");

	    double descuento = cliente.usarPuntosFidelidad(50);

	    assertEquals(0, descuento);
	    assertEquals(0, cliente.getPuntosFidelidad());
	}
	@Test
	public void testAgregarJuegoFavorito() {

	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");

	    JuegoMesa juego = new JuegoMesa(
	            "J1", "Catan", 2010, "Devir",
	            2, 4, "Estrategia",
	            false, "nuevo", false, 50000
	    );

	    cliente.adicionarJuegoFavorito(juego);

	    assertEquals(1, cliente.getJuegosFavoritos().size());
	}
	@Test
	public void testNoDuplicarJuegoFavorito() {

	    Cliente cliente = new Cliente("Juan", "123", "juan", "1234");

	    JuegoMesa juego = new JuegoMesa(
	            "J1", "Catan", 2010, "Devir",
	            2, 4, "Estrategia",
	            false, "nuevo", false, 50000
	    );

	    cliente.adicionarJuegoFavorito(juego);
	    cliente.adicionarJuegoFavorito(juego);

	    assertEquals(1, cliente.getJuegosFavoritos().size());
	}
}
