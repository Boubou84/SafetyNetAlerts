package com.safetynet.safetynetalerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 class MedicalRecordControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private MedicalRecordController medicalRecordController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(medicalRecordController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Doit retourner un dossier médical créé avec succès")
    void addMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord();
        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecord);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(medicalRecord)));
    }

    @Test
    @DisplayName("Doit retourner un dossier médical mis à jour avec succès")
    void updateMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord();
        String firstName = "John";
        String lastName = "Doe";
        when(medicalRecordService.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecord);

        mockMvc.perform(put("/medicalRecord/{firstName}/{lastName}", firstName, lastName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(medicalRecord)));
    }

    @Test
    @DisplayName("Doit retourner une confirmation de suppression de dossier médical")
    void deleteMedicalRecord() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        doNothing().when(medicalRecordService).deleteMedicalRecord(eq(firstName), eq(lastName));

        mockMvc.perform(delete("/medicalRecord/{firstName}/{lastName}", firstName, lastName))
                .andExpect(status().isNoContent());
    }
}
