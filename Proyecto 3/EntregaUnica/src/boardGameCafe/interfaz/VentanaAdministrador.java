package boardGameCafe.interfaz;

import java.awt.*;
import javax.swing.*;
import boardGameCafe.system.SistemaBoardGameCafe;

public class VentanaAdministrador extends JFrame {

    private static final long serialVersionUID = 1L;

    private CardLayout cardLayout;
    private JPanel panelContenido;
    private JLabel labelRuta;
    private SistemaBoardGameCafe sistema;

    private PanelDashboard panelDashboard;
    private PanelUsuarios panelUsuarios;
    private PanelInventario panelInventario;
    private PanelTurnos panelTurnos;
    private PanelVisualizaciones panelVisualizaciones;
    private PanelInformes panelInformes;

    public VentanaAdministrador(SistemaBoardGameCafe sistema) {
        this.sistema = sistema;
        inicializarUI();
    }

    public VentanaAdministrador() {
        this(new SistemaBoardGameCafe());
    }

    private void inicializarUI() {
        setTitle("Board Game Café — Sistema de Gestión");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(new Color(245, 245, 250));
        barraTop.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 230)),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)));

        JLabel titulo = new JLabel("Board Game Café — Sistema de Gestión");
        titulo.setFont(new Font("Serif", Font.PLAIN, 14));
        titulo.setForeground(new Color(60, 60, 80));

        JPanel semaforo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        semaforo.setOpaque(false);
        semaforo.add(circulo(new Color(255, 90, 90)));
        semaforo.add(circulo(new Color(255, 190, 50)));
        semaforo.add(circulo(new Color(60, 200, 100)));

        barraTop.add(titulo, BorderLayout.WEST);
        barraTop.add(semaforo, BorderLayout.EAST);

        labelRuta = new JLabel("Admin → Dashboard");
        labelRuta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelRuta.setForeground(new Color(100, 100, 130));
        labelRuta.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));
        labelRuta.setBackground(new Color(250, 250, 255));
        labelRuta.setOpaque(true);

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(new Color(250, 250, 255));
        cabecera.add(barraTop, BorderLayout.NORTH);
        cabecera.add(labelRuta, BorderLayout.SOUTH);

        JPanel sidebar = construirSidebar();

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(Color.WHITE);

        panelDashboard     = new PanelDashboard(this, sistema);
        panelUsuarios      = new PanelUsuarios(sistema);
        panelInventario    = new PanelInventario(sistema);
        panelTurnos        = new PanelTurnos(sistema);
        panelVisualizaciones = new PanelVisualizaciones(sistema);
        panelInformes      = new PanelInformes(sistema);

        panelContenido.add(panelDashboard,      "DASHBOARD");
        panelContenido.add(panelUsuarios,       "USUARIOS");
        panelContenido.add(panelInventario,     "INVENTARIO");
        panelContenido.add(panelTurnos,         "TURNOS");
        panelContenido.add(panelVisualizaciones,"VISUAL");
        panelContenido.add(panelInformes,       "INFORMES");

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, panelContenido);
        split.setDividerLocation(200);
        split.setDividerSize(1);
        split.setBorder(null);

        setLayout(new BorderLayout());
        add(cabecera, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }

    private JPanel construirSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(250, 250, 255));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 235)));


        JLabel seccion = new JLabel("ADMINISTRADOR");
        seccion.setFont(new Font("SansSerif", Font.BOLD, 10));
        seccion.setForeground(new Color(150, 150, 170));
        seccion.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 0));

        String[][] items = {
            {"Dashboard",       "DASHBOARD",    "Admin → Dashboard"},
            {"Usuarios",        "USUARIOS",     "Admin → Gestión de usuarios"},
            {"Inventario",      "INVENTARIO",   "Admin → Inventario de juegos"},
            {"Turnos",          "TURNOS",       "Admin → Gestión de turnos"},
            {"Visualizaciones ★","VISUAL",      "Admin → Visualizaciones de datos"},
            {"Informes",        "INFORMES",     "Admin → Informe de ventas"},
        };

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setOpaque(false);
        navPanel.add(seccion);

        for (String[] item : items) {
            JButton btn = crearBotonNav(item[0], item[1], item[2]);
            navPanel.add(btn);
        }

 
        JButton btnCerrar = new JButton("Cerrar sesión");
        btnCerrar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnCerrar.setForeground(new Color(180, 60, 60));
        btnCerrar.setBorderPainted(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 0));
        btnCerrar.addActionListener(e -> {
            sistema.cerrarSesion();
            dispose();
        });

        sidebar.add(navPanel, BorderLayout.CENTER);
        sidebar.add(btnCerrar, BorderLayout.SOUTH);
        return sidebar;
    }

    private JButton crearBotonNav(String texto, String panelKey, String ruta) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(new Color(50, 50, 80));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 0));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setForeground(new Color(80, 80, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setForeground(new Color(50, 50, 80));
            }
        });

        btn.addActionListener(e -> mostrarPanel(panelKey, ruta));
        return btn;
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

    public void mostrarPanel(String nombre) {
        String[] rutas = {
            "DASHBOARD:Admin → Dashboard",
            "USUARIOS:Admin → Gestión de usuarios",
            "INVENTARIO:Admin → Inventario de juegos",
            "TURNOS:Admin → Gestión de turnos",
            "VISUAL:Admin → Visualizaciones de datos",
            "INFORMES:Admin → Informe de ventas"
        };
        for (String r : rutas) {
            String[] p = r.split(":");
            if (p[0].equals(nombre)) { mostrarPanel(nombre, p[1]); return; }
        }
        mostrarPanel(nombre, "Admin → " + nombre);
    }

    public void mostrarPanel(String nombre, String ruta) {
        cardLayout.show(panelContenido, nombre);
        labelRuta.setText(ruta);
        if ("DASHBOARD".equals(nombre))   panelDashboard.refrescar();
        if ("TURNOS".equals(nombre))      panelTurnos.refrescar();
        if ("INVENTARIO".equals(nombre))  panelInventario.refrescar();
        if ("USUARIOS".equals(nombre))    panelUsuarios.refrescar();
        if ("INFORMES".equals(nombre))    panelInformes.refrescar();
    }

    public SistemaBoardGameCafe getSistema() {
        return sistema;
    }
}