package com.example.d308proj;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

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

        saveButton.setOnClickListener(v -> saveVacation());
    }

    private void saveVacation() {
        String title = titleInput.getText().toString();
        String hotel = hotelInput.getText().toString();
        String startDateStr = startDateInput.getText().toString();
        String endDateStr = endDateInput.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate, endDate;

        try {
            startDate = sdf.parse(startDateStr);
            endDate = sdf.parse(endDateStr);
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Use yyyy-MM-dd", Toast.LENGTH_SHORT).show();
            return;
        }

        Vacation vacation = new Vacation(title, hotel, startDate, endDate);

        Executors.newSingleThreadExecutor().execute(() -> {
            MyApplication.getDatabase().vacationDao().insert(vacation);
            runOnUiThread(() -> Toast.makeText(this, "Vacation saved!", Toast.LENGTH_SHORT).show());
        });
    }
}
