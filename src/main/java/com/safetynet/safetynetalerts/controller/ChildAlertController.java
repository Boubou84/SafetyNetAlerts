package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.DTO.ChildAlertDTO;
import com.safetynet.safetynetalerts.interfaces.IChildAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur gérant les requêtes HTTP relatives aux alertes d'enfants.
 * Il définit des endpoints pour récupérer des informations spécifiques sur les enfants en fonction des paramètres reçus.
 */

@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {

    @Autowired
    @Lazy
    private IChildAlertService childAlertService;

    @GetMapping
    public ResponseEntity<ChildAlertDTO> getChildAlert(@RequestParam String address) {
        ChildAlertDTO childAlertDTO = childAlertService.getChildAlertInfo(address);

        if (childAlertDTO == null || (childAlertDTO.getChildren().isEmpty() && childAlertDTO.getHouseholdMembers().isEmpty())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(childAlertDTO, HttpStatus.OK);
    }
}
