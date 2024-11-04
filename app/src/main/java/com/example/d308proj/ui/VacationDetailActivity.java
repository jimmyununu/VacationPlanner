package com.example.d308proj.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308proj.R;
import com.example.d308proj.database.AppDatabase;
import com.example.d308proj.database.Excursion;
import com.example.d308proj.database.ExcursionDao;
import com.example.d308proj.database.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class VacationDetailActivity extends AppCompatActivity {

    private EditText vacationTitleInput;
    private EditText hotelInput;
    private EditText startDateInput;
    private EditText endDateInput;
    private Button saveVacationButton;
    private Vacation vacation;
    private AppDatabase db;
    private ExcursionDao excursionDao;
    private RecyclerView recyclerView;
    private ExcursionAdapter excursionAdapter;
    private int vacationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_detail);



        // Initialize UI components
        vacationTitleInput = findViewById(R.id.vacationTitleInput);
        hotelInput = findViewById(R.id.hotelInput);
        startDateInput = findViewById(R.id.startDateInput);
        endDateInput = findViewById(R.id.endDateInput);
        saveVacationButton = findViewById(R.id.saveVacationButton);

        db = AppDatabase.getInstance(getApplicationContext());
        excursionDao = db.excursionDao();

        vacationId = getIntent().getIntExtra("vacationId", -1);
        loadVacationDetails(vacationId);

        saveVacationButton.setOnClickListener(v -> saveChanges());

        Button shareVacationButton = findViewById(R.id.shareVacationButton);
        shareVacationButton.setOnClickListener(v -> shareVacationDetails());

        recyclerView = findViewById(R.id.recycler_view_excursions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadExcursionsForVacation(vacationId);

        Button btnAddExcursion = findViewById(R.id.btn_add_excursion);
        btnAddExcursion.setOnClickListener(v -> openAddExcursionActivity());
    }

    private void loadVacationDetails(int vacationId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            vacation = db.vacationDao().getVacationById(vacationId);
            runOnUiThread(() -> {
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

    private void loadExcursionsForVacation(int vacationId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Excursion> excursions = excursionDao.getExcursionsForVacation(vacationId);
            runOnUiThread(() -> {
                excursionAdapter = new ExcursionAdapter(excursions, db, this);  // Pass 'this' as context
                recyclerView.setAdapter(excursionAdapter);
            });
        });
    }



    private void saveChanges() {
        String title = vacationTitleInput.getText().toString();
        String hotel = hotelInput.getText().toString();
        Date startDate, endDate;

        Log.d("VacationDetailActivity", "Saving changes for vacation: " + title + ", Hotel: " + hotel);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            startDate = dateFormat.parse(startDateInput.getText().toString());
            endDate = dateFormat.parse(endDateInput.getText().toString());
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Try yyyy-mm-dd", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDate.before(startDate)) {
            Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (vacation != null) {
            vacation.setTitle(title);
            vacation.setHotel(hotel);
            vacation.setStartDate(startDate);
            vacation.setEndDate(endDate);

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

    private void shareVacationDetails() {
        if (vacation != null) {
            String vacationDetails = "Vacation Title: " + vacation.getTitle() +
                    "\nHotel: " + vacation.getHotel() +
                    "\nStart Date: " + new SimpleDateFormat("yyyy-MM-dd").format(vacation.getStartDate()) +
                    "\nEnd Date: " + new SimpleDateFormat("yyyy-MM-dd").format(vacation.getEndDate());
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, vacationDetails);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Vacation Details");
            startActivity(Intent.createChooser(shareIntent, "Share Vacation Details"));
        } else {
            Toast.makeText(this, "No vacation details to share.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAddExcursionActivity() {
        Intent intent = new Intent(VacationDetailActivity.this, AddExcursionActivity.class);
        intent.putExtra("vacationId", vacationId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExcursionsForVacation(vacationId);
    }



}



