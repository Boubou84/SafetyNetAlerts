package com.safetynet.Safetynetalerts.service;

import com.safetynet.Safetynetalerts.DTO.InfoResidents;
import com.safetynet.Safetynetalerts.DTO.Residence;
import com.safetynet.Safetynetalerts.model.MedicalRecord;
import com.safetynet.Safetynetalerts.model.Person;
import com.safetynet.Safetynetalerts.repository.FireStationRepository;
import com.safetynet.Safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.Safetynetalerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.safetynet.Safetynetalerts.util.AgeUtil.calculateAge;

/**
 * Service pour gérer les fonctionnalités liées aux résidences.
 * Fournit des méthodes pour obtenir des informations sur les résidences et les résidents.
 */

@Service
public class ResidenceService {

    private static final Logger logger = LoggerFactory.getLogger(ResidenceService.class);
    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public List<Residence> getResidencesByStations(List<Integer> stationNumbers) {
        if (stationNumbers == null || stationNumbers.isEmpty()) {
            throw new IllegalArgumentException("La liste des numéros de station ne peut pas être nulle ou vide");
        }
        List<String> addresses = fireStationRepository.findAddressesByStations(stationNumbers);
        logger.info("Adresses pour les stations {}: {}", stationNumbers, addresses);

        if (addresses.isEmpty()) {
            logger.warn("Aucune adresse trouvée pour les stations {}", stationNumbers);
            return Collections.emptyList();
        }

        return addresses.stream()
                .map(this::getResidenceByAddress)
                .collect(Collectors.toList());
    }
    private Residence getResidenceByAddress(String address) {
        List<Person> persons = personRepository.findByAddress(address);
        List<InfoResidents> infoResidentsList = persons.stream()
                .map(this::getInfoResidents)
                .collect(Collectors.toList());

        return new Residence(address, infoResidentsList);
    }

    private InfoResidents getInfoResidents(Person person) {
        MedicalRecord medicalRecord = medicalRecordRepository
                .findByFirstNameAndLastName(person.getFirstName(), person.getLastName())
                .orElse(new MedicalRecord());

        int age = calculateAge(medicalRecord.getBirthdate());
        return new InfoResidents(
                person.getFirstName() + " " + person.getLastName(),
                person.getPhone(),
                age,
                medicalRecord.getMedications(),
                medicalRecord.getAllergies()
        );
    }
}


