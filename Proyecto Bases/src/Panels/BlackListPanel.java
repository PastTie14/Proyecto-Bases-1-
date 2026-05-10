package Panels;
 
import Components.Format;
import TablesObj.Adopter;
import TablesObj.BlackList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
 

public class BlackListPanel extends JPanel {
 
    // ════════════════════════════════════════════════════════════════
    //  IDs SELECCIONADOS
    // ════════════════════════════════════════════════════════════════
 
    public int idBanned;
 
    public int idBlacklist;
    
    public int idUser;
 
    // ════════════════════════════════════════════════════════════════
    //  COMPONENTES
    // ════════════════════════════════════════════════════════════════
 
    private final JTable      tblUsers      = new JTable();
    private final JScrollPane scrollUsers   = new JScrollPane(tblUsers);
    private final JLabel      lblUsers      = new JLabel("Usuarios");
    private final JTable      tblBlacklist  = new JTable();
    private final JScrollPane scrollBlacklist = new JScrollPane(tblBlacklist);
    private final JLabel      lblBlacklist  = new JLabel("Lista Negra");
    private final JLabel      lblStatus     = new JLabel(" ");
    public final JTextArea txaRazon  = buildTextArea();
    public final JButton btnAgregar  = buildActionButton("Agregar",  true);
    public final JButton btnActualizar = buildActionButton ("Actualizar", false);
    public final JButton btnEliminar = buildActionButton("Eliminar", false);
 
    // ════════════════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ════════════════════════════════════════════════════════════════
 
    public BlackListPanel(int idUser) {
        this.idUser = idUser;
        this.idBlacklist = BlackList.getBlackListId(idBanned);
        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);
        add(buildCenter(),  BorderLayout.CENTER);
        add(buildSouth(),   BorderLayout.SOUTH);
        ArrayList<String> userCol = new ArrayList();
        userCol.add("Id");userCol.add("email"); userCol.add("Nombre"); userCol.add("Apellido");
        fillUsersTable(userCol, Adopter.getAll());
        
        ArrayList<String> blackCol = new ArrayList();
        blackCol.add("email");blackCol.add("Nombre");blackCol.add("Motivo");
        fillBlacklistTable(blackCol, BlackList.getBannedUsers(idUser));
 
        wireEvents();
    }
 
    // ════════════════════════════════════════════════════════════════
    //  CONSTRUCCIÓN DE UI
    // ════════════════════════════════════════════════════════════════

    private JSplitPane buildCenter() {
        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                buildTablePanel(tblUsers,     scrollUsers,     lblUsers,     "Usuarios"),
                buildTablePanel(tblBlacklist, scrollBlacklist, lblBlacklist, "Lista Negra")
        );
        split.setResizeWeight(0.5);
        split.setDividerLocation(0.5);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setDividerSize(6);
        split.setBackground(Format.COLOR_BG);
        return split;
    }

    private JPanel buildTablePanel(JTable table, JScrollPane scroll,
                                   JLabel lblTitle, String titleText) {
        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(Format.COLOR_BG);
        area.setBorder(BorderFactory.createEmptyBorder(20, 20, 12, 20));
 
        // ── Estilo de tabla ───────────────────────────────────────
        table.setFont(Format.FONT_BODY_SMALL);
        table.setForeground(Format.COLOR_TEXT_PRIMARY);
        table.setBackground(Format.COLOR_BG);
        table.setGridColor(Format.COLOR_DIVIDER);
        table.setRowHeight(28);
        table.setSelectionBackground(Format.COLOR_PRIMARY_LIGHT);
        table.setSelectionForeground(Format.COLOR_TEXT_PRIMARY);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(Format.FONT_BODY_SMALL);
        table.getTableHeader().setBackground(Format.COLOR_BG_SURFACE);
        table.getTableHeader().setForeground(Format.COLOR_TEXT_SECONDARY);
        table.getTableHeader().setBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Format.COLOR_DIVIDER));
 
        scroll.setBorder(BorderFactory.createLineBorder(Format.COLOR_DIVIDER));
        scroll.getViewport().setBackground(Format.COLOR_BG);
 
        lblTitle.setText(titleText);
        lblTitle.setFont(Format.FONT_SUBTITLE);
        lblTitle.setForeground(Format.COLOR_TEXT_PRIMARY);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
 
        area.add(lblTitle, BorderLayout.NORTH);
        area.add(scroll,   BorderLayout.CENTER);
        return area;
    }
 
    private JPanel buildSouth() {
        JPanel south = new JPanel(new BorderLayout(0, 10));
        south.setBackground(Format.COLOR_BG);
        south.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Format.COLOR_DIVIDER),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)
        ));
 
        JPanel razonPanel = new JPanel(new BorderLayout(0, 6));
        razonPanel.setOpaque(false);
 
        JLabel lblRazon = new JLabel("Razón para agregar a lista negra");
        lblRazon.setFont(Format.FONT_BODY_SMALL);
        lblRazon.setForeground(Format.COLOR_TEXT_SECONDARY);
 
        JScrollPane scrollRazon = new JScrollPane(txaRazon);
        scrollRazon.setBorder(BorderFactory.createLineBorder(Format.COLOR_DIVIDER, 1, true));
        scrollRazon.setPreferredSize(new Dimension(0, 72));
        scrollRazon.getViewport().setBackground(Format.COLOR_BG);
 
        razonPanel.add(lblRazon,    BorderLayout.NORTH);
        razonPanel.add(scrollRazon, BorderLayout.CENTER);
 
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);
 
        lblStatus.setFont(Format.FONT_BODY_SMALL);
        lblStatus.setForeground(Format.COLOR_TEXT_SECONDARY);
 
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);
        btnRow.add(btnAgregar);
        btnRow.add(btnActualizar);
        btnRow.add(btnEliminar);
 
        bottomRow.add(lblStatus, BorderLayout.WEST);
        bottomRow.add(btnRow,    BorderLayout.EAST);
 
        south.add(razonPanel, BorderLayout.CENTER);
        south.add(bottomRow,  BorderLayout.SOUTH);
        return south;
    }
 
    // ════════════════════════════════════════════════════════════════
    //  EVENTOS
    // ════════════════════════════════════════════════════════════════
 
    private void wireEvents() {
        tblUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblUsers.getSelectedRow();
                if (row == -1) return;
                Object val = tblUsers.getValueAt(row, 0);
                idBanned = parseId(val);
                refreshStatusLabel();
            }
        });
 
        tblBlacklist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblBlacklist.getSelectedRow();
                if (row == -1) return;
                Object val = tblBlacklist.getValueAt(row, 0);
                idBlacklist = parseId(val);
                refreshStatusLabel();
            }
        });
    }
 
    // ════════════════════════════════════════════════════════════════
    //  MÉTODOS PÚBLICOS DE CARGA DE TABLAS
    // ════════════════════════════════════════════════════════════════
 
    public void fillUsersTable(ArrayList<String> columns,
                               ArrayList<ArrayList<Object>> data) {
        idBanned = -1;
        SwingUtilities.invokeLater(() -> {
            tblUsers.setModel(buildModel(columns, data));
            lblUsers.setText("Usuarios (" + data.size() + ")");
            refreshStatusLabel();
        });
    }
 
    public void fillBlacklistTable(ArrayList<String> columns,
                                   ArrayList<ArrayList<Object>> data) {
        idBlacklist = -1;
        SwingUtilities.invokeLater(() -> {
            tblBlacklist.setModel(buildModel(columns, data));
            lblBlacklist.setText("Lista Negra (" + data.size() + ")");
            refreshStatusLabel();
        });
    }
 
    // ════════════════════════════════════════════════════════════════
    //  HELPERS PRIVADOS
    // ════════════════════════════════════════════════════════════════
    private static DefaultTableModel buildModel(ArrayList<String> columns,
                                                ArrayList<ArrayList<Object>> data) {
        DefaultTableModel model = new DefaultTableModel(columns.toArray(), 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        for (ArrayList<Object> row : data) {
            model.addRow(row.toArray());
        }
        return model;
    }
 
    private void refreshStatusLabel() {
        String userTxt  = idBanned      == -1 ? "—" : String.valueOf(idBanned);
        String blTxt    = idBlacklist == -1 ? "—" : String.valueOf(idBlacklist);
        lblStatus.setText("  Usuario: " + userTxt + "   |   Lista Negra: " + blTxt);
    }
 
  
    private static int parseId(Object val) {
        if (val == null)                          return -1;
        if (val instanceof Integer  i)            return i;
        if (val instanceof Long     l)            return l.intValue();
        if (val instanceof java.math.BigDecimal b) return b.intValue();
        try { return Integer.parseInt(val.toString().trim()); }
        catch (NumberFormatException e)           { return -1; }
    }
 
    private static JTextArea buildTextArea() {
        JTextArea txa = new JTextArea();
        txa.setFont(Format.FONT_BODY_SMALL);
        txa.setForeground(Format.COLOR_TEXT_PRIMARY);
        txa.setBackground(Format.COLOR_BG);
        txa.setCaretColor(Format.COLOR_PRIMARY);
        txa.setLineWrap(true);
        txa.setWrapStyleWord(true);
        txa.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        return txa;
    }
 
    private static JButton buildActionButton(String text, boolean primary) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                Color base  = primary ? Format.COLOR_PRIMARY      : Format.COLOR_ACCENT_RED;
                Color hover = primary ? Format.COLOR_PRIMARY_DARK  : Format.COLOR_ACCENT_RED.darker();
                g2.setColor(getModel().isPressed() || getModel().isRollover() ? hover : base);
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
        btn.setPreferredSize(new Dimension(90, 32));
        return btn;
    }
}