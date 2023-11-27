package com.safetynet.safetynetalerts.util;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.Root;
import com.safetynet.safetynetalerts.repository.RootRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final RootRepository rootRepository;
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;

    public DataLoader(RootRepository rootRepository) {
        this.rootRepository = rootRepository;
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

    private void loadData() throws IOException {
        Root rootData = rootRepository.getRoot();

        persons = rootData.getPersons();
        firestations = rootData.getFireStations();
        medicalrecords = rootData.getMedicalRecords();

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
}
