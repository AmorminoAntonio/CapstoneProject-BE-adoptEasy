package com.example.CapstoneProject_BE_adoptEasy.service;

import com.example.CapstoneProject_BE_adoptEasy.model.Utente;
import com.example.CapstoneProject_BE_adoptEasy.payload.request.RegistrationReq;
import com.example.CapstoneProject_BE_adoptEasy.payload.response.LogResponse;
import com.example.CapstoneProject_BE_adoptEasy.repository.UtenteRepository;
import com.example.CapstoneProject_BE_adoptEasy.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Metodo di registrazione utente
    public String registrazioneUtente(RegistrationReq registrazione) {
        String passwordCodificata = passwordEncoder.encode(registrazione.getPassword());
        checkDuplicateKey(registrazione.getUsername(), registrazione.getEmail());
        Utente user = registrazioneRequest_Utente(registrazione);
        user.setPassword(passwordCodificata);

        // Gestione del ruolo
        if (registrazione.getRole() == null || registrazione.getRole().equals("ADOPTER")) {
            user.setRole("ADOPTER");
        } else if (registrazione.getRole().equals("ADMIN")) {
            user.setRole("ADMIN");
        } else if (registrazione.getRole().equals("VOLUNTEER")) {
            user.setRole("VOLUNTEER");
        } else {
            throw new RuntimeException("Errore: Il valore inserito per il ruolo non è valido!");
        }

        long id = utenteRepository.save(user).getId_user();
        return "Nuovo utente " + user.getUsername() + " con id " + id + " è stato inserito correttamente";
    }

    // Metodo di login utente con generazione token
    public LogResponse login(String username, String password) {

        // 1. Autenticazione dell'utente in fase di login
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // 2. Inserimento dell'autenticazione nel contesto di sicurezza
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Recupero dei ruoli dell'utente
        String userRole = null;
        for (var authority : authentication.getAuthorities()) {
            try {
                // Estrai il ruolo come RoleType (presupponendo che il nome dell'autorità corrisponda al valore dell' enum RoleType)
                userRole = authority.getAuthority().replace("ROLE_", "");
                break; // Poiché il login ha un solo ruolo, esci dopo il primo
            } catch (IllegalArgumentException e) {
                System.out.println("Errore: il ruolo non è valido: " + authority.getAuthority());
            }
        }

        // 4. Generazione dell'utente per il token JWT
        Utente user = new Utente();
        user.setUsername(username);
        user.setRole(userRole != null ? userRole : "ADOPTER");

        // 5. Creazione del token JWT
        String token = jwtUtil.creaToken(user);

        // 6. Creazione della risposta al client
        return new LogResponse(username, token);
    }

    // Metodo per trovare un utente per id
    public RegistrationReq findUserById(long id) {
        Optional<Utente> utenteTrovato = utenteRepository.findById(id);

        if (utenteTrovato.isPresent()) {
            return utente_registrazioneRequest(utenteTrovato.get());
        } else {
            throw new RuntimeException("Nessun utente trovato con l'id: " + id);
        }
    }

    // Metodo per aggiornare il nome utente
    public String updateUsername(String username, long id) {
        Optional<Utente> utenteTrovato = utenteRepository.findById(id);
        Utente user = utenteTrovato.orElseThrow(() -> new RuntimeException("Utente non trovato"));

        user.setUsername(username);
        utenteRepository.save(user);
        return "Username aggiornato correttamente a " + username;
    }

    public void modificaAvatar(long idUtente, String url) {
        Utente utente = utenteRepository.findById(idUtente).orElseThrow();
        utente.setAvatarUtente(url);
    }

    // Metodo per ottenere una lista di utenti "paginazione"
    public Page<RegistrationReq> getAllUtenti(Pageable page) {
        Page<Utente> listaUtenti = utenteRepository.findAll(page);
        List<RegistrationReq> listaUtentiDTO = new ArrayList<>();

        for (Utente utente : listaUtenti.getContent()) {
            listaUtentiDTO.add(utente_registrazioneRequest(utente));
        }
        return new PageImpl<>(listaUtentiDTO, page, listaUtenti.getTotalElements());
    }

    // Metodo per controllare i duplicati (username ed email)
    public void checkDuplicateKey(String username, String email) throws RuntimeException {
        if (utenteRepository.existsByUsername(username)) {
            throw new RuntimeException("Username già utilizzato, non disponibile");
        }

        if (utenteRepository.existsByEmail(email)) {
            throw new RuntimeException("Email già utilizzata da un altro utente");
        }
    }

    // ELIMINA UN UTENTE (SOLO ADMIN)
    // Solo gli Admin possono eliminare utenti
    public String deleteUtente(Long id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + id));
        utenteRepository.delete(utente);
        return "Utente con ID: " + id + " eliminato con successo.";
    }

    // OTTIENI UN UTENTE PER ID (SOLO ADMIN)
    public RegistrationReq trova_utente(long id) {
        Optional<Utente> utente = utenteRepository.findById(id);
        if (utente.isPresent()) {
            return utente_registrazioneRequest(utente.get());
        } else {
            throw new RuntimeException("Cliente non trovato");
        }
    }


    // Metodo di creazione dell'utente dalla richiesta di registrazione
    public Utente registrazioneRequest_Utente(RegistrationReq request) {
        Utente utente = new Utente();
        utente.setEmail(request.getEmail());
        utente.setFirstName(request.getFirstName());
        utente.setLastName(request.getLastName());
        utente.setUsername(request.getUsername());
        utente.setAvatarUtente(request.getAvatarUtente());
        utente.setAddress(request.getAddress());
        utente.setPhone(request.getPhone());
        utente.setRegistrationDate(LocalDate.now());
        return utente;
    }

    // Metodo di conversione dell'utente in DTO per la registrazione
    public RegistrationReq utente_registrazioneRequest(Utente utente) {
        RegistrationReq registrationReq = new RegistrationReq();
        registrationReq.setEmail(utente.getEmail());
        registrationReq.setFirstName(utente.getFirstName());
        registrationReq.setLastName(utente.getLastName());
        registrationReq.setUsername(utente.getUsername());
        registrationReq.setAvatarUtente(utente.getAvatarUtente());
        registrationReq.setAddress(utente.getAddress());
        registrationReq.setPhone(utente.getPhone());
        registrationReq.setRegistrationDate(LocalDate.now());
        return registrationReq;
    }
}

