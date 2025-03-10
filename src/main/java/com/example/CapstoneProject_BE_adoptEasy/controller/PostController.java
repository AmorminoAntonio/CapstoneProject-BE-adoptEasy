package com.example.CapstoneProject_BE_adoptEasy.controller;


import com.example.CapstoneProject_BE_adoptEasy.model.Post;
import com.example.CapstoneProject_BE_adoptEasy.payload.PostDTO;
import com.example.CapstoneProject_BE_adoptEasy.payload.response.ErrStatusResponse;
import com.example.CapstoneProject_BE_adoptEasy.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
public class PostController {
    @Autowired
    private PostService postService;

    // Metodo per registrare un nuovo post
    @PostMapping("/admin/post/create")
    public ResponseEntity<?> registerPost(@Validated @RequestBody PostDTO postDTO) {
        try {
            Post postResponse = postService.createPost(postDTO);
            return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            ErrStatusResponse errorResponse = new ErrStatusResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint per ottenere tutti i post con paginazione
    @GetMapping("/admin/post/all")
    public ResponseEntity<Page<PostDTO>> getAllPosts(Pageable pageable) {
        Page<PostDTO> posts = postService.getAllPosts(pageable);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Endpoint per ottenere un post tramite ID
    @GetMapping("/admin/post/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            PostDTO postDTO = postService.getPostById(id);
            return new ResponseEntity<>(postDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Post con ID " + id + " non trovato.", HttpStatus.NOT_FOUND);
        }
    }

    // Metodo per aggiornare un post
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(@PathVariable Long id, @Validated @RequestBody PostDTO postDTO) {
        try {
            String message = postService.updatePost(id, postDTO);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'aggiornamento del post: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Metodo per eliminare un post
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        try {
            String message = postService.deletePost(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'eliminazione del post: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
