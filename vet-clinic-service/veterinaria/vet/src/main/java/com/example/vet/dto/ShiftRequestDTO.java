package com.example.vet.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public class ShiftRequestDTO {
    @NotNull private String dayOfWeek;
    @NotNull private LocalTime startTime;
    @NotNull private LocalTime endTime;
    @NotNull private Integer idVeterinarian;

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