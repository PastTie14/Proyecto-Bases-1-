package Components;
 
import TablesObj.MedicSheet;
import TablesObj.Pet;
 
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
 
public class PetPopup extends JDialog {
 
    /*
     * ══════════════════════════════════════════════════════════════
     *  QUERY ESPERADO — Pet.getPopupItem(int id)
     *  Debe devolver UN solo registro con las siguientes columnas
     *  en este orden exacto:
     *
     *  Índice | Columna sugerida          | Ejemplo
     *  -------|---------------------------|-------------------------
     *    0    | picture_url               | "https://..."
     *    1    | status_type               | "Available"
     *    2    | pet_type                  | "Perro"
     *    3    | first_name                | "Firulais"
     *    4    | birthdate                 | "2021-03-15"
     *    5    | contact_email             | "refugio@mail.com"
     *    6    | date_lost                 | "2024-01-10"
     *    7    | date_found                | "2024-01-12"
     *    8    | size                      | "1" / "2" / "3"
     *    9    | energy_level              | "Alto"
     *   10    | training_ease             | "Fácil"
     *   11    | shelter_name              | "Refugio Esperanza"
     *   12    | bounty                    | "5000"
     *   13    | bounty_currency           | "CRC"
     *   14    | abandonment_description   | "Encontrado en la calle..."
     *   15    | diseases                  | "Ninguna" / lista separada por comas
     *   16    | treatments                | "Desparasitación" / lista separada por comas
     *
     *  Uso en Pet.java:
     *    public static ArrayList<String> getPopupItem(int id) { ... }
     *
     * ══════════════════════════════════════════════════════════════
     */
 
    // ── Índices del ArrayList ─────────────────────────────────────
    private static final int IDX_PICTURE       = 0;
    private static final int IDX_STATUS        = 1;
    private static final int IDX_PET_TYPE      = 2;
    private static final int IDX_NAME          = 3;
    private static final int IDX_BIRTHDATE     = 4;
    private static final int IDX_EMAIL         = 5;
    private static final int IDX_DATE_LOST     = 6;
    private static final int IDX_DATE_FOUND    = 7;
    private static final int IDX_SIZE          = 8;
    private static final int IDX_ENERGY        = 9;
    private static final int IDX_TRAINING      = 10;
    private static final int IDX_SHELTER       = 11;
    private static final int IDX_BOUNTY        = 12;
    private static final int IDX_CURRENCY      = 13;
    private static final int IDX_DESCRIPTION   = 14;
    private static final int IDX_DISEASES      = 15;
    private static final int IDX_TREATMENTS    = 16;
 
    // ── Estado interno ────────────────────────────────────────────
    private final Pet             pet;
    private final ArrayList<String> rs;
    private final ArrayList<ArrayList<String>> medicArr;
    private BufferedImage           image;
    private JPanel                  imagePanel;
 
    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────
 
    public PetPopup(Frame parent, Pet pet) {
        super(parent, true);
        this.pet = pet;
        this.rs  = Pet.getPopupItem(pet.getId()); // query dedicado para el popup
        medicArr = MedicSheet.getMedicalData(pet.getId());
 
        setUndecorated(true);
        setSize(Format.POPUP_WIDTH, Format.POPUP_HEIGHT);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(buildRoot());
 
        // Carga asíncrona de imagen
        String url = get(IDX_PICTURE);
        if (url != null && !url.isBlank()) {
            Format.loadImageAsync(url, img -> {
                image = img;
                imagePanel.repaint();
            });
        }
    }
 
    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCCIÓN
    // ─────────────────────────────────────────────────────────────
 
    private JPanel buildRoot() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                g2.setColor(Format.COLOR_BG);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1,
                        Format.RADIUS_CARD, Format.RADIUS_CARD);
                g2.setColor(Format.COLOR_DIVIDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1,
                        Format.RADIUS_CARD, Format.RADIUS_CARD);
                g2.dispose();
            }
        };
        root.setOpaque(false);
        root.add(buildImagePanel(),  BorderLayout.NORTH);
        root.add(buildScrollInfo(), BorderLayout.CENTER);
        root.add(buildFooter(),      BorderLayout.SOUTH);
        return root;
    }
 
    // ── Imagen superior ───────────────────────────────────────────
 
    private JPanel buildImagePanel() {
        imagePanel = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight() + Format.RADIUS_CARD,
                        Format.RADIUS_CARD, Format.RADIUS_CARD));
                if (image != null) {
                    Image scaled = Format.scaleCover(image, getWidth(), getHeight());
                    int x = (getWidth()  - scaled.getWidth(null))  / 2;
                    int y = (getHeight() - scaled.getHeight(null)) / 2;
                    g2.drawImage(scaled, x, y, null);
                } else {
                    g2.setColor(new Color(220, 220, 228));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.dispose();
            }
        };
        imagePanel.setPreferredSize(new Dimension(Format.POPUP_WIDTH, 220));
        imagePanel.setOpaque(false);
 
        // Badge de estado — índice 1: status_type
        JLabel badge = Format.buildStatusBadge(nvl(get(IDX_STATUS)));
        badge.setBounds(12, 12, 150, 26);
        imagePanel.add(badge);
 
        // Botón cerrar
        JButton closeBtn = new JButton("✕");
        closeBtn.setFont(Format.FONT_BODY);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.setBounds(Format.POPUP_WIDTH - 44, 8, 36, 36);
        closeBtn.addActionListener(e -> dispose());
        imagePanel.add(closeBtn);
 
        return imagePanel;
    }
 
    // ── Info con scroll ───────────────────────────────────────────
 
    private JScrollPane buildScrollInfo() {
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Format.COLOR_BG);
        info.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
 
        // Encabezado: tipo y nombre
        // IDX_PET_TYPE (2): pet_type  |  IDX_NAME (3): first_name
        info.add(label(nvl(get(IDX_PET_TYPE)), Format.FONT_TITLE,    Format.COLOR_TEXT_PRIMARY));
        info.add(label(nvl(get(IDX_NAME)),     Format.FONT_SUBTITLE, Format.COLOR_TEXT_SECONDARY));
        info.add(gap(8));
 
        // ── Sección: Información general ─────────────────────────
        info.add(sectionTitle("Información general"));
        // IDX_BIRTHDATE (4): birthdate
        info.add(row("📅 Fecha de nacimiento", get(IDX_BIRTHDATE)));
        // IDX_SIZE (8): size  →  string directo desde la BD (ej: "Pequeño", "Mediano", "Grande")
        info.add(row("📏 Tamaño",              get(IDX_SIZE)));
        // IDX_SHELTER (11): shelter_name
        info.add(row("📍 Refugio / Rescatista", get(IDX_SHELTER)));
        // IDX_EMAIL (5): contact_email
        info.add(row("📧 Contacto",            get(IDX_EMAIL)));
        info.add(gap(4));
 
        // ── Sección: Historial ────────────────────────────────────
        info.add(sectionTitle("Historial"));
        // IDX_DATE_LOST (6): date_lost
        info.add(row("📆 Fecha pérdida",       get(IDX_DATE_LOST)));
        // IDX_DATE_FOUND (7): date_found
        info.add(row("📆 Fecha encontrado",    get(IDX_DATE_FOUND)));
        // IDX_DESCRIPTION (14): abandonment_description
        info.add(row("📝 Descripción",         get(IDX_DESCRIPTION)));
        info.add(gap(4));
 
        // ── Sección: Comportamiento ───────────────────────────────
        info.add(sectionTitle("Comportamiento"));
        // IDX_ENERGY (9): energy_level
        info.add(row("⚡ Nivel de energía",    get(IDX_ENERGY)));
        // IDX_TRAINING (10): training_ease
        info.add(row("✋ Facilidad de entren.", get(IDX_TRAINING)));
        info.add(gap(4));
 
        // ── Sección: Salud ────────────────────────────────────────
        info.add(sectionTitle("Salud"));
        // IDX_DISEASES (15): diseases  (separadas por coma si hay varias)
        info.add(row("🦠 Enfermedades",     (medicArr.get(0)).toString() ));
        // IDX_TREATMENTS (16): treatments (separadas por coma si hay varias)
        info.add(row("💊 Tratamientos",        (medicArr.get(1)).toString()));
        info.add(gap(4));
 
        // ── Sección: Recompensa ───────────────────────────────────
        // IDX_BOUNTY (12): bounty  |  IDX_CURRENCY (13): bounty_currency
        String bounty   = get(IDX_BOUNTY);
        String currency = get(IDX_CURRENCY);
        boolean hasBounty = bounty != null && !bounty.isBlank() && !bounty.equals("0");
        if (hasBounty) {
            info.add(sectionTitle("Recompensa"));
            info.add(row("💰 Monto", nvl(currency) + " " + bounty));
            info.add(gap(4));
        }
 
        info.add(gap(8));
 
        JScrollPane scroll = new JScrollPane(info);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setBackground(Format.COLOR_BG);
        scroll.getViewport().setBackground(Format.COLOR_BG);
        return scroll;
    }
 
    // ── Footer ────────────────────────────────────────────────────
 
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        footer.setBackground(Format.COLOR_BG);
        footer.setBorder(Format.borderDivider());
 
        JButton adoptBtn = new JButton("Adoptar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                g2.setColor(Format.COLOR_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Format.RADIUS_BTN, Format.RADIUS_BTN);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        adoptBtn.setFont(Format.FONT_BUTTON);
        adoptBtn.setForeground(Format.COLOR_TEXT_ON_PRIMARY);
        adoptBtn.setContentAreaFilled(false);
        adoptBtn.setBorderPainted(false);
        adoptBtn.setPreferredSize(new Dimension(120, 36));
        adoptBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        adoptBtn.addActionListener(e -> onAdoptClicked());
        footer.add(adoptBtn);
        return footer;
    }
 
    // ─────────────────────────────────────────────────────────────
    //  EVENTO
    // ─────────────────────────────────────────────────────────────
 
    protected void onAdoptClicked() {
        // TODO: implementar lógica de adopción
    }
 
    // ─────────────────────────────────────────────────────────────
    //  HELPERS UI
    // ─────────────────────────────────────────────────────────────
 
    private JPanel row(String key, String value) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        JLabel k = new JLabel(key);
        k.setFont(Format.FONT_BODY_SMALL);
        k.setForeground(Format.COLOR_TEXT_SECONDARY);
        k.setPreferredSize(new Dimension(170, 20));
 
        JLabel v = new JLabel(value != null && !value.isBlank() ? value : "—");
        v.setFont(Format.FONT_BODY_SMALL);
        v.setForeground(Format.COLOR_TEXT_PRIMARY);
 
        row.add(k, BorderLayout.WEST);
        row.add(v, BorderLayout.CENTER);
        row.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        return row;
    }
 
    private JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Format.FONT_SUBTITLE);
        lbl.setForeground(Format.COLOR_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(Format.borderSection());
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return lbl;
    }
 
    private JLabel label(String text, Font font, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
 
    private Component gap(int h) { return Box.createVerticalStrut(h); }
 
    // ─────────────────────────────────────────────────────────────
    //  HELPERS DE DATOS
    // ─────────────────────────────────────────────────────────────
 
    /** Acceso seguro al ArrayList; devuelve null si el índice no existe. */
    private String get(int index) {
        return (rs != null && index < rs.size()) ? rs.get(index) : null;
    }
 
    private static String nvl(String s) {
        return (s != null && !s.isBlank()) ? s : "—";
    }
 
}