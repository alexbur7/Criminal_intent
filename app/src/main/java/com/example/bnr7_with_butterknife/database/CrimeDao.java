package com.example.bnr7_with_butterknife.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bnr7_with_butterknife.Crime;
import java.util.List;

/**
 * Interface that we can use for queries to db
 * Write/Select/Edit
 */

@Dao
public interface CrimeDao {
    /**
     * Get all crimes
     * @return list of crimes
     */
    @Query("SELECT * FROM Crimes")
    List<Crime> getCrimes();

    /**
     * Get selected crime
     * @param uuidToString UUID that we compare with UUID of db to get right crime
     * @return Target crime
     */
    @Query("SELECT * FROM Crimes WHERE UUID LIKE:uuidToString")
    Crime getCrime(String uuidToString);

    /**
     * Insert a crime to db
     * @param crime crime we need to insert
     * @return primary key of crime
     */
    @Insert
    Long insertCrime(Crime crime);


    /**
     * Delete a crime from db
     * @param crime crime we need to delete
     */
    @Delete
    void deleteCrime(Crime crime);

    /**
     * Update a crime from db
     * @param crime crime we need to update
     */
    @Update
    void updateCrime(Crime crime);
}
