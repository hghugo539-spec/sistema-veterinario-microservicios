package com.example.vet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "veterinarios")
public class Veterinarian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    // Esta columna existe en tu BD pero NO se usa en tu API.
    // Para evitar errores, la ignoramos:
    @Column(name = "id_veterinarian", nullable = true)
    private String idVeterinarian;

    // También existe "nombre" en tu tabla.
    // Lo agregamos aunque no lo uses.
    @Column(name = "nombre")
    private String nombre;

    // ======== Getters & Setters ========
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdVeterinarian() {
        return idVeterinarian;
    }

    public void setIdVeterinarian(String idVeterinarian) {
        this.idVeterinarian = idVeterinarian;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
