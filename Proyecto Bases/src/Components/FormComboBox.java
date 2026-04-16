package Components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FormComboBox extends JPanel {

    private final JLabel            label;
    private final JComboBox<String> combo;

    public FormComboBox(String labelText) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        setAlignmentX(LEFT_ALIGNMENT);

        label = new JLabel(labelText);
        label.setFont(Format.FONT_BODY_SMALL);
        label.setForeground(Format.COLOR_TEXT_SECONDARY);
        label.setAlignmentX(LEFT_ALIGNMENT);

        combo = new JComboBox<>();
        combo.setFont(Format.FONT_BODY);
        combo.setBackground(Format.COLOR_BG_SURFACE);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        combo.setAlignmentX(LEFT_ALIGNMENT);

        add(label);
        add(Box.createVerticalStrut(3));
        add(combo);
    }

    public void setOptions(ArrayList<String> options) {
        combo.removeAllItems();
        options.forEach(combo::addItem);
    }

    public void setOptions(String... options) {
        combo.removeAllItems();
        for (String o : options) combo.addItem(o);
    }

    public String getSelected()            { return (String) combo.getSelectedItem(); }
    public void   setSelected(String v)    { combo.setSelectedItem(v); }
    public JComboBox<String> getCombo()    { return combo; }
}
