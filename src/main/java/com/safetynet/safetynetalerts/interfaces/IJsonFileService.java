package com.safetynet.safetynetalerts.interfaces;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import java.io.IOException;
import java.util.List;

public interface IJsonFileService {

    void load() throws IOException;

    List<Person> getPersons();

    List<FireStation> getFireStations();

    List<MedicalRecord> getMedicalRecords();

    void updateJsonFile(List<Person> persons, List<FireStation> fireStations, List<MedicalRecord> medicalRecords);
}
