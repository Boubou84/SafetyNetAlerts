package com.safetynet.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.ToString;

/**
 * Modèle pour représenter une station de pompiers.
 * Contient des informations sur l'adresse et la station.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class FireStation {
    @JsonProperty("address")
    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    @JsonProperty("station")
    @Positive(message = "Le numéro de la station doit être positif")
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
