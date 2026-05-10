package Components;

import TablesObj.Adopter;
import TablesObj.PhoneNumber;
import TablesObj.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class AdopterFormPanel extends JPanel {

    // ── Modo edición ──────────────────────────────────────────────
    private final int idUser; 

    // ── Campos: cuenta de usuario ─────────────────────────────────
    private final FormField email      = new FormField("Correo electrónico");
    private final FormField password   = new FormField("Contraseña");

    // ── Campos: datos del adoptante ───────────────────────────────
    private final FormField firstName  = new FormField("Primer nombre");
    private final FormField secondName = new FormField("Segundo nombre (opcional)");
    private final FormField firstSurname  = new FormField("Primer apellido");
    private final FormField secondSurname = new FormField("Segundo apellido (opcional)");

    // ── Teléfonos dinámicos ───────────────────────────────────────
    private final DynamicFieldList phones = new DynamicFieldList("Teléfonos", "Teléfono");

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTORES
    // ─────────────────────────────────────────────────────────────

    public AdopterFormPanel(int idUser) {
        this.idUser   = idUser;

        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);
        prefill();

        JPanel inner = buildInner();
        JScrollPane scroll = new JScrollPane(inner);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getViewport().setBackground(Format.COLOR_BG);

        add(scroll,        BorderLayout.CENTER);
        add(buildSaveBar(), BorderLayout.SOUTH);
    }

    // ─────────────────────────────────────────────────────────────
    //  PRECARGA 
    // ─────────────────────────────────────────────────────────────

    private void prefill() {

        User    u = new User(idUser);
        Adopter a = new Adopter(idUser);

        email.setValue(u.getEmail());
        firstName.setValue(a.getFirstName());
        secondName.setValue(a.getSecondName());
        firstSurname.setValue(a.getFirstSurname());
        secondSurname.setValue(a.getSecondSurname());

        // Precarga de teléfonos existentes
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
        inner.add(sectionTitle("Datos del adoptante"));
        inner.add(gap());  inner.add(firstName);
        inner.add(gap());  inner.add(secondName);
        inner.add(gap());  inner.add(firstSurname);
        inner.add(gap());  inner.add(secondSurname);

        inner.add(gap(16));
        inner.add(sectionTitle("Contacto"));
        inner.add(gap());  inner.add(phones);

        return inner;
    }

    private JPanel buildSaveBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        bar.setBackground(Format.COLOR_BG);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Format.COLOR_PRIMARY.darker()));

        String label = "Guardar cambios" ;
        JButton btn  = styledButton(label);
        btn.addActionListener(e -> buildAndSave());
        bar.add(btn);
        return bar;
    }

    // ─────────────────────────────────────────────────────────────
    //  GUARDAR
    // ─────────────────────────────────────────────────────────────

    public boolean buildAndSave() {
        // ── Validaciones mínimas ──────────────────────────────────        
        if (email.getValue().isBlank() || firstName.getValue().isBlank()
                || firstSurname.getValue().isBlank()) {
            JOptionPane.showMessageDialog(this,
                "Completa los campos obligatorios: correo, primer nombre y primer apellido.",
                "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            // ── ACTUALIZACIÓN ────────────────────────────────
            if (!password.getValue().isBlank())
                new User(idUser).update(email.getValue(), password.getValue());
            new Adopter(idUser).update(
                firstName.getValue(),
                secondName.getValue(),
                firstSurname.getValue(),
                secondSurname.getValue()
            );
            ArrayList<String> nums = phones.getValues();
            if (!nums.isEmpty()) PhoneNumber.insertForUser(idUser, nums);

            

            JOptionPane.showMessageDialog(this,"Adoptante actualizado correctamente.",
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
