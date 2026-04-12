package Components;

import Pets.Pet;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel con scroll que recibe un ArrayList de Pet y construye
 * una cuadrícula de PetCard automáticamente.
 *
 * Uso:
 * <pre>
 *   PetGridPanel grid = new PetGridPanel(listaDeMascotas);
 *   mainPanel.add(grid, BorderLayout.CENTER);
 * </pre>
 */
public class PetGridPanel extends JPanel {

    private static final int COLUMNS   = 3;
    private static final int H_GAP     = 20;
    private static final int V_GAP     = 20;
    private static final int PADDING   = 24;

    private final ArrayList<Pet>      pets;
    private final ArrayList<PetCard>  cards;
    private final JScrollPane         scrollPane;
    private final JPanel              grid;

    // ─────────────────────────────────────────────────────────────
    public PetGridPanel(ArrayList<Pet> pets) {
        this.pets  = pets;
        this.cards = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);

        // Panel interno con cuadrícula
        grid = new JPanel(new WrapLayout(FlowLayout.LEFT, H_GAP, V_GAP));
        grid.setBackground(Format.COLOR_BG);
        grid.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        buildCards();

        // ScrollPane que envuelve la cuadrícula
        scrollPane = new JScrollPane(grid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(Format.COLOR_BG);
        scrollPane.getViewport().setBackground(Format.COLOR_BG);

        add(scrollPane, BorderLayout.CENTER);
    }

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCCIÓN DE TARJETAS
    // ─────────────────────────────────────────────────────────────

    /** Limpia y reconstruye todas las PetCards desde la lista actual. */
    private void buildCards() {
        grid.removeAll();
        cards.clear();

        for (Pet pet : pets) {
            PetCard card = new PetCard(pet);
            cards.add(card);
            grid.add(card);
        }

        grid.revalidate();
        grid.repaint();
    }

    // ─────────────────────────────────────────────────────────────
    //  API PÚBLICA
    // ─────────────────────────────────────────────────────────────

    /**
     * Reemplaza la lista de mascotas y reconstruye las tarjetas.
     * Útil para filtros o búsquedas.
     */
    public void updatePets(ArrayList<Pet> newPets) {
        pets.clear();
        pets.addAll(newPets);
        buildCards();
    }

    /** Devuelve el JScrollPane por si se necesita configurar externamente. */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    /** Devuelve la lista de PetCards generadas. */
    public ArrayList<PetCard> getCards() {
        return cards;
    }


    // ═════════════════════════════════════════════════════════════
    //  INNER CLASS: WrapLayout
    //  FlowLayout que hace wrap correcto dentro de un JScrollPane.
    // ═════════════════════════════════════════════════════════════

    /**
     * Extensión de FlowLayout que recalcula el preferred size
     * según el ancho disponible, permitiendo wrap real dentro
     * de un JScrollPane sin scroll horizontal.
     */
    private static class WrapLayout extends FlowLayout {

        WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            return layoutSize(target, false);
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getWidth();
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;

                int hgap = getHgap();
                int vgap = getVgap();
                Insets insets = target.getInsets();
                int maxWidth = targetWidth - insets.left - insets.right;

                int width  = 0;
                int height = 0;
                int rowWidth  = 0;
                int rowHeight = 0;

                for (int i = 0; i < target.getComponentCount(); i++) {
                    Component c = target.getComponent(i);
                    if (!c.isVisible()) continue;

                    Dimension d = preferred ? c.getPreferredSize() : c.getMinimumSize();

                    if (rowWidth + d.width > maxWidth && rowWidth > 0) {
                        width   = Math.max(width, rowWidth);
                        height += rowHeight + vgap;
                        rowWidth  = 0;
                        rowHeight = 0;
                    }

                    rowWidth  += d.width + hgap;
                    rowHeight  = Math.max(rowHeight, d.height);
                }

                width   = Math.max(width, rowWidth);
                height += rowHeight;
                height += insets.top + insets.bottom + vgap * 2;

                return new Dimension(width, height);
            }
        }
    }
}
