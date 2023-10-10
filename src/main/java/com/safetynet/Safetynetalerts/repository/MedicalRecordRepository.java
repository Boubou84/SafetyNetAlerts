package com.safetynet.Safetynetalerts.repository;

import com.safetynet.Safetynetalerts.model.MedicalRecord;
import com.safetynet.Safetynetalerts.service.JsonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Référentiel pour gérer les opérations liées aux données des dossiers médicaux.
 * Permet de récupérer, de sauvegarder et de manipuler les dossiers médicaux.
 */

@Repository
public class MedicalRecordRepository {

    private List<MedicalRecord> medicalRecords;

    @Autowired
    private JsonFileService jsonFileService;

    @Autowired
    public MedicalRecordRepository(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
        this.medicalRecords = jsonFileService.getMedicalRecords();
    }

    public Optional<MedicalRecord> findByFirstNameAndLastName(String firstName, String lastName) {
        return medicalRecords.stream()
                .filter(m -> m.getFirstName().equals(firstName) && m.getLastName().equals(lastName))
                .findFirst();
    }
}
