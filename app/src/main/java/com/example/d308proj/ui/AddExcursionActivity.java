package com.example.d308proj.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.d308proj.R;
import com.example.d308proj.database.AppDatabase;
import com.example.d308proj.database.Excursion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

public class AddExcursionActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDate;
    private int vacationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_excursion);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDate = findViewById(R.id.edit_text_date);
        Button buttonSave = findViewById(R.id.button_save_excursion);
        vacationId = getIntent().getIntExtra("vacationId", -1);

        // debug logging
        Log.d("AddExcursionActivity", "Activity started with vacationId: " + vacationId);

        buttonSave.setOnClickListener(v -> saveExcursion());
    }

    private void saveExcursion() {
        String title = editTextTitle.getText().toString().trim();
        String dateStr = editTextDate.getText().toString().trim();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            Date date = dateFormat.parse(dateStr);

            // debugging log
            Log.d("AddExcursionActivity", "Parsed date: " + date);
            Log.d("AddExcursionActivity", "Title: " + title);

            //ensure we have a vacation id assigned.
            if (vacationId == -1) {
                Toast.makeText(this, "Vacation ID is invalid.", Toast.LENGTH_SHORT).show();
                return;
            }
            Excursion excursion = new Excursion(title, date, vacationId);
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    AppDatabase.getInstance(this).excursionDao().insert(excursion);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Excursion saved!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } catch (Exception e) {
                    Log.e("AddExcursionActivity", "Error inserting excursion", e);
                    runOnUiThread(() -> Toast.makeText(this, "Error saving excursion", Toast.LENGTH_SHORT).show());
                }
            });

        } catch (ParseException e) {
            Log.e("AddExcursionActivity", "Date parsing failed", e);
            Toast.makeText(this, "Invalid date format. yyyy-MM-dd", Toast.LENGTH_SHORT).show();
        }
    }
}



