package com.example.bnr7_with_butterknife.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.bnr7_with_butterknife.Crime;

/**
 * Database class that we build in {@link com.example.bnr7_with_butterknife.CrimeLab}
 */

@Database(entities = {Crime.class},version = 1)
public abstract class CrimeDatatbase extends RoomDatabase {
    //interface that we can use to deal with db
    public abstract CrimeDao getCrimeDao();
}
