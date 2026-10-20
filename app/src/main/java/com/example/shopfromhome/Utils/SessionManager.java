package com.example.shopfromhome.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.shopfromhome.models.Ruolo;
import com.google.gson.Gson;
import com.example.shopfromhome.models.Utente;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "ShopFromHomePrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_CART_ID = "cart_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER = "user"; // Chiave per l'oggetto Utente

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

public void saveSession(String email, String role, String token, Long cartId, Long userId, Utente utente) {
    editor.putString(KEY_EMAIL, email);
    editor.putString(KEY_ROLE, role);
    editor.putString(KEY_TOKEN, token);

    if (cartId != null) {
        editor.putLong(KEY_CART_ID, cartId);
    } else {
        editor.remove(KEY_CART_ID);
    }

    if (userId != null) {
        editor.putLong(KEY_USER_ID, userId);
    } else {
        editor.remove(KEY_USER_ID);
    }

    if (utente != null) {
        Gson gson = new Gson();
        editor.putString(KEY_USER, gson.toJson(utente));
    } else {
        editor.remove(KEY_USER);
    }

    editor.apply();

    Log.d("SessionManager", "Sessione salvata correttamente.");
}

    public Utente getUtente() {
        String json = sharedPreferences.getString(KEY_USER, null);
        Gson gson = new Gson();
        return gson.fromJson(json, Utente.class);
    }

    public Long getUserId() {
        Long userId = sharedPreferences.contains(KEY_USER_ID) ? sharedPreferences.getLong(KEY_USER_ID, -1) : null;
        Log.d("SessionManager", "Recuperato User ID dalla sessione: " + userId);
        return userId;
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public String getRole() {
        return sharedPreferences.getString(KEY_ROLE, null);
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public Long getCartId() {
        Long cartId = sharedPreferences.contains(KEY_CART_ID) ? sharedPreferences.getLong(KEY_CART_ID, -1) : null;
        Log.d("SessionManager", "Recuperato Cart ID dalla sessione: " + cartId);
        return cartId;
    }

    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_EMAIL);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}