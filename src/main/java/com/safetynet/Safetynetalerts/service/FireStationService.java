package com.safetynet.Safetynetalerts.service;

import com.safetynet.Safetynetalerts.DTO.FireResponse;
import com.safetynet.Safetynetalerts.DTO.FireStationCoverageDTO;
import com.safetynet.Safetynetalerts.DTO.PersonDetails;
import com.safetynet.Safetynetalerts.DTO.PersonFireStationDTO;
import com.safetynet.Safetynetalerts.model.FireStation;
import com.safetynet.Safetynetalerts.model.MedicalRecord;
import com.safetynet.Safetynetalerts.model.Person;
import com.safetynet.Safetynetalerts.repository.FireStationRepository;
import com.safetynet.Safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.Safetynetalerts.repository.PersonRepository;
import com.safetynet.Safetynetalerts.util.AgeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.safetynet.Safetynetalerts.util.AgeUtil.calculateAge;

/**
 * Service pour gérer les fonctionnalités liées aux stations de pompiers.
 * Fournit des méthodes pour obtenir des informations sur la zone de couverture et les détails des stations.
 */

@Service
public class FireStationService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public FireResponse getFireDetailsByAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse ne peut pas être nulle ou vide");
        }
        List<Person> personsAddress = personRepository.findByAddress(address);
        Optional<FireStation> fireStation = fireStationRepository.findByAddress(address);

        List<PersonDetails> personDetailsList = personsAddress.stream().map(p -> {
            PersonDetails details = new PersonDetails();
            details.setName(p.getFirstName() + " " + p.getLastName());
            details.setPhone(p.getPhone());
            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(p.getFirstName(), p.getLastName()).orElse(null);

            if (medicalRecord != null) {
                details.setAge(calculateAge(medicalRecord.getBirthdate()));
                details.setMedications(medicalRecord.getMedications());
                details.setAllergies(medicalRecord.getAllergies());
            }
            return details;
        }).collect(Collectors.toList());

        FireResponse response = new FireResponse();
        response.setPersons(personDetailsList);
        fireStation.ifPresent(fs -> response.setStation(fs.getStation()));
        return response;
    }

    public FireStationCoverageDTO getPersonsCoveredByStation(int station) {
        List<Person> persons = personRepository.findPersonsByStationNumber(station);

        List<PersonFireStationDTO> personsDTO = new ArrayList<>();
        int adults = 0;
        int children = 0;

        for (Person person : persons) {
            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()).orElse(null);

            if (medicalRecord != null) {
                int age = AgeUtil.calculateAge(medicalRecord.getBirthdate());
                personsDTO.add(new PersonFireStationDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone()));

                if (age > 18) {
                    adults++;
                } else {
                    children++;
                }
            }
        }

        return new FireStationCoverageDTO(personsDTO, adults, children);
    }

}