package com.example.shopfromhome.models;

import java.io.Serializable;

public class Utente implements Serializable {
    private Long id;          // Aggiungi questo campo per l'ID dell'utente
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private Ruolo ruolo;
    private String token;     // Questo campo va aggiornato con il valore del token

    public Utente(){

    }
    public Utente(Long id) {
        this.id = id;
    }

    // Getter e Setter per l'ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter e Setter per nome
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter e Setter per cognome
    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    // Getter e Setter per email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter e Setter per password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter e Setter per ruolo
    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    // Getter e Setter per token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;  // Salvataggio del token
    }

    // Metodo per controllare la password
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}
