package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

public class DynamicFieldList extends JPanel {

    private final String        labelPrefix;
    private final ArrayList<FormField> fields = new ArrayList<>();

    public DynamicFieldList(String sectionLabel, String fieldLabelPrefix) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setAlignmentX(LEFT_ALIGNMENT);

        JLabel title = new JLabel(sectionLabel);
        title.setFont(Format.FONT_BODY_SMALL);
        title.setForeground(Format.COLOR_TEXT_SECONDARY);
        title.setAlignmentX(LEFT_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(3));

        this.labelPrefix = fieldLabelPrefix;
        addField(); // primer campo
    }

    private void addField() {
        int n = fields.size() + 1;
        FormField f = new FormField(labelPrefix + " " + n);
        f.getField().addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) {
                // Si este es el último campo y tiene texto, agrega uno nuevo
                if (!f.getValue().isBlank() && fields.indexOf(f) == fields.size() - 1) {
                    addField();
                    revalidate();
                    repaint();
                }
            }
        });
        fields.add(f);
        add(f);
        add(Box.createVerticalStrut(4));
    }

    public ArrayList<String> getValues() {
        ArrayList<String> result = new ArrayList<>();
        for (FormField f : fields) {
            String v = f.getValue();
            if (!v.isBlank()) result.add(v);
        }
        return result;
    }
}
