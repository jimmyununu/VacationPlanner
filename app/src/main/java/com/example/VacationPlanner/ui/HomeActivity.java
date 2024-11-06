package com.example.VacationPlanner.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences securePrefs = EncryptedSharedPreferences.create(
                    "secure_prefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            String savedPin = securePrefs.getString("user_pin", null);

            if (savedPin == null) {
                Intent setupPinIntent = new Intent(this, SetupPinActivity.class);
                startActivity(setupPinIntent);
                finish();
            } else {
                Intent verifyPinIntent = new Intent(this, PinVerificationActivity.class);
                startActivity(verifyPinIntent);
                finish();
            }
        } catch (Exception e) {
                //indication of pin error for debugging
            Toast.makeText(this, "Error setting up secure storage. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}





