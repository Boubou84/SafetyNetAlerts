package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.DTO.FireResponse;
import com.safetynet.safetynetalerts.DTO.FireStationCoverageDTO;
import com.safetynet.safetynetalerts.interfaces.IFireStationService;
import com.safetynet.safetynetalerts.model.FireStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private final Logger logger = LoggerFactory.getLogger(FireStationController.class);

    private final IFireStationService fireStationService;

    public FireStationController(IFireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @GetMapping("/fire")
    public FireResponse getFireDetailsByAddress(@RequestParam String address) {
        return fireStationService.getFireDetailsByAddress(address);
    }

    @GetMapping("/stationNumber")
    public ResponseEntity<FireStationCoverageDTO> getPersonsCoveredByStation(@RequestParam int stationNumber) throws IOException {
        FireStationCoverageDTO coverageDTO = fireStationService.getPersonsCoveredByStation(stationNumber);

        if (coverageDTO == null || coverageDTO.getPersons().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(coverageDTO);
    }

    @PostMapping("/caserne/adresse")
    public ResponseEntity<FireStation> addFireStation(@RequestBody FireStation fireStation) {
        FireStation savedFireStation = fireStationService.addFireStation(fireStation);
        return new ResponseEntity<>(savedFireStation, HttpStatus.CREATED);
    }

    @PutMapping("/{address}/{newStationNumber}")
    public ResponseEntity<FireStation> updateFireStation(@PathVariable String address, @PathVariable int newStationNumber) {
        FireStation fireStationToUpdate = new FireStation();
        fireStationToUpdate.setAddress(address);
        fireStationToUpdate.setStation(newStationNumber);
        FireStation updatedFireStation = fireStationService.updateFireStation(fireStationToUpdate);
        return new ResponseEntity<>(updatedFireStation, HttpStatus.OK);
    }

    @DeleteMapping("/{address}")
    public ResponseEntity<Void> deleteFireStation(@PathVariable String address) {
        fireStationService.deleteFireStation(address);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
