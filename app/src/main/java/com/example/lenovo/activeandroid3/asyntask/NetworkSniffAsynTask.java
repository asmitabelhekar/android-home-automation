package com.example.lenovo.activeandroid3.asyntask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;

import com.example.lenovo.activeandroid3.activity.SplashScreen;
import com.example.lenovo.activeandroid3.activity.v1.utils.Response;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import jcifs.netbios.NbtAddress;

/**
 * Created by lenovo on 15/12/17.
 */

public class NetworkSniffAsynTask extends AsyncTask<Void, Void, Void>
{
    private static final String TAG = "Asyntask" ;
    private Context context ;
    int scanStartingPosition ;
    int thread1EndValue ;
    String prefix ;

    SplashScreen splashScreen ;

    ArrayList<String> ipList = null ;

    public NetworkSniffAsynTask(Context context, SplashScreen splashScreen)
    {
        this.context = context ;
        this.splashScreen = splashScreen ;
    }

    public NetworkSniffAsynTask(Context context, int scanStartingPosition, int thread1EndValue,  String prefix )
    {
        this.scanStartingPosition = scanStartingPosition;
        this.thread1EndValue = thread1EndValue ;
        this.context = context ;
        this.prefix= prefix ;
    }

    @Override
    protected Void doInBackground( Void... voids )
    {
        try
        {
            if ( context != null )
            {
                ipList = new ArrayList<>();
                ConnectivityManager cm = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE ) ;
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo() ;
                WifiManager wm = ( WifiManager ) context.getSystemService( Context.WIFI_SERVICE ) ;
                WifiInfo connectionInfo = wm.getConnectionInfo() ;
                List<ScanResult> scanResults = wm.getScanResults();
                Log.e("scanResult size ", scanResults.size()+"" );

                int ipAddress = connectionInfo.getIpAddress() ;
                String myIp = Formatter.formatIpAddress( ipAddress ) ;
                Log.e(TAG, "activeNetwork: " + String.valueOf( activeNetwork ) ) ;
                Log.e(TAG, "my device ipAddress: " +  myIp  ) ;

                String prefix = myIp.substring( 0 , myIp.lastIndexOf(".") + 1 ) ;
                Log.e(TAG, "prefix: " + prefix ) ;

                int scanStartingPosition = 0 ;
                int scanEndingPosition = 0 ;

                String lastPositionChar = myIp.substring( myIp.lastIndexOf(".")+1, myIp.length() );
//                Log.e("lastChar ", lastPositionChar );
                int lastPositionValue = Integer.parseInt(lastPositionChar);
                if( lastPositionValue < 10 )
                {
                    scanStartingPosition = 1 ;
                    scanEndingPosition  = lastPositionValue + 10 ;
                }else {
                    scanStartingPosition  = lastPositionValue -10 ;
                    scanEndingPosition  = lastPositionValue + 10 ;
                }

//                Log.e("scanStartingPosition ", scanStartingPosition+"" );
//                Log.e("scanEndingPosition ", scanEndingPosition+"" );

                for ( int i = scanStartingPosition; i < scanEndingPosition; i++ )
                {
                    String testIp = prefix + String.valueOf( i ) ;
                    InetAddress address = InetAddress.getByName( testIp ) ;
                    boolean reachable = address.isReachable( 800 ) ;
                    String hostName = address.getCanonicalHostName() ;

                    if ( reachable )
                    {
                        Log.e(TAG, "Host: " + hostName + "(" + testIp +") is reachable!");
                        ipList.add(hostName );

                    }

                }
//===================================================================================================

//                for ( int i = scanStartingPosition; i < thread1EndValue; i++ )
//                {
//                    String testIp = prefix + String.valueOf( i ) ;
////                    Log.e("test ip", testIp );
//                    InetAddress address = InetAddress.getByName( testIp ) ;
//                    boolean reachable = address.isReachable( 800 ) ;
//                    String hostName = address.getCanonicalHostName() ;
//
//                    if ( reachable )
//                        Log.e(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) +"(" + address.getHostName() +  ") is reachable!");
//                }



            }
        } catch ( Throwable t ) {
            Log.e( TAG, "Well that's not good.", t ) ;
        }
        return null ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e(TAG, "inside onPreExecute");
    }

//    @Override
//    protected void onPostExecute(Response response)
//    {
//        super.onPostExecute(response);
//
//        try {
//            Log.e(TAG, "inside onPostExecute ") ;
//            String respons = scanner.nextLine();
//            Log.e("Response in ", "onPostExecute : "+ respons);
//
//            client.close();
//            printStream.close();
//            scanner.close();
//
//            if( methodcall == 1 ) {
//
//                my_interface.testResponse(respons);
//            }else if (methodcall ==2 )
//            {
//                my_interface.deviceSettingResponse(respons );
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
        Log.e(TAG, "onPostExecute: " );
        for (String ip : ipList)
        {
            Log.e(TAG, "onPostExecute: IP "+ ip ) ;
        }

        IDIdentificationAsynTask asyn = new IDIdentificationAsynTask(context , ipList  , splashScreen );
        asyn.execute() ;
    }
}
