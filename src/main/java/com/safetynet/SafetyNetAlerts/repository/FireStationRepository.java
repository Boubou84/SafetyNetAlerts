package com.safetynet.SafetyNetAlerts.repository;

import com.safetynet.SafetyNetAlerts.model.FireStation;
import com.safetynet.SafetyNetAlerts.util.DataLoader;
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
    public FireStationRepository(DataLoader dataLoader) {
        this.fireStations = dataLoader.getRoot().getFirestations();
    }

    public Optional<FireStation> findByAddress(String address) {
        return fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address))
                .findFirst();
    }

    public List<String> findAddressesByStations(List<Integer> stationNumbers) {
        List<String> stationNumbersAsString = stationNumbers.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());

        return fireStations.stream()
                .filter(fireStation -> stationNumbersAsString.contains(fireStation.getStation()))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());
    }

}
