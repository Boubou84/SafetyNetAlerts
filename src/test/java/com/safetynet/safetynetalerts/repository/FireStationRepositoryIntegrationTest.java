package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.Root;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FireStationRepositoryIntegrationTest {

    private final FireStationRepository fireStationRepository;

    private final RootRepository rootRepository;

    private Root originalRoot;

    @Autowired
    public FireStationRepositoryIntegrationTest(FireStationRepository fireStationRepository, RootRepository rootRepository) {
        this.fireStationRepository = fireStationRepository;
        this.rootRepository = rootRepository;
    }

    @BeforeEach
    void setUp() throws IOException {
        // Sauvegarde le contenu original du fichier JSON
        originalRoot = rootRepository.getRoot();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Restaure le fichier JSON à son état original
        rootRepository.write(originalRoot);
    }

    @Test
    @DisplayName("Doit retourner une station de pompiers ajoutée avec succès")
    void addFireStation_ValidStation_AddedSuccessfully() throws IOException {
        FireStation newStation = new FireStation("New Address", 3);
        fireStationRepository.addFireStation(newStation);

        Optional<FireStation> retrievedStation = fireStationRepository.findFireStationByAddress("New Address");
        assertTrue(retrievedStation.isPresent());
        assertEquals(3, retrievedStation.get().getStation());
    }

    @Test
    @DisplayName("Doit retourner une station de pompiers mise à jour avec succès")
    void updateFireStation_ValidUpdate_UpdatedSuccessfully() throws IOException {
        FireStation stationToUpdate = new FireStation("Existing Address", 1);
        fireStationRepository.addFireStation(stationToUpdate);

        FireStation updatedStation = new FireStation("Existing Address", 2);
        fireStationRepository.updateFireStation(updatedStation);

        Optional<FireStation> retrievedStation = fireStationRepository.findFireStationByAddress("Existing Address");
        assertTrue(retrievedStation.isPresent());
        assertEquals(2, retrievedStation.get().getStation());
    }

    @Test
    @DisplayName("Doit retourner une station de pompiers supprimée avec succès")
    void deleteFireStation_ExistingStation_DeletedSuccessfully() throws IOException {
        String address = "Address to Delete";
        fireStationRepository.addFireStation(new FireStation(address, 1));

        fireStationRepository.deleteFireStation(address);
        Optional<FireStation> retrievedStation = fireStationRepository.findFireStationByAddress(address);
        assertFalse(retrievedStation.isPresent());
    }
}
