package com.example.lenovo.activeandroid3.activity.v1.adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.MainActivity;
import com.example.lenovo.activeandroid3.activity.ResponseInterfaceNew;
import com.example.lenovo.activeandroid3.activity.v1.WrapContentLinearLayoutManager;
import com.example.lenovo.activeandroid3.activity.v1.activity.V1MainActivity;
import com.example.lenovo.activeandroid3.activity.v1.fragment.RoomFragment;
import com.example.lenovo.activeandroid3.activity.v1.utils.MethodSelection;
import com.example.lenovo.activeandroid3.activity.v1.utils.ThreadUtil;
import com.example.lenovo.activeandroid3.asyntask.CommonAsynTaskNew;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.model.SwitchBoard;
import com.example.lenovo.activeandroid3.model.SwitchButton;
import com.example.lenovo.activeandroid3.util.Constants;
import com.example.lenovo.activeandroid3.util.Conversions;
import com.google.android.flexbox.FlexboxLayout;

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by akshay on 17/5/18.
 */

public class RoomFragmentAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ResponseInterfaceNew {
    String TAG = "RoomFragmentAdapter" ;
    Context context ;
    MainActivity activity = null ;
    public ArrayList<RoomDbModel>  roomList ;
    private RoomFragment roomFragment;
    private AlertDialog alertDialog = null ;
    private AlertDialog alertDialog_add_light;
    private RecyclerView recycler_view_room_inside_dialog , recycler_view_add_light_dialog ;
    private LinearLayoutManager layout_manager ;

    //adapter
    public RoomInsideDialogAdapter roomInsideDialogAdapter ;
    private AddLightDialogAdapter  addLightDialogAdapter;

    boolean isModeDataNeedToGet = false ;

    // make this flag true. when AddLightDialogAdapter added or removed any button.
    // when dialog is closed after adding or removing button , RoomInsideDialogAdapter need to fetch latest data from Room table.
    public boolean isButtonAddedOrDeletedFlag = false ;

    //arrayList
    private ArrayList<SwitchButtonDbModel> switchButtonList ;

    //
    // Add light dialog declaration
    ImageView iv_back_add_light ;
    TextView tv_anyText_add_light ,tv_title_add_light ;
    EditText et_add_light;

    RoomFragmentAdapter my_object = this;
    private int position;

    boolean is_connection_active = false;

    public RoomFragmentAdapter( Context context ,  RoomFragment roomFragment )
    {
        this.context = context ;
        this.roomFragment =roomFragment ;
        this.roomList = new ArrayList<>() ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType )
    {
        return new RoomFragmentAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_room_fragment_adapter , parent , false ));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position ) {
        if (holder instanceof RoomFragmentAdapter.MyViewHolder) {
            final RoomDbModel singleItem = (RoomDbModel) roomList.get(position);

            // if getRommOnOff flag is 1 then , show room on main screen.
            if (singleItem.getRoomOnOff() == 1) {
                if (Conversions.checkString(singleItem.getRoomHomePageImage())) {
                    Glide.with(context).load(R.drawable.home1).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                ((RoomFragmentAdapter.MyViewHolder) holder).iv_room.setBackground(drawable);
                            }
                        }
                    });
                } else {
                    Glide.with(context).load(Integer.parseInt(singleItem.getRoomHomePageImage())).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                ((RoomFragmentAdapter.MyViewHolder) holder).iv_room.setBackground(drawable);
                            }
                        }
                    });
                }
                ((RoomFragmentAdapter.MyViewHolder) holder).name.setText(singleItem.getrName());

                ((MyViewHolder) holder).ll_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (alertDialog == null) {



                            getStatus(position);
//                            openRoom(position);

                            // send 10 command to server socket without waiting for response calculate time
//                            send10CommandToSocketWithoutWaitingForResponseAndCalculateTimeRequired() ;

                            // send 10 command to server socket without waiting for response calculate time
                        }
                    }
                });

                if (singleItem.isAtLeastOneButtonOfRoomIsOn()) {
                    ((MyViewHolder) holder).single_grid_item.setBackgroundResource(R.drawable.cornerbackground_click);
                } else {
                    ((MyViewHolder) holder).single_grid_item.setBackgroundResource(R.drawable.cornerbackground_unclick);
                }
            } else {
                holder.itemView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void getStatus(int position) {
        Log.e(TAG, "openRoom: " );
        this.position = position;
        final RoomDbModel roomDbModel =   roomList.get( position );

        SwitchBoard switchBoard = getSingleSwitchBoardFromRoomId(roomDbModel.getId().toString());
        Log.e(TAG, "openRoom: BoardName "+switchBoard.BoardName );

        String statusRequest = "*STS," + switchBoard.getId() + "#";
        CommonAsynTaskNew asynTask = new CommonAsynTaskNew(context, statusRequest, my_object, MethodSelection.STATUS, switchBoard.IP);
        if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asynTask.execute();
        }
    }


    private void send10CommandToSocketWithoutWaitingForResponseAndCalculateTimeRequired()
    {
        Log.e(TAG , " send10CommandToSocketWithoutWaitingForResponseAndCalculateTimeRequired");

        try {

            Log.e(TAG , " in try");
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("192.168.1.106", Constants.PORT), ThreadUtil.timeout );
            Log.e(TAG , " after connect ");
            PrintStream printStream = new PrintStream(socket.getOutputStream());

            long startTime =   System.currentTimeMillis();

            for (int i = 0; i < 10 ; i++ )
            {
                Log.e(TAG , " in for "+ i);
                printStream.println("abc");
            }
            long endTime =   System.currentTimeMillis();
            long diffInDays = getDateDiff(startTime, endTime, TimeUnit.MICROSECONDS);
            Log.e(TAG , " diffInDays MICROSECONDS : "+ diffInDays);
        } catch (Exception e)
        {
            Log.e(TAG , " Exception : "+e.getMessage());
            e.printStackTrace();
        }
    }

    public static long getDateDiff(long timeUpdate, long timeNow, TimeUnit timeUnit)
    {
        long diffInMillies = Math.abs(timeNow- timeUpdate);
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    private void openRoom(final int position)
    {
        final RoomDbModel roomDbModel =   roomList.get( position );

//         isRoomOpenFlag is true then only update the arrayList object of RoomInsideDialogAdapter class.
        ((V1MainActivity)context).setIsRoomOpenFlag(true) ;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme);
        LayoutInflater inflater;
        inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.v1_dialog_room_inside, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.show();

        isModeDataNeedToGet = true ;

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                boolean atLeastOneButtonOfRoomIsOn = false ;
                isModeDataNeedToGet = false ;

                // isRoomOpenFlag is true then only update the arrayList object of RoomInsideDialogAdapter class.
                ((V1MainActivity)context).setIsRoomOpenFlag( false ) ;

                RoomDbModel roomDbModel1 = roomList.get( position ) ;
                List<SwitchButton> buttonList =  roomFragment.getAllButtonOfRoom(roomDbModel1.getId()  ) ;
                for ( SwitchButton button : buttonList )
                {
                    if( button.is_on )
                        atLeastOneButtonOfRoomIsOn = true ;
                }
                if( atLeastOneButtonOfRoomIsOn )
                {
                    roomDbModel1.setAtLeastOneButtonOfRoomIsOn( true ) ;
                    notifyItemChanged( position );
                }else {
                    roomDbModel1.setAtLeastOneButtonOfRoomIsOn( false ) ;
                    notifyItemChanged( position );
                }
                alertDialog = null;
            }
        });


        this.recycler_view_room_inside_dialog = (RecyclerView) dialogView.findViewById(R.id.recycler_view_room_inside_dialog );

//        LinearLayout ll_tv_save = (LinearLayout)dialogView.findViewById(R.id.ll_tv_save );
        TextView tv_active_status = (TextView) dialogView.findViewById(R.id.tv_active_status);

        if(is_connection_active){
            tv_active_status.setVisibility(View.VISIBLE);

        }else{
            tv_active_status.setVisibility(View.GONE);
        }
        ImageView in_back_arrow = (ImageView)dialogView.findViewById( R.id.in_back_arrow );
        TextView tv_room_name_inside = ( TextView )dialogView.findViewById( R.id.tv_room_name_inside );
        TextView tv_anyText = ( TextView )dialogView.findViewById( R.id.tv_anyText );
        tv_anyText.setText("Add");

        // it hide the add option present inside room
        tv_anyText.setVisibility(View.GONE);
        tv_room_name_inside.setText(roomDbModel.getrName() );
        in_back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        tv_anyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddLightDialog( roomDbModel.getId() ) ;
            }
        });

        layout_manager = new LinearLayoutManager(context);
        this.recycler_view_room_inside_dialog.setLayoutManager(layout_manager);
        roomInsideDialogAdapter = new RoomInsideDialogAdapter( this ,context  , alertDialog , position );
        this.recycler_view_room_inside_dialog.setAdapter( roomInsideDialogAdapter );
        getRoomDataAndSetToRoomInsideDialogAdapter( roomDbModel.getId() ) ;

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recycler_view_room_inside_dialog.getContext()  ,
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(context , R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recycler_view_room_inside_dialog.addItemDecoration(horizontalDecoration);

        recycler_view_room_inside_dialog.setLayoutManager(new WrapContentLinearLayoutManager(context));
    }

    @Override
    public void getResponse(String response, MethodSelection interface_method, String IP) {


        try {
            Log.e(TAG, "getResponse: "+response );

            switch ( interface_method ) {

//                case ID :
//                    this.idOfDevice(response,IP);
//                    break;

                case STATUS:
                    this.statusResponse(response,IP );
                    break;
            }
        }catch (Exception e) {
            Log.e( "in getResponse" , e.getMessage() );
        }


    }

    private void statusResponse(String response, String IP) {

        if (response != null) {
            is_connection_active = true;
            Log.e(TAG, "statusResponse: "+IP +"  "+ response );
            String data1 = response.substring(response.indexOf(",") + 1);
            String deviceIdFromResponse = data1.substring(0, data1.indexOf(","));
            String data2 = data1.substring(data1.indexOf(",") + 1);

            List<SwitchButton> allButtonFromBoardId = getAllButtonFromBoardId(Long.parseLong(deviceIdFromResponse));

            Log.e(TAG, "STATUS:  data2 :  " + data2);
            String numberOnly = data2.replaceAll("[^0-9]", "");
            Log.e(TAG, "STATUS: numberOnly is : " + numberOnly);

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
            openRoom(position);
        } else {
            Log.e(TAG, "STATUS: status response is null");
            is_connection_active = false;
            openRoom(position);
        }

    }

    List<SwitchButton> getAllButtonFromBoardId(Long boardId) {
//        return new Select().from(SwitchButton.class).where("is_on = ?", true ).where("id =?", roomId ).execute();
//        return new Select().from(SwitchButton.class).where("is_on = ? AND RoomId = ?", boardId).execute();
        return new Select().from(SwitchButton.class).where("SwitchBoardId = ?", boardId).execute();
    }

    
    private void getRoomDataAndSetToRoomInsideDialogAdapter(Long roomId)
    {
        List<SwitchButton>  roomButtonList =  getAllButtonOfRoom( roomId ) ;
        setDataToRoomInsideDialogAdapter(roomButtonList ) ;
    }

    private void openAddLightDialog(final Long roomId )
    {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme);
        LayoutInflater inflater;
        inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.v1_dialog_add_light, null);
        dialogBuilder.setView(dialogView);
        alertDialog_add_light = dialogBuilder.create();
        alertDialog_add_light.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog_add_light.show();
        isButtonAddedOrDeletedFlag = false ;

        alertDialog_add_light.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // if button is deleted then only call this method from here otherwise no need to call
                if( isButtonAddedOrDeletedFlag )
                    getRoomDataAndSetToRoomInsideDialogAdapter( roomId ) ;
            }
        });

        Button btn_add = ( Button )dialogView.findViewById( R.id.btn_add );
        btn_add.setVisibility( View.GONE);

        et_add_light = ( EditText )dialogView.findViewById(R.id.et_add_light ) ;

        inflateDialogToolbarForAddLight(dialogView  , roomId) ;

        this.recycler_view_add_light_dialog = (RecyclerView) dialogView.findViewById(R.id.recycler_view_add_light_dialog );
        layout_manager = new LinearLayoutManager(context);
        this.recycler_view_add_light_dialog.setLayoutManager(layout_manager);
        addLightDialogAdapter = new AddLightDialogAdapter( RoomFragmentAdapter.this ,context  , alertDialog_add_light );
        this.recycler_view_add_light_dialog.setAdapter( addLightDialogAdapter );

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recycler_view_add_light_dialog.getContext()  ,
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(context , R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recycler_view_add_light_dialog.addItemDecoration(horizontalDecoration);

        addLightDialogAdapter.addItem(  switchButtonList ) ;
    }

    private void inflateDialogToolbarForAddLight(View dialogView , final Long roomId)
    {
        iv_back_add_light = ( ImageView )dialogView.findViewById( R.id.iv_back ) ;
        tv_title_add_light = ( TextView )dialogView.findViewById( R.id.tv_title );
        tv_anyText_add_light = ( TextView )dialogView.findViewById( R.id.tv_anyText );

        tv_title_add_light.setText("Add Light");
        tv_anyText_add_light.setText("Save");
        iv_back_add_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_add_light.cancel();
            }
        });

        tv_anyText_add_light.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {


                String lightName =  et_add_light.getText().toString();
                Log.e("lightName : ",lightName );

                if(  checkString( lightName ))
                {
                    Log.e("Light String is","null");
                }else {

                    Log.e("Light String not","null so add it to DB");

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


                    // when  alertDialog_add_light.cancel() is executed
                    // alertDialog_add_light.setOnCancelListener method is called. And decide what to do
                    makeIsButtonAddedOrDeletdFlagTrue() ;
                }

                alertDialog_add_light.cancel();
            }
        });

    }

    private void makeIsButtonAddedOrDeletdFlagTrue()
    {
        // fetch latest data from Room table if this flag is true.
        isButtonAddedOrDeletedFlag = true ;
    }

    private void setDataToRoomInsideDialogAdapter(List<SwitchButton> roomButtonList)
    {
        switchButtonList = new ArrayList<>();
        for (SwitchButton switchButton : roomButtonList)
        {

            switchButtonList.add( new SwitchButtonDbModel(switchButton.getId() ,
                    switchButton.SwitchButtonName ,
                    switchButton.SwitchBoardId
                    ,switchButton.SwitchButtonCreatedAt,
                    switchButton.SwitchButtonUpdatedat,
                    switchButton.is_on,
                    switchButton.RoomId,
                    switchButton.OnImage,
                    switchButton.OffImage,
                    1,
                    1 // card vr room madhe button kiti position la aahe he samjav mhanun ha flag set kela aahe.pn ethe ha fakt dummy flag aahe.ethe yach use nahi.
                    // Actucally ha flag fakt ModeFragmentAdapter madhech use kela aahe.
            )) ;
        }
//        Log.e("switchButtonList size ",switchButtonList.size()+"") ;
        roomInsideDialogAdapter.addItem( switchButtonList) ;
    }

    private List<SwitchButton> getAllButtonOfRoom(Long roomId)
    {

        //  order by id
//        return new Select().from( SwitchBoard.class ).orderBy("id ASC").execute() ;

        // access by id
//        return new Select().from(SwitchBoard.class).where("id = ?", roomId ).execute();


        // access by RoomId
        return new Select().from( SwitchButton.class ).where("RoomId = ?", roomId ).orderBy("id ASC").execute() ;

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();
    }

    // Read single SwitchBoard
    private SwitchBoard getSingleSwitchBoardFromRoomId(String roomId)
    {
        Log.e("inside","getSingleSwitchBoardFromRoomId");

        //  order by id
        return new Select().from(SwitchBoard.class).where("RoomId = ?", roomId ).executeSingle();
    }




    @Override
    public int getItemCount()
    {
        return roomList.size();
    }

    public void addItem(ArrayList<RoomDbModel> list)
    {
        roomList.clear();
        roomList.addAll(list);
//        Log.e("roomList size in","addItem "+roomList.size() ) ;
        notifyItemRangeChanged( 0, roomList.size() );
    }

    public void testMethod()
    {
        Log.e(TAG, "testMethod: " );
    }


    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name ;
        LinearLayout ll_main,   ll_edit_delete_btn ;
        public ImageView iv_edit , iv_delete , iv_room ;
        FlexboxLayout single_grid_item ;
        public MyViewHolder( View view )
        {
            super( view ) ;
            name = ( TextView ) view.findViewById( R.id.name ) ;
            iv_room =(ImageView )view.findViewById( R.id.iv_room ) ;
            ll_main = ( LinearLayout)view.findViewById( R.id.ll_main );
            single_grid_item = ( FlexboxLayout )view.findViewById( R.id.single_grid_item ) ;
        }
    }

    public void setListNull()
    {
        roomList.clear();
    }

    public boolean checkString( String str )
    {
        return str != null && (  str.equals("null") ||  str.equals("NaN") || str.equals("undefined") || str.equals("") || str.isEmpty() );
    }
}
