package com.safetynet.SafetyNetAlerts.controller;

import com.safetynet.SafetyNetAlerts.DTO.ChildAlertDTO;
import com.safetynet.SafetyNetAlerts.service.ChildAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {

    @Autowired
    private ChildAlertService childAlertService;

    @GetMapping
    public ResponseEntity<ChildAlertDTO> getChildAlert(@RequestParam String address) {
        ChildAlertDTO childAlertDTO = childAlertService.getChildAlertInfo(address);

        if (childAlertDTO == null || (childAlertDTO.getChildren().isEmpty() && childAlertDTO.getHouseholdMembers().isEmpty())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(childAlertDTO, HttpStatus.OK);
    }
}
