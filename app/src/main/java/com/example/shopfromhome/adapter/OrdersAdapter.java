package com.example.shopfromhome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shopfromhome.R;
import com.example.shopfromhome.UI.OrderDetailActivity;
import com.example.shopfromhome.models.Ordine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private Context context;
    private List<Ordine> ordiniList;
    private SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
    private SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());


    public OrdersAdapter(Context context, List<Ordine> ordiniList) {
        this.context = context;
        this.ordiniList = ordiniList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Ordine ordine = ordiniList.get(position);

        holder.orderId.setText("Ordine ID: " + ordine.getId());

        try {
            // Converte la data in un formato leggibile
            Date dataRitiro = inputFormat.parse(ordine.getDataRitiro());
            holder.orderDate.setText("Ritiro: " + displayFormat.format(dataRitiro));
        } catch (ParseException | NullPointerException e) {
            holder.orderDate.setText("Data non disponibile");
        }

        holder.orderStatus.setText("Stato: " + ordine.getStato());

        // Cambia l'icona in base allo stato dell'ordine
        if (ordine.getStato().equalsIgnoreCase("In preparazione")) {
            holder.orderStatusIcon.setImageResource(R.drawable.ic_status_in_progress); // Aggiungi icona in progress
        } else if (ordine.getStato().equalsIgnoreCase("Completato")) {
            holder.orderStatusIcon.setImageResource(R.drawable.ic_status_completed); // Aggiungi icona completato
        } else {
            holder.orderStatusIcon.setImageResource(R.drawable.ic_status_pending); // Aggiungi icona pending
        }

        // Visualizza il totale dell'ordine
        holder.orderTotal.setText("Totale: €" + String.format(Locale.getDefault(), "%.2f", ordine.getTotalePrezzo()));

        // Rendi la card cliccabile per visualizzare i dettagli
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("ordine", ordine);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return ordiniList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, orderStatus, orderTotal;
        ImageView orderStatusIcon; // Aggiungi questa variabile per l'icona dello stato

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.textOrderId);
            orderDate = itemView.findViewById(R.id.textOrderDate);
            orderStatus = itemView.findViewById(R.id.textOrderStatus);
            orderTotal = itemView.findViewById(R.id.textOrderTotal);
            orderStatusIcon = itemView.findViewById(R.id.orderStatusIcon); // Collega l'icona
        }
    }
}
