package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.DTO.FireResponse;
import com.safetynet.safetynetalerts.DTO.FireStationCoverageDTO;
import com.safetynet.safetynetalerts.DTO.PersonDetails;
import com.safetynet.safetynetalerts.DTO.PersonFireStationDTO;
import com.safetynet.safetynetalerts.interfaces.IFireStationService;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import com.safetynet.safetynetalerts.repository.RootRepository;
import com.safetynet.safetynetalerts.util.AgeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour gérer les fonctionnalités liées aux stations de pompiers.
 * Fournit des méthodes pour obtenir des informations sur la zone de couverture et les détails des stations.
 */

@Service
public class FireStationService implements IFireStationService {

    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final RootRepository rootRepository;

    @Autowired
    public FireStationService(PersonRepository personRepository, FireStationRepository fireStationRepository, MedicalRecordRepository medicalRecordRepository, RootRepository rootRepository) {
        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.rootRepository = rootRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(FireStationService.class);

    @Override
    public FireResponse getFireDetailsByAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse ne peut pas être nulle ou vide");
        }
        List<Person> personsAddress = personRepository.findByAddress(address);
        Optional<FireStation> fireStation = fireStationRepository.findFireStationByAddress(address);

        List<PersonDetails> personDetailsList = personsAddress.stream().map(p -> {
            PersonDetails details = new PersonDetails();
            details.setFirstName(p.getFirstName() + " " + p.getLastName());
            details.setPhone(p.getPhone());
            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(p.getFirstName(), p.getLastName()).orElse(null);

            if (medicalRecord != null) {
                details.setAge(AgeUtil.calculateAge(medicalRecord.getBirthdate()));
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

    @Override
    public FireStationCoverageDTO getFireStationCoverage(int stationNumber) throws IOException {
        Optional<FireStation> fireStations = fireStationRepository.findFireStationByAddress(String.valueOf(stationNumber));

        if (fireStations.isEmpty()) {
            return new FireStationCoverageDTO(new ArrayList<>(), 0, 0);
        }

        List<String> addresses = fireStations.stream()
                .map(FireStation::getAddress)
                .collect(Collectors.toList());

        List<Person> persons = rootRepository.getRoot().getPersons().stream()
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
    @Override
    public FireStationCoverageDTO getPersonsCoveredByStation(int station) throws IOException {
        List<Person> persons = personRepository.findPersonsByStationNumber(station);

        List<PersonFireStationDTO> personsDTO = new ArrayList<>();
        int adults = 0;
        int children = 0;

        for (Person person : persons) {
            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()).orElse(null);

            PersonFireStationDTO personDTO = new PersonFireStationDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone());
            personsDTO.add(personDTO);

            if (medicalRecord != null && medicalRecord.getBirthdate() != null && !medicalRecord.getBirthdate().trim().isEmpty()) {
                int age = AgeUtil.calculateAge(medicalRecord.getBirthdate());
                personsDTO.add(new PersonFireStationDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone()));

                if (age > 18) {
                    adults++;
                } else {
                    children++;
                }
            }else {
                logger.warn("Date de naissance manquante pour {} {}", person.getFirstName(), person.getLastName());
            }
        }

        return new FireStationCoverageDTO(personsDTO, adults, children);
    }

    public FireStation addFireStation(FireStation fireStation) {
        fireStationRepository.addFireStation(fireStation);
        return fireStation;
    }

    public FireStation updateFireStation(FireStation fireStation) {
        fireStationRepository.updateFireStation(fireStation);
        return fireStation;
    }

    public void deleteFireStation(String address) {
        fireStationRepository.deleteFireStation(address);
    }
}
