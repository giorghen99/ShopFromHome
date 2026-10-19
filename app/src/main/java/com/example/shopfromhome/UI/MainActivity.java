package com.example.shopfromhome.UI;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shopfromhome.R;
import com.example.shopfromhome.adapter.ProductAdapter;
import com.example.shopfromhome.models.Prodotto;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.network.ApiInterface;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private RecyclerView randomProductsRecyclerView;
    private RecyclerView weeklyOffersRecyclerView;
    private ProductAdapter randomProductsAdapter;
    private ProductAdapter weeklyOffersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setupNavigation(toolbar); // Configura la navigazione

        showWelcomeMessage(); // Mostra il messaggio di benvenuto

        // Configura RecyclerView per prodotti casuali
        randomProductsRecyclerView = findViewById(R.id.recyclerViewRandomProducts);
        randomProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        loadRandomProducts();

        // Configura RecyclerView per offerte della settimana
        weeklyOffersRecyclerView = findViewById(R.id.recyclerViewWeeklyOffers);
        weeklyOffersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        loadWeeklyOffers();
    }

    private void loadRandomProducts() {
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        Call<List<Prodotto>> call = apiInterface.getAllProducts(); // Modifica per ottenere prodotti casuali
        call.enqueue(new Callback<List<Prodotto>>() {
            @Override
            public void onResponse(Call<List<Prodotto>> call, Response<List<Prodotto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    randomProductsAdapter = new ProductAdapter(response.body(), MainActivity.this);
                    randomProductsRecyclerView.setAdapter(randomProductsAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Prodotto>> call, Throwable t) {
                // Gestisci l'errore
            }
        });
    }

    private void loadWeeklyOffers() {
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        Call<List<Prodotto>> call = apiInterface.getAllProducts(); // Modifica per ottenere le offerte della settimana
        call.enqueue(new Callback<List<Prodotto>>() {
            @Override
            public void onResponse(Call<List<Prodotto>> call, Response<List<Prodotto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    weeklyOffersAdapter = new ProductAdapter(response.body(), MainActivity.this);
                    weeklyOffersRecyclerView.setAdapter(weeklyOffersAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Prodotto>> call, Throwable t) {
                // Gestisci l'errore
            }
        });
    }

    private void showWelcomeMessage() {
        TextView welcomeMessage = findViewById(R.id.welcomeMessage);
        if (sessionManager.isLoggedIn()) {
            welcomeMessage.setText(getString(R.string.discover_offers));
        } else {
            welcomeMessage.setText(getString(R.string.register_or_login));
        }
    }
}