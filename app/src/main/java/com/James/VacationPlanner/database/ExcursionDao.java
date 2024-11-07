package com.James.VacationPlanner.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ExcursionDao {

    //adding excursions
    @Insert
    void insert(Excursion excursion);

    //grab the excursion for the asociated vacation id
    @Query("SELECT * FROM excursions WHERE vacationId = :vacationId")
    List<Excursion> getExcursionsForVacation(int vacationId);

    //grab excursion by excursion ID
    @Query("SELECT * FROM excursions WHERE id = :excursionId")
    Excursion getExcursionById(int excursionId);

    //allows for excursion to be updated in the database
    @Update
    void update(Excursion excursion);

    //allows for an excursion to be deleted if needed
    @Delete
    void delete(Excursion excursion);
}



