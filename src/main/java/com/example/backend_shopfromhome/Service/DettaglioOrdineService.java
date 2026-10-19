    package com.example.backend_shopfromhome.Service;

    import com.example.backend_shopfromhome.Model.DettaglioOrdine;
    import com.example.backend_shopfromhome.Repository.DettaglioOrdineRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class DettaglioOrdineService {

        @Autowired
        private DettaglioOrdineRepository dettaglioOrdineRepository;

        // Metodo per ottenere tutti i dettagli ordine
        public List<DettaglioOrdine> getAllDettagliOrdine() {
            return dettaglioOrdineRepository.findAll();
        }

        // Metodo per ottenere un dettaglio ordine per ID
        public Optional<DettaglioOrdine> getDettaglioOrdineById(Long id) {
            return dettaglioOrdineRepository.findById(id);
        }

        // Metodo per salvare un nuovo dettaglio ordine
        public DettaglioOrdine saveDettaglioOrdine(DettaglioOrdine dettaglioOrdine) {
            return dettaglioOrdineRepository.save(dettaglioOrdine);
        }

        // Metodo per eliminare un dettaglio ordine
        public void deleteDettaglioOrdine(Long id) {
            dettaglioOrdineRepository.deleteById(id);
        }
    }
