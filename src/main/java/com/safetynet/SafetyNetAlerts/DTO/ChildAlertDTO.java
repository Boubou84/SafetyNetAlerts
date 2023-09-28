package com.safetynet.SafetyNetAlerts.DTO;

import java.util.List;

public class ChildAlertDTO {
    private List<ChildAlertInfo> children;
    private List<String> householdMembers;

    public ChildAlertDTO() {
    }

    public ChildAlertDTO(List<ChildAlertInfo> children, List<String> householdMembers) {
        this.children = children;
        this.householdMembers = householdMembers;
    }

    public List<ChildAlertInfo> getChildren() {
        return children;
    }

    public void setChildren(List<ChildAlertInfo> children) {
        this.children = children;
    }

    public List<String> getHouseholdMembers() {
        return householdMembers;
    }

    public void setHouseholdMembers(List<String> householdMembers) {
        this.householdMembers = householdMembers;
    }
}