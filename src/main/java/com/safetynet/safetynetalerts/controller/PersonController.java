package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.interfaces.IPersonService;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.PersonInfo;
import com.safetynet.safetynetalerts.model.Root;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import com.safetynet.safetynetalerts.repository.RootRepository;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
    public class PersonController {

        private final Logger logger = LoggerFactory.getLogger(PersonController.class);

        @Autowired
        @Lazy
        private IPersonService personService;
        @Autowired
        @Lazy
        private PersonRepository personRepository;
        @Autowired
        @Lazy
        private RootRepository rootRepository;


    @GetMapping("/persons")
        public ResponseEntity<List<Person>> getPersons(
                @RequestParam(required = false) String firstName,
                @RequestParam(required = false) String lastName,
                @RequestParam(required = false) String city,
                @RequestParam(required = false) String address) throws IOException {

        Root root = rootRepository.getRoot();
        List<Person> persons = root.getPersons();

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

    @PostMapping("/person")
    public ResponseEntity<Object> addPerson(@Valid @RequestBody @NotNull Person newPerson) {
        try {
            Person addedPerson = personService.addPerson(newPerson);
            return new ResponseEntity<>(addedPerson, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("person/{oldFirstName}/{oldLastName}")
    public ResponseEntity<Object> updatePerson(
            @PathVariable String oldFirstName,
            @PathVariable String oldLastName,
            @Valid @RequestBody @NotNull Person updatedPerson) {
        try {
            Person updated = personService.updatePerson(oldFirstName, oldLastName, updatedPerson);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/person/{firstName}/{lastName}")
    public ResponseEntity<Object> deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        try {
            boolean isDeleted = personService.deletePerson(firstName, lastName);
            if (!isDeleted) {
                return new ResponseEntity<>("La personne à supprimer n'existe plus", HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
