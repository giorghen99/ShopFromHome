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

    
public void modificaQuantitaProdotto(
        Long idCarrello,
        Long idProdotto,
        BigDecimal nuovaQuantita
) {
    if (nuovaQuantita == null
            || nuovaQuantita.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("La quantità deve essere maggiore di zero.");
    }

    DettagliCarrelloId id =
            new DettagliCarrelloId(idCarrello, idProdotto);

    DettagliCarrello dettagli = dettagliCarrelloRepository
            .findById(id)
            .orElseThrow(() ->
                    new IllegalArgumentException("Prodotto non presente nel carrello."));

    dettagli.setQuantita(nuovaQuantita);
    dettagliCarrelloRepository.save(dettagli);
}


    public List<Carrello> getCarrelliByUtente(Long idUtente) {
        return carrelloRepository.findByIdUtente(idUtente);
    }

    public List<DettagliCarrello> getDettagliCarrelloById(Long idCarrello) {
        return dettagliCarrelloRepository.findById_IdCarrello(idCarrello);
    }


public Carrello creaCarrelloPerUtente(Long idUtente, String nomeCliente) {
    if (idUtente == null) {
        throw new IllegalArgumentException("L'ID utente è obbligatorio.");
    }

    List<Carrello> carrelli = carrelloRepository.findByIdUtente(idUtente);

    if (carrelli != null && !carrelli.isEmpty()) {
        return carrelli.get(0);
    }

    Carrello carrello = new Carrello();
    carrello.setIdUtente(idUtente);
    carrello.setNomeCliente(nomeCliente);

    return carrelloRepository.save(carrello);
}

}