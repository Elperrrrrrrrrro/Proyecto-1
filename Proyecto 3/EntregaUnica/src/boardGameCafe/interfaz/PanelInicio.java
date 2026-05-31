package boardGameCafe.interfaz;

import java.awt.*;
import javax.swing.*;

public class PanelInicio extends JPanel {

    private static final long serialVersionUID = 1L;

    private VentanaPrincipal ventana;

    public PanelInicio(VentanaPrincipal ventana) {
        this.ventana = ventana;
        construirUI();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(new Color(245, 245, 250));
        barraTop.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 230)),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));

        JLabel lblApp = new JLabel("Board Game Café — Sistema de Gestión");
        lblApp.setFont(new Font("Serif", Font.PLAIN, 14));
        lblApp.setForeground(new Color(60, 60, 80));

        JPanel semaforo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        semaforo.setOpaque(false);
        semaforo.add(circulo(new Color(255, 90, 90)));
        semaforo.add(circulo(new Color(255, 190, 50)));
        semaforo.add(circulo(new Color(60, 200, 100)));

        barraTop.add(lblApp,    BorderLayout.WEST);
        barraTop.add(semaforo,  BorderLayout.EAST);
        add(barraTop, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(Color.WHITE);
        centro.setBorder(BorderFactory.createEmptyBorder(60, 0, 0, 0));

        JLabel icono = new JLabel("♟", SwingConstants.CENTER);
        icono.setFont(new Font("SansSerif", Font.PLAIN, 56));
        icono.setForeground(new Color(160, 160, 190));
        icono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("Board Game Café", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.PLAIN, 28));
        titulo.setForeground(new Color(30, 30, 60));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));

        JLabel subtitulo = new JLabel("Seleccione su rol para ingresar al sistema", SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(130, 130, 160));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 32, 0));

        JPanel botones = new JPanel();
        botones.setLayout(new BoxLayout(botones, BoxLayout.Y_AXIS));
        botones.setOpaque(false);
        botones.setMaximumSize(new Dimension(260, 200));
        botones.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCliente = botonRol("👤  Cliente");
        JButton btnEmpleado = botonRol("💼  Empleado");
        JButton btnAdmin    = botonRol("🛡  Administrador");
        JButton btnSalir    = botonSalir("🚪  Salir");

        btnCliente.addActionListener(e  -> ventana.mostrarLoginCliente());
        btnEmpleado.addActionListener(e -> ventana.mostrarLoginEmpleado());
        btnAdmin.addActionListener(e    -> ventana.mostrarLoginAdministrador());
        btnSalir.addActionListener(e    -> System.exit(0));

        botones.add(btnCliente);
        botones.add(Box.createVerticalStrut(10));
        botones.add(btnEmpleado);
        botones.add(Box.createVerticalStrut(10));
        botones.add(btnAdmin);
        botones.add(Box.createVerticalStrut(10));
        botones.add(btnSalir);

        centro.add(icono);
        centro.add(titulo);
        centro.add(subtitulo);
        centro.add(botones);

        add(centro, BorderLayout.CENTER);
        JLabel nota = new JLabel("JFrame principal · CardLayout", SwingConstants.CENTER);
        nota.setFont(new Font("SansSerif", Font.PLAIN, 10));
        nota.setForeground(new Color(180, 180, 200));
        nota.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        add(nota, BorderLayout.SOUTH);
    }

    // helpers
    private JButton botonRol(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("SansSerif", Font.PLAIN, 14));
        b.setBackground(Color.WHITE);
        b.setForeground(new Color(40, 40, 70));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 210), 1, true),
            BorderFactory.createEmptyBorder(12, 0, 12, 0)));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(260, 46));
        b.setPreferredSize(new Dimension(260, 46));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(new Color(240, 240, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(Color.WHITE);
            }
        });
        return b;
    }

    private JButton botonSalir(String texto) {
        JButton b = botonRol(texto);
        b.setForeground(new Color(180, 60, 60));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 170, 170), 1, true),
            BorderFactory.createEmptyBorder(12, 0, 12, 0)));
        return b;
    }

    private JLabel circulo(Color color) {
        JLabel c = new JLabel() {
            protected void paintComponent(Graphics g) {
                g.setColor(color);
                g.fillOval(0, 0, 12, 12);
            }
        };
        c.setPreferredSize(new Dimension(12, 12));
        return c;
    }
}
