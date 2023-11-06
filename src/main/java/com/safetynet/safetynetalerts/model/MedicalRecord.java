package com.safetynet.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * Modèle pour représenter un dossier médical.
 * Contient des informations personnelles et médicales sur une personne.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class MedicalRecord {
    @JsonProperty("firstName")
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @JsonProperty("lastName")
    @NotBlank(message = "Le nom de famille est obligatoire")
    private String lastName;

    @JsonProperty("birthdate")
    @Past(message = "La date de naissance doit être dans le passé")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @NotBlank(message = "La date de naissance est obligatoire")
    private String birthdate;

    @JsonProperty("medications")
    @NotEmpty(message = "La liste des médicaments ne peut pas être vide")
    private List<String> medications;

    @JsonProperty("allergies")
    @NotEmpty(message = "La liste des allergies ne peut pas être vide")
    private List<String> allergies;
    public MedicalRecord() {
    }

    public MedicalRecord(String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
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

}