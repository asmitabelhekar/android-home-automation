package com.example.lenovo.activeandroid3.activity.v1.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.lenovo.activeandroid3.activity.v1.fragment.SettingFragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by anway on 6/4/19.
 */

public class ConfigureAsynTask  extends AsyncTask<Void, Void, String>
{
    Context context;
    String requestString;
    ResponseInterface myInterface;
    MethodSelection methodSelection;
    String IP;
    String response;

    String TAG = "ConfigureAsynTask ";

    Socket client ;
//    Scanner scanner ;
    PrintStream printStream ;
    InputStream inputStream;
    String prompt = "#" ;

    public ConfigureAsynTask(Context context, String requestString, ResponseInterface myInterface, MethodSelection methodSelection, String IP)
    {
        this.context =context;
        this.requestString=requestString;
        Log.e(TAG, "requestString is  "+requestString );
        this.myInterface= myInterface;
        this.methodSelection=methodSelection;
        this.IP = IP;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e(TAG, "inside onPreExecute");
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {
            Log.e(TAG, "doInBackground: " );
            client = new Socket(IP, ThreadUtil.port);
//            client.setSoTimeout(ThreadUtil.timeoutForButtonClick);

//            scanner = new Scanner(client.getInputStream());
            inputStream = client.getInputStream();

            printStream = new PrintStream(client.getOutputStream());

            Log.e(TAG, "doInBackground requestString data : "+requestString );
            printStream.println(requestString) ;


//                response = scanner.nextLine() ;

            response = readData(inputStream);
                if( response != null ) {
                    Log.e(TAG + "Response is : ", response);
                }else
                {
                    Log.e(TAG , "Response is NULL");
                }


        }catch (Exception e) {
            Log.e("excep in 1st attempt", TAG + e.getMessage());
        }finally {
            closeAllConnecton();
        }
        return null;
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


    @Override
    protected void onPostExecute(String res) {

        try {
            super.onPostExecute( response );
            closeAllConnecton();
            myInterface.getResponse(response , methodSelection);
        } catch (Exception e) {
            Log.e(TAG + " POST Execute",e.getMessage());
        }
    }
    private String readData(InputStream inputStream) {

        StringBuffer sb = new StringBuffer();
        char ch = 0;
        try {
            char lastChar = prompt.charAt(prompt.length() - 1);
            ch = (char) inputStream.read();
            while (true) {
                Log.e(TAG, "doInBackground: ch data : "+ ch);
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(prompt))
                    {
                        return sb.toString();
                    }
                }
                ch = (char) inputStream.read();
            }

        } catch (IOException e) {
            Log.e(TAG, "readData: exception while reading response "+e.getMessage() );
            e.printStackTrace();
        }

        return null;
    }
}
