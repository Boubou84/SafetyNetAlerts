package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.Root;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
 class PersonRepositoryIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RootRepository rootRepository;

    private Root originalRoot;

    @BeforeEach
    void setUp() throws IOException {
        originalRoot = rootRepository.getRoot();

        // Création d'une nouvelle personne pour les tests
        Person testPerson = new Person("TestFirstName", "TestLastName", "TestAddress", "TestCity", 12345, "TestPhone", "TestEmail");
        List<Person> modifiedPersons = new ArrayList<>(originalRoot.getPersons());
        modifiedPersons.add(testPerson);

        // Création d'un nouvel objet Root avec les données de test
        Root modifiedRoot = new Root(modifiedPersons, originalRoot.getFireStations(), originalRoot.getMedicalRecords());
        rootRepository.write(modifiedRoot);
        rootRepository.reloadData();
    }

    @AfterEach
    void tearDown() throws IOException {
        rootRepository.write(originalRoot);
        rootRepository.reloadData();
    }

    @Test
    @DisplayName("Doit ajouter une personne avec succès")
    void addPerson_ValidPerson_AddedSuccessfully() throws IOException {
        try {
            Person newPerson = new Person("NewFirstName", "NewLastName", "NewAddress", "NewCity", 12345, "NewPhone", "NewEmail");
            personRepository.addPerson(newPerson);

            Optional<Person> retrievedPerson = personRepository.findByFirstNameAndLastName("NewFirstName", "NewLastName");
            assertTrue(retrievedPerson.isPresent());
            assertEquals("NewAddress", retrievedPerson.get().getAddress());
        } catch (IOException e) {
            fail("IOException lors de l'ajout d'une personne : " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Doit mettre à jour une personne avec succès")
    void updatePerson_ValidUpdate_UpdatedSuccessfully() throws IOException {
        // Ajout d'une personne existante
        Person existingPerson = new Person("ExistingFirstName", "ExistingLastName", "ExistingAddress", "ExistingCity", 12345, "ExistingPhone", "ExistingEmail");
        personRepository.addPerson(existingPerson);
        rootRepository.reloadData();

        // Création d'un objet Person avec les nouvelles informations
        Person updatedPerson = new Person("ExistingFirstName", "ExistingLastName", "UpdatedAddress", "UpdatedCity", 12345, "UpdatedPhone", "UpdatedEmail");

        // Mise à jour de la personne
        personRepository.updatePerson("ExistingFirstName", "ExistingLastName", updatedPerson);
        rootRepository.reloadData(); // Assurez-vous que les données sont rechargées après la mise à jour

        // Récupération et vérification de la mise à jour
        Optional<Person> retrievedPerson = personRepository.findByFirstNameAndLastName("ExistingFirstName", "ExistingLastName");
        assertTrue(retrievedPerson.isPresent(), "La personne mise à jour doit être présente");
        assertEquals("UpdatedAddress", retrievedPerson.get().getAddress(), "L'adresse doit être mise à jour");
        assertEquals("UpdatedCity", retrievedPerson.get().getCity(), "La ville doit être mise à jour");
        assertEquals("UpdatedPhone", retrievedPerson.get().getPhone(), "Le téléphone doit être mis à jour");
        assertEquals("UpdatedEmail", retrievedPerson.get().getEmail(), "L'email doit être mis à jour");
    }


    @Test
    @DisplayName("Doit supprimer une personne avec succès")
    void deletePerson_ExistingPerson_DeletedSuccessfully() throws IOException {
        // Ajoute une personne
        String firstName = "FirstNameToDelete";
        String lastName = "LastNameToDelete";
        Person personToAdd = new Person(firstName, lastName, "AddressToDelete", "CityToDelete", 12345, "PhoneToDelete", "EmailToDelete");
        personRepository.addPerson(personToAdd);

        // Vérifie que la personne a été ajoutée
        Optional<Person> addedPerson = personRepository.findByFirstNameAndLastName(firstName, lastName);
        assertTrue(addedPerson.isPresent(), "La personne doit être ajoutée avant la suppression");

        // Supprime la personne
        boolean isDeleted = personRepository.deletePerson(firstName, lastName);
        assertTrue(isDeleted, "La suppression doit réussir");

        // Vérifie que la personne n'est plus présente
        Optional<Person> retrievedPerson = personRepository.findByFirstNameAndLastName(firstName, lastName);
        assertFalse(retrievedPerson.isPresent(), "La personne ne doit plus exister après la suppression");
    }

    @Test
    @DisplayName("Doit trouver une personne par nom et prénom")
    void findByFirstNameAndLastName_ShouldReturnPerson() {
        Optional<Person> foundPerson = personRepository.findByFirstNameAndLastName("TestFirstName", "TestLastName");
        assertTrue(foundPerson.isPresent());
        assertEquals("TestFirstName", foundPerson.get().getFirstName());
        assertEquals("TestLastName", foundPerson.get().getLastName());
    }

    @Test
    @DisplayName("Doit trouver des personnes par adresse")
    void findByAddress_ShouldReturnPersons() {
        List<Person> persons = personRepository.findByAddress("TestAddress");
        assertFalse(persons.isEmpty());
        assertTrue(persons.stream().allMatch(person -> "TestAddress".equals(person.getAddress())));
    }

    @Test
    @DisplayName("Doit retourner toutes les personnes pour les adresses données")
    void findAllByAddresses_ShouldReturnAllPersons() throws IOException {
        // Création d'une liste d'adresses pour le test
        List<String> addresses = Arrays.asList("TestAddress");

        // Appel de la méthode findAllByAddresses
        List<Person> persons = personRepository.findAllByAddresses(addresses);

        // Vérification que la liste n'est pas vide
        assertFalse(persons.isEmpty());

        // Vérification supplémentaire pour s'assurer que toutes les personnes retournées ont des adresses dans la liste fournie
        assertTrue(persons.stream().allMatch(person -> addresses.contains(person.getAddress())));
    }

    @Test
    @DisplayName("Ne doit pas trouver une personne inexistante")
    void findByFirstNameAndLastName_WhenPersonDoesNotExist_ShouldReturnEmpty() {
        Optional<Person> foundPerson = personRepository.findByFirstNameAndLastName("NonExistent", "Person");
        assertFalse(foundPerson.isPresent());
    }

    @Test
    @DisplayName("Doit trouver des personnes par différents critères")
    void findPersons_ByVariousCriteria() throws IOException {
        // Ajout de plusieurs personnes pour le test
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("John", "Doe", "123 Street", "Paris", 75000, "123-456-7890", "john@example.com"));
        persons.add(new Person("Jane", "Doe", "456 Avenue", "Lyon", 69000, "456-789-1234", "jane@example.com"));
        Root modifiedRoot = new Root(persons, originalRoot.getFireStations(), originalRoot.getMedicalRecords());
        rootRepository.write(modifiedRoot);
        rootRepository.reloadData();

        // Test avec différents critères
        List<Person> foundByCity = personRepository.findPersons(null, null, "Paris", null);
        assertEquals(1, foundByCity.size());

        List<Person> foundByAddress = personRepository.findPersons(null, null, null, "123 Street");
        assertEquals(1, foundByAddress.size());

        List<Person> foundByName = personRepository.findPersons("John", "Doe", null, null);
        assertEquals(1, foundByName.size());
    }

    @Test
    @DisplayName("Doit trouver des personnes par adresses")
    void findAllByAddresses_GivenAddresses() throws IOException {
        // Ajout de plusieurs personnes pour le test
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("John", "Doe", "123 Street", "City", 12345, "123-456-7890", "john@example.com"));
        persons.add(new Person("Jane", "Doe", "456 Avenue", "City", 12345, "456-789-1234", "jane@example.com"));
        Root modifiedRoot = new Root(persons, originalRoot.getFireStations(), originalRoot.getMedicalRecords());
        rootRepository.write(modifiedRoot);
        rootRepository.reloadData();

        // Test avec une liste d'adresses
        List<String> addresses = Arrays.asList("123 Street", "789 Road");
        List<Person> foundPersons = personRepository.findAllByAddresses(addresses);
        assertEquals(1, foundPersons.size());
    }

    @Test
    @DisplayName("Doit retourner tous les dossiers médicaux")
    void getMedicalRecords_ReturnsAllMedicalRecords() throws IOException {
        // Ajout de plusieurs dossiers médicaux pour le test
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(new MedicalRecord("John", "Doe", "01/01/1980", new ArrayList<>(), new ArrayList<>()));
        medicalRecords.add(new MedicalRecord("Jane", "Doe", "01/01/1985", new ArrayList<>(), new ArrayList<>()));
        Root modifiedRoot = new Root(originalRoot.getPersons(), originalRoot.getFireStations(), medicalRecords);
        rootRepository.write(modifiedRoot);
        rootRepository.reloadData();

        // récupére tous les dossiers médicaux
        List<MedicalRecord> allMedicalRecords = personRepository.getMedicalRecords();
        assertEquals(2, allMedicalRecords.size());
    }

    @Test
    @DisplayName("Doit trouver des personnes par numéro de station quand des adresses sont associées")
    void findPersonsByStationNumber_WithAddresses() throws IOException {
        // Configuration des stations de pompiers et leurs adresses
        List<FireStation> fireStations = Arrays.asList(
                new FireStation("123 Street", 1),
                new FireStation("456 Avenue", 1)
        );
        Root modifiedRoot = new Root(originalRoot.getPersons(), fireStations, originalRoot.getMedicalRecords());
        rootRepository.write(modifiedRoot);
        rootRepository.reloadData();

        // Configuration des personnes vivant à ces adresses
        Person person1 = new Person("John", "Doe", "123 Street", "City", 12345, "123-456-7890", "john@example.com");
        Person person2 = new Person("Jane", "Doe", "456 Avenue", "City", 12345, "456-789-1234", "jane@example.com");
        List<Person> persons = new ArrayList<>(Arrays.asList(person1, person2));
        modifiedRoot = new Root(persons, fireStations, originalRoot.getMedicalRecords());
        rootRepository.write(modifiedRoot);
        rootRepository.reloadData();

        List<Person> foundPersons = personRepository.findPersonsByStationNumber(1);
        assertFalse(foundPersons.isEmpty());
        assertTrue(foundPersons.stream().anyMatch(p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")));
        assertTrue(foundPersons.stream().anyMatch(p -> p.getFirstName().equals("Jane") && p.getLastName().equals("Doe")));
    }

    @Test
    @DisplayName("Doit retourner une liste vide pour un numéro de station sans adresses associées")
    void findPersonsByStationNumber_NoAddresses() throws IOException {
        // Configuration d'une station sans adresses associées
        List<FireStation> fireStations = Collections.singletonList(new FireStation("789 Road", 2));
        Root modifiedRoot = new Root(originalRoot.getPersons(), fireStations, originalRoot.getMedicalRecords());
        rootRepository.write(modifiedRoot);
        rootRepository.reloadData();

        List<Person> foundPersons = personRepository.findPersonsByStationNumber(1);
        assertTrue(foundPersons.isEmpty());
    }
}

