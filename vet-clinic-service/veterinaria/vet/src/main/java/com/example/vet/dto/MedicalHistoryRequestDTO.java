package com.example.vet.dto;

import java.time.LocalDate; // O Date

public class MedicalHistoryRequestDTO {
    private String description;
    private LocalDate date;
    private Integer petId; // ESTE ES EL QUE FALTABA

    public MedicalHistoryRequestDTO() {}

    // --- GETTERS Y SETTERS ---
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getPetId() { return petId; } // <--- ¡AQUÍ ESTÁ!
    public void setPetId(Integer petId) { this.petId = petId; }
}