package com.example.bnr7_with_butterknife;

import android.content.Context;
import android.view.MenuItem;
import androidx.room.Room;
import com.example.bnr7_with_butterknife.database.CrimeDatatbase;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Singleton Class that provides a lot of methods for working with list of {@link Crime}
 */

public class CrimeLab {
    private static CrimeLab mCrimeLab;
    private static CrimeDatatbase db;
    private Context mContext;

    /**
     * Singleton Constructor
     * @param context
     */
    private CrimeLab(Context context){
        mContext=context;
        db= Room.databaseBuilder(context,CrimeDatatbase.class,"TestingTheBase").allowMainThreadQueries().build();
    }

    /**
     * Returns file that stores image of target crime
     * @param crime crime we need to get file
     * @return file of target crime
     */
    public File getPhotoFile(Crime crime){
        return new File(mContext.getFilesDir(),crime.getFileName());
    }

    /**
     * Singleton getter
     * @param context
     * @return CrimeLab
     */
    public static CrimeLab getCrimeLab(Context context){
        if (mCrimeLab == null) mCrimeLab=new CrimeLab(context);
        return mCrimeLab;
    }

    public List<Crime> getCrimes(){
        return db.getCrimeDao().getCrimes();
    }

    /**
     * adds new Crime to the list
     * @return the id of new crime so we dont need to create a crime on Controller side
     * @see CrimeListFragment#onOptionsItemSelected(MenuItem)
     */
    public Crime addCrime(){
        Crime crime=new Crime();
       db.getCrimeDao().insertCrime(crime);
       return crime;
    }

    public void deleteCrime(Crime crime){
        db.getCrimeDao().deleteCrime(crime);
    }

    public Crime getCrimeById(UUID id){
        return db.getCrimeDao().getCrime(id.toString());
    }

    public void updateCrime(Crime crime){
        db.getCrimeDao().updateCrime(crime);
    }
}
