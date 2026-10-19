package com.example.shopfromhome.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class DettaglioOrdine implements Serializable {
    @SerializedName("id")
    private Long id; // Campo ID per identificare univocamente il dettaglio

    @SerializedName("prodottoId")
    private Long prodottoId;

    @SerializedName("quantita")
    private int quantita;

    @SerializedName("prezzoTotale")
    private BigDecimal prezzoTotale;

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProdottoId() { return prodottoId; }
    public void setProdottoId(Long prodottoId) { this.prodottoId = prodottoId; }

    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }

    public BigDecimal getPrezzoTotale() { return prezzoTotale; }
    public void setPrezzoTotale(BigDecimal prezzoTotale) { this.prezzoTotale = prezzoTotale; }
}