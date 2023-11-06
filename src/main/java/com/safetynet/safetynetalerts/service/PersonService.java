package com.safetynet.safetynetalerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.error.CustomErrorType;
import com.safetynet.safetynetalerts.interfaces.IPersonService;
import com.safetynet.safetynetalerts.model.*;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import com.safetynet.safetynetalerts.repository.RootRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.safetynet.safetynetalerts.util.AgeUtil.calculateAge;

@Service
public class PersonService implements IPersonService {
    private final Logger logger = LoggerFactory.getLogger(PersonService.class);
    private final RootRepository rootRepository;

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    private final ObjectMapper objectMapper;
    private final FireStationRepository fireStationRepository;

    @Autowired
    public PersonService(RootRepository rootRepository, PersonRepository personRepository,
                         MedicalRecordRepository medicalRecordRepository,
                         ObjectMapper objectMapper,
                         FireStationRepository fireStationRepository) {
        this.rootRepository = rootRepository;
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.objectMapper = objectMapper;
        this.fireStationRepository = fireStationRepository;
    }

    public List<Person> findPersons(String firstName, String lastName, String city, String address) {
        return personRepository.findPersons(firstName, lastName, city, address);
    }

    @Override
    public ResponseEntity<Object> getPerson(String firstName, String lastName) {
        Optional<Person> person = personRepository.findByFirstNameAndLastName(firstName, lastName);

        if (person.isPresent()) {
            return ResponseEntity.ok(person.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomErrorType("Personne non trouvée"));
        }
    }

    @Override
    public PersonInfo getPersonInfoByFirstNameAndLastName(String firstName, String lastName) {
        Person person = personRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personne non trouvée"));

        MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dossier médical non trouvé"));

        return new PersonInfo(person, medicalRecord, calculateAge(medicalRecord.getBirthdate()));
    }

    @Override
    public List<String> getEmailsByCity(String city) {
        return personRepository.findPersons(null, null, city, null)
                .stream()
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }
    public Person addPerson(Person newPerson) throws IOException {
        if (newPerson == null || newPerson.getFirstName().trim().isEmpty() || newPerson.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Les données de la personne sont invalides.");
        }

        Root root = rootRepository.getRoot();
        List<Person> persons = root.getPersons();

        boolean exists = persons.stream()
                .anyMatch(p -> p.getFirstName().equalsIgnoreCase(newPerson.getFirstName())
                        && p.getLastName().equalsIgnoreCase(newPerson.getLastName()));

        if (exists) {
            throw new IllegalArgumentException("La personne existe déjà.");
        }

        personRepository.addPerson(newPerson);
        return newPerson;
    }

    public Person updatePerson(String oldFirstName, String oldLastName, Person updatedPerson) throws IOException {
        if (updatedPerson == null || updatedPerson.getFirstName().trim().isEmpty() || updatedPerson.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Les données de la personne sont invalides.");
        }

        Root root = rootRepository.getRoot();
        List<Person> persons = root.getPersons();

        Optional<Person> personOptional = persons.stream()
                .filter(p -> p.getFirstName().equals(oldFirstName) && p.getLastName().equals(oldLastName))
                .findFirst();

        if (!personOptional.isPresent()) {
            throw new IllegalArgumentException("La personne à mettre à jour n'existe pas.");
        }

        Person existingPerson = personOptional.get();
        existingPerson.setFirstName(updatedPerson.getFirstName());
        existingPerson.setLastName(updatedPerson.getLastName());

        rootRepository.write(root);
        return existingPerson;
    }


    public boolean deletePerson(String firstName, String lastName) throws IOException {
        try {
            Root root = rootRepository.getRoot();
            List<Person> persons = root.getPersons();
            Optional<Person> personToDelete = persons.stream()
                    .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                    .findFirst();

            persons.remove(personToDelete.get());
            rootRepository.write(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return personRepository.deletePerson(firstName, lastName);
    }

            private void serializeAndLogError(Person person, FireStation fireStation, MedicalRecord medicalRecord) {
        try {
            objectMapper.writeValueAsString(person);
            objectMapper.writeValueAsString(fireStation);
            objectMapper.writeValueAsString(medicalRecord);
        } catch (Exception e) {
            logger.error("Erreur de sérialisation JSON", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur de sérialisation JSON", e);
        }
    }
}

