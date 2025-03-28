package com.example.CapstoneProject_BE_adoptEasy.model;

import com.example.CapstoneProject_BE_adoptEasy.enumerated.AdoptionStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "adozioni")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Adozione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_adoption;

    //data di inizio pratica di adozione
    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private String adoptionNotes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AdoptionStatusType status;

    @Column(nullable = false)
    private Boolean documentsVerified;


    @OneToOne
    @JoinColumn(name = "id_animal")
    private Animale animaleId;

    @ManyToOne
    @JoinColumn(name = "id_user") // adottante
    private Utente utenteId;


}
