package proyecto.bases;

import Components.Format;
import Components.mainWindow;
import Connect.DBConnection;
//import Components.mainWindow;

import javax.swing.*;
import java.awt.*;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.event.*;
import java.sql.ResultSet;


public class LoginPage {

    final JFrame frame;  

    private final JTextField     tfEmail = RegisterPage.buildTextField();
    private final JPasswordField tfPass  = RegisterPage.buildPasswordField();

    private final JButton btnIngresar  = RegisterPage.buildPrimaryButton("Iniciar sesión");
    private final JButton btnRegistrar = buildLinkButton("¿No tenés cuenta? Registrate");

    // ─────────────────────────────────────────────────────────────
    public LoginPage() {
        frame = new JFrame("Quiero un Peludo — Iniciar sesión");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(460, 440);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setContentPane(buildContent());

        wireEvents();
        frame.setVisible(true);
    }

    // ═════════════════════════════════════════════════════════════
    //  UI
    // ═════════════════════════════════════════════════════════════

    private JPanel buildContent() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(Format.COLOR_BG_SURFACE);
        root.add(buildCard());
        return root;
    }

    private JPanel buildCard() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                g2.setColor(Format.COLOR_SHADOW);
                g2.fillRoundRect(4, 6, getWidth() - 6, getHeight() - 6,
                        Format.RADIUS_CARD, Format.RADIUS_CARD);
                g2.setColor(Format.COLOR_BG);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2,
                        Format.RADIUS_CARD, Format.RADIUS_CARD);
                g2.setColor(Format.COLOR_DIVIDER);
                g2.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2,
                        Format.RADIUS_CARD, Format.RADIUS_CARD);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(380, 380));
        card.setBorder(BorderFactory.createEmptyBorder(32, 36, 28, 36));

        card.add(buildHeader(), BorderLayout.NORTH);
        card.add(buildForm(),   BorderLayout.CENTER);
        card.add(buildFooter(), BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        JLabel logo = centeredLabel("🐾", new Font("SansSerif", Font.PLAIN, 36));
        JLabel title = centeredLabel("Bienvenido de vuelta", Format.FONT_TITLE);
        title.setForeground(Format.COLOR_TEXT_PRIMARY);
        JLabel subtitle = centeredLabel("Iniciá sesión para continuar", Format.FONT_BODY_SMALL);
        subtitle.setForeground(Format.COLOR_TEXT_SECONDARY);

        header.add(logo);
        header.add(Box.createVerticalStrut(6));
        header.add(title);
        header.add(Box.createVerticalStrut(2));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(20));
        return header;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill    = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.gridx   = 0;

        gc.gridy = 0; gc.insets = new Insets(0, 0, 4, 0);
        form.add(fieldLabel("Email"), gc);
        gc.gridy = 1; gc.insets = new Insets(0, 0, 14, 0);
        form.add(tfEmail, gc);

        gc.gridy = 2; gc.insets = new Insets(0, 0, 4, 0);
        form.add(fieldLabel("Contraseña"), gc);
        gc.gridy = 3; gc.insets = new Insets(0, 0, 0, 0);
        form.add(tfPass, gc);

        return form;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setOpaque(false);

        btnIngresar.setAlignmentX(CENTER_ALIGNMENT);
        btnRegistrar.setAlignmentX(CENTER_ALIGNMENT);

        footer.add(Box.createVerticalStrut(20));
        footer.add(btnIngresar);
        footer.add(Box.createVerticalStrut(10));
        footer.add(btnRegistrar);
        return footer;
    }

    // ═════════════════════════════════════════════════════════════
    //  EVENTOS
    // ═════════════════════════════════════════════════════════════

    private void wireEvents() {
        btnIngresar.addActionListener(e  -> iniciarSesion());
        btnRegistrar.addActionListener(e -> abrirRegistro());
    }

    private void iniciarSesion() {
        String email = tfEmail.getText().trim();
        String pass  = new String(tfPass.getPassword()).trim();

        if (email.isBlank() || pass.isBlank()) {
            JOptionPane.showMessageDialog(frame,
                "Por favor completá el email y la contraseña.",
                "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Llama al stored procedure de login.
            // Se espera que devuelva una fila si las credenciales son correctas,
            // o un ResultSet vacío si no coinciden.
            int id = DBConnection.login(email, pass);

            if (id != 0 && id>=1) {
                frame.dispose();
                mainWindow win = new mainWindow(id);
                win.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame,
                    "Email o contraseña incorrectos.",
                    "Acceso denegado", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                "Error al conectarse. Revisá tu conexión.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirRegistro() {
        // Oculta el login (no lo cierra) y abre el registro
        frame.setVisible(false);
        new RegisterPage(frame);
    }

    // ═════════════════════════════════════════════════════════════
    //  HELPERS
    // ═════════════════════════════════════════════════════════════

    private static JButton buildLinkButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(Format.FONT_BODY_SMALL);
        btn.setForeground(Format.COLOR_PRIMARY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private static JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Format.FONT_BODY_SMALL);
        lbl.setForeground(Format.COLOR_TEXT_SECONDARY);
        return lbl;
    }

    private static JLabel centeredLabel(String text, Font font) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(font);
        lbl.setAlignmentX(CENTER_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, lbl.getPreferredSize().height + 4));
        return lbl;
    }

    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
