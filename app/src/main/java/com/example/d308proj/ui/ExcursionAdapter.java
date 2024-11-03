package com.example.d308proj.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.d308proj.R;
import com.example.d308proj.database.AppDatabase;
import com.example.d308proj.database.Excursion;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executors;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private List<Excursion> excursions;
    private AppDatabase db;

    // Constructor accepting the database instance
    public ExcursionAdapter(List<Excursion> excursions, AppDatabase db) {
        this.excursions = excursions;
        this.db = db;
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_excursion, parent, false);
        return new ExcursionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        Excursion excursion = excursions.get(position);
        holder.titleTextView.setText(excursion.getTitle());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        holder.dateTextView.setText(dateFormat.format(excursion.getDate()));
        holder.deleteButton.setOnClickListener(v -> deleteExcursion(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return excursions.size();
    }

    private void deleteExcursion(int position) {
        Excursion excursion = excursions.get(position);
        Executors.newSingleThreadExecutor().execute(() -> {
            db.excursionDao().delete(excursion);
            excursions.remove(position);
            notifyItemRemoved(position);
        });
    }

    public static class ExcursionViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        Button deleteButton;

        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_excursion_title);
            dateTextView = itemView.findViewById(R.id.text_view_excursion_date);
            deleteButton = itemView.findViewById(R.id.button_delete_excursion);
        }
    }
}



