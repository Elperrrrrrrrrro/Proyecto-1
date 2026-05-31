package boardGameCafe.interfaz;

import java.awt.*;
import javax.swing.*;
import boardGameCafe.system.SistemaBoardGameCafe;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private SistemaBoardGameCafe sistema;
    private CardLayout cardLayout;
    private JPanel panelCentral;

    private PanelInicio    panelInicio;
    private DialogoLogin   loginCliente;
    private DialogoLogin   loginEmpleado;
    private DialogoLogin   loginAdmin;

    public VentanaPrincipal() {
        sistema = new SistemaBoardGameCafe();
        try { sistema.cargarDatos(); } catch (Exception ignored) {}
        inicializarUI();
    }

    public VentanaPrincipal(SistemaBoardGameCafe sistemaExistente) {
        sistema = sistemaExistente;
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("Board Game Café — Sistema de Gestión");
        setSize(860, 540);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout   = new CardLayout();
        panelCentral = new JPanel(cardLayout);

        panelInicio   = new PanelInicio(this);
        loginCliente  = new DialogoLogin(this, sistema, DialogoLogin.Rol.CLIENTE);
        loginEmpleado = new DialogoLogin(this, sistema, DialogoLogin.Rol.EMPLEADO);
        loginAdmin    = new DialogoLogin(this, sistema, DialogoLogin.Rol.ADMINISTRADOR);

        panelCentral.add(panelInicio,   "INICIO");
        panelCentral.add(loginCliente,  "LOGIN_CLI");
        panelCentral.add(loginEmpleado, "LOGIN_EMP");
        panelCentral.add(loginAdmin,    "LOGIN_ADM");

        add(panelCentral);
        cardLayout.show(panelCentral, "INICIO");
    }

    public void mostrarLoginCliente()      { cardLayout.show(panelCentral, "LOGIN_CLI"); }
    public void mostrarLoginEmpleado()     { cardLayout.show(panelCentral, "LOGIN_EMP"); }
    public void mostrarLoginAdministrador(){ cardLayout.show(panelCentral, "LOGIN_ADM"); }
    public void mostrarInicio()            { cardLayout.show(panelCentral, "INICIO");    }

    
    public void abrirVentanaAdministrador() {
        setVisible(false);
        VentanaAdministrador va = new VentanaAdministrador(sistema);
        va.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent e) {
                loginAdmin.limpiar();
                mostrarInicio();
                setVisible(true);
            }
        });
        va.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        va.setVisible(true);
    }
    
    public void abrirVentanaEmpleado() {
        setVisible(false);
        VentanaEmpleado ve = new VentanaEmpleado(sistema, this);
        ve.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent e) {
                loginEmpleado.limpiar();
                mostrarInicio();
                setVisible(true);
            }
        });
        ve.setVisible(true);
    }

    public void abrirVentanaCliente() {
        setVisible(false);
        VentanaCliente vc = new VentanaCliente(sistema, this);
        vc.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent e) {
                loginCliente.limpiar();
                mostrarInicio();
                setVisible(true);
            }
        });
        vc.setVisible(true);
    }

    public SistemaBoardGameCafe getSistema() { return sistema; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal vp = new VentanaPrincipal();
            vp.setVisible(true);
        });
    }
}
