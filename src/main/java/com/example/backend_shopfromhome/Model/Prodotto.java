package com.example.backend_shopfromhome.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "prodotto")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Prodotto")
    private Long id;

    @Column(name = "Nome", nullable = false)
    private String nome;

    @Column(name = "Descrizione")
    private String descrizione;

    @Column(name = "Prezzo", nullable = false)
    private BigDecimal prezzo;

    @Column(name = "Quantità_Disponibile", nullable = false)
    private int quantitaDisponibile;

    @Column(name = "Foto")
    private String foto;

    @ManyToOne
    @JoinColumn(name = "ID_Categoria")
    private Categoria categoria;

    // Getter e Setter per id
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

    // Getter e Setter per descrizione
    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    // Getter e Setter per prezzo
    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    // Getter e Setter per quantitaDisponibile
    public int getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(int quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    // Getter e Setter per foto
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    // Getter e Setter per categoria
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
