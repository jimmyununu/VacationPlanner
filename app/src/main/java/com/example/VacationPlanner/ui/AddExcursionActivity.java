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
            Date excursionDate = dateFormat.parse(dateStr);

            // Debugging log
            Log.d("AddExcursionActivity", "Parsed date: " + excursionDate);
            Log.d("AddExcursionActivity", "Title: " + title);

            // Ensure we have a valid vacation ID
            if (vacationId == -1) {
                Toast.makeText(this, "Vacation ID is invalid.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Fetch the associated vacation's start and end dates
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getInstance(this);
                Vacation vacation = db.vacationDao().getVacationById(vacationId);

                if (vacation != null) {
                    Date vacationStartDate = vacation.getStartDate();
                    Date vacationEndDate = vacation.getEndDate();

                    //lets us view the date without time to guide to proper format
                    SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String simpleStart = dateOnlyFormat.format(vacationStartDate);
                    String simpleEnd = dateOnlyFormat.format(vacationEndDate);


                    // Check if the excursion date is within the vacation period
                    if (excursionDate.before(vacationStartDate) || excursionDate.after(vacationEndDate)) {
                        runOnUiThread(() -> Toast.makeText(this, "Excursion date must be within the vacation period." + simpleStart + " to " + simpleEnd, Toast.LENGTH_SHORT).show());
                    } else {
                        // If date is valid, proceed with saving the excursion
                        Excursion excursion = new Excursion(title, excursionDate, vacationId);
                        db.excursionDao().insert(excursion);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Excursion saved!", Toast.LENGTH_SHORT).show();
                            // Add notification
                            String message = "Excursion: " + title + " is happening today!";
                            AlarmScheduler.scheduleAlarm(this, excursionDate, title, message);
                            finish();
                        });
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Vacation not found.", Toast.LENGTH_SHORT).show());
                }
            });

        } catch (ParseException e) {
            Log.e("AddExcursionActivity", "Date parsing failed", e);
            Toast.makeText(this, "Invalid date format. Use yyyy-MM-dd.", Toast.LENGTH_SHORT).show();
        }
    }

}



