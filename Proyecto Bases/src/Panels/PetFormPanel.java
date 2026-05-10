package Panels;
 
import Components.DynamicFieldList;
import Components.FormComboBox;
import Components.FormField;
import Components.FormTextArea;
import Components.Format;
import TablesObj.*;
 
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
 
 
public class PetFormPanel extends JPanel {
 
    private static final Logger LOG = Logger.getLogger(PetFormPanel.class.getName());
 
    private final int idUser;
 
    private final LinkedHashMap<String, Integer> statusMap        = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> petTypeMap       = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> raceMap          = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> colorMap         = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> provinciaMap     = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> cantonMap        = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> distritoMap      = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> currencyMap      = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> energyMap        = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> trainingMap      = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> currentStatusMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> sizeMap          = new LinkedHashMap<>();
 
    private final FormField      petName    = new FormField("Nombre de la mascota");
    private final FormField      pictureUrl = new FormField("URL de imagen");
    private final FormField      birthdate  = new FormField("Fecha de nacimiento (YYYY-MM-DD)");
    private final FormField      email      = new FormField("Email de contacto");
    private final FormField      dateLost   = new FormField("Fecha de pérdida (opcional, YYYY-MM-DD)");
    private final FormField      dateFound  = new FormField("Fecha de encuentro (YYYY-MM-DD)");
    private final FormField      chipId     = new FormField("Número de chip (opcional)");
    private final FormComboBox   status     = new FormComboBox("Estado");
    private final FormComboBox   petType    = new FormComboBox("Tipo de mascota");
    private final FormComboBox   race       = new FormComboBox("Raza");
    private final FormComboBox   color      = new FormComboBox("Color");
    private final FormComboBox   size       = new FormComboBox("Tamaño");
    private final FormComboBox   provincia  = new FormComboBox("Provincia");
    private final FormComboBox   canton     = new FormComboBox("Cantón");
    private final FormComboBox   distrito   = new FormComboBox("Distrito");
 
    private final DynamicFieldList petPhones = new DynamicFieldList("Teléfonos de contacto", "Teléfono");
 
    // ── Campos: información adicional 
    private final JCheckBox    extraCheck    = new JCheckBox("Agregar información adicional");
    private final JPanel       extraSection  = new JPanel();
    private final FormComboBox currentStatus = new FormComboBox("Estado actual");
    private final FormField    beforePicture = new FormField("URL foto antes (opcional)");
    private final FormField    afterPicture  = new FormField("URL foto después (opcional)");
    private final FormField    bounty        = new FormField("Recompensa");
    private final FormComboBox currency      = new FormComboBox("Moneda");
    private final FormComboBox energyLevel   = new FormComboBox("Nivel de energía");
    private final FormComboBox trainingEase  = new FormComboBox("Facilidad de entrenamiento");
 
    // ── Campos: hoja médica ───────────────────────────────────────
    private final FormTextArea     abandonDesc   = new FormTextArea("Descripción de abandono", 3);
    private final FormField        vetFirstName  = new FormField("Primer nombre (Veterinario)");
    private final FormField        vetSecondName = new FormField("Segundo nombre (Veterinario)");
    private final FormField        vetSurname1   = new FormField("Primer apellido (Veterinario)");
    private final FormField        vetSurname2   = new FormField("Segundo apellido (Veterinario)");
    private final FormField        clinicName    = new FormField("Especialidad / Nombre de clínica");
    private final DynamicFieldList vetPhones     = new DynamicFieldList("Teléfonos (Veterinario)", "Teléfono");
    private final DynamicFieldList diseases   = new DynamicFieldList("Enfermedades",  "Enfermedad");
    private final DynamicFieldList treatments = new DynamicFieldList("Tratamientos",  "Nombre del tratamiento");
    private final DynamicFieldList doses      = new DynamicFieldList("Dosis (mg)",    "Dosis en miligramos");
 
    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────
 
    public PetFormPanel(int idUser) {
        this.idUser = idUser;
 
        setLayout(new BorderLayout());
        setBackground(Format.COLOR_BG);
 
        loadAllCombos();
 
        provincia.getCombo().addActionListener(e -> onProvinciaChanged());
        canton.getCombo().addActionListener(e -> onCantonChanged());
        petType.getCombo().addActionListener(e -> onPetTypeChanged());
 
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(Format.COLOR_BG);
        inner.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
 
        inner.add(sectionTitle("Datos básicos"));
        inner.add(gap());  inner.add(petName);
        inner.add(gap());  inner.add(pictureUrl);
        inner.add(gap());  inner.add(birthdate);
        inner.add(gap());  inner.add(email);
        inner.add(gap());  inner.add(dateLost);
        inner.add(gap());  inner.add(dateFound);
        inner.add(gap());  inner.add(chipId);
        inner.add(gap());  inner.add(status);
        inner.add(gap());  inner.add(petType);
        inner.add(gap());  inner.add(race);
        inner.add(gap());  inner.add(color);
        inner.add(gap());  inner.add(size);
        inner.add(gap());  inner.add(petPhones);      // FIX: pet phones in basic section
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
        add(buildSaveBar(), BorderLayout.SOUTH);
    }
 
    // ─────────────────────────────────────────────────────────────
    //  BARRA DE GUARDADO
    // ─────────────────────────────────────────────────────────────
 
    private JPanel buildSaveBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        bar.setBackground(Format.COLOR_BG);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Format.COLOR_PRIMARY.darker()));
 
        JButton saveBtn = new JButton("Guardar mascota");
        saveBtn.setFont(Format.FONT_BODY);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBackground(Format.COLOR_PRIMARY);
        saveBtn.setOpaque(true);
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveBtn.setPreferredSize(new Dimension(180, 36));
 
        saveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                saveBtn.setBackground(Format.COLOR_PRIMARY.darker());
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                saveBtn.setBackground(Format.COLOR_PRIMARY);
            }
        });
 
        saveBtn.addActionListener(e -> {
            saveBtn.setEnabled(false);
            saveBtn.setText("Guardando…");
 
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override protected Boolean doInBackground() { return buildAndSave(); }
                @Override protected void done() {
                    try {
                        boolean ok = get();
                        if (ok) {
                            JOptionPane.showMessageDialog(
                                PetFormPanel.this,
                                "¡Mascota guardada correctamente!",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, "Error inesperado al guardar", ex);
                    } finally {
                        saveBtn.setEnabled(true);
                        saveBtn.setText("Guardar mascota");
                    }
                }
            };
            worker.execute();
        });
 
        bar.add(saveBtn);
        return bar;
    }
 
    // ─────────────────────────────────────────────────────────────
    //  CARGA DE COMBOS DESDE BD
    // ─────────────────────────────────────────────────────────────
 
    private void loadAllCombos() {
        loadCombo(Status.getAll(),        statusMap,        status,        1, 2);
        loadCombo(PetType.getAll(),       petTypeMap,       petType,       1, 2);
        loadCombo(TablesObj.PetColor.getAll(), colorMap,    color,         1, 2);
        loadCombo(Province.getAll(),      provinciaMap,     provincia,     1, 2);
        loadCombo(Currency.getAll(),      currencyMap,      currency,      1, 3);
        loadCombo(EnergyLevel.getAll(),   energyMap,        energyLevel,   1, 2);
        loadCombo(TrainingEase.getAll(),  trainingMap,      trainingEase,  1, 2);
        loadCombo(CurrentStatus.getAll(), currentStatusMap, currentStatus, 1, 2);
        loadCombo(Size.getAll(),          sizeMap,          size,          1, 2);
        canton.setOptions("—");
        distrito.setOptions("—");
        race.setOptions("—");
    }
 
    private void loadCombo(ResultSet rs,
                           LinkedHashMap<String, Integer> map,
                           FormComboBox combo,
                           int idCol, int labelCol) {
        map.clear();
        ArrayList<String> labels = new ArrayList<>();
        try {
            while (rs != null && rs.next()) {
                int    id    = rs.getInt(idCol);
                String label = rs.getString(labelCol);
                if (label != null) {
                    map.put(label, id);
                    labels.add(label);
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error cargando combo", ex);
        }
        combo.setOptions(labels);
    }
 
    // ── Cascada Provincia → Cantón ────────────────────────────────
 
    private void onProvinciaChanged() {
        String selected = provincia.getSelected();
        if (selected == null) return;
        Integer idProvincia = provinciaMap.get(selected);
        if (idProvincia == null) return;
 
        cantonMap.clear();
        distritoMap.clear();
        distrito.setOptions("—");
 
        ArrayList<String> cantonLabels = new ArrayList<>();
        try {
            ResultSet rs = Canton.getAll();
            while (rs != null && rs.next()) {
                if (rs.getInt(3) == idProvincia) {
                    int    id    = rs.getInt(1);
                    String label = rs.getString(2);
                    if (label != null) {
                        cantonMap.put(label, id);
                        cantonLabels.add(label);
                    }
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error cargando cantones", ex);
        }
        canton.setOptions(cantonLabels.isEmpty()
                ? new ArrayList<>(java.util.List.of("—"))
                : cantonLabels);
    }
 
    // ── Cascada Cantón → Distrito ─────────────────────────────────
 
    private void onCantonChanged() {
        String selected = canton.getSelected();
        if (selected == null || selected.equals("—")) return;
        Integer idCanton = cantonMap.get(selected);
        if (idCanton == null) return;
 
        distritoMap.clear();
        ArrayList<String> distritoLabels = new ArrayList<>();
        try {
            ResultSet rs = District.getAll();
            while (rs != null && rs.next()) {
                if (rs.getInt(3) == idCanton) {
                    int    id    = rs.getInt(1);
                    String label = rs.getString(2);
                    if (label != null) {
                        distritoMap.put(label, id);
                        distritoLabels.add(label);
                    }
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error cargando distritos", ex);
        }
        distrito.setOptions(distritoLabels.isEmpty()
                ? new ArrayList<>(java.util.List.of("—"))
                : distritoLabels);
    }
 
    // ── Cascada PetType → Race ────────────────────────────────────
 
    private void onPetTypeChanged() {
        String selected = petType.getSelected();
        if (selected == null) return;
        Integer idPetType = petTypeMap.get(selected);
        if (idPetType == null) return;
 
        raceMap.clear();
        ArrayList<String> raceLabels = new ArrayList<>();
        try {
            ResultSet rs = Race.getByPetType(idPetType);
            while (rs != null && rs.next()) {
                int    id    = rs.getInt(1);
                String label = rs.getString(2);
                if (label != null) {
                    raceMap.put(label, id);
                    raceLabels.add(label);
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error cargando razas", ex);
        }
        race.setOptions(raceLabels.isEmpty()
                ? new ArrayList<>(java.util.List.of("—"))
                : raceLabels);
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
        extraCheck.addActionListener(e -> extraSection.setVisible(extraCheck.isSelected()));
        row.add(extraCheck);
        return row;
    }
 
    private JPanel buildExtraSection() {
        extraSection.setLayout(new BoxLayout(extraSection, BoxLayout.Y_AXIS));
        extraSection.setOpaque(false);
        extraSection.setAlignmentX(LEFT_ALIGNMENT);
        extraSection.setVisible(false);
 
        JPanel bountyRow = new JPanel(new GridLayout(1, 2, 10, 0));
        bountyRow.setOpaque(false);
        bountyRow.setAlignmentX(LEFT_ALIGNMENT);
        bountyRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        bountyRow.add(bounty);
        bountyRow.add(currency);
 
        extraSection.add(sectionTitle("Información adicional"));
        extraSection.add(gap());   extraSection.add(currentStatus);
        extraSection.add(gap());   extraSection.add(beforePicture);
        extraSection.add(gap());   extraSection.add(afterPicture);
        extraSection.add(gap());   extraSection.add(bountyRow);
        extraSection.add(gap());   extraSection.add(energyLevel);
        extraSection.add(gap());   extraSection.add(trainingEase);
        extraSection.add(gap(12)); extraSection.add(sectionTitle("Hoja médica"));
        extraSection.add(gap());   extraSection.add(abandonDesc);
        extraSection.add(gap());   extraSection.add(sectionTitle("Veterinario (opcional)"));
        extraSection.add(gap());   extraSection.add(vetFirstName);
        extraSection.add(gap());   extraSection.add(vetSecondName);
        extraSection.add(gap());   extraSection.add(vetSurname1);
        extraSection.add(gap());   extraSection.add(vetSurname2);
        extraSection.add(gap());   extraSection.add(clinicName);
        extraSection.add(gap());   extraSection.add(vetPhones);   
        extraSection.add(gap(12)); extraSection.add(sectionTitle("Enfermedades y tratamientos"));
        extraSection.add(gap()); extraSection.add(diseases);
        extraSection.add(gap()); extraSection.add(treatments);
        extraSection.add(gap()); extraSection.add(doses);
        return extraSection;
    }
 
    public boolean buildAndSave() {
        // ── Validación de campos obligatorios ─────────────────────
        if (pictureUrl.getValue().isBlank() ||
            petName.getValue().isBlank()    ||
            birthdate.getValue().isBlank()  ||
            email.getValue().isBlank()      ||
            dateFound.getValue().isBlank()) {
            JOptionPane.showMessageDialog(this,
                "Completa los campos obligatorios: imagen, nombre, fecha de nacimiento, email y fecha de encuentro.",
                "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
 
        int idStatus   = resolveId(statusMap,  status.getSelected());
        int idRace     = resolveId(raceMap,    race.getSelected());
        int idColor    = resolveId(colorMap,   color.getSelected());
        int idDistrito = resolveId(distritoMap, distrito.getSelected());
        int idSize     = resolveId(sizeMap,    size.getSelected());
 
        // ── Insertar mascota ──────────────────────────────────────
        int idPet = Pet.insert(
            pictureUrl.getValue(),
            petName.getValue(),
            birthdate.getValue(),
            dateLost.getValue().isBlank() ? null : dateLost.getValue(),
            dateFound.getValue(),
            email.getValue(),
            idStatus,
            idRace,   
            idUser,
            idSize
        );
 
        if (idPet <= 0) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar la mascota. Intenta de nuevo.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
 
        // ── Relaciones básicas de la mascota ──────────────────────
        if (idColor    > 0) PetXColor.insert(idPet, idColor);
        if (idDistrito > 0) PetXDistrict.insert(idPet, idDistrito);
 
        ArrayList<String> petPhoneValues = petPhones.getValues();
        if (!petPhoneValues.isEmpty()) {
            PhoneNumber.insertForPet(idPet, petPhoneValues);
        }
 
        String chipValue = chipId.getValue();
        if (!chipValue.isBlank()) {
            IdentificationChip.insert(0, chipValue, dateFound.getValue(), idPet);
        }
 
        // ── Sección extra: PetExtraInfo + MedicSheet ─────────────
        if (extraCheck.isSelected()) {
            int idCurrentSt = resolveId(currentStatusMap, currentStatus.getSelected());
            int idEnergy    = resolveId(energyMap,        energyLevel.getSelected());
            int idTraining  = resolveId(trainingMap,      trainingEase.getSelected());
 
            String beforePicVal = beforePicture.getValue().isBlank() ? null : beforePicture.getValue();
            String afterPicVal  = afterPicture.getValue().isBlank()  ? null : afterPicture.getValue();
 
            int idExtraInfo = PetExtraInfo.insert(
                0, beforePicVal, afterPicVal,
                idPet, idCurrentSt, idEnergy, idTraining
            );
 
            if (idExtraInfo > 0) {
 
                // ── Bounty ────────────────────────────────────────
                String bountyVal = bounty.getValue();
                if (!bountyVal.isBlank() && !bountyVal.equals("0")) {
                    int idCurrency = resolveId(currencyMap, currency.getSelected());
                    try {
                        int amount = Integer.parseInt(bountyVal.trim());
                        Bounty.insert(0, amount, idExtraInfo, idCurrency);
                    } catch (NumberFormatException ex) {
                        LOG.log(Level.WARNING, "Valor de recompensa no numérico: " + bountyVal, ex);
                    }
                }
 
                // ── Hoja médica ───────────────────────────────────
                boolean hasVetInfo  = !vetFirstName.getValue().isBlank()
                   || !vetSurname1.getValue().isBlank();
                boolean hasMedData  = !abandonDesc.getValue().isBlank()
                                   || hasVetInfo
                                   || diseases.getValues().stream().anyMatch(s -> s != null && !s.isBlank())
                                   || treatments.getValues().stream().anyMatch(s -> s != null && !s.isBlank());

                if (hasMedData) {
                    // Validación: cada tratamiento debe tener su dosis
                    ArrayList<String> treatmentVals = treatments.getValues();
                    ArrayList<String> doseVals      = doses.getValues();
                    for (int i = 0; i < treatmentVals.size(); i++) {
                        String tName = treatmentVals.get(i);
                        String dose  = (i < doseVals.size()) ? doseVals.get(i) : "";
                        if (tName != null && !tName.isBlank()
                                && (dose == null || dose.isBlank())) {
                            JOptionPane.showMessageDialog(this,
                                "El tratamiento \"" + tName + "\" requiere una dosis en mg.",
                                "Dosis requerida", JOptionPane.WARNING_MESSAGE);
                            return false;
                        }
                    }

                    int idVet = 0;
                    if (hasVetInfo) {
                        idVet = Veterinarian.insert(
                            vetFirstName.getValue().trim(),
                            vetSecondName.getValue().trim(),
                            vetSurname1.getValue().trim(),
                            vetSurname2.getValue().trim(),
                            clinicName.getValue().trim()
                        );
                        if (idVet > 0) {
                            ArrayList<String> vetPhoneValues = vetPhones.getValues();
                            if (!vetPhoneValues.isEmpty()) {
                                PhoneNumber.insertForVeterinarian(idVet, vetPhoneValues);
                            }
                        }
                    }

                    int idMedicSheet = MedicSheet.insert(
                        0, abandonDesc.getValue(), idVet, idExtraInfo
                    );
                    if (idMedicSheet > 0) {
                        saveDiseasesAndTreatments(idMedicSheet);
                    }
                }
            }
        }
 
        return true;
    }
 
    private void saveDiseasesAndTreatments(int idMedicSheet) {
        ArrayList<String> diseaseNames   = diseases.getValues();
        ArrayList<String> treatmentNames = treatments.getValues();
        ArrayList<String> doseValues     = doses.getValues();       // ← nuevo

        ArrayList<Integer> insertedDiseaseIds = new ArrayList<>();
        for (String name : diseaseNames) {
            if (name == null || name.isBlank()) continue;
            int idDisease = Disease.insertAndGetId(name.trim());
            if (idDisease > 0) {
                insertedDiseaseIds.add(idDisease);
                DiseaseXMedicSheet.insert(idDisease, idMedicSheet);
            }
        }

        for (int i = 0; i < treatmentNames.size(); i++) {
            String name = treatmentNames.get(i);
            if (name == null || name.isBlank()) continue;

            // La validación de arriba garantiza que dose no estará vacía
            String rawDose = (i < doseValues.size()) ? doseValues.get(i).trim() : "";
            String dose    = rawDose.isEmpty() ? "" : rawDose + " mg";  // ← "150 mg"

            int idTreatment = Treatment.insertAndGetId(name.trim(), dose);
            if (idTreatment > 0) {
                for (int idDisease : insertedDiseaseIds) {
                    TreatmentXDisease.insert(idTreatment, idDisease);
                }
            }
        }
    }
 
    // ─────────────────────────────────────────────────────────────
    //  HELPERS INTERNOS
    // ─────────────────────────────────────────────────────────────
 
    private int resolveId(LinkedHashMap<String, Integer> map, String selected) {
        if (selected == null) return 0;
        Integer id = map.get(selected);
        return id != null ? id : 0;
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
 
    private Component gap()      { return Box.createVerticalStrut(Format.GAP_META); }
    private Component gap(int h) { return Box.createVerticalStrut(h); }
}