package com.safetynet.Safetynetalerts.controller;

import com.safetynet.Safetynetalerts.DTO.FireResponse;
import com.safetynet.Safetynetalerts.DTO.FireStationCoverageDTO;
import com.safetynet.Safetynetalerts.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour gérer les requêtes HTTP liées aux stations de pompiers.
 * Il propose des endpoints pour obtenir des informations sur les personnes couvertes par une station spécifique.
 */

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    @Autowired
    private FireStationService fireStationService;

    @GetMapping("/fire")
    public FireResponse getFireDetailsByAddress(@RequestParam String address) {
        return fireStationService.getFireDetailsByAddress(address);
    }

    @GetMapping
    public ResponseEntity<FireStationCoverageDTO> getPersonsCoveredByStation(@RequestParam int stationNumber) {
        FireStationCoverageDTO coverageDTO = fireStationService.getPersonsCoveredByStation(stationNumber);

        if (coverageDTO == null || coverageDTO.getPersons().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(coverageDTO);
    }
}
