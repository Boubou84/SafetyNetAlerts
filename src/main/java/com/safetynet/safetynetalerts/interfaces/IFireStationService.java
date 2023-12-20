package com.safetynet.safetynetalerts.interfaces;

import com.safetynet.safetynetalerts.dto.FireResponse;
import com.safetynet.safetynetalerts.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalerts.model.FireStation;

import java.io.IOException;

public interface IFireStationService {
    FireResponse getFireDetailsByAddress(String address);

    FireStationCoverageDTO getPersonsCoveredByStation(int stationNumber) throws IOException;

    FireStation addFireStation(FireStation fireStation);

    FireStation updateFireStation(FireStation fireStationToUpdate);

    void deleteFireStation(String address);
}
