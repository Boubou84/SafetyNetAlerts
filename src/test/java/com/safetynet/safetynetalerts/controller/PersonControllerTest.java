package com.safetynet.safetynetalerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.interfaces.IPersonService;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.PersonInfo;
import com.safetynet.safetynetalerts.util.AgeUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests pour PersonController")
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPersonService personService;

    @Test
    @DisplayName("Doit obtenir les informations d'une personne")
    void testGetPersonInfo() throws Exception {

        Person mockPerson = new Person();
        mockPerson.setFirstName("John");
        mockPerson.setLastName("Boyd");

        MedicalRecord mockMedicalRecord = new MedicalRecord();
        mockMedicalRecord.setFirstName("John");
        mockMedicalRecord.setLastName("Boyd");
        String birthdateStr = "01/01/1992";
        mockMedicalRecord.setBirthdate(birthdateStr);

        int calculatedAge = AgeUtil.calculateAge(birthdateStr);

        PersonInfo mockPersonInfo = new PersonInfo(mockPerson, mockMedicalRecord, calculatedAge);

        when(personService.getPersonInfoByFirstNameAndLastName("John", "Boyd")).thenReturn(mockPersonInfo);

        mockMvc.perform(get("/personInfo")
                        .param("firstName", "John")
                        .param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.person.firstName").value("John"))
                .andExpect(jsonPath("$.person.lastName").value("Boyd"))
                .andExpect(jsonPath("$.age").value(calculatedAge));

        verify(personService).getPersonInfoByFirstNameAndLastName("John", "Boyd");
    }

    @Test
    @DisplayName("Doit obtenir les e-mails de la communauté")
    void testGetCommunityEmail() throws Exception {
            List<String> emails = Arrays.asList("email1@example.com", "email2@example.com");
        when(personService.getEmailsByCity(anyString())).thenReturn(emails);

        mockMvc.perform(get("/communityEmail")
                        .param("city", "Culver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(emails.size())))
                .andExpect(jsonPath("$[0]").value("email1@example.com"))
                .andExpect(jsonPath("$[1]").value("email2@example.com"));
    }

    @Test
    @DisplayName("Doit ajouter une personne")
    void testAddPerson() throws Exception {
        Person newPerson = new Person();
        Person addedPerson = new Person();
        addedPerson.setFirstName("John");
        addedPerson.setLastName("Boyd");
        when(personService.addPerson(any(Person.class))).thenReturn(addedPerson);

        String personJson = "{\"firstName\":\"John\", \"lastName\":\"Boyd\"}";
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Boyd"));
        verify(personService).addPerson(any(Person.class));
    }

    @Test
    @DisplayName("Doit mettre à jour une personne")
    void testUpdatePerson() throws Exception {
        Person person = new Person();
        person.setFirstName("Johna");
        person.setLastName("Boy");

        Person updatedPerson = new Person();
        updatedPerson.setFirstName("Johna");
        updatedPerson.setLastName("Boy");

        when(personService.updatePerson(eq("oldFirstName"), eq("oldLastName"), any(Person.class))).thenReturn(updatedPerson);

        ObjectMapper objectMapper = new ObjectMapper();
        String personJson = objectMapper.writeValueAsString(person);

        mockMvc.perform(put("/person/oldFirstName/oldLastName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updatedPerson.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedPerson.getLastName()));
        verify(personService).updatePerson(eq("oldFirstName"), eq("oldLastName"), any(Person.class));
    }

    @Test
    @DisplayName("Doit supprimer une personne")
    void testDeletePerson() throws Exception {
        doNothing().when(personService).deletePerson(anyString(), anyString());

        mockMvc.perform(delete("/person/Johna/Boy"))
                .andExpect(status().isOk());
        verify(personService).deletePerson(eq("Johna"), eq("Boy"));
    }
}
