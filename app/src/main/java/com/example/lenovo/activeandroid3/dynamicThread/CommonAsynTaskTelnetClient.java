package com.example.lenovo.activeandroid3.dynamicThread;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.lenovo.activeandroid3.activity.v1.utils.MethodSelection;
import com.example.lenovo.activeandroid3.activity.v1.utils.ResponseInterface;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class CommonAsynTaskTelnetClient extends AsyncTask<Void, Void, String> {

    Context context;
    String requestString;
    ResponseInterface myInterface;
    MethodSelection methodSelection;
    //    String IP = "192.168.1.102";
    String IP ;
//    String response;

    String TAG = "CommonAsynTaskTelnetClient";

//    Socket client ;
//    Scanner scanner ;
//    PrintStreameam printStream ;


    private TelnetClient telnet = new TelnetClient();
    private InputStream in;
    private PrintStream out;
    String prompt = "#";
    String response;


    public CommonAsynTaskTelnetClient(Context context, String requestString, ResponseInterface myInterface, MethodSelection methodSelection, String IP )
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
            // Connect to the specified server
            telnet.connect(IP, 8888);
        }catch (Exception e)
        {
            Log.e(TAG, "doInBackground: exception while connecting to server "+e.getMessage() );
        }

        // Get input and output stream references
        in = telnet.getInputStream();
//        Scanner scanner = new Scanner(telnet.getInputStream());
        out = new PrintStream(telnet.getOutputStream());

        out.println(requestString);
        out.flush();


//        String nextLine = scanner.nextLine();
//        Log.e(TAG, "doInBackground: nextLine Data: "+nextLine );


        response = readData();
        Log.e(TAG, "doInBackground: readData  :  "+response );


        try {
            telnet.disconnect();
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: exception while disconnect "+e.getMessage() );
            e.printStackTrace();
        }


//        try {
//
//            Log.e(TAG, "doInBackground: " );
//
//            try {
//                Log.e(TAG, "doInBackground: "+IP );
//                client = new Socket(IP, 8888);
//            }catch (Exception e )
//            {
//                Log.e(TAG, "doInBackground: error while connection established  "+e.getMessage() );
//            }
//
//            scanner = new Scanner(client.getInputStream());
//            printStream = new PrintStream(client.getOutputStream());
//            printStream.print(requestString) ;
//
//            Log.e(TAG, "requestString "+requestString );
//
////                if( scanner.hasNext())
////                {
//            response = scanner.nextLine() ;
//
//            if( response != null ) {
////                        Log.e(TAG + "Response when i = ", i +"iz :  "+ response);
//                Log.e(TAG + "Response ",   response);
//
//            }else
//            {
//                Log.e(TAG , "Response is NULL");
//            }
//        }catch (Exception e )
//        {
//            Log.e("excep in 1st attempt", TAG + e.getMessage() );
//
//        }finally {
//            closeAllConnecton();
//        }
        return null;
    }

    private String readData() {

        StringBuffer sb = new StringBuffer();
        char ch = 0;
        try {
            char lastChar = prompt.charAt(prompt.length() - 1);
            ch = (char) in.read();
            while (true) {
                Log.e(TAG, "doInBackground: ch data : "+ ch);
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(prompt))
                    {
                        return sb.toString();
                    }
                }
                ch = (char) in.read();
            }

        } catch (IOException e) {
            Log.e(TAG, "doInBackground: exception while reading response "+e.getMessage() );
            e.printStackTrace();
        }

        return null;
    }

//    private void closeAllConnecton() {
//
//        Log.e(TAG, "closeAllConnecton: " );
//        if( scanner != null )
//            scanner.close();
//
//        if( printStream != null )
//            printStream.close();
//
//        if( client != null )
//            try {
//                client.close();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//
//    }

    @Override
    protected void onPostExecute(String res) {

        try {

            super.onPostExecute( res );

//            closeAllConnecton();
//
            myInterface.getResponse(response , methodSelection);

        } catch (Exception e) {

            Log.e(TAG + " POST Execute",e.getMessage());
        }

    }

}
