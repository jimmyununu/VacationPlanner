package com.example.VacationPlanner.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.VacationPlanner.R;
import com.example.VacationPlanner.database.Vacation;

import java.util.ArrayList;
import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> vacationList;
    private List<Vacation> fullVacationList;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Vacation vacation);
    }

    public VacationAdapter(List<Vacation> vacationList, OnDeleteClickListener onDeleteClickListener) {
        this.vacationList = new ArrayList<>(vacationList);
        this.fullVacationList = new ArrayList<>(vacationList);
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vacation_item, parent, false);
        return new VacationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        Vacation vacation = vacationList.get(position);
        holder.titleTextView.setText(vacation.getTitle());
        holder.hotelTextView.setText(vacation.getHotel());

        holder.deleteButton.setOnClickListener(v -> {
            if (onDeleteClickListener != null) onDeleteClickListener.onDeleteClick(vacation);
        });

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) onItemClickListener.onItemClick(vacation);
        });
    }

    @Override
    public int getItemCount() {
        return vacationList.size();
    }

    public static class VacationViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, hotelTextView;
        Button deleteButton;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            hotelTextView = itemView.findViewById(R.id.hotelTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Vacation vacation);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void filter(String query) {
        vacationList.clear();
        if (query.isEmpty()) {
            vacationList.addAll(fullVacationList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Vacation vacation : fullVacationList) {
                if (vacation.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                        vacation.getHotel().toLowerCase().contains(lowerCaseQuery)) {
                    vacationList.add(vacation);
                }
            }
        }
        notifyDataSetChanged();
    }


}


