package com.example.d308proj.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ExcursionDao {

    @Insert
    void insert(Excursion excursion);

    @Query("SELECT * FROM excursions WHERE vacationId = :vacationId")
    List<Excursion> getExcursionsForVacation(int vacationId);

    @Query("SELECT * FROM excursions WHERE id = :excursionId")
    Excursion getExcursionById(int excursionId);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);
}



