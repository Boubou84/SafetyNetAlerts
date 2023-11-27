package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.exception.AlreadyExistException;
import com.safetynet.safetynetalerts.exception.NotFoundException;
import com.safetynet.safetynetalerts.interfaces.IMedicalRecordService;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalRecordService implements IMedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }
    @Override
    public MedicalRecord getMedicalRecord(String firstName, String lastName) {
        return getMedicalRecord(firstName, lastName);
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        if (medicalRecordRepository.findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName()).isPresent()) {
            throw new AlreadyExistException("Le dossier médical exist déjà !");
        }
        medicalRecordRepository.addMedicalRecord(medicalRecord);
        return medicalRecord;
    }

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> existingRecord = medicalRecordRepository.findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        if (existingRecord.isPresent()) {
            medicalRecordRepository.updateMedicalRecord(medicalRecord);
            return medicalRecord;
        } else {
            throw new NotFoundException("Le dossier médical n'existe pas");
        }
    }

    public void deleteMedicalRecord(String firstName, String lastName) {
        medicalRecordRepository.deleteMedicalRecord(firstName, lastName);
    }
}
