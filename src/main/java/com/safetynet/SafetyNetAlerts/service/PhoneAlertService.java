package com.safetynet.SafetyNetAlerts.service;

import com.safetynet.SafetyNetAlerts.model.Person;
import com.safetynet.SafetyNetAlerts.repository.FireStationRepository;
import com.safetynet.SafetyNetAlerts.repository.PersonRepository;
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

    public List<String> getPhoneNumbersByStationNumber(int stationNumber) {
        String stationNumberAsString = String.valueOf(stationNumber);
        List<String> addresses = fireStationRepository.findAddressesByStations(List.of(Integer.valueOf(stationNumberAsString)));

        return personRepository.findAllByAddresses(addresses).stream()
                .map(Person::getPhone)
                .collect(Collectors.toList());
    }
}
