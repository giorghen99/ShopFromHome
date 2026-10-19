package com.example.backend_shopfromhome.Repository;

import com.example.backend_shopfromhome.Model.Ruolo;
import com.example.backend_shopfromhome.Model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    List<Utente> findByRuolo(Ruolo ruolo); // Ricerca per ruolo
    Optional<Utente> findByEmail(String email);
}
