package com.example.backend_shopfromhome.Controller;

import com.example.backend_shopfromhome.Model.Ordine;
import com.example.backend_shopfromhome.Model.Utente;
import com.example.backend_shopfromhome.Repository.UtenteRepository;
import org.springframework.security.core.Authentication;
import com.example.backend_shopfromhome.Model.StatoOrdine;
import com.example.backend_shopfromhome.Service.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/ordini")
public class OrdineController {
    private final OrdineService ordineService;
    private final UtenteRepository utenteRepository;

    @Autowired
	public OrdineController(
        OrdineService ordineService,
        UtenteRepository utenteRepository
) {
    this.ordineService = ordineService;
    this.utenteRepository = utenteRepository;
}

    @PreAuthorize("hasRole('MAGAZZINIERE')")
    @GetMapping
    public List<Ordine> getAllOrdini() {
        List<Ordine> ordini = ordineService.getAllOrdini();
        System.out.println("Richiesta per tutti gli ordini. Totale ordini: " + ordini.size());
        // Stampa i dettagli di ogni ordine
        for (Ordine ordine : ordini) {
            System.out.println("Ordine ID: " + ordine.getId() +
                    ", Utente ID: " + ordine.getUtente().getId() +
                    ", Totale Prezzo: " + ordine.getTotalePrezzo() +
                    ", Stato: " + ordine.getStato() +
                    ", Data Ritiro: " + ordine.getDataRitiro() +
                    ", Data Ordine: " + ordine.getDataOrdine());
        }
        System.out.println("ordini: " + ordini);
        return ordini;
    }

    
@PostMapping
public ResponseEntity<Ordine> creaOrdine(
        @RequestBody Ordine ordine,
        Authentication authentication
) {
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    if (ordine.getDettagliOrdine() == null
            || ordine.getDettagliOrdine().isEmpty()) {
        return ResponseEntity.badRequest().build();
    }

    boolean dettagliNonValidi = ordine.getDettagliOrdine().stream()
            .anyMatch(dettaglio ->
                    dettaglio.getProdottoId() == null
                            || dettaglio.getQuantita() <= 0
            );

    if (dettagliNonValidi) {
        return ResponseEntity.badRequest().build();
    }

    Utente utente = utenteRepository.findByEmail(authentication.getName())
            .orElse(null);

    if (utente == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // L'utente viene ricavato dal JWT, non dal corpo della richiesta.
    ordine.setUtente(utente);

    // Il backend calcolerà il totale usando i prezzi dei prodotti.
    ordine.setTotalePrezzo(null);

    // Il backend assegna lo stato iniziale.
    ordine.setStato(com.example.backend_shopfromhome.Model.StatoOrdine.IN_LAVORAZIONE);

    
try {
    Ordine ordineCreato = ordineService.salvaOrdine(ordine);
    return ResponseEntity.status(HttpStatus.CREATED).body(ordineCreato);
} catch (IllegalArgumentException e) {
    return ResponseEntity.badRequest().build();
} catch (org.springframework.dao.DataAccessException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
}
}

    @PreAuthorize("hasRole('MAGAZZINIERE')")
    @PatchMapping("/{ordineId}/stato")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long ordineId, @RequestBody String stato) {
        System.out.println("Aggiornamento stato per ordine ID: " + ordineId + " a stato: " + stato);

        try {
            // Log per vedere la stringa ricevuta
            System.out.println("Stato ricevuto (prima di conversione): " + stato);
            StatoOrdine nuovoStato = StatoOrdine.fromString(stato); // Usa il metodo di conversione
            Ordine updatedOrder = ordineService.updateOrderStatus(ordineId, nuovoStato);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            System.out.println("Errore nella conversione dello stato: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Stato non valido. Assicurati che il valore dello stato sia corretto.");
        } catch (Exception e) {
            System.out.println("Errore generico: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nell'aggiornamento dello stato dell'ordine.");
        }
    }



    private boolean isValidStatoOrdine(String stato) {
        for (StatoOrdine statoOrdine : StatoOrdine.values()) {
            if (statoOrdine.name().equals(stato.trim().toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    // Endpoint per ottenere ordini per stato
    @PreAuthorize("hasRole('MAGAZZINIERE')")
    @GetMapping("/stato/{stato}")
    public List<Ordine> getOrdiniByStato(@PathVariable("stato") StatoOrdine stato) {
        System.out.println("Richiesta per ordini con stato: " + stato);
        return ordineService.findByStato(stato);
    }

    // Endpoint per ottenere ordini per utente
    
@GetMapping("/utente/{utenteId}")
public ResponseEntity<?> getOrdiniByUtente(
        @PathVariable("utenteId") Long utenteId,
        Authentication authentication
) {
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Utente utenteAutenticato = utenteRepository
            .findByEmail(authentication.getName())
            .orElse(null);

    if (utenteAutenticato == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    boolean isMagazziniere = authentication.getAuthorities().stream()
            .anyMatch(authority ->
                    authority.getAuthority().equals("ROLE_MAGAZZINIERE"));

    if (!isMagazziniere && !utenteAutenticato.getId().equals(utenteId)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(ordineService.findByUtenteId(utenteId));
}


    

@GetMapping("/{ordineId}")
public ResponseEntity<Ordine> getOrdineById(
        @PathVariable Long ordineId,
        Authentication authentication
) {
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Utente utenteAutenticato = utenteRepository
            .findByEmail(authentication.getName())
            .orElse(null);

    if (utenteAutenticato == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Ordine ordine;
    try {
        ordine = ordineService.getOrdineById(ordineId);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }

    if (ordine == null) {
        return ResponseEntity.notFound().build();
    }

    boolean isMagazziniere = authentication.getAuthorities().stream()
            .anyMatch(authority ->
                    authority.getAuthority().equals("ROLE_MAGAZZINIERE"));

    if (!isMagazziniere
            && (ordine.getUtente() == null
            || !utenteAutenticato.getId().equals(ordine.getUtente().getId()))) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(ordine);
}


    
@PutMapping("/{ordineId}/annulla")
public ResponseEntity<?> annullaOrdine(
        @PathVariable Long ordineId,
        Authentication authentication
) {
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Utente utenteAutenticato = utenteRepository
            .findByEmail(authentication.getName())
            .orElse(null);

    if (utenteAutenticato == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Ordine ordine;
    try {
        ordine = ordineService.getOrdineById(ordineId);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }

    boolean isMagazziniere = authentication.getAuthorities().stream()
            .anyMatch(authority ->
                    authority.getAuthority().equals("ROLE_MAGAZZINIERE"));

    if (!isMagazziniere
            && (ordine.getUtente() == null
            || !utenteAutenticato.getId().equals(ordine.getUtente().getId()))) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    try {
        ordineService.annullaOrdine(ordineId);
        return ResponseEntity.ok().build();
    } catch (IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Errore durante l'annullamento dell'ordine.");
    }
  }
}