package SYSADM;
 
import Components.Format;
import TablesObj.*;
 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 * Ventana de administración de catálogos.
 * Permite consultar, insertar, modificar y eliminar registros
 * de las tablas de catálogo mediante los objetos del modelo.
 *
 * Tablas soportadas y sus columnas editables:
 *   Currency      → name, acronym
 *   Province      → name
 *   Canton        → name, id_province
 *   District      → name, id_canton
 *   PetType       → name
 *   Race          → name, id_pet_type
 *   Status        → status_type
 *   Color         → name
 *   TrainingEase  → name
 *   EnergyLevel   → name
 *   CurrentStatus → status_type
 *   Association   → name
 */
public class admWindow extends JFrame {
 
    private static final Logger LOG = Logger.getLogger(admWindow.class.getName());
 
    // ── Estado ────────────────────────────────────────────────────
    private String selectedCB = null;
    private int    selectedId = -1;
 
    // ── Campos dinámicos ──────────────────────────────────────────
    private final List<JTextField> camposDinamicos = new ArrayList<>();
 
    // ── Componentes principales ───────────────────────────────────
    private final JComboBox<String> jComboBox1  = new JComboBox<>();
    private final JTable            tblTuplas   = new JTable();
    private final JScrollPane       scrollTabla = new JScrollPane(tblTuplas);
    private final JPanel            panelCampos = new JPanel();
    private final JButton BtnAdd    = buildActionButton("Añadir");
    private final JButton BtnUpdate = buildActionButton("Modificar");
    private final JButton BtnDel    = buildActionButton("Eliminar");
 
    // ─────────────────────────────────────────────────────────────
    public admWindow() {
        super("Administración de Catálogos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 620);
        setLocationRelativeTo(null);
        setBackground(Format.COLOR_BG);
        setContentPane(buildContent());
        wireEvents();
        construirComboBox();
        setVisible(true);
    }
 
    // ═════════════════════════════════════════════════════════════
    //  CONSTRUCCIÓN DE UI
    // ═════════════════════════════════════════════════════════════
 
    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Format.COLOR_BG);
        root.add(buildNavbar(),    BorderLayout.NORTH);
        root.add(buildLeft(),      BorderLayout.WEST);
        root.add(buildTableArea(), BorderLayout.CENTER);
        return root;
    }
 
    private JPanel buildNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(Color.WHITE);
        navbar.setPreferredSize(new Dimension(0, 56));
        navbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Format.COLOR_DIVIDER));
        JLabel logo = new JLabel("  ⚙️  Panel de Administración");
        logo.setFont(Format.FONT_SUBTITLE);
        logo.setForeground(Format.COLOR_PRIMARY);
        navbar.add(logo, BorderLayout.WEST);
        return navbar;
    }
 
    private JPanel buildLeft() {
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(Format.COLOR_BG_SURFACE);
        left.setPreferredSize(new Dimension(280, 0));
        left.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, Format.COLOR_DIVIDER),
            BorderFactory.createEmptyBorder(24, 20, 24, 20)
        ));
 
        JLabel lblTabla = new JLabel("Tabla");
        lblTabla.setFont(Format.FONT_BODY_SMALL);
        lblTabla.setForeground(Format.COLOR_TEXT_SECONDARY);
        lblTabla.setAlignmentX(LEFT_ALIGNMENT);
 
        styleComboBox(jComboBox1);
        jComboBox1.setAlignmentX(LEFT_ALIGNMENT);
        jComboBox1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
 
        JLabel lblCampos = new JLabel("Datos del registro");
        lblCampos.setFont(Format.FONT_BODY_SMALL);
        lblCampos.setForeground(Format.COLOR_TEXT_SECONDARY);
        lblCampos.setAlignmentX(LEFT_ALIGNMENT);
 
        panelCampos.setOpaque(false);
        panelCampos.setAlignmentX(LEFT_ALIGNMENT);
 
        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(LEFT_ALIGNMENT);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnPanel.add(BtnAdd);
        btnPanel.add(BtnUpdate);
        btnPanel.add(BtnDel);
 
        left.add(lblTabla);
        left.add(Box.createVerticalStrut(6));
        left.add(jComboBox1);
        left.add(Box.createVerticalStrut(20));
        left.add(sectionDivider("Campos"));
        left.add(Box.createVerticalStrut(10));
        left.add(panelCampos);
        left.add(Box.createVerticalStrut(16));
        left.add(btnPanel);
        return left;
    }
 
    private JPanel buildTableArea() {
        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(Format.COLOR_BG);
        area.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
        tblTuplas.setFont(Format.FONT_BODY_SMALL);
        tblTuplas.setForeground(Format.COLOR_TEXT_PRIMARY);
        tblTuplas.setBackground(Format.COLOR_BG);
        tblTuplas.setGridColor(Format.COLOR_DIVIDER);
        tblTuplas.setRowHeight(28);
        tblTuplas.setSelectionBackground(Format.COLOR_PRIMARY_LIGHT);
        tblTuplas.setSelectionForeground(Format.COLOR_TEXT_PRIMARY);
        tblTuplas.getTableHeader().setFont(Format.FONT_BODY_SMALL);
        tblTuplas.getTableHeader().setBackground(Format.COLOR_BG_SURFACE);
        tblTuplas.getTableHeader().setForeground(Format.COLOR_TEXT_SECONDARY);
        tblTuplas.getTableHeader().setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Format.COLOR_DIVIDER));
 
        scrollTabla.setBorder(BorderFactory.createLineBorder(Format.COLOR_DIVIDER));
        scrollTabla.getViewport().setBackground(Format.COLOR_BG);
 
        JLabel lblTabla = new JLabel("Registros");
        lblTabla.setFont(Format.FONT_SUBTITLE);
        lblTabla.setForeground(Format.COLOR_TEXT_PRIMARY);
        lblTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
 
        area.add(lblTabla,    BorderLayout.NORTH);
        area.add(scrollTabla, BorderLayout.CENTER);
        return area;
    }
 
    // ═════════════════════════════════════════════════════════════
    //  EVENTOS
    // ═════════════════════════════════════════════════════════════
 
    private void wireEvents() {
        jComboBox1.addActionListener(e -> {
            selectedCB = (String) jComboBox1.getSelectedItem();
            obtenerResultSet();
        });
 
        tblTuplas.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { obtenerIdSeleccionado(); }
        });
 
        BtnAdd.addActionListener(e -> {
            try { insertarRegistro(); }
            catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        });
 
        BtnUpdate.addActionListener(e -> {
            try { actualizarRegistro(); }
            catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        });
 
        BtnDel.addActionListener(e -> {
            try { eliminarRegistro(); }
            catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        });
    }
 
    // ═════════════════════════════════════════════════════════════
    //  LÓGICA DE BD — usa getAll() de cada clase del modelo
    // ═════════════════════════════════════════════════════════════
 
    /** Obtiene el ResultSet de la tabla seleccionada usando el objeto del modelo. */
    private ResultSet getResultSetForSelected() {
        if (selectedCB == null) return null;
        return switch (selectedCB) {
            case "Currency"      -> Currency.getAll();
            case "Province"      -> Province.getAll();
            case "Canton"        -> Canton.getAll();
            case "District"      -> District.getAll();
            case "PetType"       -> PetType.getAll();
            case "Race"          -> Race.getAll();
            case "Status"        -> Status.getAll();
            case "Color"         -> PetColor.getAll();
            case "TrainingEase"  -> TrainingEase.getAll();
            case "EnergyLevel"   -> EnergyLevel.getAll();
            case "CurrentStatus" -> CurrentStatus.getAll();
            case "Association"   -> Association.getAll();
            default              -> null;
        };
    }
 
    private void obtenerResultSet() {
        if (selectedCB == null) return;
        try {
            ResultSet rs1 = getResultSetForSelected();
            if (rs1 == null) return;
            generarCampos(rs1);
 
            ResultSet rs2 = getResultSetForSelected();
            if (rs2 == null) return;
            ConfigTable(rs2);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
 
    private void insertarRegistro() throws SQLException {
        if (selectedCB == null || camposDinamicos.size() < 2) return;
        if (!validarCampos()) return;
 
        // campo(1) = primer campo editable, campo(2) = segundo (si existe)
        String f1 = camposDinamicos.get(1).getText().trim();
        String f2 = camposDinamicos.size() > 2 ? camposDinamicos.get(2).getText().trim() : "";
 
        switch (selectedCB) {
            // Currency: col1=id, col2=name, col3=acronym
            case "Currency"     -> Currency.insert(0, f1, f2);
 
            // Province: col1=id, col2=name
            case "Province"     -> Province.insert(0, f1);
 
            // Canton: col1=id, col2=name, col3=id_province
            case "Canton"       -> Canton.insert(0, f1, parseIntSafe(f2));
 
            // District: col1=id, col2=name, col3=id_canton
            case "District"     -> District.insert(0, f1, parseIntSafe(f2));
 
            // PetType: col1=id, col2=name
            case "PetType"      -> PetType.insert(0, f1);
 
            // Race: col1=id, col2=name, col3=id_pet_type
            case "Race"         -> Race.insert(0, f1, parseIntSafe(f2));
 
            // Status: col1=id, col2=status_type
            case "Status"       -> Status.insert(0, f1);
 
            // Color: col1=id, col2=name
            case "Color"        -> PetColor.insert(0, f1);
 
            // TrainingEase: col1=id, col2=name
            case "TrainingEase" -> TrainingEase.insert(0, f1);
 
            // EnergyLevel: col1=id, col2=name
            case "EnergyLevel"  -> EnergyLevel.insert(0, f1);
 
            // CurrentStatus: col1=id, col2=status_type
            case "CurrentStatus"-> CurrentStatus.insert(0, f1);
 
            // Association: col1=id_user, col2=name
            case "Association"  -> Association.insert(0, f1);
 
            default -> JOptionPane.showMessageDialog(this,
                "Insert no implementado para: " + selectedCB,
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        obtenerResultSet();
    }
 
    private void actualizarRegistro() throws SQLException {
        if (selectedCB == null || selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecciona un registro de la tabla primero.",
                "Modificar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarCampos()) return;
 
        String f1 = camposDinamicos.get(1).getText().trim();
        String f2 = camposDinamicos.size() > 2 ? camposDinamicos.get(2).getText().trim() : "";
 
        switch (selectedCB) {
            case "Currency"      -> new Currency(selectedId).updateItem(selectedId, f1, f2);
            case "Province"      -> new Province(selectedId).updateItem(selectedId, f1);
            case "Canton"        -> new Canton(selectedId).updateItem(selectedId, f1, parseIntSafe(f2));
            case "District"      -> new District(selectedId).updateItem(selectedId, f1, parseIntSafe(f2));
            case "PetType"       -> new PetType(selectedId).updateItem(selectedId, f1);
            case "Race"          -> new Race(selectedId).updateItem(selectedId, f1, parseIntSafe(f2));
            case "Status"        -> new Status(selectedId).updateItem(selectedId, f1);
            case "Color"         -> new PetColor(selectedId).updateItem(selectedId, f1);
            case "TrainingEase"  -> new TrainingEase(selectedId).updateItem(selectedId, f1);
            case "EnergyLevel"   -> new EnergyLevel(selectedId).updateItem(selectedId, f1);
            case "CurrentStatus" -> new CurrentStatus(selectedId).updateItem(selectedId, f1);
            case "Association"   -> new Association(selectedId).update(f1);
            default -> JOptionPane.showMessageDialog(this,
                "Update no implementado para: " + selectedCB,
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        obtenerResultSet();
    }
 
    private void eliminarRegistro() throws SQLException {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecciona un registro de la tabla primero.",
                "Eliminar", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el registro con id " + selectedId + "?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
 
        if (confirm != JOptionPane.YES_OPTION) return;
 
        switch (selectedCB) {
            // Solo Association tiene deleteItem() implementado en los modelos actuales
            case "Association" -> new Association(selectedId).deleteItem();
            default -> JOptionPane.showMessageDialog(this,
                "Delete no disponible para: " + selectedCB + ".\nNo existe SP de eliminación.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        obtenerResultSet();
    }
 
    // ── Generación dinámica de campos ─────────────────────────────
 
    private void generarCampos(ResultSet rs) throws SQLException {
        panelCampos.removeAll();
        camposDinamicos.clear();
 
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
 
        panelCampos.setLayout(new GridLayout(cols, 2, 8, 8));
        panelCampos.setMaximumSize(new Dimension(Integer.MAX_VALUE, cols * 44));
 
        for (int i = 1; i <= cols; i++) {
            JLabel lbl = new JLabel(meta.getColumnLabel(i) + ":");
            lbl.setFont(Format.FONT_BODY_SMALL);
            lbl.setForeground(Format.COLOR_TEXT_SECONDARY);
 
            JTextField campo = new JTextField();
            styleTextField(campo);
 
            // La primera columna (id) no es editable
            if (i == 1) {
                campo.setEditable(false);
                campo.setBackground(Format.COLOR_BG_SURFACE);
                campo.setForeground(Format.COLOR_TEXT_SECONDARY);
            }
 
            camposDinamicos.add(campo);
            panelCampos.add(lbl);
            panelCampos.add(campo);
        }
 
        panelCampos.revalidate();
        panelCampos.repaint();
    }
 
    private void ConfigTable(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
 
        Vector<String> columnas = new Vector<>();
        for (int i = 1; i <= cols; i++) columnas.add(meta.getColumnLabel(i));
 
        Vector<Vector<Object>> filas = new Vector<>();
        while (rs.next()) {
            Vector<Object> fila = new Vector<>();
            for (int i = 1; i <= cols; i++) fila.add(rs.getObject(i));
            filas.add(fila);
        }
 
        tblTuplas.setModel(new DefaultTableModel(filas, columnas) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }
 
    private void obtenerIdSeleccionado() {
        int fila = tblTuplas.getSelectedRow();
        if (fila == -1) return;
 
        for (int col = 0; col < camposDinamicos.size(); col++) {
            Object valor = tblTuplas.getValueAt(fila, col);
            camposDinamicos.get(col).setText(valor != null ? valor.toString() : "");
        }
 
        Object valorId = tblTuplas.getValueAt(fila, 0);
        if      (valorId instanceof BigDecimal bd) selectedId = bd.intValue();
        else if (valorId instanceof Integer    i)  selectedId = i;
    }
 
    // ─────────────────────────────────────────────────────────────
    //  COMBO — tablas disponibles
    // ─────────────────────────────────────────────────────────────
 
    private void construirComboBox() {
        jComboBox1.removeAllItems();
        for (String item : new String[]{
                "Currency", "Province", "Canton", "District",
                "PetType", "Race", "Status", "Color",
                "TrainingEase", "EnergyLevel", "CurrentStatus", "Association"}) {
            jComboBox1.addItem(item);
        }
    }
 
    // ═════════════════════════════════════════════════════════════
    //  HELPERS
    // ═════════════════════════════════════════════════════════════
 
    /** Valida que todos los campos editables (índice > 0) tengan contenido.
     *  Resalta en rojo los que estén vacíos y muestra un mensaje. */
    private boolean validarCampos() {
        boolean valido = true;
        for (int i = 1; i < camposDinamicos.size(); i++) {
            JTextField campo = camposDinamicos.get(i);
            if (campo.getText().trim().isEmpty()) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Format.COLOR_ACCENT_RED, 1, true),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)
                ));
                valido = false;
            } else {
                // Restaurar borde normal si ya tiene valor
                styleTextField(campo);
            }
        }
        if (!valido) {
            JOptionPane.showMessageDialog(this,
                "Completa todos los campos antes de continuar.",
                "Campos requeridos", JOptionPane.WARNING_MESSAGE);
        }
        return valido;
    }
 
    /** Parsea un String a int de forma segura; retorna 0 si falla. */
    private static int parseIntSafe(String s) {
        if (s == null || s.isBlank()) return 0;
        try { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return 0; }
    }
 
    private static JButton buildActionButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                g2.setColor(getModel().isPressed() || getModel().isRollover()
                        ? Format.COLOR_PRIMARY_DARK
                        : Format.COLOR_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Format.RADIUS_BTN, Format.RADIUS_BTN);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(Format.FONT_BODY_SMALL);
        btn.setForeground(Format.COLOR_TEXT_ON_PRIMARY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(80, 32));
        return btn;
    }
 
    private static void styleTextField(JTextField tf) {
        tf.setFont(Format.FONT_BODY_SMALL);
        tf.setForeground(Format.COLOR_TEXT_PRIMARY);
        tf.setBackground(Format.COLOR_BG);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Format.COLOR_DIVIDER, 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }
 
    private static void styleComboBox(JComboBox<?> cb) {
        cb.setFont(Format.FONT_BODY_SMALL);
        cb.setBackground(Format.COLOR_BG);
        cb.setForeground(Format.COLOR_TEXT_PRIMARY);
        cb.setBorder(BorderFactory.createLineBorder(Format.COLOR_DIVIDER, 1, true));
    }
 
    private JLabel sectionDivider(String title) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(Format.FONT_BODY_SMALL);
        lbl.setForeground(Format.COLOR_PRIMARY);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        lbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Format.COLOR_DIVIDER),
            BorderFactory.createEmptyBorder(0, 0, 6, 0)
        ));
        return lbl;
    }
 
    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(admWindow::new);
    }
}