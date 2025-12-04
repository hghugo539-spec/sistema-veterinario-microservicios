package com.example.vet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vet.model.Shift;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    
    // Esta línea hace la magia: busca dentro del objeto 'veterinarian' el campo 'idVeterinarian'
List<Shift> findByVeterinarian_Id(Integer veterinarianId);
}