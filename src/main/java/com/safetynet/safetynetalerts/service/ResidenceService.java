package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.DTO.InfoResidents;
import com.safetynet.safetynetalerts.DTO.Residence;
import com.safetynet.safetynetalerts.interfaces.IResidenceService;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.safetynet.safetynetalerts.util.AgeUtil.calculateAge;

/**
 * Service pour gérer les fonctionnalités liées aux résidences.
 * Fournit des méthodes pour obtenir des informations sur les résidences et les résidents.
 */

@Service
public class ResidenceService implements IResidenceService {

    private static final Logger logger = LoggerFactory.getLogger(ResidenceService.class);
    private final FireStationRepository fireStationRepository;

    private final PersonRepository personRepository;

    private final MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public ResidenceService(FireStationRepository fireStationRepository, PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.fireStationRepository = fireStationRepository;
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public Residence getResidenceInfo(String address) {
        return getResidenceByAddress(address);
    }

    @Override
    public List<Residence> getResidencesByStations(List<Integer> stationNumbers) throws IOException {
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
    @Override
    public Residence getResidenceByAddress(String address) {
        List<Person> persons = personRepository.findByAddress(address);
        List<InfoResidents> infoResidentsList = persons.stream()
                .map(this::getInfoResidents)
                .collect(Collectors.toList());

        return new Residence(address, infoResidentsList);
    }
    @Override
    public InfoResidents getInfoResidents(Person person) {
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


