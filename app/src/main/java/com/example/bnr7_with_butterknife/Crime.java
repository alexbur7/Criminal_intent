package com.example.bnr7_with_butterknife;

import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.bnr7_with_butterknife.database.CrimeConverter;

import java.util.Date;
import java.util.UUID;

/**
 * Model class with info about date/time,solved or not and id
 * get mostly used with {@link CrimeLab#getCrimeLab(Context)} ()}
 * @author Kotov&BigNerdRanch
 */

@Entity(tableName = "Crimes")
@TypeConverters(CrimeConverter.class)
public class Crime {
    @PrimaryKey(autoGenerate = true) //primary key that let Android things work properly
    private long _id;
    @ColumnInfo(name = "UUID")
    private UUID mUUID;
    @ColumnInfo(name = "Title")
    private String mTitle;
    @ColumnInfo(name="Solved")
    private boolean mSolved;
    @ColumnInfo(name="Date")
    private Date mDate;
    @ColumnInfo(name = "Suspect")
    private String mSuspect;


    public Crime(){
        this.mDate=new Date();
        this.mUUID=UUID.randomUUID();
    }

    public UUID getId() {
        return mUUID;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    /**
     * Method that returns name of current image in a filesDir
     * @return name in FilesDir
     */
    public String getFileName(){
        return "IMG_"+this.getId()+".jpg";
    }
}
