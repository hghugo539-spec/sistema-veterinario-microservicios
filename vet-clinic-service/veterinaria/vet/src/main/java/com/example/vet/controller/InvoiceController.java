package com.example.vet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vet.dto.InvoiceRequestDTO;
import com.example.vet.dto.InvoiceResponseDTO;
import com.example.vet.service.InvoiceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    // Constructor Manual
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponseDTO> create(@RequestBody @Valid InvoiceRequestDTO request) {
        return new ResponseEntity<>(invoiceService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponseDTO>> getAll() { // Nombre corregido
        return ResponseEntity.ok(invoiceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDTO> getById(@PathVariable Integer id) { // Nombre corregido
        return ResponseEntity.ok(invoiceService.getById(id));
    }

    // Comentamos métodos que no existen en el servicio base (ej. por fecha o cliente)
    /*
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<InvoiceResponseDTO>> getByClientId(@PathVariable Integer clientId) {
        // return ResponseEntity.ok(invoiceService.getInvoicesByClientId(clientId));
    }
    */

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid InvoiceRequestDTO request) {
        return ResponseEntity.ok(invoiceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        invoiceService.delete(id); // Nombre corregido
        return ResponseEntity.noContent().build();
    }
}