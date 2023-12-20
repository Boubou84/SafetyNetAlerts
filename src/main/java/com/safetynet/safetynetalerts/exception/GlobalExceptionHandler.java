package com.safetynet.safetynetalerts.exception;

import com.safetynet.safetynetalerts.error.CustomErrorType;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        logger.error(ex.getMessage(), ex);
        StringBuilder allErrorMessage = new StringBuilder();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> allErrorMessage.append(error.getDefaultMessage()).append(" "));
        CustomErrorType error = new CustomErrorType(allErrorMessage.toString());
        return new ResponseEntity<>(error, BAD_REQUEST);
    }

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter
     * is missing.
     *
     * @param ex exception thrown
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        logger.error(ex.getMessage(), ex);
        CustomErrorType error = new CustomErrorType(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        logger.error(ex.getMessage(), ex);
        StringBuilder allErrorMessage = new StringBuilder();
        ex.getConstraintViolations()
                .forEach(error -> allErrorMessage.append(error.getMessage()).append(" "));
        CustomErrorType error = new CustomErrorType(allErrorMessage.toString());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

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
    /*@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, BAD_REQUEST);
    }*/

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleException(Exception e){
        logger.error("Une erreur inattendue s'est produite : ", e);
        CustomErrorType error = new CustomErrorType("Une erreur inattendue s'est produite. Veuillez réessayer plus tard.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

