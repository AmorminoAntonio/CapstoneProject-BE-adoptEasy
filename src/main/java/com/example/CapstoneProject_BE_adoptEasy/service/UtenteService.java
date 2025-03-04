package com.example.CapstoneProject_BE_adoptEasy.service;

import com.example.CapstoneProject_BE_adoptEasy.enumerated.RoleType;
import com.example.CapstoneProject_BE_adoptEasy.exception.EmailDuplicatedException;
import com.example.CapstoneProject_BE_adoptEasy.exception.NotFoundException;
import com.example.CapstoneProject_BE_adoptEasy.exception.UsernameDuplicatedException;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UtenteService {
    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    public String registrazioneUtente(RegistrationReq registrazione) {
        String passwordCodificata = passwordEncoder.encode(registrazione.getPassword());
        checkDuplicateKey(registrazione.getUsername(), registrazione.getEmail());
        Utente user = registrazioneRequest_Utente(registrazione);
        user.setPassword(passwordCodificata);
        // controllo assegnazione role
        if (registrazione.getRole() == null || registrazione.getRole().equals(RoleType.ADOPTER)) {
            user.setRole(RoleType.ADOPTER);
        } else if (registrazione.getRole().equals(RoleType.ADMIN)) {
            user.setRole(RoleType.ADMIN);
        } else if (registrazione.getRole().equals(RoleType.VOLUNTEER)) {
            user.setRole(RoleType.VOLUNTEER);
        } else {
            throw new RuntimeException("Errore: Il Valore inserito come ruolo non è valido!");
        }
        long id = utenteRepository.save(user).getId_user();
        return "Nuovo utente " + user.getUsername() + "con id " + id + " è stato inserito correttamente";
    }

    public LogResponse login(String username, String password) {

        // 1. AUTENTICAZIONE DELL'UTENTE IN FASE DI LOGIN
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // 2. INSERIMENTO DELL'AUTENTICAZIONE UTENTE NEL CONTESTO DELLA SICUREZZA
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. RECUPERO RUOLI --> String
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            try {
                // Estrai il ruolo come RoleType (presupponendo che il nome dell'autorità corrisponda a un valore della mia enum RoleType)
                RoleType.valueOf(authority.getAuthority().replace("ROLE_", ""));
                break;  // Poiché il login di solito ha un solo ruolo, esci dopo il primo
            } catch (IllegalArgumentException e) {
                System.out.println("Errore: il ruolo non è valido: " + authority.getAuthority());
            }
        }

        // 4. GENERO L'UTENTE
        Utente user = new Utente();
        user.setUsername(username);
        user.setRole(RoleType.ADOPTER);

        // 5. GENERO IL TOKEN
        String token = jwtUtil.creaToken(user);

        // 6. CREO L'OGGETTO DI RISPOSTA AL CLIENT
        return new LogResponse(username, token);
    }


    public RegistrationReq findUserById(long id) {
        Optional<Utente> utenteTrovato = utenteRepository.findById(id);

        if (utenteTrovato.isPresent()) {
            return utente_registrazioneRequest(utenteTrovato.get());
        } else {
            throw new NotFoundException("Nessun utente trovato con l'id: " + id);
        }
    }

    public String updateUsername(String username, long id) {
        Optional<Utente> utenteTrovato = utenteRepository.findById(id);
        // l'oggetto è agganciato al DB
        Utente user = utenteTrovato.orElseThrow();
        // Hibernate effettua un update sulla tabella utente
        user.setUsername(username);
        return "username aggiornato correttamente --> " + username;
    }

    public String updateAvatar(long idUtente, String url) {
        Utente utente = utenteRepository.findById(idUtente).orElseThrow();
        utente.setAvatarUtente(url);
        return "Immagine dell'avatar modificata";
    }

    public Page<RegistrationReq> getAllUtenti(Pageable page) {
        Page<Utente> listaUtenti = utenteRepository.findAll(page);
        List<RegistrationReq> listaDto = new ArrayList<>();

        for (Utente utente : listaUtenti.getContent()) {
            RegistrationReq dto = utente_registrazioneRequest(utente);
            listaDto.add(dto);
        }
        return new PageImpl<>(listaDto);
    }

    public void checkDuplicateKey(String username, String email) throws
            UsernameDuplicatedException, EmailDuplicatedException {

        if (utenteRepository.existsByUsername(username)) {
            throw new UsernameDuplicatedException("Username già utilizzato, non disponibile");
        }

        if (utenteRepository.existsByEmail(email)) {
            throw new EmailDuplicatedException("Email già utilizzata da un altro utente");
        }
    }

    public Utente registrazioneRequest_Utente(RegistrationReq request) {
        Utente utente = new Utente();
        utente.setEmail(request.getEmail());
        utente.setFirstName(request.getFirstName());
        utente.setLastName(request.getLastName());
        utente.setUsername(request.getUsername());
        utente.setAvatarUtente(request.getAvatarUtente());
        utente.setAddress(request.getAddress());
        utente.setPhone(request.getPhone());
        utente.setRegistrationDate(request.getRegistrationDate());
        return utente;
    }

    public RegistrationReq utente_registrazioneRequest(Utente utente) {
        RegistrationReq registrationReq = new RegistrationReq();
        registrationReq.setEmail(utente.getEmail());
        registrationReq.setFirstName(utente.getFirstName());
        registrationReq.setLastName(utente.getLastName());
        registrationReq.setUsername(utente.getUsername());
        registrationReq.setAvatarUtente(utente.getAvatarUtente());
        registrationReq.setAddress(utente.getAddress());
        registrationReq.setPhone(utente.getPhone());
        registrationReq.setRegistrationDate(utente.getRegistrationDate());
        return registrationReq;
    }
}
