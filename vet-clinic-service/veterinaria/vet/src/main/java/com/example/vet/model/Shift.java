package com.example.vet.model;

import java.time.LocalDate;
import java.time.LocalTime; // O Date, según lo tengas

import jakarta.persistence.Entity; // O String, según lo tengas
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "turnos") // O "shifts"
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Asegúrate de que los tipos de fecha coincidan con lo que usas (LocalDate/String)
    private LocalDate date; 
    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "veterinario_id")
    private Veterinarian veterinarian;

    // --- CONSTRUCTORES ---
    public Shift() {
    }

    public Shift(Integer id, LocalDate date, LocalTime startTime, LocalTime endTime, Veterinarian veterinarian) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.veterinarian = veterinarian;
    }

    // --- GETTERS Y SETTERS MANUALES (OBLIGATORIOS) ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Veterinarian getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(Veterinarian veterinarian) {
        this.veterinarian = veterinarian;
    }
}