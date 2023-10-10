package com.safetynet.Safetynetalerts.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleException(Exception e){
        logger.error("Une erreur inattendue s'est produite : ", e);
        return new ResponseEntity<>("Une erreur inattendue s'est produite. Veuillez réessayer plus tard.", HttpStatus.INTERNAL_SERVER_ERROR);
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
}
