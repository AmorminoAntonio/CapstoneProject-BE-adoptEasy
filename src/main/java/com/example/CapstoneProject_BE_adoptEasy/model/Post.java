package com.example.CapstoneProject_BE_adoptEasy.model;

import com.example.CapstoneProject_BE_adoptEasy.enumerated.PostStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_post;

    @Column(nullable = false)
    private LocalDate publishDate;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private PostStatusType status;

    @Column(nullable = true)
    private LocalDate lastModified;

    @Column(nullable = false)
    private Boolean visible;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "id_animal")
    private Animale animale;


}
