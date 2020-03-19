package com.example.lenovo.activeandroid3.activity.v1.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anway on 11/8/18.
 */

public class IpScanLimit
{
    public static void main(String argd[] )
    {
//        String myIp = "192.168.1.144" ;
//        System.out.println("myIp : "+myIp);
//
//        String prefix = myIp.substring( 0 , myIp.lastIndexOf(".") + 1 ) ;
//        System.out.println("prefix : " + prefix);
//
//        int scanStartingPosition = 0 ;
//        int scanEndingPosition = 0 ;
//
//        String lastPositionChar = myIp.substring( myIp.lastIndexOf(".")+1, myIp.length() );
//        System.out.println("lastChar : "+lastPositionChar );
//        int lastPositionValue = Integer.parseInt(lastPositionChar);
//        if( lastPositionValue < 10 )
//        {
//            scanStartingPosition = 1 ;
//            scanEndingPosition  = lastPositionValue + 10 ;
//        }else {
//            scanStartingPosition  = lastPositionValue -10 ;
//            scanEndingPosition  = lastPositionValue + 10 ;
//        }
//
//        System.out.println("scanStartingPosition : " + scanStartingPosition);
//        System.out.println("scanEndingPosition : " + scanEndingPosition);

        List<String> data= new ArrayList<>();
        data.add("1");

        //data.add("2"); data.add("3"); data.add("4"); data.add("5");

        int halfData = data.size() / 2 ;
        System.out.println("halfData : " + halfData);

        for ( int i = 0 ; i <= halfData ; i++ )
        {
            System.out.println("value of fist For  : " + data.get( i ));
        }

        for ( int i = halfData + 1 ; i < data.size() ; i++ )
        {
            System.out.println("value of Second for  : " + data.get( i ));
        }

//        int value = 9 ;
//
//        int result = value / 2 ;
//        System.out.println("result : " + result);

    }

}
