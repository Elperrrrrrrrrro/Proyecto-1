package boardGameCafe.interfaz;

import java.awt.*;
import javax.swing.*;
import boardGameCafe.system.SistemaBoardGameCafe;
import boardGameCafe.logic.Venta;

public class PanelDashboard extends JPanel {

    private static final long serialVersionUID = 1L;

    private SistemaBoardGameCafe sistema;
    private VentanaAdministrador ventana;

    // Etiquetas de estadísticas (se actualizan al refrescar)
    private JLabel lblJuegosPrestamo;
    private JLabel lblJuegosVenta;
    private JLabel lblMesas;
    private JLabel lblEmpleados;
    private JLabel lblVentasHoy;
    private JLabel lblTorneos;

    public PanelDashboard(VentanaAdministrador ventana, SistemaBoardGameCafe sistema) {
        this.ventana = ventana;
        this.sistema = sistema;
        construirUI();
        refrescar();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel titulo = new JLabel("Dashboard general");
        titulo.setFont(new Font("Serif", Font.PLAIN, 22));
        titulo.setForeground(new Color(30, 30, 60));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);

        //Las tarjetas de estadisticas
        JPanel gridStats = new JPanel(new GridLayout(2, 3, 20, 16));
        gridStats.setOpaque(false);
        gridStats.setMaximumSize(new Dimension(900, 160));
        gridStats.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblJuegosPrestamo = new JLabel("—");
        lblJuegosVenta    = new JLabel("—");
        lblMesas          = new JLabel("—");
        lblEmpleados      = new JLabel("—");
        lblVentasHoy      = new JLabel("—");
        lblTorneos        = new JLabel("—");

        gridStats.add(tarjeta("Juegos préstamo",  lblJuegosPrestamo));
        gridStats.add(tarjeta("Juegos en venta",  lblJuegosVenta));
        gridStats.add(tarjeta("Mesas activas",    lblMesas));
        gridStats.add(tarjeta("Empleados",        lblEmpleados));
        gridStats.add(tarjeta("Ventas hoy",       lblVentasHoy));
        gridStats.add(tarjeta("Torneos",          lblTorneos));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(900, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Sección acciones rápidas
        JLabel lblAcciones = new JLabel("Acciones rápidas");
        lblAcciones.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblAcciones.setForeground(new Color(100, 100, 130));
        lblAcciones.setBorder(BorderFactory.createEmptyBorder(16, 0, 10, 0));
        lblAcciones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botones.setOpaque(false);
        botones.setAlignmentX(Component.LEFT_ALIGNMENT);

        botones.add(botonAccion("+ Agregar juego",   () -> ventana.mostrarPanel("INVENTARIO")));
        botones.add(botonAccion("+ Nuevo usuario",   () -> ventana.mostrarPanel("USUARIOS")));
        botones.add(botonAccion("Ver visualizaciones",() -> ventana.mostrarPanel("VISUAL")));
        botones.add(botonAccion("Gestionar turnos",  () -> ventana.mostrarPanel("TURNOS")));

        centro.add(gridStats);
        centro.add(Box.createVerticalStrut(16));
        centro.add(sep);
        centro.add(lblAcciones);
        centro.add(botones);

        add(centro, BorderLayout.CENTER);
    }

    private JPanel tarjeta(String descripcion, JLabel lblValor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(248, 248, 253));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 235), 1, true),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)));

        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblDesc.setForeground(new Color(130, 130, 160));

        lblValor.setFont(new Font("Serif", Font.PLAIN, 28));
        lblValor.setForeground(new Color(20, 20, 50));

        card.add(lblDesc,  BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    private JButton botonAccion(String texto, Runnable accion) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(40, 40, 70));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 200), 1, true),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.addActionListener(e -> accion.run());
        return btn;
    }

    public void refrescar() {
        lblJuegosPrestamo.setText(String.valueOf(sistema.getInventario().size()));
        lblJuegosVenta.setText(String.valueOf(sistema.getInventarioVender().size()));

        long mesasActivas = sistema.getMesas().values().stream()
                .filter(m -> m.getClienteActual() != null).count();
        int totalMesas = sistema.getMesas().size();
        lblMesas.setText(mesasActivas + "/" + totalMesas);

        lblEmpleados.setText(String.valueOf(sistema.getEmpleados().size()));

        //Ventas del dia
        double totalHoy = sistema.getHistorialVenta().values().stream()
                .filter(v -> v.getFecha() != null &&
                        v.getFecha().toLocalDate().equals(java.time.LocalDate.now()))
                .mapToDouble(Venta::getTotal).sum();
        lblVentasHoy.setText(String.format("$%,.0f", totalHoy));

        lblTorneos.setText(String.valueOf(sistema.getTorneos().size()));
    }
}