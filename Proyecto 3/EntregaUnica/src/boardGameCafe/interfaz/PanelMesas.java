package boardGameCafe.interfaz;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import javax.swing.*;
import boardGameCafe.logic.*;
import boardGameCafe.system.SistemaBoardGameCafe;

public class PanelMesas extends JPanel {

    private static final long serialVersionUID = 1L;
    private SistemaBoardGameCafe sistema;
    private JPanel gridMesas;

    public PanelMesas(SistemaBoardGameCafe sistema) {
        this.sistema = sistema;
        construirUI();
        refrescar();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel titulo = new JLabel("Gestión de mesas");
        titulo.setFont(new Font("Serif", Font.PLAIN, 22));
        titulo.setForeground(new Color(30, 30, 60));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        barra.setOpaque(false);
        barra.setBorder(BorderFactory.createEmptyBorder(12, 0, 14, 0));

        JButton btnAgregar   = boton("+ Agregar mesa");
        JButton btnAsignar   = boton("Asignar cliente");
        JButton btnPlatillo  = boton("Agregar platillo");
        JButton btnLiberar   = boton("Liberar mesa");
        JButton btnRefrescar = boton("↻ Refrescar");

        btnAgregar.addActionListener(e   -> dialogoAgregarMesa());
        btnAsignar.addActionListener(e   -> dialogoAsignarCliente());
        btnPlatillo.addActionListener(e  -> dialogoAgregarPlatillo());
        btnLiberar.addActionListener(e   -> dialogoLiberarMesa());
        btnRefrescar.addActionListener(e -> refrescar());

        barra.add(btnAgregar);
        barra.add(btnAsignar);
        barra.add(btnPlatillo);
        barra.add(btnLiberar);
        barra.add(btnRefrescar);

        gridMesas = new JPanel(new WrapLayout(FlowLayout.LEFT, 16, 16));
        gridMesas.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(gridMesas);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 235), 1));
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(titulo, BorderLayout.NORTH);
        norte.add(barra,  BorderLayout.SOUTH);

        add(norte,  BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    public void refrescar() {
        gridMesas.removeAll();
        for (Map.Entry<String, Mesa> entry : sistema.getMesas().entrySet()) {
            gridMesas.add(tarjetaMesa(entry.getKey(), entry.getValue()));
        }
        if (sistema.getMesas().isEmpty()) {
            JLabel vacio = new JLabel("No hay mesas registradas. Use '+ Agregar mesa'.");
            vacio.setForeground(new Color(160, 160, 180));
            vacio.setFont(new Font("SansSerif", Font.ITALIC, 13));
            gridMesas.add(vacio);
        }
        gridMesas.revalidate();
        gridMesas.repaint();
    }

    private JPanel tarjetaMesa(String idMesa, Mesa mesa) {
        boolean ocupada = mesa.getClienteActual() != null;
        Color fondo     = ocupada ? new Color(255, 245, 245) : new Color(245, 255, 248);
        Color borde     = ocupada ? new Color(220, 160, 160) : new Color(160, 210, 180);

        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setPreferredSize(new Dimension(175, 130));
        card.setBackground(fondo);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borde, 1, true),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)));

        // Número de mesa
        JLabel lblNum = new JLabel("Mesa " + idMesa, SwingConstants.CENTER);
        lblNum.setFont(new Font("Serif", Font.PLAIN, 17));
        lblNum.setForeground(new Color(30, 30, 60));

        // Estado
        String estadoTxt = ocupada
                ? "● Ocupada  — " + mesa.getCantidadPersonas() + " personas"
                : "○ Libre";
        JLabel lblEstado = new JLabel(estadoTxt, SwingConstants.CENTER);
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblEstado.setForeground(ocupada ? new Color(160, 60, 60) : new Color(60, 160, 100));

        // Cliente
        String clienteTxt = ocupada && mesa.getClienteActual() != null
                ? mesa.getClienteActual().getNombre()
                : "—";
        JLabel lblCliente = new JLabel(clienteTxt, SwingConstants.CENTER);
        lblCliente.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblCliente.setForeground(new Color(100, 100, 130));

        // Items en pedido
        int items = mesa.getPedidoActual() != null ? mesa.getPedidoActual().size() : 0;
        JLabel lblItems = new JLabel("Pedido: " + items + " ítem(s)", SwingConstants.CENTER);
        lblItems.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblItems.setForeground(new Color(130, 130, 150));

        JPanel info = new JPanel(new GridLayout(4, 1, 0, 2));
        info.setOpaque(false);
        info.add(lblNum);
        info.add(lblEstado);
        info.add(lblCliente);
        info.add(lblItems);

        card.add(info, BorderLayout.CENTER);
        return card;
    }
    private void dialogoAgregarMesa() {
        JTextField fCapacidad = new JTextField(10);

        JCheckBox  cbMenores  = new JCheckBox("¿Hay menores?");

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 6));
        form.add(new JLabel("Capacidad máxima:")); form.add(fCapacidad);

        form.add(cbMenores); form.add(new JLabel(""));

        int res = JOptionPane.showConfirmDialog(this, form,
                "Agregar mesa", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            int cap      = Integer.parseInt(fCapacidad.getText().trim());

            // Mesa(numero, capacidad, menores, alergenos, infantes)
            String numMesa = String.valueOf(sistema.getMesas().size() + 1);
            Mesa mesa = new Mesa(numMesa, cap, cbMenores.isSelected(), new ArrayList<>(), false);
            sistema.agregarMesa(mesa);
            refrescar();
            JOptionPane.showMessageDialog(this, "Mesa agregada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Los números deben ser válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dialogoAsignarCliente() {
        if (sistema.getClientes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay clientes registrados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (sistema.getMesas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay mesas disponibles.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] mesasLibres = sistema.getMesas().entrySet().stream()
                .filter(e -> e.getValue().getClienteActual() == null)
                .map(Map.Entry::getKey)
                .toArray(String[]::new);

        if (mesasLibres.length == 0) {
            JOptionPane.showMessageDialog(this, "Todas las mesas están ocupadas.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] docClientes = sistema.getClientes().keySet().toArray(new String[0]);

        JComboBox<String> cbMesa     = new JComboBox<>(mesasLibres);
        JComboBox<String> cbCliente  = new JComboBox<>(docClientes);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 6));
        form.add(new JLabel("Mesa libre:"));         form.add(cbMesa);
        form.add(new JLabel("Cliente (documento):")); form.add(cbCliente);

        int res = JOptionPane.showConfirmDialog(this, form,
                "Asignar cliente a mesa", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String idMesa = (String) cbMesa.getSelectedItem();
        String doc    = (String) cbCliente.getSelectedItem();

        Cliente cliente = sistema.getClientes().get(doc);
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        sistema.getMesas().get(idMesa).setClienteActual(cliente);
        refrescar();
        JOptionPane.showMessageDialog(this,
                "Cliente " + cliente.getNombre() + " asignado a Mesa " + idMesa + ".",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void dialogoAgregarPlatillo() {
        if (sistema.getMenu().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El menú está vacío. El administrador debe aprobarlo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] mesasOcupadas = sistema.getMesas().entrySet().stream()
                .filter(e -> e.getValue().getClienteActual() != null)
                .map(Map.Entry::getKey)
                .toArray(String[]::new);

        if (mesasOcupadas.length == 0) {
            JOptionPane.showMessageDialog(this, "No hay mesas ocupadas.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] nombresMenu = sistema.getMenu().keySet().toArray(new String[0]);
        JComboBox<String> cbMesa   = new JComboBox<>(mesasOcupadas);
        JComboBox<String> cbPlato  = new JComboBox<>(nombresMenu);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 6));
        form.add(new JLabel("Mesa:")); form.add(cbMesa);
        form.add(new JLabel("Platillo:")); form.add(cbPlato);

        int res = JOptionPane.showConfirmDialog(this, form,
                "Agregar platillo a mesa", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String idMesa   = (String) cbMesa.getSelectedItem();
        String nombrePlato = (String) cbPlato.getSelectedItem();

        try {
            sistema.agregarplatoaMesa(idMesa, nombrePlato);
            refrescar();
            JOptionPane.showMessageDialog(this, "Platillo agregado al pedido.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo agregar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dialogoLiberarMesa() {
        String[] mesasOcupadas = sistema.getMesas().entrySet().stream()
                .filter(e -> e.getValue().getClienteActual() != null)
                .map(Map.Entry::getKey)
                .toArray(String[]::new);

        if (mesasOcupadas.length == 0) {
            JOptionPane.showMessageDialog(this, "No hay mesas ocupadas.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<String> cbMesa = new JComboBox<>(mesasOcupadas);
        JTextField fPropina = new JTextField("0", 10);
        JCheckBox  cbPuntos = new JCheckBox("Usar puntos de fidelidad");
        JCheckBox  cbCompartido = new JCheckBox("Descuento consumo compartido");

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 6));
        form.add(new JLabel("Mesa a liberar:")); form.add(cbMesa);
        form.add(new JLabel("Propina ($):"));    form.add(fPropina);
        form.add(cbPuntos);                       form.add(cbCompartido);

        int res = JOptionPane.showConfirmDialog(this, form,
                "Liberar mesa y registrar venta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String idMesa = (String) cbMesa.getSelectedItem();
        double propina;
        try { propina = Double.parseDouble(fPropina.getText().trim()); }
        catch (NumberFormatException ex) { propina = 0; }

        Mesa mesa = sistema.getMesas().get(idMesa);
        if (mesa == null || mesa.getClienteActual() == null) {
            JOptionPane.showMessageDialog(this, "Mesa inválida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            sistema.registrarVenta(
                idMesa,
                LocalDateTime.now(),
                mesa.getClienteActual(),
                propina,
                null,
                null,
                cbCompartido.isSelected(),
                cbPuntos.isSelected()
            );
            sistema.limpiarMesa(LocalDateTime.now(), mesa);
            refrescar();
            JOptionPane.showMessageDialog(this, "Venta registrada y mesa liberada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo liberar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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

    static class WrapLayout extends FlowLayout {
        public WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }
        @Override
        public Dimension minimumLayoutSize(Container target) {
            return layoutSize(target, false);
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int width = target.getWidth();
                if (width == 0) width = Integer.MAX_VALUE;
                int rowW = 0, rowH = 0, totalH = 0;
                Insets ins = target.getInsets();
                int maxW = width - ins.left - ins.right;
                for (Component c : target.getComponents()) {
                    Dimension d = preferred ? c.getPreferredSize() : c.getMinimumSize();
                    if (rowW + d.width + getHgap() > maxW && rowW > 0) {
                        totalH += rowH + getVgap();
                        rowW = 0; rowH = 0;
                    }
                    rowW += d.width + getHgap();
                    rowH = Math.max(rowH, d.height);
                }
                totalH += rowH + ins.top + ins.bottom + getVgap() * 2;
                return new Dimension(maxW, totalH);
            }
        }
    }
}
