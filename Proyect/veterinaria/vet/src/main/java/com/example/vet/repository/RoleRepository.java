package com.example.vet.repository;

import com.example.vet.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.example.vet.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {


    Optional<Role> findByName(String name);
}