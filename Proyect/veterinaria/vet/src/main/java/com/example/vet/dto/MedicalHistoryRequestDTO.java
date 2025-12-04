package com.example.vet.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MedicalHistoryRequestDTO {

    @NotNull(message = "El ID de la mascota no puede ser nulo")
    private Integer idPet;

    @NotNull(message = "El ID del veterinario no puede ser nulo")
    private Integer idVeterinarian;

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDate date;

    @NotBlank(message = "El diagnóstico/motivo no puede estar vacío")
    private String diagnosis;

    private String treatment;
    
    // --- CAMPO NUEVO PARA MANEJAR EL ESTADO ---
    private String status; 

    // --- GETTERS Y SETTERS ---
    
    public Integer getIdPet() { return idPet; }
    public void setIdPet(Integer idPet) { this.idPet = idPet; }

    public Integer getIdVeterinarian() { return idVeterinarian; }
    public void setIdVeterinarian(Integer idVeterinarian) { this.idVeterinarian = idVeterinarian; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    // ¡IMPORTANTE! Estos son los que faltaban
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}