package com.example.bnr7_with_butterknife.database;

import androidx.room.TypeConverter;
import java.util.Date;
import java.util.UUID;

/**
 * Type converter for {@link com.example.bnr7_with_butterknife.Crime} Entity
 * We might use it because we can fill dbs with primitives only (String,int,etc.)
 */

public class CrimeConverter {

    /**
     * Converter from UUID to bd format (String)
     * @param mUUID UUID we need to convert
     * @return converted UUID so we put it onto db
     */
    @TypeConverter
    public String fromUUID(UUID mUUID){
        return mUUID.toString();
    }

    /**
     Converter from bd format (String) to UUID
     * @param string String we need to convert
     * @return UUID that we can use in application
     */
    @TypeConverter
    public UUID toUUID(String string){
        return UUID.fromString(string);
    }

    /**
     Converter from Date to bd format (long)
     * @param mDate Date we need to convert
     * @return converted Date so we put it onto db
     */
    @TypeConverter
    public long fromDate(Date mDate){
        return mDate.getTime();
    }

    /**
     Converter from bd format (long) to Date
     * @param l long we need to convert
     * @return Date that we can use in application
     */
    @TypeConverter
    public Date toDate(long l){
        return new Date(l);
    }
}
