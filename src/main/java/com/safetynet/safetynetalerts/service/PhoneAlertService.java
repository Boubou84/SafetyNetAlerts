package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.interfaces.IPhoneAlertService;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer les fonctionnalités liées aux alertes téléphoniques.
 * Fournit des méthodes pour envoyer des alertes et récupérer des numéros de téléphone en cas d'urgence.
 */

@Service
public class PhoneAlertService implements IPhoneAlertService {

    private final FireStationRepository fireStationRepository;

    private final PersonRepository personRepository;

    @Autowired
    public PhoneAlertService(FireStationRepository fireStationRepository, PersonRepository personRepository) {
        this.fireStationRepository = fireStationRepository;
        this.personRepository = personRepository;
    }

    @Override
    public List<String> getPhoneNumbers(int fireStationNumber) throws IOException {
        return getPhoneNumbersByStationNumber(fireStationNumber);
    }

    @Override
    public List<String> getPhoneNumbersByStationNumber(int stationNumber) throws IOException {
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
