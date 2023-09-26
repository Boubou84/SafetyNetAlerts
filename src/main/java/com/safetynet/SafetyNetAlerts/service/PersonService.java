package com.safetynet.SafetyNetAlerts.service;

import com.safetynet.SafetyNetAlerts.model.MedicalRecord;
import com.safetynet.SafetyNetAlerts.model.Person;
import com.safetynet.SafetyNetAlerts.model.PersonInfo;
import com.safetynet.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.safetynet.SafetyNetAlerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public List<Person> getPeople() {
        return personRepository.findAll();
    }

    public Person findPersonByFirstNameAndLastName(String firstName, String lastName) {
        return personRepository.findByFirstNameAndLastName(firstName, lastName).orElse(null);
    }

    public PersonInfo getPersonInfoByFirstNameAndLastName(String firstName, String lastName) {
        Person person = personRepository.findByFirstNameAndLastName(firstName, lastName).orElse(null);
        MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName).orElse(null);

        if (person == null || medicalRecord == null) {
            return null;
        }

        PersonInfo personInfo = new PersonInfo();
        personInfo.setPerson(person);
        personInfo.setMedicalRecord(medicalRecord);

        return personInfo;
    }

    public List<String> getEmailsByCity(String city) {
        List<Person> personsInCity = personRepository.findByCity(city);
        return personsInCity.stream()
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }
}
