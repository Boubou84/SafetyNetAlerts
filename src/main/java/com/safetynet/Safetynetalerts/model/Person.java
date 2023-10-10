package com.safetynet.Safetynetalerts.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.ToString;
/**
 * Modèle pour représenter une personne.
 * Contient des informations de base sur une personne, son nom, son adresse, son téléphone, etc.
 */
@ToString
public class Person {
    @NotEmpty(message = "Le prénom ne peut pas être vide")
    private String firstName;
    @NotEmpty(message = "Le nom ne peut pas être vide")
    private String lastName;
    private String address;
    private String city;
    private int zip;
    private  String phone;
    private String email;

    public Person() {
    }

    public Person(String firstName, String lastName, String address, String city, int zip, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}