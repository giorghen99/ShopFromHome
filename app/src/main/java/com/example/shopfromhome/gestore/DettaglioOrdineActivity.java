package com.example.shopfromhome.gestore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopfromhome.R;
import com.example.shopfromhome.adapter.DettagliOrdiniAdapterGestore;
import com.example.shopfromhome.models.DettaglioOrdine;
import com.example.shopfromhome.models.Ordine;
import com.example.shopfromhome.models.Prodotto;
import com.example.shopfromhome.models.StatoOrdine;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DettaglioOrdineActivity extends AppCompatActivity {

    private static final String TAG = "DettaglioOrdineActivity";
    private TextView textViewNomeUtente, textViewCognomeUtente, textViewIdOrdine;
    private RecyclerView recyclerViewProdotti;
    private DettagliOrdiniAdapterGestore dettaglioOrdineAdapter;
    private ProgressBar progressBar;
    private Spinner spinnerStatoOrdine;
    private Button btnAggiornaStato;
    private Long ordineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_ordine_gestore);

        // Inizializzazione componenti UI
        textViewNomeUtente = findViewById(R.id.textViewNomeUtente);
        textViewCognomeUtente = findViewById(R.id.textViewCognomeUtente);
        textViewIdOrdine = findViewById(R.id.textViewIdOrdine);
        recyclerViewProdotti = findViewById(R.id.recyclerViewProdotti);
        progressBar = findViewById(R.id.progressBar);
        spinnerStatoOrdine = findViewById(R.id.spinnerStatoOrdine);
        btnAggiornaStato = findViewById(R.id.btnAggiornaStato);

        recyclerViewProdotti.setLayoutManager(new LinearLayoutManager(this));
        dettaglioOrdineAdapter = new DettagliOrdiniAdapterGestore(this, new ArrayList<>());
        recyclerViewProdotti.setAdapter(dettaglioOrdineAdapter);

        ordineId = getIntent().getLongExtra("ordineId", -1);
        Log.d(TAG, "ID Ordine ricevuto: " + ordineId);
        if (ordineId != -1) {
            caricaDettagliOrdine(ordineId);
        } else {
            Toast.makeText(this, "Errore: ID ordine non valido", Toast.LENGTH_SHORT).show();
        }

        // Popola il Spinner con gli stati disponibili
        ArrayAdapter<StatoOrdine> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, StatoOrdine.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatoOrdine.setAdapter(adapter);

        btnAggiornaStato.setOnClickListener(v -> aggiornaStatoOrdine());
    }

    private void caricaDettagliOrdine(Long ordineId) {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        apiInterface.getOrdineById(ordineId).enqueue(new Callback<Ordine>() {
            @Override
            public void onResponse(Call<Ordine> call, Response<Ordine> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Ordine ordine = response.body();
                    textViewNomeUtente.setText(ordine.getUtente().getNome());
                    textViewCognomeUtente.setText(ordine.getUtente().getCognome());
                    textViewIdOrdine.setText(String.format("Ordine ID: %s", ordine.getId()));

                    List<DettaglioOrdine> dettagliOrdine = ordine.getDettagliOrdine();
                    dettaglioOrdineAdapter.clear();
                    for (DettaglioOrdine dettaglio : dettagliOrdine) {
                        apiInterface.getProductDetails(dettaglio.getProdottoId()).enqueue(new Callback<Prodotto>() {
                            @Override
                            public void onResponse(Call<Prodotto> call, Response<Prodotto> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Prodotto prodotto = response.body();
                                    dettaglioOrdineAdapter.addProdottoToDetail(dettaglio, prodotto);
                                    dettaglioOrdineAdapter.notifyItemInserted(dettaglioOrdineAdapter.getItemCount() - 1);
                                }
                            }

                            @Override
                            public void onFailure(Call<Prodotto> call, Throwable t) {
                                Log.e(TAG, "Errore nel caricamento del prodotto", t);
                            }
                        });
                    }
                } else {
                    Toast.makeText(DettaglioOrdineActivity.this, "Errore nel caricamento dell'ordine.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Ordine> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DettaglioOrdineActivity.this, "Errore di rete.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void aggiornaStatoOrdine() {
        // Assicurati che lo stato selezionato sia uno dei valori dell'enum
        StatoOrdine nuovoStato = (StatoOrdine) spinnerStatoOrdine.getSelectedItem();
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        apiInterface.updateOrderStatus(ordineId, nuovoStato.name()).enqueue(new Callback<Ordine>() {
            @Override
            public void onResponse(Call<Ordine> call, Response<Ordine> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(DettaglioOrdineActivity.this, "Stato aggiornato con successo!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DettaglioOrdineActivity.this, "Errore nell'aggiornamento dello stato.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Ordine> call, Throwable t) {
                Toast.makeText(DettaglioOrdineActivity.this, "Errore di rete.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
