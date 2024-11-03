package com.example.d308proj.ui;

import android.widget.Button;
import android.widget.EditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;

import com.example.d308proj.R;
import com.example.d308proj.application.MyApplication;
import com.example.d308proj.database.Vacation;

import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements VacationAdapter.OnDeleteClickListener {

    private RecyclerView vacationRecyclerView;
    private VacationAdapter vacationAdapter;

    private EditText titleInput;
    private EditText hotelInput;
    private EditText startDateInput;
    private EditText endDateInput;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleInput = findViewById(R.id.titleInput);
        hotelInput = findViewById(R.id.hotelInput);
        startDateInput = findViewById(R.id.startDateInput);
        endDateInput = findViewById(R.id.endDateInput);
        saveButton = findViewById(R.id.saveButton);

        vacationRecyclerView = findViewById(R.id.vacationRecyclerView);
        vacationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        saveButton.setOnClickListener(v -> saveVacation());
        loadVacations();
    }

    //Saves our vacation input
    private void saveVacation() {
        String title = titleInput.getText().toString();
        String hotel = hotelInput.getText().toString();
        String startDateStr = startDateInput.getText().toString();
        String endDateStr = endDateInput.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate, endDate;

        //ensure date is in proper format
        try {
            startDate = sdf.parse(startDateStr);
            endDate = sdf.parse(endDateStr);
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date, must be yyyy-MM-dd", Toast.LENGTH_SHORT).show();
            return;
        }
        Vacation vacation = new Vacation(title, hotel, startDate, endDate);
        Executors.newSingleThreadExecutor().execute(() -> {
            MyApplication.getDatabase().vacationDao().insert(vacation);
            runOnUiThread(() -> {
                //vacation saved confirmation
                Toast.makeText(this, "Vacation has been added!", Toast.LENGTH_SHORT).show();
                loadVacations();
            });
        });
    }
    private void loadVacations() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Vacation> vacations = MyApplication.getDatabase().vacationDao().getAllVacations();
            runOnUiThread(() -> {
                vacationAdapter = new VacationAdapter(vacations, this);
                vacationRecyclerView.setAdapter(vacationAdapter);
            });
        });
    }

    //Vacation Deletion
    @Override
    public void onDeleteClick(Vacation vacation) {
        Executors.newSingleThreadExecutor().execute(() -> {

            int excursionCount = MyApplication.getDatabase().vacationDao().getExcursionCountForVacation(vacation.getId());

           //makes sure that a vacation does not have an excursion attached before deletion
            if (excursionCount > 0) {
                runOnUiThread(() -> Toast.makeText(this, "Cannot delete vacation with associated excursions.", Toast.LENGTH_SHORT).show());
            } else {
                MyApplication.getDatabase().vacationDao().delete(vacation);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Vacation deleted.", Toast.LENGTH_SHORT).show();
                    loadVacations();  // Refresh the list after deletion
                });
            }
        });
    }
}



