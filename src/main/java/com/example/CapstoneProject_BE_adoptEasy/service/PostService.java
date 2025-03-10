package com.example.CapstoneProject_BE_adoptEasy.service;


import com.example.CapstoneProject_BE_adoptEasy.exception.NotFoundException;
import com.example.CapstoneProject_BE_adoptEasy.model.Animale;
import com.example.CapstoneProject_BE_adoptEasy.model.Post;
import com.example.CapstoneProject_BE_adoptEasy.model.Utente;
import com.example.CapstoneProject_BE_adoptEasy.payload.PostDTO;
import com.example.CapstoneProject_BE_adoptEasy.repository.AnimaleRepository;
import com.example.CapstoneProject_BE_adoptEasy.repository.PostRepository;
import com.example.CapstoneProject_BE_adoptEasy.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    AnimaleRepository animaleRepository;

    // Metodo per registrare un nuovo post, accessibile solo da ADMIN o VOLUNTEER
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public Post createPost(PostDTO postDTO) {
        // Recupera le entità Animale e Utente dai loro rispettivi ID
        Animale animale = animaleRepository.findById(postDTO.getAnimaleID())
                .orElseThrow(() -> new NotFoundException("Animale non trovato"));

        Utente utente = utenteRepository.findById(postDTO.getUtenteID())
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        // Crea una nuova entità Post
        Post post = new Post();
        post.setAnimale(animale);
        post.setUtente(utente);
        post.setTitle(postDTO.getTitle());
        post.setImage(postDTO.getImage());
        post.setContent(postDTO.getContent());
        post.setPublishDate(postDTO.getPublishDate());

        // Salva il post nel database
        return postRepository.save(post);
    }


    // Metodo per ottenere tutti i post con paginazione
    public Page<PostDTO> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        if (posts.isEmpty()) {
            throw new RuntimeException("Nessun post trovato.");
        }

        List<PostDTO> postDTOs = posts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(postDTOs, pageable, posts.getTotalElements());
    }

    // Metodo per ottenere un post tramite ID
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post con ID " + id + " non trovato."));
        return toDto(post);
    }

    // Metodo per aggiornare un post
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public String updatePost(Long id, PostDTO postDTO) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post con ID " + id + " non trovato."));

        Animale animale = animaleRepository.findById(postDTO.getAnimaleID())
                .orElseThrow(() -> new RuntimeException("Animale con ID " + postDTO.getAnimaleID() + " non trovato."));

        Utente utente = utenteRepository.findById(postDTO.getUtenteID())
                .orElseThrow(() -> new RuntimeException("Utente con ID " + postDTO.getUtenteID() + " non trovato."));

        existingPost.setPublishDate(postDTO.getPublishDate());
        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());
        existingPost.setImage(postDTO.getImage());
        existingPost.setAnimale(animale);
        existingPost.setUtente(utente);
        postRepository.save(existingPost);

        return "Post con ID " + id + " è stato aggiornato con successo.";
    }

    // Metodo per eliminare un post
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public String deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post con ID " + id + " non trovato."));
        postRepository.delete(post);
        return "Post con ID " + id + " è stato eliminato con successo.";
    }

    // Metodo di conversione da DTO a Entity
    /*public Post toEntity(PostDTO postDTO) {
        Post post = new Post();
        post.setPublishDate(LocalDate.now());
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setImage(postDTO.getImage());

        // Recuperiamo l'utente dal suo ID
        Utente utente = utenteRepository.findById(postDTO.getUtenteID())
                .orElseThrow(() -> new RuntimeException("Utente con ID " + postDTO.getUtenteID() + " non trovato."));
        post.setUtente(utente);

        // Recuperiamo l'animale dal suo ID
        Animale animale = animaleRepository.findById(postDTO.getAnimaleID())
                .orElseThrow(() -> new RuntimeException("Animale con ID " + postDTO.getAnimaleID() + " non trovato."));
        post.setAnimale(animale);

        return post;
    }*/


    // Metodo di conversione da Entity a DTO
    public PostDTO toDto(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setPublishDate(post.getPublishDate());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setImage(post.getImage());
        postDTO.setAnimaleID(post.getAnimale().getId_animal());
        postDTO.setUtenteID(post.getUtente().getId_user());
        return postDTO;
    }
}
