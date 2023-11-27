package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.interfaces.PersonInfoProvider;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonInfoService implements PersonInfoProvider {
    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public PersonInfoService(PersonRepository personRepository, FireStationRepository fireStationRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public List<Person> getPersons() {
        return personRepository.getPersons();
    }

    @Override
    public List<FireStation> getFireStations() {
        return fireStationRepository.getFireStations();
    }

    @Override
    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecordRepository.getMedicalRecords();
    }
}
