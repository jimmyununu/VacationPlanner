package com.example.d308proj.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308proj.R;
import com.example.d308proj.database.AppDatabase;
import com.example.d308proj.database.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

public class VacationDetailActivity extends AppCompatActivity {

    private EditText vacationTitleInput;
    private EditText hotelInput;
    private EditText startDateInput;
    private EditText endDateInput;
    private Button saveVacationButton;
    private Vacation vacation;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_detail);

        vacationTitleInput = findViewById(R.id.vacationTitleInput);
        hotelInput = findViewById(R.id.hotelInput);
        startDateInput = findViewById(R.id.startDateInput);
        endDateInput = findViewById(R.id.endDateInput);
        saveVacationButton = findViewById(R.id.saveVacationButton);
        db = AppDatabase.getInstance(getApplicationContext());
        int vacationId = getIntent().getIntExtra("vacationId", -1);
        loadVacationDetails(vacationId);

        saveVacationButton.setOnClickListener(v -> saveChanges());
    }

    private void loadVacationDetails(int vacationId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            vacation = db.vacationDao().getVacationById(vacationId);
            runOnUiThread(() -> {
                //ensure the vacation being updated exist (Not needed but cause errors/warnings without)
                if (vacation != null) {
                    vacationTitleInput.setText(vacation.getTitle());
                    hotelInput.setText(vacation.getHotel());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    startDateInput.setText(dateFormat.format(vacation.getStartDate()));
                    endDateInput.setText(dateFormat.format(vacation.getEndDate()));
                } else {
                    Toast.makeText(this, "Vacation not found.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    private void saveChanges() {
        // Collect input values
        String title = vacationTitleInput.getText().toString();
        String hotel = hotelInput.getText().toString();
        Date startDate, endDate;

        //debug logging
        Log.d("VacationDetailActivity", "Saving changes for vacation: " + title + ", Hotel: " + hotel);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        //date format validation
        try {
            startDate = dateFormat.parse(startDateInput.getText().toString());
            endDate = dateFormat.parse(endDateInput.getText().toString());
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Try yyyy-mm-dd", Toast.LENGTH_SHORT).show();
            return;
        }
        // start date before end date
        if (endDate.before(startDate)) {
            Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (vacation != null) {
            // set the new information
            vacation.setTitle(title);
            vacation.setHotel(hotel);
            vacation.setStartDate(startDate);
            vacation.setEndDate(endDate);

            // save to database
            Executors.newSingleThreadExecutor().execute(() -> {
                db.vacationDao().update(vacation);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Vacation updated!", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedVacation", vacation);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                });
            });
            AlarmScheduler.scheduleAlarm(this, startDate, "Vacation Starting", "Your vacation '" + title + "' is starting today!");
            AlarmScheduler.scheduleAlarm(this, endDate, "Vacation Ending", "Your vacation '" + title + "' is ending today!");
        }

    }





}


