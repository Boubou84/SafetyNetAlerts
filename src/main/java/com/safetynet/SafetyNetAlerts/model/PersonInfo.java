package com.safetynet.SafetyNetAlerts.model;

public class PersonInfo {

    private Person person;
    private MedicalRecord medicalRecord;

    public PersonInfo() {
    }

    public PersonInfo(Person person, MedicalRecord medicalRecord) {
        this.person = person;
        this.medicalRecord = medicalRecord;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
}
