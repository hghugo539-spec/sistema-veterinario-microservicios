package com.example.vet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "direcciones") // O "addresses"
public class Address {

    @Id // <--- ESTO ES VITAL
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // <--- Debe llamarse "id", no "addressId"

    private String street;
    private String city;
    private String state;
    private String zipCode;

    // --- Constructor Vacío ---
    public Address() {}

    // --- GETTERS Y SETTERS MANUALES (OBLIGATORIOS) ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}