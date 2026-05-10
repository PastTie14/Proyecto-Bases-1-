package Panels;

import Components.Format;
import Components.FormComboBox;
import Components.FormField;
import TablesObj.Pet;
import TablesObj.PetExtraInfo;
import TablesObj.Status;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


 
public class PetEditDialog extends JDialog {

    private static final Logger LOG = Logger.getLogger(PetEditDialog.class.getName());

    // ── Modelo ────────────────────────────────────────────────────
    private final Pet         pet;
    private final int         idUser;
    private final MyPetsPanel owner;

    // ── Combos / Maps ─────────────────────────────────────────────
    private final LinkedHashMap<String, Integer> statusMap = new LinkedHashMap<>();

    // ── Campos ───────────────────────────────────────────────────
    private final FormField    fldName      = new FormField("Nombre");
    private final FormField    fldPicture   = new FormField("URL de imagen");
    private final FormField    fldBirthdate = new FormField("Fecha de nacimiento (YYYY-MM-DD)");
    private final FormField    fldDateLost  = new FormField("Fecha de pérdida (YYYY-MM-DD, opcional)");
    private final FormField    fldDateFound = new FormField("Fecha de encuentro (YYYY-MM-DD)");
    private final FormField    fldEmail     = new FormField("Email de contacto");
    private final FormComboBox cmbStatus    = new FormComboBox("Estado");

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────

    public PetEditDialog(Frame parent, Pet pet, int idUser, MyPetsPanel owner) {
        super(parent, "Editar mascota", true);
        this.pet    = pet;
        this.idUser = idUser;
        this.owner  = owner;

        setUndecorated(false);
        setSize(480, 560);
        setLocationRelativeTo(parent);
        setResizable(false);
        setBackground(Format.COLOR_BG);

        setContentPane(buildContent());
        prefillFields();
    }

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCCIÓN DE UI
    // ─────────────────────────────────────────────────────────────

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Format.COLOR_BG);

        // ── Título ────────────────────────────────────────────────
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(Format.COLOR_BG);
        titleBar.setBorder(BorderFactory.createCompoundBorder(
                Format.borderDivider(),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)));

        JLabel title = new JLabel("Editar mascota");
        title.setFont(Format.FONT_SUBTITLE);
        title.setForeground(Format.COLOR_TEXT_PRIMARY);
        titleBar.add(title, BorderLayout.WEST);
        root.add(titleBar, BorderLayout.NORTH);

        // ── Formulario ────────────────────────────────────────────
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Format.COLOR_BG);
        form.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        loadStatusCombo();

        form.add(fldName);
        form.add(vgap());
        form.add(fldPicture);
        form.add(vgap());
        form.add(fldBirthdate);
        form.add(vgap());
        form.add(fldDateLost);
        form.add(vgap());
        form.add(fldDateFound);
        form.add(vgap());
        form.add(fldEmail);
        form.add(vgap());
        form.add(cmbStatus);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.getViewport().setBackground(Format.COLOR_BG);
        root.add(scroll, BorderLayout.CENTER);

        // ── Footer ────────────────────────────────────────────────
        root.add(buildFooter(), BorderLayout.SOUTH);

        return root;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        footer.setBackground(Format.COLOR_BG);
        footer.setBorder(Format.borderDivider());

        // Botón cancelar
        JButton cancelBtn = new JButton("Cancelar");
        cancelBtn.setFont(Format.FONT_BODY);
        cancelBtn.setForeground(Format.COLOR_TEXT_PRIMARY);
        cancelBtn.setBackground(Format.COLOR_BG);
        cancelBtn.setBorderPainted(true);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setPreferredSize(new Dimension(110, 36));
        cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dispose());

        // Botón guardar
        JButton saveBtn = new JButton("Guardar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                g2.setColor(getModel().isPressed()
                        ? Format.COLOR_PRIMARY.darker()
                        : Format.COLOR_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                        Format.RADIUS_BTN, Format.RADIUS_BTN);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        saveBtn.setFont(Format.FONT_BUTTON);
        saveBtn.setForeground(Format.COLOR_TEXT_ON_PRIMARY);
        saveBtn.setContentAreaFilled(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setPreferredSize(new Dimension(110, 36));
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> onSave());

        footer.add(cancelBtn);
        footer.add(saveBtn);
        return footer;
    }

    // ─────────────────────────────────────────────────────────────
    //  PRE-LLENADO DE CAMPOS
    // ─────────────────────────────────────────────────────────────

    private void prefillFields() {
        fldName     .setValue(nvl(pet.getFirstName()));
        fldPicture  .setValue(nvl(pet.getPicture()));
        fldBirthdate.setValue(nvl(pet.getBirthdate()));
        fldDateLost .setValue(nvl(pet.getDateLost()));
        fldDateFound.setValue(nvl(pet.getDateFound()));
        fldEmail    .setValue(nvl(pet.getEmail()));

        // Seleccionar el estado actual en el combo
        String currentStatusLabel = labelForStatus(pet.getIdStatus());
        if (currentStatusLabel != null) {
            cmbStatus.getCombo().setSelectedItem(currentStatusLabel);
        }
    }

    /** Devuelve la label del combo que corresponde a un id_status. */
    private String labelForStatus(int idStatus) {
        for (var entry : statusMap.entrySet()) {
            if (entry.getValue() == idStatus) return entry.getKey();
        }
        return null;
    }

    // ─────────────────────────────────────────────────────────────
    //  CARGA DEL COMBO DE ESTADOS
    // ─────────────────────────────────────────────────────────────

    private void loadStatusCombo() {
        statusMap.clear();
        try {
            ResultSet rs = Status.getAll();
            while (rs != null && rs.next()) {
                int    id   = rs.getInt(1);
                String type = rs.getString(2);
                if (type != null) statusMap.put(type, id);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error cargando estados", ex);
        }
        cmbStatus.setOptions(new ArrayList<>(statusMap.keySet()));
    }

    // ─────────────────────────────────────────────────────────────
    //  GUARDAR
    // ─────────────────────────────────────────────────────────────

    private void onSave() {
        // ── Validaciones mínimas ──────────────────────────────────
        String name = fldName.getValue().trim();
        if (name.isBlank()) {
            warn("El nombre de la mascota es obligatorio.");
            return;
        }
        String dateFound = fldDateFound.getValue().trim();
        if (dateFound.isBlank()) {
            warn("La fecha de encuentro es obligatoria.");
            return;
        }

        // ── Resolver id_status ────────────────────────────────────
        String selectedStatus = cmbStatus.getSelected();
        Integer idStatus = selectedStatus != null ? statusMap.get(selectedStatus) : null;
        if (idStatus == null || idStatus <= 0) {
            warn("Selecciona un estado válido.");
            return;
        }

        // ── Llamar al update ──────────────────────────────────────
        String picture   = fldPicture.getValue().trim();
        String birthdate = fldBirthdate.getValue().trim();
        String dateLost  = fldDateLost.getValue().trim();
        String email     = fldEmail.getValue().trim();

        pet.updateItem(
            picture.isBlank()   ? null : picture,
            name,
            birthdate.isBlank() ? null : birthdate,
            dateLost.isBlank()  ? null : dateLost,
            dateFound,
            email.isBlank()     ? null : email,
            idStatus
        );

        // ── Feedback y recarga ────────────────────────────────────
        JOptionPane.showMessageDialog(this,
                "Mascota actualizada correctamente.",
                "Guardado", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        
        if (owner != null) {
            SwingUtilities.invokeLater(owner::reload);
        }
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atención", JOptionPane.WARNING_MESSAGE);
    }

    private static String nvl(String s) {
        return (s != null) ? s : "";
    }

    private Component vgap() {
        return Box.createVerticalStrut(Format.GAP_META);
    }
}
