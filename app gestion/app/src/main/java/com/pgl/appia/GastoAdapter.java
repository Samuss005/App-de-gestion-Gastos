package com.pgl.appia;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {

    private List<Gasto> gastos;
    private OnGastoClickListener listener;

    public interface OnGastoClickListener {
        void onEliminarClick(int position);
    }

    public GastoAdapter(List<Gasto> gastos, OnGastoClickListener listener) {
        this.gastos = gastos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gasto, parent, false);
        return new GastoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gasto gasto = gastos.get(position);

        holder.tvDescripcion.setText(gasto.getDescripcion());
        holder.tvMonto.setText("$" + String.format("%.2f", gasto.getMonto()));
        holder.tvCategoriaFecha.setText(gasto.getCategoria() + " • " + gasto.getFecha());

        // Icono según categoría
        String icono = obtenerIcono(gasto.getCategoria());
        holder.tvIconoCategoria.setText(icono);

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEliminarClick(position);
            }
        });
    }

    private String obtenerIcono(String categoria) {
        switch (categoria.toLowerCase()) {
            case "comida": return "🍔";
            case "transporte": return "🚗";
            case "entretenimiento": return "🎬";
            case "servicios": return "💡";
            default: return "📦";
        }
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    public void actualizarLista(List<Gasto> nuevosGastos) {
        this.gastos = nuevosGastos;
        notifyDataSetChanged();
    }

    static class GastoViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcion, tvMonto, tvCategoriaFecha, tvIconoCategoria;
        ImageButton btnEliminar;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvMonto = itemView.findViewById(R.id.tvMonto);
            tvCategoriaFecha = itemView.findViewById(R.id.tvCategoriaFecha);
            tvIconoCategoria = itemView.findViewById(R.id.tvIconoCategoria);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}