package com.example.shopfromhome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopfromhome.R;
import com.example.shopfromhome.models.Prodotto;

import java.util.List;

public class ProductAdapterGestore extends RecyclerView.Adapter<ProductAdapterGestore.ProductViewHolder> {

    private Context context;
    private List<Prodotto> productList;
    private OnProductClickListener listener;
    private OnDeleteClickListener deleteListener;

    // Interfacce per la gestione dei click
    public interface OnProductClickListener {
        void onProductClick(Prodotto product);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Prodotto product, int position);
    }

    public ProductAdapterGestore(Context context, List<Prodotto> productList,
                                 OnProductClickListener listener, OnDeleteClickListener deleteListener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_prodotto_gestore, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Prodotto product = productList.get(position);
        holder.productName.setText(product.getNome());
        holder.productPrice.setText("€ " + product.getPrezzo());
        holder.productId.setText("ID: " + product.getId());
        holder.productQuantity.setText("Quantità: " + product.getQuantitaDisponibile());

        holder.itemView.setOnClickListener(v -> listener.onProductClick(product));

        holder.deleteButton.setOnClickListener(v -> deleteListener.onDeleteClick(product, position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void removeItem(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productId, productQuantity;
        Button deleteButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productId = itemView.findViewById(R.id.productId); // Aggiungi il riferimento per l'ID
            productQuantity = itemView.findViewById(R.id.productQuantity); // Aggiungi il riferimento per la quantità
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
