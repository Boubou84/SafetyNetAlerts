package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.interfaces.IMedicalRecordService;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordService implements IMedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }
    @Override
    public MedicalRecord getMedicalRecord(String firstName, String lastName) {
        return getMedicalRecord(firstName, lastName);
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecordRepository.addMedicalRecord(medicalRecord);
        return medicalRecord;
    }

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecordRepository.updateMedicalRecord(medicalRecord);
        return medicalRecord;
    }

    public void deleteMedicalRecord(String firstName, String lastName) {
        medicalRecordRepository.deleteMedicalRecord(firstName, lastName);
    }
}
