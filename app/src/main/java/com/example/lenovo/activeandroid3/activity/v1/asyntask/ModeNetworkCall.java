package com.example.lenovo.activeandroid3.activity.v1.asyntask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.lenovo.activeandroid3.activity.v1.adapter.ModeFragmentAdapter;
import com.example.lenovo.activeandroid3.activity.v1.interfaces.ModeOnOffInterface;
import com.example.lenovo.activeandroid3.activity.v1.utils.Response;
import com.example.lenovo.activeandroid3.dbModel.ModeNetworkCallDbModel;
import com.example.lenovo.activeandroid3.model.Mode;

import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by anway on 21/8/18.
 */

public class ModeNetworkCall extends AsyncTask<Void, Void, Response>
{
    Context context ;
    Mode mode ;
    int swithNo ;
    String deviceId ;
    int action ;
    ModeOnOffInterface myInterface ;
    Long buttonId ;
//    ArrayList<ModeNetworkCallDbModel> modeNetworkCallDbModels ;

    String TAG = "ModeNetworkCall";
    Socket client ;
    Scanner scanner ;
    PrintStream printStream ;

    public ModeNetworkCall(Context context ,String deviceId , Long buttonId , int swithNo, int action, ModeOnOffInterface myInterface )
    {
        this.context = context;
        this.swithNo = swithNo ;
        this.deviceId = deviceId ;
        this.buttonId = buttonId ;
        this.action = action;
        this.myInterface = myInterface ;

//        Log.e(TAG, "deviceId: "+deviceId );
//        Log.e(TAG, "switchNumber: "+swithNo );
//        Log.e(TAG, "action: "+action );
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        Log.e(TAG, "inside onPreExecute");
    }

    @Override
    protected Response doInBackground(Void... voids)
    {
//        Log.e("inside", "doInBackground");

        try {

            if (context != null )
            {
                client = new Socket("192.168.1.101", 1234);
//                Log.e("Socket ", "created ");

                scanner = new Scanner(client.getInputStream());
//                Log.e("scanner ", "created ");

                printStream = new PrintStream(client.getOutputStream());
//                Log.e("printStream ", "created ");

                printStream.println("*ACT," + deviceId  + "," + swithNo + "," + action + "#");

                Log.e("after printing : ", "*ACT," + deviceId + "," + swithNo + "," + action + "#" );

            } else {
                Log.e(TAG, "context is null");
            }

        } catch (Exception e) {
            Log.e(TAG, "excep in doInBackground " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Response response)
    {
        super.onPostExecute(response);

        try {
            Log.e(TAG, "inside onPostExecute ") ;

            if( scanner  != null )
            {
                String respons = scanner.nextLine();
                Log.e("Response in ", "onPostExecute : " + respons);

                myInterface.modeOnOffResponse(respons);
            }else {
                Log.e("scanner is null ","in "+TAG+" onPostExecute ");
                Toast.makeText(context,"Your device is not ready", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
