package com.g1.Appli2;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactAndroid implements Serializable{
    private String contact_id;
    private String name;
    private ArrayList<String> listPhoneNumber;
    private ArrayList<String> listEmail;
    private String idImage;
    private int idCoche;
    private int idColor;

    // Constructor
    public ContactAndroid(String contact_id, String name, ArrayList<String> listPhoneNumber, ArrayList<String> listEmail, String idImage, int idCoche, int idColor) {
        this.contact_id = contact_id;
        this.name = name;
        this.listPhoneNumber = listPhoneNumber;
        this.listEmail = listEmail;
        this.idImage = idImage;
        this.idCoche = idCoche;
        this.idColor = idColor;
    }

    // Getter

    public String getContact_id() {
        return contact_id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getListPhoneNumber() {
        return listPhoneNumber;
    }

    public ArrayList<String> getListEmail() {
        return listEmail;
    }

    public String getIdImage() {
        return idImage;
    }

    public int getIdCoche() {
        return idCoche;
    }

    public int getIdColor() {
        return idColor;
    }

    // Setter
    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setListPhoneNumber(ArrayList<String> listPhoneNumber) {
        this.listPhoneNumber = listPhoneNumber;
    }

    public void setListEmail(ArrayList<String> listEmail) {
        this.listEmail = listEmail;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public void setIdCoche(int idCoche) {
        this.idCoche = idCoche;
    }

    public void setIdColor(int idColor) {
        this.idColor = idColor;
    }

    public void addPhoneNumberToList(String phoneNumber){
        this.listPhoneNumber.add(phoneNumber);
    }

    public void addEmailToList(String email){
        this.listEmail.add(email);
    }


    // ToString
    @Override
    public String toString() {
        return "ContactAndroid{" +
                "contact_id='" + contact_id + '\'' +
                ", name='" + name + '\'' +
                ", listPhoneNumber=" + listPhoneNumber +
                ", listEmail=" + listEmail +
                ", idImage=" + idImage +
                ", idCoche=" + idCoche +
                ", idColor=" + idColor +
                '}';
    }
}