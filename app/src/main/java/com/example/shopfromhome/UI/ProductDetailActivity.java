package com.example.shopfromhome.UI;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.shopfromhome.R;
import com.example.shopfromhome.models.Prodotto;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imageProduct;
    private TextView textName, textPrice, textDescription, textCategory, textAvailability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Inizializza le viste
        imageProduct = findViewById(R.id.imageProductDetails);
        textName = findViewById(R.id.textProductNameDetails);
        textPrice = findViewById(R.id.textProductPriceDetails);
        textDescription = findViewById(R.id.textProductDescriptionDetails);
        textCategory = findViewById(R.id.textProductCategoryDetails);
        textAvailability = findViewById(R.id.textProductAvailabilityDetails);

        // Ottieni l'ID del prodotto passato tramite Intent
        long productId = getIntent().getLongExtra("productId", -1);
        if (productId != -1) {
            loadProductDetails(productId);
        } else {
            Toast.makeText(this, "Errore: prodotto non trovato", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadProductDetails(long productId) {
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        Call<Prodotto> call = apiInterface.getProductDetails(productId);

        call.enqueue(new Callback<Prodotto>() {
            @Override
            public void onResponse(Call<Prodotto> call, Response<Prodotto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Prodotto prodotto = response.body();

                    textName.setText(prodotto.getNome());
                    textPrice.setText(String.format("€ %.2f", prodotto.getPrezzo()));
                    textDescription.setText(prodotto.getDescrizione());
                    textCategory.setText("Categoria: " + prodotto.getCategoria().getNome());
                    textAvailability.setText(prodotto.isDisponibile() ? "Disponibile" : "Non disponibile");

                    // Carica immagine con Glide
                    Glide.with(ProductDetailActivity.this)
                            .load(prodotto.getFoto())
                            .placeholder(R.drawable.placeholder) // Assicurati che il file esista
                            .error(R.drawable.error) // Aggiungi un'immagine di fallback in caso di errore
                            .into(imageProduct);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Errore nel caricamento del prodotto", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Prodotto> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
