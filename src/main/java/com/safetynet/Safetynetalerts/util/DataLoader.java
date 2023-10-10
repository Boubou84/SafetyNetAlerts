package com.safetynet.Safetynetalerts.util;

import com.safetynet.Safetynetalerts.model.FireStation;
import com.safetynet.Safetynetalerts.model.MedicalRecord;
import com.safetynet.Safetynetalerts.model.Person;
import com.safetynet.Safetynetalerts.service.JsonFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final JsonFileService jsonFileService;
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;

    @Autowired
    public DataLoader(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    @Override
    public void run(String... args) {
        try {
            loadData();
            logger.info("Data loaded successfully");
        } catch (Exception e) {
            logger.error("Failed to load data", e);
        }
    }

    private void loadData() {
        persons = jsonFileService.getPersons();
        firestations = jsonFileService.getFireStations();
        medicalrecords = jsonFileService.getMedicalRecords();

        if(persons == null) {
            logger.warn("Les données des personnes sont nulles. Initialisation d'une liste vide.");
            persons = new ArrayList<>();
        }

        if(firestations == null) {
            logger.warn("Les données des casernes de pompiers sont nulles. Initialisation d'une liste vide.");
            firestations = new ArrayList<>();
        }

        if(medicalrecords == null) {
            logger.warn("Les données des dossiers médicaux sont nulles. Initialisation d'une liste vide.");
            medicalrecords = new ArrayList<>();
        }
    }

    public List<Person> getPersons() {
        if (persons == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(persons);
    }
    public List<MedicalRecord> getMedicalRecords() {
        if (medicalrecords == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(medicalrecords);
    }

    public List<FireStation> getFireStations() {
        if (firestations == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(firestations);
    }
}
