package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersonInfoServiceTest {

 @Mock
 private PersonRepository personRepository;
 @Mock
 private FireStationRepository fireStationRepository;
 @Mock
 private MedicalRecordRepository medicalRecordRepository;

 @InjectMocks
 private PersonInfoService personInfoService;

 @BeforeEach
 void setUp() {
  MockitoAnnotations.openMocks(this);
 }

 @Test
 @DisplayName("Doit retourner la liste des personnes")
 void getPersons() {
  List<Person> persons = Arrays.asList(new Person(), new Person());
  when(personRepository.getPersons()).thenReturn(persons);

  List<Person> result = personInfoService.getPersons();
  assertFalse(result.isEmpty());
  assertEquals(2, result.size());
  verify(personRepository).getPersons();
 }

 @Test
 @DisplayName("Doit retourner la liste des stations de pompiers")
 void getFireStations() {
  List<FireStation> fireStations = Arrays.asList(new FireStation(), new FireStation());
  when(fireStationRepository.getFireStations()).thenReturn(fireStations);

  List<FireStation> result = personInfoService.getFireStations();
  assertFalse(result.isEmpty());
  assertEquals(2, result.size());
  verify(fireStationRepository).getFireStations();
 }

 @Test
 @DisplayName("Doit retourner la liste des dossiers médicaux")
 void getMedicalRecords() {
  List<MedicalRecord> medicalRecords = Arrays.asList(new MedicalRecord(), new MedicalRecord());
  when(medicalRecordRepository.getMedicalRecords()).thenReturn(medicalRecords);

  List<MedicalRecord> result = personInfoService.getMedicalRecords();
  assertFalse(result.isEmpty());
  assertEquals(2, result.size());
  verify(medicalRecordRepository).getMedicalRecords();
 }
}
