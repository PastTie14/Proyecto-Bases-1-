package Components;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class mainWindow extends JFrame {

    private int idUser;
    private JPanel content;
    private JLabel placeholder;

    private PetGridPanel petGrid;
    private PetFormPanel petForm;
    private PetSearchPanel petSearch;

    public mainWindow(int idUser) {
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
        petGrid = new PetGridPanel();
        petForm = new PetFormPanel(idUser);
        petSearch = new PetSearchPanel();
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
                public String getName() { return "Consultas"; }
                public void show() { setContent(petSearch); }
            },
            createPlaceholderItem("Estadísticas", "📊  Estadísticas"),
            createPlaceholderItem("Lista Negra", "🚫  Lista Negra"),
            new MenuItem() {
                public String getName() { return "Adoptar"; }
                public void show() { setContent(petGrid); }
            },
            new MenuItem() {
                public String getName() { return "Da en adopción"; }
                public void show() { setContent(petForm); }
            },
            createPlaceholderItem("Donar", "💜  Donar"),
            createPlaceholderItem("Dar Rating", "⭐  Dar Rating"),
            createPlaceholderItem("Ver casas refugio", "🏡  Ver casas refugio")
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