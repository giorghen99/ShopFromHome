package com.example.backend_shopfromhome.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "carrello")
public class Carrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrello")
    private Long idCarrello;

    @Column(name = "nome_cliente")
    private String nomeCliente;

    @Column(name = "id_utente")
    private Long idUtente;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "id_utente", referencedColumnName = "id_utente", insertable = false, updatable = false)
    private Utente utente;

    @OneToMany(mappedBy = "carrello", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DettagliCarrello> dettagliCarrello;

    // Getters and Setters
    public Long getIdCarrello() {
        return idCarrello;
    }

    public void setIdCarrello(Long idCarrello) {
        this.idCarrello = idCarrello;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public List<DettagliCarrello> getDettagliCarrello() {
        return dettagliCarrello;
    }

    public void setDettagliCarrello(List<DettagliCarrello> dettagliCarrello) {
        this.dettagliCarrello = dettagliCarrello;
    }
}
