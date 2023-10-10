package com.safetynet.Safetynetalerts.service;

import com.safetynet.Safetynetalerts.DTO.ChildAlertDTO;
import com.safetynet.Safetynetalerts.DTO.ChildAlertInfo;
import com.safetynet.Safetynetalerts.model.MedicalRecord;
import com.safetynet.Safetynetalerts.model.Person;
import com.safetynet.Safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.Safetynetalerts.repository.PersonRepository;
import com.safetynet.Safetynetalerts.util.AgeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service pour gérer les fonctionnalités liées aux alertes d'enfants.
 * Fournit des méthodes pour récupérer des informations sur les enfants en cas d'urgence.
 */

@Service
public class ChildAlertService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public ChildAlertDTO getChildAlertInfo(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse ne peut pas être nulle ou vide");
        }
        List<Person> persons = personRepository.findByAddress(address);
        List<ChildAlertInfo> childrenInfo = new ArrayList<>();
        List<String> householdMembers = new ArrayList<>();

        for (Person person : persons) {
            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()).orElse(null);

            if (medicalRecord != null) {
                int age = AgeUtil.calculateAge(medicalRecord.getBirthdate());
                if (age <= 18) {
                    childrenInfo.add(new ChildAlertInfo(person.getFirstName(), person.getLastName(), age));
                }
            }

            householdMembers.add(person.getFirstName() + " " + person.getLastName());
        }

        return new ChildAlertDTO(childrenInfo, householdMembers);
    }
}
