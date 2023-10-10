package com.safetynet.Safetynetalerts.controller;

import com.safetynet.Safetynetalerts.DTO.FireResponse;
import com.safetynet.Safetynetalerts.DTO.FireStationCoverageDTO;
import com.safetynet.Safetynetalerts.service.FireStationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private final Logger logger = LoggerFactory.getLogger(FireStationController.class);

    @Autowired
    private FireStationService fireStationService;

    @GetMapping("/fire")
    public ResponseEntity<FireResponse> getFireDetailsByAddress(@RequestParam String address) {
        try {
            FireResponse response = fireStationService.getFireDetailsByAddress(address);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                logger.warn("Aucune information trouvée pour l'adresse {}", address);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des détails de l'incendie pour l'adresse {}", address, e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public ResponseEntity<FireStationCoverageDTO> getPersonsCoveredByStation(@RequestParam int stationNumber) {
        if (stationNumber <= 0) {
            logger.error("Numéro de station invalide : {}", stationNumber);
            return ResponseEntity.badRequest().build();
        }

        try {
            FireStationCoverageDTO coverageDTO = fireStationService.getPersonsCoveredByStation(stationNumber);
            if (coverageDTO != null && !coverageDTO.getPersons().isEmpty()) {
                return ResponseEntity.ok(coverageDTO);
            } else {
                logger.warn("Aucune personne couverte trouvée pour la station numéro {}", stationNumber);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des personnes couvertes par la station numéro {}", stationNumber, e);
            return ResponseEntity.status(500).build();
        }
    }

}
