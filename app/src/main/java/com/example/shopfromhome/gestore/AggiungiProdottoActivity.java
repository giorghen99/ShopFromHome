package com.example.shopfromhome.gestore;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopfromhome.R;
import com.example.shopfromhome.models.Categoria;
import com.example.shopfromhome.models.Prodotto;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.network.ApiInterface;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AggiungiProdottoActivity extends AppCompatActivity {

    private EditText editTextNome, editTextDescrizione, editTextPrezzo, editTextQuantita, editTextFoto;
    private Button buttonAggiungi;
    private Spinner spinnerCategorie;
    private ApiInterface apiInterface;
    private List<Categoria> categorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_prodotto);

        editTextNome = findViewById(R.id.editTextNome);
        editTextDescrizione = findViewById(R.id.editTextDescrizione);
        editTextPrezzo = findViewById(R.id.editTextPrezzo);
        editTextQuantita = findViewById(R.id.editTextQuantita);
        editTextFoto = findViewById(R.id.editTextFoto);
        buttonAggiungi = findViewById(R.id.buttonAggiungi);
        spinnerCategorie = findViewById(R.id.spinnerCategorie);

        apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);

        // Carica le categorie esistenti
        loadCategories();

        buttonAggiungi.setOnClickListener(v -> addProduct());
    }

    private void loadCategories() {
        apiInterface.getAllCategories().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categorie = response.body();
                    ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(AggiungiProdottoActivity.this,
                            android.R.layout.simple_spinner_item, categorie);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategorie.setAdapter(adapter);
                } else {
                    Toast.makeText(AggiungiProdottoActivity.this, "Errore nel caricamento delle categorie", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(AggiungiProdottoActivity.this, "Errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProduct() {
        String nome = editTextNome.getText().toString().trim();
        String descrizione = editTextDescrizione.getText().toString().trim();
        String prezzoStr = editTextPrezzo.getText().toString().trim();
        String quantitaStr = editTextQuantita.getText().toString().trim();
        String foto = editTextFoto.getText().toString().trim();

        if (nome.isEmpty() || descrizione.isEmpty() || prezzoStr.isEmpty() || quantitaStr.isEmpty() || foto.isEmpty()) {
            Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_SHORT).show();
            return;
        }

        double prezzo = Double.parseDouble(prezzoStr);
        int quantita = Integer.parseInt(quantitaStr);
        Prodotto nuovoProdotto = new Prodotto();
        nuovoProdotto.setNome(nome);
        nuovoProdotto.setDescrizione(descrizione);
        nuovoProdotto.setPrezzo(BigDecimal.valueOf(prezzo));
        nuovoProdotto.setQuantitaDisponibile(quantita);
        nuovoProdotto.setFoto(foto); // Aggiunto il campo foto

        // Associa il prodotto a una categoria selezionata
        Categoria categoriaSelezionata = (Categoria) spinnerCategorie.getSelectedItem();
        nuovoProdotto.setCategoria(categoriaSelezionata);

        apiInterface.createProduct(nuovoProdotto).enqueue(new Callback<Prodotto>() {
            @Override
            public void onResponse(Call<Prodotto> call, Response<Prodotto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AggiungiProdottoActivity.this, "Prodotto aggiunto con successo", Toast.LENGTH_SHORT).show();
                    finish(); // Torna alla MagazzinoActivity
                } else {
                    Toast.makeText(AggiungiProdottoActivity.this, "Errore nell'aggiunta del prodotto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Prodotto> call, Throwable t) {
                Toast.makeText(AggiungiProdottoActivity.this, "Errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}