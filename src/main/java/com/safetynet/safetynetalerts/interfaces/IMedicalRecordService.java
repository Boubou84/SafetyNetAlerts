package com.safetynet.safetynetalerts.interfaces;

import com.safetynet.safetynetalerts.model.MedicalRecord;

public interface IMedicalRecordService {
    MedicalRecord getMedicalRecord(String firstName, String lastName);
}
