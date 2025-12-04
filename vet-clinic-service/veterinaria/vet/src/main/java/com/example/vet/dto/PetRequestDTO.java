package com.example.vet.dto;

import java.time.LocalDate;

public class PetRequestDTO {
    private String name;
    private String species; // Lo manejaremos como texto para no fallar por FK
    private String breed;
    private String color;
    private LocalDate birthDate; 
    private Integer clientId; // ID del dueño

    public PetRequestDTO() {} 

    // --- GETTERS Y SETTERS ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public Integer getClientId() { return clientId; }
    public void setClientId(Integer clientId) { this.clientId = clientId; }
}