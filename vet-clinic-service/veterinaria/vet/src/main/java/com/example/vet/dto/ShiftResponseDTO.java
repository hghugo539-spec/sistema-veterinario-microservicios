package com.example.vet.dto;

import java.time.LocalTime;

public class ShiftResponseDTO {
    private Integer idShift;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer idVeterinarian;
    
    // --- NUEVOS CAMPOS (Resuelven la ambigüedad) ---
    private String veterinarianFirstName; 
    private String veterinarianLastName;

    // Getters y Setters
    public Integer getIdShift() { return idShift; }
    public void setIdShift(Integer idShift) { this.idShift = idShift; }
    
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    
    public Integer getIdVeterinarian() { return idVeterinarian; }
    public void setIdVeterinarian(Integer idVeterinarian) { this.idVeterinarian = idVeterinarian; }

    // --- GETTERS Y SETTERS CORREGIDOS ---
    public String getVeterinarianFirstName() { 
        return veterinarianFirstName; 
    }
    public void setVeterinarianFirstName(String veterinarianFirstName) { 
        this.veterinarianFirstName = veterinarianFirstName; 
    }

    public String getVeterinarianLastName() { 
        return veterinarianLastName; 
    }
    public void setVeterinarianLastName(String veterinarianLastName) { 
        this.veterinarianLastName = veterinarianLastName; 
    }
}