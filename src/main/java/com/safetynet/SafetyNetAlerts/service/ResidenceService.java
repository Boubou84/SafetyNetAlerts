package com.safetynet.SafetyNetAlerts.service;

import com.safetynet.SafetyNetAlerts.DTO.InfoResidents;
import com.safetynet.SafetyNetAlerts.DTO.Residence;
import com.safetynet.SafetyNetAlerts.model.MedicalRecord;
import com.safetynet.SafetyNetAlerts.model.Person;
import com.safetynet.SafetyNetAlerts.repository.FireStationRepository;
import com.safetynet.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.safetynet.SafetyNetAlerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.safetynet.SafetyNetAlerts.util.AgeUtil.calculateAge;

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


