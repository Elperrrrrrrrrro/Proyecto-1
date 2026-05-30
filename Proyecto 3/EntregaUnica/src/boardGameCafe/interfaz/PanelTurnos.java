package boardGameCafe.interfaz;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import boardGameCafe.logic.*;
import boardGameCafe.system.SistemaBoardGameCafe;

public class PanelTurnos extends JPanel {

    private static final long serialVersionUID = 1L;

    private SistemaBoardGameCafe sistema;

    private static final String[] DIAS = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
    private static final String[] DIAS_COMPLETOS = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

    // Listas de empleados para cada día (índice = día)
    private DefaultListModel<String>[] modelos;

    private JButton btnSugerencias;
    private JPanel gridTurnos;

    @SuppressWarnings("unchecked")
    public PanelTurnos(SistemaBoardGameCafe sistema) {
        this.sistema = sistema;
        modelos = new DefaultListModel[7];
        for (int i = 0; i < 7; i++) modelos[i] = new DefaultListModel<>();
        construirUI();
        refrescar();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel titulo = new JLabel("Turnos semanales");
        titulo.setFont(new Font("Serif", Font.PLAIN, 22));
        titulo.setForeground(new Color(30, 30, 60));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        barra.setOpaque(false);
        barra.setBorder(BorderFactory.createEmptyBorder(12, 0, 14, 0));

        JButton btnCrear    = boton("Crear turno");
        JButton btnAsignar  = boton("Asignar empleado");
        btnSugerencias      = boton("Ver sugerencias pendientes (0)");

        btnCrear.addActionListener(e   -> dialogoCrearTurno());
        btnAsignar.addActionListener(e -> dialogoAsignarEmpleado());
        btnSugerencias.addActionListener(e -> dialogoSugerencias());

        barra.add(btnCrear);
        barra.add(btnAsignar);
        barra.add(btnSugerencias);

        gridTurnos = new JPanel(new GridLayout(1, 7, 10, 0));
        gridTurnos.setOpaque(false);

        for (int i = 0; i < 7; i++) {
            JPanel col = new JPanel(new BorderLayout(0, 4));
            col.setOpaque(false);

            JLabel lblDia = new JLabel(DIAS[i], SwingConstants.CENTER);
            lblDia.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblDia.setForeground(new Color(100, 100, 140));

            JList<String> lista = new JList<>(modelos[i]);
            lista.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lista.setBackground(new Color(250, 250, 255));
            lista.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 235), 1));

            JScrollPane scroll = new JScrollPane(lista);
            scroll.setPreferredSize(new Dimension(120, 200));
            scroll.setBorder(null);

            col.add(lblDia,  BorderLayout.NORTH);
            col.add(scroll,  BorderLayout.CENTER);
            gridTurnos.add(col);
        }

        JLabel nota = new JLabel("JPanel con GridLayout(1,7) · cada celda = JScrollPane con JList de empleados");
        nota.setFont(new Font("SansSerif", Font.PLAIN, 11));
        nota.setForeground(new Color(160, 160, 180));
        nota.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(titulo, BorderLayout.NORTH);
        norte.add(barra,  BorderLayout.SOUTH);

        add(norte,      BorderLayout.NORTH);
        add(gridTurnos, BorderLayout.CENTER);
        add(nota,       BorderLayout.SOUTH);
    }

    private void dialogoCrearTurno() {
        String[] opciones = DIAS_COMPLETOS;
        String dia = (String) JOptionPane.showInputDialog(this,
                "Seleccione el día para el nuevo turno:",
                "Crear turno", JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        if (dia == null) return;

        if (sistema.getTurnos().containsKey(dia)) {
            JOptionPane.showMessageDialog(this, "Ya existe un turno para ese día.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Turno turno = new Turno(dia);
        sistema.agregarTurno(turno);
        refrescar();
        JOptionPane.showMessageDialog(this, "Turno creado para " + dia + ".", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void dialogoAsignarEmpleado() {
        if (sistema.getTurnos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay turnos creados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (sistema.getEmpleados().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay empleados registrados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] dias     = sistema.getTurnos().keySet().toArray(new String[0]);
        String[] logins   = sistema.getEmpleados().keySet().toArray(new String[0]);

        JComboBox<String> cbDia = new JComboBox<>(dias);
        JComboBox<String> cbEmp = new JComboBox<>(logins);

        JPanel form = new JPanel(new GridLayout(0, 1, 4, 4));
        form.add(new JLabel("Día:")); form.add(cbDia);
        form.add(new JLabel("Empleado (login):")); form.add(cbEmp);

        int res = JOptionPane.showConfirmDialog(this, form,
                "Asignar empleado a turno", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String dia      = (String) cbDia.getSelectedItem();
        String idEmp    = (String) cbEmp.getSelectedItem();
        Turno turno     = sistema.getTurnos().get(dia);
        Empleado emp    = sistema.getEmpleados().get(idEmp);

        if (turno == null || emp == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el turno o empleado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        turno.adicionarEmpleado(emp);
        refrescar();
        JOptionPane.showMessageDialog(this, emp.getNombre() + " asignado al turno del " + dia + ".", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void dialogoSugerencias() {
        java.util.ArrayList<Sugerencia> pendientes = new java.util.ArrayList<>();
        for (Sugerencia s : sistema.getSugerenciasPendientes()) {
            pendientes.add(s);
        }

        if (pendientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay sugerencias pendientes.", "Sugerencias", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] items = new String[pendientes.size()];
        for (int i = 0; i < pendientes.size(); i++) {
            Sugerencia s = pendientes.get(i);
            String tipo = s.isTipoSugerencia() ? "Comida" : "Turno";
            items[i] = (i+1) + ". [" + tipo + "] " + s.getSugerenciaID();
        }

        JList<String> lista = new JList<>(items);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setPreferredSize(new Dimension(360, 180));

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.add(new JLabel("Seleccione una sugerencia y pulse Aprobar:"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        int res = JOptionPane.showConfirmDialog(this, panel,
                "Sugerencias pendientes", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res != JOptionPane.OK_OPTION) return;
        int idx = lista.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una sugerencia.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Sugerencia objetivo = pendientes.get(idx);
        try {
            String idEmp = objetivo.getEmpleado() == null ? ""
                    : objetivo.getEmpleado().getLogin() + objetivo.getEmpleado().getPassword();
            sistema.aprobarCambioTurno(idEmp, objetivo);
            refrescar();
            JOptionPane.showMessageDialog(this, "Sugerencia aprobada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo aprobar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refrescar() {
        // Limpiar todos los modelos
        for (DefaultListModel<String> m : modelos) m.clear();

        // llena cada columna según el día
        Map<String, Turno> turnos = sistema.getTurnos();
        for (int i = 0; i < DIAS_COMPLETOS.length; i++) {
            Turno turno = turnos.get(DIAS_COMPLETOS[i]);
            if (turno != null) {
                for (Empleado emp : turno.getEmpleadosAsignados()) {
                    String[] partes = emp.getNombre().split(" ");
                    String abrev = partes[0] + (partes.length > 1 ? " " + partes[1].charAt(0) + "." : "");
                    modelos[i].addElement(abrev);
                }
            }
        }

        // Actualizar contador de sugerencias
        int pendientes = sistema.getSugerenciasPendientes().size();
        btnSugerencias.setText("Ver sugerencias pendientes (" + pendientes + ")");
    }

    private JButton boton(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBackground(Color.WHITE);
        b.setForeground(new Color(40, 40, 70));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 200), 1, true),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}