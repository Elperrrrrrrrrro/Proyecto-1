package boardGameCafe.interfaz;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import boardGameCafe.logic.Venta;
import boardGameCafe.system.SistemaBoardGameCafe;

public class PanelInformes extends JPanel {

    private static final long serialVersionUID = 1L;

    private SistemaBoardGameCafe sistema;

    private JTextField fFechaInicio;
    private JTextField fFechaFin;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalGeneral;

    public PanelInformes(SistemaBoardGameCafe sistema) {
        this.sistema = sistema;
        construirUI();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel titulo = new JLabel("Informe de ventas");
        titulo.setFont(new Font("Serif", Font.PLAIN, 22));
        titulo.setForeground(new Color(30, 30, 60));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        // el formulario de las fechas
        JPanel formFechas = new JPanel(new GridBagLayout());
        formFechas.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 16);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.4;

        fFechaInicio = campoFecha();
        fFechaFin    = campoFecha();

        // Sugerencia de fecha por defecto 
        LocalDate hoy = LocalDate.now();
        fFechaInicio.setText(hoy.withDayOfMonth(1).toString());
        fFechaFin.setText(hoy.toString());

        // Etiquetas
        gbc.gridx=0; gbc.gridy=0;
        formFechas.add(etiqueta("Fecha inicio"), gbc);
        gbc.gridx=1;
        formFechas.add(etiqueta("Fecha fin"), gbc);
        gbc.gridx=2; gbc.weightx=0;
        formFechas.add(new JLabel(""), gbc); // spacer

        // Campos de texto
        gbc.gridy=1; gbc.gridx=0; gbc.weightx=0.4;
        formFechas.add(fFechaInicio, gbc);
        gbc.gridx=1;
        formFechas.add(fFechaFin, gbc);

        JButton btnGenerar = boton("Generar informe");
        btnGenerar.addActionListener(e -> generarInforme());
        gbc.gridx=2; gbc.weightx=0;
        formFechas.add(btnGenerar, gbc);

        JLabel hint = new JLabel("Formato: AAAA-MM-DD");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hint.setForeground(new Color(160, 160, 180));
        gbc.gridy=2; gbc.gridx=0; gbc.gridwidth=3;
        formFechas.add(hint, gbc);

        String[] columnas = {"ID", "Comprador", "Comida", "Juegos", "Impuesto", "Propina", "Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setRowHeight(26);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.getTableHeader().setBackground(new Color(248, 248, 253));
        tabla.setSelectionBackground(new Color(230, 230, 255));
        tabla.setGridColor(new Color(235, 235, 245));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 235), 1));

        lblTotalGeneral = new JLabel("Total del período: —");
        lblTotalGeneral.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTotalGeneral.setForeground(new Color(40, 40, 80));
        lblTotalGeneral.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JLabel notaExport = new JLabel("JTable con datos de historialVenta · botón exportar (futuro)");
        notaExport.setFont(new Font("SansSerif", Font.PLAIN, 11));
        notaExport.setForeground(new Color(160, 160, 180));

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        pie.add(lblTotalGeneral, BorderLayout.WEST);
        pie.add(notaExport,      BorderLayout.EAST);

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(titulo,     BorderLayout.NORTH);
        norte.add(formFechas, BorderLayout.CENTER);
        norte.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        add(norte,  BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(pie,    BorderLayout.SOUTH);
    }

    private void generarInforme() {
        String inicioStr = fFechaInicio.getText().trim();
        String finStr    = fFechaFin.getText().trim();

        LocalDate inicio, fin;
        try {
            inicio = LocalDate.parse(inicioStr);
            fin    = LocalDate.parse(finStr);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use AAAA-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (inicio.isAfter(fin)) {
            JOptionPane.showMessageDialog(this,
                    "La fecha inicio no puede ser posterior a la fecha fin.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modeloTabla.setRowCount(0);
        double totalGeneral = 0;

        for (Venta v : sistema.getHistorialVenta().values()) {
            if (v.getFecha() == null) continue;
            LocalDate fecha = v.getFecha().toLocalDate();
            if (fecha.isBefore(inicio) || fecha.isAfter(fin)) continue;

            // Calcular subtotales
            double comida   = v.getSubtotal();
            double impuesto = v.calcularImpoconsumo() + v.calcularIva();
            double juegosTotal = 0;
            for (boardGameCafe.logic.JuegoMesa j : v.getJuegosVendidos()) {
                juegosTotal += j.getPrecioVenta();
            }
            double total = v.getTotal();
            totalGeneral += total;

            String comprador = v.getComprador() != null ? v.getComprador().getNombre() : "—";

            modeloTabla.addRow(new Object[]{
                v.getIdVenta(),
                comprador,
                String.format("$%,.0f", comida),
                String.format("$%,.0f", juegosTotal),
                String.format("$%,.0f", impuesto),
                String.format("$%,.0f", v.getPropina()),
                String.format("$%,.0f", total)
            });
        }

        lblTotalGeneral.setText(String.format("Total del período: $%,.0f", totalGeneral));

        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron ventas en el rango indicado.", "Informe", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refrescar() {
        // Limpiar tabla al cambiar de panel el usuario debe pulsar generar
        modeloTabla.setRowCount(0);
        lblTotalGeneral.setText("Total del período: —");
    }

    //helpers
    private JTextField campoFecha() {
        JTextField f = new JTextField(14);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 215), 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private JLabel etiqueta(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(new Color(80, 80, 110));
        l.setBorder(BorderFactory.createEmptyBorder(4, 0, 2, 0));
        return l;
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