package com.example.VacationPlanner.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;



@Database(entities = {Vacation.class, Excursion.class}, version = 10, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract VacationDao vacationDao();
    public abstract ExcursionDao excursionDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "vacation_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}



