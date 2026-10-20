package com.example.shopfromhome.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopfromhome.R;
import com.example.shopfromhome.models.Utente;
import com.example.shopfromhome.models.Carrello;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.network.ApiInterface;
import com.example.shopfromhome.Utils.SessionManager;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> performLogin());
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Inserisci email e password", Toast.LENGTH_SHORT).show();
            Log.d("LoginActivity", "Email o password vuoti");
            return;
        }

        Utente utente = new Utente();
        utente.setEmail(email);
        utente.setPassword(password);

        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        Call<String> call = apiInterface.loginUser(utente);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("LoginActivity", "Codice risposta: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body();
                    String userIdString = response.headers().get("User-Id");
                    String userRole = response.headers().get("User-Role");

                    // Se il ruolo è nullo, estrailo dal token JWT
                    if (userRole == null || userRole.isEmpty()) {
                        userRole = extractRoleFromToken(token);
                    }

                    Log.d("LoginActivity", "User-Role: " + userRole);

                    if (userIdString == null || userIdString.isEmpty()) {
                        Log.e("LoginActivity", "User-Id mancante o vuoto.");
                        Toast.makeText(LoginActivity.this, "Errore: ID utente mancante", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        Long userId = Long.parseLong(userIdString);

                        if ("MAGAZZINIERE".equalsIgnoreCase(userRole)) {
                            saveSession(email, token, null, userId, userRole, utente);
                            Intent intent = new Intent(LoginActivity.this, com.example.shopfromhome.gestore.HomepageGestoreActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            getCartForUser(userId, token, email, userRole, utente);
                        }

                    } catch (NumberFormatException e) {
                        Log.e("LoginActivity", "Errore nel parsing dell'ID utente: " + e.getMessage());
                        Toast.makeText(LoginActivity.this, "Errore: ID utente non valido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("LoginActivity", "Errore nella risposta del server: " + response.message());
                    Toast.makeText(LoginActivity.this, "Errore: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("LoginActivity", "Errore durante il login: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Errore durante il login: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    
private void getCartForUser(
        Long userId,
        String token,
        String email,
        String userRole,
        Utente utente
) {
    ApiInterface apiInterface =
            ApiConfig.getRetrofit().create(ApiInterface.class);

    Call<List<Carrello>> call =
            apiInterface.getCarrelliByUtente(userId, "Bearer " + token);

    call.enqueue(new Callback<List<Carrello>>() {
        @Override
        public void onResponse(
                Call<List<Carrello>> call,
                Response<List<Carrello>> response
        ) {
            Long cartId = null;

            if (response.isSuccessful()
                    && response.body() != null
                    && !response.body().isEmpty()) {
                Carrello carrello = response.body().get(0);
                cartId = carrello.getCarrelloId();

                Log.d("LoginActivity", "Carrello trovato. ID: " + cartId);
            } else {
                Log.w("LoginActivity",
                        "Carrello non trovato o risposta non valida. Codice: "
                                + response.code());
            }

            completaLogin(cartId, email, token, userId, userRole, utente);
        }

        @Override
        public void onFailure(Call<List<Carrello>> call, Throwable t) {
            Log.e("LoginActivity",
                    "Errore durante il recupero del carrello.", t);

            // La sessione viene salvata anche se il carrello
            // non è temporaneamente raggiungibile.
            completaLogin(null, email, token, userId, userRole, utente);
        }
    });
}

private void completaLogin(
        Long cartId,
        String email,
        String token,
        Long userId,
        String userRole,
        Utente utente
) {
    Utente utenteSessione = new Utente();
    utenteSessione.setEmail(email);
    utenteSessione.setId(userId);

    saveSession(email, token, cartId, userId, userRole, utenteSessione);

    Toast.makeText(
            LoginActivity.this,
            "Login riuscito!",
            Toast.LENGTH_SHORT
    ).show();

    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
}

    private void saveSession(String email, String token, Long cartId, Long userId, String userRole, Utente utente) {
        Log.d("SessionManager", "Sessione salvata correttamente.");

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.saveSession(email, userRole, token, cartId, userId, utente);
    }

    private String extractRoleFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return null;

            String payload = new String(Base64.decode(parts[1], Base64.DEFAULT), StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(payload);

            return jsonObject.optString("role", null);
        } catch (Exception e) {
            Log.e("LoginActivity", "Errore nella decodifica del token JWT.");
            return null;
        }
    }
}
