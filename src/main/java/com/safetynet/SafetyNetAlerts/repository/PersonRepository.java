package com.safetynet.SafetyNetAlerts.repository;

import com.safetynet.SafetyNetAlerts.model.FireStation;
import com.safetynet.SafetyNetAlerts.model.MedicalRecord;
import com.safetynet.SafetyNetAlerts.model.Person;
import com.safetynet.SafetyNetAlerts.util.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PersonRepository {

    private List<Person> people;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;

    @Autowired
    public PersonRepository(DataLoader dataLoader) {
        this.people = dataLoader.getRoot().getPersons();
        this.firestations = dataLoader.getRoot().getFirestations();

    }

    public Optional<Person> findByFirstNameAndLastName(String firstName, String lastName) {
        return people.stream()
                .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                .findFirst();
    }

    public List<Person> findByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email) {
        return people.stream()
                .filter(p -> p.getFirstName().equals(firstName))
                .filter(p -> p.getLastName().equals(lastName))
                .filter(p -> p.getEmail().equals(email))
                .collect(Collectors.toList());
    }

    public List<Person> findByCity(String city) {
        return people.stream()
                .filter(p -> p.getCity().equals(city))
                .collect(Collectors.toList());
    }

    public List<Person> findByAddress(String address) {
        return people.stream()
                .filter(p -> p.getAddress().equals(address))
                .collect(Collectors.toList());
    }
    public List<Person> findAllByAddresses(List<String> addresses) {
        return people.stream()
                .filter(person -> addresses.stream()
                        .anyMatch(address -> address.equalsIgnoreCase(person.getAddress())))
                .collect(Collectors.toList());
    }

    private FireStation findFireStationByAddress(String address) {
        return firestations.stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .findFirst()
                .orElse(null);
    }

    public List<Person> findPersonsByStationNumber(int stationNumber) {
        String stationNumberAsString = String.valueOf(stationNumber);

        return people.stream()
                .filter(person -> {
                    FireStation fireStation = findFireStationByAddress(person.getAddress());
                    return fireStation != null && stationNumberAsString.equals(fireStation.getStation());
                })
                .collect(Collectors.toList());
    }

    public List<Person> findAll() {
        return people;
    }

    public List<FireStation> findAllFireStations() {
        return firestations;
    }

    public List<MedicalRecord> findAllMedicalRecords() {
        return medicalrecords;
    }

}
