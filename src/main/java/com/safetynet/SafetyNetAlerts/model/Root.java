package com.safetynet.SafetyNetAlerts.model;

import java.util.List;

/**
 * Modèle de base pour le chargement des données JSON.
 * Contient des listes de personnes, de dossiers médicaux et de stations de pompiers.
 */

public class Root {
    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;

    public Root() {
    }

    public Root(List<Person> persons, List<FireStation> firestations, List<MedicalRecord> medicalrecords) {
        this.persons = persons;
        this.firestations = firestations;
        this.medicalrecords = medicalrecords;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<FireStation> getFirestations() {
        return firestations;
    }

    public void setFirestations(List<FireStation> firestations) {
        this.firestations = firestations;
    }

    public List<MedicalRecord> getMedicalrecords() {
        return medicalrecords;
    }

    public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
        this.medicalrecords = medicalrecords;
    }
}

