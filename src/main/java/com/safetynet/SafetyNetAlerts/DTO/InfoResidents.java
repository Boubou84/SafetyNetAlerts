package com.safetynet.SafetyNetAlerts.DTO;

import java.util.List;

/**
 * DTO contenant des informations sur les résidents d'une adresse spécifique.
 * Utilisé pour les réponses aux requêtes concernant les informations résidentielles.
 */

public class InfoResidents {
    private String name;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;

    public InfoResidents() {
    }

    public InfoResidents(String name, String phone, int age, List<String> medications, List<String> allergies) {
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

