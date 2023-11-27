package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.exception.NotFoundException;
import com.safetynet.safetynetalerts.interfaces.PersonInfoProvider;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class PersonRepository implements PersonInfoProvider {

    private static final Logger logger = LoggerFactory.getLogger(PersonRepository.class);
    private List<Person> persons;

    private final RootRepository rootRepository;
    private final FireStationRepository fireStationRepository;

    public PersonRepository(RootRepository rootRepository, FireStationRepository fireStationRepository) throws IOException {
        this.persons = rootRepository.getRoot().getPersons();
        this.rootRepository = rootRepository;
        this.fireStationRepository = fireStationRepository;
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
                .toList();
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
        return findAllByAddresses(addresses).stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .toList();
    }

    public List<Person> findAllByAddresses(List<String> addresses) throws IOException {
        return rootRepository.getRoot().getPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .toList();
    }

    public List<Person> findPersons(String firstName, String lastName, String city, String address) {
        return persons.stream()
                .filter(p -> firstName == null || p.getFirstName().equalsIgnoreCase(firstName))
                .filter(p -> lastName == null || p.getLastName().equalsIgnoreCase(lastName))
                .filter(p -> city == null || p.getCity().equalsIgnoreCase(city))
                .filter(p -> address == null || p.getAddress().equalsIgnoreCase(address))
                .toList();
    }

    public Person addPerson(Person newPerson) throws IOException {
        Root root = rootRepository.getRoot();
        List<Person> persons = root.getPersons();
        persons.add(newPerson);
        rootRepository.write(root);
        rootRepository.reloadData();
        return newPerson;
    }

    public Person updatePerson(String oldFirstName, String oldLastName, Person updatedPerson) throws IOException {
        Optional<Person> existingPersonOpt = findByFirstNameAndLastName(oldFirstName, oldLastName);

        if (existingPersonOpt.isPresent()) {
            Person existingPerson = existingPersonOpt.get();

            existingPerson.setAddress(updatedPerson.getAddress());
            existingPerson.setCity(updatedPerson.getCity());
            existingPerson.setZip(updatedPerson.getZip());
            existingPerson.setPhone(updatedPerson.getPhone());
            existingPerson.setEmail(updatedPerson.getEmail());

            Root root = rootRepository.getRoot();
            root.setPersons(persons);
            rootRepository.write(root);
            // Rechargement des données pour synchroniser les modifications.
            rootRepository.reloadData();
            return existingPerson;
        } else {
            throw new NotFoundException("La personne avec le prénom " + oldFirstName + " et le nom " + oldLastName + " n'existe pas");
        }
    }

    @Override
    public List<Person> getPersons() {
        return Collections.emptyList();
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
            rootRepository.write(root);
            rootRepository.reloadData();
        }

        return isRemoved;
    }

    @Override
    public List<FireStation> getFireStations() throws IOException {
        return rootRepository.getRoot().getFireStations();
    }

    @Override
    public List<MedicalRecord> getMedicalRecords() throws IOException {
        return rootRepository.getRoot().getMedicalRecords();
    }

    public void refreshData(List<Person> updatedPersons) {
        this.persons = updatedPersons;
    }

}
