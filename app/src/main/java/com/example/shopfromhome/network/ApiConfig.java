
package com.example.shopfromhome.network;

import android.content.Context;

import com.example.shopfromhome.Utils.LocalDateTimeAdapter;
import com.example.shopfromhome.Utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfig {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/";

    private static Retrofit retrofit = null;
    private static Context appContext;

    public static void initialize(Context context) {
        appContext = context.getApplicationContext();
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {

            if (appContext == null) {
                throw new IllegalStateException(
                        "ApiConfig non inizializzato. Controlla ShopFromHomeApplication."
                );
            }

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .registerTypeAdapter(
                            LocalDateTime.class,
                            new LocalDateTimeAdapter()
                    )
                    .setLenient()
                    .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();

                        String path = original.url().encodedPath();

                        boolean publicAuthenticationEndpoint =
                                path.endsWith("/login")
                                        || path.endsWith("/register");

                        String token = new SessionManager(appContext).getToken();

                        Request.Builder builder = original.newBuilder();

                        if (!publicAuthenticationEndpoint
                                && token != null
                                && !token.trim().isEmpty()) {
                            builder.header(
                                    "Authorization",
                                    "Bearer " + token
                            );
                        }

                        return chain.proceed(builder.build());
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(
                            GsonConverterFactory.create(gson)
                    )
                    .build();
        }

        return retrofit;
    }
}
