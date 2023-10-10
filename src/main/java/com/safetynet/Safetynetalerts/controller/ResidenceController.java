package com.safetynet.Safetynetalerts.controller;

import com.safetynet.Safetynetalerts.DTO.Residence;
import com.safetynet.Safetynetalerts.service.ResidenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private ResidenceService residenceService;

    @GetMapping("/stations")
    public ResponseEntity<List<Residence>> getResidencesByStations(@RequestParam("stations") List<Integer> stationNumbers) {
        logger.info("Fetching residences by stations: {}", stationNumbers);
        List<Residence> residences = residenceService.getResidencesByStations(stationNumbers);
        if (residences.isEmpty()) {
            logger.warn("No residences found for stations: {}", stationNumbers);
            return ResponseEntity.notFound().build();
        }
        logger.info("{} residences found for stations: {}", residences.size(), stationNumbers);
        return ResponseEntity.ok(residences);
    }

}
