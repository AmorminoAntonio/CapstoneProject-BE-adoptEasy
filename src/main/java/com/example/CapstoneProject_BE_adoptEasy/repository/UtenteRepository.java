package com.example.CapstoneProject_BE_adoptEasy.repository;

import com.example.CapstoneProject_BE_adoptEasy.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Utente> findByUsername(String username);
}
