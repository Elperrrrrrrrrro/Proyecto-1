package boardGameCafe.logic;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
public class VentaTest {
	@Test
	public void testAgregarProducto() {
	    Usuario usuario = new Cliente("Juan", "123", "juan", "123");
	    Venta venta = new Venta("V1", LocalDateTime.now(), usuario);
	    ProductoMenu producto = new Bebida( "CocaCola", "B1", false, false, 5000, "COD1", "Gaseosa"
	    );
	    venta.agregarProducto(producto);
	    assertEquals(1, venta.getItemsVendidos().size());
	}
	@Test
	public void testAgregarJuegoComprado() {
	    Usuario usuario = new Cliente("Juan", "123", "juan", "123");
	    Venta venta = new Venta("V1", LocalDateTime.now(), usuario);

	    JuegoMesa juego = new JuegoMesa("J1", "Catan", 2010, "Devir",2, 4, "Estrategia",false, "nuevo", false, 50000
	    );
	    venta.agregarJuegoComprado(juego);
	    assertEquals(1, venta.getJuegosVendidos().size());
	}
	@Test
	public void testCalcularImpoconsumo() {
	    Usuario usuario = new Cliente("Juan", "123", "juan", "123");
	    Venta venta = new Venta("V1", LocalDateTime.now(), usuario);
	    ProductoMenu producto1 = new Bebida("Coca", "B1", false, false, 5000, "C1", "desc");
	    ProductoMenu producto2 = new Bebida("Jugo", "B2", false, false, 5000, "C2", "desc");
	    venta.agregarProducto(producto1);
	    venta.agregarProducto(producto2);
	    double impuesto = venta.calcularImpoconsumo();
	    assertEquals(800, impuesto, 0.01);
	}
	
	@Test
	public void testCalcularIva() {
	    Usuario usuario = new Cliente("Juan", "123", "juan", "123");
	    Venta venta = new Venta("V1", LocalDateTime.now(), usuario);
	    JuegoMesa juego = new JuegoMesa("J1", "Catan", 2010, "Devir",2, 4, "Estrategia",false, "nuevo", false, 100000
	    );
	    venta.agregarJuegoComprado(juego);
	    double iva = venta.calcularIva();
	    // 100000 * 0.19 = 19000
	    assertEquals(19000, iva, 0.01);
	}
	@Test
	public void testCalcularTotal() {
	    Usuario usuario = new Cliente("Juan", "123", "juan", "123");
	    Venta venta = new Venta("V1", LocalDateTime.now(), usuario);
	    ProductoMenu producto = new Bebida("Coca", "B1", false, false, 10000, "C1", "desc");
	    JuegoMesa juego = new JuegoMesa("J1", "Catan", 2010, "Devir",2, 4, "Estrategia", false, "nuevo", false, 50000
	    );
	    venta.agregarProducto(producto);
	    venta.agregarJuegoComprado(juego);
	    venta.setPropina(2000);
	    double total = venta.calcularTotal();
	    assertEquals(22300, total, 0.01);
	}


}
