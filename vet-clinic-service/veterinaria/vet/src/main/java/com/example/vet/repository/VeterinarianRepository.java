package com.example.vet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vet.model.Veterinarian;

@Repository
public interface VeterinarianRepository extends JpaRepository<Veterinarian, Integer> {

  
}