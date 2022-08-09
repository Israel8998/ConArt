package com.example.conart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.conart.adapter.MovimientoAdapter;
import com.example.conart.modelo.Movimiento;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ObtenerDatos extends AppCompatActivity {

    private ImageButton regresar;

    RecyclerView mRecycler;
    MovimientoAdapter mAdapter;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obtener_datos);

        //obtener y enviar valores de una pantalla a otra
        Intent intent = this.getIntent();
        Bundle extra = intent.getExtras();
        String email = extra.getString("email");

        //configurar botón regresa
        ImageButton regresar = findViewById(R.id.btnRegresar);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regresar = new Intent(ObtenerDatos.this, IngresoDatos.class);
                regresar.putExtra("email", email);
                startActivity(regresar);
            }
        });

        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        Query query = mFirestore.collection("Movimientos").document(email).collection("Libro diario");

        FirestoreRecyclerOptions<Movimiento> firestoreRecyclerOptions = new
                FirestoreRecyclerOptions.Builder<Movimiento>().setQuery(query, Movimiento.class).build();
        mAdapter = new MovimientoAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}