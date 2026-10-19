package com.example.backend_shopfromhome.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Categoria")
    private Integer idCategoria;  // Modificato da Long a Integer

    @OneToMany(mappedBy = "categoria")
    private List<Prodotto> prodotti;

    @Column(name = "Nome", nullable = false)
    private String nome;

    @Column(name = "Descrizione")
    private String descrizione;

    // Getter e Setter per id
    public Integer getId() {
        return idCategoria;
    }

    public void setId(Integer id) {
        this.idCategoria = id;
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
}
