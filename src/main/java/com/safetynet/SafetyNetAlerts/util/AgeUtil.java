package com.safetynet.SafetyNetAlerts.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AgeUtil {

    private static final DateTimeFormatter[] FORMATTERS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
    };

    public static int calculateAge(String birthdate) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                LocalDate birthDateLocal = LocalDate.parse(birthdate, formatter);
                return Period.between(birthDateLocal, LocalDate.now()).getYears();
            } catch (DateTimeParseException e) {
            }
        }

        System.err.println("Erreur de format de date, aucun format n'a réussi à parser la date: " + birthdate);
        return -1;
    }
}
