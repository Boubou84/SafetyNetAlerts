package com.safetynet.safetynetalerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.model.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class RootRepositoryIntegrationTest {

    @Autowired
    private RootRepository rootRepository;

    private Root originalRoot;

    @MockBean
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws IOException {
        originalRoot = rootRepository.getRoot();
    }

    @Test
    @DisplayName("Doit retourner un Root vide si le fichier n'est pas trouvé")
    void getRoot_WhenFileNotFound_ShouldReturnEmptyRoot() throws IOException {
        // Configure le mock pour lancer IOException lors de la lecture du fichier
        when(objectMapper.readValue(any(File.class), eq(Root.class))).thenThrow(new IOException("Fichier non trouvé"));

        Root root = rootRepository.getRoot();
        assertNotNull(root, "Root ne devrait pas être null même en cas d'erreur de lecture");
        assertTrue(root.getPersons().isEmpty(), "La liste des personnes devrait être vide");
        assertTrue(root.getFireStations().isEmpty(), "La liste des casernes de pompiers devrait être vide");
        assertTrue(root.getMedicalRecords().isEmpty(), "La liste des dossiers médicaux devrait être vide");
    }
}
