package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.error.CustomErrorType;
import com.safetynet.safetynetalerts.exception.AlreadyExistException;
import com.safetynet.safetynetalerts.exception.NotFoundException;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.PersonInfo;
import com.safetynet.safetynetalerts.model.Root;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import com.safetynet.safetynetalerts.repository.RootRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
 class PersonServiceTest {

    private Person newPerson;
    private Root mockRoot;
    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private RootRepository rootRepository;

    private PersonRepository mockPersonRepository;


    @InjectMocks
    private PersonService personService;
    private final   String address = "123 rue de paris ";
    private final   String city    = "Paris";
    private final   int    zip     = 12345;
    private final   String phone   = "123-456-7890";
    private final   String email   = "example@example.com";

    @BeforeEach
    public void setUp() throws IOException {
        newPerson = new Person("John", "Doe", "123 rue de paris", "Paris", 12345, "123-456-7890", "example@example.com");

        mockRoot = new Root(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        mockPersonRepository = mock(PersonRepository.class);

        when(rootRepository.getRoot()).thenReturn(mockRoot);
    }

    @Test
    @DisplayName("Doit tester l'ajout d'une personne qui n'existe pas")
     void whenAddPersonAndDoesNotExist_thenShouldAddPerson() throws IOException {
        when(personRepository.findByFirstNameAndLastName("Johna", "Boy")).thenReturn(Optional.empty());

        Person personToAdd = new Person("Johna", "Boy", "123 rue de paris", "Paris", 12345, "123-456-7890", "example@example.com");
        Person result = personService.addPerson(personToAdd);

        assertNotNull(result);
        assertEquals("Johna", result.getFirstName());
        assertEquals("Boy", result.getLastName());
    }

    @Test
    @DisplayName("Doit tester l'ajout d'une personne qui existe déjà")
     void whenAddPersonAndExists_thenShouldThrowException() throws IOException {
        // Crée une personne existante
        Person existingPerson = new Person();
        existingPerson.setFirstName("John");
        existingPerson.setLastName("Boyd");

        // Configuration de rootRepository pour retourner une liste contenant existingPerson
        Root mockRoot = new Root();
        mockRoot.setPersons(Arrays.asList(existingPerson));
        when(rootRepository.getRoot()).thenReturn(mockRoot);

        // Crée une nouvelle personne avec les mêmes nom et prénom
        Person newPerson = new Person();
        newPerson.setFirstName("John");
        newPerson.setLastName("Boyd");

        assertThrows(AlreadyExistException.class, () -> personService.addPerson(newPerson));
    }

    @Test
    @DisplayName("getPerson retourne ResponseEntity avec NOT_FOUND si la personne n'est pas trouvée")
    void getPerson_WhenNotFound_ReturnsNotFoundResponse() {
        when(personRepository.findByFirstNameAndLastName("Johna", "Boy"))
                .thenReturn(Optional.empty());

        ResponseEntity<Object> response = personService.getPerson("Johna", "Boy");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof CustomErrorType);
        assertEquals("Personne non trouvée", ((CustomErrorType) response.getBody()).getErrorMessage());
    }


    @Test
    @DisplayName("getPersonInfoByFirstNameAndLastName lance NotFoundException si la personne n'est pas trouvé")
    void getPersonInfoByFirstNameAndLastName_WhenNotFound_ThrowsNotFoundException() {
        when(personRepository.findByFirstNameAndLastName(anyString(), anyString()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () ->
                personService.getPersonInfoByFirstNameAndLastName("Johna", "Boy"));
        assertEquals("Personne non trouvée avec le prénom Johna et le nom de famille Boy", exception.getMessage());
    }

    @Test
    @DisplayName("getPersonInfoByFirstNameAndLastName lance NotFoundException si le dossier médical n'est pas trouvé")
    void getPersonInfoByFirstNameAndLastName_WhenMedicalRecordNotFound_ThrowsNotFoundException() {
        Person foundPerson = new Person("John", "Boyd", address, city, zip, phone, email);
        when(personRepository.findByFirstNameAndLastName("John", "Boyd"))
                .thenReturn(Optional.of(foundPerson));
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Boyd"))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () ->
                personService.getPersonInfoByFirstNameAndLastName("John", "Boyd"));
        assertEquals("Dossier médical non trouvé pour John Boyd", exception.getMessage());
    }

    @Test
    @DisplayName("getEmailsByCity lance NotFoundException si aucune adresse email n'est trouvée")
    void getEmailsByCity_WhenNotFound_ThrowsNotFoundException() {
        when(personRepository.findPersons(null, null, "London", null))
                .thenReturn(Collections.emptyList());

        Exception exception = assertThrows(NotFoundException.class, () ->
                personService.getEmailsByCity("London"));
        assertEquals("Cette ville n'existe pas, veuillez réessayer !", exception.getMessage());
    }

    @Test
    @DisplayName("getEmailsByCity lance NotFoundException pour une ville nulle ou vide")
    void getEmailsByCity_WhenCityIsNullOrBlank_ThrowsNotFoundException() {
        Exception exceptionForNull = assertThrows(NotFoundException.class, () ->
                personService.getEmailsByCity(null));
        assertEquals("La ville ne peut pas être nulle ou vide", exceptionForNull.getMessage());

        Exception exceptionForEmpty = assertThrows(NotFoundException.class, () ->
                personService.getEmailsByCity(""));
        assertEquals("La ville ne peut pas être nulle ou vide", exceptionForEmpty.getMessage());
    }

    @Test
    @DisplayName("addPerson lance AlreadyExistException si la personne existe déjà")
    void addPerson_WhenAlreadyExists_ThrowsAlreadyExistException() throws IOException {
        // Création d'une liste de personnes avec une personne existante
        List<Person> existingPersons = new ArrayList<>();
        existingPersons.add(new Person("John", "Boyd", address, city, zip, phone, email));

        // Configuration du mock pour retourner un Root avec la liste de personnes existantes
        Root mockRoot = new Root();
        mockRoot.setPersons(existingPersons);
        when(rootRepository.getRoot()).thenReturn(mockRoot);

        // Nouvelle personne avec le même nom et prénom que la personne existante
        Person newPerson = new Person("John", "Boyd", address, city, zip, phone, email);

        AlreadyExistException exception = assertThrows(AlreadyExistException.class, () ->
                personService.addPerson(newPerson));

        assertEquals("La personne existe déjà.", exception.getMessage());
    }

    @Test
    @DisplayName("updatePerson lance NotFoundException si l'identifiant de la personne à mettre à jour est invalide")
    void updatePerson_WhenIdentifierIsInvalid_ThrowsNotFoundException() {
        // Personne valide pour la mise à jour
        Person validPerson = new Person("John", "Boyd", "Some Address", "Some City", 12345, "123-456-7890", "email@example.com");

        // Tentative de mise à jour avec un identifiant invalide
        Exception exception = assertThrows(NotFoundException.class, () ->
                personService.updatePerson("", "", validPerson));

        assertEquals("Le prénom et le nom actuels de la personne sont obligatoires pour la mise à jour.", exception.getMessage());
    }

    @Test
    @DisplayName("updatePerson lance NotFoundException si les données de la personne sont invalides")
    void updatePerson_WhenPersonDataIsInvalid_ThrowsNotFoundException() {
        // Personne avec des données invalides
        Person invalidPerson = new Person("", "", address, city, zip, phone, email);

        // Action et Vérification
        Exception exception = assertThrows(NotFoundException.class, () ->
                personService.updatePerson("John", "Boyd", invalidPerson));
        assertEquals("Le prénom et le nom de la personne sont obligatoires.", exception.getMessage());
    }

    @Test
    @DisplayName("deletePerson lance NotFoundException si la personne à supprimer n'est pas trouvée")
    void deletePerson_WhenNotFound_ThrowsNotFoundException() throws IOException {
        // Création d'une liste de personnes vide
        List<Person> mockPersons = new ArrayList<>();

        // Configuration du mock pour retourner un Root avec la liste de personnes
        Root mockRoot = new Root();
        mockRoot.setPersons(mockPersons);
        when(rootRepository.getRoot()).thenReturn(mockRoot);

        Exception exception = assertThrows(NotFoundException.class, () ->
                personService.deletePerson("Boubou", "Bou"));
        assertEquals("Personne non trouvée", exception.getMessage());
    }

    @Test
    @DisplayName("Doit retourner une liste de personnes correspondantes")
    void findPersons_WithValidParameters_ReturnsMatchingPersons() {
        // Configuration du mock
        when(personRepository.findPersons(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Collections.singletonList(new Person("John", "Doe", "123 Rue", "Paris", 12345, "123-456-7890", "john@example.com")));

        List<Person> result = personService.findPersons("John", "Doe", "Paris", "123 Rue");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    @DisplayName("Doit retourner des informations détaillées sur une personne")
    void getPersonInfoByFirstNameAndLastName_WithValidParameters_ReturnsPersonInfo() {
        // Configuration du mock
        Person person = new Person("John", "Doe", "123 Rue", "Paris", 12345, "123-456-7890", "john@example.com");
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990", null, null);

        when(personRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(Optional.of(person));
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(Optional.of(medicalRecord));

        PersonInfo result = personService.getPersonInfoByFirstNameAndLastName("John", "Doe");

        assertNotNull(result);
        assertEquals("John", result.getPerson().getFirstName());
        assertEquals("01/01/1990", result.getMedicalRecord().getBirthdate());
    }

    @Test
    @DisplayName("Doit retourner une liste d'emails pour une ville donnée")
    void getEmailsByCity_WithValidCity_ReturnsEmailList() {
        // Configuration du mock
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", "123 Rue", "Paris", 12345, "123-456-7890", "john@example.com"),
                new Person("Jane", "Doe", "456 Avenue", "Paris", 12346, "123-456-7891", "jane@example.com")
        );
        when(personRepository.findPersons(null, null, "Paris", null)).thenReturn(persons);

        List<String> result = personService.getEmailsByCity("Paris");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("john@example.com"));
        assertTrue(result.contains("jane@example.com"));
    }

    @Test
    @DisplayName("Doit supprimer une personne existante avec succès")
    void deletePerson_ExistingPerson_DeletedSuccessfully() throws IOException {
        // Configuration du mock pour simuler une personne existante
        Person existingPerson = new Person("John", "Doe", "123 Street", "City", 12345, "123-456-7890", "email@example.com");
        List<Person> persons = new ArrayList<>(Arrays.asList(existingPerson));
        Root mockRoot = new Root(persons, new ArrayList<>(), new ArrayList<>());
        when(rootRepository.getRoot()).thenReturn(mockRoot);

        // Appel de la méthode deletePerson
        personService.deletePerson("John", "Doe");

        // Vérifie que la personne a été supprimée
        verify(personRepository).deletePerson("John", "Doe");
    }

    @Test
    @DisplayName("Doit supprimer une personne existante avec succès via l'expression lambda")
    void deletePerson_ExistingPersonViaLambda_DeletedSuccessfully() throws IOException {
        // Configuration du mock pour simuler plusieurs personnes dont celle à supprimer
        Person existingPerson = new Person("John", "Doe", "123 Street", "City", 12345, "123-456-7890", "email@example.com");
        List<Person> persons = new ArrayList<>(Arrays.asList(
                existingPerson,
                new Person("Jane", "Smith", "456 Street", "City", 12345, "987-654-3210", "jane@example.com")
        ));
        Root mockRoot = new Root(persons, new ArrayList<>(), new ArrayList<>());
        when(rootRepository.getRoot()).thenReturn(mockRoot);

        // Appel de la méthode deletePerson
        personService.deletePerson("John", "Doe");

        // Vérifie que la personne a été supprimée
        verify(personRepository).deletePerson("John", "Doe");
    }

    @Test
    @DisplayName("Doit lever NotFoundException via l'expression lambda si la personne n'est pas trouvée")
    void deletePerson_NonExistingPersonViaLambda_ThrowsNotFoundException() throws IOException {
        // Configuration du mock pour simuler plusieurs personnes, mais pas celle à supprimer
        List<Person> persons = new ArrayList<>(Arrays.asList(
                new Person("Jane", "Smith", "456 Street", "City", 12345, "987-654-3210", "jane@example.com")
        ));
        Root mockRoot = new Root(persons, new ArrayList<>(), new ArrayList<>());
        when(rootRepository.getRoot()).thenReturn(mockRoot);

        // Vérification que l'exception est lancée
        assertThrows(NotFoundException.class, () -> personService.deletePerson("John", "Doe"));
    }
}
