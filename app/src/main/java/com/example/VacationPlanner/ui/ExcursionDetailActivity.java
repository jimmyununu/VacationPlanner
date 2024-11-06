package com.example.VacationPlanner.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.VacationPlanner.R;
import com.example.VacationPlanner.database.AppDatabase;
import com.example.VacationPlanner.database.Excursion;
import com.example.VacationPlanner.database.Vacation;

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
    private int vacationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_detail);

        titleEditText = findViewById(R.id.edit_text_excursion_title);
        dateEditText = findViewById(R.id.edit_text_excursion_date);
        saveButton = findViewById(R.id.button_save_changes);

        db = AppDatabase.getInstance(this);

        excursionId = getIntent().getIntExtra("excursionId", -1);
        vacationId = getIntent().getIntExtra("vacationId", -1);

        // Log to verify values
        Log.d("ExcursionDetailActivity", "Excursion ID: " + excursionId + ", Vacation ID: " + vacationId);

        if (vacationId == -1) {
            Toast.makeText(this, "Invalid vacation ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadExcursionDetails(excursionId);

        // Set click listener for save button
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
            Date newExcursionDate = dateFormat.parse(newDateStr);

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getInstance(this);
                Vacation vacation = db.vacationDao().getVacationById(vacationId);

                if (vacation != null) {
                    String formattedStartDate = dateFormat.format(vacation.getStartDate());
                    String formattedEndDate = dateFormat.format(vacation.getEndDate());

                    // Validate that the excursion date is within the vacation period
                    if (newExcursionDate.before(vacation.getStartDate()) || newExcursionDate.after(vacation.getEndDate())) {
                        runOnUiThread(() -> Toast.makeText(this, "Excursion date must be within the vacation period: "
                                + formattedStartDate + " to " + formattedEndDate, Toast.LENGTH_SHORT).show());
                    } else {
                        // Update the excursion if the date is valid
                        Excursion excursion = db.excursionDao().getExcursionById(excursionId);
                        if (excursion != null) {
                            excursion.setTitle(newTitle);
                            excursion.setDate(newExcursionDate);
                            db.excursionDao().update(excursion);

                            runOnUiThread(() -> {
                                Toast.makeText(this, "Excursion updated!", Toast.LENGTH_SHORT).show();

                                // Schedule the alarm
                                String message = "Excursion: " + newTitle + " is happening today!";
                                AlarmScheduler.scheduleAlarm(this, newExcursionDate, newTitle, message);

                                finish();
                            });
                        } else {
                            runOnUiThread(() -> Toast.makeText(this, "Excursion not found.", Toast.LENGTH_SHORT).show());
                        }
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Vacation not found.", Toast.LENGTH_SHORT).show());
                }
            });

        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Use yyyy-MM-dd.", Toast.LENGTH_SHORT).show();
        }
    }
}

