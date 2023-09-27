package com.safetynet.SafetyNetAlerts.controller;

import com.safetynet.SafetyNetAlerts.service.FireStationService;
import com.safetynet.SafetyNetAlerts.DTO.FireResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FireStationController {

    @Autowired
    private FireStationService fireStationService;

    @GetMapping("/fire")
    public FireResponse getFireDetailsByAddress(@RequestParam String address) {
        return fireStationService.getFireDetailsByAddress(address);
    }
}
