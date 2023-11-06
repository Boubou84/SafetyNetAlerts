package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.interfaces.PersonInfoProvider;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.Root;
import com.safetynet.safetynetalerts.service.PersonInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PersonRepository implements PersonInfoProvider {

    private static final Logger logger = LoggerFactory.getLogger(PersonRepository.class);
    private List<Person> persons;

    private PersonInfoService personInfoService;
    private RootRepository rootRepository;
    private FireStationRepository fireStationRepository;
    private  PersonRepository personRepository;

    @Autowired
    @Lazy
    public PersonRepository(PersonInfoService personInfoService, RootRepository rootRepository, FireStationRepository fireStationRepository, PersonRepository personRepository) throws IOException {
        this.personInfoService = personInfoService;
        this.rootRepository = rootRepository;
        this.persons = rootRepository.getRoot().getPersons();
        this.fireStationRepository = fireStationRepository;
        this.personRepository = personRepository;
    }

    public Optional<Person> findByFirstNameAndLastName(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            logger.warn("Les paramètres firstName et lastName ne peuvent pas être null");
            return Optional.empty();
        }

        return persons.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public List<Person> findByAddress(String address) {
        return persons.stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }

    public List<Person> findPersonsByStationNumber(int stationNumber) throws IOException {
        // Crée une liste de stationNumbers à partir du numéro de station donné
        List<Integer> stationNumbers = Collections.singletonList(stationNumber);

        // Récupére la liste des adresses couvertes par le numéro de station donné
        List<String> addresses = fireStationRepository.findAddressesByStations(stationNumbers);

        // Vérifie si des adresses ont été trouvées
        if (addresses.isEmpty()) {
            return Collections.emptyList();
        }

        // Récupérer toutes les personnes qui vivent à ces adresses
        List<Person> persons = personRepository.findAllByAddresses(addresses).stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .collect(Collectors.toList());

        return persons;
    }

    public List<Person> findAllByAddresses(List<String> addresses) throws IOException {
        return rootRepository.getRoot().getPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .collect(Collectors.toList());
    }

    public List<Person> findPersons(String firstName, String lastName, String city, String address) {
        return persons.stream()
                .filter(p -> firstName == null || p.getFirstName().equalsIgnoreCase(firstName))
                .filter(p -> lastName == null || p.getLastName().equalsIgnoreCase(lastName))
                .filter(p -> city == null || p.getCity().equalsIgnoreCase(city))
                .filter(p -> address == null || p.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }

    public Person addPerson(Person newPerson) throws IOException {
        Root root = rootRepository.getRoot();
        List<Person> persons = root.getPersons();
        persons.add(newPerson);
        rootRepository.write(root);
        return newPerson;
    }

    private boolean personExists(Person person) {
        return persons.stream().anyMatch(p -> p.getFirstName().equals(person.getFirstName())
                && p.getLastName().equals(person.getLastName()));
    }

    public void updateJsonFile() {
        try {
            Root root = rootRepository.getRoot();
            root.setPersons(persons);
            rootRepository.write(root);
        } catch (IOException e) {
            logger.error("Erreur lors de la mise à jour du fichier JSON", e);
            throw new RuntimeException("Erreur lors de la mise à jour du fichier JSON", e);
        }
    }


    @Override
    public Person updatePerson(Person person, MedicalRecord medicalRecord, FireStation fireStation) {
        return person;
    }

    @Override
    public Person updatePerson(Person person) throws IOException {
        return addPerson(person);
    }

    public Person updatePerson(String oldFirstName, String oldLastName, Person updatedPerson) {
        Optional<Person> existingPerson = persons.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(oldFirstName)
                        && p.getLastName().equalsIgnoreCase(oldLastName))
                .findFirst();

        if (existingPerson.isPresent()) {
            int index = persons.indexOf(existingPerson.get());
            persons.set(index, updatedPerson);
            updateJsonFile();
            return updatedPerson;
        } else {
            throw new IllegalArgumentException("La personne n'existe pas");
        }
    }

    @Override
    public List<Person> getPersons() {
        return null;
    }

    public boolean deletePerson(String firstName, String lastName) throws IOException {
        Optional<Person> personToDelete = findByFirstNameAndLastName(firstName, lastName);
        if (!personToDelete.isPresent()) {
            return false;
        }

        Root root = rootRepository.getRoot();
        List<Person> persons = root.getPersons();
        boolean isRemoved = persons.removeIf(p ->
                p.getFirstName().equals(firstName) && p.getLastName().equals(lastName));

        if (isRemoved) {
            updateJsonFile();
        }
        return isRemoved;
    }

    public List<Person> getListPersons() {
        return persons;
    }

    @Override
    public List<FireStation> getFireStations() throws IOException {
        return rootRepository.getRoot().getFireStations();
    }

    @Override
    public List<MedicalRecord> getMedicalRecords() throws IOException {
        return rootRepository.getRoot().getMedicalRecords();
    }

    @Override
    public void updateJsonFile(List<Person> persons, List<FireStation> fireStations, List<MedicalRecord> medicalRecords) {

    }

    @Override
    public void updateJsonFile(List<Person> persons) {

    }
}
