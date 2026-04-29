package Components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class FormField extends JPanel {

    private final JLabel     label;
    private final JTextField field;

    public FormField(String labelText) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        setAlignmentX(LEFT_ALIGNMENT);

        label = new JLabel(labelText);
        label.setFont(Format.FONT_BODY_SMALL);
        label.setForeground(Format.COLOR_TEXT_SECONDARY);
        label.setAlignmentX(LEFT_ALIGNMENT);

        field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Format.RADIUS_BTN, Format.RADIUS_BTN);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setFont(Format.FONT_BODY);
        field.setForeground(Format.COLOR_TEXT_PRIMARY);
        field.setBackground(Format.COLOR_BG_SURFACE);
        field.setOpaque(false);
        field.setBorder(buildBorder());
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        field.setAlignmentX(LEFT_ALIGNMENT);

        add(label);
        add(Box.createVerticalStrut(3));
        add(field);
    }

    private Border buildBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Format.COLOR_DIVIDER, 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        );
    }

    public String getValue()               { return field.getText().trim(); }
    public void   setValue(String v)       { field.setText(v); }
    public JTextField getField()           { return field; }
}
