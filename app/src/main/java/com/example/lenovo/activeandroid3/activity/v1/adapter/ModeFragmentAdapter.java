package com.example.lenovo.activeandroid3.activity.v1.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.ResponseInterfaceNew;
import com.example.lenovo.activeandroid3.activity.v1.activity.V1MainActivity;
import com.example.lenovo.activeandroid3.activity.v1.interfaces.ModeOnOffInterface;
import com.example.lenovo.activeandroid3.activity.v1.utils.MethodSelection;
import com.example.lenovo.activeandroid3.activity.v1.utils.ThreadUtil;
import com.example.lenovo.activeandroid3.asyntask.CommonAsynTaskNew;
import com.example.lenovo.activeandroid3.dbModel.ModeNetworkCallDbModel;
import com.example.lenovo.activeandroid3.dbModel.ModelNewDbModel;
import com.example.lenovo.activeandroid3.dbModel.ModesActivityDbModel;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.model.Mobile;
import com.example.lenovo.activeandroid3.model.Mode;
import com.example.lenovo.activeandroid3.model.ModeOnOf;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.model.SwitchBoard;
import com.example.lenovo.activeandroid3.model.SwitchButton;
import com.example.lenovo.activeandroid3.util.Constants;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by akshay on 18/5/18.
 */

public class ModeFragmentAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ModeOnOffInterface, ResponseInterfaceNew
{
    String TAG = "ModeFragmentAdapter" ;
    Context context ;
    public ArrayList<ModesActivityDbModel> modeList ;

    // edit dialog
    AlertDialog alertDialog_edit_mode ;
    ImageView iv_back_edit_mode ;
    TextView tv_title_edit_mode , tv_anyText_edit_mode ;
    LinearLayoutManager layout_manager ;
    RecyclerView recycler_view_edit_mode_dialog ;
    EditModeDialogAdapter editModeDialogAdapter ;
    ArrayList<ModelNewDbModel> roomModeList ;

    Long  modeId;

    boolean statusOfModeWhileEditingTheMode ;
    int position = -1 ;
    Mode modeStatusOnTryToEdit ;

    ModeFragmentAdapter my_object = this;

    public ModeFragmentAdapter(Context context)
    {
        this.context = context ;
        this.modeList = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType )
    {
        return new ModeFragmentAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_mode_fragment_adapter , parent , false ));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position )
    {
        if( holder instanceof ModeFragmentAdapter.MyViewHolder )
        {
            Log.e(TAG, "onBindViewHolder: "+position );
            final ModesActivityDbModel singleItem = (ModesActivityDbModel) modeList.get( position ) ;

            ((ModeFragmentAdapter.MyViewHolder) holder ).name.setText( singleItem.getModeName()) ;

            Glide.with(context).load(  singleItem.getModeImage() ).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ((ModeFragmentAdapter.MyViewHolder ) holder ).iv_room.setBackground( drawable ) ;
                    }
                }
            });

            Glide.with(context).load(  R.drawable.edit_mode ).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ((MyViewHolder) holder).iv_mode_edit.setBackground(drawable);
                    }
                }
            });

            ((MyViewHolder) holder).ll_iv_mode_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try {
                        openEditModeDialog( position ) ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            if( singleItem.isOn() )
            {
                ((MyViewHolder) holder).single_grid_item.setBackgroundResource( R.drawable.cornerbackground_click );
            }else {
                ((MyViewHolder) holder).single_grid_item.setBackgroundResource( R.drawable.cornerbackground_unclick );
            }

            ((MyViewHolder) holder ).ll_mode_on_off_click.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                    modeOnOffClick(position );
                }
            });
        }
    }

    private void modeOnOffClick(int position) {

        Log.e("checking here",""+position);

        this.position = position ;
        ModesActivityDbModel singleItem = modeList.get(position);
        Mode mode = getSingleModeById( singleItem.getModeId() ) ;
        Log.e("mode data from table: ",mode.ModeName +" status : "+mode.isOn );
        Log.e("m data frm DbModel",singleItem.getModeName() +" status : "+singleItem.isOn() );

        if( singleItem.isOn() )
        {
            // MODE IS ON , MAKE IT OFF

            Log.e("status is true"," in if" ) ;
            offTheMode( mode , position );

            Log.e(TAG , "After OFF the mode");

        }else {

            //  MODE IS OFF . MAKE IT ON

            Mode activeMode = getModeByIsOnFlag(true);
            if( activeMode == null )
            {
                Log.e(TAG, "All modes are OFF");
                //Log.e("status is false"," in else" ) ;
                offTheMode(mode,position );
            }else
            {
                Log.e(TAG, "One mode is already ON");
                onTheMode(mode,position );
            }
        }
    }

    // please on this mode
    private void onTheMode(Mode mode, int position)
    {
        /**
         *    step 1) get Data From ModeOnOff table w.r.t that modeId
         *    step 2) get All Active rooms
         *    step 3)  find currently ON buttons and OFF buttons.
         *    step 4) here we find out which button need to make ON
         *    step 5) here we find out which button need to make OFF
         *    step 6)  make all mode off from arraylist ( modeList )
         *    step 7) call to networkCall() method with parameter needToONButtonId, needToOFFButtonId, position, singleItem.getModeId()
         *    step 8) start 2 threads for sending ON status to device and
         *            start 2 thread for sending OFF status to device.
         *            thread 5 is start after complition of these 4 threads.
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
         *             for that condition is ( all on button == configuration set in that mode)
         *             else that mode is OFF.
         *             i.e tya mode madhle configure(ON) kelele sagle buttons ON pahijet.
         *             1 pn kami nko 1 pn jast nko. exact same pahijet.tarch to mode ON hoel. else to OFF rahil
         */

        this.position = position ;
        ModesActivityDbModel singleItem = modeList.get( position );

        String message = "ModeOnOff,"+mode.getId()+","+position ;
        Log.e(TAG, "message : "+message );

        // step 1) get Data From ModeOnOff table w.r.t that modeId
        List<ModeOnOf> listModeOnOff = getDataFromModeOnOfTable(singleItem.getModeId());
        ArrayList<Long> ModeOnOffId = new ArrayList<>();
        for (ModeOnOf modeOnOf: listModeOnOff)
        {
            //Log.e(TAG, "onTheMode: ModeId: "+modeOnOf.ModeId +" ButtonId : " + modeOnOf.ButtonId +" ButtonName : " +  modeOnOf.ButtonName );
            ModeOnOffId.add(Long.parseLong(modeOnOf.ButtonId));
        }
        Log.e(TAG, " STEP -1  ModeOnOffId list " +Arrays.toString(ModeOnOffId.toArray()) );


        // step 2) get All Active rooms
        List<Rooms> allActiveRooms = getAllRoom();
        Log.e(TAG, " STEP -2  onTheMode: allActiveRooms size " +allActiveRooms.size() );

        // step 3)  find currently ON buttons and OFF buttons.
        List<SwitchButton> allButtonTotal = new ArrayList<>();
        List<SwitchButton> currentlyONButtons =  new ArrayList<>();
        List<SwitchButton> currentlyOFFButtons =  new ArrayList<>();
        for (Rooms rooms: allActiveRooms )
        {
            List<SwitchButton> allButton = getAllButton(rooms.getId());
            allButtonTotal.addAll(allButton);

            List<SwitchButton> currentlyONButtonsLocal = getButtonsBasedOnIsOnFlag(true , rooms.getId() );
            currentlyONButtons.addAll(currentlyONButtonsLocal);

            List<SwitchButton> currentlyOFFButtonsLocal = getButtonsBasedOnIsOnFlag(false , rooms.getId() );
            currentlyOFFButtons.addAll(currentlyOFFButtonsLocal);
        }

        // these are currently ON buttons
        ArrayList<Long> currentlyONButtonsId = new ArrayList<>();
        for (SwitchButton switchButton:currentlyONButtons)
        {
            currentlyONButtonsId.add(switchButton.getId());
        }
        Log.e(TAG, " STEP -3 currentlyONButtons size " +currentlyONButtons.size() );
        Log.e(TAG, " STEP -3 currentlyONButtonsId list " +Arrays.toString(currentlyONButtonsId.toArray()) );


        // these are currently OFF buttons
        ArrayList<Long> currentlyOFFButtonsId = new ArrayList<>();
        for (SwitchButton switchButton:currentlyOFFButtons)
        {
            currentlyOFFButtonsId.add(switchButton.getId());
        }
        Log.e(TAG, " STEP -3 currentlyOFFButtons size " +currentlyOFFButtons.size() );
        Log.e(TAG, " STEP -3 currentlyOFFButtonsId list " +Arrays.toString(currentlyOFFButtonsId.toArray()) );

        // step 4) here we find out which button need to make ON
        //  konte konte button on karayche aahet.
        //  ModeOnOffId intersection with currentlyOFFButtonsId
        ArrayList<Long> needToONButtonId = new ArrayList<>();
        for (Long id : ModeOnOffId )
        {
            if( currentlyOFFButtonsId.contains(id))
                needToONButtonId.add(id);
        }
        Log.e(TAG, " STEP -4  needToONButtonId size " +needToONButtonId.size() );
        Log.e(TAG, " STEP -4  needToONButtonId list " +Arrays.toString(needToONButtonId.toArray()) );

        //  step 5) here we find out which button need to make OFF
        //   konte konte buton off krayche aahet
        ArrayList<Long> needToOFFButtonId = new ArrayList<>();
        for (Long id : currentlyONButtonsId )
        {
            if( ! ModeOnOffId.contains(id))
                needToOFFButtonId.add(id);
        }
        Log.e(TAG, " STEP -5  needToOFFButtonId size " +needToOFFButtonId.size() );
        Log.e(TAG, " STEP -5  needToOFFButtonId list " +Arrays.toString(needToOFFButtonId.toArray()) );


        //  6)  make all mode off from arraylist ( modeList )
        for( int i = 0 ; i < modeList.size() ; i++ )
        {
            ModesActivityDbModel modesActivityDbModel = modeList.get(i);
            modesActivityDbModel.setOn( false ) ;
        }
        Log.e(TAG, " STEP -6  make all mode off in db" );

        // step 7) call to networkCall() method with parameter needToONButtonId, needToOFFButtonId, position, singleItem.getModeId()
        networkCall(needToONButtonId , needToOFFButtonId  , position , singleItem.getModeId()) ;

        /**
         *    all workable steps when user try to ON any mode
         *    //  stepToONMode();
         */
//          stepsToONMode();
    }

    // please off this mode

    /**
     *   To off mode stes are
     *   step-1) get all active button from ModeOnOff table.
     *   step-2) off that button by sending command to device.
     *
     *
     */
    private void offTheMode(Mode mode, int position)
    {

        String current = Thread.currentThread().getName();
        Log.e(TAG, "offTheMode: current thread name "+current );
        this.position = position ;

        // step-1 :  get all active button from ModeOnOff table.
        List<ModeOnOf> modeOnOfList = getDataFromModeOnOfTable(mode.getId());
        final ArrayList<Long> listOfONButtonId = new ArrayList<>();
        for (ModeOnOf modeOnOf: modeOnOfList )
        {
            listOfONButtonId.add(Long.parseLong(modeOnOf.ButtonId));
        }
        Log.e(TAG, "STEP-1 offTheMode: modeOnOfList list size : "+modeOnOfList.size());
        Log.e(TAG, "STEP-1 offTheMode: listOfONButtonId : "+Arrays.toString(listOfONButtonId.toArray())) ;

//        // We want to start just 2 threads at the same time, but let's control that
//        // timing from the main thread. That's why we have 3 "parties" instead of 2.
//        final CyclicBarrier gate1 = new CyclicBarrier(2);
//        final Thread t6 = new Thread() {
//            public void run()
//            {
//                try {
//                    gate1.await();

        // step-2 :  off that button by sending command to device.
        thread3ForOFFButtonFromMode(listOfONButtonId);

        // step-3 :
        Log.e(TAG, "After finish execution of thread3ForOFFButtonFromMode");
        runOnUiThread();

//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (BrokenBarrierException e) {
//                    e.printStackTrace();
//                }
//            }
//        };

//        Thread t7 = new Thread()
//        {
//            public void run() {
//                try {
//                    String TAG1 = " t7  ";
//                    t6.join();
//
//                    Log.e(TAG+ " "+TAG1, " after finished t6 thread ");
//
//                    ((Activity)context).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            runOnUiThread() ;
//                        }
//                    });
//                } catch (Exception e) {
//                    Log.e(TAG, " excep in t8" + e.getMessage());
//                }
//            }
//        };
//
//        t6.start();
//        t7.start();
//
//        // At this point, t1 to t5 are blocking on the gate.
//        // Since we gave "1" as the argument, gate is not opened yet.
//        // Now if we block on the gate from the main thread, it will open
//        // and all threads will start to do stuff!
//        // it will start all thread at a time
//        try {
//            gate1.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (BrokenBarrierException e) {
//            e.printStackTrace();
//        }
//        System.out.println("all threads started");
    }

    private void networkCall(final ArrayList<Long> needToONButtonId, final ArrayList<Long> needToOFFButtonId, final int position, Long modeId) {

        Log.e(TAG, " STEP -7  Network call method " );
        Log.e(TAG, " STEP -8  start 2 thread for sending ON status to device" );
        Log.e(TAG, " STEP -8  start 2 thread for sending OFF status to device" );
        Log.e(TAG, " STEP -8  start 5 thread after complition of these 4 thread" );

        /**
         *    step 8) start 2 threads for sending ON status to device and
         *            start 2 thread for sending OFF status to device.
         *            thread 5 is start after complition of these 4 threads.
         */

        // We want to start just 4 threads at the same time, but let's control that
        // timing from the main thread. That's why we have 5 "parties" instead of 4.
        final CyclicBarrier gate = new CyclicBarrier(5);


        final Thread t1 = new Thread() {
            public void run() {
                try {
                    gate.await();

                    thread1ForONButtonFromMode(needToONButtonId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        final Thread t2 = new Thread() {
            public void run() {
                try {
                    gate.await();
                    thread2ForONButtonFromMode(needToONButtonId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        };

        final Thread t3 = new Thread() {
            public void run() {
                try {
                    gate.await();
                    thread1ForOFFButtonFromMode(needToOFFButtonId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        };

        final Thread t4 = new Thread() {
            public void run() {
                try {
                    gate.await();
                    thread2ForOFFButtonFromMode(needToOFFButtonId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t5 = new Thread()
        {
            public void run() {
                try {
                    String TAG1 = " t5  ";
                    t1.join();
                    t2.join();
                    t3.join();
                    t4.join();


                    Log.e(TAG+ " "+TAG1, " after finished t1-t2-t3-t4 thread ");
                    Log.e(TAG+ " "+TAG1, " after finished t1-t2-t3-t4 thread "+Thread.currentThread().getName());


                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            runOnUiThread() ;
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, " excep in Thread11" + e.getMessage());
                }
            }
        };

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        t1.setName("t1forModeON");
        t2.setName("t2forModeON");
        t3.setName("t3forModeOFF");
        t4.setName("t4forModeOFF");
        t5.setName("t5AFTER4");

        // At this point, t1 to t5 are blocking on the gate.
        // Since we gave "1" as the argument, gate is not opened yet.
        // Now if we block on the gate from the main thread, it will open
        // and all threads will start to do stuff!

        // it will start all thread at a time
        try {
            gate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("all threads started");
    }

    private void runOnUiThread()
    {
        /**
         *    step 9) prepare ModeOnOff string for broadcasting to call
         */
        Log.e(TAG, "STEP -9 prepare ModeOnOff string for broadcasting to call" );
        Log.e(TAG, "STEP -9 runOnUiThread: position is "+position+"" );
        ModesActivityDbModel modesActivityDbModel = modeList.get(position);
        String finalResponse = "ModeOnOff,"+position+","+modesActivityDbModel.getModeId() ;
        Log.e(TAG,  "STEP -9 finalResponse : " +finalResponse) ;

        ((V1MainActivity)context).broadcastToAllMobileDevices( finalResponse ) ;
    }

    private void thread2ForOFFButtonFromMode(ArrayList<Long> needToOFFButtonId)
    {
        Log.e(TAG, "t4: thread2ForOFFButtonFromMode" );

        if( needToOFFButtonId.size() > 0)
        {
            int halfCount = needToOFFButtonId.size() / 2;

            for (int count = halfCount + 1 ; count < needToOFFButtonId.size() ; count++) {
                Long buttonId = needToOFFButtonId.get(count);

                SwitchButton singleButton = getSingleButton(String.valueOf(buttonId));
                int switchButtonPosition = singleButton.SwitchButtonPosition;
                String roomId = String.valueOf(singleButton.RoomId);
                int action = 1; // 0 say ON and 1 say OFF
                SwitchBoard switchBoard = getBoardById(singleButton.SwitchBoardId);
                String IP = switchBoard.IP;

                if (IP != null)
                {
                        if( switchBoard.IP != null )
                        {
                            Log.e("IP" , switchBoard.IP ) ;
                            int new_position= switchButtonPosition;

                            String requestString =  "*ACT," + roomId + "," + ++new_position + "," + action + "#" ;

                            CommonAsynTaskNew asynTask = new CommonAsynTaskNew( context,requestString , my_object , MethodSelection.BUTTON_CLICK ,switchBoard.IP);
                            if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/)
                            {
                                asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                asynTask.execute();
                            }
                        }else
                        {
                            Toast.makeText(context, "Please restart App", Toast.LENGTH_SHORT).show();
                        }

//                Log.e(TAG, "thread2ForOFFButtonFromMode buttonId " + singleButton.getId());

//                    Socket client = null;
//                    PrintStream printStream = null;
//                    Scanner scanner = null;
//                    try {
//
//                        client = new Socket();
//                        client.connect(new InetSocketAddress(IP, Constants.PORT), ThreadUtil.timeout);
//                        client.setSoTimeout(ThreadUtil.timeout);
//
//                        scanner = new Scanner(client.getInputStream());
//
//                        printStream = new PrintStream(client.getOutputStream());
//
//                        printStream.println("*ACT," + roomId + "," + switchButtonPosition + "," + action + "#");
//
//                        String respons = null ;
//                        if( scanner.hasNext() ) {
//                            respons = scanner.nextLine();
//                        }
//
//                        Log.e(TAG, "thread2ForOFFButtonFromMode Response : " + respons);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } finally {
//
//                        if (printStream != null)
//                            printStream.close();
//
//                        if (scanner != null)
//                            scanner.close();
//
//                        if (client != null)
//                            try {
//                                client.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                    }
                }else
                {
                    Log.e(TAG, "thread2ForOFFButtonFromMode IP is null");
                }
            }
        }
    }

    private void thread1ForOFFButtonFromMode(ArrayList<Long> needToOFFButtonId) {

        Log.e(TAG, "t3: thread1ForOFFButtonFromMode" );

        if( needToOFFButtonId.size() > 0)
        {
            int halfCount = needToOFFButtonId.size() / 2;
            for (int count = 0; count <= halfCount; count++) {
                Long buttonId = needToOFFButtonId.get(count);

                SwitchButton singleButton = getSingleButton(String.valueOf(buttonId));
                int switchButtonPosition = singleButton.SwitchButtonPosition;
                String roomId = String.valueOf(singleButton.RoomId);
                int action = 1; // 0 say ON and 1 say OFF
                SwitchBoard switchBoard = getBoardById(singleButton.SwitchBoardId);
                String IP = switchBoard.IP;

                if (IP != null) {


                    if( switchBoard.IP != null )
                    {
                        Log.e("IP" , switchBoard.IP ) ;
                        int new_position= switchButtonPosition;

                        String requestString =  "*ACT," + roomId + "," + ++new_position + "," + action + "#" ;

                        CommonAsynTaskNew asynTask = new CommonAsynTaskNew( context,requestString , my_object , MethodSelection.BUTTON_CLICK ,switchBoard.IP);
                        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/)
                        {
                            asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            asynTask.execute();
                        }
                    }else
                    {
                        Toast.makeText(context, "Please restart App", Toast.LENGTH_SHORT).show();
                    }





//                Log.e(TAG, "thread1ForONButtonFromMode buttonId " + singleButton.getId());

//                    Socket client = null;
//                    PrintStream printStream = null;
//                    Scanner scanner = null;
//                    try {
//
//                        client = new Socket();
//                        client.connect(new InetSocketAddress(IP, Constants.PORT), ThreadUtil.timeout);
//                        client.setSoTimeout(ThreadUtil.timeout);
//
//                        scanner = new Scanner(client.getInputStream());
//
//                        printStream = new PrintStream(client.getOutputStream());
//
//                        printStream.println("*ACT," + roomId + "," + switchButtonPosition + "," + action + "#");
//
//                        String respons = null ;
//                        if( scanner.hasNext() ) {
//                            respons = scanner.nextLine();
//                        }
//                        Log.e(TAG, "thread1ForOFFButtonFromMode Response : " + respons);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } finally {
//
//                        if (printStream != null)
//                            printStream.close();
//
//                        if (scanner != null)
//                            scanner.close();
//
//                        if (client != null)
//                            try {
//                                client.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                    }
                }else
                {
                    Log.e(TAG, "thread1ForOFFButtonFromMode IP is null");
                }
            }
        }



    }

    private void thread2ForONButtonFromMode(ArrayList<Long> needToONButtonId) {

        Log.e(TAG, "t2: thread2ForONButtonFromMode" );

        if( needToONButtonId.size() > 0)
        {
            int halfCount = needToONButtonId.size() / 2;

            for (int count = halfCount + 1 ; count < needToONButtonId.size() ; count++) {
                Long buttonId = needToONButtonId.get(count);

                SwitchButton singleButton = getSingleButton(String.valueOf(buttonId));
                int switchButtonPosition = singleButton.SwitchButtonPosition;
                String roomId = String.valueOf(singleButton.RoomId);
                int action = 0; // 0 say ON and 1 say OFF
                SwitchBoard switchBoard = getBoardById(singleButton.SwitchBoardId);
                String IP = switchBoard.IP;
                if (IP != null) {




                    if( switchBoard.IP != null )
                    {
                        Log.e("IP" , switchBoard.IP ) ;
                        int new_position= switchButtonPosition;

                        String requestString =  "*ACT," + roomId + "," + ++new_position + "," + action + "#" ;

                        CommonAsynTaskNew asynTask = new CommonAsynTaskNew( context,requestString , my_object , MethodSelection.BUTTON_CLICK ,switchBoard.IP);
                        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/)
                        {
                            asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            asynTask.execute();
                        }
                    }else
                    {
                        Toast.makeText(context, "Please restart App", Toast.LENGTH_SHORT).show();
                    }







//                Log.e(TAG, "thread1ForONButtonFromMode buttonId " + singleButton.getId());

//                    Socket client = null;
//                    PrintStream printStream = null;
//                    Scanner scanner = null;
//                    try {
//
//                        client = new Socket();
//                        client.connect(new InetSocketAddress(IP, Constants.PORT), ThreadUtil.timeout);
//                        client.setSoTimeout(ThreadUtil.timeout);
//
//                        scanner = new Scanner(client.getInputStream());
//
//                        printStream = new PrintStream(client.getOutputStream());
//
//                        printStream.println("*ACT," + roomId + "," + switchButtonPosition + "," + action + "#");
//
//                        String respons = null ;
//                        if( scanner.hasNext() ) {
//                            respons = scanner.nextLine();
//                        }
//
//                        Log.e(TAG, "thread1ForONButtonFromMode Response : " + respons);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } finally {
//
//                        if (printStream != null)
//                            printStream.close();
//
//                        if (scanner != null)
//                            scanner.close();
//
//                        if (client != null)
//                            try {
//                                client.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                    }
                }else
                {
                    Log.e(TAG, "thread2ForONButtonFromMode IP is null");
                }
            }
        }


    }

    private void thread1ForONButtonFromMode(ArrayList<Long> needToONButtonId)
    {
        Log.e(TAG, "t1: thread1ForONButtonFromMode: ");

        if( needToONButtonId.size() > 0)
        {
            int halfCount = needToONButtonId.size() / 2;
            for (int count = 0; count <= halfCount; count++)
            {
                Long buttonId = needToONButtonId.get(count);

                SwitchButton singleButton = getSingleButton(String.valueOf(buttonId));
                int switchButtonPosition = singleButton.SwitchButtonPosition;
                String roomId = String.valueOf(singleButton.RoomId);
                int action = 0; // 0 say ON and 1 say OFF
                SwitchBoard switchBoard = getBoardById(singleButton.SwitchBoardId);
                String IP = switchBoard.IP;

                if( IP != null) {

                    Log.e(TAG, "thread1ForONButtonFromMode buttonId " + singleButton.getId());


                    if( switchBoard.IP != null )
                    {
                        Log.e("IP" , switchBoard.IP ) ;
                        int new_position= switchButtonPosition;

                        String requestString =  "*ACT," + roomId + "," + ++new_position + "," + action + "#" ;

                        CommonAsynTaskNew asynTask = new CommonAsynTaskNew( context,requestString , my_object , MethodSelection.BUTTON_CLICK ,switchBoard.IP);
                        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/)
                        {
                            asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            asynTask.execute();
                        }
                    }else
                    {
                        Toast.makeText(context, "Please restart App", Toast.LENGTH_SHORT).show();
                    }


//                    Socket client = null;
//                    PrintStream printStream = null;
//                    Scanner scanner = null;
//                    try {
//
//                        client = new Socket();
//                        client.connect(new InetSocketAddress(IP, Constants.PORT), ThreadUtil.timeout);
//                        client.setSoTimeout(ThreadUtil.timeout);
//
//                        scanner = new Scanner(client.getInputStream());
//
//                        printStream = new PrintStream(client.getOutputStream());
//
//                        printStream.println("*ACT," + roomId + "," + switchButtonPosition + "," + action + "#");
//
//                        String respons = null ;
//                        if( scanner.hasNext() ) {
//                            respons = scanner.nextLine();
//                        }
//
//                        Log.e(TAG, "thread1ForONButtonFromMode Response : " + respons);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } finally {
//
//                        if (printStream != null)
//                            printStream.close();
//
//                        if (scanner != null)
//                            scanner.close();
//
//                        if (client != null)
//                            try {
//                                client.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                    }
                }else
                {
                    Log.e(TAG, "thread1ForONButtonFromMode IP is null");
                }
            }
        }
    }

    SwitchBoard getBoardById(Long boardId )
    {
        return new Select().from(SwitchBoard.class).where("id = ?", boardId ).executeSingle();
    }




    private void thread3ForOFFButtonFromMode(ArrayList<Long> listOfONButtonId)
    {
        Log.e(TAG, "thread3ForOFFButtonFromMode");

//        Long buttonId = listOfONButtonId.get(0);
//
//        SwitchButton singleButton = getSingleButton(String.valueOf(buttonId));
//        Log.e(TAG, "SwitchButtonName: "+singleButton.SwitchButtonName );
//        int switchButtonPosition = singleButton.SwitchButtonPosition;
//        String roomId = String.valueOf(singleButton.RoomId);
//        int action = 1; // 0 say ON and 1 say OFF
//        SwitchBoard switchBoard = getBoardById(singleButton.SwitchBoardId);
//        Log.e(TAG, "SwitchButtonName: "+switchBoard.BoardName );
//        String IP = switchBoard.IP;
//
//        if (IP != null) {
//
//            Log.e(TAG, "thread3ForOFFButtonFromMode buttonId and IP" + singleButton.getId()+"  "+IP );
//
//            Socket client = null;
//            PrintStream printStream = null;
//            Scanner scanner = null;
//            try {
//                client = new Socket();
//                client.connect(new InetSocketAddress(IP, Constants.PORT), ThreadUtil.timeout);
//                client.setSoTimeout(ThreadUtil.timeout);
//
//                scanner = new Scanner(client.getInputStream());
//
//                printStream = new PrintStream(client.getOutputStream());
//
//                printStream.println("*ACT," + roomId + "," + switchButtonPosition + "," + action + "#");
//
//                String respons = scanner.nextLine();
//
//                Log.e(TAG, "thread3ForOFFButtonFromMode Response : " + respons);
//
//            } catch (IOException e) {
//                Log.e(TAG, "EXCEP in thread3ForOFFButtonFromMode while connecting to" + IP);
//                e.printStackTrace();
//            } finally {
//
//                if (printStream != null)
//                    printStream.close();
//
//                if (scanner != null)
//                    scanner.close();
//
//                if (client != null)
//                    try {
//                        client.close();
//                    } catch (IOException e)
//                    {
//                        e.printStackTrace();
//                    }
//            }
//        } else {
//            Log.e(TAG, "thread3ForOFFButtonFromMode IP is null");
//        }

        for (int i = 0 ; i < listOfONButtonId.size() ; i++)
        {
            Long buttonId = listOfONButtonId.get(i);

            SwitchButton singleButton = getSingleButton(String.valueOf(buttonId));
            Log.e(TAG, "singleButton name "+singleButton.SwitchButtonName);
            int switchButtonPosition = singleButton.SwitchButtonPosition;
            String roomId = String.valueOf(singleButton.RoomId);
            int action = 1; // 0 say ON and 1 say OFF
            SwitchBoard switchBoard = getBoardById(singleButton.SwitchBoardId);
            String IP = switchBoard.IP;
            Log.e(TAG, "IP is :"+ IP);

            if (IP != null && !IP.isEmpty())
            {
                Log.e(TAG, "thread3ForOFFButtonFromMode buttonId and IP" + singleButton.getId()+"  "+IP);

                if( switchBoard.IP != null )
                {
                    Log.e("IP" , switchBoard.IP ) ;
                    int new_position= switchButtonPosition;

                    String requestString =  "*ACT," + roomId + "," + ++new_position + "," + action + "#" ;

                    CommonAsynTaskNew asynTask = new CommonAsynTaskNew( context,requestString , my_object , MethodSelection.BUTTON_CLICK ,switchBoard.IP);
                    if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/)
                    {
                        asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        asynTask.execute();
                    }
                }else
                {
                    Toast.makeText(context, "Please restart App", Toast.LENGTH_SHORT).show();
                }




//                Socket client = null;
//                PrintStream printStream = null;
//                Scanner scanner = null;
//                try {
//                    client = new Socket();
//                    client.connect(new InetSocketAddress(IP, Constants.PORT), ThreadUtil.timeout);
//                    client.setSoTimeout(ThreadUtil.timeout);
//
//                    scanner = new Scanner(client.getInputStream());
//
//                    printStream = new PrintStream(client.getOutputStream());
//
//                    printStream.println("*ACT," + roomId + "," + switchButtonPosition + "," + action + "#");
//
//                    String respons = null  ;
//                    if( scanner.hasNext() ) {
//                        respons = scanner.nextLine();
//                    }
//
//                    Log.e(TAG, "thread3ForOFFButtonFromMode Response : " + respons);
//
//                } catch (IOException e) {
//                    Log.e(TAG, "EXCEP in thread3ForOFFButtonFromMode while connecting to" + IP);
//                    e.printStackTrace();
//                } finally {
//
//                    if (printStream != null)
//                        printStream.close();
//
//                    if (scanner != null)
//                        scanner.close();
//
//                    if (client != null)
//                        try {
//                            client.close();
//                        } catch (IOException e)
//                        {
//                            e.printStackTrace();
//                        }
//                }
            } else {
                Log.e(TAG, "thread3ForOFFButtonFromMode IP is null for board "+switchBoard.BoardName);
            }
        }
    }

    private void openEditModeDialog(int position) throws JSONException {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme);
        LayoutInflater inflater;
        inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.v1_dialog_edit_mode, null);
        dialogBuilder.setView(dialogView);

        alertDialog_edit_mode = dialogBuilder.create();
        alertDialog_edit_mode.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog_edit_mode.show();

        inflateDialogToolbarForAddLight(dialogView ) ;

        ModesActivityDbModel singleItem = (ModesActivityDbModel) modeList.get( position ) ;
        this.position = position ;
        modeId = singleItem.getModeId() ;
        Log.e("mode id and status  " ,singleItem.getModeId()+" "+singleItem.isOn() ) ;
        statusOfModeWhileEditingTheMode  = singleItem.isOn();

        if( statusOfModeWhileEditingTheMode  )
        {
            modeStatusOnTryToEdit = getSingleModeById(modeId);
        }

        this.recycler_view_edit_mode_dialog = (RecyclerView) dialogView.findViewById(R.id.recycler_view_edit_mode_dialog );
        layout_manager = new LinearLayoutManager(context);
        this.recycler_view_edit_mode_dialog.setLayoutManager(layout_manager);
        editModeDialogAdapter = new EditModeDialogAdapter( ModeFragmentAdapter.this ,context  , alertDialog_edit_mode, modeId   );
        this.recycler_view_edit_mode_dialog.setAdapter( editModeDialogAdapter );

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recycler_view_edit_mode_dialog.getContext()  ,
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(context , R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recycler_view_edit_mode_dialog.addItemDecoration(horizontalDecoration);

        List<Rooms> listRoom =  getAllRoom() ;
//        Log.e("listRoom size" ,listRoom.size()+"" ) ;

        roomModeList = new ArrayList<>();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        for( int i= 0 ;i < listRoom.size(); i++ )
        {
            Rooms room = listRoom.get( i ) ;

            ArrayList<SwitchButtonDbModel>  switchButtonDbModelList= new ArrayList<>();
            List<SwitchButton> buttonList  =   getAllButtonOfRoom( room.getId() ) ;

            for( int j= 0 ; j < buttonList.size() ; j++ )
            {
                SwitchButton button =   buttonList.get( j );
                SwitchButtonDbModel switchButtonDbModel = new SwitchButtonDbModel(
                        button.getId(),
                        button.SwitchButtonName,
                        button.SwitchBoardId,
                        button.SwitchButtonCreatedAt,
                        button.SwitchButtonUpdatedat,
                        button.is_on,
                        button.RoomId,
                        button.OnImage,
                        button.OffImage,
                        i ,  // this flag is used only for editmode dialog , this is room position
                        j // 1 ka card vr chya button chi position , 1 ka card vr jr mi click kel tr tya card vr te button kiti position vr aahe. he bhetat nahi. tyasathi ha flag set kela aahe
                        //  ha flag mode edit krtani button chya click vr use kela aahe.
                        //  room madhlya button ch staus change kel aani jri recyclerview scroll kela tr status prt default set nahi hou mhanun. ha flag use kela aahe
                );
                switchButtonDbModelList.add( switchButtonDbModel );
            }
            String switchButtonDbModelListString =  gson.toJson( switchButtonDbModelList , ArrayList.class) ;
            ModelNewDbModel modelNewDbModel =  new ModelNewDbModel( room.getId() ,room.Name ,room.CreatedAt,room.Updatedat,room.RoomHomePageImage ,room.AddRoomImage,room.RoomOnOff , switchButtonDbModelListString) ;
            roomModeList.add( modelNewDbModel );
        }
        editModeDialogAdapter.addItem( roomModeList) ;
    }


    private void inflateDialogToolbarForAddLight(View dialogView )
    {
        iv_back_edit_mode = ( ImageView )dialogView.findViewById( R.id.iv_back ) ;
        tv_title_edit_mode = ( TextView )dialogView.findViewById( R.id.tv_title );
        tv_anyText_edit_mode = ( TextView )dialogView.findViewById( R.id.tv_anyText );

        tv_title_edit_mode.setText("Edit Mode");
        tv_anyText_edit_mode.setText("Save");
        iv_back_edit_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_edit_mode.cancel();
            }
        });

        tv_anyText_edit_mode.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("in" ,"save") ;
                Log.e("arrayButtonId size" , editModeDialogAdapter.arrayButtonId.size()+"" ) ;

                // first delete all data from ModeOnOff table with given modeId
                deleteModeOnOfDataWithModeId() ;

                // it is just cross check.  modeOnOfList size must be 0 .
                List<ModeOnOf> modeOnOfList =  getDataFromModeOnOfTable( modeId ) ;
                Log.e("after truncate ","modeOnOfList size  "+ modeOnOfList.size() ) ;

                // step 3 :  from arrayButtonId( selected button in mode while edit) get name and id and save it into
                // buttonNameList and buttonIdList object.
                ArrayList<String> buttonNameList  = new ArrayList<String>();
                ArrayList<String> buttonIdList  = new ArrayList<String>() ;
                for( int i = 0 ; i< editModeDialogAdapter.arrayButtonId.size()  ; i++ )
                {
                    String buttonId = editModeDialogAdapter.arrayButtonId.get( i ) ;
                    Log.e("buttonId ",buttonId );

                    // access by id
                    SwitchButton switchButton =  new Select().from(SwitchButton.class).where("id = ?", buttonId ).executeSingle() ;
                    Log.e("buttonName and Id ", switchButton.SwitchButtonName +"  "+switchButton.getId()   );
                    buttonIdList.add( String.valueOf( switchButton.getId() ));
                    buttonNameList.add( switchButton.SwitchButtonName );
                }

                //step 4 : insert new data in ModeOnOff table using buttonNameList and buttonIdList .
                ActiveAndroid.beginTransaction();
                try {
                    for (int i = 0; i < buttonNameList.size() ; i++)
                    {
                        try {
                            Date currentTime = Calendar.getInstance().getTime() ;
                            ModeOnOf modeOnOf = new ModeOnOf() ;
                            modeOnOf.CreatedAt = currentTime.getTime() ;
                            modeOnOf.Updatedat = currentTime.getTime() ;
                            modeOnOf.ModeId =  String.valueOf( editModeDialogAdapter.modeId );
                            modeOnOf.ButtonName = buttonNameList.get(i)  ;
                            modeOnOf.ButtonId = buttonIdList.get(i)  ;
                            modeOnOf.save() ;
                        }catch ( Exception e )
                        {
                            Log.e("excep while getTime",e.getMessage() );
                        }
                    }
                    ActiveAndroid.setTransactionSuccessful() ;
                } finally {
                    ActiveAndroid.endTransaction();
                }

                if( statusOfModeWhileEditingTheMode )
                {
//                    offTheMode( modeStatusOnTryToEdit ,position );
                    onTheMode(modeStatusOnTryToEdit, position );
                    position=-1;
                }

//                List<ModeOnOf> modeOnOfList1 =  getDataFromModeOnOfTable(modeId ) ;
//                Log.e("after insert new data","modeOnOfList1 size  "+ modeOnOfList1.size() ) ;

//                editModeDialogAdapter.arrayButtonId
//                new Delete().from( ModeOnOf.class ).where("ButtonId = ?", String.valueOf( singleItem.getId() )  ).execute() ;


//                String lightName =  et_add_light.getText().toString();
//                Log.e("lightName : ",lightName );
//
//                if(  checkString( lightName ))
//                {
//                    Log.e("Light String is","null");
//                }else {
//
//                    Log.e("Light String not","null so add it to DB");
//
//                    Date timestamp = Calendar.getInstance().getTime();
//                    SwitchButton switchButton = new SwitchButton();
//                    switchButton.SwitchBoardId = "0";
//                    switchButton.is_on = false;
//                    switchButton.SwitchButtonName = lightName ;
//                    switchButton.RoomId = roomId;
//                    switchButton.OnImage = R.drawable.light_click ;
//                    switchButton.OffImage = R.drawable.light;
//                    switchButton.SwitchButtonCreatedAt = timestamp.getTime();
//                    switchButton.SwitchButtonUpdatedat = timestamp.getTime();
//                    switchButton.save();
//
//
//                    // when  alertDialog_add_light.cancel() is executed
//                    // alertDialog_add_light.setOnCancelListener method is called. And decide what to do
//                    makeIsButtonAddedOrDeletdFlagTrue() ;
//                }
//
                alertDialog_edit_mode.cancel();
            }
        });
    }


    @Override
    public void modeOnOffResponse(String response)
    {
        Log.e(TAG, "modeOnOffResponse: "+ response );

    }

    // Read all Data
    private List<Rooms> getAllRoom()
    {
        //  order by id
        return new Select().from(Rooms.class).orderBy("id ASC").where("RoomOnOff = ?", 1 ).execute();

        // return data in random order
        // return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();
    }


    private void truncateModeOnOfModelTable(Class<ModeOnOf> modeOnOfClass)
    {
//        TableInfo tableInfo = Cache.getTableInfo( modeOnOfClass  );
//        ActiveAndroid.execSQL(
//                String.format("DELETE FROM %s;",
//                        tableInfo.getTableName()));
//
//        ActiveAndroid.execSQL(
//                String.format("DELETE FROM sqlite_sequence WHERE name='%s';",
//                        tableInfo.getTableName()));
    }

    private  void deleteModeOnOfDataWithModeId( )
    {
        new Delete().from( ModeOnOf.class ).where("ModeId = ?", modeId  ).execute() ;
    }

    private List<ModeOnOf> getDataFromModeOnOfTable( Long  modeId )
    {
        //  order by id
        return new Select().from( ModeOnOf.class).where("ModeId = ?", modeId ).execute() ;
    }

    private List<SwitchButton> getAllButttons()
    {
        return new Select().from( SwitchButton.class ).orderBy("id ASC").execute() ;
    }

    private List<SwitchButton> getButtonsBasedOnIsOnFlag( boolean status ,Long  roomId)
    {
        return new Select().from(SwitchButton.class).where("is_on = ? AND RoomId = ?", status , roomId).execute();
    }


//    where("Jid = ? AND MyJid = ?", "yyyy", "xxxx")

    private List<SwitchButton> getAllButtonOfRoom(Long roomId)
    {

        //  order by id
//        return new Select().from( SwitchBoard.class ).orderBy("id ASC").execute() ;

        // access by id
//        return new Select().from(SwitchBoard.class).where("id = ?", roomId ).execute();


        // access by RoomId
        return new Select().from( SwitchButton.class ).where("RoomId = ?", roomId ).execute() ;

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();
    }


    private Mode getSingleModeById( Long modeId )
    {
        // access by id
        return new Select().from(Mode.class).where("id = ?", modeId ).executeSingle();
    }

    private List<Mode> getAllMode()
    {
        // access by id
        return new Select().from(Mode.class).orderBy("id ASC").execute() ;
    }

    private Mode getModeByIsOnFlag(boolean status)
    {
        // access by id
        return new Select().from(Mode.class).where("isOn = ?", status ).executeSingle();
    }


    SwitchBoard readSingleBoardById(Long boardId )
    {
        return new Select().from(SwitchBoard.class).where("id = ?", boardId ).executeSingle();
    }

    private List<SwitchButton> getAllButton(Long roomId)
    {
        //  order by id
        return new Select().from( SwitchButton.class ).where("RoomId = ?", roomId ).orderBy("id ASC").execute() ;
    }



    private SwitchButton getSingleButton(String buttonId )
    {
        // access by id
        return new Select().from(SwitchButton.class).where("id = ?", buttonId ).executeSingle() ;
    }

    @Override
    public int getItemCount()
    {
        return modeList.size() ;
    }

    public void addItem(ArrayList<ModesActivityDbModel> lst)
    {
        modeList.addAll(lst);
        notifyDataSetChanged();
    }

    @Override
    public void getResponse(String response, MethodSelection interface_method, String IP) {
        try {

            Log.e(TAG, "getResponse: "+response );

            switch ( interface_method ) {

                case BUTTON_CLICK :
//                    this.buttonClickResponse(response);
                    break;

            }

        }catch (Exception e) {

            Log.e( "in getResponse" , e.getMessage() );
        }
    }


    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name ;
        LinearLayout ll_iv_mode_edit , ll_mode_on_off_click ;
        public ImageView iv_edit  , iv_room  , iv_mode_edit;
        FlexboxLayout single_grid_item ;
        public MyViewHolder( View view )
        {
            super( view ) ;
            name = ( TextView ) view.findViewById( R.id.name ) ;
            iv_room =(ImageView )view.findViewById( R.id.iv_room ) ;
            ll_iv_mode_edit =(LinearLayout ) view.findViewById( R.id.ll_iv_mode_edit ) ;
            ll_mode_on_off_click = ( LinearLayout )view.findViewById( R.id.ll_mode_on_off_click );
            iv_mode_edit = ( ImageView)view.findViewById( R.id.iv_mode_edit );
            single_grid_item = ( FlexboxLayout )view.findViewById( R.id.single_grid_item );
        }
    }

    public void setListNull()
    {
        modeList.clear();
    }

    private void stepsToONMode() {
//        //  1)  make all mode off from arraylist ( modeList )
//        for( int i = 0 ; i < modeList.size() ; i++ )
//        {
//            ModesActivityDbModel modesActivityDbModel = modeList.get(i);
//            modesActivityDbModel.setOn( false ) ;
//
//        }
//
//        // 2) get all mode and off them from DB
//        List<Mode >  modeData =  getAllMode() ;
//        for ( Mode mode1 : modeData  )
//        {
//            mode1.isOn = false ;
//            mode1.save() ;
//        }
//
//        // 3)  get all button from DB and off them
//        List<SwitchButton> switchButtons = getAllButton();
//        for (SwitchButton button : switchButtons) {
//            button.is_on = false;
//            button.save();
//        }
//
//        // 4)  get all button with selected modeId from ModeOnOf table
//        //     and make all button ON and save in DB.
//        List<ModeOnOf> listModeOnOff = getDataFromModeOnOfTable(singleItem.getModeId());
//        for (ModeOnOf modeOnOff : listModeOnOff) {
//            SwitchButton switchButton = getSingleButton(modeOnOff.ButtonId);
//            Log.e("Button id & name rName", switchButton.getId() + "  " + switchButton.SwitchButtonName + "  " + switchButton.RoomId + "");
//            switchButton.is_on = true;
//            Date currentTime = Calendar.getInstance().getTime();
//            switchButton.SwitchButtonUpdatedat = currentTime.getTime();
//            switchButton.save();
//        }
//
        // 5) make selected mode ON in DB and save
//        mode.isOn = true;
//        mode.save();
//
//        //   ON selected mode status in arraylist object
//        singleItem.setOn(true);
//
//        notifyDataSetChanged();
    }
}
