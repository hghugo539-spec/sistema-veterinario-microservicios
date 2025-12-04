package com.example.vet.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vet.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    // --- CORRECCIÓN ---
    // Antes tenías: findByInvoiceDate (Error, el campo no se llama invoiceDate)
    // Ahora: findByDate (Correcto, porque en el modelo es 'private LocalDate date;')
    
    List<Invoice> findByDate(LocalDate date);
    
    // Si no usas este método en el servicio, incluso puedes borrarlo para evitar problemas.
    // Pero si lo dejas, asegúrate de que se llame 'findByDate'.
}