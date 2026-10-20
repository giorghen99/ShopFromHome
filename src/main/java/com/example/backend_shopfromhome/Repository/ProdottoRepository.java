package com.example.backend_shopfromhome.Repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import com.example.backend_shopfromhome.Model.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT p FROM Prodotto p WHERE p.id = :id")
Optional<Prodotto> findByIdForUpdate(@Param("id") Long id);
    List<Prodotto> findByCategoriaId(Long categoriaId);
    List<Prodotto> findByNomeContaining(String query);
    List<Prodotto> findByNomeContainingIgnoreCase(String nome);
}

