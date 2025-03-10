package com.example.CapstoneProject_BE_adoptEasy.model;

import com.example.CapstoneProject_BE_adoptEasy.enumerated.PostStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


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

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "id_animal")
    private Animale animale;

}
