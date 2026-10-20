package com.example.backend_shopfromhome.Controller;

import com.example.backend_shopfromhome.Model.Carrello;
import com.example.backend_shopfromhome.Repository.ProdottoRepository;
import com.example.backend_shopfromhome.Model.DettagliCarrello;
import com.example.backend_shopfromhome.Model.Prodotto;
import com.example.backend_shopfromhome.Service.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend_shopfromhome.Model.Utente;
import com.example.backend_shopfromhome.Repository.UtenteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/carrelli")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;
    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    private UtenteRepository utenteRepository;

    
@PostMapping("/{idCarrello}/aggiungi")
public ResponseEntity<DettagliCarrello> aggiungiProdotto(
        @PathVariable Long idCarrello,
        @RequestBody DettagliCarrello dettagliCarrello,
        Authentication authentication
) {
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Utente utente = utenteRepository
            .findByEmail(authentication.getName())
            .orElse(null);

    if (utente == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    boolean isMagazziniere = authentication.getAuthorities().stream()
            .anyMatch(authority ->
                    authority.getAuthority().equals("ROLE_MAGAZZINIERE"));

    Carrello carrello = carrelloService.getCarrelliByUtente(utente.getId())
            .stream()
            .filter(c -> c.getIdCarrello() != null && c.getIdCarrello().equals(idCarrello))
            .findFirst()
            .orElse(null);

    if (carrello == null && !isMagazziniere) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    if (dettagliCarrello == null
            || dettagliCarrello.getProdotto() == null
            || dettagliCarrello.getProdotto().getId() == null) {
        return ResponseEntity.badRequest().build();
    }

    Prodotto prodotto = prodottoRepository
            .findById(dettagliCarrello.getProdotto().getId())
            .orElse(null);

    if (prodotto == null) {
        return ResponseEntity.badRequest().build();
    }

    dettagliCarrello.setProdotto(prodotto);

    try {
        DettagliCarrello nuovoDettaglio =
                carrelloService.aggiungiProdottoAlCarrello(idCarrello, dettagliCarrello);
        return ResponseEntity.ok(nuovoDettaglio);
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}


    
@DeleteMapping("/{idCarrello}/rimuovi/{idProdotto}")
public ResponseEntity<Void> rimuoviProdotto(
        @PathVariable Long idCarrello,
        @PathVariable Long idProdotto,
        Authentication authentication
) {
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Utente utente = utenteRepository
            .findByEmail(authentication.getName())
            .orElse(null);

    if (utente == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    boolean isMagazziniere = authentication.getAuthorities().stream()
            .anyMatch(authority ->
                    authority.getAuthority().equals("ROLE_MAGAZZINIERE"));

    boolean carrelloAutorizzato = carrelloService
            .getCarrelliByUtente(utente.getId())
            .stream()
            .anyMatch(c -> idCarrello.equals(c.getIdCarrello()));

    if (!carrelloAutorizzato && !isMagazziniere) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    try {
        carrelloService.rimuoviProdottoDalCarrello(idCarrello, idProdotto);
        return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}


    

@PutMapping("/{idCarrello}/aggiorna/{idProdotto}")
public ResponseEntity<Void> aggiornaQuantita(
        @PathVariable Long idCarrello,
        @PathVariable Long idProdotto,
        @RequestParam BigDecimal nuovaQuantita,
        Authentication authentication
) {
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    if (nuovaQuantita == null
            || nuovaQuantita.compareTo(BigDecimal.ZERO) <= 0) {
        return ResponseEntity.badRequest().build();
    }

    Utente utente = utenteRepository
            .findByEmail(authentication.getName())
            .orElse(null);

    if (utente == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    boolean isMagazziniere = authentication.getAuthorities().stream()
            .anyMatch(authority ->
                    authority.getAuthority().equals("ROLE_MAGAZZINIERE"));

    boolean carrelloAutorizzato = carrelloService
            .getCarrelliByUtente(utente.getId())
            .stream()
            .anyMatch(c -> idCarrello.equals(c.getIdCarrello()));

    if (!carrelloAutorizzato && !isMagazziniere) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    try {
        carrelloService.modificaQuantitaProdotto(
                idCarrello, idProdotto, nuovaQuantita);
        return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}

    
@GetMapping("/utente/{idUtente}")
public ResponseEntity<List<Carrello>> getCarrelliByUtente(
        @PathVariable Long idUtente,
        Authentication authentication
) {
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Utente utente = utenteRepository
            .findByEmail(authentication.getName())
            .orElse(null);

    if (utente == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    boolean isMagazziniere = authentication.getAuthorities().stream()
            .anyMatch(authority ->
                    authority.getAuthority().equals("ROLE_MAGAZZINIERE"));

    if (!isMagazziniere && !utente.getId().equals(idUtente)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(carrelloService.getCarrelliByUtente(idUtente));
}


    
@GetMapping("/{idCarrello}/dettagli")
public ResponseEntity<List<DettagliCarrello>> getDettagliCarrello(
        @PathVariable Long idCarrello,
        Authentication authentication
) {
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Utente utente = utenteRepository
            .findByEmail(authentication.getName())
            .orElse(null);

    if (utente == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    boolean isMagazziniere = authentication.getAuthorities().stream()
            .anyMatch(authority ->
                    authority.getAuthority().equals("ROLE_MAGAZZINIERE"));

    boolean carrelloAutorizzato = carrelloService
            .getCarrelliByUtente(utente.getId())
            .stream()
            .anyMatch(c -> idCarrello.equals(c.getIdCarrello()));

    if (!carrelloAutorizzato && !isMagazziniere) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    List<DettagliCarrello> dettagli =
            carrelloService.getDettagliCarrelloById(idCarrello);

    if (dettagli == null) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(dettagli);
}

}