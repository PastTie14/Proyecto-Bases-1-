/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pets;

import java.util.ArrayList;

/**
 *
 * @author Ian
 */
public class MedicSheet {
    private int id;
    private String abandonmentDescription;
    private String TrainingEase;
    private String EnergyLevel;
    private ArrayList<String> Diseases;
    private ArrayList<String> Treatments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbandonmentDescription() {
        return abandonmentDescription;
    }

    public void setAbandonmentDescription(String abandonmentDescription) {
        this.abandonmentDescription = abandonmentDescription;
    }

    public String getTrainingEase() {
        return TrainingEase;
    }

    public void setTrainingEase(String TrainingEase) {
        this.TrainingEase = TrainingEase;
    }

    public String getEnergyLevel() {
        return EnergyLevel;
    }

    public void setEnergyLevel(String EnergyLevel) {
        this.EnergyLevel = EnergyLevel;
    }

    public ArrayList<String> getDiseases() {
        return Diseases;
    }

    public void setDiseases(ArrayList<String> Diseases) {
        this.Diseases = Diseases;
    }

    public ArrayList<String> getTreatments() {
        return Treatments;
    }

    public void setTreatments(ArrayList<String> Treatments) {
        this.Treatments = Treatments;
    }
    
    
}
