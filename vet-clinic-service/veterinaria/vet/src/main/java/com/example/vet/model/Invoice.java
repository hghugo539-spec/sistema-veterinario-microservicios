package com.example.vet.model;

import java.time.LocalDate;

import jakarta.persistence.Entity; // O Date, según uses
import jakarta.persistence.GeneratedValue; // O Double, según uses
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "facturas") // O "invoices"
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate date; // Fecha de emisión
    private Double totalAmount; // Total de la factura

    // Relación con el Cliente
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // --- CONSTRUCTORES ---
    public Invoice() {
    }

    public Invoice(Integer id, LocalDate date, Double totalAmount, Client client) {
        this.id = id;
        this.date = date;
        this.totalAmount = totalAmount;
        this.client = client;
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}