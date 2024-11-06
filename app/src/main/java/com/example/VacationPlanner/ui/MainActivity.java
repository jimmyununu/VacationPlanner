package com.example.VacationPlanner.ui;

import android.widget.Button;
import android.widget.EditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;

import com.example.VacationPlanner.R;
import com.example.VacationPlanner.application.MyApplication;
import com.example.VacationPlanner.database.Vacation;

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

    private List<Vacation> vacationList = new ArrayList<>();  // List to store vacation data


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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        //ensure date is in proper format (same as in the vacation detail ediotor)
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
        AlarmScheduler.scheduleAlarm(this, startDate, "Vacation Starting", "Your vacation '" + title + "' is starting today!");
        AlarmScheduler.scheduleAlarm(this, endDate, "Vacation Ending", "Your vacation '" + title + "' is ending today!");
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
            vacationList = MyApplication.getDatabase().vacationDao().getAllVacations(); // Assign to class-level variable
            runOnUiThread(() -> {
                vacationAdapter = new VacationAdapter(vacationList, this);
                vacationRecyclerView.setAdapter(vacationAdapter);

                vacationAdapter.setOnItemClickListener(vacation -> {
                    Intent intent = new Intent(MainActivity.this, VacationDetailActivity.class);
                    intent.putExtra("vacationId", vacation.getId());
                    startActivityForResult(intent, 1);  // Use startActivityForResult to detect updates
                });
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

// Allows us to see our changes if a vacation is edited
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Vacation updatedVacation = (Vacation) data.getSerializableExtra("updatedVacation");

            //finds the updated vacation and changes it to be displayed in our viewer
            if (updatedVacation != null) {
                for (int i = 0; i < vacationList.size(); i++) {
                    if (vacationList.get(i).getId() == updatedVacation.getId()) {
                        vacationList.set(i, updatedVacation);
                        vacationAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        }
    }



}


