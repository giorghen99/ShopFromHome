package com.example.backend_shopfromhome.Controller;

import com.example.backend_shopfromhome.Model.Carrello;
import com.example.backend_shopfromhome.Repository.ProdottoRepository;
import com.example.backend_shopfromhome.Model.DettagliCarrello;
import com.example.backend_shopfromhome.Model.Prodotto;
import com.example.backend_shopfromhome.Service.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/carrelli")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;
    @Autowired
    private ProdottoRepository prodottoRepository;

    @PostMapping("/{idCarrello}/aggiungi")
    public ResponseEntity<DettagliCarrello> aggiungiProdotto(@PathVariable Long idCarrello, @RequestBody DettagliCarrello dettagliCarrello) {
        if (dettagliCarrello.getProdotto() == null || dettagliCarrello.getProdotto().getId() == null) {
            return ResponseEntity.badRequest().body(null); // Gestisci il caso in cui il prodotto è nullo
        }

        if (idCarrello == null) {
            return ResponseEntity.badRequest().body(null); // Errore: idCarrello mancante
        }

        Prodotto prodotto = prodottoRepository.findById(dettagliCarrello.getProdotto().getId()).orElse(null);
        if (prodotto == null) {
            return ResponseEntity.badRequest().body(null); // Gestisci il caso in cui il prodotto non esiste
        }

        dettagliCarrello.setProdotto(prodotto);
        DettagliCarrello nuovoDettaglio = carrelloService.aggiungiProdottoAlCarrello(idCarrello, dettagliCarrello);
        return ResponseEntity.ok(nuovoDettaglio);
    }

    @DeleteMapping("/{idCarrello}/rimuovi/{idProdotto}")
    public ResponseEntity<Void> rimuoviProdotto(@PathVariable Long idCarrello, @PathVariable Long idProdotto) {
        carrelloService.rimuoviProdottoDalCarrello(idCarrello, idProdotto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idCarrello}/aggiorna/{idProdotto}")
    public ResponseEntity<Void> aggiornaQuantita(@PathVariable Long idCarrello, @PathVariable Long idProdotto, @RequestParam BigDecimal nuovaQuantita) {
        if (nuovaQuantita.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().build(); // Gestisci il caso in cui la quantità è non valida
        }
        carrelloService.modificaQuantitaProdotto(idCarrello, idProdotto, nuovaQuantita);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/utente/{idUtente}")
    public ResponseEntity<List<Carrello>> getCarrelliByUtente(@PathVariable Long idUtente) {
        List<Carrello> carrelli = carrelloService.getCarrelliByUtente(idUtente);
        return ResponseEntity.ok(carrelli);
    }

    @GetMapping("/{idCarrello}/dettagli")
    public ResponseEntity<List<DettagliCarrello>> getDettagliCarrello(@PathVariable Long idCarrello) {
        List<DettagliCarrello> dettagli = carrelloService.getDettagliCarrelloById(idCarrello);
        if (dettagli == null || dettagli.isEmpty()) {
            return ResponseEntity.notFound().build(); // Gestisci il caso in cui non ci siano dettagli
        }
        return ResponseEntity.ok(dettagli);
    }
}