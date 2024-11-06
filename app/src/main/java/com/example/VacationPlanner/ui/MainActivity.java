package com.example.VacationPlanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.VacationPlanner.R;
import com.example.VacationPlanner.application.MyApplication;
import com.example.VacationPlanner.database.Vacation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements VacationAdapter.OnDeleteClickListener {

    private RecyclerView vacationRecyclerView;
    private VacationAdapter vacationAdapter;
    private EditText titleInput, hotelInput, startDateInput, endDateInput;
    private Button saveButton;
    private SearchView searchView;
    private List<Vacation> vacationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleInput = findViewById(R.id.titleInput);
        hotelInput = findViewById(R.id.hotelInput);
        startDateInput = findViewById(R.id.startDateInput);
        endDateInput = findViewById(R.id.endDateInput);
        saveButton = findViewById(R.id.saveButton);
        searchView = findViewById(R.id.searchView);

        vacationRecyclerView = findViewById(R.id.vacationRecyclerView);
        vacationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        saveButton.setOnClickListener(v -> saveVacation());
        setupSearchView();
        loadVacations();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                vacationAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                vacationAdapter.filter(newText);
                return false;
            }
        });
    }

    private void saveVacation() {
        String title = titleInput.getText().toString();
        String hotel = hotelInput.getText().toString();
        String startDateStr = startDateInput.getText().toString();
        String endDateStr = endDateInput.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

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
                    Toast.makeText(this, "Vacation has been added!", Toast.LENGTH_SHORT).show();
                    loadVacations();
                });
            });
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Use yyyy-MM-dd", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadVacations() {
        Executors.newSingleThreadExecutor().execute(() -> {
            vacationList = MyApplication.getDatabase().vacationDao().getAllVacations();
            runOnUiThread(() -> {
                vacationAdapter = new VacationAdapter(vacationList, this);
                vacationRecyclerView.setAdapter(vacationAdapter);
                vacationAdapter.setOnItemClickListener(vacation -> {
                    Intent intent = new Intent(MainActivity.this, VacationDetailActivity.class);
                    intent.putExtra("vacationId", vacation.getId());
                    startActivityForResult(intent, 1);  // Request code 1 for update
                });
            });
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Vacation updatedVacation = (Vacation) data.getSerializableExtra("updatedVacation");
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

    @Override
    public void onDeleteClick(Vacation vacation) {
        Executors.newSingleThreadExecutor().execute(() -> {
            int excursionCount = MyApplication.getDatabase().vacationDao().getExcursionCountForVacation(vacation.getId());
            if (excursionCount > 0) {
                runOnUiThread(() -> Toast.makeText(this, "Cannot delete vacation with associated excursions.", Toast.LENGTH_SHORT).show());
            } else {
                MyApplication.getDatabase().vacationDao().delete(vacation);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Vacation deleted.", Toast.LENGTH_SHORT).show();
                    loadVacations();
                });
            }
        });
    }
    //reloads our vacations to reflect any changes that may be made
    @Override
    protected void onResume() {
        super.onResume();
        loadVacations();
    }

}





