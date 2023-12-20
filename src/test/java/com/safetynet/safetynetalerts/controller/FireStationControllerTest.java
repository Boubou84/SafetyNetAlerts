package com.safetynet.safetynetalerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.dto.FireResponse;
import com.safetynet.safetynetalerts.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalerts.dto.PersonDetails;
import com.safetynet.safetynetalerts.dto.PersonFireStationDTO;
import com.safetynet.safetynetalerts.interfaces.IFireStationService;
import com.safetynet.safetynetalerts.model.FireStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FireStationController.class)
class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFireStationService fireStationService;

    private FireResponse mockFireResponse;
    private FireStationCoverageDTO mockFireStationCoverageDTO;
    private FireStation mockFireStation;
    private List<PersonDetails> personDetailsList;
    private List<PersonFireStationDTO> personFireStationDTOList;

    @BeforeEach
    void setUp() throws IOException {
        // Création des données de test pour PersonDetails
        personDetailsList = new ArrayList<>();
        personDetailsList.add(new PersonDetails("Prénom1", "Nom1", "555-1234", 35, "123 Street", "City", 12345, "email@example.com", Arrays.asList("medication1"), Arrays.asList("allergy1")));
        personDetailsList.add(new PersonDetails("Prénom2", "Nom2", "555-5678", 40, "456 Avenue", "City", 12345, "email2@example.com", Arrays.asList("medication2"), Arrays.asList("allergy2")));
        mockFireResponse = new FireResponse(personDetailsList, 1);

        // Création des données de test pour PersonFireStationDTO
        personFireStationDTOList = new ArrayList<>();
        personFireStationDTOList.add(new PersonFireStationDTO("Prénom1", "Nom1", "Adresse1", "555-1234"));
        personFireStationDTOList.add(new PersonFireStationDTO("Prénom2", "Nom2", "Adresse2", "555-5678"));

        // Création des données de test pour FireStationCoverageDTO
        mockFireStationCoverageDTO = new FireStationCoverageDTO(personFireStationDTOList, 5, 3);

        // Création des données de test pour FireStation
        mockFireStation = new FireStation("Test Address", 1);

        // Configuration des mocks pour les interactions avec fireStationService
        when(fireStationService.getFireDetailsByAddress(anyString())).thenReturn(mockFireResponse);
        when(fireStationService.getPersonsCoveredByStation(anyInt())).thenReturn(mockFireStationCoverageDTO);
        when(fireStationService.addFireStation(any(FireStation.class))).thenReturn(mockFireStation);
        when(fireStationService.updateFireStation(any(FireStation.class))).thenReturn(mockFireStation);
    }


    @DisplayName("Doit retourner les détails d'incendie pour une adresse donnée")
    @Test
    void getFireDetailsByAddressTest() throws Exception {
        when(fireStationService.getFireDetailsByAddress("Test Address")).thenReturn(mockFireResponse);

        mockMvc.perform(get("/firestation/fire")
                        .param("address", "Test Address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").value(1))
                .andExpect(jsonPath("$.persons", hasSize(personDetailsList.size())));
    }

    @DisplayName("Doit retourner la couverture de la caserne pour un numéro de station donné")
    @Test
    void getPersonsCoveredByStationTest() throws Exception {
        when(fireStationService.getPersonsCoveredByStation(1)).thenReturn(mockFireStationCoverageDTO);

        mockMvc.perform(get("/firestation/stationNumber")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adultCount").value(5))
                .andExpect(jsonPath("$.childCount").value(3))
                .andExpect(jsonPath("$.persons", hasSize(personFireStationDTOList.size())));
    }

    @DisplayName("Doit créer une nouvelle caserne")
    @Test
    void addFireStationTest() throws Exception {
        when(fireStationService.addFireStation(any(FireStation.class))).thenReturn(mockFireStation);

        mockMvc.perform(post("/firestation/caserne/adresse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockFireStation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address").value(mockFireStation.getAddress()))
                .andExpect(jsonPath("$.station").value(mockFireStation.getStation()));
    }

    @DisplayName("Doit mettre à jour une caserne")
    @Test
    void updateFireStationTest() throws Exception {
        FireStation fireStationToUpdate = new FireStation("Test Address", 2);
        when(fireStationService.updateFireStation(any(FireStation.class))).thenReturn(fireStationToUpdate);

        mockMvc.perform(put("/firestation/{address}/{newStationNumber}", "Test Address", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new FireStation("Test Address", 2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Test Address"))
                .andExpect(jsonPath("$.station").value(2));
    }

    @DisplayName("Doit supprimer une caserne")
    @Test
    void deleteFireStationTest() throws Exception {
        mockMvc.perform(delete("/firestation/{address}", mockFireStation.getAddress()))
                .andExpect(status().isNoContent());
    }

    // Méthode utilitaire pour convertir les objets en chaînes JSON
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
