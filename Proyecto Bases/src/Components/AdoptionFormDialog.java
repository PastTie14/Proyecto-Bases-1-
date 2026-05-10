package Components;

import TablesObj.AdoptionForm;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;


public class AdoptionFormDialog extends JDialog {

    private static final Logger LOG = Logger.getLogger(AdoptionFormDialog.class.getName());

    // ── Datos de contexto ─────────────────────────────────────────
    private final int idAdopter;
    private final int idPet;

    // ── Campos del formulario ─────────────────────────────────────
    private final FormTextArea  notes         = new FormTextArea("Notas sobre la adopción", 4);
    private final FormField     adoptionDate  = new FormField("Fecha de adopción (YYYY-MM-DD)");
    private final FormField     reference     = new FormField("Referencia / número de expediente");

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────

    public AdoptionFormDialog(Frame parent, int idAdopter, int idPet) {
        super(parent, "Formulario de Adopción", true);
        this.idAdopter = idAdopter;
        this.idPet     = idPet;

        setUndecorated(false);
        setSize(460, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
        setContentPane(buildRoot());
    }

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCCIÓN DE UI
    // ─────────────────────────────────────────────────────────────

    private JPanel buildRoot() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Format.COLOR_BG);

        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildForm(),    BorderLayout.CENTER);
        root.add(buildFooter(),  BorderLayout.SOUTH);

        return root;
    }

    // ── Encabezado ────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Format.COLOR_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("Formulario de Adopción");
        title.setFont(Format.FONT_TITLE);
        title.setForeground(Format.COLOR_TEXT_ON_PRIMARY);

        JLabel subtitle = new JLabel("Mascota #" + idPet + "  ·  Adoptante #" + idAdopter);
        subtitle.setFont(Format.FONT_BODY_SMALL);
        subtitle.setForeground(new Color(255, 255, 255, 180));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(title);
        text.add(Box.createVerticalStrut(3));
        text.add(subtitle);

        header.add(text, BorderLayout.CENTER);
        return header;
    }

    // ── Formulario ────────────────────────────────────────────────

    private JScrollPane buildForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Format.COLOR_BG);
        form.setBorder(BorderFactory.createEmptyBorder(18, 22, 10, 22));

        // Fecha de adopción — se pre-rellena con hoy
        adoptionDate.setValue(java.time.LocalDate.now().toString());

        form.add(notes);
        form.add(Box.createVerticalStrut(Format.GAP_META));
        form.add(adoptionDate);
        form.add(Box.createVerticalStrut(Format.GAP_META));
        form.add(reference);
        form.add(Box.createVerticalStrut(8));

        // Aviso informativo
        JLabel hint = new JLabel(
            "<html><i>Todos los campos son opcionales excepto la fecha de adopción.</i></html>");
        hint.setFont(Format.FONT_BODY_SMALL);
        hint.setForeground(Format.COLOR_TEXT_SECONDARY);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(hint);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.getViewport().setBackground(Format.COLOR_BG);
        return scroll;
    }

    // ── Footer con botones ────────────────────────────────────────

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        footer.setBackground(Format.COLOR_BG);
        footer.setBorder(Format.borderDivider());

        // Botón Cancelar
        JButton cancelBtn = new JButton("Cancelar");
        cancelBtn.setFont(Format.FONT_BODY);
        cancelBtn.setForeground(Format.COLOR_TEXT_SECONDARY);
        cancelBtn.setBackground(Format.COLOR_BG_SURFACE);
        cancelBtn.setOpaque(true);
        cancelBtn.setBorderPainted(true);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setPreferredSize(new Dimension(110, 34));
        cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dispose());

        // Botón Confirmar
        JButton confirmBtn = new JButton("Confirmar adopción") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                        Format.RADIUS_BTN, Format.RADIUS_BTN);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        confirmBtn.setFont(Format.FONT_BODY);
        confirmBtn.setForeground(Format.COLOR_TEXT_ON_PRIMARY);
        confirmBtn.setBackground(Format.COLOR_PRIMARY);
        confirmBtn.setContentAreaFilled(false);
        confirmBtn.setBorderPainted(false);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setPreferredSize(new Dimension(180, 34));
        confirmBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        confirmBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                confirmBtn.setBackground(Format.COLOR_PRIMARY_DARK);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                confirmBtn.setBackground(Format.COLOR_PRIMARY);
            }
        });

        confirmBtn.addActionListener(e -> {
            confirmBtn.setEnabled(false);
            confirmBtn.setText("Guardando…");

            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override protected Boolean doInBackground() { return save(); }
                @Override protected void done() {
                    try {
                        boolean ok = get();
                        if (ok) {
                            JOptionPane.showMessageDialog(
                                AdoptionFormDialog.this,
                                "¡Adopción registrada exitosamente!",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(
                                AdoptionFormDialog.this,
                                "Ocurrió un error al guardar. Verifica los datos e intenta de nuevo.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                            confirmBtn.setEnabled(true);
                            confirmBtn.setText("Confirmar adopción");
                        }
                    } catch (Exception ex) {
                        LOG.severe("Error inesperado al guardar adopción: " + ex.getMessage());
                        confirmBtn.setEnabled(true);
                        confirmBtn.setText("Confirmar adopción");
                    }
                }
            };
            worker.execute();
        });

        footer.add(cancelBtn);
        footer.add(confirmBtn);
        return footer;
    }

    // ─────────────────────────────────────────────────────────────
    //  LÓGICA DE GUARDADO
    // ─────────────────────────────────────────────────────────────

    private boolean save() {
        // ── Validación ────────────────────────────────────────────
        String dateVal = adoptionDate.getValue();
        if (dateVal.isBlank()) {
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this,
                    "La fecha de adopción es obligatoria.",
                    "Campo requerido", JOptionPane.WARNING_MESSAGE));
            return false;
        }

        // Formato básico de fecha
        if (!dateVal.matches("\\d{4}-\\d{2}-\\d{2}")) {
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Usa YYYY-MM-DD.",
                    "Formato inválido", JOptionPane.WARNING_MESSAGE));
            return false;
        }

        // ── Persistencia ──────────────────────────────────────────
        try {
            AdoptionForm.insert(
                notes.getValue(),           // notas (puede estar vacío)
                dateVal,                    // fecha de adopción
                reference.getValue(),       // referencia (puede estar vacío)
                idAdopter,                  // id del adoptante
                idPet                       // id de la mascota
            );
            return true;
        } catch (Exception ex) {
            LOG.severe("Error al insertar adopción: " + ex.getMessage());
            return false;
        }
    }
}
