package Components;

import TablesObj.CribHouse;
import TablesObj.PhoneNumber;
import TablesObj.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class CribHouseFormPanel extends JPanel {

    // ── Modo edición ──────────────────────────────────────────────
    private final int     idUser;

    private final FormField email    = new FormField("Correo electrónico");
    private final FormField password = new FormField("Contraseña");

    private final FormField cribName  = new FormField("Nombre de la casa cuna");

    private final JCheckBox requiresDonationsCheck = new JCheckBox("Requiere donaciones");

    private final DynamicFieldList phones = new DynamicFieldList("Teléfonos", "Teléfono");

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTORES
    // ─────────────────────────────────────────────────────────────

    public CribHouseFormPanel() { this(0); }

    public CribHouseFormPanel(int idUser) {
        this.idUser   = idUser;

        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);

        styleCheck(requiresDonationsCheck);

        prefill();

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
        User      u = new User(idUser);
        CribHouse c = new CribHouse(idUser);

        email.setValue(u.getEmail());
        cribName.setValue(c.getName());
        requiresDonationsCheck.setSelected(c.getRequiresDonations() == 1);

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

        // Wrapper alineado para el checkbox
        JPanel checkRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        checkRow.setOpaque(false);
        checkRow.setAlignmentX(LEFT_ALIGNMENT);
        checkRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        checkRow.add(requiresDonationsCheck);

        inner.add(sectionTitle("Cuenta de usuario"));
        inner.add(gap());  inner.add(email);
        inner.add(gap());  inner.add(password);

        inner.add(gap(16));
        inner.add(sectionTitle("Datos de la casa cuna"));
        inner.add(gap());  inner.add(cribName);
        inner.add(gap());  inner.add(checkRow);

        inner.add(gap(16));
        inner.add(sectionTitle("Contacto"));
        inner.add(gap());  inner.add(phones);

        return inner;
    }

    private JPanel buildSaveBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        bar.setBackground(Format.COLOR_BG);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Format.COLOR_PRIMARY.darker()));

        String  label = "Guardar cambios";
        JButton btn   = styledButton(label);
        btn.addActionListener(e -> buildAndSave());
        bar.add(btn);
        return bar;
    }

    // ─────────────────────────────────────────────────────────────
    //  GUARDAR
    // ─────────────────────────────────────────────────────────────

    public boolean buildAndSave() {
        if (email.getValue().isBlank() || cribName.getValue().isBlank()) {
            JOptionPane.showMessageDialog(this,
                "Completa los campos obligatorios: correo y nombre de la casa cuna.",
                "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        int requiresDon = requiresDonationsCheck.isSelected() ? 1 : 0;

        try {
            
                if (!password.getValue().isBlank())
                    new User(idUser).update(email.getValue(), password.getValue());

                new CribHouse(idUser).update(cribName.getValue(), requiresDon, 0);

                ArrayList<String> nums = phones.getValues();
                if (!nums.isEmpty()) PhoneNumber.insertForUser(idUser, nums);


            JOptionPane.showMessageDialog(this,"Casa cuna actualizada correctamente.",
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


    private void styleCheck(JCheckBox cb) {
        cb.setFont(Format.FONT_BODY);
        cb.setForeground(Format.COLOR_TEXT_PRIMARY);
        cb.setBackground(Format.COLOR_BG);
        cb.setOpaque(false);
        cb.setFocusPainted(false);
        cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
