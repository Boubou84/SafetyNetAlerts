package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.FireResponse;
import com.safetynet.safetynetalerts.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalerts.dto.PersonDetails;
import com.safetynet.safetynetalerts.dto.PersonFireStationDTO;
import com.safetynet.safetynetalerts.exception.AlreadyExistException;
import com.safetynet.safetynetalerts.exception.NotFoundException;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            throw new NotFoundException("L'adresse ne peut pas être nulle ou vide");
        }
        List<Person> personsAddress = personRepository.findByAddress(address);
        Optional<FireStation> fireStation = fireStationRepository.findFireStationByAddress(address);

        if (personsAddress.isEmpty()) {
            throw new NotFoundException("Cette adresse n'existe pas, veuillez réessayer !");
        }

        List<PersonDetails> personDetailsList = personsAddress.stream().map(p -> {
            PersonDetails details = new PersonDetails();
            details.setFirstName(p.getFirstName());
            details.setLastName(p.getLastName());
            details.setPhone(p.getPhone());
            details.setAddress(address);
            details.setCity(p.getCity());
            details.setZip(p.getZip());
            details.setEmail(p.getEmail());
            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(p.getFirstName(), p.getLastName()).orElse(null);

            if (medicalRecord != null) {
                details.setAge(AgeUtil.calculateAge(medicalRecord.getBirthdate()));
                details.setMedications(medicalRecord.getMedications());
                details.setAllergies(medicalRecord.getAllergies());
            }
            return details;
        }).toList();

        FireResponse response = new FireResponse();
        response.setPersons(personDetailsList);
        fireStation.ifPresent(fs -> response.setStation(fs.getStation()));
        logger.info("Détails de l'incendie récupérés avec succès pour l'adresse {}", address);
        return response;
    }

    @Override
    public FireStationCoverageDTO getPersonsCoveredByStation(int station) throws IOException {
        if (station <= 0) {
            throw new NotFoundException("Le numéro de la station ne peut pas être null ou négatif.");
        }
        List<Person> persons = personRepository.findPersonsByStationNumber(station);

        if (persons == null || persons.isEmpty()) {
            throw new NotFoundException("Aucune personne trouvée pour la station numéro " + station);
        }

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
        if (fireStationRepository.findFireStationByAddress(fireStation.getAddress()).isPresent()) {
            throw new AlreadyExistException("La station de pompiers existe déjà à l'adresse spécifiée.");
        }
        fireStationRepository.addFireStation(fireStation);

        return fireStation;
    }

    public FireStation updateFireStation(FireStation fireStation) {
        fireStationRepository.updateFireStation(fireStation);
        return fireStationRepository.findFireStationByAddress(fireStation.getAddress())
                .orElseThrow(() -> new NotFoundException("Station non trouvée"));
    }

    public void deleteFireStation(String address) {
        fireStationRepository.deleteFireStation(address);
    }
}
