package com.pgl.appia;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResumenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        Toolbar toolbar = findViewById(R.id.toolbarResumen);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Obtener gastos
        List<Gasto> gastos = (List<Gasto>) getIntent().getSerializableExtra("gastos");
        if (gastos == null) {
            gastos = new ArrayList<>();
        }

        TextView tvTotalGeneral = findViewById(R.id.tvTotalGeneral);
        TextView tvTotalGastos = findViewById(R.id.tvTotalGastos);
        LinearLayout layoutCategorias = findViewById(R.id.layoutCategorias);

        // Calcular total
        double totalGeneral = 0;
        for (Gasto gasto : gastos) {
            totalGeneral += gasto.getMonto();
        }

        tvTotalGeneral.setText("$" + String.format("%.2f", totalGeneral));
        tvTotalGastos.setText(gastos.size() + " gastos en total");

        // Agrupar por categoría
        Map<String, Double> totalesPorCategoria = new HashMap<>();
        for (Gasto gasto : gastos) {
            String cat = gasto.getCategoria();
            totalesPorCategoria.put(cat, totalesPorCategoria.getOrDefault(cat, 0.0) + gasto.getMonto());
        }

        // Colores para cada categoría
        Map<String, String> colores = new HashMap<>();
        colores.put("comida", "#FF5722");
        colores.put("transporte", "#2196F3");
        colores.put("entretenimiento", "#9C27B0");
        colores.put("servicios", "#4CAF50");
        colores.put("otros", "#795548");

        Map<String, String> iconos = new HashMap<>();
        iconos.put("comida", "🍔");
        iconos.put("transporte", "🚗");
        iconos.put("entretenimiento", "🎬");
        iconos.put("servicios", "💡");
        iconos.put("otros", "📦");

        // Mostrar cada categoría
        for (Map.Entry<String, Double> entry : totalesPorCategoria.entrySet()) {
            String categoria = entry.getKey();
            double total = entry.getValue();
            double porcentaje = (total / totalGeneral) * 100;

            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setPadding(0, 12, 0, 12);

            // Fila superior: nombre y monto
            LinearLayout filaSuperior = new LinearLayout(this);
            filaSuperior.setOrientation(LinearLayout.HORIZONTAL);
            filaSuperior.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvIcono = new TextView(this);
            tvIcono.setText(iconos.getOrDefault(categoria, "📦"));
            tvIcono.setTextSize(20);

            TextView tvNombre = new TextView(this);
            tvNombre.setText(categoria.substring(0, 1).toUpperCase() + categoria.substring(1));
            tvNombre.setTextSize(16);
            tvNombre.setTextColor(Color.parseColor("#212121"));
            LinearLayout.LayoutParams paramsNombre = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            paramsNombre.setMargins(12, 0, 0, 0);
            tvNombre.setLayoutParams(paramsNombre);

            TextView tvMonto = new TextView(this);
            tvMonto.setText("$" + String.format("%.2f", total));
            tvMonto.setTextSize(16);
            tvMonto.setTextColor(Color.parseColor(colores.getOrDefault(categoria, "#795548")));
            tvMonto.setTypeface(null, android.graphics.Typeface.BOLD);

            filaSuperior.addView(tvIcono);
            filaSuperior.addView(tvNombre);
            filaSuperior.addView(tvMonto);

            // Barra de progreso
            LinearLayout barraFondo = new LinearLayout(this);
            barraFondo.setBackgroundColor(Color.parseColor("#E0E0E0"));
            LinearLayout.LayoutParams paramsBarra = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 16);
            paramsBarra.topMargin = 8;
            barraFondo.setLayoutParams(paramsBarra);

            TextView barraProgreso = new TextView(this);
            barraProgreso.setBackgroundColor(Color.parseColor(colores.getOrDefault(categoria, "#795548")));
            LinearLayout.LayoutParams paramsProgreso = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT);
            paramsProgreso.weight = (float) porcentaje;
            barraProgreso.setLayoutParams(paramsProgreso);

            barraFondo.addView(barraProgreso);

            // Porcentaje
            TextView tvPorcentaje = new TextView(this);
            tvPorcentaje.setText(String.format("%.1f%% del total", porcentaje));
            tvPorcentaje.setTextSize(12);
            tvPorcentaje.setTextColor(Color.parseColor("#757575"));
            tvPorcentaje.setPadding(0, 4, 0, 0);

            itemLayout.addView(filaSuperior);
            itemLayout.addView(barraFondo);
            itemLayout.addView(tvPorcentaje);

            // Separador
            if (entry != totalesPorCategoria.entrySet().toArray()[totalesPorCategoria.size() - 1]) {
                View separador = new View(this);
                separador.setBackgroundColor(Color.parseColor("#E0E0E0"));
                LinearLayout.LayoutParams paramsSep = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
                paramsSep.topMargin = 12;
                separador.setLayoutParams(paramsSep);
                itemLayout.addView(separador);
            }

            layoutCategorias.addView(itemLayout);
        }

        if (totalesPorCategoria.isEmpty()) {
            Toast.makeText(this, "No hay datos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}