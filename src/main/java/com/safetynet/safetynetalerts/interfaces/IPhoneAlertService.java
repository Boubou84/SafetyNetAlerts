package com.safetynet.safetynetalerts.interfaces;

import java.io.IOException;
import java.util.List;

public interface IPhoneAlertService {

    List<String> getPhoneNumbersByStationNumber(int stationNumber) throws IOException;
}
