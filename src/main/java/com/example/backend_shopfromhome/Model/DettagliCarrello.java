package com.example.backend_shopfromhome.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "dettagli_carrello")
public class DettagliCarrello {

    @EmbeddedId
    private DettagliCarrelloId id;

    @ManyToOne
    @MapsId("idCarrello")
    @JoinColumn(name = "id_carrello", referencedColumnName = "id_carrello", nullable = false)
    @JsonBackReference
    private Carrello carrello;

    @ManyToOne
    @MapsId("idProdotto")
    @JoinColumn(name = "id_prodotto", referencedColumnName = "ID_Prodotto", nullable = false)
    private Prodotto prodotto;

    @Column(name = "quantita", nullable = false)
    private BigDecimal quantita;

    // Aggiungi un campo per l'ID del prodotto
    @Transient // Questo campo non verrà salvato nel database
    private Long prodottoId;

    // Getters and Setters
    public DettagliCarrelloId getId() {
        return id;
    }

    public void setId(DettagliCarrelloId id) {
        this.id = id;
    }

    public Carrello getCarrello() {
        return carrello;
    }

    public void setCarrello(Carrello carrello) {
        this.carrello = carrello;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
        if (prodotto != null) {
            this.prodottoId = prodotto.getId(); // Imposta l'ID del prodotto quando il prodotto è impostato
        }
    }

    public BigDecimal getQuantita() {
        return quantita;
    }

    public void setQuantita(BigDecimal quantita) {
        this.quantita = quantita;
    }

    public Long getProdottoId() {
        return prodottoId;
    }

    public void setProdottoId(Long prodottoId) {
        this.prodottoId = prodottoId;
    }
}