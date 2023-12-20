package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.InfoResidents;
import com.safetynet.safetynetalerts.dto.Residence;
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

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ResidenceServiceTest {

    @Mock
    private FireStationRepository fireStationRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private ResidenceService residenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Doit retourner des résidences pour des stations données")
    void getResidencesByStations_Success() throws IOException {
        List<Integer> stationNumbers = Arrays.asList(1, 2);
        List<String> addresses = Arrays.asList("Address1", "Address2");
        when(fireStationRepository.findAddressesByStations(stationNumbers)).thenReturn(addresses);

        Person person1 = new Person("John", "Doe", "Address1", "City", 12345, "123-456-7890", "john@example.com");
        Person person2 = new Person("Jane", "Doe", "Address2", "City", 12345, "123-456-7891", "jane@example.com");
        List<Person> persons = Arrays.asList(person1, person2);
        when(personRepository.findByAddress(anyString())).thenReturn(persons);

        MedicalRecord medicalRecord1 = new MedicalRecord("John", "Doe", "01/01/1980", new ArrayList<>(), new ArrayList<>());
        MedicalRecord medicalRecord2 = new MedicalRecord("Jane", "Doe", "01/01/1985", new ArrayList<>(), new ArrayList<>());
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(Optional.of(medicalRecord1));
        when(medicalRecordRepository.findByFirstNameAndLastName("Jane", "Doe")).thenReturn(Optional.of(medicalRecord2));

        List<Residence> residences = residenceService.getResidencesByStations(stationNumbers);
        assertFalse(residences.isEmpty());
    }

    @Test
    @DisplayName("Doit retourner une liste vide si aucune adresse n'est trouvée pour les stations")
    void getResidencesByStations_NoAddresses() throws IOException {
        List<Integer> stationNumbers = Arrays.asList(99);
        when(fireStationRepository.findAddressesByStations(stationNumbers)).thenReturn(Collections.emptyList());

        List<Residence> residences = residenceService.getResidencesByStations(stationNumbers);
        assertTrue(residences.isEmpty());
    }

    @Test
    @DisplayName("Doit lancer une exception si la liste des numéros de station est nulle ou vide")
    void getResidencesByStations_NullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> residenceService.getResidencesByStations(null));
        assertThrows(IllegalArgumentException.class, () -> residenceService.getResidencesByStations(Collections.emptyList()));
    }

    @Test
    @DisplayName("Doit créer InfoResidents avec des données valides")
    void createInfoResidents_ValidData() {
        String name = "John Doe";
        String phone = "123-456-7890";
        int age = 30;
        List<String> medications = Arrays.asList("med1", "med2");
        List<String> allergies = Arrays.asList("allergy1", "allergy2");

        InfoResidents infoResidents = new InfoResidents(name, phone, age, medications, allergies);

        assertEquals(name, infoResidents.getName());
        assertEquals(phone, infoResidents.getPhone());
        assertEquals(age, infoResidents.getAge());
        assertEquals(medications, infoResidents.getMedications());
        assertEquals(allergies, infoResidents.getAllergies());
    }

    @Test
    @DisplayName("Doit intégrer InfoResidents dans les résidences retournées")
    void getResidencesByStations_IntegratesInfoResidents() throws IOException {
        // Configuration des mocks
        List<Integer> stationNumbers = Arrays.asList(1, 2);
        List<String> addresses = Arrays.asList("Address1", "Address2");
        when(fireStationRepository.findAddressesByStations(stationNumbers)).thenReturn(addresses);

        List<Residence> residences = residenceService.getResidencesByStations(stationNumbers);

        // Assertions
        assertFalse(residences.isEmpty());
        residences.forEach(residence -> {
            residence.getResidents().forEach(infoResidents -> {
                assertNotNull(infoResidents.getName());
                assertNotNull(infoResidents.getPhone());
                assertTrue(infoResidents.getAge() >= 0);
                assertNotNull(infoResidents.getMedications());
                assertNotNull(infoResidents.getAllergies());
            });
        });
    }
}
