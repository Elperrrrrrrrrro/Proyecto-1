package boardGameCafe;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import boardGameCafe.system.SistemaBoardGameCafe;
import boardGameCafe.interfaz.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {
        aplicarLookAndFeel();

        SwingUtilities.invokeLater(() -> {
            SistemaBoardGameCafe sistema = new SistemaBoardGameCafe();
            sistema.cargarDatos();

            VentanaPrincipal ventana = new VentanaPrincipal(sistema);
            ventana.setVisible(true);
        });
    }

    private static void aplicarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el diseño nativo.");
        }
    }
}