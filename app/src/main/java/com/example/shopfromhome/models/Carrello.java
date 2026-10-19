package com.example.shopfromhome.models;

import java.util.ArrayList;
import java.util.List;

public class Carrello {
    private Long idCarrello;
    private List<DettagliCarrello> dettagliCarrello;

    // Costruttore senza argomenti
    public Carrello() {
        this.dettagliCarrello = new ArrayList<>(); // Inizializza la lista
    }

    public Carrello(Long carrelloId, List<DettagliCarrello> dettagliCarrello) {
        this.idCarrello = carrelloId;
        this.dettagliCarrello = dettagliCarrello;
    }

    // Getter e Setter
    public Long getCarrelloId() {
        return idCarrello;
    }

    public void setCarrelloId(Long carrelloId) {
        this.idCarrello = carrelloId;
    }

    public List<DettagliCarrello> getDettagliCarrello() {
        return dettagliCarrello;
    }

    public void setDettagliCarrello(List<DettagliCarrello> dettagliCarrello) {
        this.dettagliCarrello = dettagliCarrello;
    }
}