package com.example.CapstoneProject_BE_adoptEasy.controller;

import com.example.CapstoneProject_BE_adoptEasy.model.Post;
import com.example.CapstoneProject_BE_adoptEasy.payload.PostDTO;
import com.example.CapstoneProject_BE_adoptEasy.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    // Endpoint per registrare un nuovo post
    @PostMapping
    public ResponseEntity<Post> registerPost(@Validated @RequestBody PostDTO postDTO) {
        Post createdPost = postService.registerPost(postDTO);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // Endpoint per ottenere tutti i post
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // Endpoint per ottenere un post tramite ID
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // Endpoint per aggiornare un post
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @Validated @RequestBody PostDTO postDTO) {
        Post updatedPost = postService.updatePost(id, postDTO);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    // Endpoint per eliminare un post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
