package com.example.backend_shopfromhome.Service;

import com.example.backend_shopfromhome.Model.Carrello;
import com.example.backend_shopfromhome.Model.DettagliCarrello;
import com.example.backend_shopfromhome.Model.DettagliCarrelloId;
import com.example.backend_shopfromhome.Model.Prodotto;
import com.example.backend_shopfromhome.Repository.CarrelloRepository;
import com.example.backend_shopfromhome.Repository.DettagliCarrelloRepository;
import com.example.backend_shopfromhome.Repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CarrelloService {

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private DettagliCarrelloRepository dettagliCarrelloRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    public DettagliCarrello aggiungiProdottoAlCarrello(Long idCarrello, DettagliCarrello dettagliCarrello) {
        // Recupera il carrello esistente
        Carrello carrello = carrelloRepository.findById(idCarrello).orElse(null);
        if (carrello == null) {
            throw new RuntimeException("Carrello non trovato");
        }

        // Imposta il carrello nel dettagliCarrello
        dettagliCarrello.setCarrello(carrello);

        // Imposta l'ID del dettagliCarrello
        DettagliCarrelloId dettagliId = new DettagliCarrelloId();
        dettagliId.setIdCarrello(idCarrello);
        dettagliId.setIdProdotto(dettagliCarrello.getProdottoId());
        dettagliCarrello.setId(dettagliId);

        // Salva i dettagli del carrello
        return dettagliCarrelloRepository.save(dettagliCarrello);
    }

    public void rimuoviProdottoDalCarrello(Long idCarrello, Long idProdotto) {
        dettagliCarrelloRepository.deleteById(new DettagliCarrelloId(idCarrello, idProdotto));
    }

    public void modificaQuantitaProdotto(Long idCarrello, Long idProdotto, BigDecimal nuovaQuantita) {
        DettagliCarrello dettagli = dettagliCarrelloRepository.findById(new DettagliCarrelloId(idCarrello, idProdotto)).orElse(null);
        if (dettagli != null) {
            dettagli.setQuantita(nuovaQuantita);
            dettagliCarrelloRepository.save(dettagli);
        }
    }

    public List<Carrello> getCarrelliByUtente(Long idUtente) {
        return carrelloRepository.findByIdUtente(idUtente);
    }

    public List<DettagliCarrello> getDettagliCarrelloById(Long idCarrello) {
        return dettagliCarrelloRepository.findById_IdCarrello(idCarrello);
    }
}