package com.example.shopfromhome.network;

import com.example.shopfromhome.models.Carrello;
import com.example.shopfromhome.models.Categoria;
import com.example.shopfromhome.models.DettagliCarrello;
import com.example.shopfromhome.models.DettaglioOrdine;
import com.example.shopfromhome.models.Ordine;
import com.example.shopfromhome.models.Prodotto;
import com.example.shopfromhome.models.Utente;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    // User registration API
    @POST("utenti/register")
    Call<Utente> registerUser (@Body Utente utente);

    // User login API
    @POST("utenti/login")
    Call<String> loginUser (@Body Utente utente);

    // Get user by email API
    @GET("utenti/{email}")
    Call<Utente> getUserByEmail(@Path("email") String email);

    // Get all products API
    @GET("prodotti")
    Call<List<Prodotto>> getAllProducts();

    @GET("prodotti/{id}")
    Call<Prodotto> getProductDetails(@Path("id") Long productId);

    // Get products by category API
    @GET("prodotti/categoria/{categoriaId}")
    Call<List<Prodotto>> getProductsByCategory(@Path("categoriaId") Long categoriaId);

    // Create product API
    @POST("prodotti")
    Call<Prodotto> createProduct(@Body Prodotto prodotto);

    // Update product API
    @PUT("prodotti/{id}")
    Call<Prodotto> updateProduct(@Path("id") Long id, @Body Prodotto prodotto);

    // Delete product API
    @DELETE("prodotti/delete/{id}")
    Call<Void> deleteProduct(@Path("id") Long id);

    // Get all categories API
    @GET("categorie")
    Call<List<Categoria>> getAllCategories();

    // Create category API
    @POST("categorie")
    Call<Categoria> createCategory(@Body Categoria categoria);

    // Delete category API
    @DELETE("categorie/{id}")
    Call<Void> deleteCategory(@Path("id") Long id);

    // Get products by search query API
    @GET("prodotti/ricerca")
    Call<List<Prodotto>> getProductsBySearch(@Query("query") String query);

    // Get product details by ID API
    @GET("prodotti/{id}")
    Call<Prodotto> getProductById(@Path("id") Long id);

    @POST("carrelli/{idCarrello}/aggiungi")
    Call<DettagliCarrello> aggiungiProdottoAlCarrello(
            @Path("idCarrello") Long carrelloId,
            @Body DettagliCarrello dettagliCarrello
    );

    @GET("carrelli/utente/{utenteId}")
    Call<List<Carrello>> getCarrelliByUtente(@Path("utenteId") Long utenteId, @Header("Authorization") String token);

    // Rimuovi prodotto dal carrello
    @DELETE("carrelli/{idCarrello}/rimuovi/{idProdotto}")
    Call<Void> rimuoviProdottoDalCarrello(
            @Path("idCarrello") Long carrelloId,
            @Path("idProdotto") Long prodottoId
    );

    // Aggiorna quantità prodotto nel carrello
    @PUT("carrelli/{idCarrello}/aggiorna/{idProdotto}")
    Call<Void> aggiornaQuantitaProdotto(
            @Path("idCarrello") Long idCarrello,
            @Path("idProdotto") Long idProdotto,
            @Query("nuovaQuantita") BigDecimal nuovaQuantita
    );

    // Ottieni dettagli del carrello
    @GET("carrelli/{idCarrello}/dettagli")
    Call<List<DettagliCarrello>> getDettagliCarrelloByCarrelloId(@Path("idCarrello") Long carrelloId);

    // Ordini
    @POST("ordini")
    Call<Ordine> createOrder(@Body Ordine ordine);

    // Ottieni tutti gli ordini di un utente
    @GET("ordini/utente/{utenteId}")
    Call<List<Ordine>> getOrdiniByUtente(@Path("utenteId") Long utenteId);

    // Ottieni dettagli di un ordine
    @GET("dettagli_ordine/{id}")
    Call<DettaglioOrdine> getDettaglioOrdineById(@Path("id") Long id);

    @GET("ordini")
    Call<List<Ordine>> getAllOrdini();

    @GET("ordini/stato/{stato}")
    Call<List<Ordine>> getOrdiniByStato(@Path("stato") String stato);

    @GET("ordini/{ordineId}")
    Call<Ordine> getOrdineById(@Path("ordineId") Long ordineId);

    @PUT("ordini/{ordineId}/annulla")
    Call<Void> annullaOrdine(@Path("ordineId") Long ordineId);

    @PATCH("ordini/{ordineId}/stato")
    Call<Ordine> updateOrderStatus(@Path("ordineId") Long ordineId, @Body String stato);

}