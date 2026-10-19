package com.example.shopfromhome.gestore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopfromhome.R;
import com.example.shopfromhome.adapter.ProductAdapterGestore;
import com.example.shopfromhome.models.Prodotto;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MagazzinoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapterGestore productAdapter;
    private ApiInterface apiInterface;
    private List<Prodotto> prodottiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazzino);

        recyclerView = findViewById(R.id.recyclerViewProdotti);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);

        loadProducts();

        Button buttonAggiungiProdotto = findViewById(R.id.buttonAggiungiProdotto);
        buttonAggiungiProdotto.setOnClickListener(v -> {
            Intent intent = new Intent(MagazzinoActivity.this, AggiungiProdottoActivity.class);
            startActivity(intent);
        });
    }

    private void loadProducts() {
        Call<List<Prodotto>> call = apiInterface.getAllProducts();
        call.enqueue(new Callback<List<Prodotto>>() {
            @Override
            public void onResponse(Call<List<Prodotto>> call, Response<List<Prodotto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    prodottiList = response.body();
                    productAdapter = new ProductAdapterGestore(MagazzinoActivity.this, prodottiList,
                            product -> {
                                Intent intent = new Intent(MagazzinoActivity.this, ModificaProdottoActivity.class);
                                intent.putExtra("prodottoId", product.getId());
                                startActivity(intent);
                            },
                            (product, position) -> {
                                deleteProduct(product, position);
                            }
                    );
                    recyclerView.setAdapter(productAdapter);
                } else {
                    Toast.makeText(MagazzinoActivity.this, "Errore nel caricamento dei prodotti", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Prodotto>> call, Throwable t) {
                Toast.makeText(MagazzinoActivity.this, "Errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProduct(Prodotto product, int position) {
        Call<Void> call = apiInterface.deleteProduct(product.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    productAdapter.removeItem(position);
                    Toast.makeText(MagazzinoActivity.this, "Prodotto eliminato", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MagazzinoActivity.this, "Errore nell'eliminazione", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MagazzinoActivity.this, "Errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
