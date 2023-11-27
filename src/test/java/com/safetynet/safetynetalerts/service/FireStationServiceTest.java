package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.DTO.FireResponse;
import com.safetynet.safetynetalerts.DTO.FireStationCoverageDTO;
import com.safetynet.safetynetalerts.exception.AlreadyExistException;
import com.safetynet.safetynetalerts.exception.NotFoundException;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import com.safetynet.safetynetalerts.repository.RootRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FireStationServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private FireStationRepository fireStationRepository;
    @Mock
    private MedicalRecordRepository medicalRecordRepository;
    @Mock
    private RootRepository rootRepository;

    @InjectMocks
    private FireStationService fireStationService;

    private FireStation testFireStation;
    private Person testPerson;
    private MedicalRecord testMedicalRecord;

    @BeforeEach
    void setUp() {
        testFireStation = new FireStation("Test Address", 1);
        testPerson = new Person("Test", "Person", "Test Address", "Test City", 12345, "123-456-7890", "test@test.com");
        testMedicalRecord = new MedicalRecord("Test", "Person", "01/01/2000", new ArrayList<>(), new ArrayList<>());
    }

        @Test
    @DisplayName("Doit retourner les détails d'incendie pour une adresse donnée")
    void getFireDetailsByAddress_ReturnsDetails() {

            when(fireStationRepository.findFireStationByAddress("Test Address"))
                    .thenReturn(Optional.of(testFireStation));
            when(personRepository.findByAddress("Test Address"))
                    .thenReturn(Collections.singletonList(testPerson));
            when(medicalRecordRepository.findByFirstNameAndLastName("Test", "Person"))
                    .thenReturn(Optional.of(testMedicalRecord));

        FireResponse response = fireStationService.getFireDetailsByAddress("Test Address");

        assertNotNull(response);
        assertEquals(1, response.getStation());
        assertFalse(response.getPersons().isEmpty());
    }

    @Test
    @DisplayName("Doit lancer une exception pour une adresse inconnue lors de la récupération des détails d'incendie")
    void getFireDetailsByAddress_ThrowsNotFoundException_WhenAddressNotFound() {
        when(personRepository.findByAddress("Unknown Address")).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> fireStationService.getFireDetailsByAddress("Unknown Address"));
    }

    @Test
    @DisplayName("Doit mettre à jour une station de pompiers avec succès")
    void updateFireStation_UpdatesSuccessfully() {
        when(fireStationRepository.findFireStationByAddress("Test Address"))
                .thenReturn(Optional.of(testFireStation));

        assertDoesNotThrow(() -> fireStationService.updateFireStation(new FireStation("Test Address", 2)));
    }

    @Test
    @DisplayName("Doit lancer une exception pour une station inconnue lors de la mise à jour")
    void updateFireStation_ThrowsNotFoundException_WhenStationNotFound() {
        when(fireStationRepository.findFireStationByAddress("Unknown Address")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> fireStationService.updateFireStation(new FireStation("Unknown Address", 2)));
    }

    @Test
    @DisplayName("Doit ajouter une station de pompiers avec succès")
    void addFireStation_AddsSuccessfully() {
        when(fireStationRepository.findFireStationByAddress("New Address")).thenReturn(Optional.empty());

        FireStation newFireStation = new FireStation("New Address", 3);
        FireStation addedFireStation = fireStationService.addFireStation(newFireStation);

        assertNotNull(addedFireStation);
        assertEquals("New Address", addedFireStation.getAddress());
        assertEquals(3, addedFireStation.getStation());
    }

    @Test
    @DisplayName("Doit lancer une exception lors de l'ajout d'une station déjà existante")
    void addFireStation_ThrowsAlreadyExistException_WhenStationAlreadyExists() {
        when(fireStationRepository.findFireStationByAddress("Test Address")).thenReturn(Optional.of(testFireStation));

        FireStation newFireStation = new FireStation("Test Address", 3);

        assertThrows(AlreadyExistException.class, () -> fireStationService.addFireStation(newFireStation));
    }

    @Test
    @DisplayName("Doit supprimer une station de pompiers avec succès")
    void deleteFireStation_DeletesSuccessfully() {
        fireStationService.deleteFireStation("Test Address");
        verify(fireStationRepository, times(1)).deleteFireStation("Test Address");
    }

    @Test
    @DisplayName("Doit lancer une exception lors de la suppression d'une station inconnue")
    void deleteFireStation_ThrowsNotFoundException_WhenStationNotFound() {
        doThrow(new NotFoundException("Station non trouvée")).when(fireStationRepository).deleteFireStation("Unknown Address");

        assertThrows(NotFoundException.class, () -> fireStationService.deleteFireStation("Unknown Address"));
    }

    @Test
    @DisplayName("Doit retourner les détails de couverture pour un numéro de station donné")
    void getFireStationCoverage_ReturnsCoverageDetails() throws IOException {
        // Configuration des données de test
        int stationNumber = 2;
        List<Person> persons = List.of(testPerson);

        when(personRepository.findPersonsByStationNumber(stationNumber)).thenReturn(persons);
        when(medicalRecordRepository.findByFirstNameAndLastName("Test", "Person"))
                .thenReturn(Optional.of(testMedicalRecord));

        FireStationCoverageDTO coverageDTO = fireStationService.getPersonsCoveredByStation(stationNumber);

        assertNotNull(coverageDTO);
        assertFalse(coverageDTO.getPersons().isEmpty());
        assertEquals(2, coverageDTO.getPersons().size());
    }

    @Test
    @DisplayName("Doit lancer une exception pour un numéro de station inconnu lors de la récupération de la couverture")
    void getFireStationCoverage_ThrowsNotFoundException_WhenStationNotFound() throws IOException {
        int unknownStationNumber = 99;

        when(personRepository.findPersonsByStationNumber(unknownStationNumber)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> fireStationService.getPersonsCoveredByStation(unknownStationNumber));
    }
}
