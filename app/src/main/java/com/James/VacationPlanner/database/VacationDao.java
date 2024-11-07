package com.James.VacationPlanner.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VacationDao {

    //allows the ability to insert a new vacation in the database
    @Insert
    void insert(Vacation vacation);

    //allows the deletion of a vacation
    @Delete
    void delete(Vacation vacation);

    //gets number of excursions attached to a vacation ID
    @Query("SELECT COUNT(*) FROM excursions WHERE vacationId = :vacationId")
    int getExcursionCountForVacation(int vacationId);

    //Gets a list of all vacations
    @Query("SELECT * FROM vacations")
    List<Vacation> getAllVacations();

    //gets a vacation with associated ID
    @Query("SELECT * FROM vacations WHERE id = :vacationId")
    Vacation getVacationById(int vacationId);

    //enables a vacation to be updated
    @Update
    void update(Vacation vacation);
}



