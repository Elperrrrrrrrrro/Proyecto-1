package boardGameCafe.ui;

import java.time.LocalDateTime;
import java.util.ArrayList;

import boardGameCafe.logic.Cliente;
import boardGameCafe.logic.JuegoMesa;
import boardGameCafe.logic.Mesa;
import boardGameCafe.logic.Prestamo;
import boardGameCafe.logic.ProductoMenu;
import boardGameCafe.logic.Torneo;
import boardGameCafe.system.SistemaBoardGameCafe;

public class ConsolaCliente extends ConsolaUsuario {
	private String documentoCliente;
	private boolean usarPuntosEnCompra;

	public ConsolaCliente(SistemaBoardGameCafe sistema) {
		super(sistema);
	}

	@Override
	public void iniciar() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Consola Cliente ---");
			System.out.println("1. Iniciar sesion");
			System.out.println("0. Volver");
			String opcion = leerLinea("Seleccione una opcion: ");
			switch (opcion) {
			case "1":
				iniciarSesion();
				break;
			case "0":
				continuar = false;
				break;
			default:
				System.out.println("Opcion invalida.");
				break;
			}
		}
	}

	private void iniciarSesion() {
		String documento = leerLinea("Usuario (0 para volver): ");
		if ("0".equals(documento)) {
			return;
		}
		String password = leerLinea("Contrasena (0 para volver): ");
		if ("0".equals(password)) {
			return;
		}
		boolean iniciado = validarCliente(documento, password) && sistema.iniciarSesionCliente(documento);
		if (iniciado) {
			documentoCliente = documento;
			usarPuntosEnCompra = false;
			System.out.println("Sesion iniciada.");
			menuSesion();
			sistema.cerrarSesion();
			documentoCliente = null;
			usarPuntosEnCompra = false;
		} else {
			System.out.println("Credenciales invalidas.");
		}
	}

	private boolean validarCliente(String documento, String password) {
		if (sistema.getClientes() == null) {
			return false;
		}
		if (!sistema.getClientes().containsKey(documento)) {
			return false;
		}
		return password.equals(sistema.getClientes().get(documento).getPassword());
	}

	private void menuSesion() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Menu Cliente ---");
			System.out.println("1. Ver juegos disponibles y solicitar prestamo");
			System.out.println("2. Ver estado de prestamo y devolucion");
			System.out.println("3. Ver menu y pedir productos");
			System.out.println("4. Comprar juegos en venta");
			System.out.println("5. Inscribirse en torneos");
			System.out.println("6. Ver puntos de fidelidad y aplicar descuento");
			System.out.println("0. Cerrar sesion");
			String opcion = leerLinea("Seleccione una opcion: ");
			switch (opcion) {
			case "1":
				menuPrestamos();
				break;
			case "2":
				menuEstadoPrestamos();
				break;
			case "3":
				menuPedirProductos();
				break;
			case "4":
				comprarJuegosEnVenta();
				break;
			case "5":
				inscribirseTorneo();
				break;
			case "6":
				verPuntosFidelidad();
				break;
			case "0":
				continuar = false;
				break;
			default:
				System.out.println("Opcion invalida.");
				break;
			}
		}
	}

	private void menuPrestamos() {
		listarJuegosDisponibles();
		String idJuego = leerLinea("Id juego a prestar (0 para volver): ");
		if ("0".equals(idJuego)) {
			return;
		}
		Mesa mesa = obtenerMesaCliente();
		if (mesa == null) {
			System.out.println("El cliente no tiene mesa asignada.");
			return;
		}
		if (mesa.getPrestamoActicos().size() >= 2) {
			System.out.println("No puede tener mas de dos juegos prestados.");
			return;
		}
		JuegoMesa juego = sistema.getInventario().get(idJuego);
		if (juego == null) {
			System.out.println("Juego no existe en inventario.");
			return;
		}
		Cliente cliente = sistema.getClientes().get(documentoCliente);
		boolean prestado = sistema.procesarPrestamo(idJuego, mesa.getIdMesa(), true, cliente, null, juego, LocalDateTime.now());
		if (prestado) {
			System.out.println("Prestamo registrado.");
		} else {
			System.out.println("No fue posible realizar el prestamo.");
		}
	}

	private void menuEstadoPrestamos() {
		Mesa mesa = obtenerMesaCliente();
		if (mesa == null) {
			System.out.println("El cliente no tiene mesa asignada.");
			return;
		}
		if (mesa.getPrestamoActicos().isEmpty()) {
			System.out.println("No hay prestamos activos.");
			return;
		}
		System.out.println("Prestamos activos:");
		for (Prestamo prestamo : mesa.getPrestamoActicos()) {
			System.out.println("- " + prestamo.getJuego().getId() + " | " + prestamo.getJuego().getNombre());
		}
		String idJuego = leerLinea("Id juego a devolver (0 para volver): ");
		if ("0".equals(idJuego)) {
			return;
		}
		devolverJuego(mesa, idJuego);
	}

	private void menuPedirProductos() {
		listarMenu();
		Mesa mesa = obtenerMesaCliente();
		if (mesa == null) {
			System.out.println("El cliente no tiene mesa asignada.");
			return;
		}
		String texto = leerLinea("Productos a pedir (separados por coma, 0 para volver): ");
		if ("0".equals(texto)) {
			return;
		}
		for (String nombre : texto.split(",")) {
			String limpio = nombre.trim();
			if (!limpio.isEmpty()) {
				try {
					sistema.agregarplatoaMesa(mesa.getIdMesa(), limpio);
					System.out.println("Agregado: " + limpio);
				} catch (Exception e) {
					System.out.println("No se pudo agregar " + limpio + ": " + e.getMessage());
				}
			}
		}
	}

	private void listarJuegosDisponibles() {
		System.out.println("\nJuegos disponibles:");
		if (sistema.getInventario().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (JuegoMesa juego : sistema.getInventario().values()) {
			String estado = juego.isPrestado() ? "No disponible" : "Disponible";
			System.out.println(juego.getId() + " - " + juego.getNombre() + " - " + estado);
		}
	}

	private void listarMenu() {
		System.out.println("\nMenu:");
		if (sistema.getMenu().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (ProductoMenu producto : sistema.getMenu().values()) {
			System.out.println(producto.getNombre() + " - " + producto.getPrecioBase());
		}
	}

	private Mesa obtenerMesaCliente() {
		Cliente cliente = sistema.getClientes().get(documentoCliente);
		if (cliente == null) {
			return null;
		}
		for (Mesa mesa : sistema.getMesas().values()) {
			if (mesa.getClienteActual() != null && documentoCliente.equals(mesa.getClienteActual().getDocumentoIdentidad())) {
				return mesa;
			}
		}
		return null;
	}

	private void devolverJuego(Mesa mesa, String idJuego) {
		Prestamo objetivo = null;
		for (Prestamo prestamo : mesa.getPrestamoActicos()) {
			if (prestamo.getJuego().getId().equals(idJuego)) {
				objetivo = prestamo;
				break;
			}
		}
		if (objetivo == null) {
			System.out.println("Prestamo no encontrado.");
			return;
		}
		objetivo.getJuego().setPrestado(false);
		objetivo.setFechaDevolucion(LocalDateTime.now());
		mesa.getPrestamoActicos().remove(objetivo);
		System.out.println("Juego devuelto.");
	}

	private void comprarJuegosEnVenta() {
		listarJuegosVenta();
		Mesa mesa = obtenerMesaCliente();
		if (mesa == null) {
			System.out.println("El cliente no tiene mesa asignada.");
			return;
		}
		String texto = leerLinea("Juegos a comprar (id separados por coma, 0 para volver): ");
		if ("0".equals(texto)) {
			return;
		}
		ArrayList<JuegoMesa> juegos = new ArrayList<>();
		for (String id : texto.split(",")) {
			String limpio = id.trim();
			if (!limpio.isEmpty() && sistema.getInventarioVender().containsKey(limpio)) {
				juegos.add(sistema.getInventarioVender().get(limpio));
			}
		}
		if (juegos.isEmpty()) {
			System.out.println("No se seleccionaron juegos validos.");
			return;
		}
		Cliente cliente = sistema.getClientes().get(documentoCliente);
		boolean usarPuntos = usarPuntosEnCompra;
		usarPuntosEnCompra = false;
		String idMesa = null;
		try {
			idMesa = String.valueOf(mesa.getIdMesa());
		} catch (NumberFormatException e) {
			System.out.println("Id de mesa invalido para la venta.");
			return;
		}
		try {
			sistema.registrarVenta(idMesa, LocalDateTime.now(), cliente, 0.0, null, juegos, false, usarPuntos);
			System.out.println("Compra registrada.");
		} catch (Exception e) {
			System.out.println("No fue posible registrar la compra: " + e.getMessage());
		}
	}

	private void inscribirseTorneo() {
		listarTorneos();
		String idTorneo = leerLinea("Id torneo (0 para volver): ");
		if ("0".equals(idTorneo)) {
			return;
		}
		try {
			boolean inscrito = sistema.inscribirseTorneoCliente(idTorneo, documentoCliente);
			if (inscrito) {
				System.out.println("Inscripcion exitosa.");
			} else {
				System.out.println("No fue posible inscribirse al torneo.");
			}
		} catch (Exception e) {
			System.out.println("No fue posible inscribirse: " + e.getMessage());
		}
	}

	private void verPuntosFidelidad() {
		Cliente cliente = sistema.getClientes().get(documentoCliente);
		if (cliente == null) {
			System.out.println("Cliente no existe.");
			return;
		}
		System.out.println("Puntos de fidelidad: " + cliente.getPuntosFidelidad());
		if (cliente.getPuntosFidelidad() > 0 && confirmar("Desea aplicar los puntos en la proxima compra")) {
			usarPuntosEnCompra = true;
			System.out.println("Los puntos se aplicaran en la proxima compra.");
		}
	}

	private void listarJuegosVenta() {
		System.out.println("\nJuegos en venta:");
		if (sistema.getInventarioVender().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (JuegoMesa juego : sistema.getInventarioVender().values()) {
			System.out.println(juego.getId() + " - " + juego.getNombre() + " - " + juego.getPrecioVenta());
		}
	}

	private void listarTorneos() {
		System.out.println("\nTorneos disponibles:");
		if (sistema.getTorneos().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (Torneo torneo : sistema.getTorneos().values()) {
			System.out.println(torneo.getId() + " - " + torneo.getDiaSemana() + " - " + torneo.getJuego().getNombre());
		}
	}
}
