package com.safetynet.Safetynetalerts.repository;

import com.safetynet.Safetynetalerts.model.FireStation;
import com.safetynet.Safetynetalerts.service.JsonFileService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FireStationRepository {

    private static final Logger logger = LoggerFactory.getLogger(FireStationRepository.class);

    private List<FireStation> fireStations;

    @Autowired
    private JsonFileService jsonFileService;

    @PostConstruct
    public void init() {
        fireStations = jsonFileService.getFireStations();
    }

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
        return fireStations.stream()
                .filter(fireStation -> stationNumbers.contains(fireStation.getStation()))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());
    }

    public void addFireStation(FireStation fireStation) {
        fireStations.add(fireStation);
        logger.info("Station de pompiers ajoutée avec succès : {}", fireStation);
    }

    public void updateFireStation(FireStation fireStation) {
        findByAddress(fireStation.getAddress()).ifPresent(existingStation -> {
            existingStation.setStation(fireStation.getStation());
            logger.info("Station de pompiers mise à jour avec succès : {}", existingStation);
        });
    }

    public void deleteFireStation(String address) {
        fireStations.removeIf(fireStation -> fireStation.getAddress().equalsIgnoreCase(address));
        logger.info("Station de pompiers supprimée avec succès : {}", address);
    }
}
