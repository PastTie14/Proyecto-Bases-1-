package Components;
 
import Pets.Pet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
 
/**
 * Tarjeta visual de mascota para el catálogo principal.
 * La imagen se carga de forma asíncrona (SwingWorker) para no
 * bloquear el EDT ni lanzar excepciones en el constructor.
 */
public class PetCard extends JPanel {
 
    private Pet   pet;
    private Image image;
 
    // ── Labels declarados originalmente ──────────────────────────
    private JLabel name;
    private JLabel petInfo;
 
    // ── Labels extra ─────────────────────────────────────────────
    private JLabel statusBadge;
    private JLabel energyInfo;
    private JLabel price;
    private JLabel shelter;
 
    // ── Estado interno ────────────────────────────────────────────
    private boolean hovered = false;
    private boolean loading = true;   // true mientras la imagen no llegó
 
    // ─────────────────────────────────────────────────────────────
    public PetCard(Pet pet) {
        this.pet = pet;
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
 
        // Cargar imagen en segundo plano si hay URL
        if (pet.getPicture() != null && !pet.getPicture().isBlank()) {
            loadImageAsync(pet.getPicture());
        } else {
            loading = false;
        }
    }
 
    // ─────────────────────────────────────────────────────────────
    //  CARGA ASÍNCRONA
    // ─────────────────────────────────────────────────────────────
 
    private void loadImageAsync(String pictureUrl) {
        // Delega en Format para reutilizar la lógica de conexión
        // (User-Agent, timeouts) en cualquier componente futuro.
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
 
                // Clip redondeado sólo en esquinas superiores
                g2.clip(new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight() + Format.RADIUS_CARD,
                        Format.RADIUS_CARD, Format.RADIUS_CARD));
 
                if (loading) {
                    // Gris shimmer mientras carga
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
                    // Falló la carga o no había URL
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
 
        // Badge de estado
        String currentStatus = (pet.getExtraInfo() != null && pet.getExtraInfo().getCurrentStatus() != null)
                ? pet.getExtraInfo().getCurrentStatus()
                : pet.getStatus();
        statusBadge = Format.buildStatusBadge(currentStatus != null ? currentStatus : "Available");
        statusBadge.setBounds(8, 8, 130, 24);
        imgPanel.add(statusBadge);
 
        return imgPanel;
    }
 
    // ─────────────────────────────────────────────────────────────
    //  PANEL INFO
    // ─────────────────────────────────────────────────────────────
    private JPanel buildInfoPanel() {
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Format.COLOR_BG);
        info.setBorder(Format.borderCardPadding());
 
        name = new JLabel(pet.getPetType() != null ? pet.getPetType() : "—");
        name.setFont(Format.FONT_SUBTITLE);
        name.setForeground(Format.COLOR_TEXT_PRIMARY);
        name.setAlignmentX(LEFT_ALIGNMENT);
 
        String sizeStr = (pet.getExtraInfo() != null) ? sizeLabel(pet.getExtraInfo().getSize()) : "";
        petInfo = new JLabel(pet.getPetType() + (sizeStr.isEmpty() ? "" : " • " + sizeStr));
        petInfo.setFont(Format.FONT_BODY_SMALL);
        petInfo.setForeground(Format.COLOR_TEXT_SECONDARY);
        petInfo.setAlignmentX(LEFT_ALIGNMENT);
 
        String energy   = "";
        String training = "";
        if (pet.getExtraInfo() != null && pet.getExtraInfo().getMedicInfo() != null) {
            energy   = pet.getExtraInfo().getMedicInfo().getEnergyLevel();
            training = pet.getExtraInfo().getMedicInfo().getTrainingEase();
        }
        energyInfo = new JLabel("⚡ " + nvl(energy) + "   ✋ " + nvl(training));
        energyInfo.setFont(Format.FONT_BODY_SMALL);
        energyInfo.setForeground(Format.COLOR_TEXT_SECONDARY);
        energyInfo.setAlignmentX(LEFT_ALIGNMENT);
 
        int    bounty   = (pet.getExtraInfo() != null) ? pet.getExtraInfo().getBounty() : 0;
        String currency = (pet.getExtraInfo() != null && pet.getExtraInfo().getBountyCurrency() != null)
                ? pet.getExtraInfo().getBountyCurrency() : "$";
        price = new JLabel(currency + " " + bounty);
        price.setFont(Format.FONT_PRICE);
        price.setForeground(Format.COLOR_PRIMARY);
        price.setAlignmentX(LEFT_ALIGNMENT);
 
        //String rescuerName = (pet.getRescuer() != null) ? pet.getRescuer().toString() : "—";
        //shelter = new JLabel("📍 " + rescuerName);
        shelter.setFont(Format.FONT_BODY_SMALL);
        shelter.setForeground(Format.COLOR_TEXT_SECONDARY);
        shelter.setAlignmentX(LEFT_ALIGNMENT);
 
        info.add(name);
        info.add(Box.createVerticalStrut(2));
        info.add(petInfo);
        info.add(Box.createVerticalStrut(Format.GAP_META));
        info.add(energyInfo);
        info.add(Box.createVerticalStrut(Format.GAP_META));
        info.add(price);
        info.add(Box.createVerticalStrut(Format.GAP_META));
        info.add(shelter);
 
        return info;
    }
 
    // ─────────────────────────────────────────────────────────────
    //  PINTURA DE LA TARJETA
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
    //  MÉTODOS DE EVENTO
    // ─────────────────────────────────────────────────────────────
 
    protected void onCardClicked() {
        // TODO: abrir popup/detalle de la mascota
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        new PetPopup(parent, pet).setVisible(true);
    }
 
    protected void onCardHovered(boolean entering) {
        //
    }
 
    // ─────────────────────────────────────────────────────────────
    //  SETTERS / GETTERS
    // ─────────────────────────────────────────────────────────────
 
    public void setImage(Image image) {
        this.image = image;
        loading = false;
        repaint();
    }
 
    public Pet getPet() { return pet; }
 
    // ─────────────────────────────────────────────────────────────
    //  UTILIDADES
    // ─────────────────────────────────────────────────────────────
 
    private static String nvl(String s) {
        return (s != null && !s.isBlank()) ? s : "—";
    }
 
    private static String sizeLabel(int size) {
        if (size <= 0) return "";
        if (size == 1) return "Pequeño";
        if (size == 2) return "Mediano";
        return "Grande";
    }
}