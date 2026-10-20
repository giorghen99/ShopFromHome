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
import org.springframework.transaction.annotation.Transactional;

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
    
@Transactional
public Ordine salvaOrdine(Ordine ordine) {
    if (ordine == null
            || ordine.getUtente() == null
            || ordine.getDettagliOrdine() == null
            || ordine.getDettagliOrdine().isEmpty()) {
        throw new IllegalArgumentException(
                "L'ordine deve avere un utente e almeno un dettaglio."
        );
    }

    // Valida tutti i dettagli prima di modificare il database.
    for (DettaglioOrdine dettaglio : ordine.getDettagliOrdine()) {
        if (dettaglio == null
                || dettaglio.getProdottoId() == null
                || dettaglio.getQuantita() <= 0) {
            throw new IllegalArgumentException(
                    "Prodotto e quantità devono essere validi."
            );
        }

        Prodotto prodotto = prodottoRepository
        .findByIdForUpdate(dettaglio.getProdottoId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Prodotto non trovato."
                ));

        if (prodotto.getQuantitaDisponibile() < dettaglio.getQuantita()) {
            throw new IllegalArgumentException(
                    "Quantità non disponibile per il prodotto "
                            + prodotto.getId()
            );
        }
    }

    ordine.setStato(StatoOrdine.IN_LAVORAZIONE);
    ordine.setTotalePrezzo(BigDecimal.ZERO);

    Ordine ordineSalvato = ordineRepository.save(ordine);
    BigDecimal totaleOrdine = BigDecimal.ZERO;

    for (DettaglioOrdine dettaglio : ordine.getDettagliOrdine()) {
        Prodotto prodotto = prodottoRepository
        .findByIdForUpdate(dettaglio.getProdottoId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Prodotto non trovato."
                ));

        prodotto.setQuantitaDisponibile(
                prodotto.getQuantitaDisponibile() - dettaglio.getQuantita()
        );
        prodottoRepository.save(prodotto);

        dettaglio.setOrdine(ordineSalvato);
        dettaglio.calcolaPrezzoTotale(prodotto.getPrezzo());
        dettaglioOrdineRepository.save(dettaglio);

        totaleOrdine = totaleOrdine.add(dettaglio.getPrezzoTotale());
    }

    ordineSalvato.setTotalePrezzo(totaleOrdine);
    return ordineRepository.save(ordineSalvato);
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

    

@Transactional
public void annullaOrdine(Long ordineId) {
    Ordine ordine = ordineRepository.findById(ordineId)
            .orElseThrow(() ->
                    new RuntimeException("Ordine non trovato con ID: " + ordineId));

    if (ordine.getStato() == StatoOrdine.ANNULLATO) {
        throw new IllegalStateException("L'ordine è già annullato.");
    }

    if (ordine.getStato() == StatoOrdine.RITIRATO) {
        throw new IllegalStateException(
                "Non è possibile annullare un ordine già ritirato."
        );
    }

    List<DettaglioOrdine> dettagli =
            dettaglioOrdineRepository.findByOrdine_Id(ordineId);

    for (DettaglioOrdine dettaglio : dettagli) {
        Prodotto prodotto = prodottoRepository
                .findByIdForUpdate(dettaglio.getProdottoId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Prodotto non trovato: " + dettaglio.getProdottoId()
                        ));

        prodotto.setQuantitaDisponibile(
                prodotto.getQuantitaDisponibile() + dettaglio.getQuantita()
        );

        prodottoRepository.save(prodotto);
    }

    ordine.setStato(StatoOrdine.ANNULLATO);
    ordineRepository.save(ordine);
  }
}
