package com.example.backend_shopfromhome.Security;

import com.example.backend_shopfromhome.Model.Utente;

public class JwtResponse {
    private String token;
    private Utente user; // Oggetto utente incluso nella risposta

    // Costruttore che accetta token e utente
    public JwtResponse(String token, Utente user) {
        this.token = token;
        this.user = user;
    }

    // Getter e setter per il token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Getter e setter per l'utente
    public Utente getUser() {
        return user;
    }

    public void setUser(Utente user) {
        this.user = user;
    }
}
