package com.safetynet.Safetynetalerts.repository;

import com.safetynet.Safetynetalerts.model.FireStation;
import com.safetynet.Safetynetalerts.model.MedicalRecord;
import com.safetynet.Safetynetalerts.model.Person;
import com.safetynet.Safetynetalerts.service.JsonFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Référentiel pour gérer les opérations liées aux données des personnes.
 * Fournit des méthodes pour récupérer et manipuler les informations sur les personnes.
 */

@Repository
public class PersonRepository {

    private static final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;
    @Autowired
    private JsonFileService jsonFileService;



    public Optional<Person> findByFirstNameAndLastName(String firstName, String lastName) {
        return jsonFileService.getPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public List<Person> findByAddress(String address) {
        return jsonFileService.getPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }

    public List<Person> findPersonsByStationNumber(int stationNumber) {
        return jsonFileService.getPersons().stream()
                .filter(p -> p.getCity().equalsIgnoreCase(String.valueOf(stationNumber)))
                .collect(Collectors.toList());
    }

    public List<Person> findAllByAddresses(List<String> addresses) {
        return jsonFileService.getPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .collect(Collectors.toList());
    }


    public List<Person> findPersons(String firstName, String lastName, String city, String address) {
        return jsonFileService.getPersons().stream()
                .filter(p -> firstName == null || p.getFirstName().equalsIgnoreCase(firstName))
                .filter(p -> lastName == null || p.getLastName().equalsIgnoreCase(lastName))
                .filter(p -> city == null || p.getCity().equalsIgnoreCase(city))
                .filter(p -> address == null || p.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }

    public Person addPerson(Person person) {
        if (person == null || persons.stream().anyMatch(p -> p.getFirstName().equals(person.getFirstName())
                && p.getLastName().equals(person.getLastName()))) {
            logger.error("Échec de l'ajout de la personne : une personne avec le prénom {} et le nom {} existe déjà",
                    person.getFirstName(), person.getLastName());
            throw new IllegalArgumentException("La personne existe déjà ou l'entrée est nulle");
        }

        persons.add(person);
        jsonFileService.updateJsonFile(persons, firestations, medicalrecords);
        logger.info("Personne ajoutée avec succès : {}", person);
        return person;
    }

    public Person updatePerson(Person person) {
        Optional<Person> existingPerson = findByFirstNameAndLastName(person.getFirstName(), person.getLastName());

        if (existingPerson.isPresent()) {
            Person updatedPerson = existingPerson.get();
            updatedPerson.setAddress(person.getAddress());
            updatedPerson.setCity(person.getCity());
            updatedPerson.setZip(person.getZip());
            updatedPerson.setPhone(person.getPhone());
            updatedPerson.setEmail(person.getEmail());
            logger.info("Person updated: {}", updatedPerson);
            jsonFileService.updateJsonFile(persons, firestations, medicalrecords);
            return updatedPerson;
        } else {
            logger.warn("Person not found: {}", person);
        }
        return existingPerson.orElse(null);
    }

    public void updateJsonFile() {
        jsonFileService.updateJsonFile(persons, firestations, medicalrecords);
        logger.info("Fichier JSON mis à jour avec succès");
    }

    public boolean deletePerson(String firstName, String lastName) {
        boolean removed = persons.removeIf(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName));
        if (removed) {
            logger.info("Personne avec ce nom {} {} supprimé avec succès", firstName, lastName);
            updateJsonFile();
            return true;
        } else {
            logger.warn("Aucune personne trouvée avec ce nom {} {}", firstName, lastName);
            return false;
        }
    }
}
