package com.James.VacationPlanner.application;

import android.app.Application;
import androidx.room.Room;

import com.James.VacationPlanner.database.AppDatabase;

public class MyApplication extends Application {

    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "vacation_database")
                .fallbackToDestructiveMigration()
                .build();
    }
    public static AppDatabase getDatabase() {
        return database;
    }
}

