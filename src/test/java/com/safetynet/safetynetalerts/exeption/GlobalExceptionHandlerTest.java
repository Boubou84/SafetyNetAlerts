package com.safetynet.safetynetalerts.exeption;

import com.safetynet.safetynetalerts.error.CustomErrorType;
import com.safetynet.safetynetalerts.exception.AlreadyExistException;
import com.safetynet.safetynetalerts.exception.GlobalExceptionHandler;
import com.safetynet.safetynetalerts.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Doit gérer AlreadyExistException et retourner le bon statut")
    void handleAlreadyExistException_ReturnsCorrectStatus() {
        AlreadyExistException exception = new AlreadyExistException("Existe déjà");
        ResponseEntity<String> response = exceptionHandler.handleAlreadyExistException(exception);

        assertEquals(HttpStatus.valueOf(420), response.getStatusCode());
        assertEquals("Existe déjà", response.getBody());
    }

    @Test
    @DisplayName("Doit gérer NotFoundException et retourner le bon statut")
    void handleNotFoundException_ReturnsCorrectStatus() {
        NotFoundException exception = new NotFoundException("Non trouvé");
        ResponseEntity<CustomErrorType> response = exceptionHandler.handlePersonNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Non trouvé", response.getBody().getErrorMessage());
    }

    @Test
    @DisplayName("Doit gérer Exception et retourner le bon statut")
    void handleException_ReturnsCorrectStatus() {
        Exception exception = new Exception("Erreur");
        ResponseEntity<Object> response = exceptionHandler.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Une erreur inattendue s'est produite. Veuillez réessayer plus tard.", ((CustomErrorType) response.getBody()).getErrorMessage());
    }
}

