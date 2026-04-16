package Components;

import javax.swing.*;
import java.awt.*;

public class FormTextArea extends JPanel {

    private final JLabel    label;
    private final JTextArea area;

    public FormTextArea(String labelText, int rows) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setAlignmentX(LEFT_ALIGNMENT);

        label = new JLabel(labelText);
        label.setFont(Format.FONT_BODY_SMALL);
        label.setForeground(Format.COLOR_TEXT_SECONDARY);
        label.setAlignmentX(LEFT_ALIGNMENT);

        area = new JTextArea(rows, 0);
        area.setFont(Format.FONT_BODY);
        area.setForeground(Format.COLOR_TEXT_PRIMARY);
        area.setBackground(Format.COLOR_BG_SURFACE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(Format.COLOR_DIVIDER, 1, true));
        scroll.setAlignmentX(LEFT_ALIGNMENT);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, rows * 20 + 16));

        add(label);
        add(Box.createVerticalStrut(3));
        add(scroll);
    }

    public String getValue()         { return area.getText().trim(); }
    public void   setValue(String v) { area.setText(v); }
    public JTextArea getArea()       { return area; }
}
