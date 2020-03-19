package com.example.lenovo.activeandroid3.activity.v1.asyntask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.lenovo.activeandroid3.activity.v1.utils.Response;

import java.net.ServerSocket;

/**
 * Created by anway on 17/8/18.
 */

public class BroadcastResponseAsynTask extends AsyncTask<Void, Void, Response>
{

    Context context;

    String response ;
    ServerSocket serverSocket ;

    public BroadcastResponseAsynTask(Context context, String response, ServerSocket serverSocket)
    {
        this.context =context ;
        this.response= response;
        this.serverSocket =serverSocket ;
    }

    @Override
    protected Response doInBackground( Void... voids )
    {
        return null;
    }
}
