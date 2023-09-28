package com.safetynet.SafetyNetAlerts.controller;


import com.safetynet.SafetyNetAlerts.model.Person;
import com.safetynet.SafetyNetAlerts.model.PersonInfo;
import com.safetynet.SafetyNetAlerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur gérant les requêtes HTTP relatives aux personnes.
 * Fournit des endpoints pour accéder et manipuler les données des personnes.
 */

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

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmail(@RequestParam String city) {
        return personService.getEmailsByCity(city);
    }
}

