package com.example.vet.dto;

import java.time.LocalTime;

public class ShiftDTO {
    // No necesitamos ID aquí porque es para crear
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer idVeterinarian; // Solo recibimos el ID del veterinario

    // Getters y Setters
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    
    public Integer getIdVeterinarian() { return idVeterinarian; }
    public void setIdVeterinarian(Integer idVeterinarian) { this.idVeterinarian = idVeterinarian; }
}