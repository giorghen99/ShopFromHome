package com.example.shopfromhome.UI;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopfromhome.R;
import com.example.shopfromhome.Utils.SessionManager;
import com.example.shopfromhome.adapter.CarrelloAdapter;
import com.example.shopfromhome.models.DettagliCarrello;
import com.example.shopfromhome.models.DettaglioOrdine;
import com.example.shopfromhome.models.Ordine;
import com.example.shopfromhome.models.Utente;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.network.ApiInterface;
import com.example.shopfromhome.network.CarrelloManager;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarrelloActivity extends AppCompatActivity implements CarrelloAdapter.OnCarrelloUpdateListener {

    private RecyclerView recyclerViewCarrello;
    private TextView totaleTextView;
    private Button checkoutButton;

    private CarrelloManager carrelloManager;
    private SessionManager sessionManager;
    private List<DettagliCarrello> dettagliCarrello;
    private LocalDate dataRitiro;
    private LocalTime orarioRitiro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCarrello = findViewById(R.id.recyclerViewCarrello);
        totaleTextView = findViewById(R.id.totaleTextView);
        checkoutButton = findViewById(R.id.checkoutButton);

        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        carrelloManager = new CarrelloManager(apiInterface);
        sessionManager = new SessionManager(this);

        recyclerViewCarrello.setLayoutManager(new LinearLayoutManager(this));

        loadCarrelloDetails();

        checkoutButton.setOnClickListener(v -> showDatePickerDialog());
    }

    private void loadCarrelloDetails() {
        Long carrelloId = sessionManager.getCartId();
        if (carrelloId == null || carrelloId <= 0) {
            Log.e("CarrelloActivity", "Errore: ID carrello non valido (" + carrelloId + ")");
            Toast.makeText(this, "Errore: carrello non trovato", Toast.LENGTH_SHORT).show();
            return;
        }

        carrelloManager.getDettagliCarrello(carrelloId, new CarrelloManager.CarrelloOperationCallback<List<DettagliCarrello>>() {
            @Override
            public void onSuccess(List<DettagliCarrello> dettagli) {
                dettagliCarrello = dettagli;
                mostraDettagliCarrello(dettagli);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("CarrelloActivity", "Errore nel recupero dei dettagli del carrello: " + errorMessage);
                Toast.makeText(CarrelloActivity.this, "Errore nel recupero dei dettagli del carrello.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostraDettagliCarrello(List<DettagliCarrello> dettagli) {
        CarrelloAdapter carrelloAdapter = new CarrelloAdapter(this, dettagli, carrelloManager);
        recyclerViewCarrello.setAdapter(carrelloAdapter);
        aggiornaTotale(dettagli);
    }

    @Override
    public void onCarrelloUpdated(List<DettagliCarrello> dettagliCarrello) {
        aggiornaTotale(dettagliCarrello);
    }

    private void aggiornaTotale(List<DettagliCarrello> dettagli) {
        BigDecimal totale = BigDecimal.ZERO;
        for (DettagliCarrello dettaglio : dettagli) {
            totale = totale.add(dettaglio.getPrezzoProdotto().multiply(BigDecimal.valueOf(dettaglio.getQuantita())));
        }
        totaleTextView.setText(String.format("Totale: €%.2f", totale));
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            dataRitiro = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay);
            showTimePickerDialog();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            orarioRitiro = LocalTime.of(selectedHour, selectedMinute);
            createOrder();
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void createOrder() {
        if (dataRitiro == null || orarioRitiro == null) {
            Toast.makeText(this, "Seleziona una data e un orario di ritiro", Toast.LENGTH_SHORT).show();
            return;
        }

        String dataRitiroCompleta = dataRitiro.atTime(orarioRitiro)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        Ordine ordine = new Ordine();
        ordine.setDataRitiro(dataRitiroCompleta);

        Utente ordineUtente = new Utente();
        ordineUtente.setId(sessionManager.getUserId());
        ordine.setUtente(ordineUtente);

        ordine.setDettagliOrdine(convertToDettagliOrdine(dettagliCarrello));

        BigDecimal totalePrezzo = ordine.getDettagliOrdine().stream()
                .map(DettaglioOrdine::getPrezzoTotale)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        ordine.setTotalePrezzo(totalePrezzo);

        Log.d("CarrelloActivity", "Creazione ordine: " + new Gson().toJson(ordine));
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        apiInterface.createOrder(ordine).enqueue(new Callback<Ordine>() {
            @Override
            public void onResponse(Call<Ordine> call, Response<Ordine> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CarrelloActivity.this, "Ordine creato con successo!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("CarrelloActivity", "Errore nella creazione dell'ordine: " + response.errorBody());
                    Toast.makeText(CarrelloActivity.this, "Errore nella creazione dell'ordine", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Ordine> call, Throwable t) {
                Log.e("CarrelloActivity", "Errore nella chiamata API: " + t.getMessage());
                Toast.makeText(CarrelloActivity.this, "Errore di rete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<DettaglioOrdine> convertToDettagliOrdine(List<DettagliCarrello> dettagliCarrello) {
        List<DettaglioOrdine> dettagliOrdine = new ArrayList<>();
        for (DettagliCarrello dettaglio : dettagliCarrello) {
            if (dettaglio.getProdottoId() == null || dettaglio.getQuantita() <= 0) {
                throw new IllegalArgumentException("Prodotto o ID prodotto mancante nel dettaglio ordine");
            }
            DettaglioOrdine dettaglioOrdine = new DettaglioOrdine();
            dettaglioOrdine.setId(null); // Lascia null, sarà generato dal backend
            dettaglioOrdine.setProdottoId(dettaglio.getProdottoId());
            dettaglioOrdine.setQuantita(dettaglio.getQuantita());
            dettaglioOrdine.setPrezzoTotale(dettaglio.getPrezzoProdotto().multiply(BigDecimal.valueOf(dettaglio.getQuantita())));
            dettagliOrdine.add(dettaglioOrdine);
        }
        return dettagliOrdine;
    }
}
