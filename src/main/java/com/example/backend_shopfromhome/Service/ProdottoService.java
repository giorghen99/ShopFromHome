package com.example.backend_shopfromhome.Service;

import com.example.backend_shopfromhome.Model.Ordine;
import com.example.backend_shopfromhome.Model.Prodotto;
import com.example.backend_shopfromhome.Model.StatoOrdine;
import com.example.backend_shopfromhome.Repository.OrdineRepository;
import com.example.backend_shopfromhome.Repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdottoService {
    private final ProdottoRepository prodottoRepository;
    private final OrdineRepository ordineRepository;  // Aggiungi il repository Ordine

    @Autowired
    public ProdottoService(ProdottoRepository prodottoRepository, OrdineRepository ordineRepository) {
        this.prodottoRepository = prodottoRepository;
        this.ordineRepository = ordineRepository;  // Inietta il repository Ordine
    }

    public List<Prodotto> getAllProducts() {
        return prodottoRepository.findAll();
    }

    public List<Prodotto> getProductsByCategory(Long categoriaId) {
        return prodottoRepository.findByCategoriaId(categoriaId);
    }

    public Prodotto createProduct(Prodotto prodotto) {
        return prodottoRepository.save(prodotto);
    }

    public Prodotto updateProduct(Long id, Prodotto prodotto) {
        return prodottoRepository.findById(id).map(existingProduct -> {
            existingProduct.setNome(prodotto.getNome());
            existingProduct.setDescrizione(prodotto.getDescrizione());
            existingProduct.setPrezzo(prodotto.getPrezzo());
            existingProduct.setCategoria(prodotto.getCategoria());
            return prodottoRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id) {
        // Verifica se il prodotto è presente in ordini con stato "IN_LAVORAZIONE"
        List<Ordine> ordiniInLavorazione = ordineRepository.findByDettagliOrdine_ProdottoIdAndStato(id, StatoOrdine.IN_LAVORAZIONE);
        if (!ordiniInLavorazione.isEmpty()) {
            throw new RuntimeException("Impossibile eliminare il prodotto, presente in ordini in lavorazione.");
        }

        // Se il prodotto è presente in ordini "Ritirato" o "Annullato", elimina il prodotto
        List<Ordine> ordiniRitiratoAnnullato = ordineRepository.findByDettagliOrdine_ProdottoIdAndStatoIn(id, List.of(StatoOrdine.RITIRATO, StatoOrdine.ANNULLATO));
        if (!ordiniRitiratoAnnullato.isEmpty()) {
            // Elimina il prodotto dal repository
            prodottoRepository.deleteById(id);
            return;
        }

        // Altrimenti, se il prodotto non è presente in nessun ordine, eliminalo
        prodottoRepository.deleteById(id);
    }


    // Aggiungi il metodo di ricerca
    public List<Prodotto> searchProducts(String query) {
        return prodottoRepository.findByNomeContainingIgnoreCase(query);
    }

    public Prodotto getProductById(Long id) {
        return prodottoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));
    }
}
