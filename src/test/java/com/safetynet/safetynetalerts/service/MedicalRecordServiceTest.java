package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.exception.AlreadyExistException;
import com.safetynet.safetynetalerts.exception.NotFoundException;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

 class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Doit ajouter un dossier médical avec succès")
    void addMedicalRecord_Success() {
        MedicalRecord medicalRecord = new MedicalRecord();
        when(medicalRecordRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(Optional.empty());

        // Simule l'appel à addMedicalRecord sans spécifier de valeur de retour
        doNothing().when(medicalRecordRepository).addMedicalRecord(any(MedicalRecord.class));

        MedicalRecord savedRecord = medicalRecordService.addMedicalRecord(medicalRecord);
        assertNotNull(savedRecord);

        verify(medicalRecordRepository).addMedicalRecord(any(MedicalRecord.class));
    }

    @Test
    @DisplayName("Doit lancer AlreadyExistException lors de l'ajout d'un dossier médical existant")
    void addMedicalRecord_AlreadyExists() {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");

        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(Optional.of(medicalRecord));

        assertThrows(AlreadyExistException.class, () -> medicalRecordService.addMedicalRecord(medicalRecord));
    }

    @Test
    @DisplayName("Doit mettre à jour un dossier médical avec succès")
    void updateMedicalRecord_Success() {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990", new ArrayList<>(), new ArrayList<>());
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(Optional.of(medicalRecord));

        MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(medicalRecord);
        assertNotNull(updatedRecord);

        verify(medicalRecordRepository).updateMedicalRecord(medicalRecord);
    }

    @Test
    @DisplayName("Doit lancer NotFoundException lors de la mise à jour d'un dossier médical inexistant")
    void updateMedicalRecord_NotFound() {
        String firstName = "John";
        String lastName = "Doe";
        MedicalRecord medicalRecord = new MedicalRecord(firstName, lastName, "01/01/1990", new ArrayList<>(), new ArrayList<>());
        when(medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> medicalRecordService.updateMedicalRecord(medicalRecord));
    }

    @Test
    @DisplayName("Doit supprimer un dossier médical avec succès")
    void deleteMedicalRecord_Success() {
        doNothing().when(medicalRecordRepository).deleteMedicalRecord(anyString(), anyString());

        assertDoesNotThrow(() -> medicalRecordService.deleteMedicalRecord("John", "Doe"));
    }

    @Test
    @DisplayName("Doit lancer NotFoundException lors de la suppression d'un dossier médical inexistant")
    void deleteMedicalRecord_NotFound() {
        doThrow(new NotFoundException("Le dossier médical n'existe pas")).when(medicalRecordRepository).deleteMedicalRecord(anyString(), anyString());

        assertThrows(NotFoundException.class, () -> medicalRecordService.deleteMedicalRecord("John", "Doe"));
    }
}
