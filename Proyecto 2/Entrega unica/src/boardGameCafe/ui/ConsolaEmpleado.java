package boardGameCafe.ui;

import boardGameCafe.system.SistemaBoardGameCafe;

public class ConsolaEmpleado extends ConsolaUsuario {

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
		String login = leerLinea("Usuario: ");
		String password = leerLinea("Contrasena: ");
		boolean iniciado = sistema.inciarSesionEmpleado(login, password);
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
			System.out.println("\n--- Menu Empleado ---");
			System.out.println("0. Cerrar sesion");
			String opcion = leerLinea("Seleccione una opcion: ");
			switch (opcion) {
			case "0":
				continuar = false;
				break;
			default:
				System.out.println("Opcion invalida.");
				break;
			}
		}
	}
}