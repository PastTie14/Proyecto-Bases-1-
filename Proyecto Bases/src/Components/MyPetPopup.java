package Components;

import Panels.MyPetsPanel;
import TablesObj.Pet;

import javax.swing.*;
import java.awt.*;

public class MyPetPopup extends PetPopup {

    private static final int STATUS_ADOPTED = 4;

    private final Pet         pet;
    private final int         idUser;
    private final MyPetsPanel owner;

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────

    public MyPetPopup(Frame parent, Pet pet, int idUser, MyPetsPanel owner) {
        super(parent, pet, idUser);
        this.pet    = pet;
        this.idUser = idUser;
        this.owner  = owner;
        injectEditButton();
        
    }

    // ─────────────────────────────────────────────────────────────
    //  INYECCIÓN DEL BOTÓN EDITAR
    // ─────────────────────────────────────────────────────────────

    private void injectEditButton() {
        Container root = getContentPane();
        if (!(root instanceof JPanel)) return;

        BorderLayout bl = (BorderLayout) ((JPanel) root).getLayout();
        Component south = bl.getLayoutComponent(root, BorderLayout.SOUTH);
        if (!(south instanceof JPanel)) return;

        JPanel footer = (JPanel) south;

        JButton editBtn = buildEditButton();
        footer.setLayout(new BorderLayout(8, 0));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(editBtn);

        footer.add(leftPanel,  BorderLayout.WEST);
        footer.revalidate();
        footer.repaint();
    }

    // ─────────────────────────────────────────────────────────────
    //  BOTÓN EDITAR
    // ─────────────────────────────────────────────────────────────

    private JButton buildEditButton() {
        JButton btn = new JButton("✏  Editar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                Color base = new Color(90, 120, 200);   // azul secundario
                g2.setColor(getModel().isPressed() ? base.darker() : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                        Format.RADIUS_BTN, Format.RADIUS_BTN);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(Format.FONT_BUTTON);
        btn.setForeground(Format.COLOR_TEXT_ON_PRIMARY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> onEditClicked());
        return btn;
    }

    // ─────────────────────────────────────────────────────────────
    //  EVENTO EDITAR
    // ─────────────────────────────────────────────────────────────

    private void onEditClicked() {
        dispose(); // cerrar este popup antes de abrir el editor
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(owner);
        PetEditDialog editor = new PetEditDialog(parent, pet, idUser, owner);
        editor.setAlwaysOnTop(true);
        editor.setVisible(true);
    }
}
