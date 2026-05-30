package boardGameCafe.interfaz;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import boardGameCafe.logic.*;
import boardGameCafe.system.SistemaBoardGameCafe;

public class PanelUsuarios extends JPanel {

    private static final long serialVersionUID = 1L;

    private SistemaBoardGameCafe sistema;

    // panel que cambia según el botón q se elija 
    private JPanel panelFormulario;
    private CardLayout cardFormulario;

    // Formulario empleado
    private JTextField fNombreEmp, fDocumentoEmp, fLoginEmp, fPasswordEmp, fEdadEmp;
    private JComboBox<String> cbTipoEmp;

    // Formulario administrador
    private JTextField fNombreAdmin, fDocumentoAdmin, fLoginAdmin, fPasswordAdmin;

    // Formulario cliente
    private JTextField fNombreCli, fDocumentoCli, fLoginCli, fPasswordCli;

    public PanelUsuarios(SistemaBoardGameCafe sistema) {
        this.sistema = sistema;
        construirUI();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
        
        JLabel titulo = new JLabel("Gestión de usuarios");
        titulo.setFont(new Font("Serif", Font.PLAIN, 22));
        titulo.setForeground(new Color(30, 30, 60));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 16, 0));

        JButton btnAdmin  = botonSecundario("Nuevo administrador");
        JButton btnEmp    = botonSecundario("Nuevo empleado");
        JButton btnCli    = botonSecundario("Nuevo cliente");

        btnAdmin.addActionListener(e -> cardFormulario.show(panelFormulario, "ADMIN"));
        btnEmp.addActionListener(e ->   cardFormulario.show(panelFormulario, "EMPLEADO"));
        btnCli.addActionListener(e ->   cardFormulario.show(panelFormulario, "CLIENTE"));

        btnPanel.add(btnAdmin);
        btnPanel.add(btnEmp);
        btnPanel.add(btnCli);

        cardFormulario = new CardLayout();
        panelFormulario = new JPanel(cardFormulario);
        panelFormulario.setOpaque(false);

        panelFormulario.add(formVacio(),        "VACIO");
        panelFormulario.add(formAdministrador(),"ADMIN");
        panelFormulario.add(formEmpleado(),     "EMPLEADO");
        panelFormulario.add(formCliente(),      "CLIENTE");

        cardFormulario.show(panelFormulario, "VACIO");

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(titulo,   BorderLayout.NORTH);
        norte.add(btnPanel, BorderLayout.CENTER);

        add(norte,          BorderLayout.NORTH);
        add(panelFormulario,BorderLayout.CENTER);
    }

    private JPanel formAdministrador() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints gbc = gbc();

        JLabel subtitulo = new JLabel("Crear nuevo administrador");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(120, 120, 150));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        p.add(subtitulo, gbc);

        fNombreAdmin    = campo("Nombre completo");
        fDocumentoAdmin = campo("N° documento");
        fLoginAdmin     = campo("Usuario de acceso");
        fPasswordAdmin  = campo("Contraseña");

        gbc.gridwidth=1;
        agregarFila(p, gbc, 1, "Nombre completo", fNombreAdmin, "Documento", fDocumentoAdmin);
        agregarFila(p, gbc, 3, "Login",            fLoginAdmin,  "Contraseña", fPasswordAdmin);

        JButton btnReg = botonPrimario("Registrar");
        JButton btnCan = botonSecundario("Cancelar");

        btnReg.addActionListener(e -> registrarAdministrador());
        btnCan.addActionListener(e -> limpiarAdmin());

        JPanel bots = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bots.setOpaque(false);
        bots.add(btnReg); bots.add(btnCan);
        gbc.gridx=0; gbc.gridy=5; gbc.gridwidth=2;
        p.add(bots, gbc);

        return p;
    }

    private JPanel formEmpleado() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints gbc = gbc();

        JLabel subtitulo = new JLabel("Crear nuevo empleado  ·  JPanel con GridBagLayout");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(120, 120, 150));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        p.add(subtitulo, gbc);

        fNombreEmp    = campo("Nombre");
        fDocumentoEmp = campo("N° documento");
        fLoginEmp     = campo("Usuario de acceso");
        fPasswordEmp  = campo("Contraseña");
        fEdadEmp      = campo("Edad del empleado");
        cbTipoEmp     = new JComboBox<>(new String[]{"Mesero", "Cocinero"});
        cbTipoEmp.setFont(new Font("SansSerif", Font.PLAIN, 13));

        gbc.gridwidth=1;
        agregarFila(p, gbc, 1, "Nombre completo", fNombreEmp, "Documento", fDocumentoEmp);

        gbc.gridy=3; gbc.gridx=0;
        p.add(etiqueta("Tipo de empleado"), gbc);
        gbc.gridx=1;
        p.add(etiqueta("Login"), gbc);
        gbc.gridy=4; gbc.gridx=0; gbc.fill=GridBagConstraints.HORIZONTAL;
        p.add(cbTipoEmp, gbc);
        gbc.gridx=1;
        p.add(fLoginEmp, gbc);

        agregarFila(p, gbc, 5, "Contraseña", fPasswordEmp, "Edad", fEdadEmp);

        JButton btnReg = botonPrimario("Registrar");
        JButton btnCan = botonSecundario("Cancelar");
        btnReg.addActionListener(e -> registrarEmpleado());
        btnCan.addActionListener(e -> limpiarEmpleado());

        JPanel bots = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bots.setOpaque(false);
        bots.add(btnReg); bots.add(btnCan);
        gbc.gridx=0; gbc.gridy=7; gbc.gridwidth=2;
        p.add(bots, gbc);

        return p;
    }

    private JPanel formCliente() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints gbc = gbc();

        JLabel subtitulo = new JLabel("Crear nuevo cliente");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(120, 120, 150));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        p.add(subtitulo, gbc);

        fNombreCli    = campo("Nombre");
        fDocumentoCli = campo("N° documento");
        fLoginCli     = campo("Usuario de acceso");
        fPasswordCli  = campo("Contraseña");

        gbc.gridwidth=1;
        agregarFila(p, gbc, 1, "Nombre completo", fNombreCli, "Documento", fDocumentoCli);
        agregarFila(p, gbc, 3, "Login",            fLoginCli,  "Contraseña", fPasswordCli);

        JButton btnReg = botonPrimario("Registrar");
        JButton btnCan = botonSecundario("Cancelar");
        btnReg.addActionListener(e -> registrarCliente());
        btnCan.addActionListener(e -> limpiarCliente());

        JPanel bots = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bots.setOpaque(false);
        bots.add(btnReg); bots.add(btnCan);
        gbc.gridx=0; gbc.gridy=5; gbc.gridwidth=2;
        p.add(bots, gbc);

        return p;
    }

    private JPanel formVacio() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        p.add(new JLabel("Seleccione el tipo de usuario a crear."));
        return p;
    }

    private void registrarAdministrador() {
        String nombre   = fNombreAdmin.getText().trim();
        String doc      = fDocumentoAdmin.getText().trim();
        String login    = fLoginAdmin.getText().trim();
        String password = fPasswordAdmin.getText().trim();

        if (nombre.isEmpty() || doc.isEmpty() || login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Administrador admin = new Administrador(nombre, doc, login, password);
        sistema.registrarAdministrador(admin);
        JOptionPane.showMessageDialog(this, "Administrador registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        limpiarAdmin();
    }

    private void registrarEmpleado() {
        String nombre   = fNombreEmp.getText().trim();
        String doc      = fDocumentoEmp.getText().trim();
        String login    = fLoginEmp.getText().trim();
        String password = fPasswordEmp.getText().trim();
        String edadStr  = fEdadEmp.getText().trim();
        String tipo     = (String) cbTipoEmp.getSelectedItem();

        if (nombre.isEmpty() || doc.isEmpty() || login.isEmpty() || password.isEmpty() || edadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int edad;
        try { edad = Integer.parseInt(edadStr); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Empleado emp;
        if ("Mesero".equals(tipo)) {
            emp = new Mesero(nombre, doc, edad, login, password, new ArrayList<>());
        } else {
            emp = new Cocinero(nombre, doc, edad, login, password);
        }
        sistema.agregarEmpleado(emp);
        JOptionPane.showMessageDialog(this, "Empleado registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        limpiarEmpleado();
    }

    private void registrarCliente() {
        String nombre   = fNombreCli.getText().trim();
        String doc      = fDocumentoCli.getText().trim();
        String login    = fLoginCli.getText().trim();
        String password = fPasswordCli.getText().trim();

        if (nombre.isEmpty() || doc.isEmpty() || login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Cliente cliente = new Cliente(nombre, doc, login, password);
        sistema.registrarCliente(cliente);
        JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        limpiarCliente();
    }

    private void limpiarAdmin()    { limpiar(fNombreAdmin, fDocumentoAdmin, fLoginAdmin, fPasswordAdmin); }
    private void limpiarEmpleado() { limpiar(fNombreEmp, fDocumentoEmp, fLoginEmp, fPasswordEmp, fEdadEmp); }
    private void limpiarCliente()  { limpiar(fNombreCli, fDocumentoCli, fLoginCli, fPasswordCli); }

    private void limpiar(JTextField... campos) {
        for (JTextField f : campos) f.setText("");
    }

    public void refrescar() { }

    // helpers
    private void agregarFila(JPanel p, GridBagConstraints gbc, int fila,
            String lbl1, JComponent c1, String lbl2, JComponent c2) {
        gbc.gridy=fila;   gbc.gridx=0; p.add(etiqueta(lbl1), gbc);
        gbc.gridx=1;                   p.add(etiqueta(lbl2), gbc);
        gbc.gridy=fila+1; gbc.gridx=0; gbc.fill=GridBagConstraints.HORIZONTAL; p.add(c1, gbc);
        gbc.gridx=1;                   p.add(c2, gbc);
    }

    private JTextField campo(String placeholder) {
        JTextField f = new JTextField(22);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 215), 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(new Color(80, 80, 110));
        l.setBorder(BorderFactory.createEmptyBorder(8, 0, 2, 16));
        return l;
    }

    private GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(2, 4, 2, 16);
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;
        return g;
    }

    private JButton botonPrimario(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBackground(Color.WHITE);
        b.setForeground(new Color(40, 40, 70));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(160, 160, 190), 1, true),
            BorderFactory.createEmptyBorder(6, 16, 6, 16)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton botonSecundario(String texto) {
        return botonPrimario(texto);
    }
}