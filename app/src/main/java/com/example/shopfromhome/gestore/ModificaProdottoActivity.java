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

public class ModificaProdottoActivity extends AppCompatActivity {

    private EditText editTextNome, editTextDescrizione, editTextPrezzo, editTextQuantita, editTextFoto;
    private Button buttonModifica;
    private Spinner spinnerCategorie;
    private ApiInterface apiInterface;
    private Long prodottoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_prodotto);

        editTextNome = findViewById(R.id.editTextNome);
        editTextDescrizione = findViewById(R.id.editTextDescrizione);
        editTextPrezzo = findViewById(R.id.editTextPrezzo);
        editTextQuantita = findViewById(R.id.editTextQuantita);
        editTextFoto = findViewById(R.id.editTextFoto);
        buttonModifica = findViewById(R.id.buttonModifica);
        spinnerCategorie = findViewById(R.id.spinnerCategorie);

        apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        prodottoId = getIntent().getLongExtra("prodottoId", -1);

        loadProductDetails();
        loadCategories();

        buttonModifica.setOnClickListener(v -> updateProduct());
    }

    private void loadProductDetails() {
        apiInterface.getProductById(prodottoId).enqueue(new Callback<Prodotto>() {
            @Override
            public void onResponse(Call<Prodotto> call, Response<Prodotto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Prodotto prodotto = response.body();
                    editTextNome.setText(prodotto.getNome());
                    editTextDescrizione.setText(prodotto.getDescrizione());
                    editTextPrezzo.setText(String.valueOf(prodotto.getPrezzo()));
                    editTextQuantita.setText(String.valueOf(prodotto.getQuantitaDisponibile()));
                    editTextFoto.setText(prodotto.getFoto()); // Imposta il campo foto
                    // Imposta la categoria selezionata nel spinner
                } else {
                    Toast.makeText(ModificaProdottoActivity.this, "Errore nel caricamento del prodotto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Prodotto> call, Throwable t) {
                Toast.makeText(ModificaProdottoActivity.this, "Errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategories() {
        apiInterface.getAllCategories ().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Categoria> categorie = response.body();
                    ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(ModificaProdottoActivity.this,
                            android.R.layout.simple_spinner_item, categorie);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategorie.setAdapter(adapter);
                } else {
                    Toast.makeText(ModificaProdottoActivity.this, "Errore nel caricamento delle categorie", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(ModificaProdottoActivity.this, "Errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProduct() {
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
        Prodotto prodottoAggiornato = new Prodotto();
        prodottoAggiornato.setId(prodottoId);
        prodottoAggiornato.setNome(nome);
        prodottoAggiornato.setDescrizione(descrizione);
        prodottoAggiornato.setPrezzo(BigDecimal.valueOf(prezzo));
        prodottoAggiornato.setQuantitaDisponibile(quantita);
        prodottoAggiornato.setFoto(foto); // Aggiunto il campo foto

        // Associa il prodotto a una categoria selezionata
        Categoria categoriaSelezionata = (Categoria) spinnerCategorie.getSelectedItem();
        prodottoAggiornato.setCategoria(categoriaSelezionata);

        apiInterface.updateProduct(prodottoId, prodottoAggiornato).enqueue(new Callback<Prodotto>() {
            @Override
            public void onResponse(Call<Prodotto> call, Response<Prodotto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ModificaProdottoActivity.this, "Prodotto aggiornato con successo", Toast.LENGTH_SHORT).show();
                    finish(); // Torna alla MagazzinoActivity
                } else {
                    Toast.makeText(ModificaProdottoActivity.this, "Errore nell'aggiornamento del prodotto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Prodotto> call, Throwable t) {
                Toast.makeText(ModificaProdottoActivity.this, "Errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}