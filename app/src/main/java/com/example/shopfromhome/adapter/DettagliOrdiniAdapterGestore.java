package com.example.shopfromhome.adapter;

import android.content.Context;
import android.util.Log; // Importa Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopfromhome.R;
import com.example.shopfromhome.models.DettaglioOrdine;
import com.example.shopfromhome.models.Prodotto;

import java.util.ArrayList;
import java.util.List;

public class DettagliOrdiniAdapterGestore extends RecyclerView.Adapter<DettagliOrdiniAdapterGestore.ViewHolder> {

private final Context context;
private final List<DettaglioOrdine> dettagliOrdineList;
private final List<Prodotto> prodottiList;
private static final String TAG = "DettagliOrdiniAdapterGestore"; // Tag per i log

public DettagliOrdiniAdapterGestore(Context context, List<DettaglioOrdine> dettagliOrdineList) {
    this.context = context;
    this.dettagliOrdineList = dettagliOrdineList;
    this.prodottiList = new ArrayList<>();
}

public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView textViewNomeProdotto, textViewQuantita, textViewPrezzoTotale;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewNomeProdotto = itemView.findViewById(R.id.textDettaglioProdottoGestore);
        textViewQuantita = itemView.findViewById(R.id.textDettaglioQuantitaGestore);
        textViewPrezzoTotale = itemView.findViewById(R.id.textDettaglioPrezzoTotaleGestore);
    }
}

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_dettaglio_ordine_gestore, parent, false);
    return new ViewHolder(view);
}

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    DettaglioOrdine dettaglioOrdine = dettagliOrdineList.get(position);
    Prodotto prodotto = prodottiList.get(position); // Associa il prodotto al dettaglio ordine

    holder.textViewNomeProdotto.setText(prodotto.getNome());
    holder.textViewQuantita.setText(String.format("Quantità: %d", dettaglioOrdine.getQuantita()));
    holder.textViewPrezzoTotale.setText(String.format("Totale: € %s", dettaglioOrdine.getPrezzoTotale().toString()));
}

@Override
public int getItemCount() {
    return dettagliOrdineList.size();
}

public void addProdottoToDetail(DettaglioOrdine dettaglioOrdine, Prodotto prodotto) {
    this.dettagliOrdineList.add(dettaglioOrdine);
    this.prodottiList.add(prodotto);
    Log.d(TAG, "Prodotto aggiunto: " + prodotto.getNome());
    notifyItemInserted(dettagliOrdineList.size() - 1);
}

public void clear() {
    this.dettagliOrdineList.clear();
    this.prodottiList.clear();
    notifyDataSetChanged();
}
}