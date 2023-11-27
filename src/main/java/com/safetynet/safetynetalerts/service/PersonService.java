package com.safetynet.safetynetalerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.error.CustomErrorType;
import com.safetynet.safetynetalerts.exception.AlreadyExistException;
import com.safetynet.safetynetalerts.exception.NotFoundException;
import com.safetynet.safetynetalerts.interfaces.IPersonService;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.PersonInfo;
import com.safetynet.safetynetalerts.model.Root;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import com.safetynet.safetynetalerts.repository.RootRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new NotFoundException("Personne non trouvée avec le prénom " + firstName + " et le nom de famille " + lastName));

        MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new NotFoundException("Dossier médical non trouvé pour " + firstName + " " + lastName));
        return new PersonInfo(person, medicalRecord, calculateAge(medicalRecord.getBirthdate()));
    }

    @Override
    public List<String> getEmailsByCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new NotFoundException("La ville ne peut pas être nulle ou vide");
        }
        List<String> emails = personRepository.findPersons(null, null, city, null)
                .stream()
                .map(Person::getEmail)
                .collect(Collectors.toList());

        if (emails.isEmpty()) {
            throw new NotFoundException("Cette ville n'existe pas, veuillez réessayer !");
        }
        return emails;
    }

    public Person addPerson(Person newPerson) throws IOException {

        Root root = rootRepository.getRoot();
        List<Person> persons = root.getPersons();

        boolean exists = persons.stream()
                .anyMatch(p -> p.getFirstName().equalsIgnoreCase(newPerson.getFirstName())
                        && p.getLastName().equalsIgnoreCase(newPerson.getLastName()));

        if (exists) {
            throw new AlreadyExistException("La personne existe déjà.");
        }

        personRepository.addPerson(newPerson);
        return newPerson;
    }

    public Person updatePerson(String oldFirstName, String oldLastName, Person updatedPerson) throws IOException {
        if (updatedPerson == null || updatedPerson.getFirstName().trim().isEmpty() || updatedPerson.getLastName().trim().isEmpty()) {
            throw new NotFoundException("Le prénom et le nom de la personne sont obligatoires.");
        }

        if (oldFirstName == null || oldFirstName.trim().isEmpty() || oldLastName == null || oldLastName.trim().isEmpty()) {
            throw new NotFoundException("Le prénom et le nom actuels de la personne sont obligatoires pour la mise à jour.");
        }

        return personRepository.updatePerson(oldFirstName, oldLastName, updatedPerson);
    }

    public void deletePerson(String firstName, String lastName) throws IOException {

        Root root = rootRepository.getRoot();
        List<Person> persons = root.getPersons();
        Optional<Person> personToDelete = persons.stream()
                .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                .findFirst();
        if (personToDelete.isEmpty()) {
            throw new NotFoundException("Personne non trouvée");
        }
        persons.remove(personToDelete.get());
        rootRepository.write(root);
        personRepository.deletePerson(firstName, lastName);
    }
}

