package com.safetynet.safetynetalerts.exception;

import com.safetynet.safetynetalerts.error.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleException(Exception e){
        logger.error("Une erreur inattendue s'est produite : ", e);
        CustomErrorType error = new CustomErrorType("Une erreur inattendue s'est produite. Veuillez réessayer plus tard.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ResponseEntity<String> handleNullPointerException(NullPointerException e){
        logger.error("Une erreur est survenue en raison d'une référence nulle : ", e);
        return new ResponseEntity<>("Une erreur est survenue en raison d'une référence nulle.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e){
        logger.error("Une erreur est survenue en raison d'un argument non valide : ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException e) {
        logger.error("Une erreur de statut de réponse est survenue : {}", e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getReason());
        return new ResponseEntity<>(error, e.getStatusCode());
    }

    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorType> handlePersonNotFoundException(PersonNotFoundException e) {
        logger.error("Personne non trouvée : ", e);
        CustomErrorType error = new CustomErrorType(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPersonException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorType> handleInvalidPersonException(InvalidPersonException e) {
        logger.error("Données de personne invalides : ", e);
        CustomErrorType error = new CustomErrorType(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Erreur de validation : ", ex);
        CustomErrorType error = new CustomErrorType("Erreur de validation : " + ex.getBindingResult().toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorType> handleIOException(IOException e) {
        logger.error("Erreur d'entrée/sortie : ", e);
        CustomErrorType error = new CustomErrorType("Erreur de service : " + e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
