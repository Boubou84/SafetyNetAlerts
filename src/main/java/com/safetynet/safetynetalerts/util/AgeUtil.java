package com.safetynet.safetynetalerts.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Classe utilitaire pour gérer les fonctionnalités liées à l'âge.
 * Fournit des méthodes pour calculer l'âge et d'autres opérations liées à l'âge.
 */

public class AgeUtil {

    private static final Logger logger = LoggerFactory.getLogger(AgeUtil.class);

    private static final DateTimeFormatter[] FORMATTERS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    public static int calculateAge(String birthdate) throws IllegalArgumentException {
        if (birthdate == null || birthdate.trim().isEmpty()) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être nulle ou vide");
        }

        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                LocalDate birthDateLocal = LocalDate.parse(birthdate, formatter);
                return Period.between(birthDateLocal, LocalDate.now()).getYears();
            } catch (DateTimeParseException e) {
                logger.warn("Échec de la conversion de la date avec le format : {}", formatter.toString());
            }
        }

        logger.error("Erreur de format de date, aucun format n'a réussi la conversion de la date: {}", birthdate);
        throw new IllegalArgumentException("Format de date de naissance invalide");
    }
}
