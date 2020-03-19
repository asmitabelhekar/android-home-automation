package com.example.lenovo.activeandroid3.util;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by anway on 5/10/18.
 */

public class Constants
{
    static String TAG = "Constants ";
    public static final int PORT = 8888  ;

    public static final String IP = "192.168.4.1" ;

//    public static final String IP =  "192.168.43.133" ;
//    public static final String IP = "192.168.1.102" ;


    public static String readData(InputStream inputStream) {

        Log.e(TAG, "readData: ");
        int count = 0 ;

        try {
            StringBuffer sb = new StringBuffer();
            char ch = 0;

            while (true) {

                Log.e(TAG, "readData: read dada here" );
                ch = (char) inputStream.read();
                count = count + 1;
                Log.e(TAG, "doInBackground: ch data : "+ ch);
                sb.append(ch);
                if ((int)ch == 35) {

                    return sb.toString();
                }

                if( count > 40 )
                {
                    return  sb.toString();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: exception while reading response "+e.getMessage() );
            e.printStackTrace();
        }
        return null;
    }



}
