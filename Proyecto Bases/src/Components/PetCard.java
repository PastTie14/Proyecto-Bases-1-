package Components;

import TablesObj.Pet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class PetCard extends JPanel {

    // ── Modelo ────────────────────────────────────────────────────
    private final Pet pet;

    // ── Imagen ────────────────────────────────────────────────────
    private Image   image;
    private boolean loading = true;

    // ── Labels ───────────────────────────────────────────────────
    private JLabel statusBadge;
    private JLabel name;
    private JLabel petInfo;
    private JLabel energyInfo;
    private JLabel petType;
    private JLabel shelter;

    // ── Datos de la BD ────────────────────────────────────────────
    private final ArrayList<String> rs;

    // ── Hover ─────────────────────────────────────────────────────
    private boolean hovered = false;

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────

    public PetCard(Pet pet) {
        this.pet = pet;
        this.rs  = Pet.getCardItem(pet.getId()); // puede ser null si no hay datos

        setOpaque(false);
        setPreferredSize(new Dimension(Format.CARD_WIDTH, 370));
        setLayout(new BorderLayout());
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        add(buildImagePanel(), BorderLayout.NORTH);
        add(buildInfoPanel(),  BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { onCardClicked(); }
            @Override public void mouseEntered(MouseEvent e) { hovered = true;  onCardHovered(true);  repaint(); }
            @Override public void mouseExited (MouseEvent e) { hovered = false; onCardHovered(false); repaint(); }
        });

        String url = safeGet(0);
        if (url != null && !url.isBlank()) {
            loadImageAsync(url);
        } else {
            loading = false;
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  SAFE GETTER — nunca lanza IndexOutOfBoundsException
    // ─────────────────────────────────────────────────────────────

    /**
     * Devuelve rs.get(index) o null si rs es null, está vacío,
     * o el índice está fuera de rango.
     */
    private String safeGet(int index) {
        if (rs == null || index >= rs.size()) return null;
        return rs.get(index);
    }

    // ─────────────────────────────────────────────────────────────
    //  CARGA ASÍNCRONA
    // ─────────────────────────────────────────────────────────────

    private void loadImageAsync(String pictureUrl) {
        Format.loadImageAsync(pictureUrl, img -> {
            image   = img;
            loading = false;
            repaint();
        });
    }

    // ─────────────────────────────────────────────────────────────
    //  PANEL IMAGEN
    // ─────────────────────────────────────────────────────────────

    private JPanel buildImagePanel() {
        JPanel imgPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);

                g2.clip(new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight() + Format.RADIUS_CARD,
                        Format.RADIUS_CARD, Format.RADIUS_CARD));

                if (loading) {
                    g2.setColor(new Color(230, 230, 235));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(new Color(180, 180, 185));
                    g2.setFont(Format.FONT_BODY_SMALL);
                    FontMetrics fm = g2.getFontMetrics();
                    String txt = "Cargando…";
                    g2.drawString(txt,
                            (getWidth()  - fm.stringWidth(txt)) / 2,
                            (getHeight() + fm.getAscent())       / 2);

                } else if (image != null) {
                    Image scaled = Format.scaleCover(image, getWidth(), getHeight());
                    int x = (getWidth()  - scaled.getWidth(null))  / 2;
                    int y = (getHeight() - scaled.getHeight(null)) / 2;
                    g2.drawImage(scaled, x, y, null);

                } else {
                    g2.setColor(new Color(220, 220, 228));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(Format.COLOR_TEXT_SECONDARY);
                    g2.setFont(Format.FONT_BODY);
                    FontMetrics fm = g2.getFontMetrics();
                    String txt = "Sin imagen";
                    g2.drawString(txt,
                            (getWidth()  - fm.stringWidth(txt)) / 2,
                            (getHeight() + fm.getAscent())       / 2);
                }

                g2.dispose();
            }
        };
        imgPanel.setPreferredSize(new Dimension(Format.CARD_WIDTH, Format.CARD_IMG_HEIGHT));
        imgPanel.setOpaque(false);

        // Badge de estado — safeGet evita crash si rs es null o corto
        String status = safeGet(1);
        statusBadge = Format.buildStatusBadge(nvl(status));
        statusBadge.setBounds(8, 8, 130, 24);
        imgPanel.add(statusBadge);

        return imgPanel;
    }

    // ─────────────────────────────────────────────────────────────
    //  PANEL INFO
    // ─────────────────────────────────────────────────────────────

    private JPanel buildInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Format.COLOR_BG);
        infoPanel.setBorder(Format.borderCardPadding());

        // Nombre de la mascota (col 2)
        name = new JLabel(nvl(safeGet(2)));
        name.setFont(Format.FONT_SUBTITLE);
        name.setForeground(Format.COLOR_TEXT_PRIMARY);
        name.setAlignmentX(LEFT_ALIGNMENT);

        // Nombre + tamaño (col 6)
        String sizeStr   = nvl(safeGet(6));
        String firstName = nvl(pet.getFirstName());
        String petInfoTxt = firstName + (sizeStr.equals("—") ? "" : " • " + sizeStr);
        petInfo = new JLabel(petInfoTxt);
        petInfo.setFont(Format.FONT_BODY_SMALL);
        petInfo.setForeground(Format.COLOR_TEXT_SECONDARY);
        petInfo.setAlignmentX(LEFT_ALIGNMENT);

        // Energía (col 4) y entrenamiento (col 7)
        String energy   = nvl(safeGet(4));
        String training = nvl(safeGet(7));
        energyInfo = new JLabel("⚡ " + energy + "   ✋ " + training);
        energyInfo.setFont(Format.FONT_BODY_SMALL);
        energyInfo.setForeground(Format.COLOR_TEXT_SECONDARY);
        energyInfo.setAlignmentX(LEFT_ALIGNMENT);

        // Tipo de mascota (col 8)
        petType = new JLabel(nvl(safeGet(8)));
        petType.setFont(Format.FONT_PRICE);
        petType.setForeground(Format.COLOR_PRIMARY);
        petType.setAlignmentX(LEFT_ALIGNMENT);

        // Email / shelter (col 5)
        shelter = new JLabel("📍 " + nvl(safeGet(5)));
        shelter.setFont(Format.FONT_BODY_SMALL);
        shelter.setForeground(Format.COLOR_TEXT_SECONDARY);
        shelter.setAlignmentX(LEFT_ALIGNMENT);

        infoPanel.add(name);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(petInfo);
        infoPanel.add(Box.createVerticalStrut(Format.GAP_META));
        infoPanel.add(energyInfo);
        infoPanel.add(Box.createVerticalStrut(Format.GAP_META));
        infoPanel.add(petType);

        return infoPanel;
    }

    // ─────────────────────────────────────────────────────────────
    //  PINTURA
    // ─────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Format.enableAntiAlias(g2);

        int w = getWidth(), h = getHeight(), r = Format.RADIUS_CARD;

        g2.setColor(new Color(0, 0, 0, hovered ? 40 : 18));
        g2.fillRoundRect(3, 5, w - 6, h - 4, r, r);

        g2.setColor(Format.COLOR_BG);
        g2.fillRoundRect(0, 0, w - 2, h - 2, r, r);

        g2.setColor(Format.COLOR_DIVIDER);
        g2.drawRoundRect(0, 0, w - 2, h - 2, r, r);

        g2.dispose();
        super.paintComponent(g);
    }

    // ─────────────────────────────────────────────────────────────
    //  EVENTOS
    // ─────────────────────────────────────────────────────────────

    protected void onCardClicked() {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        new PetPopup(parent, pet).setVisible(true);
    }

    protected void onCardHovered(boolean entering) { }

    // ─────────────────────────────────────────────────────────────
    //  GETTERS
    // ─────────────────────────────────────────────────────────────

    public Pet getPet() { return pet; }

    public void setImage(Image img) {
        this.image   = img;
        this.loading = false;
        repaint();
    }

    // ─────────────────────────────────────────────────────────────
    //  UTILIDADES
    // ─────────────────────────────────────────────────────────────

    private static String nvl(String s) {
        return (s != null && !s.isBlank()) ? s : "—";
    }
}