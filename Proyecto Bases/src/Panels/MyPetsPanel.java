package Panels;

import Components.Format;
import Components.MyPetPopup;
import Components.PetCard;
import TablesObj.Pet;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MyPetsPanel extends JPanel {

    private static final Logger LOG     = Logger.getLogger(MyPetsPanel.class.getName());
    private static final int    H_GAP   = 20;
    private static final int    V_GAP   = 20;
    private static final int    PADDING = 24;

    private final int                idUser;
    private final ArrayList<Pet>     pets  = new ArrayList<>();
    private final ArrayList<PetCard> cards = new ArrayList<>();

    private final JPanel      grid;
    private final JScrollPane scrollPane;

    // ── Header ────────────────────────────────────────────────────

    private JLabel titleLabel;

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────

    public MyPetsPanel(int idUser) {
        this.idUser = idUser;

        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);

        // ── Header ────────────────────────────────────────────────
        add(buildHeader(), BorderLayout.NORTH);

        // ── Grid ─────────────────────────────────────────────────
        grid = new JPanel(new WrapLayout(FlowLayout.LEFT, H_GAP, V_GAP));
        grid.setBackground(Format.COLOR_BG);
        grid.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        scrollPane = new JScrollPane(grid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(Format.COLOR_BG);
        scrollPane.getViewport().setBackground(Format.COLOR_BG);

        add(scrollPane, BorderLayout.CENTER);

        // Carga inicial
        reload();
    }

    // ─────────────────────────────────────────────────────────────
    //  HEADER
    // ─────────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(Format.COLOR_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                Format.borderDivider(),
                BorderFactory.createEmptyBorder(14, PADDING, 14, PADDING)));

        titleLabel = new JLabel("Mis mascotas");
        titleLabel.setFont(Format.FONT_SUBTITLE);
        titleLabel.setForeground(Format.COLOR_TEXT_PRIMARY);
        header.add(titleLabel, BorderLayout.WEST);

        // Botón recargar
        JButton reloadBtn = buildIconButton("⟳  Recargar");
        reloadBtn.addActionListener(e -> reload());
        header.add(reloadBtn, BorderLayout.EAST);

        return header;
    }

    private JButton buildIconButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                g2.setColor(getModel().isPressed()
                        ? Format.COLOR_PRIMARY.darker()
                        : Format.COLOR_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                        Format.RADIUS_BTN, Format.RADIUS_BTN);
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
        btn.setPreferredSize(new Dimension(120, 32));
        return btn;
    }

    // ─────────────────────────────────────────────────────────────
    //  CARGA DESDE BD
    // ─────────────────────────────────────────────────────────────

    private ArrayList<Pet> loadMyPets() {
        ArrayList<Pet> list = new ArrayList<>();
        try {
            ResultSet rs = Pet.getByRescuer(idUser);
            if (rs == null) return list;
            while (rs.next()) {
                list.add(new Pet(rs.getInt(1)));
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al cargar mascotas del rescatista id=" + idUser, ex);
        }
        return list;
    }


    private void buildCards() {
        grid.removeAll();
        cards.clear();

        if (pets.isEmpty()) {
            grid.add(buildEmptyState());
        } else {
            for (Pet pet : pets) {
                // Tarjeta especial: abre MyPetPopup en lugar del popup genérico
                PetCard card = new PetCard(pet, idUser) {
                    @Override
                    protected void onCardClicked() {
                        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
                        new MyPetPopup(parent, pet, idUser, MyPetsPanel.this).setVisible(true);
                    }
                };
                cards.add(card);
                grid.add(card);
            }
        }

        updateTitle();
        grid.revalidate();
        grid.repaint();
    }

    /** Panel de estado vacío cuando el rescatista no tiene mascotas. */
    private JPanel buildEmptyState() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        JLabel icon = new JLabel("🐾");
        icon.setFont(icon.getFont().deriveFont(48f));
        icon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel msg = new JLabel("Aún no tienes mascotas registradas");
        msg.setFont(Format.FONT_SUBTITLE);
        msg.setForeground(Format.COLOR_TEXT_SECONDARY);
        msg.setAlignmentX(CENTER_ALIGNMENT);

        p.add(icon);
        p.add(Box.createVerticalStrut(12));
        p.add(msg);
        return p;
    }

    private void updateTitle() {
        int n = pets.size();
        titleLabel.setText("Mis mascotas" + (n > 0 ? "  (" + n + ")" : ""));
    }

    public void reload() {
        pets.clear();
        pets.addAll(loadMyPets());
        buildCards();
    }

    public ArrayList<PetCard> getCards()     { return cards; }
    public JScrollPane        getScrollPane(){ return scrollPane; }

    private static class WrapLayout extends FlowLayout {

        WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }

        @Override public Dimension preferredLayoutSize(Container t) { return layoutSize(t, true);  }
        @Override public Dimension minimumLayoutSize (Container t)  { return layoutSize(t, false); }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getWidth();
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;

                Insets ins     = target.getInsets();
                int maxWidth   = targetWidth - ins.left - ins.right;
                int width = 0, height = 0, rowWidth = 0, rowHeight = 0;

                for (int i = 0; i < target.getComponentCount(); i++) {
                    Component c = target.getComponent(i);
                    if (!c.isVisible()) continue;
                    Dimension d = preferred ? c.getPreferredSize() : c.getMinimumSize();

                    if (rowWidth + d.width > maxWidth && rowWidth > 0) {
                        width   = Math.max(width, rowWidth);
                        height += rowHeight + getVgap();
                        rowWidth = 0; rowHeight = 0;
                    }
                    rowWidth  += d.width + getHgap();
                    rowHeight  = Math.max(rowHeight, d.height);
                }

                width   = Math.max(width, rowWidth);
                height += rowHeight + ins.top + ins.bottom + getVgap() * 2;
                return new Dimension(width, height);
            }
        }
    }
}
