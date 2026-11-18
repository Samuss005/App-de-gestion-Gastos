package com.pgl.appia;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GastoAdapter.OnGastoClickListener {

    private List<Gasto> listaGastos;
    private GastoAdapter adapter;
    private RecyclerView recyclerGastos;
    private TextView tvTotalGastado, tvCantidadGastos, tvVacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaGastos = new ArrayList<>();

        // Inicializar vistas
        recyclerGastos = findViewById(R.id.recyclerGastos);
        tvTotalGastado = findViewById(R.id.tvTotalGastado);
        tvCantidadGastos = findViewById(R.id.tvCantidadGastos);
        tvVacio = findViewById(R.id.tvVacio);
        MaterialButton btnAgregar = findViewById(R.id.btnAgregar);
        MaterialButton btnResumen = findViewById(R.id.btnResumen);

        // Configurar RecyclerView
        adapter = new GastoAdapter(listaGastos, this);
        recyclerGastos.setLayoutManager(new LinearLayoutManager(this));
        recyclerGastos.setAdapter(adapter);

        // Botón Agregar
        btnAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        // Botón Resumen
        btnResumen.setOnClickListener(v -> {
            if (listaGastos.isEmpty()) {
                Toast.makeText(this, "No hay gastos para mostrar", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, ResumenActivity.class);
            intent.putExtra("gastos", new ArrayList<>(listaGastos));
            startActivity(intent);
        });

        actualizarVista();
    }

    private void mostrarDialogoAgregar() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_agregar_gasto, null);

        EditText etDescripcion = dialogView.findViewById(R.id.etDescripcion);
        EditText etMonto = dialogView.findViewById(R.id.etMonto);
        EditText etFecha = dialogView.findViewById(R.id.etFecha);
        RadioGroup rgCategoria = dialogView.findViewById(R.id.rgCategoria);

        // Fecha por defecto
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        etFecha.setText(sdf.format(new java.util.Date()));

        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String descripcion = etDescripcion.getText().toString().trim();
                    String montoStr = etMonto.getText().toString().trim();
                    String fecha = etFecha.getText().toString().trim();

                    // Obtener categoría seleccionada
                    int selectedId = rgCategoria.getCheckedRadioButtonId();
                    String categoria = "otros";
                    if (selectedId == R.id.rbComida) categoria = "comida";
                    else if (selectedId == R.id.rbTransporte) categoria = "transporte";
                    else if (selectedId == R.id.rbEntretenimiento) categoria = "entretenimiento";
                    else if (selectedId == R.id.rbServicios) categoria = "servicios";

                    // Validaciones
                    if (descripcion.isEmpty()) {
                        Toast.makeText(this, "Ingresa una descripción", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (montoStr.isEmpty()) {
                        Toast.makeText(this, "Ingresa un monto", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double monto;
                    try {
                        monto = Double.parseDouble(montoStr);
                        if (monto <= 0) {
                            Toast.makeText(this, "El monto debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Monto no válido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (fecha.isEmpty()) {
                        Toast.makeText(this, "Ingresa una fecha", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Agregar gasto
                    Gasto gasto = new Gasto(descripcion, monto, categoria, fecha);
                    listaGastos.add(0, gasto); // Agregar al inicio
                    adapter.actualizarLista(listaGastos);
                    actualizarVista();

                    Toast.makeText(this, "✓ Gasto agregado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void actualizarVista() {
        double total = 0;
        for (Gasto gasto : listaGastos) {
            total += gasto.getMonto();
        }

        tvTotalGastado.setText("$" + String.format("%.2f", total));
        tvCantidadGastos.setText(listaGastos.size() + " gasto" + (listaGastos.size() != 1 ? "s" : "") + " registrado" + (listaGastos.size() != 1 ? "s" : ""));

        if (listaGastos.isEmpty()) {
            tvVacio.setVisibility(View.VISIBLE);
            recyclerGastos.setVisibility(View.GONE);
        } else {
            tvVacio.setVisibility(View.GONE);
            recyclerGastos.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEliminarClick(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar gasto")
                .setMessage("¿Estás seguro de eliminar este gasto?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    listaGastos.remove(position);
                    adapter.actualizarLista(listaGastos);
                    actualizarVista();
                    Toast.makeText(this, "Gasto eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}