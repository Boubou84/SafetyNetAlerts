package com.safetynet.Safetynetalerts.repository;

import com.safetynet.Safetynetalerts.model.FireStation;
import com.safetynet.Safetynetalerts.service.JsonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Référentiel pour gérer les opérations liées aux données des stations de pompiers.
 * Fournit des méthodes pour accéder et manipuler les données des stations de pompiers.
 */

@Repository
public class FireStationRepository {

    private final List<FireStation> fireStations;

    @Autowired
    private JsonFileService jsonFileService;

    @Autowired
    public FireStationRepository(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
        this.fireStations = jsonFileService.getFireStations();
    }


    public Optional<FireStation> findByAddress(String address) {
        return fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address))
                .findFirst();
    }

    public List<String> findAddressesByStations(List<Integer> stationNumbers) {
        return jsonFileService.getFireStations().stream()
                .filter(fireStation -> stationNumbers.contains(fireStation.getStation()))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());
    }
}
