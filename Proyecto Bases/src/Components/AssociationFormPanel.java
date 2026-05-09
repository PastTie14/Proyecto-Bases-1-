package Components;

import TablesObj.Association;
import TablesObj.PhoneNumber;
import TablesObj.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

 
public class AssociationFormPanel extends JPanel {

    // ── Modo edición ──────────────────────────────────────────────
    private final int     idUser;
    private final boolean editMode;

    // ── Campos: cuenta de usuario ─────────────────────────────────
    private final FormField email    = new FormField("Correo electrónico");
    private final FormField password = new FormField("Contraseña");

    // ── Campos: datos de la asociación ────────────────────────────
    private final FormField assocName = new FormField("Nombre de la asociación");

    // ── Teléfonos dinámicos ───────────────────────────────────────
    private final DynamicFieldList phones = new DynamicFieldList("Teléfonos", "Teléfono");

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTORES
    // ─────────────────────────────────────────────────────────────

  
    public AssociationFormPanel(int idUser) {
        this.idUser   = idUser;
        this.editMode = (idUser > 0);

        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);

        if (editMode) prefill();

        JScrollPane scroll = new JScrollPane(buildInner());
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getViewport().setBackground(Format.COLOR_BG);

        add(scroll,         BorderLayout.CENTER);
        add(buildSaveBar(), BorderLayout.SOUTH);
    }

    // ─────────────────────────────────────────────────────────────
    //  PRECARGA 
    // ─────────────────────────────────────────────────────────────

    private void prefill() {
        User        u = new User(idUser);
        Association a = new Association(idUser);

        email.setValue(u.getEmail());
        assocName.setValue(a.getName());

        ArrayList<String> nums = PhoneNumber.getByUser(idUser);
        for (String n : nums) phones.addValue(n);
    }

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCCIÓN DE UI
    // ─────────────────────────────────────────────────────────────

    private JPanel buildInner() {
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(Format.COLOR_BG);
        inner.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        inner.add(sectionTitle("Cuenta de usuario"));
        inner.add(gap());  inner.add(email);
        inner.add(gap());  inner.add(password);

        inner.add(gap(16));
        inner.add(sectionTitle("Datos de la asociación"));
        inner.add(gap());  inner.add(assocName);

        inner.add(gap(16));
        inner.add(sectionTitle("Contacto"));
        inner.add(gap());  inner.add(phones);

        return inner;
    }

    private JPanel buildSaveBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        bar.setBackground(Format.COLOR_BG);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Format.COLOR_PRIMARY.darker()));

        String  label = editMode ? "Guardar cambios" : "Registrar asociación";
        JButton btn   = styledButton(label);
        btn.addActionListener(e -> buildAndSave());
        bar.add(btn);
        return bar;
    }

    // ─────────────────────────────────────────────────────────────
    //  GUARDAR
    // ─────────────────────────────────────────────────────────────

    public boolean buildAndSave() {
        if (email.getValue().isBlank() || assocName.getValue().isBlank()) {
            JOptionPane.showMessageDialog(this,
                "Completa los campos obligatorios: correo y nombre de la asociación.",
                "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!editMode && password.getValue().isBlank()) {
            JOptionPane.showMessageDialog(this,
                "La contraseña es obligatoria al crear una nueva asociación.",
                "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            if (editMode) {
                if (!password.getValue().isBlank())
                    new User(idUser).update(email.getValue(), password.getValue());

                new Association(idUser).update(assocName.getValue());

                ArrayList<String> nums = phones.getValues();
                if (!nums.isEmpty()) PhoneNumber.insertForUser(idUser, nums);

            } else {
                int newId = insertUserAndReturn(email.getValue(), password.getValue());
                if (newId <= 0) {
                    JOptionPane.showMessageDialog(this,
                        "Error al crear el usuario. Intenta de nuevo.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                Association.insert(newId, assocName.getValue());

                ArrayList<String> nums = phones.getValues();
                if (!nums.isEmpty()) PhoneNumber.insertForUser(newId, nums);
            }

            JOptionPane.showMessageDialog(this,
                editMode ? "Asociación actualizada correctamente."
                         : "Asociación registrada correctamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error inesperado: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────────────────────

    private static int insertUserAndReturn(String email, String password) {
        try (java.sql.Connection con = java.sql.DriverManager.getConnection(
                Connect.DBConnection.host,
                Connect.DBConnection.uName,
                Connect.DBConnection.uPass);
             java.sql.CallableStatement st = con.prepareCall(
                     "{ CALL adminUser.insertUser(?,?,?) }")) {
            st.registerOutParameter(1, java.sql.Types.NUMERIC);
            st.setString(2, email);
            st.setString(3, password);
            st.execute();
            return st.getInt(1);
        } catch (java.sql.SQLException ex) {
            java.util.logging.Logger.getLogger(AssociationFormPanel.class.getName())
                .log(java.util.logging.Level.SEVERE, "Error insertUser", ex);
        }
        return -1;
    }

    private JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Format.FONT_SUBTITLE);
        lbl.setForeground(Format.COLOR_PRIMARY);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(Format.borderSection());
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return lbl;
    }

    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(Format.FONT_BODY);
        btn.setForeground(Color.WHITE);
        btn.setBackground(Format.COLOR_PRIMARY);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 36));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e)
                { btn.setBackground(Format.COLOR_PRIMARY.darker()); }
            @Override public void mouseExited(java.awt.event.MouseEvent e)
                { btn.setBackground(Format.COLOR_PRIMARY); }
        });
        return btn;
    }

    private Component gap()      { return Box.createVerticalStrut(Format.GAP_META); }
    private Component gap(int h) { return Box.createVerticalStrut(h); }
}
