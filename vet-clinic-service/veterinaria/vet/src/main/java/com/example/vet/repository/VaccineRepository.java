package com.example.vet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vet.model.Vaccine;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Integer> {

List<Vaccine> findByPet_Id(Integer petId);

    List<Vaccine> findByVaccineNameContainingIgnoreCase(String vaccineName);

}