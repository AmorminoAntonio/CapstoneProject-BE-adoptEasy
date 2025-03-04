package com.example.CapstoneProject_BE_adoptEasy.repository;

import com.example.CapstoneProject_BE_adoptEasy.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
