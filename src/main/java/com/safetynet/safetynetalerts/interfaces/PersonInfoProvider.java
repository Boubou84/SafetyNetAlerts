package com.safetynet.safetynetalerts.interfaces;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;

import java.io.IOException;
import java.util.List;

public interface PersonInfoProvider {
    Person updatePerson(Person person, MedicalRecord medicalRecord, FireStation fireStation);

    Person updatePerson(Person person) throws IOException;

    List<Person> getPersons();
    List<FireStation> getFireStations() throws IOException;
    List<MedicalRecord> getMedicalRecords() throws IOException;
    void updateJsonFile(List<Person> persons, List<FireStation> fireStations, List<MedicalRecord> medicalRecords);

    void updateJsonFile(List<Person> persons);

    void updateJsonFile();
}
