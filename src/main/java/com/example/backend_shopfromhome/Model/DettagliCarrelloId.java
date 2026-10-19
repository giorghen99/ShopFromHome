package com.example.backend_shopfromhome.Model;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;

@Embeddable
public class DettagliCarrelloId implements Serializable {

    private Long idCarrello;
    private Long idProdotto;

    // Costruttore senza argomenti
    public DettagliCarrelloId() {
    }

    // Costruttore con argomenti
    public DettagliCarrelloId(Long idCarrello, Long idProdotto) {
        this.idCarrello = idCarrello;
        this.idProdotto = idProdotto;
    }

    // Getters and Setters
    public Long getIdCarrello() {
        return idCarrello;
    }

    public void setIdCarrello(Long idCarrello) {
        this.idCarrello = idCarrello;
    }

    public Long getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(Long idProdotto) {
        this.idProdotto = idProdotto;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DettagliCarrelloId that = (DettagliCarrelloId) o;
        return Objects.equals(idCarrello, that.idCarrello) &&
                Objects.equals(idProdotto, that.idProdotto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCarrello, idProdotto);
    }
}