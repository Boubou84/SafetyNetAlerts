package com.safetynet.SafetyNetAlerts.service;

import com.safetynet.SafetyNetAlerts.DTO.ChildAlertDTO;
import com.safetynet.SafetyNetAlerts.DTO.ChildAlertInfo;
import com.safetynet.SafetyNetAlerts.model.MedicalRecord;
import com.safetynet.SafetyNetAlerts.model.Person;
import com.safetynet.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.safetynet.SafetyNetAlerts.repository.PersonRepository;
import com.safetynet.SafetyNetAlerts.util.AgeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChildAlertService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public ChildAlertDTO getChildAlertInfo(String address) {
        List<Person> persons = personRepository.findByAddress(address);
        List<ChildAlertInfo> childrenInfo = new ArrayList<>();
        List<String> householdMembers = new ArrayList<>();

        for (Person person : persons) {
            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()).orElse(null);

            if (medicalRecord != null) {
                int age = AgeUtil.calculateAge(medicalRecord.getBirthdate());
                if (age <= 18) {
                    childrenInfo.add(new ChildAlertInfo(person.getFirstName(), person.getLastName(), age));
                }
            }

            householdMembers.add(person.getFirstName() + " " + person.getLastName());
        }

        return new ChildAlertDTO(childrenInfo, householdMembers);
    }
}
