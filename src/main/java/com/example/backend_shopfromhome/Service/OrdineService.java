// Modificato il servizio OrdineService
package com.example.backend_shopfromhome.Service;

import com.example.backend_shopfromhome.Model.DettaglioOrdine;
import com.example.backend_shopfromhome.Model.Ordine;
import com.example.backend_shopfromhome.Model.Prodotto;
import com.example.backend_shopfromhome.Model.StatoOrdine;
import com.example.backend_shopfromhome.Repository.DettaglioOrdineRepository;
import com.example.backend_shopfromhome.Repository.OrdineRepository;
import com.example.backend_shopfromhome.Repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrdineService {

    private final OrdineRepository ordineRepository;
    private final DettaglioOrdineRepository dettaglioOrdineRepository;
    private final ProdottoRepository prodottoRepository;

    @Autowired
    public OrdineService(OrdineRepository ordineRepository, DettaglioOrdineRepository dettaglioOrdineRepository, ProdottoRepository prodottoRepository) {
        this.ordineRepository = ordineRepository;
        this.dettaglioOrdineRepository = dettaglioOrdineRepository;
        this.prodottoRepository = prodottoRepository;
    }

    public Ordine salvaOrdine(Ordine ordine) {
        System.out.println("Inizio salvataggio ordine");

        if (ordine.getStato() == null) {
            ordine.setStato(StatoOrdine.IN_LAVORAZIONE);  // Stato predefinito
            System.out.println("Stato non fornito, impostato su IN_LAVORAZIONE");
        }

        // Valida l'ordine
        if (ordine.getDettagliOrdine() == null || ordine.getDettagliOrdine().isEmpty()) {
            System.out.println("Errore: l'ordine non contiene dettagli");
            throw new RuntimeException("L'ordine deve contenere almeno un dettaglio");
        }

        // Salva l'ordine principale prima di gestire i dettagli
        Ordine savedOrdine = ordineRepository.save(ordine);
        System.out.println("Ordine salvato con ID: " + savedOrdine.getId());

        // Calcola il totale dell'ordine e salva i dettagli
        BigDecimal totaleOrdine = BigDecimal.ZERO;
        for (DettaglioOrdine dettaglio : ordine.getDettagliOrdine()) {
            Long prodottoId = dettaglio.getProdottoId();
            System.out.println("Tentativo di recuperare prodotto con ID: " + prodottoId);

            Prodotto prodotto = prodottoRepository.findById(prodottoId)
                    .orElseThrow(() -> {
                        System.out.println("Errore: prodotto non trovato con ID: " + prodottoId);
                        return new RuntimeException("Prodotto non trovato: " + prodottoId);
                    });

            System.out.println("Prodotto trovato: " + prodotto.getNome());

            // Aggiorna le quantità disponibili del prodotto
            if (prodotto.getQuantitaDisponibile() < dettaglio.getQuantita()) {
                throw new RuntimeException("Quantità insufficiente per il prodotto: " + prodotto.getNome());
            }
            prodotto.setQuantitaDisponibile(prodotto.getQuantitaDisponibile() - dettaglio.getQuantita());
            prodottoRepository.save(prodotto);

            // Calcola il prezzo totale per il dettaglio
            dettaglio.calcolaPrezzoTotale(prodotto.getPrezzo());

            // Associa l'ordine al dettaglio
            dettaglio.setOrdine(savedOrdine); // Associa l'ordine salvato ai dettagli
            dettaglioOrdineRepository.save(dettaglio);

            totaleOrdine = totaleOrdine.add(dettaglio.getPrezzoTotale());
        }

        savedOrdine.setTotalePrezzo(totaleOrdine);
        ordineRepository.save(savedOrdine);  // Riferimento all'ordine aggiornato con il totale

        return savedOrdine;
    }

    public Ordine updateOrderStatus(Long ordineId, StatoOrdine stato) {
        // Recupera l'ordine esistente
        Ordine ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato con ID: " + ordineId));

        // Log dello stato attuale dell'ordine prima di fare modifiche
        System.out.println("Stato attuale dell'ordine ID: " + ordineId + " è: " + ordine.getStato());

        // Controlla se lo stato è già quello desiderato
        if (ordine.getStato() == stato) {
            // Aggiunto un log prima di lanciare l'eccezione
            System.out.println("Lo stato dell'ordine è già impostato a: " + stato);
            throw new RuntimeException("Lo stato dell'ordine è già impostato a: " + stato);
        }

        // Verifica se lo stato è annullato e non consente aggiornamenti
        if (ordine.getStato() == StatoOrdine.ANNULLATO) {
            throw new RuntimeException("Impossibile aggiornare uno stato per un ordine annullato.");
        }

        // Imposta il nuovo stato e salva
        ordine.setStato(stato);
        System.out.println("Stato aggiornato per ordine ID: " + ordineId + " a " + stato);

        // Salvataggio dell'ordine e ritorno dell'oggetto aggiornato
        return ordineRepository.save(ordine);
    }

    public List<Ordine> findByStato(StatoOrdine stato) {
        return ordineRepository.findByStato(stato);
    }

    public List<Ordine> findByUtenteId(Long utenteId) {
        return ordineRepository.findByUtenteId(utenteId);
    }

    public Ordine getOrdineById(Long ordineId) {
        return ordineRepository.findById(ordineId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato con ID: " + ordineId));
    }

    // Metodo per ottenere tutti gli ordini
    public List<Ordine> getAllOrdini() {
        return ordineRepository.findAll();
    }

    public void annullaOrdine(Long ordineId) {
        Ordine ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato con ID: " + ordineId));
        ordine.setStato(StatoOrdine.ANNULLATO);
        ordineRepository.save(ordine);
    }
}
