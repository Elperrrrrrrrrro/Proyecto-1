package boardGameCafe;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import boardGameCafe.interfaz.VentanaPrincipal;
import boardGameCafe.system.SistemaBoardGameCafe;
public class Main {
    public static void main(String[] args) {
        aplicarLookAndFeel();
        SwingUtilities.invokeLater(() -> {
            SistemaBoardGameCafe sistema = cargarSistema();
            VentanaPrincipal ventana = new VentanaPrincipal(sistema);
            ventana.setVisible(true);
        });
    }
    private static SistemaBoardGameCafe cargarSistema() {
        SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
        sistema.cargarDatos();
        if (sistema.getAdministradores().isEmpty()) {
            crearAdministradorInicial(sistema);
            sistema.guardarDatos();
        }
        return sistema;
    }
    private static void crearAdministradorInicial(SistemaBoardGameCafe sistema) {
        String login = "admin";
        String password = "123456789";
        sistema.registrarAdministrador(new boardGameCafe.logic.Administrador(
                "Administrador", "0000000000", login, password));
        System.out.println("Administrador inicial creado.");
        System.out.println("Usuario: " + login);
        System.out.println("Contrasena: " + password);
    }
    private static void aplicarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el diseno nativo.");
        }
    }
} 