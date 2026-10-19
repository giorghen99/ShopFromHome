package com.example.shopfromhome.network;

import android.util.Log;

import com.example.shopfromhome.models.DettagliCarrello;
import com.example.shopfromhome.models.Ordine;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarrelloManager {

    private ApiInterface apiInterface;

    public CarrelloManager(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public interface CarrelloOperationCallback<T> {
        void onSuccess(T response);
        void onFailure(String errorMessage);
    }

    // Metodo per aggiungere un prodotto al carrello
    public void aggiungiProdottoAlCarrello(Long carrelloId, DettagliCarrello dettagliCarrello, CarrelloOperationCallback<DettagliCarrello> callback) {
        if (carrelloId == null || carrelloId <= 0) {
            Log.e("CarrelloManager", "Errore: ID carrello non valido");
            callback.onFailure("ID carrello non valido");
            return;
        }

        Log.d("CarrelloManager", "Richiesta JSON: " + new Gson().toJson(dettagliCarrello)); // Log della richiesta

        apiInterface.aggiungiProdottoAlCarrello(carrelloId, dettagliCarrello).enqueue(new Callback<DettagliCarrello>() {
            @Override
            public void onResponse(Call<DettagliCarrello> call, Response<DettagliCarrello> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("CarrelloManager", "Prodotto aggiunto con successo");
                    callback.onSuccess(response.body());
                } else {
                    Log.e("CarrelloManager", "Errore nell'aggiunta: " + response.code() + " - " + response.message());
                    callback.onFailure("Errore nell'aggiunta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DettagliCarrello> call, Throwable t) {
                callback.onFailure("Errore di rete: " + t.getMessage());
            }
        });
    }

    // Metodo per rimuovere un prodotto dal carrello
    public void rimuoviProdottoDalCarrello(Long carrelloId, Long prodottoId, CarrelloOperationCallback<Void> callback) {
        if (carrelloId == null || carrelloId <= 0) {
            Log.e("CarrelloManager", "Errore: ID carrello non valido");
            callback.onFailure("ID carrello non valido");
            return;
        }

        apiInterface.rimuoviProdottoDalCarrello(carrelloId, prodottoId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    Log.e("CarrelloManager", "Errore nella rimozione del prodotto: " + response.message());
                    callback.onFailure("Errore nella rimozione del prodotto.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Metodo per aggiornare la quantità di un prodotto nel carrello
    public void aggiornaQuantitaProdotto(Long carrelloId, Long prodottoId, BigDecimal nuovaQuantita, CarrelloOperationCallback<Void> callback) {
        if (carrelloId == null || carrelloId <= 0) {
            Log.e("CarrelloManager", "Errore: ID carrello non valido");
            callback.onFailure("ID carrello non valido");
            return;
        }

        if (prodottoId == null || nuovaQuantita == null || nuovaQuantita.compareTo(BigDecimal.ZERO) <= 0) {
            Log.e("CarrelloManager", "Errore: parametri non validi");
            callback.onFailure("Parametri non validi per l'aggiornamento");
            return;
        }

        Log.d("CarrelloManager", "Chiamata API per aggiornare quantità: carrelloId=" + carrelloId + ", prodottoId=" + prodottoId + ", nuovaQuantita=" + nuovaQuantita);

        apiInterface.aggiornaQuantitaProdotto(carrelloId, prodottoId, nuovaQuantita).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("CarrelloManager", "Quantità aggiornata con successo");
                    callback.onSuccess(null);
                } else {
                    Log.e("CarrelloManager", "Errore nell'aggiornamento: " + response.code() + " - " + response.message());
                    callback.onFailure("Errore nell'aggiornamento: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CarrelloManager", "Errore di rete: " + t.getMessage());
                callback.onFailure("Errore di rete: " + t.getMessage());
            }
        });
    }// Metodo per ottenere i dettagli del carrello
    public void getDettagliCarrello(Long carrelloId, CarrelloOperationCallback<List<DettagliCarrello>> callback) {
        if (carrelloId == null || carrelloId <= 0) {
            Log.e("CarrelloManager", "Errore: ID carrello non valido (" + carrelloId + ")");
            callback.onFailure("ID carrello non valido");
            return;
        }

        apiInterface.getDettagliCarrelloByCarrelloId(carrelloId).enqueue(new Callback<List<DettagliCarrello>>() {
            @Override
            public void onResponse(Call<List<DettagliCarrello>> call, Response<List<DettagliCarrello>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DettagliCarrello> dettagli = response.body();
                    callback.onSuccess(dettagli);
                } else {
                    callback.onFailure("Errore nel recupero dei dettagli del carrello: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<DettagliCarrello>> call, Throwable t) {
                callback.onFailure("Errore di rete: " + t.getMessage());
            }
        });
    }
}