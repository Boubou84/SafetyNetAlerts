package com.safetynet.safetynetalerts.DTO;

import java.util.List;

/**
 * DTO pour transporter des informations détaillées sur une personne.
 * Utilisé pour afficher des informations spécifiques sur une personne dans les réponses aux requêtes.
 */

public class PersonDetails {
    private String firstName;
    private String lastName;
    private int age;
    private String phone;
    private String address;
    private String city;
    private int zip;
    private String email;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;
    private int station;
    private String fireStationAddress;

    public PersonDetails() {
    }

    public PersonDetails(String firstName, String lastName, String phone, int age,  String address, String city, int zip, String email, String birthdate, List<String> medications, List<String> allergies, int station, String fireStationAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.age = age;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.email = email;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
        this.station = station;
        this.fireStationAddress = fireStationAddress;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
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

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public String getFireStationAddress() {
        return fireStationAddress;
    }

    public void setFireStationAddress(String fireStationAddress) {
        this.fireStationAddress = fireStationAddress;
    }
}

