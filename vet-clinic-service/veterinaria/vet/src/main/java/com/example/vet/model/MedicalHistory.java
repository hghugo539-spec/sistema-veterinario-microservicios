package com.example.vet.model;

import java.time.LocalDate;

import jakarta.persistence.Entity; // Asegúrate de usar LocalDate si así lo tienes en el DTO
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "historial_medico")
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    // --- CONSTRUCTOR VACÍO ---
    public MedicalHistory() {
    }

    // --- GETTERS Y SETTERS MANUALES (OBLIGATORIOS) ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { // <--- ESTE ES EL QUE FALTABA
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}