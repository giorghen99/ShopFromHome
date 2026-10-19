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
import com.example.shopfromhome.models.Categoria;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private List<Categoria> categoriaList;
    private Context context;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Categoria categoria);
    }

    public CategoriaAdapter(List<Categoria> categoriaList, Context context, OnDeleteClickListener deleteClickListener) {
        this.categoriaList = categoriaList;
        this.context = context;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = categoriaList.get(position);
        holder.nomeCategoria.setText(categoria.getNome());

        holder.btnDelete.setOnClickListener(v -> deleteClickListener.onDeleteClick(categoria));
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView nomeCategoria;
        Button btnDelete;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeCategoria = itemView.findViewById(R.id.nomeCategoria);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}