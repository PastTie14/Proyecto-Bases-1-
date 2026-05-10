package Panels;

import Components.Format;
import TablesObj.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.*;

public class RatingPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(RatingPanel.class.getName());

    private final int idUser;

    // ════════════════════════════════════════════════════════════════
    //  ESTADO
    // ════════════════════════════════════════════════════════════════

    private int selectedIdPet     = -1;
    private int selectedIdAdopter = -1;
    private int existingRatingId  = -1;   // -1 → no existe aún, >0 → actualizar

    // ════════════════════════════════════════════════════════════════
    //  COMPONENTES
    // ════════════════════════════════════════════════════════════════

    private final JTable      tblPets    = new JTable();
    private final JScrollPane scrollPets = new JScrollPane(tblPets);

    // Panel derecho — info del adoptador
    private final JLabel      lblAdopterName = new JLabel("—");
    private final JLabel      lblAdopterType = new JLabel("");
    private final StarRating  starRating     = new StarRating();
    private final JButton     btnSave        = buildSaveBtn();
    private final JLabel      lblStatus      = new JLabel(" ");

    // ════════════════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ════════════════════════════════════════════════════════════════

    public RatingPanel(int idUser) {
        this.idUser = idUser;

        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        styleTable(tblPets);
        wireEvents();
        setRightEnabled(false);
        loadPets();
    }

    // ════════════════════════════════════════════════════════════════
    //  CONSTRUCCIÓN DE UI
    // ════════════════════════════════════════════════════════════════

    private JLabel buildHeader() {
        JLabel lbl = new JLabel("Calificar adoptadores");
        lbl.setFont(Format.FONT_SUBTITLE);
        lbl.setForeground(Format.COLOR_PRIMARY);
        lbl.setOpaque(true);
        lbl.setBackground(Format.COLOR_BG);
        lbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Format.COLOR_DIVIDER),
            BorderFactory.createEmptyBorder(16, 24, 16, 24)
        ));
        return lbl;
    }

    private JSplitPane buildCenter() {
        JSplitPane split = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            buildLeftPanel(),
            buildRightPanel()
        );
        split.setResizeWeight(0.45);
        split.setDividerLocation(0.45);
        split.setDividerSize(6);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setBackground(Format.COLOR_BG);
        return split;
    }

    // ── Panel izquierdo: tabla de mascotas ────────────────────────

    private JPanel buildLeftPanel() {
        scrollPets.setBorder(BorderFactory.createLineBorder(Format.COLOR_DIVIDER));
        scrollPets.getViewport().setBackground(Format.COLOR_BG);

        JLabel title = new JLabel("Mis mascotas registradas");
        title.setFont(Format.FONT_SUBTITLE);
        title.setForeground(Format.COLOR_TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Format.COLOR_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 12));
        panel.add(title,      BorderLayout.NORTH);
        panel.add(scrollPets, BorderLayout.CENTER);
        return panel;
    }

    // ── Panel derecho: calificación ───────────────────────────────

    private JPanel buildRightPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(Format.COLOR_BG);
        outer.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 20));

        JLabel title = new JLabel("Calificación");
        title.setFont(Format.FONT_SUBTITLE);
        title.setForeground(Format.COLOR_TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // ── Tarjeta de info del adoptador ─────────────────────────
        JPanel infoCard = new JPanel();
        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
        infoCard.setBackground(Format.COLOR_BG_SURFACE);
        infoCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Format.COLOR_DIVIDER, 1, true),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        JLabel lblCaption = new JLabel("Adoptador seleccionado");
        lblCaption.setFont(Format.FONT_BODY_SMALL);
        lblCaption.setForeground(Format.COLOR_TEXT_SECONDARY);

        lblAdopterName.setFont(Format.FONT_BODY);
        lblAdopterName.setForeground(Format.COLOR_TEXT_PRIMARY);

        lblAdopterType.setFont(Format.FONT_BODY_SMALL);
        lblAdopterType.setForeground(Format.COLOR_PRIMARY);

        infoCard.add(lblCaption);
        infoCard.add(Box.createVerticalStrut(6));
        infoCard.add(lblAdopterName);
        infoCard.add(Box.createVerticalStrut(4));
        infoCard.add(lblAdopterType);

        // ── Sección de estrellas ──────────────────────────────────
        JLabel lblStarsCaption = new JLabel("Puntuación (1 – 5)");
        lblStarsCaption.setFont(Format.FONT_BODY_SMALL);
        lblStarsCaption.setForeground(Format.COLOR_TEXT_SECONDARY);
        lblStarsCaption.setBorder(BorderFactory.createEmptyBorder(22, 0, 8, 0));

        JPanel starsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        starsRow.setOpaque(false);
        starsRow.add(starRating);

        // ── Botón guardar ─────────────────────────────────────────
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnRow.setOpaque(false);
        btnRow.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        btnRow.add(btnSave);

        // ── Ensamblar contenido ───────────────────────────────────
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        infoCard.setAlignmentX(LEFT_ALIGNMENT);
        lblStarsCaption.setAlignmentX(LEFT_ALIGNMENT);
        starsRow.setAlignmentX(LEFT_ALIGNMENT);
        btnRow.setAlignmentX(LEFT_ALIGNMENT);

        content.add(infoCard);
        content.add(lblStarsCaption);
        content.add(starsRow);
        content.add(btnRow);

        outer.add(title,   BorderLayout.NORTH);
        outer.add(content, BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        bar.setBackground(Format.COLOR_BG);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Format.COLOR_DIVIDER));
        lblStatus.setFont(Format.FONT_BODY_SMALL);
        lblStatus.setForeground(Format.COLOR_TEXT_SECONDARY);
        bar.add(lblStatus);
        return bar;
    }

    private JButton buildSaveBtn() {
        JButton btn = new JButton("Guardar calificación");
        btn.setFont(Format.FONT_BODY_SMALL);
        btn.setForeground(Color.WHITE);
        btn.setBackground(Format.COLOR_PRIMARY);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 36));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(Format.COLOR_PRIMARY_DARK);
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(Format.COLOR_PRIMARY);
            }
        });
        return btn;
    }

    // ════════════════════════════════════════════════════════════════
    //  CARGA DE DATOS 
    // ════════════════════════════════════════════════════════════════

    /** Carga las mascotas registradas por este usuario en el hilo de fondo. */
    private void loadPets() {
        setStatus("Cargando mascotas…");

        new SwingWorker<ArrayList<ArrayList<Object>>, Void>() {

            @Override protected ArrayList<ArrayList<Object>> doInBackground() {
                ArrayList<ArrayList<Object>> rows = new ArrayList<>();
                try {
                    ResultSet rs = Pet.getByRescuer(idUser);
                    while (rs != null && rs.next()) {
                        ArrayList<Object> row = new ArrayList<>();
                        row.add(rs.getObject(1));  // id_pet
                        row.add(rs.getString(2));  // nombre
                        row.add(rs.getString(3));  // estado
                        row.add(rs.getString(4));  // fecha de encuentro
                        rows.add(row);
                    }
                } catch (SQLException ex) {
                    LOG.log(Level.SEVERE, "Error cargando mascotas rescatadas", ex);
                }
                return rows;
            }

            @Override protected void done() {
                try {
                    ArrayList<ArrayList<Object>> rows = get();
                    ArrayList<String> cols = new ArrayList<>();
                    cols.add("ID"); cols.add("Nombre"); cols.add("Estado"); cols.add("Fecha encuentro");
                    fillTable(tblPets, cols, rows);
                    setStatus(rows.isEmpty()
                        ? "No tienes mascotas registradas."
                        : rows.size() + " mascota(s) encontrada(s). Selecciona una para calificar.");
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "Error mostrando mascotas", ex);
                }
            }
        }.execute();
    }

    private void onPetSelected(int idPet) {
        this.selectedIdPet     = idPet;
        this.selectedIdAdopter = -1;
        this.existingRatingId  = -1;

        setRightEnabled(false);
        lblAdopterName.setText("Buscando adoptador…");
        lblAdopterType.setText("");
        starRating.setRating(0);
        setStatus("Cargando información del adoptador…");

        new SwingWorker<AdopterInfo, Void>() {

            @Override protected AdopterInfo doInBackground() {
                return resolveAdopter(idPet);
            }

            @Override protected void done() {
                try {
                    AdopterInfo info = get();

                    if (info == null) {
                        lblAdopterName.setText("Sin adoptador registrado");
                        lblAdopterType.setText("");
                        setStatus("Esta mascota aún no ha sido adoptada.");
                        return;
                    }

                    selectedIdAdopter = info.idAdopter();
                    existingRatingId  = info.existingRatingId();

                    lblAdopterName.setText(info.name());
                    lblAdopterType.setText("Tipo: " + info.type());
                    starRating.setRating(info.existingScore());
                    setRightEnabled(true);

                    setStatus(info.existingRatingId() > 0
                        ? "Calificación existente cargada — puedes modificarla."
                        : "Selecciona una puntuación y presiona Guardar.");
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "Error cargando adoptador", ex);
                    setStatus("Error al cargar el adoptador.");
                }
            }
        }.execute();
    }

    private AdopterInfo resolveAdopter(int idPet) {
        try {
            ResultSet af = AdoptionForm.getByPet(idPet);
            if (af == null || !af.next()) return null;
            int idAdopter = af.getInt(5);
            if (idAdopter <= 0) return null;
            String name = "Desconocido";
            String type = "";

            if (Adopter.getAdopterByID(idAdopter)) {
                type = "Adoptador";
                ResultSet r = new Adopter(idAdopter).getAllRS();
                if (r != null && r.next()) {
                    name = nullSafe(r.getString(2)) + " " + nullSafe(r.getString(4));
                }
            } else if (CribHouse.getCribHouseByID(idAdopter)) {
                type = "Casa Cuna";
                ResultSet r = CribHouse.getAll();
                while (r != null && r.next()) {
                    if (r.getInt(1) == idAdopter) {
                        name = nullSafe(r.getString(2));
                        break;
                    }
                }
            } else if (Rescuer.getRescuerByID(idAdopter)) {
                type = "Rescatista";
                ResultSet r = Rescuer.getAll();
                while (r != null && r.next()) {
                    if (r.getInt(1) == idAdopter) {
                        name = nullSafe(r.getString(2)) + " " + nullSafe(r.getString(4));
                        break;
                    }
                }
            }
            int existingId    = -1;
            int existingScore =  0;
            ResultSet rat = Rating.getByUserAndAdopter(idUser, idAdopter);
            if (rat != null && rat.next()) {
                existingId    = rat.getInt(1);  // id_rating
                existingScore = rat.getInt(2);  // score
            }

            return new AdopterInfo(idAdopter, name.trim(), type, existingId, existingScore);

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en resolveAdopter para pet=" + idPet, ex);
            return null;
        }
    }

    // ════════════════════════════════════════════════════════════════
    //  GUARDAR CALIFICACIÓN
    // ════════════════════════════════════════════════════════════════

    private void saveRating() {
        if (selectedIdAdopter <= 0) return;

        int score = starRating.getRating();
        if (score == 0) {
            setStatus("Selecciona al menos una estrella antes de guardar.");
            return;
        }

        btnSave.setEnabled(false);
        setStatus("Guardando…");

        final int snapExistingId = existingRatingId;   // captura antes del hilo

        new SwingWorker<Void, Void>() {

            @Override protected Void doInBackground() {
                if (snapExistingId > 0) {
                    new Rating(snapExistingId).update(score, idUser, selectedIdAdopter);
                } else {
                    Rating.insert(0, score, idUser, selectedIdAdopter);
                }
                return null;
            }

            @Override protected void done() {
                btnSave.setEnabled(true);
                if (snapExistingId <= 0) {
                    try {
                        ResultSet rs = Rating.getByUserAndAdopter(idUser, selectedIdAdopter);
                        if (rs != null && rs.next()) existingRatingId = rs.getInt(1);
                    } catch (SQLException ex) {
                        LOG.log(Level.WARNING, "No se pudo obtener el id del nuevo rating", ex);
                    }
                }
                setStatus("->  Calificación de " + score + " estrella(s) guardada correctamente.");
            }
        }.execute();
    }

    // ════════════════════════════════════════════════════════════════
    //  EVENTOS
    // ════════════════════════════════════════════════════════════════

    private void wireEvents() {
        tblPets.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = tblPets.getSelectedRow();
                if (row < 0) return;
                int idPet = parseId(tblPets.getValueAt(row, 0));
                if (idPet > 0) onPetSelected(idPet);
            }
        });

        btnSave.addActionListener(e -> saveRating());
    }


    private void setRightEnabled(boolean on) {
        starRating.setEnabled(on);
        btnSave.setEnabled(on);
    }

    private void setStatus(String msg) {
        SwingUtilities.invokeLater(() -> lblStatus.setText("  " + msg));
    }

    private static void styleTable(JTable t) {
        t.setFont(Format.FONT_BODY_SMALL);
        t.setForeground(Format.COLOR_TEXT_PRIMARY);
        t.setBackground(Format.COLOR_BG);
        t.setGridColor(Format.COLOR_DIVIDER);
        t.setRowHeight(28);
        t.setSelectionBackground(Format.COLOR_PRIMARY_LIGHT);
        t.setSelectionForeground(Format.COLOR_TEXT_PRIMARY);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.getTableHeader().setFont(Format.FONT_BODY_SMALL);
        t.getTableHeader().setBackground(Format.COLOR_BG_SURFACE);
        t.getTableHeader().setForeground(Format.COLOR_TEXT_SECONDARY);
        t.getTableHeader().setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Format.COLOR_DIVIDER));
    }

    private static void fillTable(JTable table,
                                  ArrayList<String> cols,
                                  ArrayList<ArrayList<Object>> rows) {
        DefaultTableModel model = new DefaultTableModel(cols.toArray(), 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (ArrayList<Object> row : rows) model.addRow(row.toArray());
        SwingUtilities.invokeLater(() -> table.setModel(model));
    }

    private static int parseId(Object val) {
        if (val == null) return -1;
        if (val instanceof Integer  i) return i;
        if (val instanceof Long     l) return l.intValue();
        if (val instanceof java.math.BigDecimal b) return b.intValue();
        try { return Integer.parseInt(val.toString().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private static String nullSafe(String s) { return s != null ? s : ""; }


    private record AdopterInfo(
        int    idAdopter,
        String name,
        String type,
        int    existingRatingId,   // -1 si no hay rating previo
        int    existingScore       //  0 si no hay rating previo
    ) {}

    // ════════════════════════════════════════════════════════════════
    //  COMPONENTE INTERNO — Lo meti aqui por que solo se usa 1 vez
    // ════════════════════════════════════════════════════════════════
    static class StarRating extends JPanel {

        private static final Color COLOR_ON     = new Color(255, 185, 0);   // dorado
        private static final Color COLOR_HOVER  = new Color(255, 210, 80);  // dorado suave
        private static final Color COLOR_OFF    = new Color(200, 200, 200); // gris
        private static final Color COLOR_DISABLED = new Color(225, 225, 225);

        private final JToggleButton[] stars = new JToggleButton[5];
        private int currentRating = 0;

        StarRating() {
            setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
            setOpaque(false);

            for (int i = 0; i < 5; i++) {
                final int value = i + 1;   // valor que representa esta estrella

                JToggleButton btn = new JToggleButton("★") {
                    @Override protected void paintComponent(Graphics g) {
                        // Pintamos solo el glifo, sin fondo de botón
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        g2.setColor(getForeground());
                        FontMetrics fm = g2.getFontMetrics();
                        int x = (getWidth()  - fm.stringWidth("★")) / 2;
                        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                        g2.drawString("★", x, y);
                        g2.dispose();
                    }
                };

                btn.setFont(new Font("Dialog", Font.PLAIN, 34));
                btn.setPreferredSize(new Dimension(46, 46));
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setFocusPainted(false);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btn.setForeground(COLOR_OFF);

                // Preview de hover: ilumina hasta la estrella apuntada
                btn.addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) {
                        if (isEnabled()) applyHover(value);
                    }
                    @Override public void mouseExited(MouseEvent e) {
                        if (isEnabled()) refresh();
                    }
                });

                // Click: establece puntuación
                btn.addActionListener(e -> setRating(value));

                stars[i] = btn;
                add(btn);
            }

            refresh();
        }

        /** Establece la puntuación actual (0 = ninguna). */
        void setRating(int rating) {
            currentRating = Math.max(0, Math.min(5, rating));
            refresh();
        }

        int getRating() { return currentRating; }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            for (JToggleButton s : stars) s.setEnabled(enabled);
            refresh();
        }

        // ── Coloreado ─────────────────────────────────────────────

        /** Pinta el estado real (calificación guardada). */
        private void refresh() {
            boolean on = isEnabled();
            for (int i = 0; i < 5; i++) {
                stars[i].setForeground(
                    !on        ? COLOR_DISABLED :
                    i < currentRating ? COLOR_ON : COLOR_OFF
                );
                stars[i].setSelected(i < currentRating);
            }
            repaint();
        }

        /** Preview mientras el cursor está sobre una estrella. */
        private void applyHover(int upTo) {
            for (int i = 0; i < 5; i++) {
                stars[i].setForeground(i < upTo ? COLOR_HOVER : COLOR_OFF);
            }
            repaint();
        }
    }
}
