package com.example.shopfromhome.gestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopfromhome.R;
import com.example.shopfromhome.Utils.SessionManager;

public class HomepageGestoreActivity extends AppCompatActivity {

    private Button btnMagazzino, btnOrdini, btnCategorie; // Aggiunto il pulsante per le categorie
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_gestore);

        sessionManager = new SessionManager(this);

        // Controllo se l'utente ha il ruolo di gestore
        if (!isGestore()) {
            Toast.makeText(this, "Accesso non autorizzato", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Imposta il layout e i pulsanti
        btnMagazzino = findViewById(R.id.btnMagazzino);
        btnOrdini = findViewById(R.id.btnOrdini);
        btnCategorie = findViewById(R.id.btnCategorie); // Inizializza il pulsante per le categorie

        btnMagazzino.setOnClickListener(v -> openMagazzino());
        btnOrdini.setOnClickListener(v -> openOrdini());
        btnCategorie.setOnClickListener(v -> openCategorie()); // Aggiungi il listener per il pulsante delle categorie
    }

    private boolean isGestore() {
        String role = sessionManager.getRole();
        Log.d("HomepageGestore", "Ruolo utente: " + role);
        return "MAGAZZINIERE".equalsIgnoreCase(role);
    }

    private void openMagazzino() {
        Intent intent = new Intent(HomepageGestoreActivity.this, MagazzinoActivity.class);
        startActivity(intent);
    }

    private void openOrdini() {
        Intent intent = new Intent(HomepageGestoreActivity.this, OrdiniGestoreActivity.class);
        startActivity(intent);
    }

    private void openCategorie() {
        Intent intent = new Intent(HomepageGestoreActivity.this, CategorieActivity.class); // CategorieActivity deve essere implementata
        startActivity(intent);
    }
}