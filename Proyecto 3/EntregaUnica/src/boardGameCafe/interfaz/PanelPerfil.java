package boardGameCafe.interfaz;

import java.awt.*;
import javax.swing.*;
import boardGameCafe.logic.Cliente;
import boardGameCafe.system.SistemaBoardGameCafe;

public class PanelPerfil extends JPanel {

    private static final long serialVersionUID = 1L;

    private SistemaBoardGameCafe sistema;
    private Cliente              cliente;

    private JLabel lblNombre;
    private JLabel lblDocumento;
    private JLabel lblPuntos;
    private DefaultListModel<String> modeloFavoritos;

    public PanelPerfil(SistemaBoardGameCafe sistema, Cliente cliente) {
        this.sistema = sistema;
        this.cliente = cliente;
        construirUI();
        refrescar();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel titulo = new JLabel("Mi perfil");
        titulo.setFont(new Font("Serif", Font.PLAIN, 22));
        titulo.setForeground(new Color(30, 30, 60));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new GridBagLayout());
        cuerpo.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(6, 4, 6, 24);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.NORTHWEST;
        gbc.weightx = 0.5;

        lblNombre    = new JLabel("—");
        lblDocumento = new JLabel("—");
        lblPuntos    = new JLabel("—");

        lblNombre.setFont(new Font("Serif", Font.PLAIN, 20));
        lblPuntos.setFont(new Font("Serif", Font.PLAIN, 26));
        lblPuntos.setForeground(new Color(60, 60, 180));

        gbc.gridx = 0; gbc.gridy = 0;
        cuerpo.add(etiqueta("Nombre completo"), gbc);
        gbc.gridy = 1;
        cuerpo.add(lblNombre, gbc);

        gbc.gridy = 2;
        cuerpo.add(etiqueta("Documento de identidad"), gbc);
        gbc.gridy = 3;
        cuerpo.add(lblDocumento, gbc);

        JPanel cardPuntos = new JPanel(new BorderLayout());
        cardPuntos.setBackground(new Color(240, 242, 255));
        cardPuntos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 185, 230), 1, true),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)));
        JLabel descPuntos = new JLabel("Puntos de fidelidad acumulados");
        descPuntos.setFont(new Font("SansSerif", Font.PLAIN, 11));
        descPuntos.setForeground(new Color(100, 100, 150));
        cardPuntos.add(descPuntos, BorderLayout.NORTH);
        cardPuntos.add(lblPuntos,  BorderLayout.CENTER);

        gbc.gridy = 4; gbc.insets = new Insets(16, 4, 6, 24);
        cuerpo.add(cardPuntos, gbc);
        gbc.insets = new Insets(6, 4, 6, 24);

        JPanel panelFav = new JPanel(new BorderLayout(0, 8));
        panelFav.setOpaque(false);

        JLabel lblFavTit = new JLabel("Mis juegos favoritos");
        lblFavTit.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblFavTit.setForeground(new Color(100, 100, 130));

        modeloFavoritos = new DefaultListModel<>();
        JList<String> listaFav = new JList<>(modeloFavoritos);
        listaFav.setFont(new Font("SansSerif", Font.PLAIN, 12));
        listaFav.setBackground(new Color(250, 250, 255));

        JScrollPane scrollFav = new JScrollPane(listaFav);
        scrollFav.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 230), 1));
        scrollFav.setPreferredSize(new Dimension(250, 200));

        panelFav.add(lblFavTit, BorderLayout.NORTH);
        panelFav.add(scrollFav, BorderLayout.CENTER);

        gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 5; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        cuerpo.add(panelFav, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0;
        gbc.insets = new Insets(16, 4, 4, 24);
        JButton btnTorneo = boton("🏆  Inscribirse a torneo");
        btnTorneo.addActionListener(e -> dialogoInscribirTorneo());
        cuerpo.add(btnTorneo, gbc);

        add(cuerpo, BorderLayout.CENTER);
    }

    private void dialogoInscribirTorneo() {
        if (sistema.getTorneos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay torneos disponibles.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "No se identificó al cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] ids = sistema.getTorneos().keySet().toArray(new String[0]);
        String idTorneo = (String) JOptionPane.showInputDialog(this,
                "Seleccione el torneo al que desea inscribirse:",
                "Inscribirse a torneo", JOptionPane.PLAIN_MESSAGE,
                null, ids, ids[0]);
        if (idTorneo == null) return;

        boolean ok = sistema.inscribirseTorneoCliente(idTorneo, cliente.getDocumentoIdentidad());
        if (ok) {
            JOptionPane.showMessageDialog(this, "Inscripción exitosa.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo inscribir (ya inscrito o torneo lleno).", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refrescar() {
        if (cliente == null) {
            lblNombre.setText("Sin sesión activa");
            lblDocumento.setText("—");
            lblPuntos.setText("0");
            modeloFavoritos.clear();
            return;
        }
        lblNombre.setText(cliente.getNombre());
        lblDocumento.setText(cliente.getDocumentoIdentidad());
        lblPuntos.setText(String.format("%.0f pts", cliente.getPuntosFidelidad()));

        modeloFavoritos.clear();
        if (cliente.getJuegosFavoritos() != null) {
            for (boardGameCafe.logic.JuegoMesa j : cliente.getJuegosFavoritos()) {
                modeloFavoritos.addElement(j.getNombre() + " — " + j.getCategoria());
            }
        }
        if (modeloFavoritos.isEmpty()) {
            modeloFavoritos.addElement("(Sin juegos favoritos registrados)");
        }
    }
    
    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        l.setForeground(new Color(130, 130, 160));
        return l;
    }

    private JButton boton(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBackground(Color.WHITE);
        b.setForeground(new Color(40, 40, 70));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 210), 1, true),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
