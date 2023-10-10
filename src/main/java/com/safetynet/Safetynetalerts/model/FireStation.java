package com.safetynet.Safetynetalerts.model;

import lombok.ToString;

/**
 * Modèle pour représenter une station de pompiers.
 * Contient des informations sur l'adresse et la station.
 */
@ToString
public class FireStation {
    private String address;
    private int station;

    public FireStation() {
    }

    public FireStation(String address, int station) {
        this.address = address;
        this.station = station;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }
}
