package boardGameCafe.ui;

import boardGameCafe.system.SistemaBoardGameCafe;

public class ConsolaCliente extends ConsolaUsuario {

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
		String documento = leerLinea("Usuario: ");
		String password = leerLinea("Contrasena: ");
		boolean iniciado = validarCliente(documento, password) && sistema.iniciarSesionCliente(documento);
		if (iniciado) {
			System.out.println("Sesion iniciada.");
			menuSesion();
			sistema.cerrarSesion();
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