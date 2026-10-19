package com.example.shopfromhome.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopfromhome.R;
import com.example.shopfromhome.Utils.SessionManager;
import com.example.shopfromhome.models.DettagliCarrello;
import com.example.shopfromhome.models.Prodotto;
import com.example.shopfromhome.network.CarrelloManager;

import java.math.BigDecimal;
import java.util.List;

public class CarrelloAdapter extends RecyclerView.Adapter<CarrelloAdapter.CarrelloViewHolder> {

    private final Context context;
    private final List<DettagliCarrello> dettagliCarrello;
    private final CarrelloManager carrelloManager;
    private final OnCarrelloUpdateListener updateListener;
    private final SessionManager sessionManager;

    public CarrelloAdapter(Context context, List<DettagliCarrello> dettagliCarrello, CarrelloManager carrelloManager) {
        this.context = context;
        this.dettagliCarrello = dettagliCarrello;
        this.carrelloManager = carrelloManager;
        this.sessionManager = new SessionManager(context);
        if (context instanceof OnCarrelloUpdateListener) {
            this.updateListener = (OnCarrelloUpdateListener) context;
        } else {
            throw new IllegalStateException("Context must implement OnCarrelloUpdateListener");
        }
    }

    @NonNull
    @Override
    public CarrelloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrello, parent, false);
        return new CarrelloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrelloViewHolder holder, int position) {
        DettagliCarrello dettaglio = dettagliCarrello.get(position);

        // Mostra informazioni base del carrello
        holder.productName.setText(dettaglio.getNomeProdotto());
        holder.productPrice.setText(String.format("\u20ac %.2f", dettaglio.getPrezzoProdotto()));
        holder.productQuantity.setText(String.valueOf(dettaglio.getQuantita()));

        // Carica dettagli del prodotto per ottenere l'immagine
        caricaImmagineProdotto(dettaglio.getProdotto(), holder.productImage);

        // Gestione dei bottoni
        holder.incrementButton.setOnClickListener(v -> aggiornaQuantita(dettaglio, dettaglio.getQuantita() + 1));
        holder.decrementButton.setOnClickListener(v -> {
            if (dettaglio.getQuantita() > 1) {
                aggiornaQuantita(dettaglio, dettaglio.getQuantita() - 1);
            } else {
                Toast.makeText(context, "La quantità minima è 1", Toast.LENGTH_SHORT).show();
            }
        });

        holder.removeButton.setOnClickListener(v -> rimuoviProdotto(dettaglio));
    }

    private void caricaImmagineProdotto(Prodotto prodotto, ImageView productImage) {
        // Carica l'immagine del prodotto
        Glide.with(context)
                .load(prodotto.getFoto()) // Sostituisci con l'URL corretto
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(productImage);
    }

    private void aggiornaQuantita(DettagliCarrello dettaglio, int nuovaQuantita) {
        Long carrelloId = sessionManager.getCartId(); // Recupera l'ID del carrello dalla sessione
        if (carrelloId == null || carrelloId <= 0) {
            Toast.makeText(context, "Errore: ID carrello non valido", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("CarrelloAdapter", "Chiamata API: carrelloId=" + carrelloId
                + ", prodottoId=" + dettaglio.getProdottoId()
                + ", nuovaQuantita=" + nuovaQuantita);

        carrelloManager.aggiornaQuantitaProdotto(
                carrelloId, dettaglio.getProdottoId(), BigDecimal.valueOf(nuovaQuantita),
                new CarrelloManager.CarrelloOperationCallback<Void>() {
                    @Override
                    public void onSuccess(Void response) {
                        Log.d("CarrelloAdapter", "Quantità aggiornata con successo");
                        dettaglio.setQuantita(nuovaQuantita);
                        notifyItemChanged(dettagliCarrello.indexOf(dettaglio));
                        updateListener.onCarrelloUpdated(dettagliCarrello);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e("CarrelloAdapter", "Errore nell'aggiornamento: " + errorMessage);
                        Toast.makeText(context, "Errore nell'aggiornamento: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void rimuoviProdotto(DettagliCarrello dettaglio) {
        Log.d("CarrelloAdapter", "Rimozione prodotto ID: " + dettaglio.getProdottoId());

        Long carrelloId = sessionManager.getCartId(); // Recupera l'ID del carrello dalla sessione
        if (carrelloId == null || carrelloId <= 0) {
            Log.e("CarrelloAdapter", "Errore: ID carrello non valido");
            Toast.makeText(context, "Errore: ID carrello non valido", Toast.LENGTH_SHORT).show();
            return;
        }

        carrelloManager.rimuoviProdottoDalCarrello(carrelloId, dettaglio.getProdottoId(), new CarrelloManager.CarrelloOperationCallback<Void>() {
            @Override
            public void onSuccess(Void response) {
                int position = dettagliCarrello.indexOf(dettaglio);
                dettagliCarrello.remove(position);
                notifyItemRemoved(position);
                updateListener.onCarrelloUpdated(dettagliCarrello);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("CarrelloAdapter", "Errore nella rimozione del prodotto: " + errorMessage);
                Toast.makeText(context, "Errore nella rimozione del prodotto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dettagliCarrello.size();
    }

    static class CarrelloViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage;
        Button incrementButton, decrementButton, removeButton;

        public CarrelloViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.nomeProdotto);
            productPrice = itemView.findViewById(R.id.prezzoProdotto);
            productQuantity = itemView.findViewById(R.id.quantitaProdotto);
            productImage = itemView.findViewById(R.id.productImage);
            incrementButton = itemView.findViewById(R.id.buttonPlus);
            decrementButton = itemView.findViewById(R.id.buttonMinus);
            removeButton = itemView.findViewById(R.id.btnRimuovi);
        }
    }

    public interface OnCarrelloUpdateListener {
        void onCarrelloUpdated(List<DettagliCarrello> dettagliCarrello);
    }
}