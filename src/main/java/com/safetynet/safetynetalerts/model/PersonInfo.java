package com.safetynet.safetynetalerts.model;

/**
 * Modèle pour contenir des informations détaillées sur une personne.
 * Inclut des détails supplémentaires qui ne sont pas inclus dans le modèle Person de base.
 */

public class PersonInfo {

    private Person person;
    private MedicalRecord medicalRecord;
    private int age;

    public PersonInfo() {
    }

    public PersonInfo(Person person, MedicalRecord medicalRecord, int age) {
        this.person = person;
        this.medicalRecord = medicalRecord;
        this.age = age;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
