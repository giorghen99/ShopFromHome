package com.example.backend_shopfromhome.Repository;

import com.example.backend_shopfromhome.Model.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    List<Prodotto> findByCategoriaId(Long categoriaId);
    List<Prodotto> findByNomeContaining(String query);
    List<Prodotto> findByNomeContainingIgnoreCase(String nome);
}

