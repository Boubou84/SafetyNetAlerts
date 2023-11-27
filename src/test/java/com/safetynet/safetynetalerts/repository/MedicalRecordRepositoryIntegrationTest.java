package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Root;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
 class MedicalRecordRepositoryIntegrationTest {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private RootRepository rootRepository;

    private Root originalRoot;

    @BeforeEach
    void setUp() throws IOException {
        originalRoot = rootRepository.getRoot();
    }

    @AfterEach
    void tearDown() throws IOException {
        rootRepository.write(originalRoot);
    }

    @Test
    @DisplayName("Doit ajouter un dossier médical avec succès")
    void addMedicalRecord_ValidRecord_AddedSuccessfully() throws IOException {
        MedicalRecord newRecord = new MedicalRecord("NewFirstName", "NewLastName", "01/01/1990", null, null);
        medicalRecordRepository.addMedicalRecord(newRecord);

        Optional<MedicalRecord> retrievedRecord = medicalRecordRepository.findByFirstNameAndLastName("NewFirstName", "NewLastName");
        assertTrue(retrievedRecord.isPresent());
        assertEquals("01/01/1990", retrievedRecord.get().getBirthdate());
    }

    @Test
    @DisplayName("Doit mettre à jour un dossier médical avec succès")
    void updateMedicalRecord_ValidUpdate_UpdatedSuccessfully() throws IOException {
        MedicalRecord existingRecord = new MedicalRecord("ExistingFirstName", "ExistingLastName", "01/01/1990", null, null);
        medicalRecordRepository.addMedicalRecord(existingRecord);

        MedicalRecord updatedRecord = new MedicalRecord("ExistingFirstName", "ExistingLastName", "02/02/1992", null, null);
        medicalRecordRepository.updateMedicalRecord(updatedRecord);

        Optional<MedicalRecord> retrievedRecord = medicalRecordRepository.findByFirstNameAndLastName("ExistingFirstName", "ExistingLastName");
        assertTrue(retrievedRecord.isPresent());
        assertEquals("02/02/1992", retrievedRecord.get().getBirthdate());
    }

    @Test
    @DisplayName("Doit supprimer un dossier médical avec succès")
    void deleteMedicalRecord_ExistingRecord_DeletedSuccessfully() throws IOException {
        String firstName = "FirstNameToDelete";
        String lastName = "LastNameToDelete";
        medicalRecordRepository.addMedicalRecord(new MedicalRecord(firstName, lastName, "01/01/1990", null, null));

        medicalRecordRepository.deleteMedicalRecord(firstName, lastName);
        Optional<MedicalRecord> retrievedRecord = medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName);
        assertFalse(retrievedRecord.isPresent());
    }
}
