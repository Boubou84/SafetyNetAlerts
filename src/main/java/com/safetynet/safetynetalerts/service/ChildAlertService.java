package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.DTO.ChildAlertDTO;
import com.safetynet.safetynetalerts.DTO.ChildAlertInfo;
import com.safetynet.safetynetalerts.exception.NotFoundException;
import com.safetynet.safetynetalerts.interfaces.IChildAlertService;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import com.safetynet.safetynetalerts.util.AgeUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service pour gérer les fonctionnalités liées aux alertes d'enfants.
 * Fournit des méthodes pour récupérer des informations sur les enfants en cas d'urgence.
 */

@Service
public class ChildAlertService implements IChildAlertService {
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public ChildAlertService(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public ChildAlertDTO getChildAlertInfo(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new NotFoundException("L'adresse ne peut pas être nulle ou vide");
        }
        List<Person> persons = personRepository.findByAddress(address);
        List<ChildAlertInfo> childrenInfo = new ArrayList<>();
        List<String> householdMembers = new ArrayList<>();

        if (persons.isEmpty()) {
            throw new NotFoundException("Cette adresse n'existe pas, veuillez réessayer !");
        }

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
