package com.example.backend_shopfromhome.Repository;

import com.example.backend_shopfromhome.Model.Carrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, Long> {
    List<Carrello> findByIdUtente(Long idUtente);
}