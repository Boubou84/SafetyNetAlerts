package com.safetynet.Safetynetalerts.model;

import static com.safetynet.Safetynetalerts.util.AgeUtil.calculateAge;

/**
 * Modèle pour contenir des informations détaillées sur une personne.
 * Inclut des détails supplémentaires qui ne sont pas inclus dans le modèle Person de base.
 */

public class PersonInfo {

    private Person person;
    private int age;
    private MedicalRecord medicalRecord;

    public PersonInfo() {
    }

    public PersonInfo(Person person, MedicalRecord medicalRecord) {
        this.person = person;
        this.medicalRecord = medicalRecord;
        this.age = calculateAge(medicalRecord.getBirthdate());
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
