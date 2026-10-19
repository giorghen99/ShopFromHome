package com.example.backend_shopfromhome.Controller;

import com.example.backend_shopfromhome.Model.Ordine;
import com.example.backend_shopfromhome.Model.StatoOrdine;
import com.example.backend_shopfromhome.Service.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordini")
public class OrdineController {
    private final OrdineService ordineService;

    @Autowired
    public OrdineController(OrdineService ordineService) {
        this.ordineService = ordineService;
    }

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
    public ResponseEntity<Ordine> creaOrdine(@RequestBody Ordine ordine) {
        System.out.println("Creazione di un nuovo ordine: " + ordine);

        // Non è più necessario calcolare il prezzo totale, viene preso direttamente dalla richiesta
        ordine.getDettagliOrdine().forEach(dettaglio -> {
            if (dettaglio.getProdottoId() == null) {
                throw new RuntimeException("ID del prodotto mancante nel dettaglio ordine");
            }
        });

        Ordine ordineCreato = ordineService.salvaOrdine(ordine);
        System.out.println("Ordine creato con ID: " + ordineCreato.getId());
        return ResponseEntity.ok(ordineCreato);
    }

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
    @GetMapping("/stato/{stato}")
    public List<Ordine> getOrdiniByStato(@PathVariable("stato") StatoOrdine stato) {
        System.out.println("Richiesta per ordini con stato: " + stato);
        return ordineService.findByStato(stato);
    }

    // Endpoint per ottenere ordini per utente
    @GetMapping("/utente/{utenteId}")
    public List<Ordine> getOrdiniByUtente(@PathVariable("utenteId") Long utenteId) {
        System.out.println("Richiesta per ordini dell'utente ID: " + utenteId);
        return ordineService.findByUtenteId(utenteId);
    }

    @GetMapping("/{ordineId}")
    public ResponseEntity<Ordine> getOrdineById(@PathVariable Long ordineId) {
        System.out.println("Richiesta per ordine ID: " + ordineId);
        Ordine ordine = ordineService.getOrdineById(ordineId);
        if (ordine != null) {
            System.out.println("Ordine trovato: " + ordine);
            return ResponseEntity.ok(ordine);
        } else {
            System.out.println("Ordine non trovato con ID: " + ordineId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{ordineId}/annulla")
    public ResponseEntity<Void> annullaOrdine(@PathVariable Long ordineId) {
        try {
            ordineService.annullaOrdine(ordineId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}