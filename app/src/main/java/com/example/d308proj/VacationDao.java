package com.example.d308proj;

import androidx.room.Dao;
import androidx.room.Insert;
import com.example.d308proj.Vacation;

@Dao
public interface VacationDao {

    @Insert
    void insert(Vacation vacation);

    // data base functions to be added later.
}

