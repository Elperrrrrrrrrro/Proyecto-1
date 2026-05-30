package boardGameCafe.interfaz;

import java.awt.*;
import javax.swing.*;
import boardGameCafe.logic.*;
import boardGameCafe.system.SistemaBoardGameCafe;

public class PanelVisualizaciones extends JPanel {

    private static final long serialVersionUID = 1L;
    private SistemaBoardGameCafe sistema;

    public PanelVisualizaciones(SistemaBoardGameCafe sistema) {
        this.sistema = sistema;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
        construirUI();
    }

    private void construirUI() {
        JLabel titulo = new JLabel("Visualizaciones");
        titulo.setFont(new Font("Serif", Font.PLAIN, 22));
        titulo.setForeground(new Color(30, 30, 60));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        // Intenta usar JFreeChart si no puede hace texto
        boolean jfreeDisponible = false;
        try {
            Class.forName("org.jfree.chart.JFreeChart");
            jfreeDisponible = true;
        } catch (ClassNotFoundException ignored) {}

        if (jfreeDisponible) {
            add(panelConGraficas(), BorderLayout.CENTER);
        } else {
            add(panelFallbackTexto(), BorderLayout.CENTER);
        }
    }

    private JPanel panelConGraficas() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 16, 16));
        panel.setOpaque(false);

        try {
            // Pie Chart Distribución préstamo vs venta 
            Class<?> defaultPieDataset = Class.forName("org.jfree.data.general.DefaultPieDataset");
            Object pieDs = defaultPieDataset.getDeclaredConstructor().newInstance();
            defaultPieDataset.getMethod("setValue", Comparable.class, Number.class)
                .invoke(pieDs, "Préstamo", sistema.getInventario().size());
            defaultPieDataset.getMethod("setValue", Comparable.class, Number.class)
                .invoke(pieDs, "Venta", sistema.getInventarioVender().size());

            Class<?> chartFactory = Class.forName("org.jfree.chart.ChartFactory");
            Object pieChart = chartFactory.getMethod("createPieChart",
                    String.class,
                    Class.forName("org.jfree.data.general.PieDataset"),
                    boolean.class, boolean.class, boolean.class)
                .invoke(null, "Distribución de copias", pieDs, true, true, false);

            Class<?> chartPanel = Class.forName("org.jfree.chart.ChartPanel");
            panel.add((JPanel) chartPanel.getDeclaredConstructor(
                    Class.forName("org.jfree.chart.JFreeChart")).newInstance(pieChart));

            //  Bar Chart ventas por día (últimos 5 días) 
            Class<?> defCatDs = Class.forName("org.jfree.data.category.DefaultCategoryDataset");
            Object barDs = defCatDs.getDeclaredConstructor().newInstance();

            java.time.LocalDate hoy = java.time.LocalDate.now();
            for (int d = 4; d >= 0; d--) {
                java.time.LocalDate dia = hoy.minusDays(d);
                String etiq = dia.getDayOfMonth() + "/" + dia.getMonthValue();
                final java.time.LocalDate diaFinal = dia;

                double comida = sistema.getHistorialVenta().values().stream()
                    .filter(v -> v.getFecha() != null && v.getFecha().toLocalDate().equals(diaFinal))
                    .mapToDouble(v -> v.getSubtotal()).sum();
                double juegos = sistema.getHistorialVenta().values().stream()
                    .filter(v -> v.getFecha() != null && v.getFecha().toLocalDate().equals(diaFinal))
                    .mapToDouble(v -> v.calcularIva() / 0.19).sum();

                defCatDs.getMethod("addValue", double.class, Comparable.class, Comparable.class)
                    .invoke(barDs, comida, "Cafetería", etiq);
                defCatDs.getMethod("addValue", double.class, Comparable.class, Comparable.class)
                    .invoke(barDs, juegos, "Juegos", etiq);
            }

            Object barChart = chartFactory.getMethod("createBarChart",
                    String.class, String.class, String.class,
                    Class.forName("org.jfree.data.category.CategoryDataset"),
                    Class.forName("org.jfree.chart.plot.PlotOrientation"),
                    boolean.class, boolean.class, boolean.class)
                .invoke(null, "Ventas netas (5 días)", "", "$",
                    barDs,
                    Class.forName("org.jfree.chart.plot.PlotOrientation")
                        .getField("VERTICAL").get(null),
                    true, true, false);

            panel.add((JPanel) chartPanel.getDeclaredConstructor(
                    Class.forName("org.jfree.chart.JFreeChart")).newInstance(barChart));

            //  Line Chart reservas (préstamos) por día 
            Object lineDs = defCatDs.getDeclaredConstructor().newInstance();
            for (int d = 6; d >= 0; d--) {
                java.time.LocalDate dia = hoy.minusDays(d);
                String etiq = dia.getDayOfMonth() + "/" + dia.getMonthValue();
                final java.time.LocalDate diaFinal = dia;
                long count = sistema.getHistorialPrestamosClientes().values().stream()
                    .filter(p -> p.getFechaPrestamo() != null &&
                                 p.getFechaPrestamo().toLocalDate().equals(diaFinal))
                    .count();
                defCatDs.getMethod("addValue", double.class, Comparable.class, Comparable.class)
                    .invoke(lineDs, (double) count, "Préstamos", etiq);
            }

            Object lineChart = chartFactory.getMethod("createLineChart",
                    String.class, String.class, String.class,
                    Class.forName("org.jfree.data.category.CategoryDataset"),
                    Class.forName("org.jfree.chart.plot.PlotOrientation"),
                    boolean.class, boolean.class, boolean.class)
                .invoke(null, "Evolución de reservas", "", "Cantidad",
                    lineDs,
                    Class.forName("org.jfree.chart.plot.PlotOrientation")
                        .getField("VERTICAL").get(null),
                    true, true, false);

            panel.add((JPanel) chartPanel.getDeclaredConstructor(
                    Class.forName("org.jfree.chart.JFreeChart")).newInstance(lineChart));
            
            panel.add(new JPanel());

        } catch (Exception ex) {
            panel.removeAll();
            panel.add(panelFallbackTexto());
        }
        return panel;
    }

    // en texto por si la libreria no esta disponible
    private JPanel panelFallbackTexto() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel aviso = new JLabel("⚠  JFreeChart no detectado — mostrando resumen de datos");
        aviso.setFont(new Font("SansSerif", Font.ITALIC, 12));
        aviso.setForeground(new Color(160, 100, 40));
        aviso.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        aviso.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(aviso);

        // Tabla de resumen
        String[][] datos = {
            {"Juegos en préstamo",  String.valueOf(sistema.getInventario().size())},
            {"Juegos en venta",     String.valueOf(sistema.getInventarioVender().size())},
            {"Empleados",           String.valueOf(sistema.getEmpleados().size())},
            {"Clientes",            String.valueOf(sistema.getClientes().size())},
            {"Torneos",             String.valueOf(sistema.getTorneos().size())},
            {"Ventas registradas",  String.valueOf(sistema.getHistorialVenta().size())},
            {"Préstamos clientes",  String.valueOf(sistema.getHistorialPrestamosClientes().size())},
            {"Préstamos empleados", String.valueOf(sistema.getHistorialPrestamosEmpleados().size())},
            {"Sugerencias totales", String.valueOf(sistema.getSugerencias().size())},
            {"Sugerencias pendientes", String.valueOf(sistema.getSugerenciasPendientes().size())},
        };

        String[] cols = {"Indicador", "Valor"};
        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(datos, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setRowHeight(26);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.setGridColor(new Color(230, 230, 245));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setMaximumSize(new Dimension(500, 300));
        p.add(scroll);

        JLabel instruccion = new JLabel("Para activar gráficas: agrega jfreechart-1.x.x.jar al classpath.");
        instruccion.setFont(new Font("SansSerif", Font.ITALIC, 11));
        instruccion.setForeground(new Color(140, 140, 160));
        instruccion.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        instruccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(instruccion);

        return p;
    }
}