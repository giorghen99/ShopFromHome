    package com.example.shopfromhome.network;

    import android.content.Context;

    import com.example.shopfromhome.Utils.SessionManager;

    import retrofit2.Retrofit;
    import retrofit2.converter.gson.GsonConverterFactory;
    import com.google.gson.Gson;
    import com.google.gson.GsonBuilder;
    import com.example.shopfromhome.Utils.LocalDateTimeAdapter;

    import java.time.LocalDateTime;

    public class ApiConfig {

        private static final String BASE_URL = "http://10.0.2.2:8080/api/";
        //private static final String BASE_URL = "http://192.168.56.1:8080/api/";

        private static Retrofit retrofit = null;

        public static Retrofit getRetrofit() {
            if (retrofit == null) {
                // Configurazione Gson con adapter per LocalDateTime
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                        .setLenient()
                        .create();

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
            return retrofit;
        }

        /*public static Retrofit getRetrofit() {
            if (retrofit == null) {
                // Configura Gson per essere "lenient" (meno rigoroso)
                Gson gson = new GsonBuilder().setLenient().create();

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
            return retrofit;
        }

        // Ottieni il token JWT dalla sessione
        public static String getAuthToken(Context context) {
            SessionManager sessionManager = new SessionManager(context);
            return sessionManager.getToken();  // Restituisce il token salvato
        }*/
    }
