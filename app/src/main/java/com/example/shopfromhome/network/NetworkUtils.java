package com.example.shopfromhome.network;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkUtils {

    public interface ApiResponseListener<T> {
        void onSuccess(T response);

        void onFailure(Throwable throwable);
    }

    public static <T> void handleApiCall(Call<T> call, Context context, ApiResponseListener<T> listener) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    // Risposta con errore, visualizza il messaggio di errore
                    listener.onFailure(new Exception("Errore nella risposta del server. Codice di stato: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }
}
