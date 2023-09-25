package com.safetynet.SafetyNetAlerts.controller;


import com.safetynet.SafetyNetAlerts.model.Person;
import com.safetynet.SafetyNetAlerts.model.PersonInfo;
import com.safetynet.SafetyNetAlerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/personInfo")
    public PersonInfo getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) {
        return personService.getPersonInfoByFirstNameAndLastName(firstName, lastName);
    }


    @GetMapping("/people")
    public List<Person> getAllPeople() {
        return personService.getPeople();
    }
}

