package com.example.backend_shopfromhome.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordine")
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Ordine")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_Utente")
    private Utente utente;

    @Column(name = "Data_Ritiro", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dataRitiro;

    @Column(name = "Totale_Prezzo", nullable = false)
    private BigDecimal totalePrezzo;

    @Enumerated(EnumType.STRING)
    @Column(name = "Stato", nullable = false)
    private StatoOrdine stato;

    @OneToMany(mappedBy = "ordine")
    @JsonManagedReference
    @JsonIgnoreProperties("ordine")  // Ignoriamo la serializzazione del campo 'ordine' in 'DettaglioOrdine'
    private List<DettaglioOrdine> dettagliOrdine;

    @Column(name = "Data_Ordine")
    private LocalDateTime dataOrdine;

    // Getter e Setter per id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter e Setter per utente
    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    // Getter e Setter per dataRitiro
    public LocalDateTime getDataRitiro() {
        return dataRitiro;
    }

    public void setDataRitiro(LocalDateTime dataRitiro) {
        this.dataRitiro = dataRitiro;
    }

    // Getter e Setter per totalePrezzo
    public BigDecimal getTotalePrezzo() {
        return totalePrezzo;
    }

    public void setTotalePrezzo(BigDecimal totalePrezzo) {
        this.totalePrezzo = totalePrezzo;
    }

    // Getter e Setter per stato
    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    // Getter e Setter per dettagliOrdine
    public List<DettaglioOrdine> getDettagliOrdine() {
        return dettagliOrdine;
    }

    public void setDettagliOrdine(List<DettaglioOrdine> dettagliOrdine) {
        this.dettagliOrdine = dettagliOrdine;
    }

    // Getter e Setter per dataOrdine
    public LocalDateTime getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(LocalDateTime dataOrdine) {
        this.dataOrdine = dataOrdine;
    }
}


