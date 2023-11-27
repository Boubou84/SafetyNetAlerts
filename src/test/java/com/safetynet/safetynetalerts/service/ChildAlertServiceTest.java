package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.DTO.ChildAlertDTO;
import com.safetynet.safetynetalerts.exception.NotFoundException;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@ExtendWith(SpringExtension.class)
class ChildAlertServiceTest {

 @Mock
 private PersonRepository personRepository;

 @Mock
 private MedicalRecordRepository medicalRecordRepository;

 @InjectMocks
 private ChildAlertService childAlertService;

 private List<Person> personsWithChildren;
 private List<Person> personsWithoutChildren;
 private MedicalRecord childMedicalRecord;
 private MedicalRecord adultMedicalRecord;

 @BeforeEach
 void setUp() {
  // création d'objets Person représentant des enfants
  Person child1 = new Person("PrénomEnfant1", "NomEnfant1", "Adresse Avec Enfants", "Ville", 12345, "Phone", "Email");
  Person child2 = new Person("PrénomEnfant2", "NomEnfant2", "Adresse Avec Enfants", "Ville", 12345, "Phone", "Email");
  personsWithChildren = Arrays.asList(child1, child2);

  // création d'objets Person sans enfants
  Person adult1 = new Person("PrénomAdulte1", "NomAdulte1", "Adresse Sans Enfants", "Ville", child1.getZip(), "Phone", "Email");
  personsWithoutChildren = Collections.singletonList(adult1);

  // Création de MedicalRecord pour les enfants et les adultes
  childMedicalRecord = new MedicalRecord("PrénomEnfant1", "NomEnfant1", "01/01/2010", new ArrayList<>(), new ArrayList<>());
  adultMedicalRecord = new MedicalRecord("PrénomAdulte1", "NomAdulte1", "01/01/1980", new ArrayList<>(), new ArrayList<>());

  // Configuration des mocks pour les requêtes de repository
  when(personRepository.findByAddress("Avec Enfants")).thenReturn(personsWithChildren);
  when(personRepository.findByAddress("Sans Enfants")).thenReturn(personsWithoutChildren);
  when(personRepository.findByAddress("Adresse Inexistante")).thenReturn(Collections.emptyList());

  // Configuration des mocks pour les requêtes de medicalRecordRepository
  when(medicalRecordRepository.findByFirstNameAndLastName("PrénomEnfant1", "NomEnfant1")).thenReturn(Optional.of(childMedicalRecord));
  when(medicalRecordRepository.findByFirstNameAndLastName("PrénomAdulte1", "NomAdulte1")).thenReturn(Optional.of(adultMedicalRecord));
 }

 @DisplayName("Doit retourner les informations des enfants et des membres du foyer pour une adresse avec enfants")
 @Test
 void getChildAlertInfoWithChildren() {
  ChildAlertDTO result = childAlertService.getChildAlertInfo("Avec Enfants");

  assertFalse(result.getChildren().isEmpty(), "La liste des enfants devrait être non vide");
  assertFalse(result.getHouseholdMembers().isEmpty(), "La liste des membres du foyer devrait être non vide");
 }

 @DisplayName("Doit retourner une liste vide pour les enfants mais pas pour les membres du foyer lorsque l'adresse ne contient pas d'enfants")
 @Test
 void getChildAlertInfoWithoutChildren() {
  ChildAlertDTO result = childAlertService.getChildAlertInfo("Sans Enfants");

  assertTrue("La liste des enfants devrait être vide", result.getChildren().isEmpty());
  assertFalse(result.getHouseholdMembers().isEmpty(), "La liste des membres du foyer ne devrait pas être vide");
 }

 @DisplayName("Doit lancer une exception pour une adresse invalide")
 @Test
 void getChildAlertInfoWithInvalidAddress() {
  assertThrows(NotFoundException.class, () -> childAlertService.getChildAlertInfo(""));
 }

 @DisplayName("Doit lancer une exception pour une adresse sans résultats")
 @Test
 void getChildAlertInfoWithNoResults() {
  assertThrows(NotFoundException.class, () -> childAlertService.getChildAlertInfo("Adresse Inexistante"));
 }
}