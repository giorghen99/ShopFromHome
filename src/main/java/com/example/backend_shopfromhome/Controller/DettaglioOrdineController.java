package com.example.backend_shopfromhome.Controller;

import com.example.backend_shopfromhome.Model.DettaglioOrdine;
import com.example.backend_shopfromhome.Service.DettaglioOrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dettagli_ordine")
public class DettaglioOrdineController {

    @Autowired
    private DettaglioOrdineService dettaglioOrdineService;

    // Endpoint per ottenere tutti i dettagli ordine
    @GetMapping
    public List<DettaglioOrdine> getAllDettagliOrdine() {
        return dettaglioOrdineService.getAllDettagliOrdine();
    }

    // Endpoint per ottenere un dettaglio ordine per ID
    @GetMapping("/{id}")
    public ResponseEntity<DettaglioOrdine> getDettaglioOrdineById(@PathVariable Long id) {
        return dettaglioOrdineService.getDettaglioOrdineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint per creare un nuovo dettaglio ordine
    @PostMapping
    public DettaglioOrdine createDettaglioOrdine(@RequestBody DettaglioOrdine dettaglioOrdine) {
        return dettaglioOrdineService.saveDettaglioOrdine(dettaglioOrdine);
    }

    // Endpoint per eliminare un dettaglio ordine
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDettaglioOrdine(@PathVariable Long id) {
        dettaglioOrdineService.deleteDettaglioOrdine(id);
        return ResponseEntity.noContent().build();
    }
}
