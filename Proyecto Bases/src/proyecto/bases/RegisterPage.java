package proyecto.bases;

import Components.Format;
import Connect.DBConnection;

import javax.swing.*;
import java.awt.*;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Página de registro de usuarios.
 *
 * Flujo:
 *   - Al registrarse exitosamente cierra esta ventana y muestra el LoginPage.
 *   - El botón "Ya tengo cuenta" cierra esta ventana y muestra el LoginPage.
 */
public class RegisterPage {

    private final JFrame    frame;
    private final JFrame    loginFrame;   // referencia al Login para volver a él

    private final JTextField tfName = buildTextField();
    private final JTextField tfFirstName = buildTextField();
    private final JTextField tfSecondName = buildTextField();
    private final JTextField tfFirstSurname = buildTextField();
    private final JTextField tfSecondSurname = buildTextField();
    
    private final JCheckBox donationsCheckBox = buildCheckBox("Requires donations");
    private final JPasswordField tfPass  = buildPasswordField();
    private final JTextField tfEmail  = buildTextField();

    private final JButton btnRegistrar  = buildPrimaryButton("Registrarse");
    private final JButton btnIrALogin   = buildLinkButton("¿Ya tenés cuenta? Iniciá sesión");
    
    private final String[] userTypes = {"Adopter", "Association", "Crib house", "Rescuer"};
    private final JComboBox comboBoxUsers = buildComboBox(userTypes);
    
    private JPanel form;
    private GridBagConstraints gc;
    private List<JCheckBox> sizeList = new ArrayList<>();
    private JPanel sizesPanel;

    // ─────────────────────────────────────────────────────────────
    public RegisterPage(JFrame loginFrame) {
        this.loginFrame = loginFrame;

        frame = new JFrame("Quiero un Peludo — Registro");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(660, 900);
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
        //   NORTH: encabezado (logo + título)
        //   CENTER: formulario (GridBagLayout)
        //   SOUTH: botones
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
        card.setPreferredSize(new Dimension(680, 600));
        card.setBorder(BorderFactory.createEmptyBorder(100, 40, 100, 40));

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
        form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        gc = new GridBagConstraints();
        gc.fill      = GridBagConstraints.HORIZONTAL;
        gc.weightx   = 1.0;
        gc.gridx     = 0;
        gc.insets    = new Insets(0, 0, 4, 0);

        // User types
        gc.gridy = 0; gc.insets = new Insets(0, 0, 4, 0);
        form.add(fieldLabel("User Type"), gc);
        gc.gridy = 1; gc.insets = new Insets(0, 0, 4, 0);
        form.add(comboBoxUsers, gc);
        
        /*
        // Nombre
        gc.gridy = 2; form.add(fieldLabel("Nombre de usuario"), gc);
        gc.gridy = 3; gc.insets = new Insets(0, 0, 14, 0);
        form.add(tfNombre, gc);
        
        
        // Email
        gc.gridy = 2; gc.insets = new Insets(0, 0, 4, 0);
        form.add(fieldLabel("Email"), gc);
        gc.gridy = 3; gc.insets = new Insets(0, 0, 0, 0);
        form.add(tfEmail, gc);
        
        // Contraseña
        gc.gridy = 4; gc.insets = new Insets(0, 0, 4, 0);
        form.add(fieldLabel("Contraseña"), gc);
        gc.gridy = 5; gc.insets = new Insets(0, 0, 14, 0);
        form.add(tfPass, gc);
*/
        updateFormFields();
        
        return form;
    }
    
    
    // Method for updating all the form fields depending on the user type
    private void updateFormFields() {
        // first, a list for the fixed components (the user type label and comboBox) is made
        List<Component> fixedComponents = new ArrayList<>();
        // next, all components in the form are saved in the list
        for (int i = 0; i < form.getComponentCount(); i++) {
            Component comp = form.getComponent(i);
            GridBagConstraints compGc = ((GridBagLayout) form.getLayout()).getConstraints(comp);
            
            if (compGc.gridy <= 1)
                fixedComponents.add(comp);
        }
        // everything is removed
        form.removeAll();
        
        // and then the fixed components are reconstructed
        for (Component comp : fixedComponents)
            form.add(comp);
        
        // the current amount of components 
        int current = 2;
        
        // depending on what user type is selected, different components are shown
        if (comboBoxUsers.getSelectedItem().equals("Adopter") || comboBoxUsers.getSelectedItem().equals("Rescuer")) {
            addFormField(current, "First name", tfFirstName, true);
            current+= 2;
            
            addFormField(current, "Second name", tfSecondName, true);
            current+= 2;
            
            addFormField(current, "First surname", tfFirstSurname, true);
            current+= 2;
            
            addFormField(current, "Second surname", tfSecondSurname, true);   
            current+= 2;
        }
        
        if (comboBoxUsers.getSelectedItem().equals("Association")) {
            addFormField(current, "Name", tfName, true);
            current+= 2;
        }
        
        if (comboBoxUsers.getSelectedItem().equals("Crib house")) {
            addFormField(current, "Name", tfName, true);
            current+= 2;
            
            addFormField(current, "Requires donations", donationsCheckBox, true);
            current+= 2;
            
            addSizesSection(current);
            current+= 2;
        }
        
        // adds the email and password text fields
        addFormField(current, "Email", tfEmail, true);   
        current+= 2;
            
        addFormField(current, "Password", tfPass, true);
        
        // updates the window
        form.revalidate();
        form.repaint();
    }
    
    // Método para agregar la sección de tamaños
    private void addSizesSection(int gridY) {
        // Label para la sección
        GridBagConstraints labelGc = new GridBagConstraints();
        labelGc.fill = GridBagConstraints.HORIZONTAL;
        labelGc.weightx = 1.0;
        labelGc.gridx = 0;
        labelGc.gridy = gridY;
        labelGc.insets = new Insets(0, 0, 4, 0);
        form.add(fieldLabel("Accepted pet sizes:"), labelGc);
        
        // Panel para los checkboxes
        sizesPanel = new JPanel();
        sizesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        sizesPanel.setOpaque(false);
        sizesPanel.setBackground(Format.COLOR_BG);
        sizesPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Format.COLOR_DIVIDER, 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        // Cargar tamaños desde la base de datos
        loadSizesFromDatabase();
        
        // Agregar el panel al formulario
        GridBagConstraints fieldGc = new GridBagConstraints();
        fieldGc.fill = GridBagConstraints.HORIZONTAL;
        fieldGc.weightx = 1.0;
        fieldGc.gridx = 0;
        fieldGc.gridy = gridY + 1;
        fieldGc.insets = new Insets(0, 0, 14, 0);
        form.add(sizesPanel, fieldGc);
    }
    
    // Método para cargar tamaños desde la base de datos
    private void loadSizesFromDatabase() {
        sizeList.clear();
        sizesPanel.removeAll();
        
        try (ResultSet rs = DBConnection.getSizes()) {
            while (rs.next()) {
                int id = rs.getInt("id_size");
                String name = rs.getString("name");
                
                JCheckBox checkBox = new JCheckBox(name);
                checkBox.setFont(Format.FONT_BODY_SMALL);
                checkBox.setForeground(Format.COLOR_TEXT_PRIMARY);
                checkBox.setOpaque(false);
                checkBox.putClientProperty("id_size", id); // Guardar el ID
                
                sizeList.add(checkBox);
                sizesPanel.add(checkBox);
            }
            
            // Si no hay tamaños, mostrar un mensaje
            if (sizeList.isEmpty()) {
                JLabel emptyLabel = new JLabel("No hay tamaños disponibles");
                emptyLabel.setFont(Format.FONT_BODY_SMALL);
                emptyLabel.setForeground(Format.COLOR_TEXT_SECONDARY);
                sizesPanel.add(emptyLabel);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error al cargar tamaños");
            errorLabel.setFont(Format.FONT_BODY_SMALL);
            errorLabel.setForeground(Color.RED);
            sizesPanel.add(errorLabel);
        }
        
        sizesPanel.revalidate();
        sizesPanel.repaint();
    }
    
    // Método para obtener los tamaños seleccionados
    private List<Integer> getSelectedSizes() {
        List<Integer> selectedIds = new ArrayList<>();
        for (JCheckBox checkBox : sizeList) {
            if (checkBox.isSelected()) {
                Integer id = (Integer) checkBox.getClientProperty("id_size");
                selectedIds.add(id);
            }
        }
        return selectedIds;
    }

    /*
        Adds form components
    */
    private void addFormField(int gridY, String labelText, JComponent field, boolean addBottomMargin) {
        // The label
        GridBagConstraints labelGc = new GridBagConstraints();
        labelGc.fill = GridBagConstraints.HORIZONTAL;
        labelGc.weightx = 1.0;
        labelGc.gridx = 0;
        labelGc.gridy = gridY;
        labelGc.insets = new Insets(0, 0, 4, 0);
        form.add(fieldLabel(labelText), labelGc);

        // the field
        GridBagConstraints fieldGc = new GridBagConstraints();
        fieldGc.fill = GridBagConstraints.HORIZONTAL;
        fieldGc.weightx = 1.0;
        fieldGc.gridx = 0;
        fieldGc.gridy = gridY + 1;
        // checks wether a bottom margin is necessary
        fieldGc.insets = new Insets(0, 0, addBottomMargin ? 14 : 0, 0);
        form.add(field, fieldGc);
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
        footer.add(Box.createVerticalStrut(20));
        footer.add(btnIrALogin);
        return footer;
    }

    // ═════════════════════════════════════════════════════════════
    //  EVENTOS
    // ═════════════════════════════════════════════════════════════

    private void wireEvents() {
        btnRegistrar.addActionListener(e -> registrar());
        btnIrALogin.addActionListener(e -> volverALogin());
        comboBoxUsers.addActionListener(e -> updateFormFields());

        // Cerrar la ventana también vuelve al login
        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { mostrarLogin(); }
        });
    }

    private void registrar() {
        String pass   = new String(tfPass.getPassword()).trim();
        String email  = tfEmail.getText().trim();
        
        // they share attributes
        if (comboBoxUsers.getSelectedItem().equals("Adopter") || comboBoxUsers.getSelectedItem().equals("Rescuer")) {
            
            String firstName = tfFirstName.getText().trim();
            String secondName = tfSecondName.getText().trim();
            String firstSurname = tfFirstSurname.getText().trim();
            String secondSurname = tfSecondSurname.getText().trim();
            
            if (firstName.isBlank() || secondName.isBlank() || firstSurname.isBlank() || 
                    secondSurname.isBlank() || pass.isBlank() || email.isBlank()) {
            JOptionPane.showMessageDialog(frame,
                "Por favor completá todos los campos.",
                "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            
            if (comboBoxUsers.getSelectedItem().equals("Adopter"))
                DBConnection.insertAdopter(email, pass, firstName, secondName, firstSurname, secondSurname);

            if (comboBoxUsers.getSelectedItem().equals("Rescuer"))
                DBConnection.insertRescuer(email, pass, firstName, secondName, firstSurname, secondSurname);
                    
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
        } // end of adopter and rescuer
        
        if (comboBoxUsers.getSelectedItem().equals("Association")) {
            
            String name = tfName.getText().trim();
            
            
            if (name.isBlank() || pass.isBlank() || email.isBlank()) {
            JOptionPane.showMessageDialog(frame,
                "Por favor completá todos los campos.",
                "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            
            DBConnection.insertAssociation(email, pass, name);
                    
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
        } // end of association
        
        
        if (comboBoxUsers.getSelectedItem().equals("Crib house")) {
            
            String name = tfName.getText().trim();
            
            // 1 if the checkBox is selected, 0 if not
            int requiresDonations = donationsCheckBox.isSelected() ? 1 : 0;
            
            if (name.isBlank() || pass.isBlank() || email.isBlank()) {
            JOptionPane.showMessageDialog(frame,
                "Por favor completá todos los campos.",
                "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Integer> selectedSizes = getSelectedSizes();
            if (selectedSizes.isEmpty()){
                JOptionPane.showMessageDialog(frame, "Select at least one pet size", 
                        "No sizes selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            DBConnection.insertCribHouse(email, pass, name, requiresDonations, selectedSizes);
                    
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
        } // end of Crib house
        
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
    
    private static JComboBox buildComboBox(String[] users) {
        JComboBox combox = new JComboBox(users);
        combox.setFont(Format.FONT_BODY_SMALL);
        combox.setForeground(Format.COLOR_PRIMARY);
        return combox;
    }
    
    private static JCheckBox buildCheckBox(String label) {
        JCheckBox checkBox = new JCheckBox(label);
        checkBox.setFont(Format.FONT_BODY_SMALL);
        checkBox.setForeground(Format.COLOR_PRIMARY);
        checkBox.setContentAreaFilled(false);
        checkBox.setBorderPainted(false);
        checkBox.setFocusPainted(false);
        return checkBox;
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