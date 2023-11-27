package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.DTO.ChildAlertDTO;
import com.safetynet.safetynetalerts.DTO.ChildAlertInfo;
import com.safetynet.safetynetalerts.exception.NotFoundException;
import com.safetynet.safetynetalerts.service.ChildAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChildAlertController.class)
public class ChildAlertControllerTest {

 @Autowired
 private MockMvc mockMvc;

 @MockBean
 private ChildAlertService childAlertService;

 private ChildAlertDTO mockChildAlertDTOWithChildren;

 private ChildAlertDTO mockChildAlertDTOEmpty;

 @BeforeEach
 void setUp() {
  // Initialisation avec des enfants
  List<ChildAlertInfo> childrenInfoWithChildren = Arrays.asList(
          new ChildAlertInfo("PrénomEnfant1", "NomEnfant1", 10),
          new ChildAlertInfo("PrénomEnfant2", "NomEnfant2", 15));
  List<String> householdMembersWithChildren = Arrays.asList("PrénomAdulte1 NomAdulte1", "PrénomAdulte2 NomAdulte2");
  mockChildAlertDTOWithChildren = new ChildAlertDTO(childrenInfoWithChildren, householdMembersWithChildren);

  // Initialisation sans enfants
  mockChildAlertDTOEmpty = new ChildAlertDTO(new ArrayList<>(), new ArrayList<>());
 }

 @DisplayName("Doit retourner les informations des enfants et des membres du foyer")
 @Test
 void getChildAlertWithChildren() throws Exception {
  given(childAlertService.getChildAlertInfo("Adresse Avec Enfants")).willReturn(mockChildAlertDTOWithChildren);

  mockMvc.perform(get("/childAlert")
                  .param("address", "Adresse Avec Enfants")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.children", hasSize(2)))
          .andExpect(jsonPath("$.householdMembers", hasSize(2)));
 }

 @DisplayName("Doit retourner une réponse vide lorsque l'adresse ne contient aucun enfant")
 @Test
 void getChildAlertWithoutChildren() throws Exception {
  given(childAlertService.getChildAlertInfo("Adresse Sans Enfants")).willReturn(mockChildAlertDTOEmpty);

  mockMvc.perform(get("/childAlert")
                  .param("address", "Adresse Sans Enfants")
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.children", hasSize(0)))
          .andExpect(jsonPath("$.householdMembers", hasSize(0)));
 }

 @DisplayName("Doit retourner une exception lorsque l'adresse est invalide dans ChildAlert")
 @Test
 void getChildAlertWithInvalidAddress() throws Exception {
  // Configure le mock pour lancer une NotFoundException
  given(childAlertService.getChildAlertInfo(anyString())).willThrow(new NotFoundException("L'adresse ne peut pas être nulle ou vide"));

  // Effectue la requête avec une adresse invalide
  mockMvc.perform(get("/childAlert").param("address", ""))
          .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
   }
}