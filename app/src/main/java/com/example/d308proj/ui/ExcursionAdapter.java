package com.example.d308proj.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.d308proj.R;
import com.example.d308proj.database.Excursion;

import java.text.SimpleDateFormat;
import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private List<Excursion> excursions;

    public ExcursionAdapter(List<Excursion> excursions) {
        this.excursions = excursions;
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
    }

    @Override
    public int getItemCount() {
        return excursions.size();
    }

    public static class ExcursionViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;

        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_excursion_title);
            dateTextView = itemView.findViewById(R.id.text_view_excursion_date);
        }
    }
}

