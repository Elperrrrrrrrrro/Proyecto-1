package boardGameCafe.ui;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import boardGameCafe.logic.Bebida;
import boardGameCafe.logic.Cliente;
import boardGameCafe.logic.Empleado;
import boardGameCafe.logic.JuegoMesa;
import boardGameCafe.logic.Pasteleria;
import boardGameCafe.logic.ProductoMenu;
import boardGameCafe.logic.Turno;
import boardGameCafe.logic.Torneo;
import boardGameCafe.system.SistemaBoardGameCafe;

public class ConsolaEmpleado extends ConsolaUsuario {
	private String empleadoId;

	public ConsolaEmpleado(SistemaBoardGameCafe sistema) {
		super(sistema);
	}

	@Override
	public void iniciar() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Consola Empleado ---");
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
		String login = leerLinea("Usuario (0 para volver): ");
		if ("0".equals(login)) {
			return;
		}
		String password = leerLinea("Contrasena (0 para volver): ");
		if ("0".equals(password)) {
			return;
		}
		boolean iniciado = sistema.inciarSesionEmpleado(login, password);
		if (iniciado) {
			empleadoId = login + password;
			System.out.println("Sesion iniciada.");
			menuSesion();
			sistema.cerrarSesion();
			empleadoId = null;
		} else {
			System.out.println("Credenciales invalidas.");
		}
	}

	private void menuSesion() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Menu Empleado ---");
			System.out.println("1. Registrar venta");
			System.out.println("2. Asignar mesa y gestionar pedidos");
			System.out.println("3. Sugerir platillo");
			System.out.println("4. Solicitar cambio de turno");
			System.out.println("5. Ver turnos asignados");
			System.out.println("6. Inscribirse a torneo");
			System.out.println("7. Ver torneos disponibles");
			System.out.println("8. Liberar mesa");
			System.out.println("0. Cerrar sesion");
			String opcion = leerLinea("Seleccione una opcion: ");
			switch (opcion) {
			case "1":
				registrarVenta();
				break;
			case "2":
				menuMesasYPedidos();
				break;
			case "3":
				sugerirPlatillo();
				break;
			case "4":
				solicitarCambioTurno();
				break;
			case "5":
				verTurnosAsignados();
				break;
			case "6":
				inscribirseTorneo();
				break;
			case "7":
				verTorneosDisponibles();
				break;
			case "8":
				liberarMesa();
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

	private void liberarMesa() {
		String idMesa = leerLinea("Id mesa a liberar: ");
		if (!sistema.getMesas().containsKey(idMesa)) {
			System.out.println("La mesa no existe.");
			return;
		}
		try {
			sistema.limpiarMesa(LocalDateTime.now(), sistema.getMesas().get(idMesa));
			System.out.println("Mesa liberada.");
		} catch (Exception e) {
			System.out.println("No fue posible liberar la mesa: " + e.getMessage());
		}
	}

	private void verTorneosDisponibles() {
		System.out.println("\nTorneos disponibles:");
		if (sistema.getTorneos().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (Torneo torneo : sistema.getTorneos().values()) {
			System.out.println(torneo.getId() + " - " + torneo.getDiaSemana() + " - " + torneo.getJuego().getNombre());
		}
	}

	private void registrarVenta() {
		System.out.println("Tipo de venta: 1. Cliente  2. Empleado");
		String tipo = leerLinea("Seleccione una opcion: ");
		if ("1".equals(tipo)) {
			registrarVentaCliente();
			return;
		}
		if ("2".equals(tipo)) {
			registrarVentaEmpleado();
			return;
		}
		System.out.println("Opcion invalida.");
	}

	private void registrarVentaCliente() {
		String documento = leerLinea("Documento cliente: ");
		Cliente cliente = sistema.getClientes().get(documento);
		if (cliente == null) {
			System.out.println("Cliente no existe.");
			return;
		}
		String idMesa = leerLinea("Id mesa: ");
		agregarProductosMesa(idMesa);
		ArrayList<ProductoMenu> productos = leerProductosMenu();
		ArrayList<JuegoMesa> juegos = leerJuegosVenta();
		double propina = leerDouble("Propina: ");
		boolean descuentoCompartido = confirmar("Aplicar descuento compartido");
		boolean puntos = confirmar("Aplicar puntos");
		try {
			sistema.registrarVenta(idMesa, LocalDateTime.now(), cliente, propina, productos, juegos, descuentoCompartido, puntos);
			System.out.println("Venta registrada.");
		} catch (Exception e) {
			System.out.println("No fue posible registrar la venta: " + e.getMessage());
		}
	}

	private void registrarVentaEmpleado() {
		Empleado empleado = sistema.getEmpleados().get(empleadoId);
		if (empleado == null) {
			System.out.println("Empleado no existe.");
			return;
		}
		ArrayList<ProductoMenu> productosExtras = leerProductosMenu();
		ArrayList<JuegoMesa> juegos = leerJuegosVenta();
		try {
			sistema.registrarVenta(null, LocalDateTime.now(), empleado, 0.0, productosExtras, juegos, false, false);
			System.out.println("Compra registrada.");
		} catch (Exception e) {
			System.out.println("No fue posible registrar la compra: " + e.getMessage());
		}
	}

	private void menuMesasYPedidos() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Mesas y pedidos ---");
			System.out.println("1. Asignar mesa a cliente");
			System.out.println("2. Agregar producto a mesa");
			System.out.println("0. Volver");
			String opcion = leerLinea("Seleccione una opcion: ");
			switch (opcion) {
			case "1":
				asignarMesaCliente();
				break;
			case "2":
				agregarProductoAMesa();
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

	private void asignarMesaCliente() {
		String documento = leerLinea("Documento cliente: ");
		Cliente cliente = sistema.getClientes().get(documento);
		if (cliente == null) {
			System.out.println("Cliente no existe.");
			return;
		}
		try {
			sistema.asignarMesa(cliente);
			System.out.println("Mesa asignada.");
		} catch (Exception e) {
			System.out.println("No fue posible asignar mesa: " + e.getMessage());
		}
	}

	private void agregarProductoAMesa() {
		String idMesa = leerLinea("Id mesa: ");
		String nombrePlato = leerLinea("Nombre del producto: ");
		try {
			sistema.agregarplatoaMesa(idMesa, nombrePlato);
			System.out.println("Producto agregado al pedido.");
		} catch (Exception e) {
			System.out.println("No fue posible agregar el producto: " + e.getMessage());
		}
	}

	private void sugerirPlatillo() {
		System.out.println("Tipo de platillo: 1. Bebida  2. Pasteleria");
		String tipo = leerLinea("Seleccione una opcion: ");
		String nombre = leerLinea("Nombre: ");
		String codigo = leerLinea("Codigo: ");
		double precio = leerDouble("Precio base: ");
		String codigoItem = leerLinea("Codigo item: ");
		String descripcion = leerLinea("Descripcion: ");
		ProductoMenu producto;
		if ("1".equals(tipo)) {
			boolean alcoholica = confirmar("Es alcoholica");
			boolean caliente = confirmar("Es caliente");
			producto = new Bebida(nombre, codigo, alcoholica, caliente, precio, codigoItem, descripcion);
		} else if ("2".equals(tipo)) {
			String textoAlergenos = leerLinea("Alergenos (separados por coma, vacio si no): ");
			ArrayList<String> alergenos = new ArrayList<>();
			if (!textoAlergenos.isEmpty()) {
				for (String item : textoAlergenos.split(",")) {
					String limpio = item.trim();
					if (!limpio.isEmpty()) {
						alergenos.add(limpio);
					}
				}
			}
			producto = new Pasteleria(nombre, codigo, precio, codigoItem, descripcion, alergenos);
		} else {
			System.out.println("Tipo invalido.");
			return;
		}
		try {
			sistema.sugerirPlatillo(empleadoId, producto);
			System.out.println("Sugerencia registrada.");
		} catch (Exception e) {
			System.out.println("No fue posible registrar la sugerencia: " + e.getMessage());
		}
	}

	private void solicitarCambioTurno() {
		String diaNuevo = leerLinea("Dia nuevo solicitado: ");
		try {
			sistema.SolicitarCambioTurno(empleadoId, diaNuevo);
			System.out.println("Solicitud enviada.");
		} catch (Exception e) {
			System.out.println("No fue posible enviar la solicitud: " + e.getMessage());
		}
	}

	private void verTurnosAsignados() {
		try {
			List<Turno> turnos = sistema.verTurnosEmpleado(empleadoId);
			if (turnos.isEmpty()) {
				System.out.println("No tiene turnos asignados.");
				return;
			}
			System.out.println("\nTurnos asignados:");
			for (Turno turno : turnos) {
				System.out.println("- " + turno.getDiaSemana());
			}
		} catch (Exception e) {
			System.out.println("No fue posible consultar los turnos: " + e.getMessage());
		}
	}

	private void inscribirseTorneo() {
		String idTorneo = leerLinea("Id torneo: ");
		try {
			boolean inscrito = sistema.inscribirseTorneoEmpleado(idTorneo, empleadoId);
			if (inscrito) {
				System.out.println("Inscripcion exitosa.");
			} else {
				System.out.println("No fue posible inscribirse al torneo.");
			}
		} catch (Exception e) {
			System.out.println("No fue posible inscribirse: " + e.getMessage());
		}
	}

	private void agregarProductosMesa(String idMesa) {
		String texto = leerLinea("Productos para agregar (separados por coma, vacio para omitir): ");
		if (texto.isEmpty()) {
			return;
		}
		for (String nombre : texto.split(",")) {
			String limpio = nombre.trim();
			if (!limpio.isEmpty()) {
				try {
					sistema.agregarplatoaMesa(idMesa, limpio);
				} catch (Exception e) {
					System.out.println("No se pudo agregar " + limpio + ": " + e.getMessage());
				}
			}
		}
	}

	private ArrayList<ProductoMenu> leerProductosMenu() {
		ArrayList<ProductoMenu> productos = new ArrayList<>();
		String texto = leerLinea("Productos (separados por coma, vacio para omitir): ");
		if (texto.isEmpty()) {
			return productos;
		}
		for (String nombre : texto.split(",")) {
			String limpio = nombre.trim();
			if (!limpio.isEmpty() && sistema.getMenu().containsKey(limpio)) {
				productos.add(sistema.getMenu().get(limpio));
			}
		}
		return productos;
	}

	private ArrayList<JuegoMesa> leerJuegosVenta() {
		ArrayList<JuegoMesa> juegos = new ArrayList<>();
		String texto = leerLinea("Juegos a comprar (id separados por coma, vacio para omitir): ");
		if (texto.isEmpty()) {
			return juegos;
		}
		for (String id : texto.split(",")) {
			String limpio = id.trim();
			if (!limpio.isEmpty() && sistema.getInventarioVender().containsKey(limpio)) {
				juegos.add(sistema.getInventarioVender().get(limpio));
			}
		}
		return juegos;
	}
}
