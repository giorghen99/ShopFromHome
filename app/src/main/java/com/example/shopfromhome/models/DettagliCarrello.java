package com.example.shopfromhome.models;

import java.math.BigDecimal;

public class DettagliCarrello {
    private Long carrelloId; // ID del carrello
    private Prodotto prodotto; // Oggetto prodotto
    private int quantita; // Quantità del prodotto

    public DettagliCarrello() {
    }

    public DettagliCarrello(Long carrelloId, Prodotto prodotto, int quantita) {
        this.carrelloId = carrelloId;
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    public Long getCarrelloId() {
        return carrelloId;
    }

    public void setCarrelloId(Long carrelloId) {
        this.carrelloId = carrelloId;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public Long getProdottoId() {
        return prodotto != null ? prodotto.getId() : null; // Restituisce l'ID del prodotto
    }

    public String getNomeProdotto() {
        return prodotto != null ? prodotto.getNome() : null; // Restituisce il nome del prodotto
    }

    public BigDecimal getPrezzoProdotto() {
        return prodotto != null ? prodotto.getPrezzo() : BigDecimal.ZERO; // Restituisce il prezzo del prodotto
    }
}