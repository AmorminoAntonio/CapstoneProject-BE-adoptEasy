package com.example.CapstoneProject_BE_adoptEasy.repository;

import com.example.CapstoneProject_BE_adoptEasy.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UtenteRepository extends JpaRepository<Utente, Long> {
}
