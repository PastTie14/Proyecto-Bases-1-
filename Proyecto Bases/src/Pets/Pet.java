/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pets;
import Users.User;
/**
 *
 * @author Ian
 */
public class Pet {
    private int id;
    private String picture; //Dir de la imagen
    private String birthdate;
    private String email;
    private String dateLost;
    private String dateFound;
    private String createdBy;
    private String createdAt;
    private String modifiedBy;
    private String modifiedAt;
    private String Status; //Valor Cerrado, depende de la tabla en la BD
    private String petType;
    private User rescuer;
    private PetExtraInfo extraInfo;

    public Pet(int id, String birthdate, String email, String dateLost, String dateFound, String createdBy, String createdAt, String Status, String petType) {
        this.id = id;
        this.birthdate = birthdate;
        this.email = email;
        this.dateLost = dateLost;
        this.dateFound = dateFound;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.Status = Status;
        this.petType = petType;
    }

    public Pet(int id, String picture, String birthdate, String email, String dateLost, String createdBy, String createdAt, String Status, String petType, User rescuer) {
        this.id = id;
        this.picture = picture;
        this.birthdate = birthdate;
        this.email = email;
        this.dateLost = dateLost;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.Status = Status;
        this.petType = petType;
        this.rescuer = rescuer;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateLost() {
        return dateLost;
    }

    public void setDateLost(String dateLost) {
        this.dateLost = dateLost;
    }

    public String getDateFound() {
        return dateFound;
    }

    public void setDateFound(String dateFound) {
        this.dateFound = dateFound;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public User getRescuer() {
        return rescuer;
    }

    public void setRescuer(User rescuer) {
        this.rescuer = rescuer;
    }

    public PetExtraInfo getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(PetExtraInfo extraInfo) {
        this.extraInfo = extraInfo;
    }
    
    
}
