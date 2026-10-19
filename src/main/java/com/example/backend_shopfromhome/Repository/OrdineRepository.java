package com.example.backend_shopfromhome.Repository;

import com.example.backend_shopfromhome.Model.Ordine;
import com.example.backend_shopfromhome.Model.StatoOrdine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    List<Ordine> findByStato(StatoOrdine stato); // Ricerca per stato
    List<Ordine> findByUtenteId(Long utenteId); // Ricerca per utente
    List<Ordine> findByDettagliOrdine_ProdottoIdAndStato(Long prodottoId, StatoOrdine stato); // Ricerca ordini per prodotto e stato
    List<Ordine> findByDettagliOrdine_ProdottoIdAndStatoIn(Long prodottoId, List<StatoOrdine> stati); // Ordini per prodotto e lista di stati
}
