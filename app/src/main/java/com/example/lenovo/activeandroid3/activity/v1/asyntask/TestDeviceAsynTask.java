package com.example.lenovo.activeandroid3.activity.v1.asyntask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.lenovo.activeandroid3.activity.v1.fragment.SettingFragment;
import com.example.lenovo.activeandroid3.activity.v1.interfaces.ButtonInterface;
import com.example.lenovo.activeandroid3.activity.v1.interfaces.TestDeviceInterface;
import com.example.lenovo.activeandroid3.activity.v1.utils.Response;
import com.example.lenovo.activeandroid3.activity.v1.utils.ThreadUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by anway on 7/8/18.
 */

public class TestDeviceAsynTask extends AsyncTask<Void, Void, Response>
{
    Context context;

    TestDeviceInterface my_interface ;
    String TAG = "TestDeviceAsynTask";

    Socket client ;
    Scanner scanner ;
    PrintStream printStream ;

    Long roomId ;
    String newSSID ;
    String newPassword ;
    String resp ;

    // 1 for test command call
    // 2 for set Id, SSID and password send command
    int methodcall = -1 ;

    public TestDeviceAsynTask(TestDeviceInterface my_interface, int methodcall )
    {
        this.my_interface = my_interface ;
        this.methodcall = methodcall ;
    }

    public TestDeviceAsynTask(TestDeviceInterface my_interface, Long roomId, String newSSID, String newPassword, int methodcall)
    {
        Log.e(TAG, "TestDeviceAsynTask: roomId : "+roomId+" newSSID : "+newSSID +" newPassword : "+newPassword );
        this.my_interface = my_interface ;
        this.roomId = roomId;
        this.newSSID = newSSID ;
        this.newPassword = newPassword ;
        this.methodcall = methodcall ;
    }


    @Override
    protected Response doInBackground(Void... voids)
    {
        try {
            Log.e("inside", "doInBackground");

            // dev device ip and port
            // port = 8888 , testIP =  192.168.4.1

//            client = new Socket("192.168.1.106", 8888 );

             client = new Socket("192.168.4.1"  , 8888) ;
             client.setSoTimeout(1000);

            //test device ip and port
//            client = new Socket("192.168.1.101", 8888) ;

            Log.e("Socket ", "created ");

            scanner = new Scanner(client.getInputStream());
            Log.e("scanner ", "created ");

            printStream = new PrintStream(client.getOutputStream());
            Log.e("printStream ", "created ");

            if( methodcall == 1 )
            {
                // test command
                printStream.println("*TST#") ;
                Log.e("after printing : ", "*TST#");
            }else if ( methodcall == 2 )
            {
                // send Id, SSID, Password command  so that device can connected to wifi.
                printStream.println("*SID," + roomId + "," + "" + newSSID + "," + newPassword + "#");
                Log.e("after printing : ", "*SID," + roomId + "," + "" + newSSID + "," + newPassword + "#");

                resp = scanner.nextLine() ;
                Log.e("Response is : ", resp);
            }

            if( scanner.hasNext())
            {
                resp = scanner.nextLine() ;
                Log.e("Response is : ", resp);
            }else
            {
                Log.e(TAG, "doInBackground: scanner has NO next string" );
            }

        } catch (Exception e) {
            Log.e("exception in testCall", e.getMessage());
            if( scanner != null )
                scanner.close();

            if( printStream != null )
                printStream.close();

            if( client != null )
                try {
                    client.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
        return null ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e(TAG, "inside onPreExecute");
    }

    @Override
    protected void onPostExecute(Response response)
    {
        super.onPostExecute(response);

        Log.e(TAG, "inside onPostExecute ") ;

        try {

//            if( scanner.hasNext() )
//            {
//                respons = scanner.nextLine();
//                Log.e("Response in ", "onPostExecute : "+ respons);
//            }else
//            {
//                Log.e(TAG, "doInBackground: scanner has NO next string" );
//            }

            if( scanner != null )
                scanner.close();

            if ( printStream != null )
                printStream.close();

            if( client != null )
                client.close();

            if( methodcall == 1 ) {

                my_interface.testResponse(resp);
            }else if (methodcall ==2 )
            {
                my_interface.deviceSettingResponse(resp );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {


        }
    }
}
