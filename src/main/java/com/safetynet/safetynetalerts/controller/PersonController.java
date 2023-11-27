package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.interfaces.IPersonService;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.PersonInfo;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Validated
    public class PersonController {

    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private final IPersonService personService;

    public PersonController(IPersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/personInfo")
    public ResponseEntity<PersonInfo> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("Requête pour obtenir des informations sur la personne avec le prénom {} et le nom de famille {}", firstName, lastName);
            PersonInfo personInfo = personService.getPersonInfoByFirstNameAndLastName(firstName, lastName);
            if (personInfo != null) {
                return new ResponseEntity<>(personInfo, HttpStatus.OK);
            } else {
                logger.warn("Personne non trouvée avec le prénom {} et le nom {}", firstName, lastName);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmail(@RequestParam String city) {
        return personService.getEmailsByCity(city);
    }

    @PostMapping("/person")
    public ResponseEntity<Object> addPerson(@Valid @RequestBody @NotNull Person newPerson) throws IOException {

            Person addedPerson = personService.addPerson(newPerson);
            return new ResponseEntity<>(addedPerson, HttpStatus.CREATED);
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
    public ResponseEntity<Void> deletePerson(@PathVariable String firstName, @PathVariable String lastName) throws IOException {
        personService.deletePerson(firstName, lastName);
    return ResponseEntity.ok().build();
    }
}