package proyecto.bases;

import Components.Format;
import Connect.DBConnection;

import javax.swing.*;
import java.awt.*;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Página de registro de usuarios.
 *
 * Flujo:
 *   - Al registrar con éxito  → cierra esta ventana y muestra el LoginPage.
 *   - El botón "Ya tengo cuenta" → cierra esta ventana y muestra el LoginPage.
 */
public class RegisterPage {

    private final JFrame    frame;
    private final JFrame    loginFrame;   // referencia al Login para volver a él

    private final JTextField tfNombre = buildTextField();
    private final JPasswordField tfPass  = buildPasswordField();
    private final JTextField tfEmail  = buildTextField();

    private final JButton btnRegistrar  = buildPrimaryButton("Registrarse");
    private final JButton btnIrALogin   = buildLinkButton("¿Ya tenés cuenta? Iniciá sesión");

    // ─────────────────────────────────────────────────────────────
    public RegisterPage(JFrame loginFrame) {
        this.loginFrame = loginFrame;

        frame = new JFrame("Quiero un Peludo — Registro");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(460, 560);
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
        // La tarjeta usa BorderLayout:
        //   NORTH  → encabezado (logo + título)
        //   CENTER → formulario (GridBagLayout)
        //   SOUTH  → botones
        JPanel card = new JPanel(new BorderLayout(0, 0)) {
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
        card.setPreferredSize(new Dimension(380, 480));
        card.setBorder(BorderFactory.createEmptyBorder(32, 36, 28, 36));

        card.add(buildHeader(), BorderLayout.NORTH);
        card.add(buildForm(),   BorderLayout.CENTER);
        card.add(buildFooter(), BorderLayout.SOUTH);
        return card;
    }

    /** Logo + título + subtítulo, todos centrados. */
    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        JLabel logo = centeredLabel("🐾", new Font("SansSerif", Font.PLAIN, 36));
        JLabel title = centeredLabel("Crear cuenta", Format.FONT_TITLE);
        title.setForeground(Format.COLOR_TEXT_PRIMARY);
        JLabel subtitle = centeredLabel("Únete a Quiero un Peludo", Format.FONT_BODY_SMALL);
        subtitle.setForeground(Format.COLOR_TEXT_SECONDARY);

        header.add(logo);
        header.add(Box.createVerticalStrut(6));
        header.add(title);
        header.add(Box.createVerticalStrut(2));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(20));
        return header;
    }

    /**
     * Formulario con GridBagLayout — garantiza que labels y campos
     * estén perfectamente alineados y ocupen todo el ancho disponible.
     */
    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill      = GridBagConstraints.HORIZONTAL;
        gc.weightx   = 1.0;
        gc.gridx     = 0;
        gc.insets    = new Insets(0, 0, 4, 0);

        // Nombre
        gc.gridy = 0; form.add(fieldLabel("Nombre de usuario"), gc);
        gc.gridy = 1; gc.insets = new Insets(0, 0, 14, 0);
        form.add(tfNombre, gc);

        // Contraseña
        gc.gridy = 2; gc.insets = new Insets(0, 0, 4, 0);
        form.add(fieldLabel("Contraseña"), gc);
        gc.gridy = 3; gc.insets = new Insets(0, 0, 14, 0);
        form.add(tfPass, gc);

        // Email
        gc.gridy = 4; gc.insets = new Insets(0, 0, 4, 0);
        form.add(fieldLabel("Email"), gc);
        gc.gridy = 5; gc.insets = new Insets(0, 0, 0, 0);
        form.add(tfEmail, gc);

        return form;
    }

    /** Botones al pie de la tarjeta. */
    private JPanel buildFooter() {
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setOpaque(false);

        btnRegistrar.setAlignmentX(CENTER_ALIGNMENT);
        btnIrALogin.setAlignmentX(CENTER_ALIGNMENT);

        footer.add(Box.createVerticalStrut(20));
        footer.add(btnRegistrar);
        footer.add(Box.createVerticalStrut(10));
        footer.add(btnIrALogin);
        return footer;
    }

    // ═════════════════════════════════════════════════════════════
    //  EVENTOS
    // ═════════════════════════════════════════════════════════════

    private void wireEvents() {
        btnRegistrar.addActionListener(e -> registrar());
        btnIrALogin.addActionListener(e -> volverALogin());

        // Cerrar la ventana también vuelve al login
        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { mostrarLogin(); }
        });
    }

    private void registrar() {
        String nombre = tfNombre.getText().trim();
        String pass   = new String(tfPass.getPassword()).trim();
        String email  = tfEmail.getText().trim();

        if (nombre.isBlank() || pass.isBlank() || email.isBlank()) {
            JOptionPane.showMessageDialog(frame,
                "Por favor completá todos los campos.",
                "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String fecha = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            DBConnection.insertUser(nombre, pass, email, fecha, "User", fecha);

            JOptionPane.showMessageDialog(frame,
                "Cuenta creada correctamente. Podés iniciar sesión.",
                "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

            volverALogin();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                "Error al registrar el usuario.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverALogin() {
        frame.removeWindowListener(frame.getWindowListeners()[0]); // evita doble disparo
        frame.dispose();
        mostrarLogin();
    }

    private void mostrarLogin() {
        if (loginFrame != null) {
            loginFrame.setVisible(true);
            loginFrame.toFront();
        }
    }

    // ═════════════════════════════════════════════════════════════
    //  HELPERS DE ESTILO
    // ═════════════════════════════════════════════════════════════

    static JTextField buildTextField() {
        JTextField tf = new JTextField();
        styleInputBase(tf);
        return tf;
    }

    static JPasswordField buildPasswordField() {
        JPasswordField pf = new JPasswordField();
        styleInputBase(pf);
        return pf;
    }

    private static void styleInputBase(JTextField tf) {
        tf.setFont(Format.FONT_BODY);
        tf.setForeground(Format.COLOR_TEXT_PRIMARY);
        tf.setBackground(Format.COLOR_BG);
        tf.setPreferredSize(new Dimension(0, 38));
        applyFieldBorder(tf, false);

        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { applyFieldBorder(tf, true);  }
            @Override public void focusLost  (FocusEvent e) { applyFieldBorder(tf, false); }
        });
    }

    private static void applyFieldBorder(JTextField tf, boolean focused) {
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                focused ? Format.COLOR_PRIMARY : Format.COLOR_DIVIDER, 1, true),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
    }

    static JButton buildPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                g2.setColor(getModel().isPressed() || getModel().isRollover()
                        ? Format.COLOR_PRIMARY_DARK : Format.COLOR_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                        Format.RADIUS_BTN, Format.RADIUS_BTN);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(Format.FONT_BODY);
        btn.setForeground(Format.COLOR_TEXT_ON_PRIMARY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        return btn;
    }

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

    /** Label centrado horizontalmente dentro de un BoxLayout Y_AXIS. */
    private static JLabel centeredLabel(String text, Font font) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(font);
        lbl.setAlignmentX(CENTER_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, lbl.getPreferredSize().height + 4));
        return lbl;
    }
}