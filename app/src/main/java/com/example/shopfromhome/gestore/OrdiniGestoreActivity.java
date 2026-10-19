package com.example.shopfromhome.gestore;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // Importa Log
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopfromhome.R;
import com.example.shopfromhome.adapter.OrdineAdapter;
import com.example.shopfromhome.models.Ordine;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.network.ApiInterface;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdiniGestoreActivity extends AppCompatActivity {

    private static final String TAG = "OrdiniGestoreActivity"; // Tag per i log
    private EditText editTextCercaOrdini;
    private Button buttonFiltraOrdini;
    private RecyclerView recyclerViewOrdini;
    private OrdineAdapter ordineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordini_gestore);

        // Inizializzazione componenti
        editTextCercaOrdini = findViewById(R.id.editTextCercaOrdini);
        buttonFiltraOrdini = findViewById(R.id.buttonFiltraOrdini);
        recyclerViewOrdini = findViewById(R.id.recyclerViewOrdini);

        // Configura RecyclerView
        recyclerViewOrdini.setLayoutManager(new LinearLayoutManager(this));
        ordineAdapter = new OrdineAdapter(Collections.emptyList(), ordine -> {
            // Logica al click su un ordine
            Log.d(TAG, "Ordine selezionato: " + ordine.getId());
            Toast.makeText(this, "Ordine selezionato: " + ordine.getId(), Toast.LENGTH_SHORT).show();
        });
        recyclerViewOrdini.setAdapter(ordineAdapter);

        // Carica ordini iniziali
        caricaOrdini();

        // Cerca ordine per ID
        editTextCercaOrdini.setOnEditorActionListener((v, actionId, event) -> {
            String idOrdine = editTextCercaOrdini.getText().toString().trim();
            if (!TextUtils.isEmpty(idOrdine)) {
                Log.d(TAG, "Cercando ordine con ID: " + idOrdine);
                cercaOrdinePerId(Long.parseLong(idOrdine));
            } else {
                Toast.makeText(this, "Inserisci un ID valido", Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        // Filtra ordini nelle ultime 24 ore
        buttonFiltraOrdini.setOnClickListener(v -> filtraOrdiniUltime24Ore());
    }

    private void caricaOrdini() {
        Log.d(TAG, "Caricamento ordini...");
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        apiInterface.getAllOrdini().enqueue(new Callback<List<Ordine>>() {
            @Override
            public void onResponse(Call<List<Ordine>> call, Response<List<Ordine>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Risposta JSON: " + new Gson().toJson(response.body()));
                    List<Ordine> ordini = response.body();
                    Log.d(TAG, "Ordini caricati: " + ordini.size());

                    // Stampa i dettagli di ogni ordine
                    for (Ordine ordine : ordini) {
                        Log.d(TAG, "Ordine ID: " + ordine.getId() +
                                ", Utente ID: " + ordine.getUtente().getId() +
                                ", Totale Prezzo: " + ordine.getTotalePrezzo() +
                                ", Stato: " + ordine.getStato() +
                                ", Data Ritiro: " + ordine.getDataRitiro() +
                                ", Data Ordine: " + ordine.getDataOrdine());
                    }

                    ordineAdapter.setOrdini(ordini);
                } else {
                    Log.e(TAG, "Errore nel caricamento degli ordini: " + response.message());
                    Toast.makeText(OrdiniGestoreActivity.this, "Errore nel caricamento degli ordini", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ordine>> call, Throwable t) {
                Log.e(TAG, "Errore di rete", t);
                Toast.makeText(OrdiniGestoreActivity.this, "Errore di rete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cercaOrdinePerId(Long idOrdine) {
        Log.d(TAG, "Cercando ordine per ID: " + idOrdine);
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        apiInterface.getOrdineById(idOrdine).enqueue(new Callback<Ordine>() {
            @Override
            public void onResponse(Call<Ordine> call, Response<Ordine> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Ordine trovato: " + response.body().getId());
                    ordineAdapter.setOrdini(List.of(response.body()));
                } else {
                    Log.e(TAG, "Ordine non trovato: " + response.message());
                    Toast.makeText(OrdiniGestoreActivity.this, "Ordine non trovato", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Ordine> call, Throwable t) {
                Log.e(TAG, "Errore di rete", t);
                Toast.makeText(OrdiniGestoreActivity.this, "Errore di rete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filtraOrdiniUltime24Ore() {
        Log.d(TAG, "Filtraggio ordini nelle ultime 24 ore...");
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        apiInterface.getAllOrdini().enqueue(new Callback<List<Ordine>>() {
            @Override
            public void onResponse(Call<List<Ordine>> call, Response<List<Ordine>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LocalDateTime ieri = LocalDateTime.now().minusHours(24);
                    List<Ordine> ordiniFiltrati = response.body().stream()
                            .filter(ordine -> {
                                String dataOrdine = ordine.getDataOrdine();
                                if (dataOrdine != null && !dataOrdine.isEmpty()) {
                                    try {
                                        LocalDateTime data = LocalDateTime.parse(dataOrdine);
                                        return data.isAfter(ieri);
                                    } catch (Exception e) {
                                        Log.e(TAG, "Errore nel parsing della data: " + e.getMessage());
                                    }
                                }
                                return false;
                            })
                            .collect(Collectors.toList());
                    Log.d(TAG, "Ordini filtrati: " + ordiniFiltrati.size());
                    ordineAdapter.setOrdini(ordiniFiltrati);
                } else {
                    Log.e(TAG, "Errore nel filtraggio: " + response.message());
                    Toast.makeText(OrdiniGestoreActivity.this, "Errore nel filtraggio", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ordine>> call, Throwable t) {
                Log.e(TAG, "Errore di rete", t);
                Toast.makeText(OrdiniGestoreActivity.this, "Errore di rete", Toast.LENGTH_SHORT).show();
            }
        });
    }
}