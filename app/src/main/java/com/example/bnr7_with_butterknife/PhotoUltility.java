package com.example.bnr7_with_butterknife;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Util Class for image of {@Crime} scaling and decode
 */
public class PhotoUltility {
    public static Bitmap decodeBitmap(String packageName,int srcWidth,int srcHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(packageName,options);
        int decWidth=options.outWidth;
        int decHeight=options.outHeight;
        long sampleSize=1;
        if (srcWidth>decWidth || srcHeight>decHeight){
            float recWidth=decWidth/srcWidth;
            float recHeight=decHeight/srcHeight;
            sampleSize=Math.round(recWidth>recHeight ? recWidth:recHeight);
        }
        options=new BitmapFactory.Options();//?
        options.inSampleSize= (int) sampleSize;
        return BitmapFactory.decodeFile(packageName,options);
    }
}
