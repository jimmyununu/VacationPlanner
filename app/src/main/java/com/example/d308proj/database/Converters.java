package com.example.d308proj.database;

import androidx.room.TypeConverter;
import java.util.Date;


//converts longs into dates
public class Converters {

    //long to date
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    //date to long
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}

