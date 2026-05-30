package boardGameCafe.ui;

import java.util.Scanner;
import boardGameCafe.system.SistemaBoardGameCafe;

public abstract class ConsolaUsuario {
	protected final SistemaBoardGameCafe sistema;
	protected final Scanner scanner;

	protected ConsolaUsuario(SistemaBoardGameCafe sistema) {
		this.sistema = sistema;
		this.scanner = new Scanner(System.in);
	}

	public abstract void iniciar();

	protected String leerLinea(String mensaje) {
		System.out.print(mensaje);
		return scanner.nextLine().trim();
	}

	protected int leerEntero(String mensaje) {
		while (true) {
			String texto = leerLinea(mensaje);
			try {
				return Integer.parseInt(texto);
			} catch (NumberFormatException e) {
				System.out.println("Entrada invalida. Debe ser un numero entero.");
			}
		}
	}

	protected double leerDouble(String mensaje) {
		while (true) {
			String texto = leerLinea(mensaje);
			try {
				return Double.parseDouble(texto);
			} catch (NumberFormatException e) {
				System.out.println("Entrada invalida. Debe ser un numero.");
			}
		}
	}

	protected boolean confirmar(String mensaje) {
		String respuesta = leerLinea(mensaje + " (s/n): ");
		return "s".equalsIgnoreCase(respuesta);
	}
}