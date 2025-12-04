package com.example.vet.dto;
import java.time.LocalDate;

public class PetResponseDTO {
    private Integer idPet;
    private String name;
    private LocalDate birthDate;
    private String breed;
    private Integer idSpecies;
    private Integer idClient;
    
    // --- CAMPO NUEVO ---
    private String ownerName; // Nombre del dueño

    // Getters y Setters normales...
    public Integer getIdPet() { return idPet; }
    public void setIdPet(Integer idPet) { this.idPet = idPet; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public Integer getIdSpecies() { return idSpecies; }
    public void setIdSpecies(Integer idSpecies) { this.idSpecies = idSpecies; }
    public Integer getIdClient() { return idClient; }
    public void setIdClient(Integer idClient) { this.idClient = idClient; }
    
    // Nuevo Getter/Setter
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
}