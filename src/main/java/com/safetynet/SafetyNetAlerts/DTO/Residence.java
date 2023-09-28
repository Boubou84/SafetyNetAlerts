package com.safetynet.SafetyNetAlerts.DTO;

import java.util.List;

/**
 * DTO pour transporter des informations sur une résidence.
 * Contient des détails tels que l'adresse et les informations sur les résidents.
 */

public class Residence {
    private String address;
    private List<InfoResidents> residents;

    public Residence() {
    }

    public Residence(String address, List<InfoResidents> residents) {
        this.address = address;
        this.residents = residents;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<InfoResidents> getResidents() {
        return residents;
    }

    public void setResidents(List<InfoResidents> residents) {
        this.residents = residents;
    }
}
