package com.example.d308proj.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface VacationDao {
    @Insert
    void insert(Vacation vacation);
    @Delete
    void delete(Vacation vacation);
    @Query("SELECT COUNT(*) FROM excursions WHERE vacationId = :vacationId")
    int getExcursionCountForVacation(int vacationId);
    @Query("SELECT * FROM vacations")
    List<Vacation> getAllVacations();
}



