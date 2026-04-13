package boardGameCafe;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        // Cargar sistema (si no existe, crea uno nuevo)
        SistemaBoardGameCafe sistema = Persistencia.cargarSistema();


        //Crear datos de prueba SOLO si está vacío
        if (sistema == null) {
            sistema = new SistemaBoardGameCafe();
        }

        // Crear cliente
        Cliente cliente = new Cliente("Juan", "123", "juan", "1234");

        // Crear juego
        JuegoMesa juego = new JuegoMesa(
                "J1",
                "Catan",
                2010,
                "Devir",
                2,
                4,
                "Estrategia",
                false,
                "nuevo",
                false,
                50000
        );

        // Crear mesa
        ArrayList<String> alergenos = new ArrayList<>();
        Mesa mesa = new Mesa(1, 3, false, alergenos, false);
        mesa.setClienteActual(cliente);

        // Agregar al sistema
        sistema.getClientes().put(cliente.getDocumento(), cliente);
        sistema.getInventario().put(juego.getId(), juego);
        sistema.getMesas().put("1", mesa);

        //  Crear préstamo
        PrestamoCliente prestamo = new PrestamoCliente(
                "P1",
                juego,
                LocalDateTime.now(),
                null,
                cliente
        );

        if (prestamo.sonAptos(mesa)) {
            mesa.AgregarPrestamo(prestamo);
            juego.setPrestado(true);
            System.out.println(" Préstamo realizado");
        } else {
            System.out.println("No se pudo realizar préstamo");
        }

        // Guardar sistema
        Persistencia.guardarSistema(sistema);

        System.out.println(" Sistema guardado");

        //  Cargar otra vez
        SistemaBoardGameCafe sistemaCargado = Persistencia.cargarSistema();


        // Verificar datos
        System.out.println("Clientes cargados: " + sistemaCargado.getClientes().size());
        System.out.println("Juegos en inventario: " + sistemaCargado.getInventario().size());
        System.out.println("Mesas: " + sistemaCargado.getMesas().size());

        Mesa mesaCargada = sistemaCargado.getMesas().get("1");

        if (mesaCargada != null) {
            System.out.println("Préstamos en mesa: " + mesaCargada.getPrestamoActicos().size());
        }

        System.out.println("Fin");
    }
}