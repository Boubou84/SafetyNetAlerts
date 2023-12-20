package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.Residence;
import com.safetynet.safetynetalerts.interfaces.IResidenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Contrôleur pour gérer les requêtes concernant les résidences.
 * Propose des méthodes pour obtenir des informations sur les résidents et leur domicile.
 */

@RestController
@RequestMapping("/flood")
public class ResidenceController {

    private final Logger logger = LoggerFactory.getLogger(ResidenceController.class);

    private IResidenceService residenceService;

    public ResidenceController(IResidenceService residenceService) {
        this.residenceService = residenceService;
    }

    @GetMapping("/stations")
    public ResponseEntity<List<Residence>> getResidencesByStations(@RequestParam("stations") List<Integer> stationNumbers) throws IOException {
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
