package com.example.lenovo.activeandroid3.dynamicThread;

import android.util.Log;

import com.example.lenovo.activeandroid3.activity.SplashScreen;

public class MyRunnable implements Runnable {

   int startCount;
   int endCount;
   String TAG = "MyRunnable";
   SplashScreen splashScreen;

   public MyRunnable(int startCount, int endCount, SplashScreen splashScreen) {
       // store parameter for later user
      this.endCount=endCount;
      this.startCount=startCount;
      this.splashScreen=splashScreen;
   }

   public void run()
   {
//      Log.e(TAG, "run: "+startCount +" and "+endCount );
      splashScreen.thread1(startCount,endCount);


   }
}