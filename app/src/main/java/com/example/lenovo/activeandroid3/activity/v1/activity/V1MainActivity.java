package com.example.lenovo.activeandroid3.activity.v1.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.LoginActivity;
import com.example.lenovo.activeandroid3.activity.ResponseInterfaceNew;
import com.example.lenovo.activeandroid3.activity.SplashScreen;
import com.example.lenovo.activeandroid3.activity.v1.fragment.ModeFragments;
import com.example.lenovo.activeandroid3.activity.v1.fragment.NotificationFragment;
import com.example.lenovo.activeandroid3.activity.v1.fragment.RoomFragment;
import com.example.lenovo.activeandroid3.activity.v1.fragment.SettingFragment;
import com.example.lenovo.activeandroid3.activity.v1.utils.MethodSelection;
import com.example.lenovo.activeandroid3.activity.v1.utils.ThreadUtil;
import com.example.lenovo.activeandroid3.asyntask.CommonAsynTaskNew;
import com.example.lenovo.activeandroid3.dbModel.ModesActivityDbModel;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.model.Mobile;
import com.example.lenovo.activeandroid3.model.Mode;
import com.example.lenovo.activeandroid3.model.ModeOnOf;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.model.SwitchBoard;
import com.example.lenovo.activeandroid3.model.SwitchButton;
import com.example.lenovo.activeandroid3.util.Constants;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class V1MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ResponseInterfaceNew {

    private SectionsPagerAdapter mSectionsPagerAdapter ;
    private ViewPager mViewPager ;
    Context context ;
    TabLayout tabLayout ;
    NavigationView navigationView ;
    Toolbar toolbar ;
    View nav_header ;
    static FrameLayout frameLayout ;
    V1MainActivity my_object = this ;


    SharedPreferences  sharedPref ;
    String email , homeName ;

    List<Mobile> allMobile;

    //Room home page image
    int[]  roomImageArray = { R.drawable.hall_room  , R.drawable.kitchen_room, R.drawable.master_bedroom , R.drawable.bedroom_room
            , R.drawable.bathroom_room , R.drawable.balcony_room , R.drawable.pooja_room  ,R.drawable.servent_room,R.drawable.hall_room ,R.drawable.hall_room } ;

    //Add  Room images
    int[]  addRoomImages = { R.drawable.add_room_hall  , R.drawable.add_room_kitchen, R.drawable.add_room_master_bedroom ,
            R.drawable.add_room_bedroom
            , R.drawable.add_room_bathroom , R.drawable.add_room_bedroom , R.drawable.add_room_pooja_hall  , R.drawable.add_room_servant_room,R.drawable.add_room_hall,R.drawable.add_room_hall} ;

    String isTableCreated = "" ;
    public List<Rooms> roomList = new ArrayList<>();

    String[] roomNamesArray = {"Hall", "Kitchen" , "Master Bedroom" ,"Bedroom" ,"Bathroom" , "Balcony" ,"Pooja room" ,"Servent room","Office room","Conference room"} ;
    int[] roomOnOffArray = {1 ,1,1,0,1,0,0,0,0,0} ;
    Rooms room;

    //MODES
    String[] modeNamesArray = {"Morning", "Afternoon" ,"Evening","Night"};
    int[]  modeImageArray = { R.drawable.mode_morning  , R.drawable.mode_afternoon, R.drawable.mode_evening , R.drawable.mode_night} ;
    String isModesCreated ;

    // Button
//    String[] buttonNameArray = { "LED" , "LED 02" , "Spotlight" ,"Spotlight 2", "Lamp" , "Tube" ,"Fan" , "Tube 2"} ;
//    boolean[] buttonOnOffArray = { false  , false  , false , false , false , false , false, false } ;
//
//    int[] buttonOnImagearray = { R.drawable.light_click , R.drawable.light_click , R.drawable.spotlight_clickt , R.drawable.spotlight_clickt,
//            R.drawable.lamp_click ,R.drawable.tube_click ,R.drawable.fan_click, R.drawable.tube_click };
//    int[] buttonOffImagearray = { R.drawable.light , R.drawable.light, R.drawable.spotlight , R.drawable.spotlight,
//            R.drawable.lamp ,R.drawable.tube ,R.drawable.fan, R.drawable.tube };

//    String[] buttonNameArray = { "3 Pin" , "Air" , "Doorbell" ,"Music", "Set box" , "Table Fan" ,"TV" , "Water Purifier"} ;
//    boolean[] buttonOnOffArray = { false  , false  , false , false , false , false , false, false } ;
//
//    int[] buttonOnImagearray = { R.drawable.threepin_click , R.drawable.air_click , R.drawable.doorball_click , R.drawable.music_click,
//            R.drawable.sep_box_click ,R.drawable.table_fan_click ,R.drawable.tv_click, R.drawable.water_pur_click };
//    int[] buttonOffImagearray = { R.drawable.threepin , R.drawable.air, R.drawable.doorball , R.drawable.music,
//            R.drawable.sep_box ,R.drawable.table_fan ,R.drawable.tv, R.drawable.water_pur };


    String[] buttonNameArray = { "LED" , "LED 02" , "Spotlight" ,"Spotlight 2", "Lamp" , "Tube" ,"Fan" , "Tube 2","3 Pin" , "Air" , "Doorbell" ,"Music", "Set box" , "Table Fan" ,"TV" , "Water Purifier"} ;
    boolean[] buttonOnOffArray = { false  , false  , false , false , false , false , false, false ,false  , false  , false , false , false , false , false, false } ;

    int[] buttonOnImagearray = { R.drawable.light_click , R.drawable.light_click , R.drawable.spotlight_clickt , R.drawable.spotlight_clickt,
            R.drawable.lamp_click ,R.drawable.tube_click ,R.drawable.fan_click, R.drawable.tube_click,R.drawable.threepin_click , R.drawable.air_click , R.drawable.doorball_click , R.drawable.music_click,
            R.drawable.sep_box_click ,R.drawable.table_fan_click ,R.drawable.tv_click, R.drawable.water_pur_click };
    int[] buttonOffImagearray = { R.drawable.light , R.drawable.light, R.drawable.spotlight , R.drawable.spotlight,
            R.drawable.lamp ,R.drawable.tube ,R.drawable.fan, R.drawable.tube,R.drawable.threepin , R.drawable.air, R.drawable.doorball , R.drawable.music,
            R.drawable.sep_box ,R.drawable.table_fan ,R.drawable.tv, R.drawable.water_pur };




    String TAG = "V1MainActivity" ;
    DrawerLayout drawer ;

    public static final int SERVERPORT = 9999;
    Handler uiHandler ;
    Thread thread1 ;


    /**
     *   server socket objets
     */
    ServerSocket serverSocket = null;
    Socket socket = null;
    PrintStream printStream = null ;
    Scanner scanner1 = null ;




    // for mode flow
    //----------------------------------------
//    Socket threadOneClientSocket;
//    Scanner threadOneScanner;
//    PrintStream threadOnePrintStream;

    Socket threadOneStatusSocket;
    Scanner threadOneStatusScanner;
    PrintStream threadOneStatusPrintStream;

//    List<String> switchBoardIpList = null ;


//    Socket threadTwoClientSocket;
//    Scanner threadTwoScanner;
//    PrintStream threadTwoPrintStream;
//
//    Socket threadTwoStatusSocket;
//    Scanner threadTwoStatusScanner;
//    PrintStream threadTwoStatusPrintStream;

    // isRoomOpenFlag is true then only update the arrayList object of RoomInsideDialogAdapter class.
    boolean isRoomOpenFlag = false ;


    Socket   clientSocket = null;
    PrintStream printStreamClientSocket ;

    String roomId;
    String roomPosition;
    String switchPosition;
    FloatingActionButton fabAllBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_main);

        /**
         *    network call must be in asyntask and set this strictmode policy is required for it
         */
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        toolbar = ( Toolbar ) findViewById( R.id.activity_v1_main_toolbar ) ;
        frameLayout = ( FrameLayout )findViewById( R.id.activity_v1_main_frame);
        fabAllBtn = (FloatingActionButton) findViewById(R.id.fab_all_btn);
        fabAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getAllButtonFromDbAndOffThem() ;

            }
        });

        setSupportActionBar( toolbar ) ;
        context = this ;
        room = new Rooms() ;

        try
        {
            toolbar.getTitleMarginStart();

            getSupportActionBar().setTitle(" My Home") ;

            // get data from sharedPrefrences
            getSharedPreferencesVariable() ;

            createServerSocket();

            // checkString return true if string is null  , it will return false if string is not null
            if( checkString( isTableCreated ) )
            {
//                Log.e("isTableCreated ",  isTableCreated );
                addRoom() ;
                roomList =  getAllRoom() ;

                for (Rooms room : roomList)
                {
                    ActiveAndroid.beginTransaction() ;
                    try
                    {
                        Date currentTime = Calendar.getInstance().getTime();
                        SwitchBoard sb = new SwitchBoard() ;
                        sb.BoardName =  room.Name +" Board" ;
                        sb.BoardCreatedAt = currentTime.getTime() ;
                        sb.BoardUpdatedat = currentTime.getTime() ;
                        sb.RoomId = String.valueOf( room.getId() );
                        Long boardId = sb.save();
                        SwitchBoard switchBoard =  readSingleBoardById( boardId )  ;
                        Log.e(TAG, "onCreate: switchBoard "+switchBoard.BoardName +"  "+switchBoard.getId() );

                        for (int i = 0; i < buttonNameArray.length ; i++ )
                        {
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
            }else{

                Log.e("00000","pppppp");
            }

            List<SwitchBoard> allSwitchBoard = getAllSwitchBoard();
            for ( SwitchBoard board :  allSwitchBoard  )
            {
                Log.e("Board id ip rid & name" , board.getId()  +"  "+board.IP + "  "+board.RoomId+" "+board.BoardName) ;
            }

            if( checkString( isModesCreated ))
            {
                createModes();
            }

//            getAllModeAndMakeOffThem();
            updateModeStatusAccordingToModeConfiguration();




            drawer = (DrawerLayout) findViewById(R.id.activity_v1_main_drawer_layout );
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = ( NavigationView ) findViewById( R.id.activity_v1_main_drawer_layout_nav_view );
            navigationView.setNavigationItemSelectedListener( this );
            navigationView.getMenu().getItem(0).setChecked(true);

            // this will set icon and text to navigation drawer.
            setIconToNavigationDrawerElements();

            //hideItemNavigationDrawerItem();
            nav_header = navigationView.getHeaderView(0);
            loadHeader();


            TextView  navNotification =( TextView ) MenuItemCompat.getActionView( navigationView.getMenu().
                    findItem( R.id.nav_notification ) ) ;
            navNotification.setGravity(Gravity.CENTER_VERTICAL);
            navNotification.setTypeface(null, Typeface.BOLD);
            navNotification.setTextSize(15);
            navNotification.setTextColor(ContextCompat.getColor(context, R.color.Open));
            navNotification.setText("" + 5);


//            nav_notification
//                    nav_settings
//            nav_help
//                    nav_share
//            nav_logout

            navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item)
                {
                    Log.e("in","setNavigationItemSelectedListener");
                    drawer.closeDrawers();
                    final int id = item.getItemId();
                    Log.e("id ",id+"");

                    if (id == R.id.nav_home)
                    {
                        mViewPager.setVisibility( View.VISIBLE ) ;
                        tabLayout.setVisibility( View.VISIBLE ) ;
                        frameLayout.setVisibility( View.GONE ) ;
                        toolbar.setTitle(" My Home");

                        setAllItemOfNavigationViewToFalse();
                        navigationView.getMenu().getItem(0).setChecked(true);
                        setPagerFragment( 0 ) ;

                    }else if(id ==  R.id.nav_logout ){

                        Intent veriy = new Intent(V1MainActivity.this, LoginActivity.class);
                        startActivity(veriy);
                        V1MainActivity.this.finish();


                    }else if(id ==  R.id.nav_share ){

//                        Intent veriy = new Intent(V1MainActivity.this, LoginActivity.class);
//                        startActivity(veriy);
//                        V1MainActivity.this.finish();


                    }else if(id ==  R.id.nav_help ){

//                        Intent veriy = new Intent(V1MainActivity.this, LoginActivity.class);
//                        startActivity(veriy);
//                        V1MainActivity.this.finish();


                    } else if (id == R.id.nav_settings)
                    {
                        Log.e("in","nav_settings");
                        Log.e("isChecked ", item.isChecked() +"" )  ;

                        if( ! item.isChecked() )
                        {

//                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//                            LayoutInflater inflater;
//                            inflater = LayoutInflater.from(context);
//                            final View dialogView = inflater.inflate(R.layout.dialog_verification_for_setting, null);
//                            dialogBuilder.setView(dialogView);
//                            final AlertDialog alertDialog_verification_for_setting = dialogBuilder.create();
//                            alertDialog_verification_for_setting.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
//                            alertDialog_verification_for_setting.show();
//
//                            TextView tv_close = ( TextView)alertDialog_verification_for_setting.findViewById(R.id.tv_close );
//                            final EditText et_password = ( EditText )alertDialog_verification_for_setting.findViewById( R.id.et_password );
//                            Button btn_submit = (Button )alertDialog_verification_for_setting.findViewById( R.id.btn_submit );
//
//
//                            tv_close.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//
//                                    alertDialog_verification_for_setting.cancel();
//                                }
//                            });
//
//                            btn_submit.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//
//                                    boolean cancel = false ;
//                                    View focusView = null ;
//
//
//                                    if (TextUtils.isEmpty( et_password.getText().toString()  ) )
//                                    {
//                                        et_password.setError("please Enter password") ;
//                                        focusView = et_password ;
//                                        cancel = true ;
//                                    }
//
//                                    if (cancel ) {
//                                        focusView.requestFocus() ;
//                                    }else
//                                    {
//                                        String  passwordShared = sharedPref.getString("password", "");
//                                        if( ! passwordShared.equals( et_password.getText().toString() ))
//                                        {
//                                            et_password.setError("incorrect password");
//                                            et_password.requestFocus();
//                                        }else {
//
//                                            alertDialog_verification_for_setting.cancel();
                            mViewPager.setVisibility(View.GONE);
                            tabLayout.setVisibility(View.GONE);
                            frameLayout.setVisibility(View.VISIBLE);

                            setAllItemOfNavigationViewToFalse();

                            navigationView.getMenu().getItem(2).setChecked(true);

                            openSettingFragment(new SettingFragment() , " Setting ") ;
//                                        }
//                                    }
//                                }
//                            });
                        }
                    } else if ( id == R.id.nav_notification)
                    {
                        Log.e("in"," notification ");
                        Log.e("isChecked ", item.isChecked() +"" )  ;

                        mViewPager.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                        setAllItemOfNavigationViewToFalse();

                        navigationView.getMenu().getItem(1).setChecked(true);

                        openSettingFragment(new NotificationFragment() , " Notification ") ;
                    }
                    return false ;
                }
            }) ;

            setViewPagerAndTabLayout();

            mViewPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener()
            {
                @Override
                public void onPageSelected( int position )
                {

                    Log.e("in" ,"addOnPageChangeListener") ;
/**
 *   This line is used make navigation drawer item chackable when we select item from tab or from navigation drawer itself according to position
 */
                    navigationView.getMenu().getItem( position ).setChecked( true );

//                    String[] tabsTitles={"MyHome","MyHome","MyHome"};
                    toolbar.setTitle(" "+ "My Home") ;

                    if(  position == 0  )
                    {
                        Log.e("in" ,"position " + 0 ) ;
                        navigationView.getMenu().getItem(1).setChecked( false ) ;
                        Log.e("title ", navigationView.getMenu().getItem(1).getTitle().toString()) ;
                        RoomFragment   roomFragment =
                                ( RoomFragment ) mViewPager.getAdapter()
                                        .instantiateItem( mViewPager, mViewPager.getCurrentItem() );
                        roomFragment.makeAdapterListNull();
                        roomFragment.onLoadData();
                    }
                    if(  position == 1  )
                    {
                        Log.e("in" ,"position " + 1 ) ;
                        navigationView.getMenu().getItem(1).setChecked( false ) ;
                        Log.e("title ", navigationView.getMenu().getItem(1).getTitle().toString()) ;
                        ModeFragments frag1 =
                                ( ModeFragments ) mViewPager.getAdapter()
                                        .instantiateItem( mViewPager, mViewPager.getCurrentItem() ) ;

                        (( ModeFragments ) frag1 ).makeAdapterListNull() ;
                        (( ModeFragments) frag1 ).onLoadData() ;
                    }
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2)
                {
                }

                @Override
                public void onPageScrollStateChanged(int pos) {
                }
            });
        } catch (Exception e)
        {
            Log.e(TAG," Exeption "+ e.getMessage() );
        }
    }


    private void createServerSocket() {
        Log.e(TAG, "createServerSocket" );
        uiHandler = new Handler();
        thread1 = new Thread(new ServerSocketThread() ) ;
        thread1.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        broadcastMyIPToOtherMobileDevice();
    }

    private void broadcastMyIPToOtherMobileDevice() {
        allMobile = getAllMobile();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wm.getConnectionInfo();

        int ipAddress = connectionInfo.getIpAddress();
        String myIp = Formatter.formatIpAddress(ipAddress);

        Log.e(TAG, "broadcastMyIPToOtherMobileDevice myIp is: "+myIp );

        String messageToSend = "MobileAdded,"+myIp ;

        for ( Mobile mobile : allMobile ) {
            Log.e(TAG, "broadcastMyIPToOtherMobileDevice : "+ mobile.IP +" " );
            try {
                if( !mobile.IP.equals(myIp)) {
                    clientSocket = new Socket();
                    clientSocket.connect(new InetSocketAddress(mobile.IP, SERVERPORT), 400);
                    Log.e(TAG, "socket created for " + mobile.IP);
                    printStreamClientSocket = new PrintStream(clientSocket.getOutputStream());
                    Log.e(TAG, "run: messageToSend: " + messageToSend);
                    printStreamClientSocket.println(messageToSend);
                }
            }catch (Exception e ) {
                Log.e(TAG, "Excep while creating socket "+mobile.IP );
                closeAllConnecton();
            }finally {
                closeAllConnecton();
            }
        }
    }

    @Override
    public void getResponse(String response, MethodSelection interface_method, String IP) {

        try {
            Log.e(TAG, "getResponse: "+response );

            switch ( interface_method ) {

                case STATUS:
                    this.statusResponse(response,IP );
                    break;
            }
        }catch (Exception e) {
            Log.e( "in getResponse" , e.getMessage() );
        }
    }

    private void statusResponse(String response, String IP)
    {
        if (response != null) {
//            Log.e(TAG, "statusResponse: "+IP +"  "+ response );
            String data1 = response.substring(response.indexOf(",") + 1);
            String deviceIdFromResponse = data1.substring(0, data1.indexOf(","));
            String data2 = data1.substring(data1.indexOf(",") + 1);

            List<SwitchButton> allButtonFromBoardId = getAllButtonFromBoardId(Long.parseLong(deviceIdFromResponse));

//            Log.e(TAG, "STATUS:  data2 :  " + data2);
            String numberOnly = data2.replaceAll("[^0-9]", "");
//            Log.e(TAG, "STATUS: numberOnly is : " + numberOnly);

            ActiveAndroid.beginTransaction();
            try {
                for (int i =0 ; i< allButtonFromBoardId.size() ; i++)
                {
                    SwitchButton switchB = allButtonFromBoardId.get(i);
                    String statusValue = String.valueOf(numberOnly.charAt(i));
                    if (statusValue.equals("0"))
                    {
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

            int currentItem =  mViewPager.getCurrentItem() ;

            // means if user is on Roomfragment
            if( currentItem == 0 ) {
                RoomFragment   roomFragment =
                        ( RoomFragment ) mViewPager.getAdapter()
                                .instantiateItem( mViewPager, mViewPager.getCurrentItem() );

//                Log.e(TAG, "subjectButtonstatus: isRoomOpenFlag "+isRoomOpenFlag);

                /**
                 * if it is  true then only update the arrayList of RoomInsideDialogAdapter.
                 *  means user is inside the Room . so we have to update arrayList to see changes quikly
                 */
                if( isRoomOpenFlag )
                {
//                    Log.e(TAG, "subjectButtonstatus: isRoomOpenFlag is true " );

                    // update arrayList of RoomInsideDialogAdapter
                    updateArrayListOfRoomInsideDialogAdapterAfterResponse( roomFragment,roomId, switchPosition );

                }else {
//                    Log.e(TAG, "subjectButtonstatus: isRoomOpenFlag is flase" ) ;

                    // if room is not open and current item is 0 it means user is on Room fragment .
                    // and if another user ON at least any one button of room , then room must be ON .
                    //for that setAtLeastOneButtonOfRoomIsOn to true
                    updateRoomTableAfterResponse( roomFragment , roomPosition ) ;
                }
            }


        } else {
            Log.e(TAG, "STATUS: status response is null");
        }
    }

    private class ServerSocketThread implements Runnable {
        String TAG = "ServerSocketThread" ;
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(SERVERPORT);
                while (true) {
                        try {
                            Log.e(TAG, "createServerSocket: waiting for client");
                            socket = serverSocket.accept();
                            scanner1 = new Scanner(socket.getInputStream());
                            printStream = new PrintStream(socket.getOutputStream());
                            String nextLine = scanner1.nextLine();

                            Log.e(TAG, "================  Receiving broadcasting response ===============" + nextLine);

                            if (nextLine.contains("ID")) {
                                Log.e(TAG, "run: send Mobile response back");
                                printStream.print("Mobile");
                            } else {
                                uiHandler.post(new UpdateUiThread(nextLine));
                            }

                        } catch (IOException e) {
                            Log.e(TAG, "run: Exception in inner try : " + e.getMessage());

                        } finally {

                            closeServerSocketConnection();
                        }
                    }
//                }
            } catch (Exception e)
            {
                Log.e(TAG, "Excep while creating ServerSocket");
                e.printStackTrace();
            }
        }
    }

    private void closeServerSocketConnection() {
        Log.e(TAG, "in closeServerSocketConnection: " );
        if( printStream!= null )
            printStream.close();

        if(scanner1 != null  )
            scanner1.close();

        if( socket != null  ) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "closeServerSocketConnection: "+e.getMessage() );
                e.printStackTrace();
            }
        }
    }


    private class BroadcastingThread implements Runnable {
        String TAG = "BroadcastingThread " ;
        String messageToSend ;
        public BroadcastingThread(String messageToSend) {
//            Log.e(TAG, " Constructor ");
            this.messageToSend = messageToSend ;
        }

        @Override
        public void run() {
            try {
//                Log.e(TAG, "isRoomOpenFlag before boradcasting to all mobile : "+isRoomOpenFlag );
                allMobile = getAllMobile();
                Log.e(TAG, "run:  allMobile size  "+allMobile.size()  );
                for (Mobile list : allMobile ) {
                    Log.e(TAG, "Broadcasting to following IP's "+list.IP );
                }

                for ( Mobile mobile : allMobile ) {
//                    Log.e(TAG, "run : "+ mobile.IP +" " );
                    try {
                        clientSocket = new Socket();
                        clientSocket.connect(new InetSocketAddress(mobile.IP, SERVERPORT), 1000);

//                        Log.e(TAG, "socket created for " + mobile.IP);
                        printStreamClientSocket = new PrintStream(clientSocket.getOutputStream());
//                        Log.e(TAG, "run: messageToSend: "+messageToSend );
                        printStreamClientSocket.println(messageToSend) ;
                    }catch (Exception e ) {
                        Log.e(TAG, "Excep while creating socket "+mobile.IP );
                        closeAllConnecton();
                    }finally {
                        closeAllConnecton();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "run: excep "+e.getMessage() );
                e.printStackTrace();
            }
        }
    }

    private void closeAllConnecton() {

        if( printStreamClientSocket != null )
            printStreamClientSocket.close();

        if( clientSocket != null )
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
    }


    public void broadcastToAllMobileDevices(final String response) {
        Log.e(TAG, "broadcastToAllMobileDevices6666: "+response );
        BroadcastingThread broadcastingThread = new BroadcastingThread( response ) ;
        new Thread(broadcastingThread).start();
    }

    private class UpdateUiThread implements Runnable {

        String TAG1 = " UpdateUiThread " ;
        String message ;
        public UpdateUiThread(String readLine)
        {
            message = readLine;
        }

        @Override
        public void run()
        {
//            Log.e(TAG, "isRoomOpenFlag in UpdateUiThread "+isRoomOpenFlag );
            Log.e(TAG1, "Broadcast message in UpdateUiThread is : "+ message );

//            if(  message.contains("ButtonStatus") )
//            {
                String subject = message.substring(0, message.indexOf(","));
               Log.e(TAG1, "subject "+ subject );

                if( subject.equals("ButtonStatus") )
                {
                    String data1 = message.substring(message.indexOf(",") + 1);
//                    Log.e(TAG, "data1 "+ data1 );

                    String roomId = data1.substring(0, data1.indexOf(","));
//                    Log.e(TAG, "roomId "+ roomId );

                    String data2 = data1.substring(data1.indexOf(",") + 1);
                    //Log.e(TAG, "data2 "+ data2 );

                    String roomPosition = data2.substring(0, data2.indexOf(","));
//                    Log.e(TAG, "roomPosition "+ roomPosition );


                    String data3 = data2.substring(data2.indexOf(",") + 1);
//                   Log.e(TAG, "data3 "+ data3 );

                    String buttonId = data3.substring(0, data3.indexOf(","));
//                    Log.e(TAG, "buttonId "+ buttonId );

                    String data4 = data3.substring(data3.indexOf(",") + 1);
//                   Log.e(TAG, "data4 "+ data4 );

                    String switchPosition = data4.substring(0, data4.indexOf(","));
//                    Log.e(TAG, "switchPosition "+ switchPosition );

                    String action = data4.substring(data4.indexOf(",") + 1);
//                    Log.e(TAG, "action "+ action );

                    subjectButtonstatus( roomId , roomPosition ,  buttonId , switchPosition , action);

                }else if (subject.equals("ModeOnOff")) {

                    // message contapin "ModeOnOff,0,1"
                    // here 0 is position and 1 is mode id


                    /**
                     *    step 9)  receive broadcasting messsage  in ServerSocketThread
                     *    step 10) call to UpdateUiThread with response string.
                     *    step 11) get position and modeId from response string
                     *    step 12) call to neworkCallForIdAndStatusAfterModeOnOff() with position and modeId
                     *    step 13) call threadForGettingStatus()for getting status of all active rooms and update all button status in DB.
                     *    step 14) get all mode and Off them in DB
                     *    step 15) get all active rooms
                     *    step 16) get all ON buttons ID form these active rooms
                     *    step 17) decide Mode status
                     *             here we have to change the status of Mode to ON.
                     *             for that condition is ( all on button = configuration set in that mode)
                     *             else that mode is OFF.
                     *             i.e tya mode madhle configure(ON) kelele sagle buttons ON pahijet.
                     *             1 pn kami nko 1 pn jast nko. exact same pahijet.tarch to mode ON hoel. else to OFF rahil
                     */



                    Log.e(TAG, "Broadcast response in UpdateUiThread---> run---> ModeOnOff" );

                    //String requestString = "ModeOnOff,0,1";
                    String data1 = message.substring(message.indexOf(",") + 1);

                    String modePosition= data1.substring(0, data1.indexOf(",")) ;
                    Log.e(TAG, "modePosition "+ modePosition ); // 0,1

                    String modeId = data1.substring(data1.indexOf(",") + 1); // data 2 : 4,0#
                    Log.e(TAG, "modeId"+ modeId );

                    neworkCallForIdAndStatusAfterModeOnOff(modePosition , modeId);



                }else if (subject.equals("MobileAdded"))
                {
                    Log.e(TAG, "inside MobileAdded else condition ----------------->" );


                    String myIP = message.substring(message.indexOf(",") + 1);
                    Log.e(TAG, "mobileIp "+ myIP );

//                    String myIP = "192.168.0.150" ;

                    List<Mobile> allMobile = getAllMobile();
                    List<String> allMobileIp = new ArrayList<>();

                    for (Mobile mobile : allMobile )
                    {
                        allMobileIp.add(mobile.IP);
                    }
                    if(! allMobileIp.contains(myIP))
                    {
                        Mobile mobile = new Mobile();
                        mobile.IP = myIP;
                        mobile.save();
                    }
                }
//            }
        }
    }

    private void neworkCallForIdAndStatusAfterModeOnOff(String modePosition, final String modeId)
    {
        String TAG1 = "neworkCallForIdAndStatusAfterModeOnOff " ;

        // if currentItem is 0 then roomfragment is open and if currentItem is 1 then modeFragment is open
        int currentItem  =  mViewPager.getCurrentItem() ;
        Log.e(TAG, " currentItem " + currentItem+"");

        // step 13) call threadForGettingStatus()for getting status of all active rooms and update all button status in DB.
        threadForGettingStatus();

        Log.e("=======After getting ", "============== Latest status =====================");

        //step 14) get all mode and Off them in DB
        // get all mode and off them from DB
        List<Mode> modeData = getAllMode();
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < modeData.size(); i++) {
                Mode mode1 = modeData.get(i);
                mode1.isOn = false;
                mode1.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        //step 15) get all active rooms
        List<Rooms> allActiveRooms = getAllRoomBasedOnRoomOnOff(1);

        //step 16) get all ON buttons ID form these active rooms
        List<Long> currentlyONButtonsID = new ArrayList<>();
        for (Rooms rooms : allActiveRooms) {
            List<SwitchButton> currentlyONButtonsLocal = getButtonsBasedOnIsOnFlag(true, rooms.getId());
            for (SwitchButton switchButton : currentlyONButtonsLocal)
            {
                currentlyONButtonsID.add(switchButton.getId());
            }
        }
        Log.e(TAG, "currentlyONButtonsID list " + Arrays.toString(currentlyONButtonsID.toArray()));




        /**    //step 17) decide Mode status
         *     here we have to change the status of Mode to ON.
         *     for that condition is ( all on button = configuration set in that mode )else that mode is OFF.
         *     i.e tya mode madhle configure(ON) kelele sagle buttons ON pahijet. 1 pn kami nko 1 pn jast nko. exact same pahijet.
         *     tarch to mode ON hoel. else to OFF rahil
         */

        if( currentlyONButtonsID.size() > 0)
        {
            List<Mode> allMode = getAllMode();
            for (Mode mode : allMode)
            {
                List<ModeOnOf> listModeOnOff = getDataFromModeOnOfTable(mode.getId());
                ArrayList<Long> modeOnOffId = new ArrayList<>();
                for (ModeOnOf modeOnOf : listModeOnOff) {
                    //Log.e(TAG, "onTheMode: ModeId: "+modeOnOf.ModeId +" ButtonId : " + modeOnOf.ButtonId +" ButtonName : " +  modeOnOf.ButtonName );
                    modeOnOffId.add(Long.parseLong(modeOnOf.ButtonId));
                }
                Log.e(TAG, "ModeOnOffId list " + Arrays.toString(modeOnOffId.toArray()));

                boolean modeStatus = listEqualsIgnoreOrder(currentlyONButtonsID, modeOnOffId);
                Log.e(TAG, "status  of mode " + mode.getId() + "  should be : " + modeStatus + "");
                if( modeStatus )
                {
                    // make selected mode ON in DB and save
                    mode.isOn = true;
                    mode.save();

                    updateModeAdapter(currentItem , modePosition  , true );
                }
            }
        }else
        {
            updateModeAdapter(currentItem , modePosition , false  );
        }
    }

    private void updateModeAdapter(int currentItem , String modePosition , boolean modeStatus) {

        if( currentItem  == 1 )
        {
            Log.e(TAG, " currentItem is 1 so we are on modeFragment");
            ModeFragments   modeFragments =
                    ( ModeFragments ) mViewPager.getAdapter()
                            .instantiateItem( mViewPager, mViewPager.getCurrentItem() );

            //  1)  make all mode off from arraylist ( modeList )
            for( int i = 0 ; i < modeFragments.adapter.modeList.size() ; i++ )
            {
                ModesActivityDbModel modesActivityDbModel = modeFragments.adapter.modeList.get(i);
                modesActivityDbModel.setOn(false);
            }
            ModesActivityDbModel modesActivityDbModel = modeFragments.adapter.modeList.get(Integer.parseInt(modePosition));
            modesActivityDbModel.setOn(modeStatus);

//            modeFragments.adapter.notifyItemChanged(Integer.parseInt(modePosition));

            modeFragments.adapter.notifyDataSetChanged();
        }else
        {
            Log.e(TAG, " currentItem is 0 so we are on roomFragment");
        }
    }

    private void threadForGettingStatus()
    {

//        switchBoardIpList = new ArrayList<>();
        Log.e(TAG, "neworkCallForIdAndStatusAfterModeOnOff: ");

        // get all Active rooms
        List<Rooms> allRoomActiveRooms = getAllRoomActiveRooms();

        for (Rooms rooms : allRoomActiveRooms )
        {
            SwitchBoard switchBoard = readSingleBoardById(rooms.getId());
            String ip = switchBoard.IP;
            if (ip != null && !ip.isEmpty())
            {
//                switchBoardIpList.add(ip);
                Log.e(TAG, " trying to connect " + ip);
                try {
                    // way 2 for creating socket
                    threadOneStatusSocket = new Socket();
                    threadOneStatusSocket.connect(new InetSocketAddress(ip, Constants.PORT), ThreadUtil.timeout);
                    threadOneStatusSocket.setSoTimeout(ThreadUtil.timeout);

                    threadOneStatusScanner = new Scanner(threadOneStatusSocket.getInputStream());
                    threadOneStatusPrintStream = new PrintStream(threadOneStatusSocket.getOutputStream());
                    threadOneStatusPrintStream.println("*STS," + switchBoard.getId() + "#");

                    /** response2 contain data like this : "*REC,1,0,1,2,3,4,5,6,7#"
                     this 0 to 7 are switch number
                     1 is  this is device id   **/

                    try {
                        String response2 = null ;
                        if( threadOneStatusScanner.hasNext() )
                        {
                            response2 = threadOneStatusScanner.nextLine();
                        }

                        if (response2 != null) {
                            Log.e(TAG, " status response2 : " + response2);

                            String data1 = response2.substring(response2.indexOf(",") + 1);
                            String deviceIdFromResponse = data1.substring(0, data1.indexOf(","));
                            String data2 = data1.substring(data1.indexOf(",") + 1);

                            List<SwitchButton> allButtonFromBoardId = getAllButtonFromBoardId(Long.parseLong(deviceIdFromResponse));

                            Log.e(TAG, "  data2 :  " + data2);
                            String numberOnly = data2.replaceAll("[^0-9]", "");
                            Log.e(TAG, " numberOnly is : " + numberOnly);

//                            for (int i = 0; i < allButtonFromBoardId.size(); i++)
//                            {
//                                SwitchButton switchB = allButtonFromBoardId.get(i);
//                                ActiveAndroid.beginTransaction();
//                                try {
//                                    String statusValue = String.valueOf(numberOnly.charAt(i));
//                                    if (statusValue.equals("0")) {
//                                        switchB.is_on = true;
//                                    } else {
//                                        switchB.is_on = false;
//                                    }
//                                    switchB.save();
//                                    ActiveAndroid.setTransactionSuccessful();
//                                } finally {
//                                    ActiveAndroid.endTransaction();
//                                }
//                            }
//                            ActiveAndroid.beginTransaction();

                            //step 4 : insert new data in ModeOnOff table using buttonNameList and buttonIdList .
                            ActiveAndroid.beginTransaction();
                            try {
                                for (int i = 0; i < allButtonFromBoardId.size() ; i++)
                                {
                                    SwitchButton switchB = allButtonFromBoardId.get(i);
                                    String statusValue = String.valueOf(numberOnly.charAt(i));
                                    if (statusValue.equals("0")) {
                                        switchB.is_on = true;
                                    } else {
                                        switchB.is_on = false;
                                    }
                                    switchB.save();
                                }
                                ActiveAndroid.setTransactionSuccessful() ;
                            } finally {
                                ActiveAndroid.endTransaction();
                            }
                        } else {
                            Log.e(TAG, " status response2 is null");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, " exception while reading status response ");
                    } finally {

                        if (threadOneStatusPrintStream != null)
                            threadOneStatusPrintStream.close();

                        if (threadOneStatusScanner != null)
                            threadOneStatusScanner.close();

                        if (threadOneStatusSocket != null)
                            try {
                                threadOneStatusSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "neworkCallForIdAndStatusAfterModeOnOff: Exception while connecting to " + ip);
                }
            }else {
                Log.e(TAG, "threadForGettingStatus: ip is null or empty for board  and room name"+switchBoard.BoardName+ "  "+rooms.Name);
            }
        }
    }

    private void subjectModeOnOff(String modeId , String position ,boolean action )
    {
        if( mViewPager.getCurrentItem() == 1 )
        {
            ModeFragments   modeFragments =
                    ( ModeFragments ) mViewPager.getAdapter()
                            .instantiateItem( mViewPager, mViewPager.getCurrentItem() );
            if( action )
            {
                //  step 1 : get all Mode and Off Them
                offMode() ;

                // step 2 : get all button from Db and Off them
                getAllButtonFromDbAndOffThem() ;

                // step 3 : get all button with selected modeId from ModeOnOf table and make all button ON and save in DB.
                getAllButtonAssociateWithModeIdAndOnThem( modeId );

                // step 4 :  make selected mode ON or Off  in DB and save
                Mode mode = getSingleModeById(Long.parseLong(modeId));
                mode.isOn = action;
                mode.save();
//                readSingleBoardById()

                //  1)  make all mode off from arraylist ( modeList )
                for( int i = 0 ; i < modeFragments.adapter.modeList.size() ; i++ )
                {
                    ModesActivityDbModel modesActivityDbModel = modeFragments.adapter.modeList.get(i);
                    modesActivityDbModel.setOn( action ) ;
                }
                modeFragments.adapter.notifyItemChanged(Integer.parseInt(position) );
            }else
            {
                ModesActivityDbModel singleItem = modeFragments.adapter.modeList.get(Integer.parseInt(position));
                Mode mode = getSingleModeById(Long.parseLong(modeId));

                //  OFF mode .but  the button present inside mode are remains ON.
                mode.isOn = false;
                singleItem.setOn(false);
                mode.save();
                modeFragments.adapter.notifyItemChanged(Integer.parseInt(position) );
            }
        }else
        {
        }
    }


    private void subjectButtonstatus(String roomId,  String roomPosition , String buttonId, String switchPosition, String action)
    {
        this.roomPosition = roomPosition;
        this.roomId = roomId;
        this.switchPosition = switchPosition;

        // if any button status is change while mode is on  then first off that mode
        offMode();

        //  get status of all button of single room and update db
        getStatus(roomId);

        // update database.
//        updateButtonTableAfterResponse( roomId , Long.parseLong( buttonId ) , switchPosition , action  );

        int currentItem =  mViewPager.getCurrentItem() ;

        // means if user is on Roomfragment
        if( currentItem == 0 ) {
            RoomFragment   roomFragment =
                    ( RoomFragment ) mViewPager.getAdapter()
                            .instantiateItem( mViewPager, mViewPager.getCurrentItem() );

            Log.e(TAG, "subjectButtonstatus: isRoomOpenFlag "+isRoomOpenFlag);

            /**
             * if it is  true then only update the arrayList of RoomInsideDialogAdapter.
             *  means user is inside the Room . so we have to update arrayList to see changes quikly
             */
            if( isRoomOpenFlag )
            {
                Log.e(TAG, "subjectButtonstatus: isRoomOpenFlag is true " );

                // update arrayList of RoomInsideDialogAdapter
                updateArrayListOfRoomInsideDialogAdapterAfterResponse( roomFragment,roomId, switchPosition );

            }else {
                Log.e(TAG, "subjectButtonstatus: isRoomOpenFlag is flase" ) ;

                // if room is not open and current item is 0 it means user is on Room fragment .
                // and if another user ON at least any one button of room , then room must be ON .
                //for that setAtLeastOneButtonOfRoomIsOn to true
                updateRoomTableAfterResponse( roomFragment , roomPosition ) ;
            }
        }
    }

    private void getStatus(String roomId) {
        SwitchBoard switchBoard = getSingleSwitchBoardFromRoomId(roomId);
        String statusRequest = "*STS," + switchBoard.getId() + "#";
        CommonAsynTaskNew asynTask = new CommonAsynTaskNew(context, statusRequest, my_object, MethodSelection.STATUS, switchBoard.IP);
        if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asynTask.execute();
        }
    }


    // update arrayList of RoomInsideDialogAdapter
    private void updateArrayListOfRoomInsideDialogAdapterAfterResponse(RoomFragment roomFragment, String roomId , String switchPosition)
    {
        try {
            Log.e(TAG, "updateArrayListOfRoomInsideDialogAdapterAfterResponse: " + switchPosition);
//            int parseInt = Integer.parseInt(switchPosition);
//            parseInt = parseInt - 1;
//            Log.e(TAG, "updateArrayListOfRoomInsideDialogAdapterAfterResponse: parseInt: " + parseInt);

            ArrayList<SwitchButtonDbModel> roomButtonList = roomFragment.adapter.roomInsideDialogAdapter.roomButtonList;
            for (SwitchButtonDbModel singleModel : roomButtonList) {
                SwitchButton singleButtonById = getSingleButtonById(singleModel.getId());
                singleModel.setOn(singleButtonById.is_on);
            }
            roomFragment.adapter.roomInsideDialogAdapter.notifyDataSetChanged();
        }catch (Exception e )
        {
            Log.e(TAG, "Exception in updateArrayListOfRoomInsideDialogAdapterAfterResponse "+e.getMessage() );
        }
    }


//    // update arrayList of RoomInsideDialogAdapter
//    private void updateArrayListOfRoomInsideDialogAdapterAfterResponse(RoomFragment roomFragment, String roomId , String switchPosition)
//    {
//        try {
//            Log.e(TAG, "updateArrayListOfRoomInsideDialogAdapterAfterResponse: " + switchPosition);
//            int parseInt = Integer.parseInt(switchPosition);
//            parseInt = parseInt - 1;
//            Log.e(TAG, "updateArrayListOfRoomInsideDialogAdapterAfterResponse: parseInt: " + parseInt);
//
//            SwitchButtonDbModel switchButtonDbModel = roomFragment.adapter.roomInsideDialogAdapter.roomButtonList.get(parseInt);
//            Log.e(TAG, switchButtonDbModel.getsButtonName() + " " + switchButtonDbModel.getOn());
//            Log.e(TAG, "updateArrayListOfRoomInsideDialogAdapterAfterResponse: roomId: "+roomId +"  button attach room Id: "+switchButtonDbModel.getRoomId() );
//
//            if( roomId.equals(String.valueOf(switchButtonDbModel.getRoomId())))
//            {
//                if (switchButtonDbModel.getOn()) {
//                    switchButtonDbModel.setOn(false);
//                } else {
//                    switchButtonDbModel.setOn(true);
//                }
//                roomFragment.adapter.roomInsideDialogAdapter.notifyItemChanged(parseInt);
//            }
//        }catch (Exception e )
//        {
//            Log.e(TAG, "Exception in updateArrayListOfRoomInsideDialogAdapterAfterResponse "+e.getMessage() );
//        }
//    }

    // if room is not open and current item is 0 it means user is on Room fragment .
    // and if another user ON at least any one button of room , then room must be ON .
    //for that setAtLeastOneButtonOfRoomIsOn to true
    private void updateRoomTableAfterResponse(RoomFragment roomFragment, String roomPosition)
    {
        Log.e(TAG, "updateRoomTableAfterResponse: " );

        if( roomList !=null) {
            RoomDbModel roomDbModel = roomFragment.adapter.roomList.get(Integer.parseInt(roomPosition));
//        List<SwitchButton> buttonList =  roomFragment.getAllButtonOfRoom(roomDbModel.getId()  ) ;
            List<SwitchButton> onlyOnButtonFromSingleRoom = getOnlyOnButtonFromSingleRoom(roomDbModel.getId());
            Log.e("size of ON button is : ", onlyOnButtonFromSingleRoom.size() + "");

            if (onlyOnButtonFromSingleRoom.size() > 0) {
                roomDbModel.setAtLeastOneButtonOfRoomIsOn(true);
            } else {
                roomDbModel.setAtLeastOneButtonOfRoomIsOn(false);
            }
            roomFragment.adapter.notifyItemChanged( Integer.parseInt( roomPosition ) );
//            roomFragment.adapter.notifyDataSetChanged();
        }else{

            Log.e("hhh","kkk");
        }
    }

    private void offMode() {

        List<Mode> allMode = getAllMode();
        for( int i=0 ; i< allMode.size() ; i++ )
        {
            Mode mode = allMode.get(i);
            mode.isOn=false;
            mode.save();
        }
    }

    // update button db after response
    private void updateButtonTableAfterResponse( String roomId , Long buttonId , String position , String action  )
    {
        Log.e(TAG, "updateButtonTableAfterResponse" );
        final SwitchButton switchButton = getSingleButtonById( buttonId ) ;

        Log.e(TAG, "SwitchButton before update : "+ switchButton.SwitchButtonName +"  "+switchButton.is_on );
        long time = Calendar.getInstance().getTime().getTime();
        switchButton.SwitchButtonName = switchButton.SwitchButtonName ;
        boolean isOn =  false ;
        if( Integer.parseInt( action ) == 1 )
        {
            isOn = false;
        }else {
            isOn = true ;
        }
        Log.e(TAG, "updateButtonTableAfterResponse  isOn : "+isOn );
        switchButton.is_on = isOn ;
        switchButton.SwitchButtonUpdatedat = time ;
        switchButton.save();
        SwitchButton switchButton1 = getSingleButtonById( buttonId ) ;
        Log.e(TAG, "SwitchButton after update : "+ switchButton1.SwitchButtonName +"  "+switchButton1.is_on );
    }

    boolean isOnDestroyCall = false ;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        isOnDestroyCall = true;

        Log.e(TAG, "onDestroy: " );

        closeServerSocketConnection();
    }

    private void setViewPagerAndTabLayout()
    {
        // Set up the ViewPager with the sections adapter.
        mViewPager = ( ViewPager ) findViewById( R.id.activity_v1_main_container ) ;
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(0);
        tabLayout = (TabLayout) findViewById( R.id.activity_v1_main_tabs ) ;
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter( mSectionsPagerAdapter ) ;
        tabLayout.setupWithViewPager( mViewPager ) ;
        tabLayout.setTabMode( TabLayout.MODE_FIXED ) ;
        tabLayout.setTabGravity( TabLayout.GRAVITY_FILL ) ;
    }

    private void openSettingFragment(Fragment fragment, String toolbatTitle )
    {
        try {

            this.getSupportFragmentManager().popBackStack();
            toolbar.setTitle( toolbatTitle );
            Log.e("inside on","openSettingFragment") ;
            getSupportFragmentManager().beginTransaction()
                    .add(  R.id.activity_v1_main_frame , fragment )
                    .addToBackStack( null )
                    .commit() ;
        }
        catch (Exception e)
        {
        }
    }

    @Override
    public void onBackPressed()
    {
        Log.e("In ", "onBackPressed") ;

        int count = getFragmentManager().getBackStackEntryCount();
        Log.e("count is ",count+"") ;

        if ( count == 0)
        {
            super.onBackPressed();
            Log.e("inside "," if ") ;
            mViewPager.setVisibility( View.VISIBLE );
            tabLayout.setVisibility( View.VISIBLE );
            frameLayout.setVisibility( View.GONE );

            setAllItemOfNavigationViewToFalse();

            if( mViewPager.getCurrentItem() == 0 )
            {
                Log.e(TAG, "onBackPressed: currentItem 0" );
                navigationView.getMenu().getItem(0).setChecked(true);
                RoomFragment frag1 =
                        (RoomFragment) mViewPager.getAdapter()
                                .instantiateItem(mViewPager, mViewPager.getCurrentItem());

                ((RoomFragment) frag1).makeAdapterListNull();
                ((RoomFragment) frag1).onLoadData();
            }else if( mViewPager.getCurrentItem() == 1 )
            {
                Log.e(TAG, "onBackPressed: currentItem 1" );

                navigationView.getMenu().getItem(1).setChecked(true);

                ModeFragments frag1 =
                        ( ModeFragments ) mViewPager.getAdapter()
                                .instantiateItem( mViewPager, mViewPager.getCurrentItem() ) ;

                (( ModeFragments ) frag1 ).makeAdapterListNull() ;
                (( ModeFragments) frag1 ).onLoadData() ;
            }
            //additional code
        } else {
            Log.e("inside ","else") ;
            this.getSupportFragmentManager().popBackStack();
        }
    }


    // Read all room
    private List<Rooms> getAllRoom()
    {

//        Log.e("inside","getAllRoom");

        //  order by id
        return new Select().from(Rooms.class).orderBy("id ASC").execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();

    }

    // Read all Data
    private List<Rooms> getAllRoomBasedOnRoomOnOff(int  roomstatus)
    {
        //  order by id
        return new Select().from(Rooms.class).orderBy("id ASC").where("RoomOnOff = ?", roomstatus ).execute();

        // return data in random order
        // return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();
    }


    private Mode getSingleModeById( Long modeId )
    {
        // access by id
        return new Select().from(Mode.class).where("id = ?", modeId ).executeSingle();
    }

    private void getAllButtonAssociateWithModeIdAndOnThem(String modeId)
    {
        //   get all button with selected modeId from ModeOnOf table
        //     and make all button ON and save in DB.
        List<ModeOnOf> listModeOnOff = getDataFromModeOnOfTable(Long.parseLong(  modeId ) );
        for ( ModeOnOf modeOnOff : listModeOnOff )
        {
            SwitchButton switchButton = getSingleButtonById(Long.parseLong(modeOnOff.ButtonId ) ) ;
            Log.e("Button id & name rName", switchButton.getId() + "  " + switchButton.SwitchButtonName + "  " + switchButton.RoomId + "");
            switchButton.is_on = true;
            Date currentTime = Calendar.getInstance().getTime();
            switchButton.SwitchButtonUpdatedat = currentTime.getTime();
            switchButton.save();
        }
    }

    private List<ModeOnOf> getDataFromModeOnOfTable( Long  modeId )
    {
        //  order by id
        return new Select().from( ModeOnOf.class).where("ModeId = ?", modeId ).execute() ;
    }

    // get all button from Db and Off them
    private void getAllButtonFromDbAndOffThem()
    {
        List<SwitchButton> switchButtons = getAllButton();
        for (SwitchButton button : switchButtons) {
            button.is_on = false;
            button.save();
        }

//        String statusRequest = "*STS," + switchBoard.getId() + "#";
//        CommonAsynTaskNew asynTask = new CommonAsynTaskNew(context, statusRequest, my_object, MethodSelection.STATUS, switchBoard.IP);
//        if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
//            asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } else {
//            asynTask.execute();
//        }

        Log.e("button oof","off");
    }

    private List<SwitchButton> getAllButton()
    {
        //  order by id
        return new Select().from( SwitchButton.class ).orderBy("id ASC").execute() ;
    }

    // Read all Mobile
    private List<Mobile> getAllMobile()
    {

        Log.e("inside","getAllMobile");

        //  order by id
        return new Select().from(Mobile.class).orderBy("id ASC").execute();

        // return data in random order
//        return new Select().from(Mobile.class).orderBy("RANDOM()") .execute();

    }

    // Read all SwitchBoard
    private List<SwitchBoard> getAllSwitchBoard()
    {
        Log.e("inside","getAllSwitchBoard");

        //  order by id
        return new Select().from(SwitchBoard.class).orderBy("id ASC").execute();
    }


    SwitchButton getSingleButtonById(Long buttonId )
    {
        return new Select().from(SwitchButton.class).where("id = ?", buttonId ).executeSingle();
    }

    SwitchBoard readSingleBoardById(Long boardId )
    {
        return new Select().from(SwitchBoard.class).where("id = ?", boardId ).executeSingle();
    }

    List<SwitchButton> getOnlyOnButtonFromSingleRoom(Long roomId )
    {
//        return new Select().from(SwitchButton.class).where("is_on = ?", true ).where("id =?", roomId ).execute();
        return new Select().from(SwitchButton.class).where("is_on = ? AND RoomId = ?", true, roomId).execute();
    }

    // checkString return true if string is null  , it will return false if string is not null
    public boolean checkString( String str )
    {
        return str != null && (  str.equals("null") ||  str.equals("NaN") || str.equals("undefined") || str.equals("") || str.isEmpty() );
    }


    public void setPagerFragment( int a )
    {
        mViewPager.setCurrentItem( a ) ;
    }

    private void setIconToNavigationDrawerElements()
    {
/**
 *    This line is used to make first item in drawer chakable , when we open app
 */
//        navigationView.getMenu().getItem(1).setChecked(true);
        Menu menu = navigationView.getMenu();

        MenuItem nav_home  = menu.findItem(R.id.nav_home);
        nav_home.setIcon(R.drawable.menu_home);

        MenuItem nav_notification  = menu.findItem(R.id.nav_notification);
        nav_notification.setIcon(R.drawable.menu_notofication );

        MenuItem nav_settings  = menu.findItem(R.id.nav_settings);
        nav_settings.setIcon(R.drawable.menu_setting );

        MenuItem nav_help  = menu.findItem(R.id.nav_help);
        nav_help.setIcon(R.drawable.menu_help);

        MenuItem nav_share  = menu.findItem(R.id.nav_share);
        nav_share.setIcon(R.drawable.menu_share );

        MenuItem nav_logout  = menu.findItem(R.id.nav_logout);
        nav_logout.setIcon(R.drawable.menu_logout );
    }

    // Read single SwitchBoard
    private SwitchBoard getSingleSwitchBoardFromRoomId(String roomId)
    {
        Log.e("inside","getSingleSwitchBoardFromRoomId");

        //  order by id
        return new Select().from(SwitchBoard.class).where("RoomId = ?", roomId ).executeSingle();
    }

    private void loadHeader()
    {
//        TextView navigationDrawerTitle = ( TextView ) nav_header.findViewById( R.id.activity_v1_main_drawer_navigation_bar_title );
        TextView navigationDrawerEmail = ( TextView ) nav_header.findViewById( R.id.activity_v1_main_drawer_navigation_bar_email );
//        final ImageView imageView = ( ImageView )nav_header.findViewById( R.id.imageView );

//        Glide.with(context).load(R.drawable.menu_home).asBitmap().into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                Drawable drawable = new BitmapDrawable(resource);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    imageView.setBackground(drawable);
//                }
//            }
//        });

        if( email != null  && !email.equals("null") && !email.equals("") ) {

            navigationDrawerEmail.setText( email );
        } else  {

            navigationDrawerEmail.setVisibility( View.GONE );
        }

//        if( homeName != null  && !homeName.equals("null") && !homeName.equals("") ) {
//
//            navigationDrawerTitle.setText( homeName );
//
//        } else  {
//            navigationDrawerTitle.setVisibility( View.GONE );
//        }


    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item)
//    {
//
//        Log.e("in","onNavigationItemSelected");
//        drawer.closeDrawers();
//        final int id = item.getItemId() ;
//        if (id == R.id.nav_home )
//        {
////            if ( ! item.isChecked() )
////            {
////                Intent intent= new Intent( context , ModesActivity.class ) ;
////                context.startActivity( intent ) ;
////                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_right ) ;
////            }
//        }else if ( id == R.id.nav_notification )
//        {
////            if( ! item.isChecked() )
////            {
////                Intent i = new Intent( context , AdminMainActivity.class ) ;
////                startActivity( i ) ;
////                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_right ) ;
////            }
//
//        }else if ( id == R.id. nav_settings )
//        {
//            Log.e("click on","setting") ;
////            if( ! item.isChecked() )
////            {
////                Intent i = new Intent( context , AdminMainActivity.class ) ;
////                startActivity( i ) ;
////                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_right ) ;
////            }
//
//        }else  if( id == R.id.nav_logout )
//        {
//            if( ! item.isChecked() )
//            {
//
//                sharedPref.edit().clear() ;
//                Intent i = new Intent( context , SplashScreen.class ) ;
//                startActivity( i ) ;
//
//
////                Intent intent = new Intent() ;
////                intent.setType("image/*") ;
////                intent.setAction( Intent.ACTION_GET_CONTENT ) ;
////                startActivityForResult( Intent.createChooser( intent, "Select Picture"), PICK_IMAGE_REQUEST ) ;
//            }
//        }
//        else  if( id == R.id.nav_roomImage )
//        {
//            if( ! item.isChecked() )
//            {
//
//                Intent i = new Intent( context , AddRoomImageActivity.class ) ;
//                startActivity( i ) ;
//
//
////                Intent intent = new Intent() ;
////                intent.setType("image/*") ;
////                intent.setAction( Intent.ACTION_GET_CONTENT ) ;
////                startActivityForResult( Intent.createChooser( intent, "Select Picture"), PICK_IMAGE_REQUEST ) ;
//            }
//        }

//        return false;
//    }

    private void getSharedPreferencesVariable()
    {
        Log.e(TAG, "getSharedPreferencesVariable: " );
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        isTableCreated = sharedPref.getString("isTableCreated", "");
        homeName = sharedPref.getString("homeName", "");
        isModesCreated = sharedPref.getString("isModesCreated", "");

        String  firstName = sharedPref.getString("firstName", "");
        String  lastName = sharedPref.getString("lastName", "");
        email = sharedPref.getString("email", "");
        String  password = sharedPref.getString("password", "");
        String  gender = sharedPref.getString("gender", "");

//        String  dob = sharedPref.getString("dob", "");

//        Log.e("firstName "+firstName, "lastName "+lastName+" "+"email "+email+"  password "+password+"  gender "+gender ) ;
    }

    private void createModes()
    {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < modeNamesArray.length ; i++)
            {
                try {
                    Date currentTime = Calendar.getInstance().getTime();
                    Mode modes = new Mode();
                    modes.ModeName = modeNamesArray[i];
                    modes.CreatedAt = currentTime.getTime() ;
                    modes.Updatedat = currentTime.getTime() ;
                    modes.isOn = false ;
                    modes.ModeImage = modeImageArray[i] ;
                    modes.save() ;
                }catch ( Exception e )
                {
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

    private void addRoom()
    {
        ActiveAndroid.beginTransaction() ;
        try {
            for (int i = 0; i < roomNamesArray.length ; i++ )
            {
                try
                {
                    Date currentTime = Calendar.getInstance().getTime() ;
                    Rooms rooms = new Rooms() ;
                    rooms.Name = roomNamesArray[i] ;
                    rooms.RoomHomePageImage = String.valueOf( roomImageArray[i]) ;
                    rooms.AddRoomImage = String.valueOf( addRoomImages[i] ) ;
                    rooms.RoomOnOff = roomOnOffArray[i] ;
                    rooms.CreatedAt = currentTime.getTime() ;
                    rooms.Updatedat = currentTime.getTime() ;
                    rooms.save() ;
                }catch ( Exception e )
                {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        public SectionsPagerAdapter( FragmentManager fm )
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position )
        {
            switch ( position )
            {
                case 0:
                    return new RoomFragment();
                case 1:
//                    Log.e("===1","===========");
                    return new ModeFragments();
                default:
                    return null;
            }
        }

        @Override
        public int getCount()
        {
            // Show 4 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle( int position )
        {
            switch ( position )
            {
                case 0:
                    return "Room";
                case 1:
                    return "Mode";
            }
            return null;
        }
    }

    // in android device we have three click option at bottom.
    // 1) back press ( KEYCODE_BACK ) 2) home  3) menu press ( KEYCODE_MENU ) ( on oppo after long press it will
    // show number of app running in background )
    // The reason to override this method here is. when we set menu to toolbar. and if we click menu press button on bottom screen
    // menu is shown at bottom and we dont want want this behaviour.
    // after click on toolbar menu icon then only menu should be display
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            Log.e("in" ,"KEYCODE_BACK");
//            return true;
//        }
        if (keyCode == KeyEvent.KEYCODE_MENU)
        {
            Log.e("in" ,"KEYCODE_MENU");
            return true;
        }

//        if (keyCode == KeyEvent.KEYCODE_HOME)
//        {
//            Log.e("in" ,"KEYCODE_HOME");
//            return false;
//        }
//        if (keyCode == KeyEvent.KEYCODE_SEARCH)
//        {
//            Log.e("in" ,"KEYCODE_SEARCH");
//            return false;
//        }
//        if (keyCode == KeyEvent.KEYCODE_SETTINGS)
//        {
//            Log.e("in" ,"KEYCODE_SETTINGS");
//            return false;
//        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId())
//        {
//            case R.id.action_sign_out:
//
//                Log.e("Click","action sign out") ;
//                break;
//            case R.id.action_change_background_icon:
//                Log.e("Click","action_change_background_icon") ;
//                break;
////            case R.id.about:
////                break;
//        }
//        return true ;
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.toolbar_menu, menu);
//        // return true so that the menu pop up is opened
//        return true;
//    }

    private void setAllItemOfNavigationViewToFalse()
    {
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    private List<Mode> getAllMode() {
        // access by id
        return new Select().from(Mode.class).orderBy("id ASC").execute();
    }

    // isRoomOpenFlag is true then only update the arrayList object of RoomInsideDialogAdapter class.
    public void setIsRoomOpenFlag(boolean isRoomOpenFlag)
    {
        Log.e(TAG , "SET setIsRoomOpenFlag to "+isRoomOpenFlag );
        this.isRoomOpenFlag = isRoomOpenFlag ;
    }


    public class EchoThread extends Thread {
//        protected Socket socket;
//
//        public EchoThread(Socket clientSocket) {
//            this.socket = clientSocket;
//        }
//
//        public void run() {
//            InputStream inp = null;
//            Scanner scanner = null;
//            PrintStream printStream11 = null;
//            try {
//                inp = socket.getInputStream();
//                scanner = new Scanner(socket.getInputStream() ) ;
//                printStream11 = new PrintStream(socket.getOutputStream());
//            } catch (IOException e) {
//                return;
//            }
//            String line;
//            while (true) {
//                try {
//                    line = scanner.nextLine();
//                    Log.e(TAG, "line "+line );
//                    if ((line == null) || line.equalsIgnoreCase("QUIT")) {
//                        socket.close();
//                        return;
//                    } else {
//                        printStream11.println(line);
////                        printStream11.flush();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return;
//                }
//            }
//        }
    }

    private class Thread2  implements Runnable
    {
        //        private  Socket clientsocket ;
//        Scanner scanner  ;
//        String TAG = "Thread2" ;
//        public Thread2(Socket socket)
//        {
//            this.clientsocket = socket ;
//            try
//            {
//                scanner = new Scanner(clientsocket.getInputStream() );
//
//                if( socket.isConnected() )
//                {
//                    try {
//                        printStream = new PrintStream(clientsocket.getOutputStream());
//                        Log.e(TAG, "printStream created ");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    Log.e(TAG, "socket is not connected" );
//                }
//
//
//            }catch (Exception e )
//            {
//                Log.e(TAG, " Constructor " );
//            }
//        }
//
        @Override
        public void run()
        {
////            Log.e(TAG, "run: in thread2" );
//            while ( !Thread.currentThread().isInterrupted() )
//            {
//                String readLine = null ;
//                try {
//                    readLine = scanner.nextLine();
//                    Log.e(TAG, "readline contain "+readLine );
//                    if (readLine != null   )
//                    {
//                        if( !readLine.isEmpty() )
//                        {
//                            Log.e(TAG, " in Thread 2 "+readLine);
//                            uiHandler.post(new UpdateUiThread(readLine));
//                        }else
//                        {
//                            Log.e(TAG, "run: readline is empty 2" );
//                        }
//                    }else {
//                        thread1 = new Thread(new ServerSocketThread() );
//                        thread1.start();
//                        return;
//                    }
//                } catch (Exception e)
//                {
//                    Log.e(TAG, "server socker is not running "+e.getMessage() ) ;
//                    e.printStackTrace();
//                }
//            }
        }
    }

    private class ClientThread2 implements Runnable
    {
        //        private Socket clientsocket ;
//        private Scanner scanner;
//        public ClientThread2(Socket socket)
//        {
//            this.clientsocket = socket ;
//            try
//            {
//                scanner = new Scanner(clientsocket.getInputStream() );
//                if( socket.isConnected() ) {
//                    try {
//                        printStreamClientSocket = new PrintStream(clientsocket.getOutputStream());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    Log.e(TAG, "ClientThread2: socket is not connected" );
//                }
//            }catch (Exception e )
//            {
//                Log.e(TAG, "Thread2: Constructor " );
//            }
//        }
//
        @Override
        public void run()
        {
////            Log.e(TAG, "ClientThread2 in run" );
////            while ( !Thread.currentThread().isInterrupted() )
////            {
////                try {
////                    String readLine = scanner.nextLine();
////                    Log.e(TAG, "run: readline contain server says : "+readLine );
//////                    Toast.makeText(MainActivityClientSocket.this, "Server says : "+readLine  , Toast.LENGTH_SHORT).show();
////                    if (readLine != null  )
////                    {
////                        if( !readLine.isEmpty() )
////                            uiHandler.post(new UpdateUiThread(readLine ));
////                    }else {
////                        thread1 = new Thread(new ServerSocketThread());
////                        thread1.start();
////                        return;
////                    }
////                } catch (Exception e)
////                {
////                    Log.e(TAG, "run: in while "+e.getMessage() ) ;
////                    e.printStackTrace();
////                }
        }
//        }
    }

    private class ClientThread1 implements Runnable
    {
        //        String TAG= "ClientThread1" ;
        @Override
        public void run()
        {
//            Log.e(TAG, "inside run " );
//            for (Mobile mobile : allMobile )
//            {
//                try {
//                    Log.e(TAG, "run: allmobile "+ mobile.IP );
//                    Socket   clientSocket = new Socket(mobile.IP , SERVERPORT) ;
//                    Log.e(TAG, "ClientThread1 socket created for "+mobile.IP );
//                    PrintStream printStreamClientSocket = new PrintStream(clientSocket.getOutputStream());
//                    clientSocketArrayList.add( clientSocket );
//                    clientPrintStreamArrayList.add( printStreamClientSocket );
//                    ipArrayList.add(mobile.IP);
//
//                }catch (Exception e)
//                {
//                    Log.e(TAG, "Cant't create socket with ip "+e.getMessage() );
//
//                }

//            }
//
////                ClientThread2 clientThread2 = new ClientThread2( clientSocket );
////                new Thread(clientThread2).start();
//            return;
        }
    }

    // Read all active rooms
    private List<Rooms> getAllRoomActiveRooms()
    {
        //  order by id
        return new Select().from(Rooms.class).orderBy("id ASC").where("RoomOnOff = ?", 1 ).execute();

        // return data in random order
        // return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();
    }

    List<SwitchButton> getAllButtonFromBoardId(Long boardId) {
//        return new Select().from(SwitchButton.class).where("is_on = ?", true ).where("id =?", roomId ).execute();
//        return new Select().from(SwitchButton.class).where("is_on = ? AND RoomId = ?", boardId).execute();
        return new Select().from(SwitchButton.class).where("SwitchBoardId = ?", boardId).execute();
    }

    private List<SwitchButton> getButtonsBasedOnIsOnFlag( boolean status ,Long  roomId)
    {
        return new Select().from(SwitchButton.class).where("is_on = ? AND RoomId = ?", status , roomId).execute();
    }

    public static <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2)
    {
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }

    private void updateModeStatusAccordingToModeConfiguration() {

        List<Mode> allMode = getAllMode();

        // get all active rooms
        List<Rooms> allActiveRooms = getAllRoomBasedOnRoomOnOff(1);

        // get all ON buttons ID form these active rooms
        List<Long> currentlyONButtonsID = new ArrayList<>();
        for (Rooms rooms : allActiveRooms) {
            List<SwitchButton> currentlyONButtonsLocal = getButtonsBasedOnIsOnFlag(true, rooms.getId());
            for (SwitchButton switchButton : currentlyONButtonsLocal)
            {
                currentlyONButtonsID.add(switchButton.getId());
            }
        }
        Log.e(TAG, "currentlyONButtonsID list " + Arrays.toString(currentlyONButtonsID.toArray()));

        if( currentlyONButtonsID.size() > 0 )
        {
            for (Mode mode : allMode)
            {
                List<ModeOnOf> listModeOnOff = getDataFromModeOnOfTable(mode.getId());
                ArrayList<Long> modeOnOffId = new ArrayList<>();
                for (ModeOnOf modeOnOf : listModeOnOff) {
                    //Log.e(TAG, "onTheMode: ModeId: "+modeOnOf.ModeId +" ButtonId : " + modeOnOf.ButtonId +" ButtonName : " +  modeOnOf.ButtonName );
                    modeOnOffId.add(Long.parseLong(modeOnOf.ButtonId));
                }
                Log.e(TAG, "ModeOnOffId list " + Arrays.toString(modeOnOffId.toArray()));

                boolean modeStatus = listEqualsIgnoreOrder(currentlyONButtonsID, modeOnOffId);
                Log.e(TAG, "status  of mode " + mode.getId() + "  should be : " + modeStatus + "");
                if( modeStatus )
                {
                    // make selected mode ON in DB and save
                    mode.isOn = true;
                    mode.save();
                }
            }
        }else
        {
            ActiveAndroid.beginTransaction();
            try {
                for (int i =0 ; i< allMode.size() ; i++)
                {
                    Mode mode = allMode.get(i);
                    mode.isOn=false ;
                    mode.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            }
            finally {
                ActiveAndroid.endTransaction();
            }
        }
    }

}
