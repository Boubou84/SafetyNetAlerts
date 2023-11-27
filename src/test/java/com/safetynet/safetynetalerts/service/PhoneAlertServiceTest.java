package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.exception.NotFoundException;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

 class PhoneAlertServiceTest {

    @Mock
    private FireStationRepository fireStationRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PhoneAlertService phoneAlertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Doit retourner une liste de numéros de téléphone pour une station donnée")
    void getPhoneNumbersByStationNumber_Success() throws Exception {
        List<String> addresses = Arrays.asList("123 Main St", "456 Elm St");
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", "123 Main St", "City", 12345, "123-456-7890", "email"),
                new Person("Jane", "Doe", "456 Elm St", "City", 12345, "987-654-3210", "email")
        );

        when(fireStationRepository.findAddressesByStations(anyList())).thenReturn(addresses);
        when(personRepository.findAllByAddresses(addresses)).thenReturn(persons);

        List<String> phoneNumbers = phoneAlertService.getPhoneNumbersByStationNumber(1);
        assertEquals(2, phoneNumbers.size());
        assertTrue(phoneNumbers.contains("123-456-7890"));
        assertTrue(phoneNumbers.contains("987-654-3210"));
    }

    @Test
    @DisplayName("Doit lancer NotFoundException pour un numéro de station négatif")
    void getPhoneNumbersByStationNumber_StationNumberNegative() {
        Exception exception = assertThrows(NotFoundException.class, () ->
                phoneAlertService.getPhoneNumbersByStationNumber(-1));
        assertEquals("Le numéro de la station doit être positif", exception.getMessage());
    }

    @Test
    @DisplayName("Doit lancer NotFoundException pour un numéro de station non trouvé")
    void getPhoneNumbersByStationNumber_StationNumberNotFound() throws IOException {
        when(fireStationRepository.findAddressesByStations(anyList())).thenReturn(Arrays.asList());

        Exception exception = assertThrows(NotFoundException.class, () ->
                phoneAlertService.getPhoneNumbersByStationNumber(99));
        assertEquals("Aucun numéro de téléphone trouvée pour la station numéro 99", exception.getMessage());
    }
 }
