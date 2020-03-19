package com.example.lenovo.activeandroid3.activity.v1.asyntask;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.example.lenovo.activeandroid3.activity.v1.adapter.RoomInsideDialogAdapter;
import com.example.lenovo.activeandroid3.activity.v1.interfaces.ButtonInterface;
import com.example.lenovo.activeandroid3.activity.v1.utils.Response;
import com.example.lenovo.activeandroid3.activity.v1.utils.ThreadUtil;
import com.example.lenovo.activeandroid3.util.Constants;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


/**
 * Created by anway on 4/8/18.
 */

public class ButtonNetworkCall extends AsyncTask<Void, Void, String> {
    Context context;
//    String roomId;
//    Long buttonId ;
//    int swithNo, action;
    String IP ;
    ButtonInterface my_interface ;
    String TAG = "ButtonNetworkCall";

    Socket client ;
    PrintStream printStream ;
    InputStream inputStream;
    String prompt = "#" ;
    String response;
    String requestString;

    public ButtonNetworkCall(Context context, String requestString, ButtonInterface my_interface, String ip) {

        this.context = context;
        this.my_interface = my_interface;
        this.IP = ip ;
        this.requestString = requestString;

        // Run a timer after you started the AsyncTask
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                // Do nothing
            }

            public void onFinish() {
                this.cancel();
            }

        }.start();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e(TAG, "inside onPreExecute");
    }

    @Override
    protected String doInBackground(Void... voids)
    {
        Log.e("inside", "doInBackground");

        try {
            Log.e(TAG, "doInBackground: " );

            client = new Socket(IP, ThreadUtil.port);


            Log.e(TAG, "doInBackground: socket creted " );

            inputStream = client.getInputStream();
            Log.e(TAG, "doInBackground: inputStream creted " );

            printStream = new PrintStream(client.getOutputStream());
            Log.e(TAG, "doInBackground: printStream creted " );


            printStream.println(requestString) ;
            Log.e(TAG, "doInBackground: Deta send "+requestString );

//            response = readData(inputStream);
//            Log.e(TAG, "doInBackground: response received " );
//
//
//            if( response != null ) {
//                Log.e(TAG + "Response is : ", response);
//            }else
//            {
//                Log.e(TAG , "Response is NULL");
//            }

        }catch (Exception e) {

            Log.e(TAG, "doInBackground: Exception while socket operations "+e.getMessage() );

        }finally {
            closeAllConnecton();
        }

        return null;
    }


    private String readData(InputStream inputStream) {

        Log.e(TAG, "readData: ");
        int count = 0 ;

        try {
            StringBuffer sb = new StringBuffer();
            char ch = 0;

//            char lastChar = prompt.charAt(prompt.length() - 1);
//            Log.e(TAG, "readData: lastChar : "+lastChar );

            while (true) {

                Log.e(TAG, "readData: read dada here" );
                ch = (char) inputStream.read();

                count = count + 1;
                Log.e(TAG, "doInBackground: ch data : "+ ch);
                sb.append(ch);
                if ((int)ch == 35) {

                    if( inputStream != null )
                    {
                        inputStream.close();
                    }
                    return sb.toString();
                }

                if( count > 40 )
                {
                    if( inputStream != null )
                    {
                        inputStream.close();
                    }
                    return  sb.toString();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: exception while reading response "+e.getMessage() );
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String resp)
    {
        super.onPostExecute(response);

        try {
            super.onPostExecute( response );
            closeAllConnecton();
            my_interface.buttonClickResponse(response);
        } catch (Exception e) {

            Log.e(TAG + " POST Execute",e.getMessage());
        }
    }

    private void closeAllConnecton() {

        if( inputStream != null ) {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "closeAllConnecton: Exception while closing inputStream "+e.getMessage() );
            }
        }

        if( printStream != null )
            printStream.close();

        if( client != null )
            try {
                client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
    }



//    private String readData(InputStream inputStream) {
//
//        Log.e(TAG, " in readData: ");
//        StringBuffer sb = new StringBuffer();
//        char ch = 0;
//        try {
//            char lastChar = prompt.charAt(prompt.length() - 1);
//            Log.e(TAG, "readData: lastChar : "+lastChar );
//
//            while (true) {
//                ch = (char) inputStream.read();
//                Log.e(TAG, "readData: data : "+ ch);
//                sb.append(ch);
//
//                if( (int)ch == 35 )
//                {
//                    if (sb.toString().endsWith(prompt))
//                    {
//                        return sb.toString();
//                    }
//                }
//
////                ch = (char) inputStream.read();
//
//
//            }
//
//        } catch (IOException e) {
//            Log.e(TAG, "readData: exception while reading response "+e.getMessage() );
//            e.printStackTrace();
//        }
//
//        return null;
//    }


}