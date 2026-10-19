package com.example.backend_shopfromhome.Controller;

import com.example.backend_shopfromhome.Model.Prodotto;
import com.example.backend_shopfromhome.Service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {
    private final ProdottoService prodottoService;

    @Autowired
    public ProdottoController(ProdottoService prodottoService) {
        this.prodottoService = prodottoService;
    }

    @GetMapping
    public List<Prodotto> getAllProducts() {
        return prodottoService.getAllProducts();
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Prodotto> getProductsByCategory(@PathVariable Long categoriaId) {
        return prodottoService.getProductsByCategory(categoriaId);
    }

    @GetMapping("/ricerca")
    public List<Prodotto> searchProducts(@RequestParam String query) {
        return prodottoService.searchProducts(query);
    }

    @PostMapping
    public Prodotto createProduct(@RequestBody Prodotto prodotto) {
        System.out.println("Ricevuta richiesta per creare prodotto: " + prodotto.getNome()); // Aggiungi questo log
        return prodottoService.createProduct(prodotto);
    }

    @PutMapping("/{id}")
    public Prodotto updateProduct(@PathVariable Long id, @RequestBody Prodotto prodotto) {
        return prodottoService.updateProduct(id, prodotto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            prodottoService.deleteProduct(id);
            return ResponseEntity.ok("Prodotto eliminato con successo.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public Prodotto getProductById(@PathVariable Long id) {
        return prodottoService.getProductById(id);
    }

}
