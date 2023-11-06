package com.safetynet.safetynetalerts.interfaces;

import com.safetynet.safetynetalerts.DTO.ChildAlertDTO;

public interface IChildAlertService {
    ChildAlertDTO getChildAlertInfo(String address);
}
