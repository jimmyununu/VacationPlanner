package com.example.VacationPlanner.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecurePreferencesHelper {

    private static SharedPreferences getEncryptedPrefs(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            return EncryptedSharedPreferences.create(
                    "secure_prefs",
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing EncryptedSharedPreferences", e);
        }
    }
    public static void putString(Context context, String key, String value) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        prefs.edit().putString(key, value).apply();
    }
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        return prefs.getString(key, defaultValue);
    }
}






