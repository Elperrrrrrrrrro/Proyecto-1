package boardGameCafe.interfaz;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import boardGameCafe.logic.*;
import boardGameCafe.system.SistemaBoardGameCafe;

public class PanelDashboardEmpleado extends JPanel {

    private static final long serialVersionUID = 1L;

    private SistemaBoardGameCafe sistema;
    private VentanaEmpleado      ventana;

    private JLabel lblMesasOcupadas;
    private JLabel lblMisTurnos;

    public PanelDashboardEmpleado(VentanaEmpleado ventana, SistemaBoardGameCafe sistema) {
        this.ventana = ventana;
        this.sistema = sistema;
        construirUI();
        refrescar();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel titulo = new JLabel("Panel de empleado");
        titulo.setFont(new Font("Serif", Font.PLAIN, 22));
        titulo.setForeground(new Color(30, 30, 60));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);

        JPanel gridStats = new JPanel(new GridLayout(1, 2, 20, 0));
        gridStats.setOpaque(false);
        gridStats.setMaximumSize(new Dimension(600, 100));
        gridStats.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblMesasOcupadas = new JLabel("—");
        lblMisTurnos     = new JLabel("—");

        gridStats.add(tarjeta("Mesas ocupadas",  lblMesasOcupadas));
        gridStats.add(tarjeta("Mis turnos",      lblMisTurnos));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(900, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblAcciones = new JLabel("Acciones rápidas");
        lblAcciones.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblAcciones.setForeground(new Color(100, 100, 130));
        lblAcciones.setBorder(BorderFactory.createEmptyBorder(16, 0, 10, 0));
        lblAcciones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botones.setOpaque(false);
        botones.setAlignmentX(Component.LEFT_ALIGNMENT);

        botones.add(botonAccion("Registrar venta",       () -> ventana.mostrarPanel("VENTA")));
        botones.add(botonAccion("Gestionar mesas",       () -> ventana.mostrarPanel("MESAS")));
        botones.add(botonAccion("Sugerir platillo",      this::dialogoSugerirPlatillo));
        botones.add(botonAccion("Cambio de turno",       this::dialogoCambioTurno));
        botones.add(botonAccion("Inscribirse a torneo",  this::dialogoInscribirTorneo));

        centro.add(gridStats);
        centro.add(Box.createVerticalStrut(16));
        centro.add(sep);
        centro.add(lblAcciones);
        centro.add(botones);

        JLabel nota = new JLabel("JFrame con JSplitPane · cada botón abre JDialog o cambia panel");
        nota.setFont(new Font("SansSerif", Font.PLAIN, 10));
        nota.setForeground(new Color(180, 180, 200));
        nota.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        nota.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(nota);

        add(centro, BorderLayout.CENTER);
    }

    private void dialogoSugerirPlatillo() {
        Empleado emp = empleadoActual();
        if (emp == null) {
            JOptionPane.showMessageDialog(this, "No se pudo identificar al empleado actual.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField fNombre  = campo("Nombre del platillo");
        JTextField fPrecio  = campo("Precio base");
        String[]   tipos    = {"Bebida", "Pasteleria"};
        JComboBox<String> cbTipo = new JComboBox<>(tipos);
        cbTipo.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 6));
        form.add(etiqueta("Nombre"));      form.add(fNombre);
        form.add(etiqueta("Precio base")); form.add(fPrecio);
        form.add(etiqueta("Tipo"));        form.add(cbTipo);

        int res = JOptionPane.showConfirmDialog(this, form,
                "Sugerir platillo al menú", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String nombre = fNombre.getText().trim();
        String precioStr = fPrecio.getText().trim();
        if (nombre.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double precio;
        try { precio = Double.parseDouble(precioStr); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ProductoMenu producto;
        String tipo = (String) cbTipo.getSelectedItem();
        if ("Bebida".equals(tipo)) {
            // Bebida(nombre, id, alcoholica, caliente, precio, itemId, descripcion)
            String idBebida = "B" + System.currentTimeMillis();
            producto = new Bebida(nombre, idBebida, false, false, precio, idBebida, nombre);
        } else {
            // Pasteleria(nombre, id, precio, categoriaId, descripcion, alergenos)
            String idPast = "P" + System.currentTimeMillis();
            producto = new Pasteleria(nombre, idPast, precio, idPast, nombre, new java.util.ArrayList<>());
        }

        String idEmp = emp.getLogin() + emp.getPassword();
        boolean ok = sistema.sugerirPlatillo(idEmp, producto);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Sugerencia enviada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo enviar la sugerencia.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dialogoCambioTurno() {
        if (sistema.getTurnos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay turnos disponibles.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Empleado emp = empleadoActual();
        if (emp == null) {
            JOptionPane.showMessageDialog(this, "No se pudo identificar al empleado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] dias = sistema.getTurnos().keySet().toArray(new String[0]);
        String diaDestino = (String) JOptionPane.showInputDialog(this,
                "Seleccione el día al que desea cambiarse:",
                "Solicitar cambio de turno", JOptionPane.PLAIN_MESSAGE,
                null, dias, dias[0]);

        if (diaDestino == null) return;

        String idEmp = emp.getLogin() + emp.getPassword();
        boolean ok = sistema.SolicitarCambioTurno(idEmp, diaDestino);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Solicitud enviada. El administrador la revisará.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo enviar la solicitud.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dialogoInscribirTorneo() {
        if (sistema.getTorneos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay torneos disponibles.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Empleado emp = empleadoActual();
        if (emp == null) {
            JOptionPane.showMessageDialog(this, "No se pudo identificar al empleado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] ids = sistema.getTorneos().keySet().toArray(new String[0]);
        String idTorneo = (String) JOptionPane.showInputDialog(this,
                "Seleccione el torneo al que desea inscribirse:",
                "Inscribirse a torneo", JOptionPane.PLAIN_MESSAGE,
                null, ids, ids[0]);

        if (idTorneo == null) return;

        String idEmp = emp.getLogin() + emp.getPassword();
        boolean ok = sistema.inscribirseTorneoEmpleado(idTorneo, idEmp);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Inscripción exitosa.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo inscribir (turno conflictivo o torneo lleno).", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refrescar() {
        long ocupadas = sistema.getMesas().values().stream()
                .filter(m -> m.getClienteActual() != null).count();
        lblMesasOcupadas.setText(ocupadas + " / " + sistema.getMesas().size());

        // Turnos del empleado actual
        Empleado emp = empleadoActual();
        if (emp != null) {
            String idEmp = emp.getLogin() + emp.getPassword();
            try {
                java.util.List<boardGameCafe.logic.Turno> turnos = sistema.verTurnosEmpleado(idEmp);
                if (turnos.isEmpty()) {
                    lblMisTurnos.setText("Sin turnos");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (boardGameCafe.logic.Turno t : turnos) {
                        if (sb.length() > 0) sb.append(", ");
                        sb.append(t.getDiaSemana().substring(0, 3));
                    }
                    lblMisTurnos.setText(sb.toString());
                }
            } catch (Exception ex) {
                lblMisTurnos.setText("—");
            }
        } else {
            lblMisTurnos.setText("—");
        }
    }

    // helpers
    private Empleado empleadoActual() {
        for (Empleado e : sistema.getEmpleados().values()) {
            String idEmp = e.getLogin() + e.getPassword();
            try {
                if (!sistema.verTurnosEmpleado(idEmp).isEmpty() ||
                        sistema.getEmpleados().containsKey(idEmp)) {
                }
            } catch (Exception ignored) {}
        }
        for (Empleado e : sistema.getEmpleados().values()) {
            return e; 
        }
        return null;
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

        lblValor.setFont(new Font("Serif", Font.PLAIN, 26));
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

    private JTextField campo(String placeholder) {
        JTextField f = new JTextField(18);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setToolTipText(placeholder);
        return f;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }
}
