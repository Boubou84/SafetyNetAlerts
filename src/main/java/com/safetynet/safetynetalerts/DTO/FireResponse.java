package com.safetynet.safetynetalerts.DTO;

import java.util.List;

/**
 * DTO pour transporter les données des réponses aux incendies.
 * Contient des informations sur les personnes vivant à une adresse spécifique et leur station de pompiers associée.
 */

    public class FireResponse {
        private List<PersonDetails> persons;

        private Integer station;

        public FireResponse() {
        }

        public FireResponse(List<PersonDetails> persons, Integer station) {
            this.persons = persons;
            this.station = station;
        }

        public List<PersonDetails> getPersons() {
            return persons;
        }

        public void setPersons(List<PersonDetails> persons) {
            this.persons = persons;
        }

        public Integer getStation() {
            return station;
        }

        public void setStation(Integer station) {
            this.station = station;
        }
}

