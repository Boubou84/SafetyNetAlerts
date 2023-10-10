package com.safetynet.Safetynetalerts.service;

import com.safetynet.Safetynetalerts.model.MedicalRecord;
import com.safetynet.Safetynetalerts.model.Person;
import com.safetynet.Safetynetalerts.model.PersonInfo;
import com.safetynet.Safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.Safetynetalerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.safetynet.Safetynetalerts.util.AgeUtil.calculateAge;

@Service
public class PersonService {

    private final Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private JsonFileService jsonFileService;

    public List<Person> findPersons(String firstName, String lastName, String city, String address) {
        return personRepository.findPersons(firstName, lastName, city, address);
    }


    public ResponseEntity<Object> getPerson(String firstName, String lastName) {
        validateNames(firstName, lastName);
        Optional<Person> person = personRepository.findByFirstNameAndLastName(firstName, lastName);
        if (person.isPresent()) {
            return new ResponseEntity<>(person.get(), HttpStatus.OK);
        } else {
            logger.error("Personne non trouvée avec le prénom {} et le nom {}", firstName, lastName);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Personne non trouvée");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }


    public PersonInfo getPersonInfoByFirstNameAndLastName(String firstName, String lastName) {
        validateNames(firstName, lastName);
        ResponseEntity<Object> response = getPerson(firstName, lastName);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personne non trouvée");
        }

        Person person = (Person) response.getBody();

        MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dossier médical non trouvé"));

        PersonInfo personInfo = new PersonInfo();
        personInfo.setPerson(person);
        personInfo.setMedicalRecord(medicalRecord);
        personInfo.setAge(calculateAge(medicalRecord.getBirthdate()));

        logger.info("Informations trouvées : {}", personInfo);
        return personInfo;
    }


    public List<String> getEmailsByCity(String city) {
        List<Person> personsInCity = personRepository.findPersons(null, null, city, null);
        return personsInCity.stream()
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }

    private void updateJsonFile() {

    }


    public Person addPerson(Person person) {
        validatePerson(person);
        if (personRepository.addPerson(person) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Échec de l'ajout de la personne");
        }
        updateJsonFile();
        logger.info("Personne ajoutée avec succès : {}", person);
        return person;
    }

    private void validateNames(String firstName, String lastName) {
        if (firstName == null || lastName == null || firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            logger.error("Le prénom et le nom ne peuvent pas être nuls ou vides");  // Ajout du log d'erreur
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le prénom et le nom ne peuvent pas être nuls ou vides");
        }
    }

    private void validatePerson(Person person) {
        if (person == null || person.getFirstName() == null || person.getLastName() == null ||
                person.getFirstName().trim().isEmpty() || person.getLastName().trim().isEmpty()) {
            logger.error("Les informations de la personne sont invalides");  // Ajout du log d'erreur
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les informations de la personne sont invalides");
        }
    }

    public Person updatePerson(Person person) {
        validatePerson(person);
        if (personRepository.updatePerson(person) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personne non trouvée pour la mise à jour");
        }
        updateJsonFile();
        return person;
    }

    public void deletePerson(String firstName, String lastName) {
        validateNames(firstName, lastName);
        if (!personRepository.deletePerson(firstName, lastName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Échec de la suppression de la personne");
        }
        updateJsonFile();
    }
}
