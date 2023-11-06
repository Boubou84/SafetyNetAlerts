package com.safetynet.safetynetalerts.interfaces;

import java.io.IOException;
import java.util.List;

public interface IPhoneAlertService {
    List<String> getPhoneNumbers(int fireStationNumber) throws IOException;

    List<String> getPhoneNumbersByStationNumber(int stationNumber) throws IOException;
}
