package com.example.shopfromhome.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopfromhome.R;
import com.example.shopfromhome.adapter.DettaglioOrdineAdapter;
import com.example.shopfromhome.models.DettaglioOrdine;
import com.example.shopfromhome.models.Ordine;
import com.example.shopfromhome.models.Prodotto;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView orderId, orderDate, orderStatus, orderTotal;
    private Button cancelButton;
    private ListView dettagliOrdineList;
    private DettaglioOrdineAdapter dettaglioOrdineAdapter;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Inizializzazione componenti UI
        orderId = findViewById(R.id.textDetailOrderId);
        orderDate = findViewById(R.id.textDetailOrderDate);
        orderStatus = findViewById(R.id.textDetailOrderStatus);
        orderTotal = findViewById(R.id.textDetailOrderTotal);
        cancelButton = findViewById(R.id.buttonCancelOrder);
        dettagliOrdineList = findViewById(R.id.dettagliOrdineList);

        // Configurazione Retrofit
        apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);

        // Recupero ordine dall'intent
        Ordine ordine = (Ordine) getIntent().getSerializableExtra("ordine");

        if (ordine != null) {
            populateOrderDetails(ordine);

            // Inizializza l'adapter per i dettagli dell'ordine
            dettaglioOrdineAdapter = new DettaglioOrdineAdapter(this, ordine.getDettagliOrdine());
            dettagliOrdineList.setAdapter(dettaglioOrdineAdapter);

            // Recupera i dettagli dei prodotti
            fetchProductDetails(ordine);

            // Imposta visibilità e comportamento del bottone Annulla
                if ("IN_LAVORAZIONE".equalsIgnoreCase(ordine.getStato())) {
                cancelButton.setVisibility(View.VISIBLE);
                cancelButton.setOnClickListener(v -> annullaOrdine(ordine));
            } else {
                cancelButton.setVisibility(View.GONE);
            }
        }
    }

    private void populateOrderDetails(Ordine ordine) {
        orderId.setText("Ordine ID: " + ordine.getId());
        orderDate.setText("Data R itiro: " + ordine.getDataRitiro());
        orderStatus.setText("Stato: " + ordine.getStato());
        orderTotal.setText("Totale: €" + ordine.getTotalePrezzo());
    }

    private void fetchProductDetails(Ordine ordine) {
        for (DettaglioOrdine dettaglio : ordine.getDettagliOrdine()) {
            apiInterface.getProductById(dettaglio.getProdottoId()).enqueue(new Callback<Prodotto>() {
                @Override
                public void onResponse(Call<Prodotto> call, Response<Prodotto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Prodotto prodotto = response.body();
                        dettaglioOrdineAdapter.addProdottoToDetail(dettaglio, prodotto);
                    }
                }

                @Override
                public void onFailure(Call<Prodotto> call, Throwable t) {
                    Toast.makeText(OrderDetailActivity.this, "Errore nel recupero del prodotto", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void annullaOrdine(Ordine ordine) {
        apiInterface.annullaOrdine(ordine.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(OrderDetailActivity.this, "Ordine annullato con successo!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Errore durante l'annullamento dell'ordine", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Errore nella connessione", Toast.LENGTH_SHORT).show();
            }
        });
    }
}