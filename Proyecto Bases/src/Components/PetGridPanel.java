package Components;
 
import TablesObj.Pet;
import TablesObj.Status;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class PetGridPanel extends JPanel {
 
    private static final Logger LOG     = Logger.getLogger(PetGridPanel.class.getName());
    private static final int    H_GAP   = 20;
    private static final int    V_GAP   = 20;
    private static final int    PADDING = 24;
 
    /** Sentinel para la opción "All" en el combo. */
    private static final int ALL_STATUS_ID = -1;
 
    private final ArrayList<Pet>     pets;
    private final ArrayList<PetCard> cards;
    private final JScrollPane        scrollPane;
    private final JPanel             grid;
 
    /** Mapa ordenado: label visible → id_status (-1 = All). */
    private final LinkedHashMap<String, Integer> statusMap = new LinkedHashMap<>();
    private final FormComboBox statusCombo;
 
    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTORES
    // ─────────────────────────────────────────────────────────────
 
    /**
     * Constructor sin argumentos: carga todas las mascotas desde la BD.
     */
    public PetGridPanel() {
        this(loadPetsByStatusId(1)); // carga inicial con status 1
    }
 
    
    public PetGridPanel(ArrayList<Pet> pets) {
        this.pets  = new ArrayList<>(pets);
        this.cards = new ArrayList<>();
 
        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);
 
        // ── Panel de filtros ──────────────────────────────────────
        statusCombo = buildStatusCombo();
        JPanel filterBar = buildFilterBar(statusCombo);
        add(filterBar, BorderLayout.NORTH);
 
        // ── Grid de tarjetas ──────────────────────────────────────
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
    //  CONSTRUCCIÓN DEL COMBO DE ESTADOS
    // ─────────────────────────────────────────────────────────────
 
    
    private FormComboBox buildStatusCombo() {
        statusMap.clear();
        statusMap.put("All", ALL_STATUS_ID);
 
        try {
            ResultSet rs = Status.getAll();
            while (rs != null && rs.next()) {
                int    id   = rs.getInt(1);    // id_status
                String type = rs.getString(2); // status_type
                if (type != null) statusMap.put(type, id);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error cargando estados para el combo", ex);
        }
 
        FormComboBox combo = new FormComboBox("Filter by status");
        combo.setOptions(new ArrayList<>(statusMap.keySet()));
 
        // Seleccionar el primer status real (índice 1) si existe, si no "All"
        if (statusMap.size() > 1) {
            combo.getCombo().setSelectedIndex(1);
        }
 
        // Listener: recarga mascotas al cambiar selección
        combo.getCombo().addActionListener(e -> onStatusChanged(combo.getSelected()));
 
        return combo;
    }
 
    /**
     * Construye el panel horizontal de filtros que va al NORTH del layout.
     */
    private JPanel buildFilterBar(FormComboBox combo) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, PADDING, 10));
        bar.setBackground(Format.COLOR_BG);
        bar.setBorder(Format.borderDivider());
 
        // Limitar el ancho del combo para que no ocupe toda la barra
        combo.setMaximumSize(new Dimension(200, 58));
        combo.setPreferredSize(new Dimension(200, 58));
 
        bar.add(combo);
        return bar;
    }
 
    /**
     * Se ejecuta cada vez que el usuario cambia el estado en el combo.
     */
    private void onStatusChanged(String selectedLabel) {
        if (selectedLabel == null) return;
        Integer statusId = statusMap.get(selectedLabel);
        if (statusId == null) return;
 
        if (statusId == ALL_STATUS_ID) {
            updatePets(loadAllPets());
        } else {
            updatePets(loadPetsByStatusId(statusId));
        }
    }
 
    // ─────────────────────────────────────────────────────────────
    //  CARGA DESDE BD
    // ─────────────────────────────────────────────────────────────
 

    private static ArrayList<Pet> loadPetsByStatusId(int statusId) {
        ArrayList<Pet> list = new ArrayList<>();
        try {
            ResultSet rs = Pet.getAllPetsByStatus(statusId);
            if (rs == null) return list;
            while (rs.next()) {
                list.add(new Pet(rs.getInt(1))); // columna 1 → id_pet
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al cargar mascotas por status id=" + statusId, ex);
        }
        return list;
    }
 
    /**
     * Carga todas las mascotas sin filtro de estado.
     * Usado cuando el usuario selecciona "All".
     */
    private static ArrayList<Pet> loadAllPets() {
        ArrayList<Pet> list = new ArrayList<>();
        try {
            ResultSet rs = Pet.getAllPets(); // asume que este método existe en Pet
            if (rs == null) return list;
            while (rs.next()) {
                list.add(new Pet(rs.getInt(1)));
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al cargar todas las mascotas", ex);
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
 
    /** Recarga las mascotas respetando el filtro actualmente seleccionado. */
    public void reload() {
        onStatusChanged(statusCombo.getSelected());
    }
 
    /** Reemplaza la lista y reconstruye las tarjetas. */
    public void updatePets(ArrayList<Pet> newPets) {
        pets.clear();
        pets.addAll(newPets);
        buildCards();
    }
 
    public JScrollPane        getScrollPane()  { return scrollPane; }
    public ArrayList<PetCard> getCards()       { return cards; }
    public FormComboBox       getStatusCombo() { return statusCombo; }
 
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