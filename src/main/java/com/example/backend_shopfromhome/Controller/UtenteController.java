package com.example.backend_shopfromhome.Controller;

import com.example.backend_shopfromhome.Model.Ruolo;
import com.example.backend_shopfromhome.Model.Utente;
import com.example.backend_shopfromhome.Service.UtenteService;
import com.example.backend_shopfromhome.Security.JwtTokenUtil;
import com.example.backend_shopfromhome.Security.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    private final UtenteService utenteService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UtenteController(UtenteService utenteService, JwtTokenUtil jwtTokenUtil) {
        this.utenteService = utenteService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Utente utente) {
        try {
            // Creazione dell'utente
            Utente createdUser = utenteService.createUser(utente);

            // Generazione del token JWT
            String token = jwtTokenUtil.generateToken(createdUser);

            // Risposta contenente l'utente creato e il token
            return ResponseEntity.status(HttpStatus.CREATED).body(new JwtResponse(token, createdUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante la registrazione: " + e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<Utente> getUserByEmail(@PathVariable String email) {
        Optional<Utente> user = utenteService.findByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/ruolo/{ruolo}")
    public List<Utente> getUtentiByRuolo(@PathVariable("ruolo") Ruolo ruolo) {
        return utenteService.findByRuolo(ruolo);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Utente userInput) {
        Optional<Utente> user = utenteService.findByEmail(userInput.getEmail());

        // Controllo dell'utente e della password
        if (user.isPresent() && user.get().checkPassword(userInput.getPassword())) {
            // Generazione del token
            String token = jwtTokenUtil.generateToken(user.get());

            // Restituzione del token e dell'ID dell'utente nel header
            return ResponseEntity.ok()
                    .header("User-Id", user.get().getId().toString()) // Aggiungi l'ID utente nei headers
                    .body(token); // Restituisci solo il token nel body
        }

        // Se le credenziali non corrispondono
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
