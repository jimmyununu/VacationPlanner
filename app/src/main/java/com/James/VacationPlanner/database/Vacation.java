package com.James.VacationPlanner.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;


@Entity(tableName = "vacations")
public class Vacation implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String hotel;
    private Date startDate;
    private Date endDate;


    //need for room constructor
    public Vacation() {
    }

    //main constructor
    @Ignore
    public Vacation(String title, String hotel, Date startDate, Date endDate) {
        this.title = title;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
    }

//getters and setters

    //ID
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    //Title
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    //Hotel
    public String getHotel() {return hotel;}
    public void setHotel(String hotel) {this.hotel = hotel;}

    //Start Date
    public Date getStartDate() {return startDate;}
    public void setStartDate(Date startDate) {this.startDate = startDate;}

    //End Date
    public Date getEndDate() {return endDate;}
    public void setEndDate(Date endDate) {this.endDate = endDate;}

}

