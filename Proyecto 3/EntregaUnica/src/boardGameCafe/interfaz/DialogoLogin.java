package boardGameCafe.interfaz;

import java.awt.*;
import javax.swing.*;
import boardGameCafe.system.SistemaBoardGameCafe;

public class DialogoLogin extends JPanel {

    private static final long serialVersionUID = 1L;

    public enum Rol { CLIENTE, EMPLEADO, ADMINISTRADOR }

    private VentanaPrincipal  ventana;
    private SistemaBoardGameCafe sistema;
    private Rol rol;

    private JTextField     fUsuario;
    private JPasswordField fPassword;
    private JLabel         lblError;

    public DialogoLogin(VentanaPrincipal ventana, SistemaBoardGameCafe sistema, Rol rol) {
        this.ventana  = ventana;
        this.sistema  = sistema;
        this.rol      = rol;
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

        barraTop.add(lblApp,   BorderLayout.WEST);
        barraTop.add(semaforo, BorderLayout.EAST);
        add(barraTop, BorderLayout.NORTH);

        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(new Color(248, 248, 253));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 235), 1, true),
            BorderFactory.createEmptyBorder(32, 40, 32, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(4, 4, 4, 4);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;

        JLabel lblTitulo = new JLabel("Iniciar sesión");
        lblTitulo.setFont(new Font("Serif", Font.PLAIN, 20));
        lblTitulo.setForeground(new Color(30, 30, 60));
        gbc.gridx = 0; gbc.gridy = 0;
        tarjeta.add(lblTitulo, gbc);

        JLabel lblRol = new JLabel(subtituloRol() + "  ·  JDialog modal");
        lblRol.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblRol.setForeground(new Color(140, 140, 170));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 4, 16, 4);
        tarjeta.add(lblRol, gbc);
        gbc.insets = new Insets(4, 4, 4, 4);

        gbc.gridy = 2; gbc.gridwidth = 2;
        tarjeta.add(etiqueta(etiquetaCampo1()), gbc);
        gbc.gridy = 3;
        fUsuario = campo(placeholderCampo1());
        tarjeta.add(fUsuario, gbc);

        if (rol != Rol.CLIENTE) {
            gbc.gridy = 4;
            tarjeta.add(etiqueta("Contraseña"), gbc);
            gbc.gridy = 5;
            fPassword = new JPasswordField(22);
            estilizarCampo(fPassword);
            tarjeta.add(fPassword, gbc);
        }

        lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblError.setForeground(new Color(180, 50, 50));
        gbc.gridy = 6; gbc.insets = new Insets(2, 4, 6, 4);
        tarjeta.add(lblError, gbc);
        
        gbc.gridy  = 7;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridwidth = 1; gbc.weightx = 0.5;

        JButton btnIngresar  = botonPrimario("Ingresar");
        JButton btnCancelar  = botonSecundario("Cancelar");

        btnIngresar.addActionListener(e -> intentarLogin());
        btnCancelar.addActionListener(e -> {
            limpiar();
            ventana.mostrarInicio();
        });

        // Enter también hace login
        getRootPane().setDefaultButton(btnIngresar);
        if (fPassword != null) {
            fPassword.addActionListener(e -> intentarLogin());
        }
        fUsuario.addActionListener(e -> intentarLogin());

        gbc.gridx = 0; tarjeta.add(btnIngresar, gbc);
        gbc.gridx = 1; tarjeta.add(btnCancelar, gbc);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(tarjeta);
        add(wrapper, BorderLayout.CENTER);
    }

    private void intentarLogin() {
        String usuario = fUsuario.getText().trim();
        String password = (fPassword != null)
                ? new String(fPassword.getPassword()).trim()
                : "";

        if (usuario.isEmpty()) {
            mostrarError("Ingrese " + etiquetaCampo1().toLowerCase() + ".");
            return;
        }
        if (rol != Rol.CLIENTE && password.isEmpty()) {
            mostrarError("Ingrese la contraseña.");
            return;
        }

        boolean exito;
        switch (rol) {
            case CLIENTE:
                exito = sistema.iniciarSesionCliente(usuario);
                break;
            case EMPLEADO:
                exito = sistema.inciarSesionEmpleado(usuario, password);
                break;
            case ADMINISTRADOR:
            default:
                exito = sistema.iniciarSesionAdministrador(usuario, password);
                break;
        }

        if (exito) {
            lblError.setText(" ");
            switch (rol) {
                case CLIENTE:       ventana.abrirVentanaCliente();       break;
                case EMPLEADO:      ventana.abrirVentanaEmpleado();      break;
                case ADMINISTRADOR: ventana.abrirVentanaAdministrador(); break;
            }
        } else {
            mostrarError("Credenciales inválidas. Intente nuevamente.");
        }
    }

    private void mostrarError(String msg) {
        lblError.setText(msg);
    }

    public void limpiar() {
        fUsuario.setText("");
        if (fPassword != null) fPassword.setText("");
        lblError.setText(" ");
    }

    private String subtituloRol() {
        switch (rol) {
            case CLIENTE:       return "Cliente";
            case EMPLEADO:      return "Empleado";
            case ADMINISTRADOR: return "Administrador";
            default:            return "";
        }
    }

    private String etiquetaCampo1() {
        return (rol == Rol.CLIENTE) ? "Documento de identidad" : "Usuario";
    }

    private String placeholderCampo1() {
        switch (rol) {
            case CLIENTE:       return "N° de documento";
            case EMPLEADO:      return "Login de empleado";
            case ADMINISTRADOR: return "admin";
            default:            return "";
        }
    }

    // helpers

    private JTextField campo(String placeholder) {
        JTextField f = new JTextField(22);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setToolTipText(placeholder);
        estilizarCampo(f);
        return f;
    }

    private void estilizarCampo(JComponent f) {
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 215), 1, true),
            BorderFactory.createEmptyBorder(7, 10, 7, 10)));
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(new Color(80, 80, 110));
        l.setBorder(BorderFactory.createEmptyBorder(6, 0, 2, 0));
        return l;
    }

    private JButton botonPrimario(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBackground(new Color(60, 60, 160));
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 140), 1, true),
            BorderFactory.createEmptyBorder(9, 0, 9, 0)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton botonSecundario(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBackground(Color.WHITE);
        b.setForeground(new Color(100, 100, 130));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 215), 1, true),
            BorderFactory.createEmptyBorder(9, 0, 9, 0)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
