package com.example.CapstoneProject_BE_adoptEasy.security;

import com.example.CapstoneProject_BE_adoptEasy.model.Utente;
import com.example.CapstoneProject_BE_adoptEasy.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente user = utenteRepository.findByUsername(username).orElseThrow();

        return User.builder().username(user.getUsername())
                .password((user.getPassword()))
                .roles(String.valueOf(user.getRole())).build();
    }
}
