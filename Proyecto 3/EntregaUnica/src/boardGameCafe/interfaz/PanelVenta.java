package boardGameCafe.interfaz;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import javax.swing.*;
import boardGameCafe.logic.*;
import boardGameCafe.system.SistemaBoardGameCafe;

public class PanelVenta extends JPanel {

    private static final long serialVersionUID = 1L;
    private SistemaBoardGameCafe sistema;

    private JComboBox<String> cbTipoComprador;
    private JTextField        fIdComprador;
    private JTextField        fPropina;
    private JCheckBox         cbCompartido;
    private JCheckBox         cbPuntos;
    private DefaultListModel<String> modeloJuegos;
    private JList<String>            listaJuegos;

    private javax.swing.table.DefaultTableModel modeloTabla;
    private JLabel lblTotalDia;

    public PanelVenta(SistemaBoardGameCafe sistema) {
        this.sistema = sistema;
        construirUI();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel titulo = new JLabel("Registrar venta");
        titulo.setFont(new Font("Serif", Font.PLAIN, 22));
        titulo.setForeground(new Color(30, 30, 60));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);
        formulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 235), 1, true),
            BorderFactory.createEmptyBorder(18, 20, 18, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets   = new Insets(4, 4, 4, 8);
        gbc.fill     = GridBagConstraints.HORIZONTAL;
        gbc.weightx  = 1.0;
        gbc.gridwidth = 2;

        // Tipo de comprador
        gbc.gridx = 0; gbc.gridy = 0;
        formulario.add(etiqueta("Tipo de comprador"), gbc);
        cbTipoComprador = new JComboBox<>(new String[]{"Cliente (documento)", "Empleado (login+password)"});
        cbTipoComprador.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridy = 1;
        formulario.add(cbTipoComprador, gbc);

        // id comprador
        gbc.gridy = 2;
        formulario.add(etiqueta("ID / Documento del comprador"), gbc);
        fIdComprador = campo("Ej: 12345678 ó loginpassword");
        gbc.gridy = 3;
        formulario.add(fIdComprador, gbc);

        // Propina
        gbc.gridy = 4;
        formulario.add(etiqueta("Propina ($)"), gbc);
        fPropina = campo("0");
        gbc.gridy = 5;
        formulario.add(fPropina, gbc);

        // Opciones
        cbCompartido = new JCheckBox("Descuento consumo compartido (10%)");
        cbPuntos     = new JCheckBox("Usar puntos de fidelidad");
        cbCompartido.setOpaque(false);
        cbPuntos.setOpaque(false);
        cbCompartido.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cbPuntos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 6; formulario.add(cbCompartido, gbc);
        gbc.gridy = 7; formulario.add(cbPuntos, gbc);

        // Lista de juegos disponibles para venta
        gbc.gridy = 8;
        formulario.add(etiqueta("Juegos a comprar (inventario de venta)"), gbc);

        modeloJuegos = new DefaultListModel<>();
        listaJuegos  = new JList<>(modeloJuegos);
        listaJuegos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaJuegos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JScrollPane scrollJuegos = new JScrollPane(listaJuegos);
        scrollJuegos.setPreferredSize(new Dimension(280, 100));
        scrollJuegos.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 225), 1));
        gbc.gridy = 9;
        formulario.add(scrollJuegos, gbc);

        // Botones
        JButton btnRegistrar = boton("✔ Registrar venta");
        JButton btnLimpiar   = boton("✕ Limpiar");
        btnRegistrar.addActionListener(e -> registrarVenta());
        btnLimpiar.addActionListener(e   -> limpiarFormulario());
        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 0));
        botones.setOpaque(false);
        botones.add(btnRegistrar);
        botones.add(btnLimpiar);
        gbc.gridy = 10; gbc.insets = new Insets(12, 4, 4, 8);
        formulario.add(botones, gbc);

        JPanel historial = new JPanel(new BorderLayout(0, 8));
        historial.setOpaque(false);

        JLabel lblHist = new JLabel("Ventas de hoy");
        lblHist.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblHist.setForeground(new Color(100, 100, 130));

        String[] cols = {"ID", "Comprador", "Total"};
        modeloTabla = new javax.swing.table.DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.getTableHeader().setBackground(new Color(248, 248, 253));
        tabla.setGridColor(new Color(235, 235, 245));

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 235), 1));

        lblTotalDia = new JLabel("Total hoy: —");
        lblTotalDia.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTotalDia.setForeground(new Color(40, 40, 80));

        historial.add(lblHist,      BorderLayout.NORTH);
        historial.add(scrollTabla,  BorderLayout.CENTER);
        historial.add(lblTotalDia,  BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formulario, historial);
        split.setDividerLocation(360);
        split.setDividerSize(8);
        split.setBorder(null);
        split.setOpaque(false);

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(titulo, BorderLayout.NORTH);

        add(norte,  BorderLayout.NORTH);
        add(split,  BorderLayout.CENTER);
    }

    private void registrarVenta() {
        String idComprador = fIdComprador.getText().trim();
        if (idComprador.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID del comprador.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double propina;
        try { propina = Double.parseDouble(fPropina.getText().trim()); }
        catch (NumberFormatException ex) { propina = 0; }

        String tipo = (String) cbTipoComprador.getSelectedItem();
        Usuario comprador = null;

        if (tipo != null && tipo.startsWith("Cliente")) {
            comprador = sistema.getClientes().get(idComprador);
        } else {
            comprador = sistema.getEmpleados().get(idComprador);
        }

        if (comprador == null) {
            JOptionPane.showMessageDialog(this, "Comprador no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<JuegoMesa> juegosComprados = new ArrayList<>();
        for (int idx : listaJuegos.getSelectedIndices()) {
            String itemStr = modeloJuegos.getElementAt(idx);
            String idJuego = itemStr.split(" — ")[0].trim();
            JuegoMesa j = sistema.getInventarioVender().get(idJuego);
            if (j != null) juegosComprados.add(j);
        }

        // La venta de empleado no usa mesa (null)
        String idMesa = null;
        if (comprador instanceof Cliente) {
            // Buscar si el cliente tiene mesa asignada
            for (Map.Entry<String, Mesa> entry : sistema.getMesas().entrySet()) {
                Mesa m = entry.getValue();
                if (m.getClienteActual() != null &&
                    m.getClienteActual().getDocumentoIdentidad().equals(idComprador)) {
                    idMesa = entry.getKey();
                    break;
                }
            }
        }

        try {
            sistema.registrarVenta(
                idMesa,
                LocalDateTime.now(),
                comprador,
                propina,
                null,
                juegosComprados,
                cbCompartido.isSelected(),
                cbPuntos.isSelected()
            );
            JOptionPane.showMessageDialog(this, "Venta registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            refrescar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        fIdComprador.setText("");
        fPropina.setText("0");
        cbCompartido.setSelected(false);
        cbPuntos.setSelected(false);
        listaJuegos.clearSelection();
    }

    public void refrescar() {
        modeloJuegos.clear();
        for (JuegoMesa j : sistema.getInventarioVender().values()) {
            if (!j.isPrestado()) {
                modeloJuegos.addElement(j.getId() + " — " + j.getNombre()
                        + "  ($" + String.format("%,.0f", j.getPrecioVenta()) + ")");
            }
        }
        modeloTabla.setRowCount(0);
        double totalHoy = 0;
        java.time.LocalDate hoy = java.time.LocalDate.now();
        for (Venta v : sistema.getHistorialVenta().values()) {
            if (v.getFecha() == null) continue;
            if (!v.getFecha().toLocalDate().equals(hoy)) continue;
            String comp = v.getComprador() != null ? v.getComprador().getNombre() : "—";
            modeloTabla.addRow(new Object[]{
                v.getIdVenta(), comp,
                String.format("$%,.0f", v.getTotal())
            });
            totalHoy += v.getTotal();
        }
        lblTotalDia.setText(String.format("Total hoy: $%,.0f", totalHoy));
    }
    
    private JTextField campo(String placeholder) {
        JTextField f = new JTextField(22);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setToolTipText(placeholder);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 215), 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(new Color(80, 80, 110));
        l.setBorder(BorderFactory.createEmptyBorder(6, 0, 2, 0));
        return l;
    }

    private JButton boton(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBackground(Color.WHITE);
        b.setForeground(new Color(40, 40, 70));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 200), 1, true),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
