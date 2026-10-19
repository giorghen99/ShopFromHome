package com.example.shopfromhome.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Ordine implements Serializable {
    @SerializedName("id")
    private Long id; // Campo ID per identificare univocamente l'ordine

    @SerializedName("dataRitiro")
    private String dataRitiro;

    @SerializedName("dataOrdine")
    private String dataOrdine; // Data in cui l'ordine è stato creato

    @SerializedName("stato")
    private String stato; // Stato dell'ordine (es. "In preparazione", "Completato")

    @SerializedName("dettagliOrdine")
    private List<DettaglioOrdine> dettagliOrdine;

    @SerializedName("totalePrezzo")
    private BigDecimal totalePrezzo;

    @SerializedName("utente")
    private Utente utente;

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDataRitiro() { return dataRitiro; }
    public void setDataRitiro(String dataRitiro) { this.dataRitiro = dataRitiro; }

    public String getDataOrdine() { return dataOrdine; }
    public void setDataOrdine(String dataOrdine) { this.dataOrdine = dataOrdine; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public List<DettaglioOrdine> getDettagliOrdine() { return dettagliOrdine; }
    public void setDettagliOrdine(List<DettaglioOrdine> dettagliOrdine) { this.dettagliOrdine = dettagliOrdine; }

    public BigDecimal getTotalePrezzo() { return totalePrezzo; }
    public void setTotalePrezzo(BigDecimal totalePrezzo) { this.totalePrezzo = totalePrezzo; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }
}
