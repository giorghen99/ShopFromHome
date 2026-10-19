package com.example.shopfromhome.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopfromhome.R;
import com.example.shopfromhome.Utils.SessionManager;
import com.example.shopfromhome.adapter.ProductAdapter;
import com.example.shopfromhome.models.Categoria;
import com.example.shopfromhome.models.DettagliCarrello;
import com.example.shopfromhome.models.Prodotto;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.network.ApiInterface;
import com.example.shopfromhome.network.CarrelloManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private SessionManager sessionManager;
    private Spinner categoriaSpinner;
    private List<Categoria> categoriaList;
    private SearchView searchView;
    private BottomNavigationView bottomNavigationView;
    private CarrelloManager carrelloManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        sessionManager = new SessionManager(this);

        // Configura la toolbar e la navigazione
        Toolbar toolbar = findViewById(R.id.toolbar);
        setupNavigation(toolbar); // Configura la navigazione

        // Impostazione del RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Impostazione del Spinner per la selezione della categoria
        categoriaSpinner = findViewById(R.id.spinnerCategorie);
        loadCategories();

        // Inizializza ApiInterface e CarrelloManager
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        carrelloManager = new CarrelloManager(apiInterface);

        // Caricamento dei prodotti senza filtri
        loadProducts();

        // SearchView per ricerca prodotti
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProducts(newText);
                return true;
            }
        });

        // Barra di navigazione inferiore
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(this::handleBottomNavigation);
    }

    private void loadCategories() {
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        Call<List<Categoria>> call = apiInterface.getAllCategories();
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoriaList = response.body();

                    // Aggiungi "Tutte le categorie" come opzione predefinita
                    Categoria allCategoriesOption = new Categoria();
                    allCategoriesOption.setId(0L); // ID speciale per "Tutte le categorie"
                    allCategoriesOption.setNome("Tutte le categorie");
                    categoriaList.add(0, allCategoriesOption);

                    // Imposta l'adapter per lo Spinner
                    ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(ProductListActivity.this,
                            android.R.layout.simple_spinner_item, categoriaList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categoriaSpinner.setAdapter(adapter);

                    // Listener per selezione categoria
                    categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            Categoria selectedCategoria = (Categoria) parentView.getItemAtPosition(position);

                            if (selectedCategoria.getId() == 0L) {
                                // Carica tutti i prodotti
                                loadProducts();
                            } else {
                                // Carica prodotti per la categoria selezionata
                                loadProductsByCategory(selectedCategoria.getId());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // Carica tutti i prodotti se nessuna categoria è selezionata
                            loadProducts();
                        }
                    });
                } else {
                    Toast.makeText(ProductListActivity.this, "Errore nel caricamento delle categorie", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Errore nel caricamento delle categorie: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProducts() {
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        Call<List<Prodotto>> call = apiInterface.getAllProducts();
        call.enqueue(new Callback<List<Prodotto>>() {
            @Override
            public void onResponse(Call<List<Prodotto>> call, Response<List<Prodotto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Prodotto> products = response.body();
                    productAdapter = new ProductAdapter(products, ProductListActivity.this);
                    recyclerView.setAdapter(productAdapter);
                } else {
                    Toast.makeText(ProductListActivity.this, "Errore nel caricamento dei prodotti", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Prodotto>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Errore nel caricamento dei prodotti: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProductsByCategory(Long categoryId) {
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        Call<List<Prodotto>> call = apiInterface.getProductsByCategory(categoryId);
        call.enqueue(new Callback<List<Prodotto>>() {
            @Override
            public void onResponse(Call<List<Prodotto>> call, Response<List<Prodotto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Prodotto> products = response.body();
                    productAdapter = new ProductAdapter(products, ProductListActivity.this);
                    recyclerView.setAdapter(productAdapter);
                } else {
                    Toast.makeText(ProductListActivity.this, "Errore nel caricamento dei prodotti per categoria", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Prodotto>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Errore nel caricamento dei prodotti per categoria: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchProducts(String query) {
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        Call<List<Prodotto>> call = apiInterface.getProductsBySearch(query);
        call.enqueue(new Callback<List<Prodotto>>() {
            @Override
            public void onResponse(Call<List<Prodotto>> call, Response<List<Prodotto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Prodotto> products = response.body();
                    productAdapter = new ProductAdapter(products, ProductListActivity.this);
                    recyclerView.setAdapter(productAdapter);
                } else {
                    Toast.makeText(ProductListActivity.this, "Nessun prodotto trovato", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Prodotto>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Errore nel caricamento dei prodotti: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void aggiungiProdottoAlCarrello(Long prodottoId, int quantita) {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Utente non loggato. Effettua il login per aggiungere prodotti al carrello.", Toast.LENGTH_SHORT).show();
            return;
        }

        Long carrelloId = sessionManager.getCartId();
        if (carrelloId == null || carrelloId == -1L) {
            Toast.makeText(ProductListActivity.this, "Errore: carrello non trovato o non valido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Recupera il prodotto dalla lista
        Prodotto prodotto = null;
        for (Prodotto p : productAdapter.getProductList()) {
            if (p.getId().equals(prodottoId)) {
                prodotto = p;
                break;
            }
        }

        if (prodotto == null) {
            Toast.makeText(ProductListActivity.this, "Errore: prodotto non trovato", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creazione dell'oggetto DettagliCarrello
        DettagliCarrello dettagliCarrello = new DettagliCarrello();
        dettagliCarrello.setProdotto(prodotto); // Imposta l'oggetto prodotto
        dettagliCarrello.setQuantita(quantita);

        carrelloManager.aggiungiProdottoAlCarrello(carrelloId, dettagliCarrello, new CarrelloManager.CarrelloOperationCallback<DettagliCarrello>() {
            @Override
            public void onSuccess(DettagliCarrello response) {
                Toast.makeText(ProductListActivity.this, "Prodotto aggiunto al carrello", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ProductListActivity.this, "Errore nell'aggiunta al carrello: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void handleDrawerNavigation(MenuItem item) {
        super.handleDrawerNavigation(item); // Chiama il metodo della BaseActivity
        // Aggiungi eventuali logiche specifiche per ProductListActivity se necessario
    }

    @Override
    protected boolean handleBottomNavigation(@NonNull MenuItem item) {
        return super.handleBottomNavigation(item); // Chiama il metodo della BaseActivity
    }
}