package com.example.backend_shopfromhome.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"ordini"})
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utente")
    private Long id;

    @NotNull(message = "Il nome è obbligatorio")
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull(message = "Il cognome è obbligatorio")
    @Column(name = "cognome", nullable = false)
    private String cognome;

    @NotNull(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "La password è obbligatoria")
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ruolo", nullable = false)
    private Ruolo ruolo;

    // Modificato da @OneToMany a @OneToOne per avere un solo carrello
    @OneToOne(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Carrello carrello;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ordine> ordini = new ArrayList<>();

    // Metodo per verificare la password
    public boolean checkPassword(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, this.password);
    }

    // Aggiungi e rimuovi ordini
    public void addOrdine(Ordine ordine) {
        ordine.setUtente(this);
        this.ordini.add(ordine);
    }

    public void removeOrdine(Ordine ordine) {
        ordine.setUtente(null);
        this.ordini.remove(ordine);
    }

    // Aggiungi e rimuovi carrello
    public void setCarrello(Carrello carrello) {
        // Se c'era un carrello precedente, lo rimuoviamo
        if (this.carrello != null) {
            this.carrello.setUtente(null);
        }
        this.carrello = carrello;
        if (carrello != null) {
            carrello.setUtente(this);
        }
    }
}
