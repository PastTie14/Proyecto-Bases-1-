package Panels;
 
import Components.FormComboBox;
import Components.FormField;
import Components.Format;
import TablesObj.PetType;
import TablesObj.consult;
 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class ConsultPanel extends JPanel {
 
    private static final Logger LOG = Logger.getLogger(ConsultPanel.class.getName());
 
    // ── Constantes de reporte ─────────────────────────────────────
    private static final String R_DONATIONS   = "Donaciones";
    private static final String R_BLACKLIST   = "Lista Negra";
    private static final String R_MATCHES     = "Matches";
    private static final String R_TREATMENTS  = "Tratamientos Necesarios";
    private static final String R_CRIB_HOUSES = "Casas Cuna Compatibles";
    private static final String R_BEST_USERS  = "Mejores Rescatistas / Adoptantes";
 
    private static final String[] REPORT_NAMES = {
        R_DONATIONS, R_BLACKLIST, R_MATCHES,
        R_TREATMENTS, R_CRIB_HOUSES, R_BEST_USERS
    };
 
    // ── Mapa label → id para tipo de mascota ─────────────────────
    private final LinkedHashMap<String, Integer> petTypeMap = new LinkedHashMap<>();
 
    // ── Selector de reporte ───────────────────────────────────────
    private final JComboBox<String> reportCombo = new JComboBox<>(REPORT_NAMES);
 
    // ── CardLayout para los paneles de filtros ────────────────────
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     cardPanel  = new JPanel(cardLayout);
 
    // ── Filtros: Donaciones ───────────────────────────────────────
    // SQL: getDonations(startDate, endDate, idDonor, idAssociation)
    private final FormField donStartDate     = new FormField("Fecha inicio (YYYY-MM-DD)");
    private final FormField donEndDate       = new FormField("Fecha fin   (YYYY-MM-DD)");
    private final FormField donIdDonor       = new FormField("ID Donante  (opcional)");
    private final FormField donIdAssociation = new FormField("ID Asociación (opcional)");
 
    // ── Filtros: Matches ──────────────────────────────────────────
    // SQL: getMatches(idLostPet, idFoundPet)
    private final FormField matchIdLostPet  = new FormField("ID Mascota Perdida");
    private final FormField matchIdFoundPet = new FormField("ID Mascota Encontrada");
 
    // ── Filtros: Tratamientos ─────────────────────────────────────
    // SQL: getPetNecessaryTreatments(minTreatments, maxTreatments)
    private final FormField treatMin = new FormField("Mín. tratamientos");
    private final FormField treatMax = new FormField("Máx. tratamientos");
 
    // ── Filtros: Casas Cuna ───────────────────────────────────────
    // SQL: getCompatibleCribHouses(idPetType)
    private final FormComboBox cribPetType = new FormComboBox("Tipo de mascota");
 
    // ── Filtros: Mejores Rescatistas / Adoptantes ─────────────────
    // SQL: getBestRescuersAndAdopters(startDate, endDate)
    private final FormField bestStartDate = new FormField("Fecha inicio (YYYY-MM-DD)");
    private final FormField bestEndDate   = new FormField("Fecha fin   (YYYY-MM-DD)");
 
    // ── Tabla de resultados ───────────────────────────────────────
    private final JTable      resultsTable = new JTable();
    private final JScrollPane scrollTabla  = new JScrollPane(resultsTable);
    private final JLabel      lblResults   = new JLabel("Resultados");
 
    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────
 
    public ConsultPanel() {
        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);
 
        loadCombos();
        buildCardPanel();
 
        reportCombo.addActionListener(e -> {
            String sel = (String) reportCombo.getSelectedItem();
            if (sel != null) cardLayout.show(cardPanel, sel);
        });
 
        add(buildLeft(),   BorderLayout.WEST);
        add(buildCenter(), BorderLayout.CENTER);
    }
 
    // ─────────────────────────────────────────────────────────────
    //  CARGA DE COMBOS
    // ─────────────────────────────────────────────────────────────
 
    private void loadCombos() {
        petTypeMap.put("— Todos —", 0);
        try {
            ResultSet rs = PetType.getAll();
            while (rs != null && rs.next())
                petTypeMap.put(rs.getString(2), rs.getInt(1));
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "Error cargando petType", ex); }
 
        cribPetType.setOptions(new ArrayList<>(petTypeMap.keySet()));
    }
 
    // ─────────────────────────────────────────────────────────────
    //  PANELES DE FILTRO
    // ─────────────────────────────────────────────────────────────
 
    private void buildCardPanel() {
        cardPanel.setOpaque(false);
 
        // 1. Donaciones — rango fechas + IDs opcionales
        cardPanel.add(filterCard(
            sectionLabel("Rango de fechas"),
            donStartDate, donEndDate,
            sectionLabel("Participantes (opcionales)"),
            donIdDonor, donIdAssociation
        ), R_DONATIONS);
 
        // 2. Lista Negra — sin filtros
        cardPanel.add(
            emptyCard("Sin filtros — se muestran todos los registros."),
            R_BLACKLIST
        );
 
        // 3. Matches — ID mascota perdida e ID mascota encontrada
        cardPanel.add(filterCard(
            sectionLabel("Mascotas a comparar"),
            matchIdLostPet,
            matchIdFoundPet
        ), R_MATCHES);
 
        // 4. Tratamientos — rango de cantidad de tratamientos
        cardPanel.add(filterCard(
            sectionLabel("Rango de tratamientos"),
            treatMin, treatMax
        ), R_TREATMENTS);
 
        // 5. Casas Cuna — tipo de mascota
        cardPanel.add(filterCard(
            sectionLabel("Tipo de mascota"),
            cribPetType
        ), R_CRIB_HOUSES);
 
        // 6. Mejores Rescatistas / Adoptantes — rango de fechas
        cardPanel.add(filterCard(
            sectionLabel("Rango de fechas"),
            bestStartDate, bestEndDate
        ), R_BEST_USERS);
    }
 
    private JPanel filterCard(JComponent... components) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        for (JComponent c : components) {
            c.setAlignmentX(LEFT_ALIGNMENT);
            p.add(c);
            p.add(Box.createVerticalStrut(Format.GAP_META));
        }
        return p;
    }
 
    private JPanel emptyCard(String msg) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        p.setOpaque(false);
        JLabel lbl = new JLabel(msg);
        lbl.setFont(Format.FONT_BODY_SMALL);
        lbl.setForeground(Format.COLOR_TEXT_SECONDARY);
        p.add(lbl);
        return p;
    }
 
    // ─────────────────────────────────────────────────────────────
    //  PANEL IZQUIERDO
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
 
        JLabel title = new JLabel("Consultas");
        title.setFont(Format.FONT_SUBTITLE);
        title.setForeground(Format.COLOR_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);
 
        JLabel reportLabel = new JLabel("Tipo de reporte");
        reportLabel.setFont(Format.FONT_BODY_SMALL);
        reportLabel.setForeground(Format.COLOR_TEXT_SECONDARY);
        reportLabel.setAlignmentX(LEFT_ALIGNMENT);
 
        reportCombo.setFont(Format.FONT_BODY);
        reportCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        reportCombo.setAlignmentX(LEFT_ALIGNMENT);
 
        JLabel filterLabel = sectionLabel("Filtros");
        cardPanel.setAlignmentX(LEFT_ALIGNMENT);
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
 
        JScrollPane filterScroll = new JScrollPane(cardPanel);
        filterScroll.setBorder(BorderFactory.createEmptyBorder());
        filterScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        filterScroll.getVerticalScrollBar().setUnitIncrement(12);
        filterScroll.getViewport().setBackground(Format.COLOR_BG_SURFACE);
        filterScroll.setAlignmentX(LEFT_ALIGNMENT);
 
        JButton searchBtn = buildSearchButton();
 
        left.add(title);
        left.add(Box.createVerticalStrut(16));
        left.add(reportLabel);
        left.add(Box.createVerticalStrut(6));
        left.add(reportCombo);
        left.add(Box.createVerticalStrut(16));
        left.add(filterLabel);
        left.add(Box.createVerticalStrut(10));
        left.add(filterScroll);
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
        btn.addActionListener(e -> runSelectedConsult());
        return btn;
    }
 
    // ─────────────────────────────────────────────────────────────
    //  PANEL CENTRAL — tabla de resultados
    // ─────────────────────────────────────────────────────────────
 
    private JPanel buildCenter() {
        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(Format.COLOR_BG);
        area.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
        resultsTable.setFont(Format.FONT_BODY_SMALL);
        resultsTable.setForeground(Format.COLOR_TEXT_PRIMARY);
        resultsTable.setBackground(Format.COLOR_BG);
        resultsTable.setGridColor(Format.COLOR_DIVIDER);
        resultsTable.setRowHeight(28);
        resultsTable.setSelectionBackground(Format.COLOR_PRIMARY_LIGHT);
        resultsTable.setSelectionForeground(Format.COLOR_TEXT_PRIMARY);
        resultsTable.setAutoCreateRowSorter(true);
 
        resultsTable.getTableHeader().setFont(Format.FONT_BODY_SMALL);
        resultsTable.getTableHeader().setBackground(Format.COLOR_BG_SURFACE);
        resultsTable.getTableHeader().setForeground(Format.COLOR_TEXT_SECONDARY);
        resultsTable.getTableHeader().setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Format.COLOR_DIVIDER));
 
        scrollTabla.setBorder(BorderFactory.createLineBorder(Format.COLOR_DIVIDER));
        scrollTabla.getViewport().setBackground(Format.COLOR_BG);
 
        lblResults.setFont(Format.FONT_SUBTITLE);
        lblResults.setForeground(Format.COLOR_TEXT_PRIMARY);
        lblResults.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
 
        area.add(lblResults, BorderLayout.NORTH);
        area.add(scrollTabla, BorderLayout.CENTER);
        return area;
    }
 
    // ─────────────────────────────────────────────────────────────
    //  LÓGICA DE CONSULTA
    // ─────────────────────────────────────────────────────────────
 
    private void runSelectedConsult() {
        String report = (String) reportCombo.getSelectedItem();
        if (report == null) return;
 
        ArrayList<ArrayList<Object>> rows;
        String[] columns;
 
        switch (report) {
 
            // SQL devuelve: amount | id_donnor | createdAt | association_name | total_records
            case R_DONATIONS:
                rows = consult.getDonations(
                    donStartDate.getValue(),
                    donEndDate.getValue(),
                    parseIntField(donIdDonor.getValue()),
                    parseIntField(donIdAssociation.getValue())
                );
                columns = new String[]{
                    "Monto", "Moneda", "Email Donante", "Fecha",
                    "Asociación", "Total Donaciones", "Monto Total"
                };
                break;
 
            // SQL devuelve: id_user | email | first_name | second_name |
            //              first_surname | second_surname | score | reason | total_records
            case R_BLACKLIST:
                rows = consult.getBlackListReport();
                columns = new String[]{
                    "ID Usuario", "Email",
                    "Primer nombre", "Segundo nombre", 
                    "Primer apellido", "Segundo apellido",
                    "Calificación", "Razón", "Total Registros"
                };
                break;
 
            // SQL devuelve: similarity_pct | total_records
            case R_MATCHES:
                rows = consult.getMatches(
                    parseIntField(matchIdLostPet.getValue()),
                    parseIntField(matchIdFoundPet.getValue())
                );
                columns = new String[]{ "% Similitud", "Total Registros" };
                break;
 
            // id_pet | first_name | pet_type | status_type | disease_count | treatment_count | total_records
            case R_TREATMENTS:
                rows = consult.getPetNecessaryTreatments(
                    parseIntField(treatMin.getValue()),
                    parseIntField(treatMax.getValue())
                );
                columns = new String[]{
                    "ID Mascota", "Nombre", "Tipo",
                    "Estado actual", "Nº Enfermedades",
                    "Nº Tratamientos", "Total Registros"
                };
                break;
 
            // SQL devuelve: id_user | name | email | requires_donations |
            //              pet_type_name | size_name | total_records
            case R_CRIB_HOUSES:
                rows = consult.getCompatibleCribHouses(
                    resolveId(petTypeMap, cribPetType.getSelected())
                );
                columns = new String[]{
                    "ID Casa Cuna", "Nombre", "Email",
                    "Requiere Donaciones", "Mascota Aceptada",
                    "Tamaño Aceptado", "Total Registros"
                };
                break;
 
            // SQL devuelve: id_user | email | first_name | second_name |
            //              first_surname | second_surname | rescues | adoptions | total_registers
            case R_BEST_USERS:
                rows = consult.getBestRescuersAndAdopters(
                    bestStartDate.getValue(),
                    bestEndDate.getValue()
                );
                columns = new String[]{
                    "ID Usuario", "Email",
                    "Primer nombre", "Segundo nombre", 
                    "Primer apellido", "Segundo apellido", 
                    "Rescates", "Adopciones", "Total Registros"
                };
                break;
 
            default:
                return;
        }
 
        if (rows.isEmpty()) clearTable("Sin resultados");
        else                populateTable(rows, columns);
    }
 
    // ─────────────────────────────────────────────────────────────
    //  TABLA — render
    // ─────────────────────────────────────────────────────────────
 
    private void populateTable(ArrayList<ArrayList<Object>> rows, String[] columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (ArrayList<Object> row : rows) model.addRow(row.toArray());
        SwingUtilities.invokeLater(() -> {
            resultsTable.setModel(model);
            lblResults.setText("Resultados (" + rows.size() + ")");
        });
    }
 
    private void clearTable(String msg) {
        SwingUtilities.invokeLater(() -> {
            resultsTable.setModel(new DefaultTableModel());
            lblResults.setText(msg);
        });
    }
 
    // ─────────────────────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────────────────────
 
    private static int parseIntField(String val) {
        if (val == null || val.isBlank()) return 0;
        try { return Integer.parseInt(val.trim()); }
        catch (NumberFormatException e) { return 0; }
    }
 
    private static int resolveId(LinkedHashMap<String, Integer> map, String sel) {
        if (sel == null) return 0;
        Integer id = map.get(sel);
        return id != null ? id : 0;
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
}