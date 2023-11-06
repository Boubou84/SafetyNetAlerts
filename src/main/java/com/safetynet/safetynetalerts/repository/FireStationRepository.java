package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Référentiel pour gérer les opérations liées aux données des stations de pompiers.
 * Permet de récupérer, de sauvegarder et de manipuler les informations des stations de pompiers.
 */

@Repository
public class FireStationRepository {

    private static final Logger logger = LoggerFactory.getLogger(FireStationRepository.class);

    private RootRepository rootRepository;

    private List<FireStation> firestations;

    @Autowired
    public FireStationRepository(RootRepository rootRepository) {
        this.rootRepository = rootRepository;
        initializeFireStations();
    }

    public void initializeFireStations() {
        try {
            Root root = rootRepository.getRoot();
            this.firestations = root.getFireStations();
        } catch (IOException e) {
            logger.error("Erreur lors de la récupération des informations des stations de pompiers", e);
        }
    }

    public List<FireStation> getFireStations() {
        return firestations;
    }

    public List<String> findAddressesByStations(List<Integer> stationNumbers) throws IOException {
        return rootRepository.getRoot().getFireStations().stream()
                .filter(fireStation -> stationNumbers.contains(fireStation.getStation()))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());
    }
    public Optional<FireStation> findFireStationByAddress(String address) {
        return getFireStations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .findFirst();
    }

    public void addFireStation(FireStation fireStation) {
        if (fireStation == null) {
            logger.error("La tentative d'ajout d'une station de pompiers nulle a été détectée");
            throw new IllegalArgumentException("La station de pompiers ne peut pas être null");
        }

        firestations.add(fireStation);
        updateRoot();
        logger.info("Station de pompiers ajoutée avec succès : {}", fireStation);
    }

    public void updateFireStation(FireStation firestation) {
        Optional<FireStation> existingStation = findFireStationByAddress(firestation.getAddress());
        if (existingStation.isPresent()) {
            int index = firestations.indexOf(existingStation.get());
            firestations.set(index, firestation);
            updateRoot();
            logger.info("Station de pompiers mise à jour avec succès : {}", firestation);
        } else {
            throw new IllegalArgumentException("La station de pompiers n'existe pas");
        }
    }

    public void deleteFireStation(String address) {
        boolean isRemoved = firestations.removeIf(fs -> fs.getAddress().equalsIgnoreCase(address));

        if (isRemoved) {
            updateRoot();
            logger.info("Station de pompiers supprimée avec succès pour : {}", address);
        } else {
            throw new IllegalArgumentException("La station de pompiers n'existe pas");
        }
    }


    private void updateRoot() {
        try {
            Root root = rootRepository.getRoot();
            root.setFireStations(firestations);
            rootRepository.write(root);
        } catch (IOException e) {
            logger.error("Erreur lors de la mise à jour des données", e);
            throw new RuntimeException("Erreur lors de la mise à jour des données", e);
        }
    }
}
