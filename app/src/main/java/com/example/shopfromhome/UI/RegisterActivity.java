package com.example.shopfromhome.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopfromhome.R;
import com.example.shopfromhome.models.Ruolo;
import com.example.shopfromhome.models.Utente;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.network.ApiInterface;
import com.example.shopfromhome.network.NetworkUtils;
import com.example.shopfromhome.Utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNome, etCognome, etEmail, etPassword;
    private Button btnRegistrati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inizializza i componenti
        etNome = findViewById(R.id.etNome);
        etCognome = findViewById(R.id.etCognome);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegistrati = findViewById(R.id.btnRegistrati);

        // Aggiungi il listener al pulsante di registrazione
        btnRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();  // Chiamata al metodo per registrare l'utente
            }
        });
    }

    // Metodo per gestire la registrazione dell'utente
    private void performRegistration() {
        String nome = etNome.getText().toString();
        String cognome = etCognome.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        // Log dei dati inviati
        Log.d("RegisterActivity", "Dati inviati: Nome = " + nome + ", Cognome = " + cognome + ", Email = " + email);

        // Verifica se tutti i campi sono compilati
        if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creazione dell'utente con il ruolo USER
        Utente newUser = new Utente();
        newUser.setNome(nome);
        newUser.setCognome(cognome);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRuolo(Ruolo.USER);  // Imposta sempre il ruolo come "USER"

        // Chiamata API per la registrazione
        ApiInterface apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);
        Call<Utente> call = apiInterface.registerUser(newUser);

        // Gestione della risposta API
        NetworkUtils.handleApiCall(call, this, new NetworkUtils.ApiResponseListener<Utente>() {
            @Override
            public void onSuccess(Utente response) {
                if (response == null || response.getToken() == null) {
                    Log.e("RegisterActivity", "La risposta è nulla o il token non è stato ricevuto");
                    Toast.makeText(RegisterActivity.this, "Errore nella risposta del server", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Log di successo e salvataggio sessione
                Log.d("RegisterActivity", "Registrazione riuscita.");

                String authToken = response.getToken();
                Long userId = response.getId(); // Ottieni l'ID dell'utente dalla risposta

                saveSession(response, authToken, userId); // Passa anche l'ID utente al metodo saveSession

                Toast.makeText(RegisterActivity.this, "Registrazione riuscita!", Toast.LENGTH_SHORT).show();

                // Naviga alla homepage dopo la registrazione
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("RegisterActivity", "Errore nella registrazione: " + throwable.getMessage());
                Toast.makeText(RegisterActivity.this, "Errore nella registrazione: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Modifica il metodo per chiamare saveSession con cartId
    private void saveSession(Utente user, String authToken, Long userId) {
        SessionManager sessionManager = new SessionManager(this);
        Long cartId = null; // Puoi impostare un valore predefinito o ottenere un ID carrello se necessario
        sessionManager.saveSession(user.getEmail(), user.getRuolo().name(), authToken, cartId, userId, user);
        Log.d("RegisterActivity", "Session salvata con successo.");
    }
}
