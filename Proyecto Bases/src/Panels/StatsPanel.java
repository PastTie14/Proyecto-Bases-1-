package Panels;
 
import Components.Format;
import TablesObj.PetType;
import TablesObj.Race;
import TablesObj.Status;
import TablesObj.stats;
 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
 
//========================================IMPORTANTE===============================================
//DESCARGAR JFREECHART https://sourceforge.net/projects/jfreechart/
//Extraer y en la carpeta de lib hay 2 .jar que agregar como dependencias
//1) jfreechart-1.0.19.jar
//2) jcommon-1.0.23.jar
//Documentacion de jfreechart en su version 1.0.19 https://javadoc.io/doc/org.jfree/jfreechart/1.0.19/index.html
//=================================================================================================
 
public class StatsPanel extends JPanel {
 
    private static final Logger LOG = Logger.getLogger(StatsPanel.class.getName());
 
    // ── Nombres de vistas ─────────────────────────────────────────
    private static final String V_PETS_TYPE_STATUS    = "Mascotas por Tipo y Estado";
    private static final String V_DON_ASSOCIATION     = "Donaciones por Asociación";
    private static final String V_DON_CRIB            = "Donaciones por Casa Cuna";
    private static final String V_ADOPTED_VS          = "Adoptadas vs No Adoptadas";
    private static final String V_AGE_RANGE           = "No Adoptadas por Edad";
 
    // ── Paleta pie chart ──────────────────────────────────────────
    private static final Color[] PIE_COLORS = {
        new Color(160,   0, 160),
        new Color( 30, 100, 200),
        new Color( 34, 139,  34),
        new Color(220,  50,  50),
        new Color(180, 120,   0),
        new Color( 80, 180, 180),
        new Color(200, 100,  30),
        new Color(100,  60, 180),
    };
 
    // ── Estado público (id de fila seleccionada, col 0) ───────────
    public int idSelected = -1;
 
    // ── Mapas label → id ─────────────────────────────────────────
    private final LinkedHashMap<String, Integer> petTypeMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> raceMap    = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> statusMap  = new LinkedHashMap<>();
 
    // ── Componentes principales ───────────────────────────────────
    private final JTable       tblData     = new JTable();
    private final JScrollPane  scrollData  = new JScrollPane(tblData);
    private final JLabel       lblTable    = new JLabel("Datos");
    private final JPanel       chartHolder = new JPanel(new BorderLayout());
    private final JLabel       lblNoChart  = buildPlaceholder();
    public  final JComboBox<String> cmbOptions = new JComboBox<>();
    private final JLabel       lblStatus   = new JLabel(" ");
 
    // ── Panel de filtros (CardLayout) ─────────────────────────────
    private final CardLayout filterLayout = new CardLayout();
    private final JPanel     filterCard   = new JPanel(filterLayout);
 
    // Filtros vista 1: Mascotas por tipo/estado
    private final JComboBox<String> cbPetType1  = new JComboBox<>();
    private final JComboBox<String> cbStatus1   = new JComboBox<>();
    private final JTextField        tfStart1    = styledField("YYYY-MM-DD");
    private final JTextField        tfEnd1      = styledField("YYYY-MM-DD");
 
    // Filtros vista 2 y 3: rangos de fecha
    private final JTextField tfStart2 = styledField("YYYY-MM-DD");
    private final JTextField tfEnd2   = styledField("YYYY-MM-DD");
    private final JTextField tfStart3 = styledField("YYYY-MM-DD");
    private final JTextField tfEnd3   = styledField("YYYY-MM-DD");
 
    // Filtros vista 4: adoptados vs no — tipo y raza
    private final JComboBox<String> cbPetType4 = new JComboBox<>();
    private final JComboBox<String> cbRace4    = new JComboBox<>();
 
    // Filtros vista 6: mejores usuarios — rango de fechas
    private final JTextField tfStart6 = styledField("YYYY-MM-DD");
    private final JTextField tfEnd6   = styledField("YYYY-MM-DD");
 
    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────
 
    public StatsPanel() {
        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);
 
        loadCombos();
        buildFilterCards();
        populateViewCombo();
 
        cmbOptions.addActionListener(e -> {
            String sel = (String) cmbOptions.getSelectedItem();
            if (sel != null) filterLayout.show(filterCard, sel);
        });
 
        add(buildNavbar(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        add(buildStatus(),  BorderLayout.SOUTH);
 
        styleCombo();
        wireTableEvents();
 
        chartHolder.setBackground(Format.COLOR_BG);
        chartHolder.add(lblNoChart, BorderLayout.CENTER);
    }
 
    // ─────────────────────────────────────────────────────────────
    //  CARGA DE CATÁLOGOS
    // ─────────────────────────────────────────────────────────────
 
    private void loadCombos() {
        // —— Status ——————————————————————————
        statusMap.put("— Todos —", 0);
        try {
            ResultSet rs = Status.getAll();
            while (rs != null && rs.next())
                statusMap.put(rs.getString(2), rs.getInt(1)); // col2=status_type, col1=id_status
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error cargando status", ex);
        }
        statusMap.keySet().forEach(cbStatus1::addItem);

        // —— Tipo mascota ————————————————————
        petTypeMap.put("— Todos —", 0);
        try {
            ResultSet rs = PetType.getAll();
            while (rs != null && rs.next())
                petTypeMap.put(rs.getString(2), rs.getInt(1));
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "Error cargando petType", ex); }

        // —— Raza ——————————————————————————
        raceMap.put("— Todos —", 0);
        try {
            ResultSet rs = Race.getAll();
            while (rs != null && rs.next())
                raceMap.put(rs.getString(2), rs.getInt(1));
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "Error cargando race", ex); }

        petTypeMap.keySet().forEach(k -> { cbPetType1.addItem(k); cbPetType4.addItem(k); });
        raceMap.keySet().forEach(cbRace4::addItem);
    }
 
    // ─────────────────────────────────────────────────────────────
    //  COMBO DE VISTAS
    // ─────────────────────────────────────────────────────────────
 
    private void populateViewCombo() {
        for (String v : new String[]{
            V_PETS_TYPE_STATUS, V_DON_ASSOCIATION, V_DON_CRIB,
            V_ADOPTED_VS, V_AGE_RANGE
        }) cmbOptions.addItem(v);
    }
 
    // ─────────────────────────────────────────────────────────────
    //  PANELES DE FILTRO POR VISTA
    // ─────────────────────────────────────────────────────────────
 
    private void buildFilterCards() {
        filterCard.setOpaque(false);
 
        // Vista 1 — tipo, estado, rango fechas
        filterCard.add(buildFilterBox(
            filterRow("Tipo mascota", cbPetType1),
            filterRow("Estado",       cbStatus1),
            filterRow("Fecha inicio", tfStart1),
            filterRow("Fecha fin",    tfEnd1),
            buildRunButton()
        ), V_PETS_TYPE_STATUS);
 
        // Vista 2 — donaciones por asociación, rango fechas
        filterCard.add(buildFilterBox(
            filterRow("Fecha inicio", tfStart2),
            filterRow("Fecha fin",    tfEnd2),
            buildRunButton()
        ), V_DON_ASSOCIATION);
 
        // Vista 3 — donaciones por casa cuna, rango fechas
        filterCard.add(buildFilterBox(
            filterRow("Fecha inicio", tfStart3),
            filterRow("Fecha fin",    tfEnd3),
            buildRunButton()
        ), V_DON_CRIB);
 
        // Vista 4 — adoptadas vs no, tipo y raza
        filterCard.add(buildFilterBox(
            filterRow("Tipo mascota", cbPetType4),
            filterRow("Raza",         cbRace4),
            buildRunButton()
        ), V_ADOPTED_VS);
 
        // Vista 5 — por edad, sin filtros
        filterCard.add(buildFilterBox(
            noFilterLabel(),
            buildRunButton()
        ), V_AGE_RANGE);
 
    }
 
    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCCIÓN DE UI
    // ─────────────────────────────────────────────────────────────
 
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
 
    /** JSplitPane: izquierda = filtros + tabla  |  derecha = chart */
    private JSplitPane buildCenter() {
        JSplitPane split = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            buildLeftSection(),
            buildChartSection()
        );
        split.setResizeWeight(0.5);
        split.setDividerSize(6);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setBackground(Format.COLOR_BG);
        return split;
    }
 
    private JPanel buildLeftSection() {
        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(Format.COLOR_BG_SURFACE);
        area.setBorder(BorderFactory.createEmptyBorder(16, 16, 12, 16));
 
        // Filtros arriba
        JLabel lblFilter = new JLabel("Filtros");
        lblFilter.setFont(Format.FONT_BODY_SMALL);
        lblFilter.setForeground(Format.COLOR_TEXT_SECONDARY);
        lblFilter.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
 
        JPanel filterWrapper = new JPanel(new BorderLayout());
        filterWrapper.setOpaque(false);
        filterWrapper.add(lblFilter,  BorderLayout.NORTH);
        filterWrapper.add(filterCard, BorderLayout.CENTER);
 
        // Tabla abajo
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
        lblTable.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
 
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setOpaque(false);
        tableWrapper.add(lblTable,   BorderLayout.NORTH);
        tableWrapper.add(scrollData, BorderLayout.CENTER);
 
        JSplitPane vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, filterWrapper, tableWrapper);
        vSplit.setResizeWeight(0.3);
        vSplit.setDividerSize(5);
        vSplit.setBorder(BorderFactory.createEmptyBorder());
        vSplit.setBackground(Format.COLOR_BG_SURFACE);
 
        area.add(vSplit, BorderLayout.CENTER);
        return area;
    }
 
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
 
    // ─────────────────────────────────────────────────────────────
    //  LÓGICA — ejecutar estadística seleccionada
    // ─────────────────────────────────────────────────────────────
 
    private void runSelected() {
        String view = (String) cmbOptions.getSelectedItem();
        if (view == null) return;
 
        ArrayList<ArrayList<Object>> rows;
        String[] columns;
        // Para el pie chart: col del label y col del valor (índices base-0 en el result)
        int labelCol, valueCol;
 
        switch (view) {
 
            // ── Mascotas por tipo y estado ────────────────────────
            // SQL: pet_type_name | status_type | pet_count
            case V_PETS_TYPE_STATUS:
                rows = stats.getPetsByTypeAndStatus(
                    resolveCombo(petTypeMap, cbPetType1),
                    resolveCombo(statusMap, cbStatus1),
                    tfStart1.getText().trim(),
                    tfEnd1.getText().trim()
                );
                columns  = new String[]{ "Tipo Mascota", "Estado", "Cantidad" };
                labelCol = 0; valueCol = 2;
                break;
 
            // ── Donaciones por asociación ─────────────────────────
            // SQL: association_name | donation_count
            case V_DON_ASSOCIATION:
                rows = stats.getDonationsByAssociation(
                    tfStart2.getText().trim(), tfEnd2.getText().trim());
                columns  = new String[]{ "Asociación", "Donaciones" };
                labelCol = 0; valueCol = 1;
                break;
 
            // ── Donaciones por casa cuna ──────────────────────────
            // SQL: crib_house_name | donation_count
            case V_DON_CRIB:
                rows = stats.getDonationsByCribHouse(
                    tfStart3.getText().trim(), tfEnd3.getText().trim());
                columns  = new String[]{ "Casa Cuna", "Donaciones" };
                labelCol = 0; valueCol = 1;
                break;
 
            // ── Adoptadas vs no adoptadas ─────────────────────────
            // SQL: status_type | pet_type_name | race_name | count
            case V_ADOPTED_VS:
                rows = stats.getAdoptedVSUnadopted(
                    resolveCombo(petTypeMap, cbPetType4),
                    resolveCombo(raceMap,    cbRace4)
                );
                columns  = new String[]{ "Estado", "Tipo Mascota", "Raza", "Cantidad" };
                labelCol = 0; valueCol = 3;
                break;
 
            // ── No adoptadas por rango de edad ────────────────────
            // SQL: age_range | pet_count
            case V_AGE_RANGE:
                rows = stats.getUnadoptedPetsByAgeRange();
                columns  = new String[]{ "Rango Edad", "Cantidad" };
                labelCol = 0; valueCol = 1;
                break;
 
            default:
                return;
        }
 
        if (rows.isEmpty()) {
            clearTable("Sin resultados");
            showNoChart();
            return;
        }
 
        fillTable(columns, rows);
        buildPieChart(view, rows, labelCol, valueCol);
    }
 
    // ─────────────────────────────────────────────────────────────
    //  MÉTODOS PÚBLICOS 
    // ─────────────────────────────────────────────────────────────
 
    public void fillTable(String[] columns, ArrayList<ArrayList<Object>> data) {
        idSelected = -1;
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            for (ArrayList<Object> row : data) model.addRow(row.toArray());
            tblData.setModel(model);
            lblTable.setText("Datos (" + data.size() + ")");
            refreshStatus();
        });
    }
 
    /** Sobrecarga con ArrayList<String> para columnas (mantiene compatibilidad con código previo). */
    public void fillTable(ArrayList<String> columns, ArrayList<ArrayList<Object>> data) {
        fillTable(columns.toArray(new String[0]), data);
    }
 
    public void buildPieChart(String title,
                              ArrayList<String> labels,
                              ArrayList<Number> values) {
        SwingUtilities.invokeLater(() -> {
            DefaultPieDataset dataset = new DefaultPieDataset();
            int count = Math.min(labels.size(), values.size());
            for (int i = 0; i < count; i++) dataset.setValue(labels.get(i), values.get(i));
            renderPie(title, dataset, count, labels);
        });
    }
 
    /** Construye el pie extrayendo label y valor de las propias filas de datos. */
    public void buildPieChart(String title,
                              ArrayList<ArrayList<Object>> rows,
                              int labelCol, int valueCol) {
        SwingUtilities.invokeLater(() -> {
            DefaultPieDataset dataset = new DefaultPieDataset();
            ArrayList<String> usedLabels = new ArrayList<>();
            for (ArrayList<Object> row : rows) {
                String lbl = String.valueOf(row.get(labelCol));
                Number val = toNumber(row.get(valueCol));
                // Si la etiqueta ya existe, acumular (UNION puede repetir labels)
                if (dataset.getIndex(lbl) >= 0) {
                    double prev = dataset.getValue(lbl).doubleValue();
                    dataset.setValue(lbl, prev + val.doubleValue());
                } else {
                    dataset.setValue(lbl, val);
                    usedLabels.add(lbl);
                }
            }
            renderPie(title, dataset, usedLabels.size(), usedLabels);
        });
    }
 
    public void setComboOptions(ArrayList<String> options) {
        SwingUtilities.invokeLater(() -> {
            cmbOptions.removeAllItems();
            for (String opt : options) cmbOptions.addItem(opt);
        });
    }
 
    // ─────────────────────────────────────────────────────────────
    //  RENDER DEL PIE CHART
    // ─────────────────────────────────────────────────────────────
 
    private void renderPie(String title, DefaultPieDataset dataset,
                           int count, java.util.List<String> labels) {
        JFreeChart chart = ChartFactory.createPieChart(title, dataset, true, true, false);
 
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
 
        for (int i = 0; i < count; i++)
            plot.setSectionPaint(labels.get(i), PIE_COLORS[i % PIE_COLORS.length]);
 
        chartHolder.removeAll();
        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(Format.COLOR_BG);
        cp.setMouseWheelEnabled(true);
        cp.setPopupMenu(null);
        chartHolder.add(cp, BorderLayout.CENTER);
        chartHolder.revalidate();
        chartHolder.repaint();
    }
 
    private void showNoChart() {
        SwingUtilities.invokeLater(() -> {
            chartHolder.removeAll();
            chartHolder.add(lblNoChart, BorderLayout.CENTER);
            chartHolder.revalidate();
            chartHolder.repaint();
        });
    }
 
    private void clearTable(String msg) {
        SwingUtilities.invokeLater(() -> {
            tblData.setModel(new DefaultTableModel());
            lblTable.setText(msg);
        });
    }
 
    // ─────────────────────────────────────────────────────────────
    //  HELPERS DE CONSTRUCCIÓN DE UI
    // ─────────────────────────────────────────────────────────────
 
    private JPanel buildFilterBox(JComponent... rows) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(4, 0, 8, 0));
        for (JComponent c : rows) {
            c.setAlignmentX(LEFT_ALIGNMENT);
            p.add(c);
            p.add(Box.createVerticalStrut(6));
        }
        return p;
    }
 
    private JPanel filterRow(String labelText, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
 
        JLabel lbl = new JLabel(labelText + ":");
        lbl.setFont(Format.FONT_BODY_SMALL);
        lbl.setForeground(Format.COLOR_TEXT_SECONDARY);
        lbl.setPreferredSize(new Dimension(110, 24));
 
        if (field instanceof JTextField tf) {
            tf.setFont(Format.FONT_BODY_SMALL);
            tf.setPreferredSize(new Dimension(120, 24));
            tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Format.COLOR_DIVIDER),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
            ));
        } else if (field instanceof JComboBox<?> cb) {
            cb.setFont(Format.FONT_BODY_SMALL);
            cb.setPreferredSize(new Dimension(120, 24));
        }
        row.add(lbl,   BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }
 
    private JButton buildRunButton() {
        JButton btn = new JButton("Generar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                g2.setColor(getModel().isRollover() || getModel().isPressed()
                        ? Format.COLOR_PRIMARY.darker() : Format.COLOR_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Format.RADIUS_BTN, Format.RADIUS_BTN);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(Format.FONT_BODY_SMALL);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        btn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 32));
        btn.addActionListener(e -> runSelected());
        return btn;
    }
 
    private JLabel noFilterLabel() {
        JLabel lbl = new JLabel("Sin filtros disponibles.");
        lbl.setFont(Format.FONT_BODY_SMALL);
        lbl.setForeground(Format.COLOR_TEXT_SECONDARY);
        return lbl;
    }
 
    private static JTextField styledField(String placeholder) {
        JTextField tf = new JTextField();
        tf.putClientProperty("JTextField.placeholderText", placeholder);
        return tf;
    }
 
    private static JLabel buildPlaceholder() {
        JLabel lbl = new JLabel("Sin datos para graficar", SwingConstants.CENTER);
        lbl.setFont(Format.FONT_BODY);
        lbl.setForeground(Format.COLOR_TEXT_SECONDARY);
        lbl.setBorder(BorderFactory.createDashedBorder(Format.COLOR_DIVIDER, 4, 4));
        return lbl;
    }
 
    // ─────────────────────────────────────────────────────────────
    //  HELPERS LÓGICA
    // ─────────────────────────────────────────────────────────────
 
    private void styleCombo() {
        cmbOptions.setFont(Format.FONT_BODY_SMALL);
        cmbOptions.setForeground(Format.COLOR_TEXT_PRIMARY);
        cmbOptions.setBackground(Format.COLOR_BG_SURFACE);
        cmbOptions.setPreferredSize(new Dimension(220, 28));
        cmbOptions.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cmbOptions.setBorder(BorderFactory.createLineBorder(Format.COLOR_DIVIDER, 1, true));
    }
 
    private void wireTableEvents() {
        tblData.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
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
        if (val == null)                           return -1;
        if (val instanceof Integer  i)             return i;
        if (val instanceof Long     l)             return l.intValue();
        if (val instanceof BigDecimal b)           return b.intValue();
        try { return Integer.parseInt(val.toString().trim()); }
        catch (NumberFormatException e)            { return -1; }
    }
 
    private static Number toNumber(Object val) {
        if (val instanceof Number n) return n;
        try { return Double.parseDouble(String.valueOf(val)); }
        catch (NumberFormatException e) { return 0; }
    }
 
    private static int resolveCombo(LinkedHashMap<String, Integer> map, JComboBox<String> cb) {
        Object sel = cb.getSelectedItem();
        if (sel == null) return 0;
        Integer id = map.get(sel.toString());
        return id != null ? id : 0;
    }
}