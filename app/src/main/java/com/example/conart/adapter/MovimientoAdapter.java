package com.example.conart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conart.R;
import com.example.conart.modelo.Movimiento;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MovimientoAdapter extends FirestoreRecyclerAdapter<Movimiento, MovimientoAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MovimientoAdapter(@NonNull FirestoreRecyclerOptions<Movimiento> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Movimiento model) {
        holder.fecha.setText(model.getFecha());
        holder.valor.setText(model.getValor().toString());
        holder.descripción.setText(model.getDescripción());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_movimientos_single, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fecha, valor, descripción;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fecha = itemView.findViewById(R.id.fecha);
            valor = itemView.findViewById(R.id.valor);
            descripción = itemView.findViewById(R.id.descripcion);
        }
    }
}
