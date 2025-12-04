package com.example.vet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClientRequestDTO {

    // --- ¡HEMOS QUITADO EMAIL Y PASSWORD! ---
    // Ahora solo pedimos los datos del perfil, porque la cuenta ya existe.

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 20)
    private String phone;

    @NotNull(message = "La dirección no puede ser nula")
    @Valid 
    private AddressRequestDTO address;

    // Getters y Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public AddressRequestDTO getAddress() { return address; }
    public void setAddress(AddressRequestDTO address) { this.address = address; }
}