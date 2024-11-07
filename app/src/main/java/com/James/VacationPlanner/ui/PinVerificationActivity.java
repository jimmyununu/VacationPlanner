package com.James.VacationPlanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.James.VacationPlanner.R;
import com.James.VacationPlanner.utils.SecurePreferencesHelper;

public class PinVerificationActivity extends AppCompatActivity {

    private EditText pinInput;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_verification);

        pinInput = findViewById(R.id.pin_input);
        verifyButton = findViewById(R.id.verify_button);

        verifyButton.setOnClickListener(v -> verifyPin());
    }

    private void verifyPin() {
        // Get the user-entered PIN
        String enteredPin = pinInput.getText().toString().trim();

        // Retrieve the stored PIN from SecurePreferencesHelper
        String storedPin = SecurePreferencesHelper.getString(this, "user_pin", "");

        if (storedPin.isEmpty()) {
            // if no pin is set this will redirect to setup a pin
            Intent intent = new Intent(PinVerificationActivity.this, SetupPinActivity.class);
            startActivity(intent);
            finish();
        } else if (enteredPin.equals(storedPin)) {
            // if pin entered matches redirect to home screen
            Intent intent = new Intent(PinVerificationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // PIN is incorrect, show an error
            Toast.makeText(this, "Incorrect PIN. Please try again.", Toast.LENGTH_SHORT).show();
            pinInput.setText(""); // Clear the input
        }
    }
}


