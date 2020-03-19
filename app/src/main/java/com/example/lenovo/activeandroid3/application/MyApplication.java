package com.example.lenovo.activeandroid3.application;



import android.app.Application ;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid ;


/**
 * Created by lenovo on 27/11/17.
 */

public class MyApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        //Initializing Active Android
        ActiveAndroid.initialize( this ) ;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
