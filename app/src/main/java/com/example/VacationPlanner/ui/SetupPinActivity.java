package com.example.VacationPlanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.VacationPlanner.R;
import com.example.VacationPlanner.utils.SecurePreferencesHelper;

public class SetupPinActivity extends AppCompatActivity {

    private EditText pinInput;
    private EditText confirmPinInput;
    private Button savePinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_pin);

        pinInput = findViewById(R.id.pin_input);
        confirmPinInput = findViewById(R.id.confirm_pin_input);
        savePinButton = findViewById(R.id.save_pin_button);

        savePinButton.setOnClickListener(v -> savePin());
    }

    private void savePin() {
        String pin = pinInput.getText().toString().trim();
        String confirmPin = confirmPinInput.getText().toString().trim();

        // checks that pins match
        if (pin.isEmpty() || confirmPin.isEmpty()) {
            Toast.makeText(this, "Please enter and confirm your PIN.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pin.equals(confirmPin)) {
            Toast.makeText(this, "PINs do not match. Please try again.", Toast.LENGTH_SHORT).show();
            confirmPinInput.setText(""); // Clear the confirm field for re-entry
            return;
        }

        if (pin.length() < 4) {
            Toast.makeText(this, "PIN must be at least 4 digits.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Save pin
        SecurePreferencesHelper.putString(this, "user_pin", pin);

        Toast.makeText(this, "PIN set successfully!", Toast.LENGTH_SHORT).show();

        // Redirect to the PIN verification
        Intent intent = new Intent(SetupPinActivity.this, PinVerificationActivity.class);
        startActivity(intent);
        finish();
    }
}


