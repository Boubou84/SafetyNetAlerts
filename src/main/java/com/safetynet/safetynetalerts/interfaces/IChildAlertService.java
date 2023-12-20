package com.safetynet.safetynetalerts.interfaces;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;

public interface IChildAlertService {
    ChildAlertDTO getChildAlertInfo(String address);
}
