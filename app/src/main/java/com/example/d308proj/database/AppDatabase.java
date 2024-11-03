package com.example.d308proj.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.d308proj.database.Converters;
import com.example.d308proj.database.Excursion;
import com.example.d308proj.database.ExcursionDao;
import com.example.d308proj.database.Vacation;
import com.example.d308proj.database.VacationDao;

@Database(entities = {Vacation.class, Excursion.class}, version = 7, exportSchema = false)
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




