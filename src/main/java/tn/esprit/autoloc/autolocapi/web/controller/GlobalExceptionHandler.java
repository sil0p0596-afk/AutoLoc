package tn.esprit.autoloc.autolocapi.web.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gestion centralisée des erreurs pour tous les contrôleurs.
 * - ResourceNotFoundException        → 404 Not Found
 * - IllegalArgumentException         → 400 Bad Request
 * - MethodArgumentNotValidException  → 400 Bad Request (violations @Valid)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(ResourceNotFoundException ex) {
        return buildBody(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBadRequest(IllegalArgumentException ex) {
        return buildBody(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * Déclenché quand un @Valid échoue sur un DTO.
     * Renvoie un message listant tous les champs invalides.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() == null
                                ? "Valeur invalide"
                                : error.getDefaultMessage(),
                        (first, ignored) -> first,
                        LinkedHashMap::new));
        Map<String, Object> body = buildBody(HttpStatus.BAD_REQUEST.value(), "Les données sont invalides");
        body.put("erreurs", errors);
        return body;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> body = buildBody(HttpStatus.BAD_REQUEST.value(), "Les paramètres sont invalides");
        body.put("erreurs", ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage(),
                        (first, ignored) -> first,
                        LinkedHashMap::new)));
        return body;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleUnreadableMessage(HttpMessageNotReadableException ex) {
        return buildBody(HttpStatus.BAD_REQUEST.value(), "Le corps de la requête est invalide");
    }

    private Map<String, Object> buildBody(int status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("statut", status);
        body.put("message", message);
        body.put("horodatage", LocalDateTime.now().toString());
        return body;
    }
}
