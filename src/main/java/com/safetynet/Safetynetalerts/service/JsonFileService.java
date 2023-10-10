package com.safetynet.Safetynetalerts.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.Safetynetalerts.model.FireStation;
import com.safetynet.Safetynetalerts.model.MedicalRecord;
import com.safetynet.Safetynetalerts.model.Person;
import com.safetynet.Safetynetalerts.model.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JsonFileService {
    @JsonIgnoreProperties(ignoreUnknown = true)

    private final String filePath = "/Users/senepascal/Desktop/Alert/data.json";
    private List<Person> persons = new ArrayList<>();
    private List<FireStation> firestations = new ArrayList<>();
    private List<MedicalRecord> medicalrecords = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(JsonFileService.class);

    public JsonFileService() {
        try {
            logger.info("Initialisation de JsonFileService");
            load();
        } catch (FileNotFoundException e) {
            logger.error("Le fichier data.json n'a pas été trouvé", e);
        }
    }

    public void load() throws FileNotFoundException {
        File file = new File(filePath);

        logger.info("Tentative de chargement du fichier JSON depuis {}", filePath);

        if (file.exists() && !file.isDirectory()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Root root = objectMapper.readValue(file, Root.class);

                if (root != null) {
                    this.persons = root.getPersons() != null ? root.getPersons() : new ArrayList<>();
                    logger.info("Nombre de personnes chargées : {}", this.persons.size());
                    this.firestations = root.getFireStations() != null ? root.getFireStations() : new ArrayList<>();
                    logger.info("Nombre de casernes de pompiers chargées : {}", this.firestations.size());
                    this.medicalrecords = root.getMedicalrecords() != null ? root.getMedicalrecords() : new ArrayList<>();
                    logger.info("Nombre de dossiers médicaux chargés : {}", this.medicalrecords.size());

                    logger.info("Données JSON chargées avec succès");
                } else {
                    logger.error("La racine du fichier JSON est null");
                }
            } catch (Exception e) {
                logger.error("Erreur lors de la lecture du fichier JSON", e);
            }
        } else {
            logger.error("Le fichier JSON n'existe pas à l'emplacement spécifié");
        }
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<FireStation> getFireStations() {
        return firestations;
    }

    public List<MedicalRecord> getMedicalRecords(){
        return medicalrecords;
    }

    public void updateJsonFile(List<Person> persons, List<FireStation> fireStations, List<MedicalRecord> medicalRecords) {
        backupOldJsonFile();
        updateJsonData(persons, fireStations, medicalRecords);
    }

    private void backupOldJsonFile() {
        File file = new File(filePath);
        File backupFile = new File(filePath + ".backup");

        if (file.exists()) {
            try {
                Files.copy(file.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                logger.info("Fichier JSON sauvegardé avec succès.");
            } catch (IOException e) {
                logger.error("Échec de la sauvegarde du fichier JSON", e);
            }
        }
    }

    private void updateJsonData(List<Person> persons, List<FireStation> fireStations, List<MedicalRecord> medicalRecords) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> dataMap = new HashMap<>();
        File file = new File(filePath);

        try {
            String jsonData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataMap);
            Files.write(file.toPath(), jsonData.getBytes());
            logger.info("Fichier JSON mis à jour avec succès.");
        } catch (IOException e) {
            logger.error("Échec de la mise à jour du fichier JSON", e);
            restoreJsonFileFromBackup();
        }
    }

    private void restoreJsonFileFromBackup() {
        File file = new File(filePath);
        File backupFile = new File(filePath + ".backup");

        if (backupFile.exists()) {
            try {
                Files.copy(backupFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                logger.info("Fichier JSON restauré à partir de la sauvegarde.");
            } catch (IOException e) {
                logger.error("Échec de la restauration du fichier JSON", e);
            }
        }
    }

    private Root loadData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File("/Users/senepascal/Desktop/Alert/data.json");
            return objectMapper.readValue(jsonFile, Root.class);
        } catch (IOException e) {
            logger.error("Failed to load data", e);
            return new Root();
        }
    }
}
