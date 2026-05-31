package boardGameCafe.interfaz;

import java.awt.*;
import javax.swing.*;
import boardGameCafe.logic.Cliente;
import boardGameCafe.system.SistemaBoardGameCafe;

public class VentanaCliente extends JFrame {

    private static final long serialVersionUID = 1L;

    private SistemaBoardGameCafe sistema;
    private VentanaPrincipal     ventanaPrincipal;
    private Cliente              clienteActual;

    private CardLayout cardLayout;
    private JPanel     panelContenido;
    private JLabel     labelRuta;

    private PanelPerfil       panelPerfil;
    private PanelJuegos       panelJuegos;
    private PanelMenuCliente  panelMenu;

    public VentanaCliente(SistemaBoardGameCafe sistema, VentanaPrincipal ventanaPrincipal) {
        this.sistema          = sistema;
        this.ventanaPrincipal = ventanaPrincipal;
        this.clienteActual    = resolverClienteActual();
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("Board Game Café — Cliente");
        setSize(1050, 640);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(new Color(245, 245, 250));
        barraTop.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 230)),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)));

        String nombreCliente = clienteActual != null ? clienteActual.getNombre() : "Cliente";
        JLabel titulo = new JLabel("Board Game Café — Bienvenido, " + nombreCliente);
        titulo.setFont(new Font("Serif", Font.PLAIN, 14));
        titulo.setForeground(new Color(60, 60, 80));

        JPanel semaforo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        semaforo.setOpaque(false);
        semaforo.add(circulo(new Color(255, 90, 90)));
        semaforo.add(circulo(new Color(255, 190, 50)));
        semaforo.add(circulo(new Color(60, 200, 100)));

        barraTop.add(titulo,   BorderLayout.WEST);
        barraTop.add(semaforo, BorderLayout.EAST);

        labelRuta = new JLabel("Cliente → Mi perfil");
        labelRuta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelRuta.setForeground(new Color(100, 100, 130));
        labelRuta.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));
        labelRuta.setBackground(new Color(250, 250, 255));
        labelRuta.setOpaque(true);

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(new Color(250, 250, 255));
        cabecera.add(barraTop,  BorderLayout.NORTH);
        cabecera.add(labelRuta, BorderLayout.SOUTH);

        cardLayout     = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(Color.WHITE);

        panelPerfil = new PanelPerfil(sistema, clienteActual);
        panelJuegos = new PanelJuegos(sistema, clienteActual);
        panelMenu   = new PanelMenuCliente(sistema, clienteActual);

        panelContenido.add(panelPerfil, "PERFIL");
        panelContenido.add(panelJuegos, "JUEGOS");
        panelContenido.add(panelMenu,   "MENU");

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, construirSidebar(), panelContenido);
        split.setDividerLocation(200);
        split.setDividerSize(1);
        split.setBorder(null);

        setLayout(new BorderLayout());
        add(cabecera, BorderLayout.NORTH);
        add(split,    BorderLayout.CENTER);

        mostrarPanel("PERFIL");
    }

    private JPanel construirSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(250, 250, 255));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 235)));

        JLabel seccion = new JLabel("CLIENTE");
        seccion.setFont(new Font("SansSerif", Font.BOLD, 10));
        seccion.setForeground(new Color(150, 150, 170));
        seccion.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 0));

        String[][] items = {
            {"Mi perfil",   "PERFIL",  "Cliente → Mi perfil"},
            {"Juegos",      "JUEGOS",  "Cliente → Catálogo de juegos"},
            {"Menú",        "MENU",    "Cliente → Menú de cafetería"},
        };

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setOpaque(false);
        navPanel.add(seccion);

        for (String[] item : items) {
            navPanel.add(crearBotonNav(item[0], item[1], item[2]));
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
            sistema.guardarDatos();
            dispose();
        });

        sidebar.add(navPanel,  BorderLayout.CENTER);
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
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setForeground(new Color(80, 80, 200)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setForeground(new Color(50, 50, 80));  }
        });
        btn.addActionListener(e -> mostrarPanel(panelKey, ruta));
        return btn;
    }

    public void mostrarPanel(String nombre) {
        switch (nombre) {
            case "PERFIL": mostrarPanel("PERFIL", "Cliente → Mi perfil");           break;
            case "JUEGOS": mostrarPanel("JUEGOS", "Cliente → Catálogo de juegos");  break;
            case "MENU":   mostrarPanel("MENU",   "Cliente → Menú de cafetería");   break;
            default:       mostrarPanel(nombre, "Cliente → " + nombre);
        }
    }

    public void mostrarPanel(String nombre, String ruta) {
        cardLayout.show(panelContenido, nombre);
        labelRuta.setText(ruta);
        if ("PERFIL".equals(nombre)) panelPerfil.refrescar();
        if ("JUEGOS".equals(nombre)) panelJuegos.refrescar();
        if ("MENU".equals(nombre))   panelMenu.refrescar();
    }

 
    private Cliente resolverClienteActual() {
        for (Cliente c : sistema.getClientes().values()) {
            return c;
        }
        return null;
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

    public SistemaBoardGameCafe getSistema()   { return sistema; }
    public Cliente getClienteActual()          { return clienteActual; }
}
