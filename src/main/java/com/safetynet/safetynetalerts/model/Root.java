package com.safetynet.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Modèle de base pour le chargement des données JSON.
 * Contient des listes de personnes, de dossiers médicaux et de stations de pompiers.
 */

@Data
@Slf4j
public class Root {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonProperty("persons")
    private List<Person> persons;

    @JsonProperty("firestations")
    private List<FireStation> firestations;

    @JsonProperty("medicalrecords")
    private List<MedicalRecord> medicalrecords;

    public Root() {
        this.persons = new ArrayList<>();
        this.firestations = new ArrayList<>();
        this.medicalrecords = new ArrayList<>();
    }

    public Root(List<Person> persons, List<FireStation> firestations, List<MedicalRecord> medicalrecords) {
        this.persons = persons != null ? persons : new ArrayList<>();
        this.firestations = firestations != null ? firestations : new ArrayList<>();
        this.medicalrecords = medicalrecords != null ? medicalrecords : new ArrayList<>();
    }

    public List<Person> getPersons() {
        log.info("Nombre de personnes chargées : {}", persons != null ? persons.size() : 0);
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public  List<FireStation> getFireStations() {
        return firestations;
    }

    public void setFireStations(List<FireStation> firestations) {
        this.firestations = firestations;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return this.medicalrecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalrecords) {
        this.medicalrecords = medicalrecords;
    }
}

