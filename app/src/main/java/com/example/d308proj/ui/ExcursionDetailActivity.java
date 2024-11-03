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

public class ExcursionDetailActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText dateEditText;
    private Button saveButton;
    private AppDatabase db;
    private int excursionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_detail);

        titleEditText = findViewById(R.id.edit_text_excursion_title);
        dateEditText = findViewById(R.id.edit_text_excursion_date);
        saveButton = findViewById(R.id.button_save_changes);

        db = AppDatabase.getInstance(this);

        excursionId = getIntent().getIntExtra("excursionId", -1);
        loadExcursionDetails(excursionId);

        saveButton.setOnClickListener(v -> saveChanges());
    }

    private void loadExcursionDetails(int excursionId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Excursion excursion = db.excursionDao().getExcursionById(excursionId);
            runOnUiThread(() -> {
                if (excursion != null) {
                    titleEditText.setText(excursion.getTitle());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateEditText.setText(dateFormat.format(excursion.getDate()));
                } else {
                    Toast.makeText(this, "Excursion not found.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    private void saveChanges() {
        String newTitle = titleEditText.getText().toString().trim();
        String newDateStr = dateEditText.getText().toString().trim();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            Date newDate = dateFormat.parse(newDateStr);

            Executors.newSingleThreadExecutor().execute(() -> {
                Excursion excursion = db.excursionDao().getExcursionById(excursionId);
                if (excursion != null) {
                    excursion.setTitle(newTitle);
                    excursion.setDate(newDate);
                    db.excursionDao().update(excursion);
                    runOnUiThread(() -> Toast.makeText(this, "Excursion updated!", Toast.LENGTH_SHORT).show());
                    //excursion alarm
                    String message = "Excursion: " + newTitle + " is happening today!";
                    AlarmScheduler.scheduleAlarm(this, newDate, newTitle, message);
                    finish();
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Excursion not found.", Toast.LENGTH_SHORT).show());
                }
            });

        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. yyyy-MM-dd", Toast.LENGTH_SHORT).show();
            Log.e("ExcursionDetailActivity", "Date parsing error", e);
        }
    }
}


