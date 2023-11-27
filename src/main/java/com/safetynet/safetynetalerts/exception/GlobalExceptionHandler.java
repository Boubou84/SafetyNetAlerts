package com.safetynet.safetynetalerts.exception;

import com.safetynet.safetynetalerts.error.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;



@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseBody
    public ResponseEntity<String> handleAlreadyExistException(AlreadyExistException e){
        logger.error("Une erreur est survenue en raison d'un argument non valide : ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(420));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorType> handlePersonNotFoundException(NotFoundException e) {
        logger.error("Personne non trouvée : ", e);
        CustomErrorType error = new CustomErrorType(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleException(Exception e){
        logger.error("Une erreur inattendue s'est produite : ", e);
        CustomErrorType error = new CustomErrorType("Une erreur inattendue s'est produite. Veuillez réessayer plus tard.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

