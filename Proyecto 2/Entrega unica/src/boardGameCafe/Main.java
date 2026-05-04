package boardGameCafe;

import java.util.Scanner;

import boardGameCafe.logic.Administrador;
import boardGameCafe.system.SistemaBoardGameCafe;
import boardGameCafe.ui.ConsolaAdministrador;
import boardGameCafe.ui.ConsolaCliente;
import boardGameCafe.ui.ConsolaEmpleado;

public class Main {
    public static void main(String[] args) {
    	SistemaBoardGameCafe sistema = cargarSistema();
    	ConsolaAdministrador consolaAdministrador = new ConsolaAdministrador(sistema);
    	ConsolaCliente consolaCliente = new ConsolaCliente(sistema);
    	ConsolaEmpleado consolaEmpleado = new ConsolaEmpleado(sistema);

    	mostrarMenuTipoUsuario(consolaAdministrador, consolaCliente, consolaEmpleado);
    }

    private static SistemaBoardGameCafe cargarSistema() {
    	SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
    	
    	sistema.cargarDatos();
    	
    	if (sistema.getAdministradores().isEmpty()) {
			Administrador adminInicial = crearAdminInicial();
			sistema.registrarAdministrador(adminInicial);
			sistema.guardarDatos();
			mostrarCredencialesIniciales(adminInicial);
		}
    	
    	return sistema;
    	
    }

    private static Administrador crearAdminInicial() {
    	String login = "admin";
    	String password = "123456789";
    	return new Administrador("Administrador", "0000000000", login, password);
    }

    private static void mostrarCredencialesIniciales(Administrador adminInicial) {
    	System.out.println("Administrador inicial creado.");
    	System.out.println("Usuario: " + adminInicial.getLogin());
    	System.out.println("Contrasena: " + adminInicial.getPassword());
    }

    private static void mostrarMenuTipoUsuario(
    			ConsolaAdministrador consolaAdministrador,
    			ConsolaCliente consolaCliente,
    			ConsolaEmpleado consolaEmpleado) {
    	Scanner scanner = new Scanner(System.in);
    	
    	
    	boolean continuar = true;

    	while (continuar) {
    		System.out.println("Seleccione el tipo de usuario para iniciar sesion:");
    		System.out.println("1. Cliente");
    		System.out.println("2. Empleado");
    		System.out.println("3. Administrador");
    		System.out.println("0. Salir");

    		String opcion = scanner.nextLine().trim();

    		switch (opcion) {
    		case "1":
    			consolaCliente.iniciar();
    			break;
    		case "2":
    			consolaEmpleado.iniciar();
    			break;
    		case "3":
    			consolaAdministrador.iniciar();
    			break;
    		case "0":
    			continuar = false;
    			break;
    		default:
    			System.out.println("Opcion invalida. Intente de nuevo.");
    			break;
    		}
    	}

    	scanner.close();
    }
}