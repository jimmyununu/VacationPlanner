package com.example.d308proj.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.d308proj.R;

public class ExcursionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_detail);
        TextView titleTextView = findViewById(R.id.text_view_excursion_title);
        TextView dateTextView = findViewById(R.id.text_view_excursion_date);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");
        titleTextView.setText(title);
        dateTextView.setText(date);
    }
}

