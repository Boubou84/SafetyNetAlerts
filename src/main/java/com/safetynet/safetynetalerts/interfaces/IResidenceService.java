package com.safetynet.safetynetalerts.interfaces;

import com.safetynet.safetynetalerts.dto.InfoResidents;
import com.safetynet.safetynetalerts.dto.Residence;
import com.safetynet.safetynetalerts.model.Person;

import java.io.IOException;
import java.util.List;

public interface IResidenceService {

    List<Residence> getResidencesByStations(List<Integer> stationNumbers) throws IOException;

    Residence getResidenceByAddress(String address);

    InfoResidents getInfoResidents(Person person);
}
