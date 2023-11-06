package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.interfaces.IPhoneAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Contrôleur pour gérer les alertes téléphoniques en cas d'urgence.
 * Fournit un endpoint pour récupérer les numéros de téléphone des résidents desservis par une station spécifique.
 */

@RestController
public class PhoneAlertController {

    private IPhoneAlertService phoneAlertService;

    @Autowired
    public PhoneAlertController(IPhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhoneNumbers(@RequestParam int firestation) throws IOException {
        List<String> phoneNumbers = phoneAlertService.getPhoneNumbersByStationNumber(firestation);
        if (phoneNumbers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(phoneNumbers, HttpStatus.OK);
    }

}
