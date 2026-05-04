package boardGameCafe.system;

import java.util.*;
import boardGameCafe.logic.*;
import boardGameCafe.persistencia.*;
import java.io.Serializable;
import java.time.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;






public class SistemaBoardGameCafeTest {




    public SistemaBoardGameCafeTest() {
    }

	

    @Test
    public void testVerificarSesionSinUsuario() {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        // Intentar realizar una acción que requiere sesión sin haber iniciado sesión
        Assertions.assertThrows(SecurityException.class, () -> {
            sistema.registrarCliente(new boardGameCafe.logic.Cliente("Juan", "123", "juan@mail.com", "555"));
        }, "Debería lanzar SecurityException porque no hay usuario en sesión.");
    }



    @Test
    public void testVerificarSesionConUsuario() {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        // Corrección del constructor: (nombre, documento, edad, login, password)
        boardGameCafe.logic.Empleado emp = new boardGameCafe.logic.Empleado("Admin", "123", 30, "admin", "123");
        
        sistema.inciarSesion("admin", "123");
        // No debería lanzar excepción si la sesión es válida
        Assertions.assertDoesNotThrow(() -> {
            sistema.registrarCliente(new boardGameCafe.logic.Cliente("Juan", "123", "juan@mail.com", "555"));
        });

	
    }    @Test
    public void testProcesarPrestamo() {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        
        // Configuración inicial: Empleado, Cliente, Juego y Mesa
        // Corrección: (nombre, documento, edad, login, password)
        Empleado emp = new Empleado("Admin", "123", 30, "admin", "123");
        
        // Inyectamos el empleado para poder operar
        sistema.inciarSesion("admin", "123");
        
        Cliente cliente = new Cliente("Carlos", "1010", "carlos@mail.com", "300");
        
        // Corrección JuegoMesa: (id, nombre, ano, empresa, minJug, maxJug, categoria, soloAdultos, estado, dificil, precio)
        JuegoMesa juego = new JuegoMesa("J001", "Catan", 1995, "KOSMOS", 2, 4, "Estrategia", false, "Bueno", false, 50.0);
        
        // Corrección Mesa: (numero, capacidad, menores, alergenos, infantes)
        Mesa mesa = new Mesa(1, 4, false, new ArrayList<>(), false);
        
        // Nota: Debido a verificarSesion(), el setup de estos objetos en el sistema 
        // podría requerir inicializar los mapas internos directamente en un entorno de test.

        // Ejecución del préstamo
        boolean resultado = sistema.procesarPrestamo("J001", 1, true, cliente, null, juego, LocalDateTime.now());
        
        // Verificaciones
        Assertions.assertTrue(resultado, "El préstamo debería procesarse exitosamente.");
        Assertions.assertTrue(juego.isPrestado(), "El juego debería marcarse como prestado.");
        Assertions.assertEquals(1, juego.getVecesPrestado(), "El contador de veces prestado debería aumentar.");
        
        // Intento de préstamo del mismo juego (ya prestado)
        boolean resultadoFallido = sistema.procesarPrestamo("J001", 1, true, cliente, null, juego, LocalDateTime.now());
        Assertions.assertFalse(resultadoFallido, "No debería permitirse prestar un juego que ya está prestado.");
    }

    @Test
    public void testLimpiarMesa() {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        
        // Simulamos un inicio de sesión para cumplir con verificarSesion()
        // Constructor Empleado: (nombre, documento, edad, login, password)
        Empleado emp = new Empleado("Admin", "123", 30, "admin", "123");
        sistema.inciarSesion("admin", "123");

        // Configuración de una mesa ocupada con un préstamo activo
        ArrayList<String> alergenos = new ArrayList<>();
        // Constructor Mesa: (numero, capacidad, menores, alergenos, infantes)
        Mesa mesa = new Mesa(1, 4, false, alergenos, false);
        Cliente cliente = new Cliente("Carlos", "1010", "carlos", "pass");
        mesa.setClienteActual(cliente);
        mesa.setCantidadPersonas(2);

        // Creamos un juego y lo asignamos como prestado en la mesa
        JuegoMesa juego = new JuegoMesa("J001", "Catan", 1995, "KOSMOS", 2, 4, "Estrategia", false, "Bueno", false, 50.0);
        juego.setPrestado(true);
        PrestamoCliente prestamo = new PrestamoCliente("P001", juego, LocalDateTime.now(), null, cliente);
        mesa.AgregarPrestamo(prestamo);

        // Acción: Limpiar la mesa
        sistema.limpiarMesa(LocalDateTime.now(), mesa);

        // Verificaciones
        Assertions.assertNull(mesa.getClienteActual(), "El cliente debería ser nulo tras limpiar.");
        Assertions.assertEquals(0, mesa.getCantidadPersonas(), "La cantidad de personas debería reiniciarse a 0.");
        Assertions.assertTrue(mesa.getPrestamoActicos().isEmpty(), "La lista de préstamos activos de la mesa debe quedar vacía.");
        Assertions.assertFalse(juego.isPrestado(), "El juego debe volver a estar marcado como no prestado.");
        Assertions.assertNotNull(prestamo.getFechaDevolucion(), "El préstamo debe tener registrada su fecha de devolución.");
    }

    @Test
    public void testAprobarCambioTurnoExitoso() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        
        // Inyectamos un administrador por reflexión para cumplir la precondición de seguridad
        java.lang.reflect.Field fieldUsuario = sistema.getClass().getDeclaredField("usuarioActual");
        fieldUsuario.setAccessible(true);
        Administrador admin = new Administrador("Admin", "1", "admin", "admin");
        fieldUsuario.set(sistema, admin);

        // Inyectamos los mapas de empleados y turnos para simular el estado previo del sistema
        java.lang.reflect.Field fieldEmpleados = sistema.getClass().getDeclaredField("empleados");
        fieldEmpleados.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Empleado> empleados = (Map<String, Empleado>) fieldEmpleados.get(sistema);
        
        java.lang.reflect.Field fieldTurnos = sistema.getClass().getDeclaredField("turnos");
        fieldTurnos.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Turno> turnos = (Map<String, Turno>) fieldTurnos.get(sistema);

        // Configuración de datos de prueba
        Empleado emp = new Empleado("Juan", "101", 25, "juan", "123");
        String keyEmpleado = "juan123"; // La llave es login + password según SistemaBoardGameCafe.java:189
        empleados.put(keyEmpleado, emp);
        
        Turno lunes = new Turno("Lunes");
        turnos.put("Lunes", lunes);

        // Creamos una sugerencia de tipo turno (false)
        Sugerencia sugerencia = new Sugerencia("S001", false, false, "Lunes", emp, null);

        // Ejecución
        boolean resultado = sistema.aprobarCambioTurno(keyEmpleado, sugerencia);

        // Verificaciones
        Assertions.assertTrue(resultado, "El método debería retornar true al ser exitoso.");
        Assertions.assertTrue(sugerencia.isEstaAprobado(), "La sugerencia debería marcarse como aprobada.");
        Assertions.assertTrue(lunes.getEmpleadosAsignados().contains(emp), "El empleado debería haber sido añadido al turno.");
    }

    @Test
    public void testAprobarCambioTurnoSinPermisos() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        
        // Simulamos un usuario que NO es Administrador
        java.lang.reflect.Field fieldUsuario = sistema.getClass().getDeclaredField("usuarioActual");
        fieldUsuario.setAccessible(true);
        Empleado empNormal = new Empleado("Juan", "101", 25, "juan", "123");
        fieldUsuario.set(sistema, empNormal);

        Sugerencia sugerencia = new Sugerencia("S001", false, false, "Lunes", empNormal, null);

        // Verificamos que lance la excepción de seguridad
        Assertions.assertThrows(SecurityException.class, () -> {
            sistema.aprobarCambioTurno("juan123", sugerencia);
        }, "Debería lanzar SecurityException si quien aprueba no es Administrador.");
    }

    @Test
    public void testAgregarJuegoMesa() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema); // Helper para poner admin en sesion

        JuegoMesa juego = new JuegoMesa("J001", "Catan", 1995, "KOSMOS", 2, 4, "Estrategia", false, "Bueno", false, 50.0);
        sistema.agregarJuegoMesa(juego);

        Assertions.assertTrue(sistema.getInventario().containsKey("J001"));
        Assertions.assertEquals("Catan", sistema.getInventario().get("J001").getNombre());
    }

    @Test
    public void testCambiarEstadoJuego() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema);

        JuegoMesa juego = new JuegoMesa("J001", "Catan", 1995, "KOSMOS", 2, 4, "Estrategia", false, "Bueno", false, 50.0);
        sistema.agregarJuegoMesa(juego);
        sistema.cambiarEstadoJuego("J001", "Deteriorado");

        Assertions.assertEquals("Deteriorado", sistema.getInventario().get("J001").getEstado());
    }

    @Test
    public void testRegistrarCliente() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema);

        Cliente cliente = new Cliente("Maria", "2020", "maria@mail.com", "pass");
        sistema.registrarCliente(cliente);

        Assertions.assertEquals(cliente, sistema.getClientes().get("2020"));
    }

    @Test
    public void testAgregarPlatoAMesa() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema);

        Mesa mesa = new Mesa(1, 4, false, new ArrayList<>(), false);
        sistema.agregarMesa(mesa); // Esto la mete en el mapa con llave "1"

        ProductoMenu bebida = new Bebida("Jugo", "B01", false, false, 5.0, "IT01", "Jugo natural");
        sistema.agregarProductoMenu(bebida);

        sistema.agregarplatoaMesa("1", "Jugo");

        Assertions.assertTrue(sistema.getMesas().get("1").getPedidoActual().contains(bebida));
    }

    @Test
    public void testAsignarMesa() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema);

        Mesa mesa = new Mesa(1, 4, false, new ArrayList<>(), false);
        sistema.agregarMesa(mesa); // Al agregarla, se hace push a mesasDesocupadas

        Cliente cliente = new Cliente("Luis", "3030", "luis", "123");
        sistema.asignarMesa(cliente);

        Assertions.assertEquals(cliente, mesa.getClienteActual());
    }

    @Test
    public void testAgregarEmpleado() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema);

        Empleado nuevo = new Empleado("Emp1", "4040", 22, "emp1", "pass");
        sistema.agregarEmpleado(nuevo);

        // Accedemos al mapa privado para verificar
        java.lang.reflect.Field field = sistema.getClass().getDeclaredField("empleados");
        field.setAccessible(true);
        Map<String, Empleado> empleados = (Map<String, Empleado>) field.get(sistema);

        Assertions.assertTrue(empleados.containsKey("emp1pass"));
    }

    @Test
    public void testAgregarTurno() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema);

        Turno turno = new Turno("Martes");
        sistema.agregarTurno(turno);

        java.lang.reflect.Field field = sistema.getClass().getDeclaredField("turnos");
        field.setAccessible(true);
        Map<String, Turno> turnos = (Map<String, Turno>) field.get(sistema);

        Assertions.assertEquals(turno, turnos.get("Martes"));
    }

    @Test
    public void testAgregarMesa() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema);

        Mesa mesa = new Mesa(5, 6, true, new ArrayList<>(), true);
        sistema.agregarMesa(mesa);

        Assertions.assertTrue(sistema.getMesas().containsKey("1"));
    }

    @Test
    public void testAgregarProductoMenu() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema);

        ProductoMenu pastel = new Pasteleria("Torta", "P01", 10.0, "C01", "Torta chocolate", new ArrayList<>());
        sistema.agregarProductoMenu(pastel);

        java.lang.reflect.Field field = sistema.getClass().getDeclaredField("menu");
        field.setAccessible(true);
        Map<String, ProductoMenu> menu = (Map<String, ProductoMenu>) field.get(sistema);

        Assertions.assertEquals(pastel, menu.get("Torta"));
    }

    @Test
    public void testAgregarPrestamos() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema);

        JuegoMesa juego = new JuegoMesa("J1", "J", 2000, "E", 1, 2, "C", false, "B", false, 10.0);
        
        // Prestamo Cliente
        Cliente c = new Cliente("C", "1", "l", "p");
        PrestamoCliente pc = new PrestamoCliente("PC1", juego, LocalDateTime.now(), null, c);
        sistema.agregarPrestamoCliente(pc);

        // Prestamo Empleado
        Empleado e = new Empleado("E", "2", 20, "l", "p");
        PrestamoEmpleado pe = new PrestamoEmpleado("PE1", juego, LocalDateTime.now(), null, e, new HashMap<>());
        sistema.agregarPrestamoEmpleado(pe);

        java.lang.reflect.Field fieldC = sistema.getClass().getDeclaredField("historialPrestamosClientes");
        java.lang.reflect.Field fieldE = sistema.getClass().getDeclaredField("historailPrestamosEmpleados");
        fieldC.setAccessible(true);
        fieldE.setAccessible(true);

        Assertions.assertTrue(((Map)fieldC.get(sistema)).containsKey("PC1"));
        Assertions.assertTrue(((Map)fieldE.get(sistema)).containsKey("PE1"));
    }

    @Test
    public void testAgregarSugerencia() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema);

        Sugerencia sug = new Sugerencia("S1", false, true, "Lunes", null, null);
        sistema.agregarSugerencia(sug);

        java.lang.reflect.Field field = sistema.getClass().getDeclaredField("Sugerencias");
        field.setAccessible(true);
        Assertions.assertTrue(((Map)field.get(sistema)).containsKey("S1"));
    }

    @Test
    public void testAgregarVenta() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema);

        Venta venta = new Venta("V1", LocalDateTime.now(), new Cliente("X", "1", "l", "p"));
        sistema.agregarVenta(venta);

        java.lang.reflect.Field field = sistema.getClass().getDeclaredField("historialVenta");
        field.setAccessible(true);
        Assertions.assertTrue(((Map)field.get(sistema)).containsKey("V1"));
    }

    @Test
    public void testAgregarJuegoVender() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema);

        JuegoMesa juego = new JuegoMesa("JV1", "Venta", 2023, "E", 1, 5, "C", false, "N", false, 100.0);
        sistema.agregarJuegoVender(juego);

        java.lang.reflect.Field field = sistema.getClass().getDeclaredField("inventarioVender");
        field.setAccessible(true);
        Assertions.assertTrue(((Map)field.get(sistema)).containsKey("JV1"));
    }

    @Test
    public void testInscribirseTorneoEmpleado() throws Exception {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        setAdminSession(sistema);

        Empleado emp = new Empleado("E1", "99", 25, "e1", "p");
        sistema.agregarEmpleado(emp);

        Torneo torneo = new TorneoAmistoso("Lunes", new ArrayList<>(), null, 10, 10.0, "T1");
        // Inyectamos el torneo manualmente ya que no hay método público simple sin validaciones extras
        java.lang.reflect.Field fieldT = sistema.getClass().getDeclaredField("torneos");
        fieldT.setAccessible(true);
        ((Map)fieldT.get(sistema)).put("T1", torneo);

        boolean inscrito = sistema.inscribirseTorneoEmpleado("T1", "e1p");
        Assertions.assertTrue(inscrito);
        Assertions.assertTrue(torneo.getParticipantes().contains(emp));
    }

    // Método auxiliar para evitar repetir código de reflexión
    private void setAdminSession(SistemaBoardGameCafe sistema) throws Exception {
        java.lang.reflect.Field fieldUsuario = sistema.getClass().getDeclaredField("usuarioActual");
        fieldUsuario.setAccessible(true);
        fieldUsuario.set(sistema, new Administrador("Admin", "1", "admin", "admin"));
    }

}