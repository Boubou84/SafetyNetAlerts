package com.safetynet.Safetynetalerts.DTO;

import java.util.List;

/**
 * DTO pour transporter des informations détaillées sur une personne.
 * Utilisé pour afficher des informations spécifiques sur une personne dans les réponses aux requêtes.
 */

public class PersonDetails {
    private String name;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;

    public PersonDetails() {
    }

    public PersonDetails(String name, String phone, int age, List<String> medications, List<String> allergies) {
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}

