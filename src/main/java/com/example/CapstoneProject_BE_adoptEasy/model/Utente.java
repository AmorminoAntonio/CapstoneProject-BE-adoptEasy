package com.example.CapstoneProject_BE_adoptEasy.model;

import com.example.CapstoneProject_BE_adoptEasy.enumerated.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Entity(name = "utenti")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_user;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column
    private String avatarUtente;

    @OneToMany(mappedBy = "utente") // volontario
    private List<Post> posts;

    @OneToMany(mappedBy = "utente") // adottante
    private List<Adozione> adoptions;


}
