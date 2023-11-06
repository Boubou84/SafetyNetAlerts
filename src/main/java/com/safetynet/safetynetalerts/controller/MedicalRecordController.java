package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        MedicalRecord savedMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecord);
        return new ResponseEntity<>(savedMedicalRecord, HttpStatus.CREATED);
    }

    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable String firstName,
                                                             @PathVariable String lastName,
                                                             @RequestBody MedicalRecord medicalRecord) {
        medicalRecord.setFirstName(firstName);
        medicalRecord.setLastName(lastName);
        MedicalRecord updatedMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecord);
        return new ResponseEntity<>(updatedMedicalRecord, HttpStatus.OK);
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String firstName, @PathVariable String lastName) {
        medicalRecordService.deleteMedicalRecord(firstName, lastName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
