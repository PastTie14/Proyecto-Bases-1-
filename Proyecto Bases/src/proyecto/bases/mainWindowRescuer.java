package proyecto.bases;

import Panels.PetGridPanel;
import Panels.ConsultPanel;
import Components.MenuItem;
import Panels.MyPetsPanel;
import Panels.SideMenuPanel;
import Panels.StatsPanel;
import Panels.RescuerFormPanel;
import Panels.PetSearchPanel;
import Panels.PetFormPanel;
import Panels.RatingPanel;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class mainWindowRescuer extends JFrame {

    private int idUser;
    private JPanel content;
    private JLabel placeholder;

    private PetGridPanel petGrid;
    private PetFormPanel petForm;
    private PetSearchPanel petSearch;
    private ConsultPanel consult;
    private StatsPanel stats;
    private RescuerFormPanel config;
    private MyPetsPanel myPets;
    private RatingPanel rating;

    public mainWindowRescuer(int idUser) {
        this.idUser = idUser;
        initUI();
    }

    private void initUI() {
        setTitle("Quiero un Peludo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); 
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        initContent();
        initComponents();

        setVisible(true);
    }

    private void initContent() {
        content = new JPanel(new BorderLayout());
        content.setBackground(new Color(245, 245, 250));

        placeholder = new JLabel("Selecciona una opción del menú", SwingConstants.CENTER);
        placeholder.setFont(new Font("SansSerif", Font.PLAIN, 18));
        placeholder.setForeground(new Color(160, 160, 160));

        content.add(placeholder, BorderLayout.CENTER);
    }

    private void initComponents() {
        petGrid = new PetGridPanel(PetGridPanel.loadPetsByStatusId(1),idUser);
        petForm = new PetFormPanel(idUser);
        petSearch = new PetSearchPanel();
        stats = new StatsPanel();
        consult = new ConsultPanel();
        config = new RescuerFormPanel(idUser);
        rating = new RatingPanel(idUser);
        myPets = new MyPetsPanel(idUser);
        List<MenuItem> items = createMenuItems();

        SideMenuPanel menu = new SideMenuPanel(items);
        JPanel navbar = createNavbar(menu);

        add(navbar, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        add(menu, BorderLayout.EAST);
    }

    private JPanel createNavbar(SideMenuPanel menu) {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(Color.WHITE);
        navbar.setPreferredSize(new Dimension(0, 56));
        navbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
                new Color(220, 220, 220)));

        JLabel logo = new JLabel("  🐾  Quiero un Peludo");
        logo.setFont(new Font("SansSerif", Font.BOLD, 16));
        logo.setForeground(new Color(160, 0, 160));

        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        btnWrapper.setOpaque(false);
        btnWrapper.add(menu.getToggleButton());

        navbar.add(logo, BorderLayout.WEST);
        navbar.add(btnWrapper, BorderLayout.EAST);

        return navbar;
    }

    private List<MenuItem> createMenuItems() {
        return Arrays.asList(
            new MenuItem() {
                public String getName() { return "Buscar Mascota"; }
                public void show() { setContent(petSearch); }
            },
            new MenuItem(){
                public String getName() { return "Consultas"; }
                public void show() { setContent(consult); }
            },
            new MenuItem(){
                public String getName() { return "Estadisticas"; }
                public void show() { setContent(stats);}
            },
            new MenuItem() {
                public String getName() { return "Adoptar"; }
                public void show() { setContent(petGrid); }
            },
            new MenuItem() {
                public String getName() { return "Da en adopción"; }
                public void show() { setContent(petForm); }
            },
            new MenuItem() {
                public String getName() { return "Mis mascotas"; }
                public void show() { setContent(myPets); }
            },
            new MenuItem() {
                public String getName() { return "Puntajes"; }
                public void show() { setContent(rating); }
            },
            new MenuItem() {
                public String getName() { return "Mi cuenta"; }
                public void show() { setContent(config); }
            }
            
        );
    }

    private MenuItem createPlaceholderItem(String name, String text) {
        return new MenuItem() {
            public String getName() { return name; }
            public void show() {
                placeholder.setText(text);
                setContent(placeholder);
            }
        };
    }

    private void setContent(Component component) {
        content.removeAll();
        content.add(component, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }
}