package com.example.backend_shopfromhome.Service;

import com.example.backend_shopfromhome.Model.Ruolo;
import com.example.backend_shopfromhome.Model.Utente;
import com.example.backend_shopfromhome.Repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.backend_shopfromhome.Model.Carrello;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CarrelloService carrelloService;

    @Autowired
public UtenteService(
        UtenteRepository utenteRepository,
        BCryptPasswordEncoder passwordEncoder,
        CarrelloService carrelloService
) {
    this.utenteRepository = utenteRepository;
    this.passwordEncoder = passwordEncoder;
    this.carrelloService = carrelloService;
}

    
@Transactional
public Utente createUser(Utente utente) {
utente.setRuolo(Ruolo.USER);
    if (utente.getPassword() == null || utente.getPassword().isEmpty()) {
        throw new IllegalArgumentException("La password è obbligatoria");
    }

    Optional<Utente> existingUser = utenteRepository.findByEmail(utente.getEmail());
    if (existingUser.isPresent()) {
        throw new IllegalArgumentException("L'email è già associata a un altro account.");
    }

    String encryptedPassword = passwordEncoder.encode(utente.getPassword());
    utente.setPassword(encryptedPassword);

    Utente createdUser = utenteRepository.save(utente);

    String nomeCliente = createdUser.getNome() + " " + createdUser.getCognome();

    carrelloService.creaCarrelloPerUtente(createdUser.getId(), nomeCliente);

    return createdUser;
}


    public Optional<Utente> findByEmail(String email) {
        return utenteRepository.findByEmail(email);
    }

    public void deleteUser(Long id) {
        utenteRepository.deleteById(id);
    }

    public List<Utente> findByRuolo(Ruolo ruolo) {
        return utenteRepository.findByRuolo(ruolo);
    }
}
