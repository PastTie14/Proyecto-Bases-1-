package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;


public class SideMenuPanel extends JPanel {

    // ── Paleta ────────────────────────────────────────────
    private static final Color BG          = Color.WHITE;
    private static final Color DIVIDER     = new Color(220, 220, 220);
    private static final Color TEXT_NORMAL = new Color(40,  40,  40);
    private static final Color TEXT_HOVER  = new Color(160,  0, 160);
    private static final Color HOVER_BG    = new Color(248, 235, 255);
    private static final Color ACTIVE_BAR  = new Color(160,  0, 160);
    private static final Color BTN_BG      = new Color(160,  0, 160);
    private static final Color BTN_FG      = Color.WHITE;

    // ── Tipografía ────────────────────────────────────────
    private static final Font FONT_ITEM    = new Font("SansSerif", Font.PLAIN, 16);
    private static final Font FONT_BTN     = new Font("SansSerif", Font.BOLD, 10);

    // ── Dimensiones ──────────────────────────────────────
    private static final int MAX_WIDTH     = 200;
    private static final int ITEM_HEIGHT   = 46;
    private static final int ACTIVE_BAR_W  = 3;

    // ── Animación ─────────────────────────────────────────
    private static final int ANIM_STEP     = 18;   // px por tick
    private static final int ANIM_DELAY    = 12;   // ms por tick (~80fps)

    private int     currentWidth = 0;
    private boolean isOpen       = false;
    private final Timer animTimer;

    // ── Estado ────────────────────────────────────────────
    private int activeIndex = -1;

    // ── Botón externo ─────────────────────────────────────
    private final JButton toggleButton;

    // ─────────────────────────────────────────────────────

    /**
     *
     * @param items
     */

    public SideMenuPanel(List<MenuItem> items) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(BG);
        setPreferredSize(new Dimension(0, 0));  // empieza cerrado
        setMinimumSize(new Dimension(0, 0));
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, DIVIDER));

        for (int i = 0; i < items.size(); i++) {
            add(buildRow(items.get(i), i, items.size()));
        }
        add(Box.createVerticalGlue());

        toggleButton = buildToggleButton();
        animTimer    = new Timer(ANIM_DELAY, e -> animationTick());
    }
    
    public JButton getToggleButton() {
        return toggleButton;
    }

    /** Abre o cierra el menú según su estado actual. */
    public void toggle() {
        if (isOpen) close(); else open();
    }

    /** Abre el menú con animación de derecha a izquierda. */
    public void open() {
        if (isOpen) return;
        isOpen = true;
        toggleButton.setText("✕");
        animTimer.start();
    }

    /** Cierra el menú con animación de izquierda a derecha. */
    public void close() {
        if (!isOpen) return;
        isOpen = false;
        toggleButton.setText("≡");
        animTimer.start();
    }

    /**
     * Marca el ítem en {@code index} como activo visualmente.
     * Útil para sincronizar el menú con la vista que ya está abierta.
     */
    public void setActiveIndex(int index) {
        activeIndex = index;
        repaint();
    }

    // ── Animación ─────────────────────────────────────────

    private void animationTick() {
        if (isOpen) {
            // Expandir → de 0 a MAX_WIDTH (desliza de derecha a izquierda)
            currentWidth = Math.min(currentWidth + ANIM_STEP, MAX_WIDTH);
        } else {
            // Contraer → de MAX_WIDTH a 0
            currentWidth = Math.max(currentWidth - ANIM_STEP, 0);
        }

        setPreferredSize(new Dimension(currentWidth, getHeight()));
        revalidate();
        repaint();

        if ((isOpen  && currentWidth >= MAX_WIDTH) ||
            (!isOpen && currentWidth <= 0)) {
            animTimer.stop();
        }
    }

    // ── Botón hamburguesa ─────────────────────────────────

    private JButton buildToggleButton() {
        JButton btn = new JButton("≡") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()
                        ? BTN_BG.darker()
                        : getModel().isRollover() ? BTN_BG.brighter() : BTN_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFont(FONT_BTN);
        btn.setForeground(BTN_FG);
        btn.setPreferredSize(new Dimension(42, 32));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> toggle());
        return btn;
    }

    // ── Filas del menú ────────────────────────────────────

    private JPanel buildRow(MenuItem item, int index, int total) {
        JPanel row = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (activeIndex == index) {
                    g.setColor(ACTIVE_BAR);
                    g.fillRect(0, 0, ACTIVE_BAR_W, getHeight());
                }
            }
        };
        row.setOpaque(true);
        row.setBackground(BG);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, ITEM_HEIGHT));
        row.setPreferredSize(new Dimension(MAX_WIDTH, ITEM_HEIGHT));
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(item.getName());
        label.setFont(FONT_ITEM);
        label.setForeground(TEXT_NORMAL);
        label.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        row.add(label, BorderLayout.CENTER);

        if (index < total - 1) {
            JSeparator sep = new JSeparator();
            sep.setForeground(DIVIDER);
            row.add(sep, BorderLayout.SOUTH);
        }

        row.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (activeIndex != index) {
                    row.setBackground(HOVER_BG);
                    label.setForeground(TEXT_HOVER);
                }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (activeIndex != index) {
                    row.setBackground(BG);
                    label.setForeground(TEXT_NORMAL);
                }
            }
            @Override public void mouseClicked(MouseEvent e) {
                activeIndex = index;
                row.setBackground(HOVER_BG);
                label.setForeground(TEXT_HOVER);
                repaint();
                item.show();
            }
        });

        return row;
    }
}
