package com.example.d308proj.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308proj.R;
import com.example.d308proj.database.Vacation;

import java.util.List;


//displays our list of currently saved vacation
public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> vacationList;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Vacation vacation);
    }

    public VacationAdapter(List<Vacation> vacationList, OnDeleteClickListener onDeleteClickListener) {
        this.vacationList = vacationList;
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
        holder.deleteButton.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(vacation));
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
}

