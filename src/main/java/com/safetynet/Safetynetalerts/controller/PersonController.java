package com.safetynet.Safetynetalerts.controller;

import com.safetynet.Safetynetalerts.model.Person;
import com.safetynet.Safetynetalerts.model.PersonInfo;
import com.safetynet.Safetynetalerts.service.PersonService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

    @RestController
    public class PersonController {

        private final Logger logger = LoggerFactory.getLogger(PersonController.class);

        @Autowired
        private PersonService personService;

        @GetMapping("/persons")
        public ResponseEntity<List<Person>> getPersons(
                @RequestParam(required = false) String firstName,
                @RequestParam(required = false) String lastName,
                @RequestParam(required = false) String city,
                @RequestParam(required = false) String address) {

            List<Person> persons = personService.findPersons(firstName, lastName, city, address);

            if (persons.isEmpty()) {
                logger.warn("Aucune personne trouvée avec les critères donnés");
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(persons);
        }

    @GetMapping("/personInfo")
    public ResponseEntity<PersonInfo> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("Requête pour obtenir des informations sur la personne avec le prénom {} et le nom de famille {}", firstName, lastName);
        try {
            PersonInfo personInfo = personService.getPersonInfoByFirstNameAndLastName(firstName, lastName);
            if (personInfo != null) {
                return new ResponseEntity<>(personInfo, HttpStatus.OK);
            } else {
                logger.warn("Personne non trouvée avec le prénom {} et le nom {}", firstName, lastName);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des informations de la personne", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmail(@RequestParam String city) {
        return personService.getEmailsByCity(city);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public ResponseEntity<Object> addPerson(@Valid @RequestBody Person person) {
        logger.info("Adding person: {}", person);
        Person addedPerson = personService.addPerson(person);
        if (addedPerson != null) {
            logger.info("Person added successfully: {}", addedPerson);
            return new ResponseEntity<>(addedPerson, HttpStatus.CREATED);
        } else {
            logger.error("Failed to add person: {}", person);
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unable to add person");
            error.put("reason", "Validation error or database constraint");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        Person updatedPerson = personService.updatePerson(person);
        if (updatedPerson != null) {
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        try {
            personService.deletePerson(firstName, lastName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            logger.error("Erreur lors de la suppression de la personne avec le prénom {} et le nom de famille {}", firstName, lastName, e);
            return new ResponseEntity<>(e.getReason(), HttpStatus.BAD_REQUEST);
        }
    }
}
