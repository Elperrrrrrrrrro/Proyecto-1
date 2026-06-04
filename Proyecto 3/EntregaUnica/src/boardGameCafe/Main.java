package boardGameCafe;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import boardGameCafe.interfaz.VentanaPrincipal;
import boardGameCafe.logic.*;
import boardGameCafe.system.SistemaBoardGameCafe;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        aplicarLookAndFeel();
        SwingUtilities.invokeLater(() -> {
            SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
            cargarDatosPrueba(sistema);
            VentanaPrincipal ventana = new VentanaPrincipal(sistema);
            ventana.setVisible(true);
        });
    }

    private static void cargarDatosPrueba(SistemaBoardGameCafe sistema) {

        sistema.registrarAdministrador(
            new Administrador("Administrador", "0000000000", "admin", "123456789"));
        sistema.iniciarSesionAdministrador("admin", "123456789");

      
        Mesero   m1 = new Mesero("Ana Martinez",   "2001", 28, "ana",    "pass1", new ArrayList<>());
        Mesero   m2 = new Mesero("Pedro Lopez",    "2002", 32, "pedro",  "pass2", new ArrayList<>());
        Mesero   m3 = new Mesero("Laura Gomez",    "2003", 25, "laura",  "pass3", new ArrayList<>());
        Cocinero c1 = new Cocinero("Chef Ramirez", "2004", 40, "chef",   "pass4");
        Cocinero c2 = new Cocinero("Chef Suarez",  "2005", 35, "suarez", "pass5");
        sistema.agregarEmpleado(m1);
        sistema.agregarEmpleado(m2);
        sistema.agregarEmpleado(m3);
        sistema.agregarEmpleado(c1);
        sistema.agregarEmpleado(c2);

        
        Cliente cli1 = new Cliente("Carlos Garcia", "3001", "carlos", "c1");
        Cliente cli2 = new Cliente("Maria Lopez",   "3002", "maria",  "c2");
        Cliente cli3 = new Cliente("Ana Perez",     "3003", "anap",   "c3");
        Cliente cli4 = new Cliente("Juan Torres",   "3004", "juan",   "c4");
        sistema.registrarCliente(cli1);
        sistema.registrarCliente(cli2);
        sistema.registrarCliente(cli3);
        sistema.registrarCliente(cli4);

        
        JuegoMesa j1 = new JuegoMesa("J001", "Catan",    2000, "Mayfair",  3, 4,  "Tablero", false, "Bueno",   false, 120000);
        JuegoMesa j2 = new JuegoMesa("J002", "Uno",      1971, "Mattel",   2, 10, "Cartas",  false, "Nuevo",   false,  25000);
        JuegoMesa j3 = new JuegoMesa("J003", "Twister",  1966, "Hasbro",   2, 6,  "Accion",  false, "Bueno",   false,  35000);
        JuegoMesa j4 = new JuegoMesa("J004", "Monopoly", 1935, "Hasbro",   2, 8,  "Tablero", false, "Regular", false,  80000);
        JuegoMesa j5 = new JuegoMesa("J005", "Ajedrez",  1200, "Generico", 2, 2,  "Tablero", false, "Bueno",   true,   15000);
        JuegoMesa j6 = new JuegoMesa("J006", "Dixit",    2008, "Libellud", 3, 6,  "Cartas",  false, "Nuevo",   false,  90000);
        sistema.agregarJuegoMesa(j1);
        sistema.agregarJuegoMesa(j2);
        sistema.agregarJuegoMesa(j3);
        sistema.agregarJuegoMesa(j4);
        sistema.agregarJuegoMesa(j5);
        sistema.agregarJuegoMesa(j6);

        
        JuegoMesa jv1 = new JuegoMesa("JV01", "Pandemic",    2008, "Z-Man", 2, 4, "Tablero", false, "Nuevo", false, 150000);
        JuegoMesa jv2 = new JuegoMesa("JV02", "Ticket Ride", 2004, "Days",  2, 5, "Tablero", false, "Bueno", false, 130000);
        sistema.agregarJuegoMesa(jv1);
        sistema.agregarJuegoMesa(jv2);
        try {
            sistema.moverJuegoAInventarioVenta("JV01");
            sistema.moverJuegoAInventarioVenta("JV02");
        } catch (Exception ignored) {}

        
        sistema.agregarMesa(new Mesa("1", 0, false, new ArrayList<>(), false));
        sistema.agregarMesa(new Mesa("2", 0, false, new ArrayList<>(), false));
        sistema.agregarMesa(new Mesa("3", 0, false, new ArrayList<>(), false));
        sistema.agregarMesa(new Mesa("4", 0, false, new ArrayList<>(), false));
        sistema.agregarMesa(new Mesa("5", 0, false, new ArrayList<>(), false));
        sistema.agregarMesa(new Mesa("6", 0, false, new ArrayList<>(), false));
        sistema.agregarMesa(new Mesa("7", 0, false, new ArrayList<>(), false));
        sistema.agregarMesa(new Mesa("8", 0, false, new ArrayList<>(), false));

        
        String[] diasLaborales = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};
        for (String dia : diasLaborales) {
            Turno t = new Turno(dia);
            t.adicionarEmpleado(m1);
            t.adicionarEmpleado(m2);
            t.adicionarEmpleado(c1);
            sistema.agregarTurno(t);
        }
        Turno tSab = new Turno("Sabado");
        tSab.adicionarEmpleado(m3);
        tSab.adicionarEmpleado(c2);
        sistema.agregarTurno(tSab);

        
        Bebida b1 = new Bebida("Cafe",            "B01", false, true,  5000, "B01", "Cafe negro caliente");
        Bebida b2 = new Bebida("Cerveza",         "B02", true,  false, 8000, "B02", "Cerveza fria");
        Bebida b3 = new Bebida("Jugo de naranja", "B03", false, false, 6000, "B03", "Jugo natural");
        ArrayList<String> gluten = new ArrayList<>();
        gluten.add("gluten");
        Pasteleria p1 = new Pasteleria("Croissant", "P01", 7000, "P01", "Croissant de mantequilla", gluten);
        Pasteleria p2 = new Pasteleria("Brownie",   "P02", 6000, "P02", "Brownie de chocolate", new ArrayList<>());
        sistema.agregarProductoMenu(b1);
        sistema.agregarProductoMenu(b2);
        sistema.agregarProductoMenu(b3);
        sistema.agregarProductoMenu(p1);
        sistema.agregarProductoMenu(p2);

       
        try {
            sistema.CrearTorneoCompetitivo("Viernes", new ArrayList<>(), j1, 8, 50000, 10000, "T001");
            sistema.CrearTorneoAmistoso("Sabado",     new ArrayList<>(), j2, 6, 15.0,         "T002");
        } catch (Exception ignored) {}

        
        LocalDateTime hoy       = LocalDateTime.now();
        LocalDateTime ayer      = hoy.minusDays(1);
        LocalDateTime antesAyer = hoy.minusDays(2);

        Venta v1 = new Venta("V001", hoy,       cli1);
        v1.agregarProducto(b1); v1.agregarProducto(p2); v1.calcularTotal();
        sistema.getHistorialVenta().put("V001", v1);

        Venta v2 = new Venta("V002", hoy,       cli2);
        v2.agregarJuegoComprado(jv1); v2.calcularTotal();
        sistema.getHistorialVenta().put("V002", v2);

        Venta v3 = new Venta("V003", ayer,      cli3);
        v3.agregarProducto(b2); v3.agregarProducto(b3); v3.agregarProducto(p1); v3.calcularTotal();
        sistema.getHistorialVenta().put("V003", v3);

        Venta v4 = new Venta("V004", antesAyer, cli4);
        v4.agregarProducto(b1); v4.agregarJuegoComprado(jv2); v4.calcularTotal();
        sistema.getHistorialVenta().put("V004", v4);

        sistema.cerrarSesion();

        System.out.println("=== Datos de prueba cargados ===");
        System.out.println("Admin:    admin  / 123456789");
        System.out.println("Empleado: ana    / pass1");
        System.out.println("Cliente:  doc      3001");
    }

    private static void aplicarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el diseno nativo.");
        }
    }
}