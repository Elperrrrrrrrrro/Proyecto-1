package boardGameCafe.interfaz;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import boardGameCafe.logic.*;
import boardGameCafe.system.SistemaBoardGameCafe;

public class PanelJuegos extends JPanel {

    private static final long serialVersionUID = 1L;

    private SistemaBoardGameCafe sistema;
    private Cliente              cliente;

    private DefaultTableModel modeloCatalogo;
    private DefaultTableModel modeloPrestamos;

    public PanelJuegos(SistemaBoardGameCafe sistema, Cliente cliente) {
        this.sistema = sistema;
        this.cliente = cliente;
        construirUI();
        refrescar();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel titulo = new JLabel("Catálogo de juegos");
        titulo.setFont(new Font("Serif", Font.PLAIN, 22));
        titulo.setForeground(new Color(30, 30, 60));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        barra.setOpaque(false);
        barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JButton btnSolicitar = boton("Solicitar préstamo");
        JButton btnRefrescar = boton("↻ Refrescar");

        btnSolicitar.addActionListener(e -> dialogoSolicitarPrestamo());
        btnRefrescar.addActionListener(e -> refrescar());

        barra.add(btnSolicitar);
        barra.add(btnRefrescar);

        String[] colsCat = {"ID", "Nombre", "Empresa", "Categoría", "Jugadores", "Disponible"};
        modeloCatalogo = new DefaultTableModel(colsCat, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaCat = new JTable(modeloCatalogo);
        estilizarTabla(tablaCat);

        JScrollPane scrollCat = new JScrollPane(tablaCat);
        scrollCat.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 230), 1),
                "Juegos disponibles para préstamo",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.PLAIN, 11),
                new Color(120, 120, 150)),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        String[] colsPrest = {"ID Préstamo", "Juego", "Fecha inicio", "Estado"};
        modeloPrestamos = new DefaultTableModel(colsPrest, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaPrest = new JTable(modeloPrestamos);
        estilizarTabla(tablaPrest);

        JScrollPane scrollPrest = new JScrollPane(tablaPrest);
        scrollPrest.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 230), 1),
                "Mis préstamos activos",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.PLAIN, 11),
                new Color(120, 120, 150)),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollCat, scrollPrest);
        split.setResizeWeight(0.65);
        split.setDividerSize(6);
        split.setBorder(null);

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(titulo, BorderLayout.NORTH);
        norte.add(barra,  BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }

    private void dialogoSolicitarPrestamo() {
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "No se identificó al cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] idsJuegos = sistema.getInventario().values().stream()
                .filter(j -> !j.isPrestado())
                .map(JuegoMesa::getId)
                .toArray(String[]::new);

        if (idsJuegos.length == 0) {
            JOptionPane.showMessageDialog(this, "No hay juegos disponibles para préstamo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mesas ocupadas (cliente necesita mesa asignada)
        String[] idsMesas = sistema.getMesas().entrySet().stream()
                .filter(e -> e.getValue().getClienteActual() != null)
                .map(Map.Entry::getKey)
                .toArray(String[]::new);

        if (idsMesas.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Necesita tener una mesa asignada para solicitar un préstamo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<String> cbJuego = new JComboBox<>(idsJuegos);
        JComboBox<String> cbMesa  = new JComboBox<>(idsMesas);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 6));
        form.add(new JLabel("Juego:")); form.add(cbJuego);
        form.add(new JLabel("Mesa:"));  form.add(cbMesa);

        int res = JOptionPane.showConfirmDialog(this, form,
                "Solicitar préstamo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String idJuego = (String) cbJuego.getSelectedItem();
        String idMesa  = (String) cbMesa.getSelectedItem();

        boolean ok = sistema.procesarPrestamo(
                idJuego, idMesa, true,
                cliente, null,
                sistema.getInventario().get(idJuego),
                LocalDateTime.now()
        );

        if (ok) {
            JOptionPane.showMessageDialog(this, "Préstamo registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            refrescar();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo procesar el préstamo (mesa incompatible o juego no disponible).",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refrescar() {
        modeloCatalogo.setRowCount(0);
        for (JuegoMesa j : sistema.getInventario().values()) {
            modeloCatalogo.addRow(new Object[]{
                j.getId(),
                j.getNombre(),
                j.getEmpresa(),
                j.getCategoria(),
                j.getMinJugadores() + "–" + j.getMaxJugadores(),
                j.isPrestado() ? "No" : "Sí"
            });
        }

        // Préstamos del cliente
        modeloPrestamos.setRowCount(0);
        if (cliente != null) {
            for (PrestamoCliente p : sistema.getHistorialPrestamosClientes().values()) {
                if (p.getCliente() == null) continue;
                if (!p.getCliente().getDocumentoIdentidad()
                        .equals(cliente.getDocumentoIdentidad())) continue;

                String nombreJuego = p.getJuego() != null ? p.getJuego().getNombre() : "—";
                String fechaInicio = p.getFechaPrestamo() != null
                        ? p.getFechaPrestamo().toLocalDate().toString() : "—";
                String estado = (p.getFechaDevolucion() == null) ? "Activo" : "Devuelto";

                modeloPrestamos.addRow(new Object[]{
                    p.getIdPrestamo(), nombreJuego, fechaInicio, estado
                });
            }
        }
        if (modeloPrestamos.getRowCount() == 0) {
            modeloPrestamos.addRow(new Object[]{"—", "Sin préstamos activos", "—", "—"});
        }
    }

    // helperss

    private void estilizarTabla(JTable tabla) {
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.getTableHeader().setBackground(new Color(248, 248, 253));
        tabla.setSelectionBackground(new Color(230, 230, 255));
        tabla.setGridColor(new Color(235, 235, 245));
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
