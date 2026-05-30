package boardGameCafe.interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import boardGameCafe.logic.JuegoMesa;
import boardGameCafe.system.SistemaBoardGameCafe;

public class PanelInventario extends JPanel {

    private static final long serialVersionUID = 1L;

    private SistemaBoardGameCafe sistema;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public PanelInventario(SistemaBoardGameCafe sistema) {
        this.sistema = sistema;
        construirUI();
        refrescar();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel titulo = new JLabel("Inventario de juegos");
        titulo.setFont(new Font("Serif", Font.PLAIN, 22));
        titulo.setForeground(new Color(30, 30, 60));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        barra.setOpaque(false);
        barra.setBorder(BorderFactory.createEmptyBorder(12, 0, 14, 0));

        JButton btnAgregar  = boton("+ Agregar a préstamo");
        JButton btnMover    = boton("Mover a venta");
        JButton btnEstado   = boton("Cambiar estado");

        btnAgregar.addActionListener(e -> dialogoAgregarJuego());
        btnMover.addActionListener(e   -> moverJuegoAVenta());
        btnEstado.addActionListener(e  -> cambiarEstadoJuego());

        barra.add(btnAgregar);
        barra.add(btnMover);
        barra.add(btnEstado);

        String[] columnas = {"ID", "Nombre", "Empresa", "Categoría", "Estado", "Prestado", "Solo Adultos", "Precio Venta"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setRowHeight(26);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.getTableHeader().setBackground(new Color(248, 248, 253));
        tabla.setSelectionBackground(new Color(230, 230, 255));
        tabla.setGridColor(new Color(235, 235, 245));
        tabla.setShowGrid(true);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 235), 1));

        JLabel nota = new JLabel("JTable con DefaultTableModel · botones en JToolBar");
        nota.setFont(new Font("SansSerif", Font.PLAIN, 11));
        nota.setForeground(new Color(160, 160, 180));
        nota.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(titulo, BorderLayout.NORTH);
        norte.add(barra,  BorderLayout.SOUTH);

        add(norte,  BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(nota,   BorderLayout.SOUTH);
    }

    private void dialogoAgregarJuego() {
        JTextField fId         = campo(); JTextField fNombre    = campo();
        JTextField fAno        = campo(); JTextField fEmpresa   = campo();
        JTextField fMin        = campo(); JTextField fMax       = campo();
        JTextField fCategoria  = campo(); JTextField fEstado    = campo();
        JTextField fPrecio     = campo();
        JCheckBox  cbAdultos   = new JCheckBox("Solo adultos");
        JCheckBox  cbDificil   = new JCheckBox("Difícil");

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 6));
        form.add(etiqueta("ID")); form.add(fId);
        form.add(etiqueta("Nombre")); form.add(fNombre);
        form.add(etiqueta("Año publicación")); form.add(fAno);
        form.add(etiqueta("Empresa")); form.add(fEmpresa);
        form.add(etiqueta("Mín. jugadores")); form.add(fMin);
        form.add(etiqueta("Máx. jugadores")); form.add(fMax);
        form.add(etiqueta("Categoría (Cartas/Tablero/Accion)")); form.add(fCategoria);
        form.add(etiqueta("Estado")); form.add(fEstado);
        form.add(etiqueta("Precio venta")); form.add(fPrecio);
        form.add(cbAdultos); form.add(cbDificil);

        int res = JOptionPane.showConfirmDialog(this, form,
                "Agregar juego al inventario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res != JOptionPane.OK_OPTION) return;

        try {
            String id       = fId.getText().trim();
            String nombre   = fNombre.getText().trim();
            int    ano      = Integer.parseInt(fAno.getText().trim());
            String empresa  = fEmpresa.getText().trim();
            int    min      = Integer.parseInt(fMin.getText().trim());
            int    max      = Integer.parseInt(fMax.getText().trim());
            String cat      = fCategoria.getText().trim();
            String estado   = fEstado.getText().trim();
            double precio   = Double.parseDouble(fPrecio.getText().trim());
            boolean adultos = cbAdultos.isSelected();
            boolean dificil = cbDificil.isSelected();

            JuegoMesa juego = new JuegoMesa(id, nombre, ano, empresa, min, max, cat, adultos, estado, dificil, precio);
            sistema.agregarJuegoMesa(juego);
            refrescar();
            JOptionPane.showMessageDialog(this, "Juego agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifica los campos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void moverJuegoAVenta() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un juego de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String idJuego = (String) modeloTabla.getValueAt(fila, 0);
        try {
            sistema.moverJuegoAInventarioVenta(idJuego);
            refrescar();
            JOptionPane.showMessageDialog(this, "Juego movido a inventario de venta.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo mover: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstadoJuego() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un juego de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String idJuego = (String) modeloTabla.getValueAt(fila, 0);
        String estadoActual = (String) modeloTabla.getValueAt(fila, 4);

        String[] opciones = {"Nuevo", "Bueno", "Regular", "Malo", "Dañado"};
        String nuevoEstado = (String) JOptionPane.showInputDialog(this,
                "Estado actual: " + estadoActual + "\nSeleccione nuevo estado:",
                "Cambiar estado", JOptionPane.PLAIN_MESSAGE, null, opciones, estadoActual);

        if (nuevoEstado == null) return;
        try {
            sistema.cambiarEstadoJuego(idJuego, nuevoEstado);
            refrescar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo cambiar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Recarga la tabla con datos del inventario de préstamo */
    public void refrescar() {
        modeloTabla.setRowCount(0);
        for (JuegoMesa j : sistema.getInventario().values()) {
            modeloTabla.addRow(new Object[]{
                j.getId(),
                j.getNombre(),
                j.getEmpresa(),
                j.getCategoria(),
                j.getEstado(),
                j.isPrestado() ? "Sí" : "No",
                j.isSoloAdultos() ? "Sí" : "No",
                String.format("$%,.0f", j.getPrecioVenta())
            });
        }
    }

    // helpers
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

    private JTextField campo() {
        JTextField f = new JTextField(18);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return f;
    }

    private JLabel etiqueta(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }
}