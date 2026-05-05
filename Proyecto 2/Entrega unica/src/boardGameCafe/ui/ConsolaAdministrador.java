package boardGameCafe.ui;

import boardGameCafe.logic.Administrador;
import boardGameCafe.logic.Bebida;
import boardGameCafe.logic.Cliente;
import boardGameCafe.logic.Cocinero;
import boardGameCafe.logic.Empleado;
import boardGameCafe.logic.JuegoMesa;
import boardGameCafe.logic.Mesa;
import boardGameCafe.logic.Mesero;
import boardGameCafe.logic.Pasteleria;
import boardGameCafe.logic.PrestamoCliente;
import boardGameCafe.logic.PrestamoEmpleado;
import boardGameCafe.logic.ProductoMenu;
import boardGameCafe.logic.Sugerencia;
import boardGameCafe.logic.Torneo;
import boardGameCafe.logic.Turno;
import boardGameCafe.logic.Venta;
import boardGameCafe.system.SistemaBoardGameCafe;

import java.time.LocalDate;

public class ConsolaAdministrador extends ConsolaUsuario {

	public ConsolaAdministrador(SistemaBoardGameCafe sistema) {
		super(sistema);
	}

	@Override
	public void iniciar() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Consola Administrador ---");
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
		boolean iniciado = sistema.iniciarSesionAdministrador(login, password);
		if (iniciado) {
			System.out.println("Sesion iniciada.");
			menuSesion();
			sistema.cerrarSesion();
		} else {
			System.out.println("Credenciales invalidas.");
		}
	}

	private void menuSesion() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Menu Administrador ---");
			System.out.println("1. Crear usuarios");
			System.out.println("2. Crear mesas, turnos y menu");
			System.out.println("3. Gestion inventario de juegos");
			System.out.println("4. Cambiar estado de juego");
			System.out.println("5. Crear torneos");
			System.out.println("6. Ver listados del sistema");
			System.out.println("7. Gestion de turnos");
			System.out.println("8. Aprobar sugerencias y cambios de turno");
			System.out.println("9. Generar informes de ventas");
			System.out.println("0. Cerrar sesion");
			String opcion = leerLinea("Seleccione una opcion: ");
			switch (opcion) {
			case "1":
				menuCrearUsuarios();
				break;
			case "2":
				menuCrearOperaciones();
				break;
			case "3":
				menuInventarioJuegos();
				break;
			case "4":
				cambiarEstadoJuego();
				break;
			case "5":
				menuTorneos();
				break;
			case "6":
				menuListados();
				break;
			case "7":
				menuGestionTurnos();
				break;
			case "8":
				menuAprobaciones();
				break;
			case "9":
				generarInformeVentas();
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

	private void menuCrearUsuarios() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Crear usuarios ---");
			System.out.println("1. Administrador");
			System.out.println("2. Empleado");
			System.out.println("3. Cliente");
			System.out.println("0. Volver");
			String opcion = leerLinea("Seleccione una opcion: ");
			switch (opcion) {
			case "1":
				crearAdministrador();
				break;
			case "2":
				crearEmpleado();
				break;
			case "3":
				crearCliente();
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

	private void crearAdministrador() {
		String nombre = leerLinea("Nombre: ");
		String documento = leerLinea("Documento: ");
		String login = leerLinea("Login: ");
		String password = leerLinea("Contrasena: ");
		Administrador admin = new Administrador(nombre, documento, login, password);
		sistema.registrarAdministrador(admin);
		System.out.println("Administrador registrado.");
	}

	private void crearEmpleado() {
		System.out.println("Tipo empleado: 1. Mesero  2. Cocinero");
		String tipo = leerLinea("Seleccione una opcion: ");
		String nombre = leerLinea("Nombre: ");
		String documento = leerLinea("Documento: ");
		int edad = leerEntero("Edad: ");
		String login = leerLinea("Login: ");
		String password = leerLinea("Contrasena: ");
		Empleado empleado;
		if ("1".equals(tipo)) {
			empleado = new Mesero(nombre, documento, edad, login, password, new java.util.ArrayList<>());
		} else if ("2".equals(tipo)) {
			empleado = new Cocinero(nombre, documento, edad, login, password);
		} else {
			System.out.println("Tipo invalido.");
			return;
		}
		sistema.agregarEmpleado(empleado);
		System.out.println("Empleado registrado.");
	}

	private void crearCliente() {
		String nombre = leerLinea("Nombre: ");
		String documento = leerLinea("Documento: ");
		String login = leerLinea("Login: ");
		String password = leerLinea("Contrasena: ");
		Cliente cliente = new Cliente(nombre, documento, login, password);
		sistema.registrarCliente(cliente);
		System.out.println("Cliente registrado.");
	}

	private void menuCrearOperaciones() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Crear mesas, turnos y menu ---");
			System.out.println("1. Crear mesa");
			System.out.println("2. Crear turno");
			System.out.println("3. Crear producto de menu");
			System.out.println("0. Volver");
			String opcion = leerLinea("Seleccione una opcion: ");
			switch (opcion) {
			case "1":
				crearMesa();
				break;
			case "2":
				crearTurno();
				break;
			case "3":
				crearProductoMenu();
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

	private void crearMesa() {
		String idMesa = leerLinea("Id mesa: ");
		java.util.ArrayList<String> alergenos = new java.util.ArrayList<>();
		Mesa mesa = new Mesa(idMesa, 0, false, alergenos, false);
		sistema.agregarMesa(mesa);
		System.out.println("Mesa creada.");
	}

	private void crearTurno() {
		String dia = leerLinea("Dia de la semana: ");
		Turno turno = new Turno(dia);
		sistema.agregarTurno(turno);
		System.out.println("Turno creado.");
	}

	private void crearProductoMenu() {
		System.out.println("Tipo producto: 1. Bebida  2. Pasteleria");
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
			java.util.ArrayList<String> alergenos = new java.util.ArrayList<>();
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
		sistema.agregarProductoMenu(producto);
		System.out.println("Producto agregado al menu.");
	}

	private void menuInventarioJuegos() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Gestion inventario ---");
			System.out.println("1. Agregar juego a inventario de prestamo");
			System.out.println("2. Mover juego a inventario de venta");
			System.out.println("0. Volver");
			String opcion = leerLinea("Seleccione una opcion: ");
			switch (opcion) {
			case "1":
				agregarJuegoInventario();
				break;
			case "2":
				moverJuegoInventarioVenta();
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

	private void agregarJuegoInventario() {
		String id = leerLinea("Id juego: ");
		String nombre = leerLinea("Nombre: ");
		int ano = leerEntero("Ano publicacion: ");
		String empresa = leerLinea("Empresa: ");
		int minJug = leerEntero("Min jugadores: ");
		int maxJug = leerEntero("Max jugadores: ");
		String categoria = leerLinea("Categoria (Cartas/Tablero/Accion): ");
		boolean soloAdultos = confirmar("Solo adultos");
		String estado = leerLinea("Estado: ");
		boolean dificil = confirmar("Es dificil");
		double precioVenta = leerDouble("Precio venta: ");
		JuegoMesa juego = new JuegoMesa(id, nombre, ano, empresa, minJug, maxJug, categoria, soloAdultos, estado, dificil, precioVenta);
		sistema.agregarJuegoMesa(juego);
		System.out.println("Juego agregado.");
	}

	private void moverJuegoInventarioVenta() {
		String idJuego = leerLinea("Id juego a mover: ");
		try {
			sistema.moverJuegoAInventarioVenta(idJuego);
			System.out.println("Juego movido a inventario de venta.");
		} catch (Exception e) {
			System.out.println("No fue posible mover el juego: " + e.getMessage());
		}
	}

	private void cambiarEstadoJuego() {
		String idJuego = leerLinea("Id juego: ");
		String estado = leerLinea("Nuevo estado: ");
		try {
			sistema.cambiarEstadoJuego(idJuego, estado);
			System.out.println("Estado actualizado.");
		} catch (Exception e) {
			System.out.println("No fue posible actualizar el estado: " + e.getMessage());
		}
	}

	private void menuTorneos() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Torneos ---");
			System.out.println("1. Crear torneo competitivo");
			System.out.println("2. Crear torneo amistoso");
			System.out.println("0. Volver");
			String opcion = leerLinea("Seleccione una opcion: ");
			switch (opcion) {
			case "1":
				crearTorneoCompetitivo();
				break;
			case "2":
				crearTorneoAmistoso();
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

	private void crearTorneoCompetitivo() {
		String id = leerLinea("Id torneo: ");
		String dia = leerLinea("Dia semana: ");
		String idJuego = leerLinea("Id juego: ");
		JuegoMesa juego = sistema.getInventario().get(idJuego);
		if (juego == null) {
			System.out.println("Juego no existe en inventario.");
			return;
		}
		int cupos = leerEntero("Numero participantes: ");
		double premio = leerDouble("Premio: ");
		double costo = leerDouble("Costo inscripcion: ");
		try {
			sistema.CrearTorneoCompetitivo(dia, new java.util.ArrayList<>(), juego, cupos, premio, costo, id);
			System.out.println("Torneo competitivo creado.");
		} catch (Exception e) {
			System.out.println("No fue posible crear torneo: " + e.getMessage());
		}
	}

	private void crearTorneoAmistoso() {
		String id = leerLinea("Id torneo: ");
		String dia = leerLinea("Dia semana: ");
		String idJuego = leerLinea("Id juego: ");
		JuegoMesa juego = sistema.getInventario().get(idJuego);
		if (juego == null) {
			System.out.println("Juego no existe en inventario.");
			return;
		}
		int cupos = leerEntero("Numero participantes: ");
		double descuento = leerDouble("Porcentaje descuento: ");
		try {
			sistema.CrearTorneoAmistoso(dia, new java.util.ArrayList<>(), juego, cupos, descuento, id);
			System.out.println("Torneo amistoso creado.");
		} catch (Exception e) {
			System.out.println("No fue posible crear torneo: " + e.getMessage());
		}
	}

	private void menuListados() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Listados ---");
			System.out.println("1. Administradores");
			System.out.println("2. Empleados");
			System.out.println("3. Clientes");
			System.out.println("4. Inventario prestamo");
			System.out.println("5. Inventario venta");
			System.out.println("6. Mesas");
			System.out.println("7. Turnos");
			System.out.println("8. Menu");
			System.out.println("9. Torneos");
			System.out.println("10. Ventas");
			System.out.println("11. Prestamos clientes");
			System.out.println("12. Prestamos empleados");
			System.out.println("13. Sugerencias");
			System.out.println("0. Volver");
			String opcion = leerLinea("Seleccione una opcion: ");
			switch (opcion) {
			case "1":
				imprimirAdministradores();
				break;
			case "2":
				imprimirEmpleados();
				break;
			case "3":
				imprimirClientes();
				break;
			case "4":
				imprimirInventario();
				break;
			case "5":
				imprimirInventarioVenta();
				break;
			case "6":
				imprimirMesas();
				break;
			case "7":
				imprimirTurnos();
				break;
			case "8":
				imprimirMenu();
				break;
			case "9":
				imprimirTorneos();
				break;
			case "10":
				imprimirVentas();
				break;
			case "11":
				imprimirPrestamosClientes();
				break;
			case "12":
				imprimirPrestamosEmpleados();
				break;
			case "13":
				imprimirSugerencias();
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

	private void menuGestionTurnos() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Gestion de turnos ---");
			System.out.println("1. Asignar empleado a turno");
			System.out.println("2. Cambiar turno de empleado");
			System.out.println("0. Volver");
			String opcion = leerLinea("Seleccione una opcion: ");
			switch (opcion) {
			case "1":
				asignarEmpleadoATurno();
				break;
			case "2":
				cambiarTurnoEmpleado();
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

	private void asignarEmpleadoATurno() {
		String dia = leerLinea("Dia de la semana: ");
		String idEmpleado = leerLinea("Id empleado (login+password): ");
		Turno turno = sistema.getTurnos().get(dia);
		Empleado empleado = sistema.getEmpleados().get(idEmpleado);
		if (turno == null) {
			System.out.println("Turno no existe.");
			return;
		}
		if (empleado == null) {
			System.out.println("Empleado no existe.");
			return;
		}
		turno.adicionarEmpleado(empleado);
		System.out.println("Empleado asignado al turno.");
	}

	private void cambiarTurnoEmpleado() {
		String idEmpleado = leerLinea("Id empleado (login+password): ");
		String diaOrigen = leerLinea("Dia origen: ");
		String diaDestino = leerLinea("Dia destino: ");
		try {
			sistema.cambiarTurnoDirecto(idEmpleado, diaOrigen, diaDestino);
			System.out.println("Turno actualizado.");
		} catch (Exception e) {
			System.out.println("No fue posible cambiar el turno: " + e.getMessage());
		}
	}

	private void menuAprobaciones() {
		boolean continuar = true;
		while (continuar) {
			System.out.println("\n--- Aprobaciones ---");
			System.out.println("1. Ver sugerencias pendientes");
			System.out.println("2. Aprobar sugerencia por indice");
			System.out.println("0. Volver");
			String opcion = leerLinea("Seleccione una opcion: ");
			switch (opcion) {
			case "1":
				listarSugerenciasPendientes();
				break;
			case "2":
				aprobarSugerenciaPendiente();
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

	private void listarSugerenciasPendientes() {
		System.out.println("\nSugerencias pendientes:");
		if (sistema.getSugerenciasPendientes().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		int index = 1;
		for (Sugerencia sugerencia : sistema.getSugerenciasPendientes()) {
			String tipo = sugerencia.isTipoSugerencia() ? "Comida" : "Turno";
			System.out.println(index + ". " + tipo + " - " + sugerencia.getSugerenciaID());
			index++;
		}
	}

	private void aprobarSugerenciaPendiente() {
		if (sistema.getSugerenciasPendientes().isEmpty()) {
			System.out.println("No hay sugerencias pendientes.");
			return;
		}
		int indice = leerEntero("Indice a aprobar: ");
		if (indice < 1 || indice > sistema.getSugerenciasPendientes().size()) {
			System.out.println("Indice invalido.");
			return;
		}
		int actual = 1;
		Sugerencia objetivo = null;
		for (Sugerencia sugerencia : sistema.getSugerenciasPendientes()) {
			if (actual == indice) {
				objetivo = sugerencia;
				break;
			}
			actual++;
		}
		if (objetivo == null) {
			System.out.println("No fue posible encontrar la sugerencia.");
			return;
		}
		try {
			String idEmpleado = objetivo.getEmpleado() == null ? "" : objetivo.getEmpleado().getLogin() + objetivo.getEmpleado().getPassword();
			sistema.aprobarCambioTurno(idEmpleado, objetivo);
			System.out.println("Sugerencia aprobada.");
		} catch (Exception e) {
			System.out.println("No fue posible aprobar: " + e.getMessage());
		}
	}

	private void generarInformeVentas() {
		String inicioTexto = leerLinea("Fecha inicio (YYYY-MM-DD): ");
		String finTexto = leerLinea("Fecha fin (YYYY-MM-DD): ");
		try {
			LocalDate inicio = LocalDate.parse(inicioTexto);
			LocalDate fin = LocalDate.parse(finTexto);
			sistema.generarInformeVentasDetallado(inicio, fin);
		} catch (Exception e) {
			System.out.println("No fue posible generar el informe: " + e.getMessage());
		}
	}

	private void imprimirAdministradores() {
		System.out.println("\nAdministradores:");
		if (sistema.getAdministradores().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (Administrador admin : sistema.getAdministradores().values()) {
			System.out.println(admin.getNombre() + " - " + admin.getDocumentoIdentidad() + " - " + admin.getLogin());
		}
	}

	private void imprimirEmpleados() {
		System.out.println("\nEmpleados:");
		if (sistema.getEmpleados().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (Empleado empleado : sistema.getEmpleados().values()) {
			System.out.println(empleado.getNombre() + " - " + empleado.getDocumentoIdentidad() + " - " + empleado.getLogin());
		}
	}

	private void imprimirClientes() {
		System.out.println("\nClientes:");
		if (sistema.getClientes().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (Cliente cliente : sistema.getClientes().values()) {
			System.out.println(cliente.getNombre() + " - " + cliente.getDocumentoIdentidad() + " - " + cliente.getLogin());
		}
	}

	private void imprimirInventario() {
		System.out.println("\nInventario prestamo:");
		if (sistema.getInventario().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (JuegoMesa juego : sistema.getInventario().values()) {
			System.out.println(juego.getId() + " - " + juego.getNombre() + " - " + juego.getEstado());
		}
	}

	private void imprimirInventarioVenta() {
		System.out.println("\nInventario venta:");
		if (sistema.getInventarioVender().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (JuegoMesa juego : sistema.getInventarioVender().values()) {
			System.out.println(juego.getId() + " - " + juego.getNombre() + " - " + juego.getPrecioVenta());
		}
	}

	private void imprimirMesas() {
		System.out.println("\nMesas:");
		if (sistema.getMesas().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (Mesa mesa : sistema.getMesas().values()) {
			System.out.println(mesa.getIdMesa() + " - personas: " + mesa.getCantidadPersonas());
		}
	}

	private void imprimirTurnos() {
		System.out.println("\nTurnos:");
		if (sistema.getTurnos().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (Turno turno : sistema.getTurnos().values()) {
			System.out.println(turno.getDiaSemana() + " - empleados: " + turno.getEmpleadosAsignados().size());
		}
	}

	private void imprimirMenu() {
		System.out.println("\nMenu:");
		if (sistema.getMenu().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (ProductoMenu producto : sistema.getMenu().values()) {
			System.out.println(producto.getNombre() + " - " + producto.getPrecioBase());
		}
	}

	private void imprimirTorneos() {
		System.out.println("\nTorneos:");
		if (sistema.getTorneos().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (Torneo torneo : sistema.getTorneos().values()) {
			System.out.println(torneo.getId() + " - " + torneo.getDiaSemana() + " - " + torneo.getJuego().getNombre());
		}
	}

	private void imprimirVentas() {
		System.out.println("\nVentas:");
		if (sistema.getHistorialVenta().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (Venta venta : sistema.getHistorialVenta().values()) {
			System.out.println(venta.getIdVenta() + " - " + venta.getFecha() + " - " + venta.getTotal());
		}
	}

	private void imprimirPrestamosClientes() {
		System.out.println("\nPrestamos clientes:");
		if (sistema.getHistorialPrestamosClientes().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (PrestamoCliente prestamo : sistema.getHistorialPrestamosClientes().values()) {
			System.out.println(prestamo.getIdPrestamo() + " - " + prestamo.getJuego().getNombre());
		}
	}

	private void imprimirPrestamosEmpleados() {
		System.out.println("\nPrestamos empleados:");
		if (sistema.getHistorialPrestamosEmpleados().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (PrestamoEmpleado prestamo : sistema.getHistorialPrestamosEmpleados().values()) {
			System.out.println(prestamo.getIdPrestamo() + " - " + prestamo.getJuego().getNombre());
		}
	}

	private void imprimirSugerencias() {
		System.out.println("\nSugerencias:");
		if (sistema.getSugerencias().isEmpty()) {
			System.out.println("(sin registros)");
			return;
		}
		for (Sugerencia sugerencia : sistema.getSugerencias().values()) {
			System.out.println(sugerencia.getSugerenciaID() + " - aprobado: " + sugerencia.isEstaAprobado());
		}
	}
}
