package com.example.shopfromhome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopfromhome.R;
import com.example.shopfromhome.UI.ProductDetailActivity;
import com.example.shopfromhome.UI.ProductListActivity;
import com.example.shopfromhome.models.Prodotto;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Prodotto> productList;
    private Context context;

    public ProductAdapter(List<Prodotto> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Prodotto prodotto = productList.get(position);
        holder.productName.setText(prodotto.getNome());
        holder.productPrice.setText(String.format("€ %.2f", prodotto.getPrezzo()));

        // Use Glide to load the image
        Glide.with(context)
                .load(prodotto.getFoto())
                .placeholder(R.drawable.placeholder)
                .into(holder.productImage);

        // Aggiungi il listener per il pulsante di aggiunta al carrello
        holder.addToCartButton.setOnClickListener(v -> {
            int quantity = 1; // Può essere modificato per ottenere la quantità scelta
            Long prodottoId = prodotto.getId();

            // Chiama il metodo per aggiungere il prodotto al carrello
            ((ProductListActivity) context).aggiungiProdottoAlCarrello(prodottoId, quantity);
        });

        // Handle click on product
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productId", prodotto.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView productImage;
        Button addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.textProductName);
            productPrice = itemView.findViewById(R.id.textProductPrice);
            productImage = itemView.findViewById(R.id.imageProduct);
            addToCartButton = itemView.findViewById(R.id.btnAddToCart);
        }
    }

    // Aggiungi un metodo per ottenere la lista dei prodotti
    public List<Prodotto> getProductList() {
        return productList;
    }
}