package com.safetynet.safetynetalerts.interfaces;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;

import java.io.IOException;
import java.util.List;

public interface PersonInfoProvider {
    List<Person> getPersons();
    List<FireStation> getFireStations() throws IOException;
    List<MedicalRecord> getMedicalRecords() throws IOException;
}
