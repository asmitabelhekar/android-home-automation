package com.example.lenovo.activeandroid3.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.TableInfo;
import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.v1.activity.V1MainActivity;
import com.example.lenovo.activeandroid3.activity.v1.utils.DatabaseQuery;
import com.example.lenovo.activeandroid3.activity.v1.utils.MethodSelection;
import com.example.lenovo.activeandroid3.activity.v1.utils.ResponseInterface;
import com.example.lenovo.activeandroid3.activity.v1.utils.ThreadUtil;
import com.example.lenovo.activeandroid3.asyntask.CommonAsynTaskNew;
import com.example.lenovo.activeandroid3.dynamicThread.CommonAsynTaskTelnetClient;
import com.example.lenovo.activeandroid3.dynamicThread.MyRunnable;
import com.example.lenovo.activeandroid3.model.Mobile;
import com.example.lenovo.activeandroid3.model.Mode;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.model.SwitchBoard;
import com.example.lenovo.activeandroid3.model.SwitchButton;
import com.example.lenovo.activeandroid3.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


/**
 * Created by hanumant on 17/11/16.
 */
public class SplashScreen extends Activity implements ResponseInterfaceNew {

    SharedPreferences sharedPref;
    Context context;
    static String TAG = "SplashScreen ";

    LinearLayout ll_pBar;
    ProgressBar pBar;
    List<String> ipadd = new ArrayList<>();
    int totalDeviceScanCompleted = 0 ;
    List<String> totalIPScanCompleted = new ArrayList<>();


    WifiInfo connectionInfo ;
    String myIp;
    String prefix ;
    SplashScreen my_object = this;

    List<String>  myDeviceList = new ArrayList<>();

    String email , homeName ;
    String isModesCreated ;

    //MODES
    String[] modeNamesArray = {"Morning", "Afternoon" ,"Evening","Night"};
    int[]  modeImageArray = { R.drawable.mode_morning  , R.drawable.mode_afternoon, R.drawable.mode_evening , R.drawable.mode_night} ;



    /**
     *
     */
    Socket threadTwoClientSocket;
    //    Scanner threadTwoScanner;
    PrintStream threadTwoPrintStream;

    Socket threadTwoStatusSocket;
    //    Scanner threadTwoStatusScanner;
    PrintStream threadTwoStatusPrintStream;

    /**
     * @param savedInstanceState
     */
    Socket threadOneClientSocket;
    //    Scanner threadOneScanner;
    PrintStream threadOnePrintStream;

    Socket threadOneStatusSocket;
    //    Scanner threadOneStatusScanner;
    PrintStream threadOneStatusPrintStream;

    String prompt = "#" ;
    List<Thread> threadList ;
    String isTableCreated = "" ;
//    InputStream in;

    String[] roomNamesArray = {"Hall", "Kitchen" , "Master Bedroom" ,"Bedroom" ,"Bathroom" , "Balcony" ,"Pooja room" ,"Servent room","Office room","Conference room"} ;
    int[] roomOnOffArray = {1 ,1,1,0,1,0,0,0,0,0} ;
    Rooms room;

    public List<Rooms> roomList = new ArrayList<>();

    //Room home page image
    int[]  roomImageArray = { R.drawable.hall_room  , R.drawable.kitchen_room, R.drawable.master_bedroom , R.drawable.bedroom_room
            , R.drawable.bathroom_room , R.drawable.balcony_room , R.drawable.pooja_room  ,R.drawable.servent_room,R.drawable.hall_room,R.drawable.hall_room} ;

    //Add  Room images
    int[]  addRoomImages = { R.drawable.add_room_hall  , R.drawable.add_room_kitchen, R.drawable.add_room_master_bedroom ,
            R.drawable.add_room_bedroom
            , R.drawable.add_room_bathroom , R.drawable.add_room_bedroom , R.drawable.add_room_pooja_hall  , R.drawable.add_room_servant_room, R.drawable.add_room_hall, R.drawable.add_room_hall} ;


    // Button
    String[] buttonNameArray = { "LED" , "LED 02" , "Spotlight" ,"Spotlight 2", "Lamp" , "Tube" ,"Fan" , "Tube 2"} ;
    boolean[] buttonOnOffArray = { false  , false  , false , false , false , false , false, false } ;

    int[] buttonOnImagearray = { R.drawable.light_click , R.drawable.light_click , R.drawable.spotlight_clickt , R.drawable.spotlight_clickt,
            R.drawable.lamp_click ,R.drawable.tube_click ,R.drawable.fan_click, R.drawable.tube_click };
    int[] buttonOffImagearray = { R.drawable.light , R.drawable.light, R.drawable.spotlight , R.drawable.spotlight,
            R.drawable.lamp ,R.drawable.tube ,R.drawable.fan, R.drawable.tube };

//    String[] buttonNameArray = { "LED" , "LED 02" , "Spotlight" ,"Spotlight 2", "Lamp" , "Tube" ,"Fan" , "Tube 2","3 Pin" , "Air" , "Doorbell" ,"Music", "Set box" , "Table Fan" ,"TV" , "Water Purifier"} ;
//    boolean[] buttonOnOffArray = { false  , false  , false , false , false , false , false, false ,false  , false  , false , false , false , false , false, false } ;
//
//    int[] buttonOnImagearray = { R.drawable.light_click , R.drawable.light_click , R.drawable.spotlight_clickt , R.drawable.spotlight_clickt,
//          R.drawable.lamp_click ,R.drawable.tube_click ,R.drawable.fan_click, R.drawable.tube_click,R.drawable.threepin_click , R.drawable.air_click , R.drawable.doorball_click , R.drawable.music_click,
//            R.drawable.sep_box_click ,R.drawable.table_fan_click ,R.drawable.tv_click, R.drawable.water_pur_click };
//    int[] buttonOffImagearray = { R.drawable.light , R.drawable.light, R.drawable.spotlight , R.drawable.spotlight,
//           R.drawable.lamp ,R.drawable.tube ,R.drawable.fan, R.drawable.tube,R.drawable.threepin , R.drawable.air, R.drawable.doorball , R.drawable.music,
//            R.drawable.sep_box ,R.drawable.table_fan ,R.drawable.tv, R.drawable.water_pur };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        context = this;

        Log.e(TAG, "onCreate: " );

        ll_pBar = (LinearLayout) findViewById(R.id.ll_pBar);
        pBar = (ProgressBar) findViewById(R.id.pBar);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        // this will hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.splash_screen_linear_layout);
        final ImageView iv_splash_screen = (ImageView) findViewById(R.id.iv_splash_screen);

        ll_pBar.setVisibility(View.GONE);

        Glide.with(this).load(R.drawable.splash_screen).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    //Log.e("after ","set flash screen img");
                    linearLayout.setBackground(drawable);
                }
            }
        });

        if ( ! sharedPref.contains("isPreviousLogin")) {

            Intent veriy = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(veriy);
            SplashScreen.this.finish();
        }else {
            callNetworkSniffAsynTask();
        }
    }


    private void callNetworkSniffAsynTask() {

//        Log.e(TAG, "callNetworkSniffAsynTask: " );
//        getResponseFromServerSocketAndUpdateDb();


        // create all tables and boards

        // get data from sharedPrefrences
        getSharedPreferencesVariable() ;

        // checkString return true if string is null  , it will return false if string is not null
        if( checkString( isTableCreated ) ) {
//                Log.e("isTableCreated ",  isTableCreated );
            addRoom() ;
            roomList =  getAllRoom() ;
            for (Rooms room : roomList) {
                ActiveAndroid.beginTransaction() ;
                try {
                    Date currentTime = Calendar.getInstance().getTime();
                    SwitchBoard sb = new SwitchBoard() ;
                    sb.BoardName =  room.Name +" Board" ;
                    sb.BoardCreatedAt = currentTime.getTime() ;
                    sb.BoardUpdatedat = currentTime.getTime() ;
                    sb.RoomId = String.valueOf( room.getId() );
                    Long boardId = sb.save();
                    SwitchBoard switchBoard =  readSingleBoardById( boardId )  ;
                    Log.e(TAG, "onCreate: switchBoard "+switchBoard.BoardName +"  "+switchBoard.getId() );

                    for (int i = 0; i < buttonNameArray.length ; i++ ) {
//                            String[] buttonNameArray = { "LED" , "LED 02" , "Spotlight" ,"Spotlight 2", "Lamp" , "Tube" ,"Fan" , "Tube 2"} ;
                        String buttonName = buttonNameArray[i] ;
                        Date timestamp = Calendar.getInstance().getTime();
                        SwitchButton switchButton = new SwitchButton();
                        switchButton.SwitchButtonName = buttonName ;
                        switchButton.SwitchButtonCreatedAt = timestamp.getTime() ;
                        switchButton.SwitchButtonUpdatedat = timestamp.getTime() ;
                        switchButton.SwitchBoardId = boardId ;
                        switchButton.is_on = buttonOnOffArray[i] ;
                        switchButton.RoomId = room.getId() ;
                        switchButton.OnImage = buttonOnImagearray[i] ;
                        switchButton.OffImage = buttonOffImagearray[i] ;
                        switchButton.SwitchButtonPosition = i ;
                        switchButton.save() ;
                    }
                    ActiveAndroid.setTransactionSuccessful() ;
                } finally {
                    ActiveAndroid.endTransaction();
                }
            }
        }else {
            List<SwitchButton> allButtons = DatabaseQuery.getAllButtons();
            Log.e(TAG, "callNetworkSniffAsynTask: allButtons size: "+allButtons.size() );
            ActiveAndroid.beginTransaction();
            try {
                for (int i = 0 ; i< allButtons.size() ; i++) {
                    SwitchButton switchButton = allButtons.get(i);
                    switchButton.is_on = false;
                    switchButton.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            }
            finally {
                ActiveAndroid.endTransaction();
            }
        }

        List<SwitchBoard> allSwitchBoard = getAllSwitchBoard();
        for ( SwitchBoard board :  allSwitchBoard  ) {
            Log.e("Board id ip rid & name" , board.getId()  +"  "+board.IP + "  "+board.RoomId+" "+board.BoardName) ;
        }

        if( checkString( isModesCreated )) {
            createModes();
        }

        // make all IP null in SwitchBoard table
        makeAllIPNullInSwitchBoard();
        truncateTable_Mobile(Mobile.class);
        threadList = new ArrayList<>();

        int a=0;
        int b=2;

        for (int i = 0 ; i <= 84 ; i++ ) {
            Runnable r = new MyRunnable(a,b , this);
            threadList.add( new Thread(r)) ;
            threadList.get(i).start();

            a = b +1 ;
            b = a + 2;
        }

        Thread t11 = new Thread() {
            public void run() {
                try {
                    String TAG1 = " Thread11  ";
                    for ( Thread thread:
                            threadList) {
                        thread.join();
                    }
                    Log.e(TAG1, TAG1+ " after finished t1 to 10 thread ");
                    for (String ip :
                            ipadd) {
                        Log.e(TAG1, " ip is : " + ip);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            networkCallForIdAndStatus();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, " excep in Thread11" + e.getMessage());
                }
            }
        };
        t11.start();
    }

    private void networkCallForIdAndStatus() {
        Log.e(TAG, "networkCallForIdAndStatus initialy ipadd size:  "+ ipadd.size()+"    totalDeviceScanCompleted: "+ totalDeviceScanCompleted );
        Log.e(TAG, "networkCallForIdAndStatus ipadd for sending ID command:  "+ipadd );
        for (String ipAddress : ipadd ) {
//            if( ipAddress.equals("192.168.0.105") ) {
//                totalDeviceScanCompleted  = totalDeviceScanCompleted + 1;
//                totalIPScanCompleted.add("192.168.0.105");
//            }else{
                String requestString = "*ID?#";
                CommonAsynTaskNew asynTask = new CommonAsynTaskNew( context,requestString , my_object , MethodSelection.ID ,ipAddress);
                if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
                    asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    asynTask.execute();
                }
//            }
        }
    }


    private void makeAllIPNullInSwitchBoard() {
        Log.e(TAG, "makeAllIPNullInSwitchBoard: ");
        List<SwitchBoard> allBoard = getAllBoard();

        for (SwitchBoard switchBoard : allBoard) {
            switchBoard.BoardUpdatedat = Calendar.getInstance().getTime().getTime();
            switchBoard.IP = "";
            switchBoard.save();
        }
    }

    private void truncateTable_Mobile(Class<Mobile> mobileClass) {
        Log.e(TAG, "truncateTable_Mobile: ");
        TableInfo tableInfo = Cache.getTableInfo(mobileClass);
        ActiveAndroid.execSQL(
                String.format("DELETE FROM %s;",
                        tableInfo.getTableName()));
    }



    public void thread1(int startCount, int endCount) {
//        String TAG1 = " Thread1  ";

        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        connectionInfo = wm.getConnectionInfo();

        int ipAddress = connectionInfo.getIpAddress();
        myIp = Formatter.formatIpAddress(ipAddress);
//        Log.e(TAG1, "my device ipAddress: " + myIp);

        prefix = myIp.substring(0, myIp.lastIndexOf(".") + 1);

//        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
//        Log.e(TAG1, "doInBackground startTime : " + timeStamp + "");

        for (int i = startCount; i <= endCount; i++) {
            String testIp = prefix + String.valueOf(i);
            InetAddress address = null;
            try {
                address = InetAddress.getByName(testIp);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            boolean reachable = false;
            try {
                reachable = address.isReachable(ThreadUtil.timeout);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String hostName = address.getCanonicalHostName();

            if (reachable) {
//                Log.e(TAG1, "IP : " + testIp + " and SSID : " + connectionInfo.getSSID() + " is reachable ");
                ipadd.add(hostName);
            }
        }
//        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
//        Log.e(TAG1, " End-Time  : " + endTime + "");
    }

    private List<SwitchBoard> getAllBoard() {
        // get single SwitchBoard
//        return new Select().from(SwitchBoard.class).where("id = ?",   deviceId  ).executeSingle();

        return new Select().from(SwitchBoard.class).orderBy("id ASC").execute();
    }

    public void readyToGoV1MainActivity()
    {
        Log.e(TAG, "readyToGoV1MainActivity: ");

        if (sharedPref.contains("isPreviousLogin")) {
            Intent veriy = new Intent(SplashScreen.this, V1MainActivity.class);
            startActivity(veriy);
            SplashScreen.this.finish();
        } else {
            Intent veriy = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(veriy);
            SplashScreen.this.finish();
        }
    }

    private SwitchBoard getSingleBoardFromId(long deviceId) {
        return new Select().from(SwitchBoard.class).where("id = ?", deviceId).executeSingle();
    }

    List<SwitchButton> getAllButtonFromBoardId(Long boardId) {
//        return new Select().from(SwitchButton.class).where("is_on = ?", true ).where("id =?", roomId ).execute();
//        return new Select().from(SwitchButton.class).where("is_on = ? AND RoomId = ?", boardId).execute();
        return new Select().from(SwitchButton.class).where("SwitchBoardId = ?", boardId).execute();
    }

    @Override
    public void getResponse(String response, MethodSelection interface_method,String IP) {

        try {
            Log.e(TAG, "getResponse: "+response );
            switch ( interface_method ) {

                case ID :
                    this.idOfDevice(response,IP);
                    break;

                case STATUS:
                    this.statusResponse(response,IP );
                    break;
            }
        }catch (Exception e) {
            Log.e( "in getResponse" , e.getMessage() );
        }
    }

    private void statusResponse(String response2, String IP ) {
        Log.e(TAG, "statusResponse: "+IP +"  "+ response2 );
        String TAG1 = "statusResponse ";
        totalDeviceScanCompleted = totalDeviceScanCompleted + 1;
        totalIPScanCompleted.add(IP);

        if (response2 != null) {
            Log.e(TAG1, "STATUS: status response2 : " + response2);
            String data1 = response2.substring(response2.indexOf(",") + 1);
            String deviceIdFromResponse = data1.substring(0, data1.indexOf(","));
            String data2 = data1.substring(data1.indexOf(",") + 1);

            List<SwitchButton> allButtonFromBoardId = getAllButtonFromBoardId(Long.parseLong(deviceIdFromResponse));

            Log.e(TAG1, "STATUS:  data2 :  " + data2);
            String numberOnly = data2.replaceAll("[^0-9]", "");
            Log.e(TAG1, "STATUS: numberOnly is : " + numberOnly);

            ActiveAndroid.beginTransaction();
            try {
                for (int i =0 ; i< allButtonFromBoardId.size() ; i++) {
                    SwitchButton switchB = allButtonFromBoardId.get(i);
                    String statusValue = String.valueOf(numberOnly.charAt(i));
                    if (statusValue.equals("0")) {
                        switchB.is_on = true;
                    } else {
                        switchB.is_on = false;
                    }
                    switchB.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            }
            finally {
                ActiveAndroid.endTransaction();
            }
        } else {
            Log.e(TAG1, "STATUS: status response2 is null");
        }
        goToMainActivity();
    }

    private void idOfDevice(String response,String IP ) {
        Log.e(TAG, "testDeviceBeforeStatus: "+IP +"  "+ response );
        if( response != null ) {
            if( response.equals("Mobile")) {
                totalDeviceScanCompleted  = totalDeviceScanCompleted + 1;
                totalIPScanCompleted.add(IP);
            }else if( response.contains("#")) {
                myDeviceList.add(IP);
                String deviceId = response.replaceAll("[^0-9]", "");
                if( Integer.parseInt(deviceId) != 0 ) {
                    SwitchBoard switchBoard = getSingleBoardFromId(Long.parseLong(deviceId));

                    if (switchBoard != null) {
                        switchBoard.IP = IP;
                        switchBoard.save();
                    }
                    String statusRequest = "*STS," + switchBoard.getId() + "#";
                    CommonAsynTaskNew asynTask = new CommonAsynTaskNew(context, statusRequest, my_object, MethodSelection.STATUS, IP);
                    if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
                        asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        asynTask.execute();
                    }
                }
            }
        }else {
            Date currentTime = Calendar.getInstance().getTime();
            Mobile mobile = new Mobile();
            mobile.IP = IP;
            mobile.MobileName = "Mobile " + IP;
            mobile.CreatedAt = currentTime.getTime();
            mobile.Updatedat = currentTime.getTime();
            mobile.save();

            totalDeviceScanCompleted  = totalDeviceScanCompleted + 1;
            totalIPScanCompleted.add(IP);
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        if( ipadd.size() == totalDeviceScanCompleted ) {
            readyToGoV1MainActivity();
        }
        Log.e(TAG, "goToMainActivity ipadd.size():  "+ipadd.size() + "   AND  totalDeviceScanCompleted: "+totalDeviceScanCompleted );
        Log.e(TAG, "goToMainActivity: totalIPScanCompleted:  "+totalIPScanCompleted );
    }

    private void getSharedPreferencesVariable() {
        Log.e(TAG, "getSharedPreferencesVariable: " );
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        isTableCreated = sharedPref.getString("isTableCreated", "");
        homeName = sharedPref.getString("homeName", "");
        isModesCreated = sharedPref.getString("isModesCreated", "");
        email = sharedPref.getString("email", "");
    }


    // checkString return true if string is null  , it will return false if string is not null
    public boolean checkString( String str ) {
        return str != null && (  str.equals("null") ||  str.equals("NaN") || str.equals("undefined") || str.equals("") || str.isEmpty() );
    }

    private void addRoom() {
        ActiveAndroid.beginTransaction() ;
        try {
            for (int i = 0; i < roomNamesArray.length ; i++ ) {
                try {
                    Date currentTime = Calendar.getInstance().getTime() ;
                    Rooms rooms = new Rooms() ;
                    rooms.Name = roomNamesArray[i] ;
                    rooms.RoomHomePageImage = String.valueOf( roomImageArray[i]) ;
                    rooms.AddRoomImage = String.valueOf( addRoomImages[i] ) ;
                    rooms.RoomOnOff = roomOnOffArray[i] ;
                    rooms.CreatedAt = currentTime.getTime() ;
                    rooms.Updatedat = currentTime.getTime() ;
                    rooms.save() ;
                }catch ( Exception e ) {
                    Log.e("excep while addRoom",e.getMessage() );
                }
            }
            ActiveAndroid.setTransactionSuccessful() ;
        } finally {
            ActiveAndroid.endTransaction();
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("isTableCreated", "1" ) ;
        editor.commit() ;
    }

    // Read all room
    private List<Rooms> getAllRoom() {

//        Log.e("inside","getAllRoom");

        //  order by id
        return new Select().from(Rooms.class).orderBy("id ASC").execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();

    }

    SwitchButton getSingleButtonById(Long buttonId ) {
        return new Select().from(SwitchButton.class).where("id = ?", buttonId ).executeSingle();
    }

    SwitchBoard readSingleBoardById(Long boardId ) {
        return new Select().from(SwitchBoard.class).where("id = ?", boardId ).executeSingle();
    }

    // Read all SwitchBoard
    private List<SwitchBoard> getAllSwitchBoard() {
        Log.e("inside","getAllSwitchBoard");

        //  order by id
        return new Select().from(SwitchBoard.class).orderBy("id ASC").execute();
    }

    private void createModes() {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < modeNamesArray.length ; i++)
            { try {
                    Date currentTime = Calendar.getInstance().getTime();
                    Mode modes = new Mode();
                    modes.ModeName = modeNamesArray[i];
                    modes.CreatedAt = currentTime.getTime() ;
                    modes.Updatedat = currentTime.getTime() ;
                    modes.isOn = false ;
                    modes.ModeImage = modeImageArray[i] ;
                    modes.save() ;
                }catch ( Exception e ) {
                    Log.e("excep while getTime",e.getMessage() );
                }
            }
            ActiveAndroid.setTransactionSuccessful() ;
        } finally {
            ActiveAndroid.endTransaction();
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("isModesCreated", "1" ) ;
        editor.commit() ;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


//    private void thread10() {
//        String TAG1 = " Thread10  ";
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
////        Log.e(TAG1, "doInBackground startTime : " + timeStamp + "");
//
//        for (int i = ThreadUtil.threadTenStartCount; i <= ThreadUtil.threadTenEndCount; i++) {
//            String testIp = prefix + String.valueOf(i);
//            InetAddress address = null;
//            try {
//                address = InetAddress.getByName(testIp);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            }
//            boolean reachable = false;
//            try {
//
//                reachable = address.isReachable(ThreadUtil.timeout);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String hostName = address.getCanonicalHostName();
//
//            if (reachable) {
//                Log.e(TAG , TAG1  + "  IP : " + testIp + " and SSID : " + connectionInfo.getSSID() + "  is reachable! ");
//                ipadd.add(hostName);
//            }
//        }
//        String endTime = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
////        Log.e(TAG1 , " End-Time  : " + endTime + "");
//    }

//    private void thread9() {
//        String TAG1 = " Thread9  ";
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
////        Log.e(TAG1, "doInBackground startTime : " + timeStamp + "");
//
//        for (int i = ThreadUtil.threadNineStartCount; i <= ThreadUtil.threadNineEndCount; i++) {
//            String testIp = prefix + String.valueOf(i);
//            InetAddress address = null;
//            try {
//                address = InetAddress.getByName(testIp);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            }
//            boolean reachable = false;
//            try {
//                reachable = address.isReachable(ThreadUtil.timeout);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String hostName = address.getCanonicalHostName();
//
//            if (reachable) {
//                Log.e(TAG , TAG1  + "  IP : " + testIp + " and SSID : " + connectionInfo.getSSID() + "  is reachable! ");
//                ipadd.add(hostName);
//            }
//        }
//        String endTime = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
////        Log.e(TAG1, " End-Time  : " + endTime + "");
//    }

//    private void thread8() {
//        String TAG1 = " Thread8  ";
//
//
//        for (int i = ThreadUtil.threadEightStartCount; i <= ThreadUtil.threadEightEndCount; i++) {
//            String testIp = prefix + String.valueOf(i);
//            InetAddress address = null;
//            try {
//                address = InetAddress.getByName(testIp);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            }
//            boolean reachable = false;
//            try {
//                reachable = address.isReachable(ThreadUtil.timeout);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String hostName = address.getCanonicalHostName();
//
//            if (reachable) {
//                Log.e(TAG , TAG1  + "  IP : " + testIp + " and SSID : " + connectionInfo.getSSID() + "  is reachable! ");
//                ipadd.add(hostName);
//            }
//        }
//        String endTime = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
////        Log.e(TAG1, " End-Time  : " + endTime + "");
//    }

//    private void thread7() {
//        String TAG1 = " Thread7 ";
//
//
//        for (int i = ThreadUtil.threadSevenStartCount; i <= ThreadUtil.threadSevenEndCount; i++) {
//            String testIp = prefix + String.valueOf(i);
//            InetAddress address = null;
//            try {
//                address = InetAddress.getByName(testIp);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            }
//            boolean reachable = false;
//            try {
//                reachable = address.isReachable(ThreadUtil.timeout);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String hostName = address.getCanonicalHostName();
//
//            if (reachable) {
//                Log.e(TAG , TAG1  + "  IP : " + testIp + " and SSID : " + connectionInfo.getSSID() + "  is reachable! ");
//                ipadd.add(hostName);
//            }
//        }
//        String endTime = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
////        Log.e(TAG1, " End-Time  : " + endTime + "");
//    }
//
//    private void thread6() {
//        String TAG1 = " Thread6  ";
//
//
//        for (int i = ThreadUtil.threadSixStartCount; i <= ThreadUtil.threadSixEndCount; i++) {
//            String testIp = prefix + String.valueOf(i);
//            InetAddress address = null;
//            try {
//                address = InetAddress.getByName(testIp);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            }
//            boolean reachable = false;
//            try {
//                reachable = address.isReachable(ThreadUtil.timeout);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String hostName = address.getCanonicalHostName();
//
//            if (reachable) {
//                Log.e(TAG , TAG1  + "  IP : " + testIp + " and SSID : " + connectionInfo.getSSID() + "  is reachable! ");
//                ipadd.add(hostName);
//            }
//        }
//        String endTime = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
//    }
//
//    private void thread5() {
//        String TAG1 = " Thread5  ";
//
//
//        for (int i = ThreadUtil.threadFiveStartCount; i <= ThreadUtil.threadFiveEndCount; i++) {
//            String testIp = prefix + String.valueOf(i);
//            InetAddress address = null;
//            try {
//                address = InetAddress.getByName(testIp);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            }
//            boolean reachable = false;
//            try {
//                reachable = address.isReachable(ThreadUtil.timeout);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String hostName = address.getCanonicalHostName();
//
//            if (reachable) {
////                Log.e(TAG1, "Host: " + hostName + "(" + testIp + ") is reachable!");
//                Log.e(TAG , TAG1  + "  IP : " + testIp + " and SSID : " + connectionInfo.getSSID() + "  is reachable! ");
//                ipadd.add(hostName);
//            }
//        }
//        String endTime = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
////        Log.e(TAG1, " End-Time  : " +End-Time endTime + "");
//    }
//
//    private void thread4() {
//
//        String TAG1 = " Thread4  ";
//
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
////        Log.e(TAG1, "doInBackground startTime : " + timeStamp + "");
//
//        for (int i = ThreadUtil.threadFourStartCount; i <= ThreadUtil.threadFourEndCount; i++) {
//            String testIp = prefix + String.valueOf(i);
//            InetAddress address = null;
//            try {
//                address = InetAddress.getByName(testIp);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            }
//            boolean reachable = false;
//            try {
//                reachable = address.isReachable(ThreadUtil.timeout);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String hostName = address.getCanonicalHostName();
//
//            if (reachable) {
//                Log.e(TAG , TAG1  + "  IP : " + testIp + " and SSID : " + connectionInfo.getSSID() + "  is reachable! ");
//                ipadd.add(hostName);
//            }
//        }
//        String endTime = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
////        Log.e(TAG1, " End-Time  : " + endTime + "");
//    }
//
//
//    private void thread3() {
//        String TAG1 = " Thread3  ";
//
//        for (int i = ThreadUtil.threadThreeStartCount; i <= ThreadUtil.threadThreeEndCount; i++) {
//            String testIp = prefix + String.valueOf(i);
//            InetAddress address = null;
//            try {
//                address = InetAddress.getByName(testIp);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            }
//            boolean reachable = false;
//            try {
//                reachable = address.isReachable(ThreadUtil.timeout);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String hostName = address.getCanonicalHostName();
//
//            if (reachable) {
//                Log.e(TAG , TAG1  + "  IP : " + testIp + " and SSID : " + connectionInfo.getSSID() + "  is reachable! ");
//                ipadd.add(hostName);
//            }
//        }
//        String endTime = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
////        Log.e(TAG1, " End-Time  : " + endTime + "");
//    }
//
//    private void thread2() {
//        String TAG1 = " Thread2  ";
//
////        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
////        Log.e(TAG1, "doInBackground startTime : " + timeStamp + "");
//
//        for (int i = ThreadUtil.threadTwoStartCount; i <= ThreadUtil.threadTwoEndCount; i++) {
//            String testIp = prefix + String.valueOf(i);
//            InetAddress address = null;
//            try {
//                address = InetAddress.getByName(testIp);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            }
//            boolean reachable = false;
//            try {
//                reachable = address.isReachable(ThreadUtil.timeout);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String hostName = address.getCanonicalHostName();
//
//            if (reachable) {
//                Log.e(TAG , TAG1  + "  IP : " + testIp + " and SSID : " + connectionInfo.getSSID() + "  is reachable! ");
//                ipadd.add(hostName);
//            }
//        }
////        String endTime = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
////        Log.e(TAG1, " End-Time  : " + endTime + "");
//    }
//
//
//    private void threadOneForIdAndStatus() {
//
//
//        InputStream inputStream = null ;
//        InputStream threadOneStatusSocketInputStream = null  ;
//
//        String TAG1 = "threadOneForIdAndStatus";
//        int ipaddSize = ipadd.size();
//
//        if (ipaddSize > 0)
//        {
//
////            int halfCount = ipadd.size() / 2;
//
////            for (int count = 0; count <= halfCount; count++)
//            for (int count = 0; count < ipadd.size(); count++)
//            {
//                String ip = ipadd.get(count);
//
//                try
//                {
//                    Log.e(TAG1, " trying to connect " + ip );
//
//                    // way 2
//                    threadOneClientSocket = new Socket();
//                    threadOneClientSocket.connect(new InetSocketAddress(ip, Constants.PORT), ThreadUtil.timeout );
//
//                    inputStream = threadOneClientSocket.getInputStream();
//
//                    threadOnePrintStream = new PrintStream(threadOneClientSocket.getOutputStream());
//
//                    threadOnePrintStream.println("*ID?#");
//
//                    try
//                    {
//                        if( inputStream != null ) {
//                            try {
//                                inputStream.close();
//                            } catch (IOException e) {
//                                Log.e(TAG, "closeAllConnecton: Exception while closing inputStream "+e.getMessage() );
//                            }
//                        }
//
//                        if( threadOnePrintStream != null )
//                            threadOnePrintStream.close();
//
//                        if( threadOneClientSocket != null )
//                            try {
//                                threadOneClientSocket.close();
//                            } catch (IOException e1) {
//                                e1.printStackTrace();
//                            }
//
//                    }catch (Exception e )
//                    {
//                        Log.e(TAG, "threadOneForIdAndStatus: exception while closing" );
//                    }
//
////                    threadOnePrintStream.flush();
//
//                    Log.e(TAG1, " after printing :  *ID?#");
//                    String response = null ;
//
//                    response = readData(inputStream);
//
//                    if (threadOnePrintStream != null)
//                        threadOnePrintStream.close();
//
//                    if (inputStream != null) {
//                        try {
//                            inputStream.close();
//                        } catch (IOException e) {
//                            Log.e(TAG, "threadOneForIdAndStatus: exception while closing inputStream "+e.getMessage() );
//                            e.printStackTrace();
//                        }
//                    }
//
//                    try {
//                        if (threadOneClientSocket != null)
//                            threadOneClientSocket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//
//
//
//
//                    Log.e(TAG1, " ID Response : " + response);
//
//                    String deviceId = response.replaceAll("[^0-9]", "");
//                    Log.e(TAG1, " deviceId: " + deviceId);
//
//
//                    if( deviceId != null )
//                    {
//                        if( Integer.parseInt(deviceId) != 0 )
//                        {
//                            SwitchBoard switchBoard = getSingleBoardFromId(Long.parseLong(deviceId));
//
//                            if (switchBoard != null)
//                            {
////                                Log.e(TAG1, "STATUS: switchBoard ID Name and RoomId : " + switchBoard.getId() + " " + switchBoard.BoardName + "  " + switchBoard.RoomId);
////                                Log.e(TAG1, "STATUS: 1");
//                                switchBoard.IP = ip;
////                                Log.e(TAG1, "STATUS: 2");
//                                switchBoard.save();
////                                Log.e(TAG1, "STATUS: 3");
//
//                                // way 1 for creating socket
////                        threadOneStatusSocket = new Socket(ip, Constants.PORT);
////                        threadOneStatusSocket.setSoTimeout(ThreadUtil.timeout);
//
//                                Log.e(TAG1, "STATUS: creating socket for status with ip: " + ip);
//                                // way 2 for creating socket
////                                threadOneStatusSocket = new Socket();
////                                threadOneStatusSocket.connect(new InetSocketAddress(ip, Constants.PORT), ThreadUtil.timeout );
////                                threadOneStatusSocket.setSoTimeout(ThreadUtil.timeout);
//
//                                threadOneStatusSocket = new Socket(ip,8888);
//
//                                Log.e(TAG1, "STATUS : socket is created with ip: " + ip);
//
////                                threadOneStatusScanner = new Scanner(threadOneStatusSocket.getInputStream());
//                                threadOneStatusSocketInputStream = threadOneStatusSocket.getInputStream();
//
//                                threadOneStatusPrintStream = new PrintStream(threadOneStatusSocket.getOutputStream());
//
//                                String  statusRequest =    "*STS," + switchBoard.getId() + "#" ;
//                                Log.e(TAG1, "STATUS: sending status request ");
//                                threadOneStatusPrintStream.println(statusRequest);
//                                threadOneStatusPrintStream.println(statusRequest);
//
//
//                                Log.e(TAG1, "STATUS: after sending status request: "+"*STS," + switchBoard.getId() + "#");
//
//                                /** response2 contain data like this : "*REC,1,0,1,2,3,4,5,6,7#"
//                                 this 0 to 7 are switch number
//
//                                 1 is  this is device id   **/
//
//                                try
//                                {
////                                    String response2 = threadOneStatusScanner.nextLine();
//                                    String response2 = readData(threadOneStatusSocketInputStream);
//
//                                    if (response2 != null) {
//                                        Log.e(TAG1, "STATUS: status response2 : " + response2);
//
//                                        String data1 = response2.substring(response2.indexOf(",") + 1);
//                                        String deviceIdFromResponse = data1.substring(0, data1.indexOf(","));
//                                        String data2 = data1.substring(data1.indexOf(",") + 1);
//
//                                        List<SwitchButton> allButtonFromBoardId = getAllButtonFromBoardId(Long.parseLong(deviceIdFromResponse));
//
//                                        Log.e(TAG1, "STATUS:  data2 :  " + data2);
//                                        String numberOnly = data2.replaceAll("[^0-9]", "");
//                                        Log.e(TAG1, "STATUS: numberOnly is : " + numberOnly);
//
//                                        ActiveAndroid.beginTransaction();
//                                        try {
//                                            for (int i =0 ; i< allButtonFromBoardId.size() ; i++)
//                                            {
//                                                SwitchButton switchB = allButtonFromBoardId.get(i);
//                                                String statusValue = String.valueOf(numberOnly.charAt(i));
//                                                if (statusValue.equals("0"))
//                                                {
//                                                    switchB.is_on = true;
//                                                } else {
//                                                    switchB.is_on = false;
//                                                }
//                                                switchB.save();
//                                            }
//
//                                            ActiveAndroid.setTransactionSuccessful();
//                                        }
//                                        finally {
//                                            ActiveAndroid.endTransaction();
//                                        }
//
//                                    } else {
//                                        Log.e(TAG1, "STATUS: status response2 is null");
//                                    }
//
//                                } catch (Exception e) {
//                                    Log.e(TAG1, "STATUS: exception while reading status response: "+e.getMessage());
//
//                                }
//
//                            } else {
//
//                                Log.e(TAG1, " switch board is not present with deviceId " + deviceId);
//                            }
//                        }else
//                        {
//                            Log.e(TAG, "threadOneForIdAndStatus deviceId is: "+deviceId);
//                        }
//                    }
//                } catch (Exception e) {
//                    Log.e(TAG1, "exception in doInBackground  IP : " + ip + "  " + e.getMessage());
//                    Log.e(TAG1, " This IP:"+ ip +" is of mobile ");
//                    Date currentTime = Calendar.getInstance().getTime();
//                    Mobile mobile = new Mobile();
//                    mobile.IP = ip;
//                    mobile.MobileName = "Mobile " + ip;
//                    mobile.CreatedAt = currentTime.getTime();
//                    mobile.Updatedat = currentTime.getTime();
//                    mobile.save();
//                } finally {
//
//                    Log.e(TAG1, " finally block ");
//                    if (threadOnePrintStream != null)
//                        threadOnePrintStream.close();
//
//                    if (inputStream != null) {
//                        try {
//                            inputStream.close();
//                        } catch (IOException e) {
//                            Log.e(TAG, "threadOneForIdAndStatus: exception while closing inputStream "+e.getMessage() );
//                            e.printStackTrace();
//                        }
//                    }
//
//                    try {
//                        if (threadOneClientSocket != null)
//                            threadOneClientSocket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (threadOneStatusPrintStream != null)
//                        threadOneStatusPrintStream.close();
//
//                    if (threadOneStatusSocketInputStream != null) {
//                        try {
//                            threadOneStatusSocketInputStream.close();
//                        } catch (IOException e) {
//                            Log.e(TAG, "threadOneForIdAndStatus: exception while closing threadOneStatusSocketInputStream "+e.getMessage() );
////                            e.printStackTrace();
//                        }
//                    }
//                    try {
//
//                        if (threadOneStatusSocket != null)
//                            threadOneStatusSocket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } else {
//            Log.e(TAG1, " ipadd list size is 0 ");
//        }
//    }




//    private void threadTwoForIdAndStatus() {
//
//        String TAG1 = "ThreadTwoForIdAndStatus";
//        int ipaddSize = ipadd.size();
//
//        InputStream inputStream1 = null  ;
//        InputStream threadTwoStatusSocketInputStream = null;
//
//        if (ipaddSize > 0) {
//            int halfCount = ipadd.size() / 2;
//
//            for (int count = halfCount + 1; count < ipadd.size(); count++) {
//                String ip = ipadd.get(count);
//
//                try {
//                    Log.e(TAG1, " trying to connect " + ip);
//
//                    // way 2
//                    threadTwoClientSocket = new Socket();
//                    threadTwoClientSocket.connect(new InetSocketAddress(ip, Constants.PORT), ThreadUtil.timeout );
////                    threadTwoClientSocket.setSoTimeout(ThreadUtil.timeout);
//
//                    Log.e(TAG, " successful connect with ip: " + ip );
//
//
////                    threadTwoScanner = new Scanner(threadTwoClientSocket.getInputStream());
//                    inputStream1 = threadTwoClientSocket.getInputStream();
//
//                    threadTwoPrintStream = new PrintStream(threadTwoClientSocket.getOutputStream());
//                    Log.e(TAG, TAG1+" printStream created ");
//
//                    threadTwoPrintStream.println("*ID?#");
//                    Log.e(TAG, TAG1+" after printing :  *ID?# ");
//
////                    String response = threadTwoScanner.nextLine();
//                    String response = readData(inputStream1);
//                    Log.e(TAG, TAG1+" ID Request Response is  " + response);
//
//                    String deviceId = response.replaceAll("[^0-9]", "");
////                    Log.e(TAG1, " deviceId  " + deviceId);
//
//                    if( deviceId != null )
//                    {
//                        if( Integer.parseInt(deviceId) != 0)
//                        {
//                            SwitchBoard switchBoard = getSingleBoardFromId(Long.parseLong(deviceId));
//
//                            if (switchBoard != null) {
//                                Log.e(TAG1, " switchBoard ID Name and RoomId  " + switchBoard.getId() + " " + switchBoard.BoardName + "  " + switchBoard.RoomId);
//
//                                switchBoard.IP = ip;
//                                switchBoard.save();
//
//                                // way 1 for creating socket
////                        threadTwoStatusSocket = new Socket(ip, Constants.PORT);
////                        threadTwoStatusSocket.setSoTimeout(ThreadUtil.timeout);
//
//                                // way 2 for creating socket
//                                threadTwoStatusSocket = new Socket();
//                                threadTwoStatusSocket.connect(new InetSocketAddress(ip, Constants.PORT), ThreadUtil.timeout );
////                                threadTwoStatusSocket.setSoTimeout(ThreadUtil.timeout);
//
////                                threadTwoStatusScanner = new Scanner(threadTwoStatusSocket.getInputStream());
//                                 threadTwoStatusSocketInputStream = threadTwoStatusSocket.getInputStream();
//
//                                threadTwoStatusPrintStream = new PrintStream(threadTwoStatusSocket.getOutputStream());
//                                threadTwoStatusPrintStream.println("*STS," + switchBoard.getId() + "#");
//
//                                /** response2 contain data like this : "*REC,1,0,1,2,3,4,5,6,7#"
//                                 this 0 to 7 are switch number
//                                 1 is  this is device id   **/
//
//                                try {
//
////                                    String response2 = threadTwoStatusScanner.nextLine();
//                                    String response2 = readData(threadTwoStatusSocketInputStream);
//
//                                    if (response2 != null) {
//                                        Log.e(TAG1, " status response2 : " + response2);
//
//                                        String data1 = response2.substring(response2.indexOf(",") + 1);
//                                        String deviceIdFromResponse = data1.substring(0, data1.indexOf(","));
//                                        String data2 = data1.substring(data1.indexOf(",") + 1);
//
//
//                                        Log.e(TAG1, "  deviceIdFromResponse :  " + deviceIdFromResponse);
//                                        List<SwitchButton> allButtonFromBoardId = getAllButtonFromBoardId(Long.parseLong(deviceIdFromResponse));
//
////                                for (SwitchButton sB : allButtonFromBoardId )
////                                {
////                                    Log.e(TAG1, "  allButtonFroBoardIdBefore :  " +"Board Id : "+ sB.SwitchBoardId +"ButtonName : "+ sB.SwitchButtonName +"Button status"+ sB.is_on);
////                                }
//                                        Log.e(TAG1, "  data2 :  " + data2);
//                                        String numberOnly = data2.replaceAll("[^0-9]", "");
//                                        Log.e(TAG1, " numberOnly is : " + numberOnly);
//
//                                        ActiveAndroid.beginTransaction();
//                                        try {
//                                            for (int i =0 ; i< allButtonFromBoardId.size() ; i++)
//                                            {
//                                                SwitchButton switchB = allButtonFromBoardId.get(i);
//                                                String statusValue = String.valueOf(numberOnly.charAt(i));
//                                                if (statusValue.equals("0"))
//                                                {
//                                                    switchB.is_on = true;
//                                                } else {
//                                                    switchB.is_on = false;
//                                                }
//                                                switchB.save();
//                                            }
//
//                                            ActiveAndroid.setTransactionSuccessful();
//                                        }
//                                        finally {
//                                            ActiveAndroid.endTransaction();
//                                        }
//
//
////                                for (int i =0 ; i< allButtonFromBoardId.size() ; i++)
////                                {
////                                    SwitchButton switchB = allButtonFromBoardId.get(i);
////                                    ActiveAndroid.beginTransaction();
////                                    try {
////
////                                        String statusValue = String.valueOf(numberOnly.charAt(i));
////                                        if (statusValue.equals("0"))
////                                        {
////                                            switchB.is_on = true;
////                                        } else {
////                                            switchB.is_on = false;
////                                        }
////                                        switchB.save();
////                                        ActiveAndroid.setTransactionSuccessful();
////                                    } finally {
////                                        ActiveAndroid.endTransaction();
////                                    }
////                                }
////                                ActiveAndroid.beginTransaction();
//
////                                for (int i = 0; i < allButtonFromBoardId.size(); i++)
////                                {
////                                    SwitchButton switchButton = allButtonFromBoardId.get(i);
////                                    String statusValue = String.valueOf(numberOnly.charAt(i));
////                                    if (statusValue.equals("0")) {
////                                        switchButton.is_on = true;
////                                    } else {
////                                        switchButton.is_on = false;
////                                    }
////                                    switchButton.save();
////                                }
//
//                                        List<SwitchButton> allButtonFroBoardIdAfter = getAllButtonFromBoardId(Long.parseLong(deviceIdFromResponse));
//
//                                        for (SwitchButton sB : allButtonFroBoardIdAfter )
//                                        {
//                                            Log.e(TAG1, "  ALL BUTTON :  " +"Board Id : "+ sB.SwitchBoardId +"ButtonName: "+ sB.SwitchButtonName +" Button status: "+ sB.is_on);
//                                        }
//
//
//                                    } else {
//                                        Log.e(TAG1, " status response2 is null ");
//                                    }
//                                } catch (Exception e) {
//
//                                } finally {
//
//                                    if (threadTwoStatusPrintStream != null)
//                                        threadTwoStatusPrintStream.close();
//
//                                    if (threadTwoStatusSocketInputStream != null)
//                                        threadTwoStatusSocketInputStream.close();
//
//                                    if (threadTwoStatusSocket != null)
//                                        threadTwoStatusSocket.close();
//                                }
////                                inputStream.close();
////                                threadTwoStatusPrintStream.close();
////                                threadTwoStatusSocket.close();
//                            } else {
//                                Log.e(TAG1, " switch board is not present with deviceId " + deviceId);
//                            }
//
//                            threadTwoPrintStream.close();
//                            inputStream1.close();
//                            threadTwoClientSocket.close();
//                        }else
//                        {
//                            Log.e(TAG, "threadTwoForIdAndStatus device id is: "+deviceId );
////                            Log.e(TAG1, " switch board is not present with deviceId " + deviceId);
//                        }
//                    }
//                } catch (Exception e) {
//                    Log.e(TAG1, "exception in doInBackground  IP : " + ip + "  " + e.getMessage());
//                    Log.e(TAG1, " This is Mobile ");
//                    Date currentTime = Calendar.getInstance().getTime();
//                    Mobile mobile = new Mobile();
//                    mobile.IP = ip;
//                    mobile.MobileName = "Mobile " + ip;
//                    mobile.CreatedAt = currentTime.getTime();
//                    mobile.Updatedat = currentTime.getTime();
//                    mobile.save();
//                } finally {
//                    Log.e(TAG1, " finally block ");
//
//                    if (threadTwoPrintStream != null)
//                        threadTwoPrintStream.close();
//
//                    if (inputStream1 != null) {
//                        try {
//                            inputStream1.close();
//                        } catch (IOException e) {
//                            Log.e(TAG, "threadTwoForIdAndStatus: exception while closing inputStream1 "+e.getMessage() );
//                            e.printStackTrace();
//                        }
//                    }
//
//                    try {
//                        if (threadTwoClientSocket != null)
//                            threadTwoClientSocket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (threadTwoStatusPrintStream != null)
//                        threadTwoStatusPrintStream.close();
//
//                    if (threadTwoStatusSocketInputStream != null) {
//                        try {
//                            threadTwoStatusSocketInputStream.close();
//                        } catch (IOException e) {
//                            Log.e(TAG, "threadTwoForIdAndStatus: Exception while closing threadTwoStatusSocketInputStream "+e.getMessage() );
//                            e.printStackTrace();
//                        }
//                    }
//
//                    try {
//                        if (threadTwoStatusSocket != null)
//                            threadTwoStatusSocket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } else {
//            Log.e(TAG1, "ipAdd list size is 0 ");
//        }
//    }
}