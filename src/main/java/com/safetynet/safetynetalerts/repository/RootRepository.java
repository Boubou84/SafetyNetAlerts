package com.safetynet.safetynetalerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.model.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;

@Repository
public class RootRepository {

    private String filePath;
    private static final Logger logger = LoggerFactory.getLogger(RootRepository.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final PersonRepository personRepository;

    @Autowired
    public RootRepository(@Value("${data.file.path:src/main/resources/data.json}")
                                  String filePath, @Lazy PersonRepository personRepository) {
        this.filePath = filePath;
        this.personRepository = personRepository;
    }

    public Root getRoot() throws IOException {
        return readRoot();
    }

    protected Root readRoot() throws IOException {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            return objectMapper.readValue(file, Root.class);
        } else {
            logger.error("Le fichier JSON n'existe pas à l'emplacement spécifié");
            return new Root();
        }
    }

    public void reloadData() throws IOException {
        Root updatedRoot = readRoot();

        // Mettre à jour les listes avec les données fraîchement lues
        // Repoduire la meme chose pour FireStationRepository et MedicalRecordRepository

        personRepository.refreshData(updatedRoot.getPersons());
        //this.firestations = updatedRoot.getFireStations();
        //this.medicalrecords = updatedRoot.getMedicalRecords();
    }

    public void write(Root root) throws IOException {
        File file = new File(filePath);
        objectMapper.writeValue(file, root);
        reloadData();
    }
}
