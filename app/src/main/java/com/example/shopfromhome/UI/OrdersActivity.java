package com.example.shopfromhome.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopfromhome.R;
import com.example.shopfromhome.adapter.OrdersAdapter;
import com.example.shopfromhome.models.Ordine;
import com.example.shopfromhome.network.ApiInterface;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.Utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Ordine> ordiniList;
    private ApiInterface apiInterface;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordini);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sessionManager = new SessionManager(this);
        apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);

        fetchOrders();
    }

    private void fetchOrders() {
        Long userId = sessionManager.getUserId();
        String token = sessionManager.getToken();

        apiInterface.getOrdiniByUtente(userId).enqueue(new Callback<List<Ordine>>() {
            @Override
            public void onResponse(Call<List<Ordine>> call, Response<List<Ordine>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("OrdersActivity", "risposta server : " + response);
                    Log.d("OrdersActivity", "risposta server : " + response.body());
                    ordiniList = response.body();
                    for (Ordine ordine : ordiniList) {
                        Log.d("OrdersActivity", "Ordine id: " + ordine.getId());
                    }
                    ordersAdapter = new OrdersAdapter(OrdersActivity.this, ordiniList);
                    recyclerView.setAdapter(ordersAdapter);

                } else {
                    Toast.makeText(OrdersActivity.this, "Errore nel recupero ordini", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ordine>> call, Throwable t) {
                Toast.makeText(OrdersActivity.this, "Errore di connessione", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
