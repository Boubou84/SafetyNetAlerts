package com.safetynet.safetynetalerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"data.file.path=src/main/resources/data.json"})
class RootRepositoryIntegrationProductionTest {

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
    @DisplayName("Doit retourner les données Root valides")
    void getRoot_ShouldReturnRootData() throws IOException {
        Root root = rootRepository.getRoot();
        assertNotNull(root, "La récupération de Root ne devrait pas être nulle");
    }

    @Test
    @DisplayName("Doit mettre à jour les données dans le fichier et en mémoire")
    void writeAndReloadData_ShouldUpdateDataInFileAndMemory() throws IOException {
        Root modifiedRoot = new Root();

        rootRepository.write(modifiedRoot);
        Root updatedRoot = rootRepository.getRoot();

        // Vérifie que les données en mémoire sont mises à jour
        assertEquals(modifiedRoot, updatedRoot, "Les données Root devraient être mises à jour en mémoire");

        // Restaure les données d'origine pour ne pas affecter d'autres tests
        rootRepository.write(originalRoot);
    }

    @Test
    @DisplayName("Doit lever une exception si le fichier ne peut pas être écrit")
    void write_WhenFileCannotBeWritten_ShouldThrowException() throws IOException {
        // Utilise un ObjectMapper mock pour simuler une écriture de fichier échouée
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        doThrow(new IOException("Impossible d'écrire le fichier")).when(mockMapper).writeValue(any(File.class), any(Root.class));
        ReflectionTestUtils.setField(rootRepository, "objectMapper", mockMapper);

        Root root = new Root();

        Exception exception = assertThrows(IOException.class, () -> rootRepository.write(root));
        assertNotNull(exception, "Une IOException devrait être levée lors d'une erreur d'écriture");
    }

    @Test
    @DisplayName("Le constructeur doit initialiser toutes les listes avec des données non nulles")
    void constructor_ShouldInitializeAllLists_WhenGivenNonNullLists() {
        // Préparation
        List<Person> persons = Arrays.asList(new Person("John", "Doe", "123 Street", "City", 12345, "123-456-7890", "john@example.com"));
        List<FireStation> firestations = Arrays.asList(new FireStation());
        List<MedicalRecord> medicalrecords = Arrays.asList(new MedicalRecord());

        // Action
        Root root = new Root(persons, firestations, medicalrecords);

        // Vérification
        assertNotNull(root.getPersons());
        assertNotNull(root.getFireStations());
        assertNotNull(root.getMedicalRecords());
        assertEquals(1, root.getPersons().size());
        assertEquals(1, root.getFireStations().size());
        assertEquals(1, root.getMedicalRecords().size());
    }

    @Test
    @DisplayName("Le constructeur doit initialiser des listes vides lorsque des listes nulles sont données")
    void constructor_ShouldInitializeEmptyLists_WhenGivenNullLists() {
        // Action
        Root root = new Root(null, null, null);

        // Vérification
        assertNotNull(root.getPersons());
        assertNotNull(root.getFireStations());
        assertNotNull(root.getMedicalRecords());
        assertTrue(root.getPersons().isEmpty());
        assertTrue(root.getFireStations().isEmpty());
        assertTrue(root.getMedicalRecords().isEmpty());
    }
}
