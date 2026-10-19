package com.example.backend_shopfromhome.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "dettaglio_ordine")
public class DettaglioOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Dettaglio_Ordine")
    @JsonProperty("id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_Ordine")
    @JsonBackReference
    @JsonIgnoreProperties("dettagliOrdine")  // Aggiungi questa linea per evitare il ciclo di serializzazione
    private Ordine ordine;

    @Column(name = "ID_Prodotto", nullable = false)
    @JsonProperty("prodottoId")
    private Long prodottoId;

    @Column(name = "Quantità", nullable = false)
    @JsonProperty("quantita")
    private int quantita;

    @Column(name = "Prezzo_Totale", nullable = false)
    @JsonProperty("prezzoTotale")
    private BigDecimal prezzoTotale;

    // Getter e Setter per id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter e Setter per ordine
    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }

    // Getter e Setter per prodottoId
    public Long getProdottoId() {
        return prodottoId;
    }

    public void setProdottoId(Long prodottoId) {
        this.prodottoId = prodottoId;
    }

    // Getter e Setter per quantita
    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    // Getter e Setter per prezzoTotale
    public BigDecimal getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(BigDecimal prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    // Metodo per calcolare il prezzo totale
    public void calcolaPrezzoTotale(BigDecimal prezzoProdotto) {
        this.prezzoTotale = prezzoProdotto.multiply(BigDecimal.valueOf(quantita));
    }
}


