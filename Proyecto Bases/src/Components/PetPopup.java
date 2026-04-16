package Components;

import Pets.Pet;
import Pets.MedicSheet;
import Pets.PetExtraInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Popup modal que muestra toda la información de una mascota.
 * Se abre desde PetCard.onCardClicked().
 *
 * Uso:
 * <pre>
 *   PetPopup popup = new PetPopup(parentFrame, pet);
 *   popup.setVisible(true);
 * </pre>
 */
public class PetPopup extends JDialog {

    private final Pet pet;
    private BufferedImage image;

    // Panel de imagen (se repinta al cargar async)
    private JPanel imagePanel;

    public PetPopup(Frame parent, Pet pet) {
        super(parent, true); // modal
        this.pet = pet;

        setUndecorated(true);
        setSize(Format.POPUP_WIDTH, Format.POPUP_HEIGHT);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0)); // transparente para sombra

        JPanel root = buildRoot();
        setContentPane(root);

        // Cargar imagen si existe
        if (pet.getPicture() != null && !pet.getPicture().isBlank()) {
            Format.loadImageAsync(pet.getPicture(), img -> {
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
        root.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

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

                // Clip redondeado solo en esquinas superiores
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

        // Badge de estado sobre la imagen
        String status = pet.getExtraInfo() != null && pet.getExtraInfo().getCurrentStatus() != null
                ? pet.getExtraInfo().getCurrentStatus() : pet.getStatus();
        JLabel badge = Format.buildStatusBadge(status != null ? status : "Available");
        badge.setBounds(12, 12, 140, 26);
        imagePanel.add(badge);

        // Botón cerrar (X)
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

    // ── Panel de info con scroll ──────────────────────────────────
    private JScrollPane buildScrollInfo() {
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Format.COLOR_BG);
        info.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));

        // Nombre / tipo
        info.add(label(pet.getPetType(), Format.FONT_TITLE, Format.COLOR_TEXT_PRIMARY));
        info.add(gap(4));

        // ── Sección: Datos básicos ────────────────────────────────
        info.add(sectionTitle("Información general"));
        info.add(row("📅 Fecha de nacimiento", pet.getBirthdate()));
        info.add(row("📧 Contacto",            pet.getEmail()));
        info.add(row("📆 Fecha pérdida",       pet.getDateLost()));
        info.add(row("📆 Fecha encontrado",    pet.getDateFound()));

        PetExtraInfo extra = pet.getExtraInfo();
        if (extra != null) {
            info.add(gap(4));
            info.add(sectionTitle("Detalles"));
            info.add(row("📏 Tamaño",    sizeLabel(extra.getSize())));
            info.add(row("💰 Recompensa", extra.getBountyCurrency() + " " + extra.getBounty()));

            MedicSheet medic = extra.getMedicInfo();
            if (medic != null) {
                info.add(gap(4));
                info.add(sectionTitle("Ficha médica"));
                info.add(row("⚡ Nivel de energía",    medic.getEnergyLevel()));
                info.add(row("✋ Facilidad de entren.", medic.getTrainingEase()));
                info.add(row("📝 Descripción",         medic.getAbandonmentDescription()));

                if (medic.getDiseases() != null && !medic.getDiseases().isEmpty()) {
                    info.add(row("🦠 Enfermedades", String.join(", ", medic.getDiseases())));
                }
                if (medic.getTreatments() != null && !medic.getTreatments().isEmpty()) {
                    info.add(row("💊 Tratamientos", String.join(", ", medic.getTreatments())));
                }
            }
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

    // ── Footer con botón adoptar ──────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        footer.setBackground(Format.COLOR_BG);
        footer.setBorder(Format.borderDivider());   // línea superior sutil

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
    //  EVENTO DE ADOPTAR
    // ─────────────────────────────────────────────────────────────

    /** Se ejecuta al presionar el botón "Adoptar". */
    protected void onAdoptClicked() {
        // TODO: implementar lógica de adopción
    }

    // ─────────────────────────────────────────────────────────────
    //  HELPERS DE UI
    // ─────────────────────────────────────────────────────────────

    /** Fila clave–valor alineada a la izquierda. */
    private JPanel row(String key, String value) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        row.setAlignmentX(LEFT_ALIGNMENT);

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

    /** Título de sección con línea divisora inferior. */
    private JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Format.FONT_SUBTITLE);
        lbl.setForeground(Format.COLOR_PRIMARY);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(Format.borderSection());
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return lbl;
    }

    /** Label genérico. */
    private JLabel label(String text, Font font, Color color) {
        JLabel lbl = new JLabel(nvl(text));
        lbl.setFont(font);
        lbl.setForeground(color);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    /** Espacio vertical. */
    private Component gap(int h) {
        return Box.createVerticalStrut(h);
    }

    private static String nvl(String s) {
        return (s != null && !s.isBlank()) ? s : "—";
    }

    private static String sizeLabel(int size) {
        if (size == 1) return "Pequeño";
        if (size == 2) return "Mediano";
        if (size >= 3) return "Grande";
        return "—";
    }
}
