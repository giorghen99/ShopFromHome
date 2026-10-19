package com.example.shopfromhome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shopfromhome.R;
import com.example.shopfromhome.models.DettaglioOrdine;
import com.example.shopfromhome.models.Prodotto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DettaglioOrdineAdapter extends BaseAdapter {

    private Context context;
    private List<DettaglioOrdine> dettaglioOrdineList;
    private Map<DettaglioOrdine, Prodotto> prodottoMap = new HashMap<>();

    public DettaglioOrdineAdapter(Context context, List<DettaglioOrdine> dettaglioOrdineList) {
        this.context = context;
        this.dettaglioOrdineList = dettaglioOrdineList;
    }

    @Override
    public int getCount() {
        return dettaglioOrdineList.size();
    }

    @Override
    public Object getItem(int position) {
        return dettaglioOrdineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dettaglio_ordine, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DettaglioOrdine dettaglio = dettaglioOrdineList.get(position);
        Prodotto prodotto = prodottoMap.get(dettaglio);

        if (prodotto != null) {
            holder.prodottoName.setText(prodotto.getNome());
        }

        holder.quantita.setText("Quantità: " + dettaglio.getQuantita());
        holder.prezzoTotale.setText("Prezzo Totale: €" + dettaglio.getPrezzoTotale());

        return convertView;
    }

    public void addProdottoToDetail(DettaglioOrdine dettaglio, Prodotto prodotto) {
        prodottoMap.put(dettaglio, prodotto);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView prodottoName, quantita, prezzoTotale;

        ViewHolder(View view) {
            prodottoName = view.findViewById(R.id.textDettaglioProdotto);
            quantita = view.findViewById(R.id.textDettaglioQuantita);
            prezzoTotale = view.findViewById(R.id.textDettaglioPrezzoTotale);
        }
    }
}