package com.safetynet.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
    private  List<FireStation> firestations;

    @JsonProperty("medicalrecords")
    private List<MedicalRecord> medicalrecords;

    public Root() {
        // Log pour indiquer que l'instance de Root est créée
        log.info("Une instance de Root a été créée");
    }

    public Root(List<Person> persons, List<FireStation> firestations, List<MedicalRecord> medicalrecords) {
        this.persons = persons;
        this.firestations = firestations;
        this.medicalrecords = medicalrecords;
        // Log pour indiquer que l'instance de Root est créée avec des données
        log.info("Une instance de Root a été créée avec des données");
    }

    public List<Person> getPersons() {
        // Log pour afficher le nombre de personnes chargées
        log.info("Nombre de personnes chargées : {}", persons != null ? persons.size() : 0);
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
        // Log pour afficher le nombre de personnes définies
        log.info("Nombre de personnes définies : {}", persons != null ? persons.size() : 0);
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

