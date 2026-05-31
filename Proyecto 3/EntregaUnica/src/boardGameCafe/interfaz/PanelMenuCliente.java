package boardGameCafe.interfaz;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import boardGameCafe.logic.*;
import boardGameCafe.system.SistemaBoardGameCafe;

public class PanelMenuCliente extends JPanel {

    private static final long serialVersionUID = 1L;

    private SistemaBoardGameCafe sistema;
    private Cliente              cliente;

    private DefaultListModel<String> modeloBebidas;
    private DefaultListModel<String> modeloPasteleria;
    private DefaultListModel<String> modeloJuegosVenta;

    public PanelMenuCliente(SistemaBoardGameCafe sistema, Cliente cliente) {
        this.sistema = sistema;
        this.cliente = cliente;
        construirUI();
        refrescar();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel titulo = new JLabel("Menú del café");
        titulo.setFont(new Font("Serif", Font.PLAIN, 22));
        titulo.setForeground(new Color(30, 30, 60));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        add(titulo, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabs.setBackground(Color.WHITE);

        modeloBebidas = new DefaultListModel<>();
        tabs.addTab("Bebidas", panelLista(modeloBebidas));

        modeloPasteleria = new DefaultListModel<>();
        tabs.addTab("Pastelería", panelLista(modeloPasteleria));

        modeloJuegosVenta = new DefaultListModel<>();
        tabs.addTab("Juegos (venta)", panelLista(modeloJuegosVenta));

        add(tabs, BorderLayout.CENTER);

        JLabel nota = new JLabel("Para realizar un pedido comuníquese con el mesero asignado a su mesa.");
        nota.setFont(new Font("SansSerif", Font.ITALIC, 11));
        nota.setForeground(new Color(140, 140, 170));
        nota.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        add(nota, BorderLayout.SOUTH);
    }

    private JPanel panelLista(DefaultListModel<String> modelo) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));

        JList<String> lista = new JList<>(modelo);
        lista.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lista.setBackground(new Color(250, 250, 255));
        lista.setCellRenderer(new ItemMenuRenderer());

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(215, 215, 230), 1));

        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    public void refrescar() {
        modeloBebidas.clear();
        modeloPasteleria.clear();
        modeloJuegosVenta.clear();

        if (sistema.getMenu().isEmpty()) {
            modeloBebidas.addElement("  (El menú aún no tiene ítems aprobados)");
            modeloPasteleria.addElement("  (El menú aún no tiene ítems aprobados)");
        } else {
            for (ProductoMenu p : sistema.getMenu().values()) {
                String linea = String.format("  %-28s  $%,.0f", p.getNombre(), p.getPrecioBase());
                if (p instanceof Bebida) {
                    modeloBebidas.addElement(linea);
                } else if (p instanceof Pasteleria) {
                    modeloPasteleria.addElement(linea);
                } else {
                    modeloBebidas.addElement(linea); // fallback
                }
            }
        }

        if (sistema.getInventarioVender().isEmpty()) {
            modeloJuegosVenta.addElement("  (No hay juegos disponibles para compra)");
        } else {
            for (JuegoMesa j : sistema.getInventarioVender().values()) {
                if (!j.isPrestado()) {
                    String linea = String.format("  %-28s  $%,.0f  [%s]",
                            j.getNombre(), j.getPrecioVenta(), j.getCategoria());
                    modeloJuegosVenta.addElement(linea);
                }
            }
        }
    }

    static class ItemMenuRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            lbl.setBorder(BorderFactory.createEmptyBorder(7, 8, 7, 8));
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            if (!isSelected) {
                lbl.setBackground(index % 2 == 0 ? Color.WHITE : new Color(248, 248, 253));
            }
            return lbl;
        }
    }
}
