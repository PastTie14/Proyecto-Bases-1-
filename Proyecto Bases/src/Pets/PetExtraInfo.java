/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pets;

/**
 *
 * @author Ian
 */
public class PetExtraInfo {
    private int id;
    private int size;
    private String beforePicture; //Dir de la imagen
    private String afterPicture; //Dir de la iamgen
    private String currentStatus;
    private int bounty;
    private String bountyCurrency;
    private MedicSheet medicInfo;

    public PetExtraInfo(int id, int size) {
        this.id = id;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getBeforePicture() {
        return beforePicture;
    }

    public void setBeforePicture(String beforePicture) {
        this.beforePicture = beforePicture;
    }

    public String getAfterPicture() {
        return afterPicture;
    }

    public void setAfterPicture(String afterPicture) {
        this.afterPicture = afterPicture;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public int getBounty() {
        return bounty;
    }

    public void setBounty(int bounty) {
        this.bounty = bounty;
    }

    public String getBountyCurrency() {
        return bountyCurrency;
    }

    public void setBountyCurrency(String bountyCurrency) {
        this.bountyCurrency = bountyCurrency;
    }

    public MedicSheet getMedicInfo() {
        return medicInfo;
    }

    public void setMedicInfo(MedicSheet medicInfo) {
        this.medicInfo = medicInfo;
    }
    
    
}
