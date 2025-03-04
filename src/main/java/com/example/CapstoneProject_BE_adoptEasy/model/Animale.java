package com.example.CapstoneProject_BE_adoptEasy.model;


import com.example.CapstoneProject_BE_adoptEasy.enumerated.AnimalStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "animali")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Animale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_animal;

    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private LocalDate foundDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private AnimalStatusType status;

    @Column(nullable = false)
    private LocalDate availableSince;

    @Column(nullable = false)
    private String photo;

    @Column(nullable = false)
    private String foundLocation;

    @Column(nullable = false)
    private String observation;

    @OneToMany(mappedBy = "animale")
    private List<Post> posts;

    @OneToOne(mappedBy = "animale")
    private Adozione adoption;



}
