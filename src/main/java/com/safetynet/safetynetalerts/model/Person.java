package com.safetynet.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.ToString;

import java.util.Objects;

/**
 * Modèle pour représenter une personne.
 * Contient des informations de base sur une personne, son nom, son adresse, son téléphone, etc.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Person {
    @JsonProperty("firstName")
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @JsonProperty("lastName")
    @NotBlank(message = "Le nom de famille est obligatoire")
    private String lastName;

    @JsonProperty("address")
   // @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    @JsonProperty("city")
   // @NotBlank(message = "La ville est obligatoire")
    private String city;

    @JsonProperty("zip")
   // @Positive(message = "Le code postal doit être positif")
    private int zip;

    @JsonProperty("phone")
    //@NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String phone;

    @JsonProperty("email")
    // @Email(message = "L'e-mail doit être valide")
    // @NotBlank(message = "L'e-mail est obligatoire")
    private String email;

    public Person() {
    }

    public Person(String firstName, String lastName, String address, String city, int zip, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Person person = (Person) obj;

        return Objects.equals(firstName, person.firstName) &&
                Objects.equals(lastName, person.lastName);
    }
}
