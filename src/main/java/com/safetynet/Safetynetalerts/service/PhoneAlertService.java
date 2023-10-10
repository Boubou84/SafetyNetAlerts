package com.safetynet.Safetynetalerts.service;

import com.safetynet.Safetynetalerts.model.Person;
import com.safetynet.Safetynetalerts.repository.FireStationRepository;
import com.safetynet.Safetynetalerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer les fonctionnalités liées aux alertes téléphoniques.
 * Fournit des méthodes pour envoyer des alertes et récupérer des numéros de téléphone en cas d'urgence.
 */

@Service
public class PhoneAlertService {

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private JsonFileService jsonFileService;

    public List<String> getPhoneNumbersByStationNumber(int stationNumber) {
        if (stationNumber <= 0) {
            throw new IllegalArgumentException("Le numéro de la station doit être positif");
        }
        String stationNumberAsString = String.valueOf(stationNumber);
        List<String> addresses = fireStationRepository.findAddressesByStations(List.of(Integer.valueOf(stationNumberAsString)));

        return personRepository.findAllByAddresses(addresses).stream()
                .map(Person::getPhone)
                .collect(Collectors.toList());
    }
}
