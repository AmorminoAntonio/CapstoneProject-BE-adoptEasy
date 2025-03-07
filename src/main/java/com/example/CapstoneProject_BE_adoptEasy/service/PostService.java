package com.example.CapstoneProject_BE_adoptEasy.service;

import com.example.CapstoneProject_BE_adoptEasy.model.Post;
import com.example.CapstoneProject_BE_adoptEasy.payload.PostDTO;
import com.example.CapstoneProject_BE_adoptEasy.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PostService {
    @Autowired
    private PostRepository postRepository;

    // Metodo per registrare un nuovo post, accessibile solo da ADMIN o VOLUNTEER
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public Post registerPost(PostDTO postDTO) {
        Post post = toEntity(postDTO);
        return postRepository.save(post);
    }

    // Metodo per ottenere tutti i post
    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        if (posts.isEmpty()) {
            throw new RuntimeException("Nessun post trovato.");
        }
        return posts;
    }

    // Metodo per ottenere un post tramite ID
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post con ID " + id + " non trovato."));
    }

    // Metodo per aggiornare un post
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public Post updatePost(Long id, PostDTO postDTO) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post con ID " + id + " non trovato."));

        // Aggiorniamo le informazioni del post
        existingPost.setPublishDate(postDTO.getPublishDate());
        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());
        existingPost.setImage(postDTO.getImage());
        existingPost.setStatus(postDTO.getStatus());
        existingPost.setLastModified(postDTO.getLastModified());
        existingPost.setVisible(postDTO.getVisible());
        existingPost.setAnimale(postDTO.getAnimale());
        existingPost.setUtente(postDTO.getUtente());

        return postRepository.save(existingPost);
    }

    // Metodo per eliminare un post
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post con ID " + id + " non trovato."));
        postRepository.delete(post);
    }

    // Metodo di conversione da DTO a Entity
    public Post toEntity(PostDTO postDTO) {
        Post post = new Post();
        post.setPublishDate(postDTO.getPublishDate());
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setImage(postDTO.getImage());
        post.setStatus(postDTO.getStatus());
        post.setLastModified(postDTO.getLastModified());
        post.setVisible(postDTO.getVisible());
        post.setAnimale(postDTO.getAnimale());
        post.setUtente(postDTO.getUtente());
        return post;
    }

    // Metodo di conversione da Entity a DTO
    public PostDTO toDto(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setPublishDate(post.getPublishDate());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setImage(post.getImage());
        postDTO.setStatus(post.getStatus());
        postDTO.setLastModified(post.getLastModified());
        postDTO.setVisible(post.getVisible());
        postDTO.setAnimale(post.getAnimale());
        postDTO.setUtente(post.getUtente());
        return postDTO;
    }
}
