package com.safetynet.SafetyNetAlerts.service;

import com.safetynet.SafetyNetAlerts.DTO.FireResponse;
import com.safetynet.SafetyNetAlerts.DTO.PersonDetails;
import com.safetynet.SafetyNetAlerts.model.FireStation;
import com.safetynet.SafetyNetAlerts.model.MedicalRecord;
import com.safetynet.SafetyNetAlerts.model.Person;
import com.safetynet.SafetyNetAlerts.repository.FireStationRepository;
import com.safetynet.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.safetynet.SafetyNetAlerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.safetynet.SafetyNetAlerts.util.AgeUtil.calculateAge;

@Service
public class FireStationService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public FireResponse getFireDetailsByAddress(String address) {
        List<Person> personsAtAddress = personRepository.findByAddress(address);
        Optional<FireStation> fireStation = fireStationRepository.findByAddress(address);

        List<PersonDetails> personDetailsList = personsAtAddress.stream().map(p -> {
            PersonDetails details = new PersonDetails();
            details.setName(p.getFirstName() + " " + p.getLastName());
            details.setPhone(p.getPhone());
            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(p.getFirstName(), p.getLastName()).orElse(null);

            if (medicalRecord != null) {
                details.setAge(calculateAge(medicalRecord.getBirthdate()));
                details.setMedications(medicalRecord.getMedications());
                details.setAllergies(medicalRecord.getAllergies());
            }
            return details;
        }).collect(Collectors.toList());

        FireResponse response = new FireResponse();
        response.setPersons(personDetailsList);
        fireStation.ifPresent(fs -> response.setStationNumber(Integer.parseInt(fs.getStation())));

        return response;
    }
}
