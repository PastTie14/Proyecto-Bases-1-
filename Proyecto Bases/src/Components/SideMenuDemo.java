package Components;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Demo de SideMenuPanel con navbar y botón hamburguesa animado.
 * Ejecutar: javac *.java && java SideMenuDemo
 */
public class SideMenuDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Quiero un Peludo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);


            // ── Contenido principal ───────────────────────
            JPanel content = new JPanel(new BorderLayout());
            content.setBackground(new Color(245, 245, 250));
            JLabel placeholder = new JLabel("Selecciona una opción del menú",
                    SwingConstants.CENTER);
            placeholder.setFont(new Font("SansSerif", Font.PLAIN, 18));
            placeholder.setForeground(new Color(160, 160, 160));
            content.add(placeholder, BorderLayout.CENTER);

            // ── Ítems del menú ────────────────────────────
            List<MenuItem> items = Arrays.asList(

                new MenuItem() {
                    public String getName() { return "Consultas"; }
                    public void show() { placeholder.setText("📋  Consultas"); }
                },
                new MenuItem() {
                    public String getName() { return "Estadísticas"; }
                    public void show() { placeholder.setText("📊  Estadísticas"); }
                },
                new MenuItem() {
                    public String getName() { return "Lista Negra"; }
                    public void show() { placeholder.setText("🚫  Lista Negra"); }
                },
                new MenuItem() {
                    public String getName() { return "Adoptar"; }
                    public void show() { placeholder.setText("🐾  Adoptar"); }
                },
                new MenuItem() {
                    public String getName() { return "Da en adopción"; }
                    public void show() { placeholder.setText("🏠  Da en adopción"); }
                },
                new MenuItem() {
                    public String getName() { return "Donar"; }
                    public void show() { placeholder.setText("💜  Donar"); }
                },
                new MenuItem() {
                    public String getName() { return "Dar Rating"; }
                    public void show() { placeholder.setText("⭐  Dar Rating"); }
                },
                new MenuItem() {
                    public String getName() { return "Ver casas refugio"; }
                    public void show() { placeholder.setText("🏡  Ver casas refugio"); }
                }
            );

            // ── Menú lateral ──────────────────────────────
            SideMenuPanel menu = new SideMenuPanel(items);

            // ── Navbar superior ───────────────────────────
            JPanel navbar = new JPanel(new BorderLayout());
            navbar.setBackground(Color.WHITE);
            navbar.setPreferredSize(new Dimension(0, 56));
            navbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
                    new Color(220, 220, 220)));

            // Logo / título
            JLabel logo = new JLabel("  🐾  Quiero un Peludo");
            logo.setFont(new Font("SansSerif", Font.BOLD, 16));
            logo.setForeground(new Color(160, 0, 160));
            navbar.add(logo, BorderLayout.WEST);

            // Botón hamburguesa a la derecha
            JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
            btnWrapper.setOpaque(false);
            btnWrapper.add(menu.getToggleButton());   // <── aquí se obtiene el botón
            navbar.add(btnWrapper, BorderLayout.EAST);

            // ── Ensamblar ─────────────────────────────────
            frame.setLayout(new BorderLayout());
            frame.add(navbar,  BorderLayout.NORTH);
            frame.add(content, BorderLayout.CENTER);
            frame.add(menu,    BorderLayout.EAST);   // empieza con ancho 0

            frame.setVisible(true);
        });
    }
}
