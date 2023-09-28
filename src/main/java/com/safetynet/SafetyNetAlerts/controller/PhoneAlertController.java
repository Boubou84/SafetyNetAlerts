package com.safetynet.SafetyNetAlerts.controller;

import com.safetynet.SafetyNetAlerts.service.PhoneAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur pour gérer les alertes téléphoniques en cas d'urgence.
 * Fournit un endpoint pour récupérer les numéros de téléphone des résidents desservis par une station spécifique.
 */

@RestController
public class PhoneAlertController {

    @Autowired
    private PhoneAlertService phoneAlertService;

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumbers(@RequestParam int firestation) {
        return phoneAlertService.getPhoneNumbersByStationNumber(firestation);
    }
}
