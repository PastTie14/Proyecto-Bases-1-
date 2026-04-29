package Components;

import Pets.MedicSheet;
import Pets.Pet;
import Pets.PetExtraInfo;
import Pets.Veterinarian;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PetFormPanel extends JPanel {

    // ── Datos básicos ─────────────────────────────────────────────
    private final FormField    pictureUrl   = new FormField("URL de imagen");
    private final FormField    birthdate    = new FormField("Fecha de nacimiento");
    private final FormField    email        = new FormField("Email de contacto");
    private final FormField    dateLost     = new FormField("Fecha de pérdida (opcional)");
    private final FormField    dateFound    = new FormField("Fecha de encuentro");
    private final FormField    chipId       = new FormField("Número de chip (opcional)");
    private final FormComboBox status       = new FormComboBox("Estado");
    private final FormComboBox petType      = new FormComboBox("Tipo de mascota");
    private final FormComboBox color        = new FormComboBox("Color");
    private final FormComboBox provincia    = new FormComboBox("Provincia");
    private final FormComboBox canton       = new FormComboBox("Cantón");
    private final FormComboBox distrito     = new FormComboBox("Distrito");

    // ── Información adicional (tras checkbox) ─────────────────────
    private final JCheckBox    extraCheck   = new JCheckBox("Agregar información adicional");
    private final JPanel       extraSection = new JPanel();

    private final FormComboBox currentStatus = new FormComboBox("Estado actual");
    private final FormField    bounty        = new FormField("Recompensa");
    private final FormComboBox currency      = new FormComboBox("Moneda");
    private final FormComboBox energyLevel   = new FormComboBox("Nivel de energía");
    private final FormComboBox trainingEase  = new FormComboBox("Facilidad de entrenamiento");

    // ── Hoja médica ───────────────────────────────────────────────
    private final FormTextArea abandonDesc  = new FormTextArea("Descripción de abandono", 3);
    private final FormField    vetFirstName  = new FormField("Primer nombre (Veterinario)");
    private final FormField    vetSecondName = new FormField("Segundo nombre (Veterinario)");
    private final FormField    vetSurname1   = new FormField("Primer apellido (Veterinario)");
    private final FormField    vetSurname2   = new FormField("Segundo apellido (Veterinario)");
    private final FormField    clinicName    = new FormField("Nombre de clínica");

    private final DynamicFieldList phones     = new DynamicFieldList("Teléfonos",     "Teléfono");
    private final DynamicFieldList diseases   = new DynamicFieldList("Enfermedades",  "Enfermedad");
    private final DynamicFieldList treatments = new DynamicFieldList("Tratamientos",  "Tratamiento");

    // ─────────────────────────────────────────────────────────────
    public PetFormPanel() {
        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(Format.COLOR_BG);
        inner.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        buildDefaults();

        inner.add(sectionTitle("Datos básicos"));
        inner.add(gap());  inner.add(pictureUrl);
        inner.add(gap());  inner.add(birthdate);
        inner.add(gap());  inner.add(email);
        inner.add(gap());  inner.add(dateLost);
        inner.add(gap());  inner.add(dateFound);
        inner.add(gap());  inner.add(chipId);
        inner.add(gap());  inner.add(status);
        inner.add(gap());  inner.add(petType);
        inner.add(gap());  inner.add(color);

        inner.add(gap());  inner.add(sectionTitle("Ubicación"));
        inner.add(gap());  inner.add(provincia);
        inner.add(gap());  inner.add(canton);
        inner.add(gap());  inner.add(distrito);

        inner.add(gap(16));
        inner.add(buildExtraToggle());
        inner.add(gap());
        inner.add(buildExtraSection());

        JScrollPane scroll = new JScrollPane(inner);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getViewport().setBackground(Format.COLOR_BG);

        add(scroll, BorderLayout.CENTER);
    }

    // ─────────────────────────────────────────────────────────────
    //  SECCIÓN EXTRA (toggle con checkbox)
    // ─────────────────────────────────────────────────────────────

    private JPanel buildExtraToggle() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);

        extraCheck.setFont(Format.FONT_BODY);
        extraCheck.setForeground(Format.COLOR_PRIMARY);
        extraCheck.setOpaque(false);
        extraCheck.addActionListener(e -> {
            extraSection.setVisible(extraCheck.isSelected());
        });
        row.add(extraCheck);
        return row;
    }

    private JPanel buildExtraSection() {
        extraSection.setLayout(new BoxLayout(extraSection, BoxLayout.Y_AXIS));
        extraSection.setOpaque(false);
        extraSection.setAlignmentX(LEFT_ALIGNMENT);
        extraSection.setVisible(false);

        // Recompensa + moneda en fila horizontal
        JPanel bountyRow = new JPanel(new GridLayout(1, 2, 10, 0));
        bountyRow.setOpaque(false);
        bountyRow.setAlignmentX(LEFT_ALIGNMENT);
        bountyRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        bountyRow.add(bounty);
        bountyRow.add(currency);

        extraSection.add(sectionTitle("Información adicional"));
        extraSection.add(gap());  extraSection.add(currentStatus);
        extraSection.add(gap());  extraSection.add(bountyRow);
        extraSection.add(gap());  extraSection.add(energyLevel);
        extraSection.add(gap());  extraSection.add(trainingEase);

        extraSection.add(gap(12)); extraSection.add(sectionTitle("Hoja médica"));
        extraSection.add(gap());   extraSection.add(abandonDesc);
        extraSection.add(gap());   extraSection.add(vetFirstName);
        extraSection.add(gap());   extraSection.add(vetSecondName);
        extraSection.add(gap());   extraSection.add(vetSurname1);
        extraSection.add(gap());   extraSection.add(vetSurname2);
        extraSection.add(gap());   extraSection.add(clinicName);
        extraSection.add(gap());   extraSection.add(phones);
        extraSection.add(gap());   extraSection.add(diseases);
        extraSection.add(gap());   extraSection.add(treatments);

        return extraSection;
    }

    // ─────────────────────────────────────────────────────────────
    //  OPCIONES POR DEFECTO
    // ─────────────────────────────────────────────────────────────

    private void buildDefaults() {
        status.setOptions("Available", "Pending Adoption", "Adopted", "In Treatment");
        petType.setOptions("Perro", "Gato", "Otro");
        color.setOptions("Café", "Negro", "Blanco", "Dorado", "Gris", "Naranja", "Mixto");
        currentStatus.setOptions("Available", "Pending Adoption", "Adopted", "In Treatment");
        currency.setOptions("₡", "$", "€");
        energyLevel.setOptions("Low", "Medium", "High", "Very High");
        trainingEase.setOptions("Easy", "Moderate", "Hard");
        provincia.setOptions("San José", "Alajuela", "Cartago", "Heredia",
                             "Guanacaste", "Puntarenas", "Limón");
        canton.setOptions("—");
        distrito.setOptions("—");
    }

    // ─────────────────────────────────────────────────────────────
    //  API PÚBLICA — configurar ComboBoxes desde fuera
    // ─────────────────────────────────────────────────────────────

    public void setStatusOptions      (ArrayList<String> opts) { status.setOptions(opts); }
    public void setPetTypeOptions     (ArrayList<String> opts) { petType.setOptions(opts); }
    public void setColorOptions       (ArrayList<String> opts) { color.setOptions(opts); }
    public void setProvinciaOptions   (ArrayList<String> opts) { provincia.setOptions(opts); }
    public void setCantonOptions      (ArrayList<String> opts) { canton.setOptions(opts); }
    public void setDistritoOptions    (ArrayList<String> opts) { distrito.setOptions(opts); }
    public void setCurrencyOptions    (ArrayList<String> opts) { currency.setOptions(opts); }
    public void setEnergyOptions      (ArrayList<String> opts) { energyLevel.setOptions(opts); }
    public void setTrainingOptions    (ArrayList<String> opts) { trainingEase.setOptions(opts); }

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUIR OBJETOS DEL MODELO
    // ─────────────────────────────────────────────────────────────

    public Pet buildPet() {
        Pet pet = new Pet(
            0,
            birthdate.getValue(),
            email.getValue(),
            dateLost.getValue().isBlank()  ? null : dateLost.getValue(),
            dateFound.getValue(),
            "user",
            java.time.LocalDate.now().toString(),
            status.getSelected(),
            petType.getSelected()
        );
        pet.setPicture(pictureUrl.getValue());

        if (extraCheck.isSelected()) {
            pet.setExtraInfo(buildExtraInfo());
        }
        return pet;
    }

    private PetExtraInfo buildExtraInfo() {
        PetExtraInfo extra = new PetExtraInfo(0, 0);
        extra.setCurrentStatus(currentStatus.getSelected());
        extra.setBountyCurrency(currency.getSelected());
        try { extra.setBounty(Integer.parseInt(bounty.getValue())); }
        catch (NumberFormatException ignored) {}
        extra.setMedicInfo(buildMedicSheet());
        return extra;
    }

    private MedicSheet buildMedicSheet() {
        MedicSheet m = new MedicSheet();
        m.setAbandonmentDescription(abandonDesc.getValue());
        m.setEnergyLevel(energyLevel.getSelected());
        m.setTrainingEase(trainingEase.getSelected());
        m.setDiseases(diseases.getValues());
        m.setTreatments(treatments.getValues());
        return m;
    }

    public Veterinarian buildVeterinarian() {
        Veterinarian v = new Veterinarian();
        v.setFirstName(vetFirstName.getValue());
        v.setSecondName(vetSecondName.getValue());
        v.setFirstSurname(vetSurname1.getValue());
        v.setSecondSurname(vetSurname2.getValue());
        v.setClinicName(clinicName.getValue());

        ArrayList<String> rawPhones = phones.getValues();
        ArrayList<Integer> phoneInts = new ArrayList<>();
        for (String p : rawPhones) {
            try { phoneInts.add(Integer.parseInt(p)); }
            catch (NumberFormatException ignored) {}
        }
        v.setPhoneNumber(phoneInts);
        return v;
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

    private Component gap()      { return Box.createVerticalStrut(Format.GAP_META); }
    private Component gap(int h) { return Box.createVerticalStrut(h); }
}
