package com.safetynet.SafetyNetAlerts.DTO;

import java.util.List;

/**
 * DTO pour transporter les données des réponses aux incendies.
 * Contient des informations sur les personnes vivant à une adresse spécifique et leur station de pompiers associée.
 */

    public class FireResponse {
        private List<PersonDetails> persons;
        private Integer stationNumber;

        public FireResponse() {
        }

        public FireResponse(List<PersonDetails> persons, Integer stationNumber) {
            this.persons = persons;
            this.stationNumber = stationNumber;
        }

        public List<PersonDetails> getPersons() {
            return persons;
        }

        public void setPersons(List<PersonDetails> persons) {
            this.persons = persons;
        }

        public Integer getStationNumber() {
            return stationNumber;
        }

        public void setStationNumber(Integer stationNumber) {
            this.stationNumber = stationNumber;
        }
    }

