package com.example.shopfromhome.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log; // Importa Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopfromhome.R;
import com.example.shopfromhome.gestore.DettaglioOrdineActivity;
import com.example.shopfromhome.models.Ordine;

import java.util.ArrayList;
import java.util.List;

public class OrdineAdapter extends RecyclerView.Adapter<OrdineAdapter.OrdineViewHolder> {
    private List<Ordine> ordini = new ArrayList<>();
    private OnItemClickListener listener;
    private static final String TAG = "OrdineAdapter"; // Tag per i log

    public interface OnItemClickListener {
        void onItemClick(Ordine ordine);
    }

    public OrdineAdapter(List<Ordine> ordini, OnItemClickListener listener) {
        this.ordini = ordini != null ? new ArrayList<>(ordini) : new ArrayList<>();
        this.listener = listener;
    }

    public void setOrdini(List<Ordine> nuoviOrdini) {
        this.ordini.clear();
        if (nuoviOrdini != null) {
            this.ordini.addAll(nuoviOrdini);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrdineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ordine_gestore, parent, false);
        return new OrdineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdineViewHolder holder, int position) {
        Ordine ordine = ordini.get(position);
        holder.bind(ordine, listener);
    }

    @Override
    public int getItemCount() {
        return ordini.size();
    }

    public static class OrdineViewHolder extends RecyclerView.ViewHolder {
        TextView idOrdine, stato, totalePrezzo, dataRitiro;

        public OrdineViewHolder(View itemView) {
            super(itemView);
            idOrdine = itemView.findViewById(R.id.id_ordine);
            stato = itemView.findViewById(R.id.stato_ordine);
            totalePrezzo = itemView.findViewById(R.id.totale_prezzo);
            dataRitiro = itemView.findViewById(R.id.data_ritiro);
        }

        public void bind(final Ordine ordine, final OnItemClickListener listener) {
            idOrdine.setText(String.format("ID: %s", ordine.getId()));
            stato.setText(String.format("Stato: %s", ordine.getStato()));
            totalePrezzo.setText(String.format("Totale: €%.2f", ordine.getTotalePrezzo()));
            dataRitiro.setText(String.format("Ritiro: %s", ordine.getDataRitiro()));

            // Colore stato per evidenziare se è completato o in attesa
            if (ordine.getStato().equalsIgnoreCase("Completato")) {
                stato.setTextColor(Color.GREEN);
            } else if (ordine.getStato().equalsIgnoreCase("In Attesa")) {
                stato.setTextColor(Color.RED);
            } else {
                stato.setTextColor(Color.BLACK);
            }

            // Click su un ordine
            itemView.setOnClickListener(v -> {
                Log.d(TAG, "Cliccato ordine ID: " + ordine.getId());
                Intent intent = new Intent(itemView.getContext(), DettaglioOrdineActivity.class);
                intent.putExtra("ordineId", ordine.getId());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}