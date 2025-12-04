package com.example.vet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "veterinarian")
public class Veterinarian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veterinarian")
    private Integer idVeterinarian;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "license_number", unique = true, nullable = false)
    private String licenseNumber;

    // --- ¡CORRECCIÓN! ---
    // Se eliminan los campos 'phone' y 'email' de esta tabla.

    // --- ¡NUEVA RELACIÓN! ---
    // Esta es la conexión a la tabla de usuarios.
    // (Usa 'User' o 'AppUser' dependiendo de cómo llamaste a tu clase de usuario)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User appUser; 

    @OneToMany(mappedBy = "veterinarian")
    @JsonIgnore
    private List<Shift> shifts;

    @OneToMany(mappedBy = "veterinarian")
    @JsonIgnore
    private List<MedicalHistory> medicalHistories;

    // --- GETTERS Y SETTERS (Actualizados) ---

    public Integer getIdVeterinarian() {
        return idVeterinarian;
    }

    public void setIdVeterinarian(Integer idVeterinarian) {
        this.idVeterinarian = idVeterinarian;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }

    public List<MedicalHistory> getMedicalHistories() {
        return medicalHistories;
    }

    public void setMedicalHistories(List<MedicalHistory> medicalHistories) {
        this.medicalHistories = medicalHistories;
    }

    // Getter y Setter para la nueva relación con User
    public User getAppUser() {
        return appUser;
    }

    public void setAppUser(User appUser) {
        this.appUser = appUser;
    }
}