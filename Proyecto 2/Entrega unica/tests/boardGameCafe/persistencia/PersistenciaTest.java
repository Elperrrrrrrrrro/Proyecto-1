package boardGameCafe.persistencia;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import boardGameCafe.system.SistemaBoardGameCafe;
import boardGameCafe.persistencia.Persistencia;
import boardGameCafe.logic.*;
import java.util.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;


public class PersistenciaTest {

	@Test
	public void testPersistenciaSistema() {

	    SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();

	    Empleado emp = new Empleado("Admin", "123", 30, "admin", "123");

	    try {
	        java.lang.reflect.Field field = sistema.getClass().getDeclaredField("empleados");
	        field.setAccessible(true);
	        ((Map<String, Empleado>) field.get(sistema)).put("admin123", emp);
	    } catch (Exception e) { e.printStackTrace(); }

	    sistema.inciarSesionEmpleado("admin", "123");

	    Cliente cliente = new Cliente("Carlos", "1010", "carlos", "300");
	    sistema.registrarCliente(cliente);

	    Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);
	    sistema.getMesas().put("1", mesa);

	    Persistencia.guardarSistema(sistema);
	    SistemaBoardGameCafe cargado = Persistencia.cargarSistema();

	    Assertions.assertNotNull(cargado);
	    Assertions.assertEquals(1, cargado.getClientes().size());
	    Assertions.assertEquals(1, cargado.getMesas().size());
	}

	    @Test
	    public void testPersistenciaConVenta() {

	        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
	        // iniciar empleado y  sesion 
	        Empleado emp = new Empleado("Admin", "123", 30, "admin", "123");
	        try {
	            java.lang.reflect.Field field = sistema.getClass().getDeclaredField("empleados");
	            field.setAccessible(true);
	            ((Map<String, Empleado>) field.get(sistema)).put("admin123", emp);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        sistema.inciarSesionEmpleado("admin", "123");
	 
	        Cliente cliente = new Cliente("Carlos", "1010", "carlos", "300");
	        sistema.registrarCliente(cliente);
	       
	        Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);
	        sistema.getMesas().put("1", mesa);
	        mesa.setClienteActual(cliente);
	        //  crear producto
	        ArrayList<String> alergenos = new ArrayList<>();
	        Pasteleria pastel = new Pasteleria(
	                "Torta",
	                "C1",
	                8000,
	                "P1",
	                "Torta de chocolate",
	                alergenos
	        );     
	        sistema.agregarProductoMenu(pastel);
	        // agregar al pedido de la mesa
	        mesa.agregarAlPedido(pastel);
	        // registrar la venta
	        sistema.registrarVenta("1",LocalDateTime.now(),cliente,2000,new ArrayList<>(),new ArrayList<>(),false,false);
	        Map<String, Venta> ventasAntes = null;

	        try {
	            java.lang.reflect.Field field = sistema.getClass().getDeclaredField("historialVenta");
	            field.setAccessible(true);
	            ventasAntes = (Map<String, Venta>) field.get(sistema);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        assertEquals(1, ventasAntes.size());
	        Persistencia.guardarSistema(sistema);
	        SistemaBoardGameCafe cargado = Persistencia.cargarSistema();
	        
	        Map<String, Venta> ventasDespues = null;

	        try {
	            java.lang.reflect.Field field = cargado.getClass().getDeclaredField("historialVenta");
	            field.setAccessible(true);
	            ventasDespues = (Map<String, Venta>) field.get(cargado);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        assertEquals(1, ventasDespues.size());
	    }
	    
	    @Test
	    public void testPersistenciaInventario() {

	        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();

	        JuegoMesa juego = new JuegoMesa(
	                "J001", "Catan", 1995, "KOSMOS",
	                2, 4, "Estrategia",
	                false, "Bueno", false, 50.0
	        );

	        sistema.getInventario().put(juego.getId(), juego);

	        Persistencia.guardarSistema(sistema);
	        SistemaBoardGameCafe cargado = Persistencia.cargarSistema();

	        Assertions.assertEquals(1, cargado.getInventario().size());
	    }
	    @Test
	    public void testCargarSinArchivo() {

	        // se borra el  archivo si existe
	        java.io.File archivo = new java.io.File("sistema.dat");
	        if (archivo.exists()) {
	            archivo.delete();
	        }
	        SistemaBoardGameCafe sistema = Persistencia.cargarSistema();

	        assertNotNull(sistema);
	        assertEquals(0, sistema.getClientes().size());
	    }
	 
	    @Test
	    public void testIntegridadDatos() {

	        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();

	        Cliente cliente = new Cliente("Carlos", "1010", "carlos", "300");
	        sistema.getClientes().put(cliente.getDocumentoIdentidad(), cliente);

	        Persistencia.guardarSistema(sistema);
	        SistemaBoardGameCafe cargado = Persistencia.cargarSistema();

	        Cliente c = cargado.getClientes().get("1010");

	        Assertions.assertEquals("Carlos", c.getNombre());
	    }
	}