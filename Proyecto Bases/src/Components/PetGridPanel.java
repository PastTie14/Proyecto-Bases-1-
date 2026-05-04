package Components;
 
import TablesObj.Pet;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class PetGridPanel extends JPanel {
 
    private static final Logger LOG    = Logger.getLogger(PetGridPanel.class.getName());
    private static final int    H_GAP  = 20;
    private static final int    V_GAP  = 20;
    private static final int    PADDING = 24;
 
    private final ArrayList<Pet>     pets;
    private final ArrayList<PetCard> cards;
    private final JScrollPane        scrollPane;
    private final JPanel             grid;
 
    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTORES
    // ─────────────────────────────────────────────────────────────
 
    /**
     * Constructor sin argumentos: carga todas las mascotas desde la BD 
     */
    public PetGridPanel() {
        this(loadFromDB());
    }
 
    /**
     * Constructor con lista externa.
     * Útil cuando los datos ya están filtrados.
     */
    public PetGridPanel(ArrayList<Pet> pets) {
        this.pets  = new ArrayList<>(pets);
        this.cards = new ArrayList<>();
 
        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);
 
        grid = new JPanel(new WrapLayout(FlowLayout.LEFT, H_GAP, V_GAP));
        grid.setBackground(Format.COLOR_BG);
        grid.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
 
        buildCards();
 
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
    //  CARGA DESDE BD
    // ─────────────────────────────────────────────────────────────
 
    /**
     * Itera el ResultSet de getAllPets() y construye un Pet(id) por fila.
     * La primera columna debe ser id_pet.
     */
    private static ArrayList<Pet> loadFromDB() {
        ArrayList<Pet> list = new ArrayList<>();
        try {
            //
            ResultSet rs = Pet.getAllPetsByStatus(1); //Aqui se puede llamar el getPetsByStatus para obtener por ejemplo solo las que estan perdidas o en adopcion
            if (rs == null) return list;
            while (rs.next()) {
                int id = rs.getInt(1); // columna 0 → id_pet
                list.add(new Pet(id));
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al cargar mascotas desde la BD", ex);
        }
        return list;
    }
 
    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCCIÓN DE TARJETAS
    // ─────────────────────────────────────────────────────────────
 
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
 
    /** Recarga las mascotas desde la BD y reconstruye las tarjetas. */
    public void reload() {
        updatePets(loadFromDB());
    }
 
    /** Reemplaza la lista y reconstruye las tarjetas. */
    public void updatePets(ArrayList<Pet> newPets) {
        pets.clear();
        pets.addAll(newPets);
        buildCards();
    }
 
    public JScrollPane      getScrollPane() { return scrollPane; }
    public ArrayList<PetCard> getCards()    { return cards; }
 
    // ═════════════════════════════════════════════════════════════
    //  INNER CLASS: WrapLayout
    // ═════════════════════════════════════════════════════════════
 
    private static class WrapLayout extends FlowLayout {
 
        WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }
 
        @Override public Dimension preferredLayoutSize(Container target) { return layoutSize(target, true);  }
        @Override public Dimension minimumLayoutSize (Container target)  { return layoutSize(target, false); }
 
        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getWidth();
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
 
                Insets insets  = target.getInsets();
                int maxWidth   = targetWidth - insets.left - insets.right;
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
                height += rowHeight + insets.top + insets.bottom + getVgap() * 2;
                return new Dimension(width, height);
            }
        }
    }
}