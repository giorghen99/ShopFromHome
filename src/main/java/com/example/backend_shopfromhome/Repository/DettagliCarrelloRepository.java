package com.example.backend_shopfromhome.Repository;

import com.example.backend_shopfromhome.Model.DettagliCarrello;
import com.example.backend_shopfromhome.Model.DettagliCarrelloId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DettagliCarrelloRepository extends JpaRepository<DettagliCarrello, DettagliCarrelloId> {
    // Modifica il metodo per utilizzare la chiave composta
    List<DettagliCarrello> findById_IdCarrello(Long idCarrello);
}