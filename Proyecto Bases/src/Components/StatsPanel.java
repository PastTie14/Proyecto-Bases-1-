package Components;
 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


//========================================IMPORTANTE===============================================
//DESCARGAR JFREECHART https://sourceforge.net/projects/jfreechart/
//Extraer y en la carpeta de lib hay 2 .jar que agregar como dependencias
//1) jfreechart-1.0.19.jar
//2) jcommon-1.0.23.jar
//Documentacion de jfreechart en su version 1.0.19 https://javadoc.io/doc/org.jfree/jfreechart/1.0.19/index.html
//=================================================================================================

public class StatsPanel extends JPanel {
 
    // ════════════════════════════════════════════════════════════════
    //  PALETA EXTENDIDA PARA EL PIE CHART
    // ════════════════════════════════════════════════════════════════
 
    /** Colores de los sectores del pie, en orden de aparición. */
    private static final Color[] PIE_COLORS = {
        new Color(160,   0, 160),   // morado primario
        new Color( 30, 100, 200),   // azul
        new Color( 34, 139,  34),   // verde
        new Color(220,  50,  50),   // rojo
        new Color(180, 120,   0),   // ámbar
        new Color( 80, 180, 180),   // cian
        new Color(200, 100,  30),   // naranja
        new Color(100,  60, 180),   // violeta
    };
 
    // ════════════════════════════════════════════════════════════════
    //  ESTADO PÚBLICO
    // ════════════════════════════════════════════════════════════════
 
    public int idSelected = -1;
 
    // ════════════════════════════════════════════════════════════════
    //  COMPONENTES
    // ════════════════════════════════════════════════════════════════
 
    private final JTable      tblData      = new JTable();
    private final JScrollPane scrollData   = new JScrollPane(tblData);
    private final JLabel      lblTable     = new JLabel("Datos");
    private final JPanel      chartHolder  = new JPanel(new BorderLayout());
    private final JLabel      lblNoChart   = buildPlaceholder();
    public final JComboBox<String> cmbOptions = new JComboBox<>();
    private final JLabel lblStatus = new JLabel(" ");
 
    // ════════════════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ════════════════════════════════════════════════════════════════
 
    public StatsPanel() {
        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);
 
        add(buildNavbar(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        add(buildStatus(),  BorderLayout.SOUTH);
 
        styleCombo();
        wireTableEvents();
 
        // Placeholder inicial en el área del chart
        chartHolder.setBackground(Format.COLOR_BG);
        chartHolder.add(lblNoChart, BorderLayout.CENTER);
    }
 
    // ════════════════════════════════════════════════════════════════
    //  CONSTRUCCIÓN DE UI
    // ════════════════════════════════════════════════════════════════
 
    /** Barra NORTH: título a la izquierda, ComboBox a la derecha. */
    private JPanel buildNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(Format.COLOR_BG);
        navbar.setPreferredSize(new Dimension(0, 56));
        navbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Format.COLOR_DIVIDER));
 
        JLabel logo = new JLabel("  📊  Estadísticas");
        logo.setFont(Format.FONT_SUBTITLE);
        logo.setForeground(Format.COLOR_PRIMARY);
 
        JPanel comboWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        comboWrapper.setOpaque(false);
 
        JLabel lblCombo = new JLabel("Vista:");
        lblCombo.setFont(Format.FONT_BODY_SMALL);
        lblCombo.setForeground(Format.COLOR_TEXT_SECONDARY);
 
        comboWrapper.add(lblCombo);
        comboWrapper.add(cmbOptions);
 
        navbar.add(logo,         BorderLayout.WEST);
        navbar.add(comboWrapper, BorderLayout.EAST);
        return navbar;
    }
 
    /** Área CENTER: JSplitPane tabla | chart. */
    private JSplitPane buildCenter() {
        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                buildTableSection(),
                buildChartSection()
        );
        split.setResizeWeight(0.45);
        split.setDividerSize(6);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setBackground(Format.COLOR_BG);
        return split;
    }
 
    /** Panel izquierdo: etiqueta + tabla con scroll. */
    private JPanel buildTableSection() {
        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(Format.COLOR_BG);
        area.setBorder(BorderFactory.createEmptyBorder(20, 20, 12, 20));
 
        // ── Estilo de tabla ───────────────────────────────────────
        tblData.setFont(Format.FONT_BODY_SMALL);
        tblData.setForeground(Format.COLOR_TEXT_PRIMARY);
        tblData.setBackground(Format.COLOR_BG);
        tblData.setGridColor(Format.COLOR_DIVIDER);
        tblData.setRowHeight(28);
        tblData.setSelectionBackground(Format.COLOR_PRIMARY_LIGHT);
        tblData.setSelectionForeground(Format.COLOR_TEXT_PRIMARY);
        tblData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblData.getTableHeader().setFont(Format.FONT_BODY_SMALL);
        tblData.getTableHeader().setBackground(Format.COLOR_BG_SURFACE);
        tblData.getTableHeader().setForeground(Format.COLOR_TEXT_SECONDARY);
        tblData.getTableHeader().setBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Format.COLOR_DIVIDER));
 
        scrollData.setBorder(BorderFactory.createLineBorder(Format.COLOR_DIVIDER));
        scrollData.getViewport().setBackground(Format.COLOR_BG);
 
        lblTable.setFont(Format.FONT_SUBTITLE);
        lblTable.setForeground(Format.COLOR_TEXT_PRIMARY);
        lblTable.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
 
        area.add(lblTable,    BorderLayout.NORTH);
        area.add(scrollData,  BorderLayout.CENTER);
        return area;
    }
 
    /** Panel derecho: contenedor del ChartPanel / placeholder. */
    private JPanel buildChartSection() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Format.COLOR_BG);
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 6, 12, 20));
 
        JLabel lblChart = new JLabel("Gráfico de distribución");
        lblChart.setFont(Format.FONT_SUBTITLE);
        lblChart.setForeground(Format.COLOR_TEXT_PRIMARY);
        lblChart.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
 
        wrapper.add(lblChart,    BorderLayout.NORTH);
        wrapper.add(chartHolder, BorderLayout.CENTER);
        return wrapper;
    }
 
    /** Barra SOUTH con el ID seleccionado. */
    private JPanel buildStatus() {
        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(Format.COLOR_BG);
        south.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Format.COLOR_DIVIDER),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        lblStatus.setFont(Format.FONT_BODY_SMALL);
        lblStatus.setForeground(Format.COLOR_TEXT_SECONDARY);
        south.add(lblStatus, BorderLayout.WEST);
        return south;
    }
 
    // ════════════════════════════════════════════════════════════════
    //  MÉTODOS PÚBLICOS
    // ════════════════════════════════════════════════════════════════

    public void fillTable(ArrayList<String> columns,
                          ArrayList<ArrayList<Object>> data) {
        idSelected = -1;
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel model = new DefaultTableModel(columns.toArray(), 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            for (ArrayList<Object> row : data) model.addRow(row.toArray());
            tblData.setModel(model);
            lblTable.setText("Datos (" + data.size() + ")");
            refreshStatus();
        });
    }
 
    public void buildPieChart(String title,
                              ArrayList<String> labels,
                              ArrayList<Number> values) {
        SwingUtilities.invokeLater(() -> {
            DefaultPieDataset dataset = new DefaultPieDataset();
            int count = Math.min(labels.size(), values.size());
            for (int i = 0; i < count; i++) {
                dataset.setValue(labels.get(i), values.get(i));
            }
 
            JFreeChart chart = ChartFactory.createPieChart(
                    title,    // título
                    dataset,  // datos
                    true,     // leyenda
                    true,     // tooltips
                    false     // URLs
            );
 
            chart.setBackgroundPaint(Format.COLOR_BG);
            chart.getTitle().setFont(Format.FONT_SUBTITLE);
            chart.getTitle().setPaint(Format.COLOR_TEXT_PRIMARY);
            if (chart.getLegend() != null) {
                chart.getLegend().setBackgroundPaint(Format.COLOR_BG);
                chart.getLegend().setItemFont(Format.FONT_BODY_SMALL);
            }
 
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setBackgroundPaint(Format.COLOR_BG);
            plot.setOutlineVisible(false);
            plot.setShadowPaint(null);
            plot.setLabelFont(Format.FONT_BODY_SMALL);
            plot.setLabelPaint(Format.COLOR_TEXT_PRIMARY);
            plot.setLabelBackgroundPaint(Format.COLOR_BG_SURFACE);
            plot.setLabelOutlinePaint(Format.COLOR_DIVIDER);
            plot.setLabelShadowPaint(null);
 
            // Aplicar paleta de colores personalizada
            for (int i = 0; i < count; i++) {
                plot.setSectionPaint(labels.get(i),
                        PIE_COLORS[i % PIE_COLORS.length]);
            }
 
            chartHolder.removeAll();
            ChartPanel cp = new ChartPanel(chart);
            cp.setBackground(Format.COLOR_BG);
            cp.setMouseWheelEnabled(true);
            cp.setPopupMenu(null);                 
            chartHolder.add(cp, BorderLayout.CENTER);
            chartHolder.revalidate();
            chartHolder.repaint();
        });
    }
 

    public void setComboOptions(ArrayList<String> options) {
        SwingUtilities.invokeLater(() -> {
            cmbOptions.removeAllItems();
            for (String opt : options) cmbOptions.addItem(opt);
        });
    }
 
    // ════════════════════════════════════════════════════════════════
    //  HELPERS PRIVADOS
    // ════════════════════════════════════════════════════════════════
 
    private void styleCombo() {
        cmbOptions.setFont(Format.FONT_BODY_SMALL);
        cmbOptions.setForeground(Format.COLOR_TEXT_PRIMARY);
        cmbOptions.setBackground(Format.COLOR_BG_SURFACE);
        cmbOptions.setPreferredSize(new Dimension(180, 28));
        cmbOptions.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cmbOptions.setBorder(BorderFactory.createLineBorder(Format.COLOR_DIVIDER, 1, true));
    }
 
    private static JLabel buildPlaceholder() {
        JLabel lbl = new JLabel("Sin datos para graficar", SwingConstants.CENTER);
        lbl.setFont(Format.FONT_BODY);
        lbl.setForeground(Format.COLOR_TEXT_SECONDARY);
        lbl.setBorder(BorderFactory.createDashedBorder(Format.COLOR_DIVIDER, 4, 4));
        return lbl;
    }
 
    private void wireTableEvents() {
        tblData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblData.getSelectedRow();
                if (row == -1) return;
                Object val = tblData.getValueAt(row, 0);
                idSelected = parseId(val);
                refreshStatus();
            }
        });
    }
 
    private void refreshStatus() {
        String txt = idSelected == -1 ? "—" : String.valueOf(idSelected);
        lblStatus.setText("  Seleccionado: " + txt);
    }
 

    private static int parseId(Object val) {
        if (val == null)                               return -1;
        if (val instanceof Integer  i)                 return i;
        if (val instanceof Long     l)                 return l.intValue();
        if (val instanceof java.math.BigDecimal b)     return b.intValue();
        try { return Integer.parseInt(val.toString().trim()); }
        catch (NumberFormatException e)                { return -1; }
    }
}