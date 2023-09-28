package com.safetynet.SafetyNetAlerts.controller;

import com.safetynet.SafetyNetAlerts.DTO.Residence;
import com.safetynet.SafetyNetAlerts.service.ResidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour gérer les requêtes concernant les résidences.
 * Propose des méthodes pour obtenir des informations sur les résidents et leur domicile.
 */

@RestController
@RequestMapping("/flood")
public class ResidenceController {

    @Autowired
    private ResidenceService residenceService;

    @GetMapping("/stations")
    public ResponseEntity<List<Residence>> getResidencesByStations(
            @RequestParam("stations") List<Integer> stationNumbers) {

        List<Residence> residences = residenceService.getResidencesByStations(stationNumbers);
        if (residences.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(residences);
    }
}
