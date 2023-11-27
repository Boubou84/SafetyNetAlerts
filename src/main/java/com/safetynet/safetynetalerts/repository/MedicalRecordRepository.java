package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.exception.NotFoundException;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Root;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Référentiel pour gérer les opérations liées aux données des dossiers médicaux.
 * Permet de récupérer, de sauvegarder et de manipuler les dossiers médicaux.
 */

@Repository
public class MedicalRecordRepository {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordRepository.class);

    private final RootRepository rootRepository;

    private List<MedicalRecord> medicalrecords;

    public MedicalRecordRepository(RootRepository rootRepository, List<MedicalRecord> medicalrecords) throws IOException {
        this.rootRepository = rootRepository;
        this.medicalrecords = medicalrecords;
        initializeMedicalRecords();
    }

    @PostConstruct
    public void initializeMedicalRecords() throws IOException {
        Root root = rootRepository.getRoot();
        if (root != null) {
            this.medicalrecords = root.getMedicalRecords();
        } else {
            throw new NotFoundException("L'objet Root n'a pas pu être récupéré.");
        }
    }

    public Optional<MedicalRecord> findByFirstNameAndLastName(String firstName, String lastName) {
        return medicalrecords.stream()
                .filter(m -> m.getFirstName().equals(firstName) && m.getLastName().equals(lastName))
                .findFirst();
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalrecords;
    }

    public void addMedicalRecord(MedicalRecord medicalRecord) {
        if (medicalRecord == null || medicalRecord.getFirstName().trim().isEmpty() || medicalRecord.getLastName().trim().isEmpty()) {
            logger.error("La tentative d'ajout d'un dossier médical nul a été détectée");
            throw new NotFoundException("Le dossier médical ne peut pas être null ou vide !");
        }

        medicalrecords.add(medicalRecord);
        updateRoot();
        logger.info("Dossier médical ajouté avec succès : {}", medicalRecord);
    }

    public void updateMedicalRecord(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> existingRecord = findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        if (existingRecord.isPresent()) {
            int index = medicalrecords.indexOf(existingRecord.get());
            medicalrecords.set(index, medicalRecord);
            updateRoot();
            logger.info("Dossier médical mis à jour avec succès : {}", medicalRecord);
        } else {
            throw new NotFoundException("Le dossier médical n'existe pas");
        }
    }

    public void deleteMedicalRecord(String firstName, String lastName) {
        boolean isRemoved = medicalrecords.removeIf(m ->
                m.getFirstName().equalsIgnoreCase(firstName) && m.getLastName().equalsIgnoreCase(lastName));

        if (isRemoved) {
            updateRoot();
            logger.info("Dossier médical supprimé avec succès pour : {} {}", firstName, lastName);
        } else {
            throw new NotFoundException("Le dossier médical n'existe pas");
        }
    }

    private void updateRoot() {
        try {
            Root root = rootRepository.getRoot();
            root.setMedicalRecords(medicalrecords);
            rootRepository.write(root);
        } catch (IOException e) {
            logger.error("Erreur lors de la mise à jour des données", e);
            throw new RuntimeException("Erreur lors de la mise à jour des données", e);
        }
    }
}
