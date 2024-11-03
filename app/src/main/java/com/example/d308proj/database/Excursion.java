package com.example.d308proj.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

import java.util.Date;

@Entity(tableName = "excursions",
        foreignKeys = @ForeignKey(
                entity = Vacation.class,
                parentColumns = "id",
                childColumns = "vacationId",
                onDelete = ForeignKey.CASCADE
        ))
public class Excursion {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private Date date;

    private int vacationId;  // Foreign key linking to Vacation

    public Excursion(String title, Date date, int vacationId) {
        this.title = title;
        this.date = date;
        this.vacationId = vacationId;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getVacationId() { return vacationId; }
    public void setVacationId(int vacationId) { this.vacationId = vacationId; }
}

