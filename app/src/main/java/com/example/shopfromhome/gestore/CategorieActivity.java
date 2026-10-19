package com.example.shopfromhome.gestore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopfromhome.R;
import com.example.shopfromhome.adapter.CategoriaAdapter;
import com.example.shopfromhome.models.Categoria;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategorieActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCategorie;
    private CategoriaAdapter categoriaAdapter;
    private EditText editTextCategoria;
    private Button buttonAggiungiCategoria;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie);

        recyclerViewCategorie = findViewById(R.id.recyclerViewCategorie);
        editTextCategoria = findViewById(R.id.editTextCategoria);
        buttonAggiungiCategoria = findViewById(R.id.buttonAggiungiCategoria);

        recyclerViewCategorie.setLayoutManager(new LinearLayoutManager(this));
        apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);

        // Carica le categorie esistenti
        loadCategories();

        // Aggiungi un listener per il pulsante di aggiunta
        buttonAggiungiCategoria.setOnClickListener(v -> addCategory());
    }

    private void loadCategories() {
        Call<List<Categoria>> call = apiInterface.getAllCategories();
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Categoria> categorie = response.body();
                    categoriaAdapter = new CategoriaAdapter(categorie, CategorieActivity.this, CategorieActivity.this::deleteCategory);
                    recyclerViewCategorie.setAdapter(categoriaAdapter);
                } else {
                    Toast.makeText(CategorieActivity.this, "Errore nel caricamento delle categorie", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(CategorieActivity.this, "Errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCategory() {
        String nomeCategoria = editTextCategoria.getText().toString().trim();
        if (nomeCategoria.isEmpty()) {
            Toast.makeText(this, "Inserisci un nome per la categoria", Toast.LENGTH_SHORT).show();
            return;
        }

        // Controlla se la categoria esiste già
        Call<List<Categoria>> call = apiInterface.getAllCategories();
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Categoria> categorie = response.body();
                    for (Categoria categoria : categorie) {
                        if (categoria.getNome().equalsIgnoreCase(nomeCategoria)) {
                            Toast.makeText(CategorieActivity.this, "Categoria già esistente", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    // Se la categoria non esiste, procedi con la creazione
                    Categoria nuovaCategoria = new Categoria();
                    nuovaCategoria.setNome(nomeCategoria);
                    apiInterface.createCategory(nuovaCategoria).enqueue(new Callback<Categoria>() {
                        @Override
                        public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(CategorieActivity.this, "Categoria aggiunta con successo", Toast.LENGTH_SHORT).show();
                                editTextCategoria.setText(""); // Pulisci il campo di input
                                loadCategories(); // Ricarica le categorie
                            } else {
                                Toast.makeText(CategorieActivity.this, "Errore nell'aggiunta della categoria", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Categoria> call, Throwable t) {
                            Toast.makeText(CategorieActivity.this, "Errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(CategorieActivity.this, "Errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCategory(Categoria categoria) {
        apiInterface.deleteCategory(categoria.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CategorieActivity.this, "Categoria eliminata con successo", Toast.LENGTH_SHORT).show();
                    loadCategories(); // Ricarica le categorie
                } else {
                    Toast.makeText(CategorieActivity.this, "Errore nell'eliminazione della categoria", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CategorieActivity.this, "Errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}