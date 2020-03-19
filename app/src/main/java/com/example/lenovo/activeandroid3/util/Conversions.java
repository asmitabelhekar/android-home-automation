package com.example.lenovo.activeandroid3.util;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by root on 2/6/17.
 */

public class Conversions
{


    // checkString return true if string is null  , it will return false if string is not null
    public static  boolean checkString( String str )
    {
        return str != null && (  str.equals("null") ||  str.equals("NaN") || str.equals("undefined") || str.equals("") || str.isEmpty() );
    }

    public static String convertTimeStampToDateString( Long timestamp )
    {
        try {
            Date start_date = new Date( timestamp );
            return DateFormat.format("dd MMM, yyyy", start_date ).toString();
        }catch ( Exception e ) {

            return null;
        }
    }


    public static String convertTimeStampToDateString1( Long timestamp )
    {
        try
        {
            Date start_date = new Date( timestamp );
            return DateFormat.format("dd-MM-yyyy", start_date ).toString();

        }catch ( Exception e )
        {
            return null;
        }
    }

    public  static  String convertTimeStampToDateTimeString( Long timestamp )
    {
        try
        {
            Date start_date = new Date( timestamp );
            return DateFormat.format("dd MMM, yyyy  hh:mm a", start_date ).toString() ;
        }catch (Exception e) {

            return null;
        }

    }

    public static String makeFirstLeterCap( String s )
    {

        try {
            if( s == null && ( s.equals( "null" ) && s.equals( "" )   ) )
            {
//                Log.e("s is null","null");
                return  s;

            }else {

                return s.substring( 0, 1 ).toUpperCase() + s.substring( 1 );

            }
        } catch (Exception e) {
//            Log.e("excp in makefirstLe", e.getMessage());
        }
        return s ;
    }

    private static StringBuilder EachWordFirstLetterCapital( String str )
    {
        StringBuilder result = null ;
        try {
            result = new StringBuilder( str.length() );
            String words[] = str.split("\\ ");
            for ( int i = 0; i < words.length; i++ ) {
                result.append( Character.toUpperCase( words[i].charAt(0) ) ).append( words[i].substring(1) ).append(" ");
            }
        }catch ( Exception e )
        {
        }
        return  result ;

    }

}