package com.safetynet.SafetyNetAlerts.repository;

import com.safetynet.SafetyNetAlerts.model.MedicalRecord;
import com.safetynet.SafetyNetAlerts.util.DataLoader;
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
    public MedicalRecordRepository(DataLoader dataLoader) {
        this.medicalRecords = dataLoader.getRoot().getMedicalrecords();
    }

    public Optional<MedicalRecord> findByFirstNameAndLastName(String firstName, String lastName) {
        return medicalRecords.stream()
                .filter(m -> m.getFirstName().equals(firstName) && m.getLastName().equals(lastName))
                .findFirst();
    }
}
