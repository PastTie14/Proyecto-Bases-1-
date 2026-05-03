package Components;

import Pets.Pet;
import Pets.PetExtraInfo;
import Pets.MedicSheet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
public class SideMenuDemo2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Quiero un Peludo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            // ── Panel de contenido principal ──────────────
            JPanel content = new JPanel(new BorderLayout());
            content.setBackground(new Color(245, 245, 250));

            // Placeholder genérico
            JLabel placeholder = new JLabel("Selecciona una opción del menú",
                    SwingConstants.CENTER);
            placeholder.setFont(new Font("SansSerif", Font.PLAIN, 18));
            placeholder.setForeground(new Color(160, 160, 160));
            content.add(placeholder, BorderLayout.CENTER);

            // ── PetGridPanel con mascotas de prueba ───────
            PetGridPanel petGrid = new PetGridPanel(buildDemoPets());
            
            //PetForm para test
            PetFormPanel petForm = new PetFormPanel();

            // ── Ítems del menú ────────────────────────────
            List<MenuItem> items = Arrays.asList(

                new MenuItem() {
                    public String getName() { return "Consultas"; }
                    public void show() {
                        content.removeAll();
                        placeholder.setText("📋  Consultas");
                        content.add(placeholder, BorderLayout.CENTER);
                        content.revalidate();
                        content.repaint();
                    }
                },
                new MenuItem() {
                    public String getName() { return "Estadísticas"; }
                    public void show() {
                        content.removeAll();
                        placeholder.setText("📊  Estadísticas");
                        content.add(placeholder, BorderLayout.CENTER);
                        content.revalidate();
                        content.repaint();
                    }
                },
                new MenuItem() {
                    public String getName() { return "Lista Negra"; }
                    public void show() {
                        content.removeAll();
                        placeholder.setText("🚫  Lista Negra");
                        content.add(placeholder, BorderLayout.CENTER);
                        content.revalidate();
                        content.repaint();
                    }
                },
                new MenuItem() {
                    public String getName() { return "Adoptar"; }
                    public void show() {
                        // ── Reemplaza el contenido con el grid de mascotas ──
                        content.removeAll();
                        content.add(petGrid, BorderLayout.CENTER);
                        content.revalidate();
                        content.repaint();
                    }
                },
                new MenuItem() {
                    public String getName() { return "Da en adopción"; }
                    public void show() {
                        content.removeAll();
                        content.add(petForm);
                        content.revalidate();
                        content.repaint();
                    }
                },
                new MenuItem() {
                    public String getName() { return "Donar"; }
                    public void show() {
                        content.removeAll();
                        placeholder.setText("💜  Donar");
                        content.add(placeholder, BorderLayout.CENTER);
                        content.revalidate();
                        content.repaint();
                    }
                },
                new MenuItem() {
                    public String getName() { return "Dar Rating"; }
                    public void show() {
                        content.removeAll();
                        placeholder.setText("⭐  Dar Rating");
                        content.add(placeholder, BorderLayout.CENTER);
                        content.revalidate();
                        content.repaint();
                    }
                },
                new MenuItem() {
                    public String getName() { return "Ver casas refugio"; }
                    public void show() {
                        content.removeAll();
                        placeholder.setText("🏡  Ver casas refugio");
                        content.add(placeholder, BorderLayout.CENTER);
                        content.revalidate();
                        content.repaint();
                    }
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

            JLabel logo = new JLabel("  🐾  Quiero un Peludo");
            logo.setFont(new Font("SansSerif", Font.BOLD, 16));
            logo.setForeground(new Color(160, 0, 160));
            navbar.add(logo, BorderLayout.WEST);

            JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
            btnWrapper.setOpaque(false);
            btnWrapper.add(menu.getToggleButton());
            navbar.add(btnWrapper, BorderLayout.EAST);

            // ── Ensamblar ─────────────────────────────────
            frame.setLayout(new BorderLayout());
            frame.add(navbar,  BorderLayout.NORTH);
            frame.add(content, BorderLayout.CENTER);
            frame.add(menu,    BorderLayout.EAST);

            frame.setVisible(true);
        });
    }

    // ─────────────────────────────────────────────────────────────
    //  DATOS DE PRUEBA
    // ─────────────────────────────────────────────────────────────

    private static ArrayList<Pet> buildDemoPets() {
        ArrayList<Pet> list = new ArrayList<>();

        list.add(makePet(1, "Max",            "Golden Retriever", "Available",        "High",   "Easy",     300, "$"));
        list.add(makePet(2, "Luna",           "Tabby",            "Available",        "Medium", "Moderate", 150, "$"));
        list.add(makePet(3, "Charlie",        "Beagle",           "Available",        "High",   "Moderate", 350, "$"));
        list.add(makePet(4, "Mia",            "Siamese",          "Pending Adoption", "Low",    "Easy",     200, "$"));
        list.add(makePet(5, "Rocky",          "Labrador",         "Available",        "High",   "Easy",     280, "$"));
        list.add(makePet(6, "Bella",          "Persian",          "Adopted",          "Low",    "Easy",     180, "$"));
        list.add(makePet(7, "Toby",           "Poodle",           "Available",        "Medium", "Easy",     320, "$"));
        list.add(makePet(8, "Nala",           "Maine Coon",       "In Treatment",     "Medium", "Moderate", 220, "$"));
        list.add(makePet(9, "Zeus",           "German Shepherd",  "Available",        "High",   "Hard",     400, "$"));
        
        list.get(0).setPicture("https://misanimales.com/wp-content/uploads/2021/07/golden-retriever-valla-768x576.jpg");

        return list;
    }

    // Crea un Pet con PetExtraInfo y MedicSheet a partir de los datos básicos. 
    private static Pet makePet(int id, String name, String breed,
                               String status, String energy,
                               String training, int bounty, String currency) {
        // Pet usa petType como campo de nombre/raza en el demo
        Pet pet = new Pet(id, "2020-01-01", name + "@demo.com",
                          null, null, "admin", "2024-01-01", status, breed);

        MedicSheet medic = new MedicSheet();
        medic.setEnergyLevel(energy);
        medic.setTrainingEase(training);

        PetExtraInfo extra = new PetExtraInfo(id, 2);
        extra.setCurrentStatus(status);
        extra.setBounty(bounty);
        extra.setBountyCurrency(currency);
        extra.setMedicInfo(medic);

        pet.setExtraInfo(extra);
        return pet;
    }
}
*/