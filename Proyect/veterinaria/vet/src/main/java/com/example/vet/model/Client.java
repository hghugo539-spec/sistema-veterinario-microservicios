package com.example.vet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "client")
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Integer idClient;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone; 

 
    // --- ¡NUEVA RELACIÓN! ---
    // Conexión a la tabla de usuarios
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User appUser; // Usa tu clase 'User'

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_address", referencedColumnName = "id_address")
    private Address address;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Pet> pets;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Invoice> invoices;
    
    // --- Getters y Setters (Actualizados) ---
    public Integer getIdClient() { return idClient; }
    public void setIdClient(Integer idClient) { this.idClient = idClient; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public List<Pet> getPets() { return pets; }
    public void setPets(List<Pet> pets) { this.pets = pets; }
    public List<Invoice> getInvoices() { return invoices; }
    public void setInvoices(List<Invoice> invoices) { this.invoices = invoices; }

    // Getter y Setter para la nueva relación con User
    public User getAppUser() { return appUser; }
    public void setAppUser(User appUser) { this.appUser = appUser; }
}