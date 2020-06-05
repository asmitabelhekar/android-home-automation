package com.example.lenovo.activeandroid3.asyntask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.lenovo.activeandroid3.activity.*;
import com.example.lenovo.activeandroid3.activity.v1.utils.MethodSelection;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

public class CommonAsynTaskNew extends AsyncTask<Void, Void, String> {

    Context context;
    String requestString;
    ResponseInterfaceNew myInterface;
    MethodSelection methodSelection;
    //    String IP = "192.168.1.102";
    String IP ;
    String response;

    String TAG = "CommonAsynTaskNew ";

    Socket client ;
    PrintStream printStream ;

    private InputStream in;
    String prompt = "#";

    public CommonAsynTaskNew(Context context, String requestString, ResponseInterfaceNew myInterface, MethodSelection methodSelection, String IP )
    {
        this.context =context;
        this.requestString=requestString;
        Log.e(TAG, "requestString is  "+requestString +" IP: "+IP );
        this.myInterface= myInterface;
        this.methodSelection=methodSelection;
        this.IP = IP;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        Log.e(TAG, "inside onPreExecute");
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {

            Log.e(TAG, "doInBackground: " );


            client = new Socket(IP, 8888);

            in = client.getInputStream();

            printStream = new PrintStream(client.getOutputStream());
            printStream.print(requestString) ;

//            Log.e(TAG, "requestString "+requestString );

            response = readData();
//            Log.e(TAG, "doInBackground: readData  :  "+response );

            return  response;

        }catch (Exception e )
        {
//            Log.e("excep in 1st attempt", TAG + e.getMessage() );
            closeAllConnecton();


        }finally {
            closeAllConnecton();
        }
        return null;
    }

    private void closeAllConnecton() {

//        Log.e(TAG, "closeAllConnecton: " );
        if( in != null ) {
            try {
                in.close();
            } catch (IOException e) {
                Log.e(TAG, "closeAllConnecton: exception while closing input stream :"+e.getMessage() );
                e.printStackTrace();
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

            super.onPostExecute(res);

//            Log.e(TAG, "onPostExecute: res: "+res );
            Log.e(TAG, "onPostExecute: response: "+response );


            myInterface.getResponse(response , methodSelection, this.IP );

        } catch (Exception e) {

            Log.e(TAG + " POST Execute",e.getMessage());
        }
    }

    private String readData() {

        StringBuffer sb = new StringBuffer();
        char ch = 0;
        try {
            char lastChar = prompt.charAt(prompt.length() - 1);
            ch = (char) in.read();
            int count = 0;
            while (true) {
//                Log.e(TAG, "doInBackground: ch data : "+ ch);
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(prompt))
                    {
                        return sb.toString();
                    }
                }
                ch = (char) in.read();
                count =count + 1;

                if( count > 40 )
                {
                    break;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: exception while reading response "+e.getMessage() );
            e.printStackTrace();
        }
        return null;
    }
}
