package com.safetynet.safetynetalerts.interfaces;

import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.PersonInfo;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface IPersonService {

    List<Person> findPersons(String firstName, String lastName, String city, String address);

    ResponseEntity<Object> getPerson(String firstName, String lastName);

    PersonInfo getPersonInfoByFirstNameAndLastName(String firstName, String lastName);

    List<String> getEmailsByCity(String city);

    Person addPerson(Person person) throws IOException;

    Person updatePerson(String oldFirstName, String oldLastName, Person updatedPerson) throws IOException;

    public boolean deletePerson(String firstName, String lastName) throws IOException;
}
