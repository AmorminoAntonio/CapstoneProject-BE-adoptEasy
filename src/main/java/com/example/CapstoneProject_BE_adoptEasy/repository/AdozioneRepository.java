package com.example.CapstoneProject_BE_adoptEasy.repository;

import com.example.CapstoneProject_BE_adoptEasy.model.Adozione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdozioneRepository extends JpaRepository<Adozione, Long> {

}
