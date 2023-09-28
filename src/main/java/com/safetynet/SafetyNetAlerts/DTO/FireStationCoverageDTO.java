package com.safetynet.SafetyNetAlerts.DTO;

import java.util.List;

public class FireStationCoverageDTO {
    private List<PersonFireStationDTO> persons;
    private int adultCount;
    private int childCount;

    public FireStationCoverageDTO() {
    }

    public FireStationCoverageDTO(List<PersonFireStationDTO> persons, int adultCount, int childCount) {
        this.persons = persons;
        this.adultCount = adultCount;
        this.childCount = childCount;
    }

    public List<PersonFireStationDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonFireStationDTO> persons) {
        this.persons = persons;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }
}
