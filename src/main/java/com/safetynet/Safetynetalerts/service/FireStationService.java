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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    JsonFileService jsonFileService;

    private final Logger logger = LoggerFactory.getLogger(FireStationService.class);


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
        logger.info("Détails de l'incendie récupérés avec succès pour l'adresse {}", address);
        return response;
    }

    public FireStationCoverageDTO getPersonsCoveredByStation(int stationNumber) {
        List<FireStation> fireStations = jsonFileService.getFireStations().stream()
                .filter(fs -> fs.getStation() == stationNumber)
                .collect(Collectors.toList());

        if (fireStations.isEmpty()) {
            return new FireStationCoverageDTO(new ArrayList<>(), 0, 0);
        }

        List<String> addresses = fireStations.stream()
                .map(FireStation::getAddress)
                .collect(Collectors.toList());

        List<Person> persons = jsonFileService.getPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .collect(Collectors.toList());

        List<PersonFireStationDTO> personDTOs = persons.stream()
                .map(p -> new PersonFireStationDTO(p.getFirstName(), p.getLastName(), p.getAddress(), p.getPhone()))
                .collect(Collectors.toList());

        long adultsCount = persons.stream()
                .filter(p -> {
                    MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(p.getFirstName(), p.getLastName()).orElse(null);
                    return medicalRecord != null && AgeUtil.calculateAge(medicalRecord.getBirthdate()) > 18;
                })
                .count();

        long childrenCount = persons.size() - adultsCount;

        return new FireStationCoverageDTO(personDTOs, (int) adultsCount, (int) childrenCount);
    }
}