package com.example.shopfromhome.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopfromhome.R;
import com.example.shopfromhome.network.ApiConfig;
import com.example.shopfromhome.network.ApiInterface;
import com.example.shopfromhome.models.Utente;
import com.example.shopfromhome.Utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {
    private TextView textViewName, textViewEmail, textViewRole;
    private Button btnLogout;
    private SessionManager sessionManager;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewRole = findViewById(R.id.textViewRole);
        btnLogout = findViewById(R.id.btnLogout);

        sessionManager = new SessionManager(getApplicationContext());
        apiInterface = ApiConfig.getRetrofit().create(ApiInterface.class);

        loadUserInfo();

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserInfo() {
        String email = sessionManager.getEmail();
        if (email != null) {
            Call<Utente> call = apiInterface.getUserByEmail(email);
            call.enqueue(new Callback<Utente>() {
                @Override
                public void onResponse(Call<Utente> call, Response<Utente> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Utente utente = response.body();
                        Log log = null;
                        log.d("AccountActivity","nome: " + utente.getNome() + "cognome: " + utente.getCognome() + "ruolo: " +  utente.getRuolo().toString());
                        textViewName.setText(utente.getNome() + " " + utente.getCognome());
                        textViewEmail.setText(utente.getEmail());
                        textViewRole.setText("Ruolo: " + utente.getRuolo().toString());
                    } else {
                        Toast.makeText(AccountActivity.this, "Errore nel recupero delle informazioni.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Utente> call, Throwable t) {
                    Toast.makeText(AccountActivity.this, "Errore di connessione.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
