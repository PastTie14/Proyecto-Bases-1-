package Components;

import TablesObj.*;
import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;

/**
 * PetSearchPanel — híbrido entre PetFormPanel y admWindow.
 *
 * Panel izquierdo : filtros de búsqueda (solo campos principales, sin imagen ni extra info).
 * Panel central   : tabla de resultados con el mismo estilo de admWindow.
 *
 * Stored procedure: getPetFilters(pIdChip, pIdDistrict, pIdCanton, pIdProvince,
 *                                 pIdStatus, pIdPetType, pIdRescuer, pIdRace,
 *                                 pIdColor, petCursor OUT)
 * Pasar 0 en cualquier parámetro = sin filtro (Oracle usa NVL).
 */
public class PetSearchPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(PetSearchPanel.class.getName());

    // ── Mapas label → id ─────────────────────────────────────────
    private final LinkedHashMap<String, Integer> statusMap   = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> petTypeMap  = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> colorMap    = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> provinciaMap= new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> cantonMap   = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> distritoMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> raceMap     = new LinkedHashMap<>();

    // ── Campos de filtro ─────────────────────────────────────────
    private final FormField    chipId      = new FormField("N.º de chip");
    private final FormField    idRescuer   = new FormField("ID Rescatista");
    private final FormComboBox status      = new FormComboBox("Estado");
    private final FormComboBox petType     = new FormComboBox("Tipo de mascota");
    private final FormComboBox color       = new FormComboBox("Color");
    private final FormComboBox race        = new FormComboBox("Raza");
    private final FormComboBox provincia   = new FormComboBox("Provincia");
    private final FormComboBox canton      = new FormComboBox("Cantón");
    private final FormComboBox distrito    = new FormComboBox("Distrito");

    // ── Tabla de resultados ───────────────────────────────────────
    private final JTable     resultsTable  = new JTable();
    private final JScrollPane scrollTabla  = new JScrollPane(resultsTable);
    private final JLabel      lblResultados= new JLabel("Resultados");

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────

    public PetSearchPanel() {
        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);

        loadAllCombos();

        provincia.getCombo().addActionListener(e -> onProvinciaChanged());
        canton.getCombo().addActionListener(e -> onCantonChanged());

        add(buildLeft(),   BorderLayout.WEST);
        add(buildCenter(), BorderLayout.CENTER);
    }

    // ─────────────────────────────────────────────────────────────
    //  PANEL IZQUIERDO — filtros
    // ─────────────────────────────────────────────────────────────

    private JPanel buildLeft() {
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(Format.COLOR_BG_SURFACE);
        left.setPreferredSize(new Dimension(300, 0));
        left.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, Format.COLOR_DIVIDER),
            BorderFactory.createEmptyBorder(24, 20, 24, 20)
        ));

        // ── Título ────────────────────────────────────────────────
        JLabel title = new JLabel("Buscar mascotas");
        title.setFont(Format.FONT_SUBTITLE);
        title.setForeground(Format.COLOR_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);

        // ── Scroll para los filtros ───────────────────────────────
        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setOpaque(false);
        fields.setAlignmentX(LEFT_ALIGNMENT);

        fields.add(sectionLabel("Identificación"));
        fields.add(gap(6));  fields.add(chipId);
        fields.add(gap());   fields.add(idRescuer);
        fields.add(gap(14)); fields.add(sectionLabel("Características"));
        fields.add(gap(6));  fields.add(status);
        fields.add(gap());   fields.add(petType);
        fields.add(gap());   fields.add(race);
        fields.add(gap());   fields.add(color);
        fields.add(gap(14)); fields.add(sectionLabel("Ubicación"));
        fields.add(gap(6));  fields.add(provincia);
        fields.add(gap());   fields.add(canton);
        fields.add(gap());   fields.add(distrito);

        JScrollPane scroll = new JScrollPane(fields);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.getViewport().setBackground(Format.COLOR_BG_SURFACE);
        scroll.setAlignmentX(LEFT_ALIGNMENT);

        // ── Botón buscar ──────────────────────────────────────────
        JButton searchBtn = buildSearchButton();

        left.add(title);
        left.add(Box.createVerticalStrut(16));
        left.add(scroll);
        left.add(Box.createVerticalStrut(12));
        left.add(searchBtn);
        return left;
    }

    private JButton buildSearchButton() {
        JButton btn = new JButton("Buscar") {
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
        btn.setFont(Format.FONT_BODY);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.addActionListener(e -> {
            ArrayList rs = runSearch();
            if (rs != null) populateTable(rs);
            else            clearTable("Sin resultados");
        });
        return btn;
    }

    // ─────────────────────────────────────────────────────────────
    //  PANEL CENTRAL — tabla
    // ─────────────────────────────────────────────────────────────

    private JPanel buildCenter() {
        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(Format.COLOR_BG);
        area.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Estilo de tabla igual a admWindow
        resultsTable.setFont(Format.FONT_BODY_SMALL);
        resultsTable.setForeground(Format.COLOR_TEXT_PRIMARY);
        resultsTable.setBackground(Format.COLOR_BG);
        resultsTable.setGridColor(Format.COLOR_DIVIDER);
        resultsTable.setRowHeight(28);
        resultsTable.setSelectionBackground(Format.COLOR_PRIMARY_LIGHT);
        resultsTable.setSelectionForeground(Format.COLOR_TEXT_PRIMARY);
        resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        resultsTable.getTableHeader().setFont(Format.FONT_BODY_SMALL);
        resultsTable.getTableHeader().setBackground(Format.COLOR_BG_SURFACE);
        resultsTable.getTableHeader().setForeground(Format.COLOR_TEXT_SECONDARY);
        resultsTable.getTableHeader().setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Format.COLOR_DIVIDER));

        scrollTabla.setBorder(BorderFactory.createLineBorder(Format.COLOR_DIVIDER));
        scrollTabla.getViewport().setBackground(Format.COLOR_BG);

        lblResultados.setFont(Format.FONT_SUBTITLE);
        lblResultados.setForeground(Format.COLOR_TEXT_PRIMARY);
        lblResultados.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        area.add(lblResultados, BorderLayout.NORTH);
        area.add(scrollTabla,   BorderLayout.CENTER);
        return area;
    }

    // ─────────────────────────────────────────────────────────────
    //  BÚSQUEDA — llama al stored procedure
    // ─────────────────────────────────────────────────────────────

    private ArrayList runSearch() {
        int pIdChip     = parseIntField(chipId.getValue());
        int pIdRescuer  = parseIntField(idRescuer.getValue());
        int pIdStatus   = resolveId(statusMap,    status.getSelected());
        int pIdPetType  = resolveId(petTypeMap,   petType.getSelected());
        int pIdColor    = resolveId(colorMap,     color.getSelected());
        int pIdRace     = resolveId(raceMap,      race.getSelected());
        int pIdProvince = resolveId(provinciaMap, provincia.getSelected());
        int pIdCanton   = resolveId(cantonMap,    canton.getSelected());
        int pIdDistrict = resolveId(distritoMap,  distrito.getSelected());

        return Pet.runSearch(pIdChip, pIdRescuer, pIdStatus, pIdPetType, pIdColor, pIdRace, pIdProvince, pIdCanton, pIdDistrict);
    }

    /** Pasa NULL al SP si el valor es 0 (sin filtro), o el int si > 0. */


    // ─────────────────────────────────────────────────────────────
    //  TABLA — render del ResultSet
    // ─────────────────────────────────────────────────────────────

    private void populateTable(ArrayList<ArrayList<Object>> filas) {

        ArrayList<String> columnas = new ArrayList();
        columnas.add("ID Chip");
        columnas.add("Distrito");
        columnas.add("Cantón");
        columnas.add("Provincia");
        columnas.add( "Estado");
        columnas.add("Tipo");
        columnas.add("Raza");
        columnas.add("Rescatista");
        columnas.add("Color");
        
        
        List a = new List();

        DefaultTableModel model = new DefaultTableModel(columnas.toArray(), 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (ArrayList<Object> fila : filas) {
            model.addRow(fila.toArray());
        }

        SwingUtilities.invokeLater(() -> {
            resultsTable.setModel(model);
            lblResultados.setText("Resultados (" + filas.size() + ")");
        });
    }

    private void clearTable(String msg) {
        SwingUtilities.invokeLater(() -> {
            resultsTable.setModel(new DefaultTableModel());
            lblResultados.setText(msg);
        });
    }

    // ─────────────────────────────────────────────────────────────
    //  CARGA DE COMBOS
    // ─────────────────────────────────────────────────────────────

    private void loadAllCombos() {
        loadCombo(Status.getAll(),   statusMap,   status,   1, 2);
        loadCombo(PetType.getAll(),  petTypeMap,  petType,  1, 2);
        loadCombo(PetColor.getAll(), colorMap,    color,    1, 2);
        loadCombo(Province.getAll(), provinciaMap,provincia, 1, 2);
        loadCombo(Race.getAll(),     raceMap,     race,     1, 2);
        canton.setOptions("—");
        distrito.setOptions("—");
    }

    private void loadCombo(ResultSet rs, LinkedHashMap<String, Integer> map,
                           FormComboBox combo, int idCol, int labelCol) {
        map.clear();
        ArrayList<String> labels = new ArrayList<>();
        labels.add("—");          // opción vacía = sin filtro
        map.put("—", 0);
        try {
            while (rs != null && rs.next()) {
                int    id    = rs.getInt(idCol);
                String label = rs.getString(labelCol);
                if (label != null) { map.put(label, id); labels.add(label); }
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "Error cargando combo", ex); }
        combo.setOptions(labels);
    }

    // ── Cascadas Provincia → Cantón → Distrito ────────────────────

    private void onProvinciaChanged() {
        String sel = provincia.getSelected();
        if (sel == null || sel.equals("—")) { canton.setOptions("—"); distrito.setOptions("—"); return; }
        Integer idProv = provinciaMap.get(sel);
        if (idProv == null || idProv == 0) return;

        cantonMap.clear(); distritoMap.clear(); distrito.setOptions("—");
        ArrayList<String> labels = new ArrayList<>(); labels.add("—"); cantonMap.put("—", 0);
        try {
            ResultSet rs = Canton.getAll();
            while (rs != null && rs.next()) {
                if (rs.getInt(3) == idProv) {
                    int id = rs.getInt(1); String label = rs.getString(2);
                    if (label != null) { cantonMap.put(label, id); labels.add(label); }
                }
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "Error cargando cantones", ex); }
        canton.setOptions(labels.size() > 1 ? labels : new ArrayList<>(java.util.List.of("—")));
    }

    private void onCantonChanged() {
        String sel = canton.getSelected();
        if (sel == null || sel.equals("—")) { distrito.setOptions("—"); return; }
        Integer idCanton = cantonMap.get(sel);
        if (idCanton == null || idCanton == 0) return;

        distritoMap.clear();
        ArrayList<String> labels = new ArrayList<>(); labels.add("—"); distritoMap.put("—", 0);
        try {
            ResultSet rs = District.getAll();
            while (rs != null && rs.next()) {
                if (rs.getInt(3) == idCanton) {
                    int id = rs.getInt(1); String label = rs.getString(2);
                    if (label != null) { distritoMap.put(label, id); labels.add(label); }
                }
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "Error cargando distritos", ex); }
        distrito.setOptions(labels.size() > 1 ? labels : new ArrayList<>(java.util.List.of("—")));
    }

    // ─────────────────────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────────────────────

    private static int resolveId(LinkedHashMap<String, Integer> map, String sel) {
        if (sel == null) return 0;
        Integer id = map.get(sel); return id != null ? id : 0;
    }

    private static int parseIntField(String val) {
        if (val == null || val.isBlank()) return 0;
        try { return Integer.parseInt(val.trim()); } catch (NumberFormatException e) { return 0; }
    }

    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Format.FONT_BODY_SMALL);
        lbl.setForeground(Format.COLOR_PRIMARY);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Format.COLOR_DIVIDER));
        return lbl;
    }

    private Component gap()      { return Box.createVerticalStrut(Format.GAP_META); }
    private Component gap(int h) { return Box.createVerticalStrut(h); }
}