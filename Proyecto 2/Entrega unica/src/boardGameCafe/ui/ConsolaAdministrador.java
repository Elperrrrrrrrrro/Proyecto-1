package boardGameCafe.ui;

import boardGameCafe.system.SistemaBoardGameCafe;

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
		String login = leerLinea("Usuario: ");
		String password = leerLinea("Contrasena: ");
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