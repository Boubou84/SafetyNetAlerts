package com.safetynet.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AddPersonRequest {
    @JsonProperty("persons")
    private Person person;

    @JsonProperty("firestations")
    private FireStation fireStation;

    @JsonProperty("medicalrecords")
    private MedicalRecord medicalRecord;

    public AddPersonRequest() {
    }

    public AddPersonRequest(Person person, FireStation fireStation, MedicalRecord medicalRecord) {
        this.person = person;
        this.fireStation = fireStation;
        this.medicalRecord = medicalRecord;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(List<Person> person) {
        this.person = (Person) person;
    }

    public List<FireStation> getFireStation() {
        return (List<FireStation>) fireStation;
    }

    public void setFireStation(List<FireStation> fireStation) {
        this.fireStation = (FireStation) fireStation;
    }

    public List<MedicalRecord> getMedicalRecord() {
        return (List<MedicalRecord>) medicalRecord;
    }

    public void setMedicalRecord(List<MedicalRecord> medicalRecord) {
        this.medicalRecord = (MedicalRecord) medicalRecord;
    }
}
