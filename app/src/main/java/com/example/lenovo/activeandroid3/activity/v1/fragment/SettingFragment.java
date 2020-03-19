package com.example.lenovo.activeandroid3.activity.v1.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.ResponseInterfaceNew;
import com.example.lenovo.activeandroid3.activity.v1.adapter.AddLightDialogAdapter;
import com.example.lenovo.activeandroid3.activity.v1.adapter.AddRoomDialogAdapter;
import com.example.lenovo.activeandroid3.activity.v1.adapter.RoomFragmentAdapter;
import com.example.lenovo.activeandroid3.activity.v1.adapter.ShowWifiDeviceListAdpter;
import com.example.lenovo.activeandroid3.activity.v1.asyntask.TestDeviceAsynTask;
import com.example.lenovo.activeandroid3.activity.v1.interfaces.TestDeviceInterface;
import com.example.lenovo.activeandroid3.activity.v1.utils.CommonAsynTask;
import com.example.lenovo.activeandroid3.activity.v1.utils.ConfigureAsynTask;
import com.example.lenovo.activeandroid3.activity.v1.utils.MethodSelection;
import com.example.lenovo.activeandroid3.activity.v1.utils.Response;
import com.example.lenovo.activeandroid3.activity.v1.utils.ResponseInterface;
import com.example.lenovo.activeandroid3.asyntask.CommonAsynTaskNew;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.util.Constants;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by akshay on 6/7/18.
 */

public class SettingFragment extends Fragment  implements  ResponseInterfaceNew
{

    Context context;

    String TAG = " SettingFragment " ;

    // setting dialog items
    TextView tv_add_device , tv_synchronize , tv_notification, tv_add_edit_light_point, tv_add_room, tv_edit_profile , tv_change_password;
    LinearLayout ll_add_device , ll_synchronize , ll_notification, ll_add_edit_light_point, ll_add_room , ll_edit_profile, ll_change_password ;
    EditText et_password;
    TextView tv_router_name;
    Button btn_test, btn_save;

    ArrayList<RoomDbModel> roomDbModelList = new ArrayList<RoomDbModel>();
    ArrayList<String> roomNames = new ArrayList<String>();

    ArrayList<String> listSSID = new ArrayList<>();
    AlertDialog alertDialog_wifi_list = null;
    AlertDialog alertDialog = null ;
    String selectedDeviceName = null;
    StringBuilder responseString = new StringBuilder();

    SettingFragment my_object = this;
    // if test command return *OK# then only this flag is true and show screen otherwise show only toast message.
    boolean testResponseStatus = false ;

    LinearLayout ll_main_dialog ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View rootview = inflater.inflate(R.layout.fragment_setting, container, false);
        try {
            inflateView(rootview);
            ll_add_device.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    addDeviceDialog() ;
                }
            });

            ll_add_room.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    addRoomDialog() ;
                }
            });

            ll_add_edit_light_point.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    addDeviceAndEditButton();
                }
            });
        } catch (Exception e) {
            Log.e("Achiv frag", e.getMessage());
        }
        return rootview;
    }

    private void addDeviceAndEditButton()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme ) ;
        LayoutInflater inflater;
        inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.v1_add_device_and_edit_button_from_setting, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog  alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.show();

        ImageView iv_back = ( ImageView)dialogView.findViewById(R.id.iv_back );
        TextView tv_title = ( TextView)dialogView.findViewById(R.id.tv_title );
        TextView tv_anyText = (TextView)dialogView.findViewById(R.id.tv_anyText );
        tv_anyText.setVisibility(View.GONE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

//        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//
//                // if button is deleted then only call this method from here otherwise no need to call
////                if( isButtonAddedOrDeletedFlag )
////                    getRoomDataAndSetToRoomInsideDialogAdapter( roomId ) ;
//            }
//        });

//        Button btn_add = ( Button )dialogView.findViewById( R.id.btn_add );
//        btn_add.setVisibility( View.GONE);

//        EditText  et_add_light = ( EditText )dialogView.findViewById(R.id.et_add_light ) ;

//        inflateDialogToolbarForAddLight(dialogView  , roomId) ;



//        RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.recycler_view_add_light_dialog );
//        LinearLayoutManager  layout_manager = new LinearLayoutManager(context);
//        recyclerView.setLayoutManager(layout_manager);
//        AddLightDialogAdapter addLightDialogAdapter = new AddLightDialogAdapter( RoomFragmentAdapter.this ,context  , alertDialog_add_light );
//        recyclerView.setAdapter( addLightDialogAdapter );
//
//        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext()  ,
//                DividerItemDecoration.VERTICAL);
//        Drawable horizontalDivider = ContextCompat.getDrawable(context , R.drawable.horizontal_divider);
//        horizontalDecoration.setDrawable(horizontalDivider);
//        recyclerView.addItemDecoration(horizontalDecoration);
//
//        addLightDialogAdapter.addItem(  switchButtonList ) ;
    }

    private void addRoomDialog()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme);
        LayoutInflater inflater;
        inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.v1_add_room, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.show();

        RecyclerView recycler_view_add_room_dialog ;
        LinearLayoutManager layout_manager_add_room_dialog ;

        recycler_view_add_room_dialog = (RecyclerView) dialogView.findViewById(R.id.recycler_view_add_room_dialog );
        LinearLayout ll_tv_save = (LinearLayout)dialogView.findViewById(R.id.ll_tv_save );
        ImageView in_back_arrow = (ImageView)dialogView.findViewById( R.id.in_back_arrow );
        in_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.cancel();
            }
        });
        layout_manager_add_room_dialog = new LinearLayoutManager(context);
        recycler_view_add_room_dialog.setLayoutManager(layout_manager_add_room_dialog);
        final AddRoomDialogAdapter   addRoomDialogAdapter = new AddRoomDialogAdapter( this ,context  , alertDialog );
        recycler_view_add_room_dialog.setAdapter( addRoomDialogAdapter );

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recycler_view_add_room_dialog.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recycler_view_add_room_dialog.addItemDecoration(horizontalDecoration);

        List<Rooms> allRoom = getAllRoom();
        ArrayList<RoomDbModel> list = new ArrayList<>();

        for ( Rooms room : allRoom )
        {
            list.add(new RoomDbModel
                    (
                            room.getId() ,
                            room.Name,
                            room.CreatedAt,
                            room.Updatedat,
                            room.RoomHomePageImage,
                            room.AddRoomImage,
                            room.RoomOnOff ,
                            true
                    ));
        }

        addRoomDialogAdapter.setDataToAdapter( list ) ;

        ll_tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRoomDialogAdapter.updateRoomDatabase();
            }
        });
    }

    private void addDeviceDialog()
    {
        roomNames.clear();
        roomDbModelList.clear();

        List<Rooms> roomList = getAllONRoom();
        for (Rooms room : roomList) {
            Log.e("roomName roomOnOffFlag", room.Name + "  " + room.RoomOnOff);
            roomNames.add(room.Name);
            roomDbModelList.add(new RoomDbModel
                    (
                            room.getId(),
                            room.Name,
                            room.CreatedAt,
                            room.Updatedat,
                            room.RoomHomePageImage,
                            room.AddRoomImage,
                            room.RoomOnOff,
                            room.AtLeastOneButtonOfRoomIsOn
                    ));
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme);
        LayoutInflater inflater;
        inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.dialog_add_device, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.show();

        ll_main_dialog = (LinearLayout) dialogView.findViewById(R.id.ll_main_dialog);
        et_password = (EditText) alertDialog.findViewById(R.id.et_password);
        btn_test = (Button) alertDialog.findViewById(R.id.btn_test);
        btn_save = (Button) alertDialog.findViewById(R.id.btn_save);
        Button btn_scan_wifi = (Button) dialogView.findViewById(R.id.btn_scan_wifi);
        ImageView iv_back_arrow = (ImageView) dialogView.findViewById(R.id.iv_back_arrow);
        tv_router_name = (TextView) dialogView.findViewById(R.id.tv_router_name);

        final MaterialSpinner spinner_room = (MaterialSpinner) dialogView.findViewById(R.id.spinner_room);
        spinner_room.setHint("Select Room");

//        ll_main_dialog.setVisibility(View.GONE);

        btn_scan_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "scan wifi " );
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                List<ScanResult> scanResults = wm.getScanResults();
                Log.e("scanResult size ", scanResults.size() + "");

                listSSID.clear();
                for (ScanResult scanResult : scanResults) {
                    Log.e("SSID List :", scanResult.SSID);
                    if (scanResult.SSID.equals("")) {
                    } else {
                        listSSID.add(scanResult.SSID);
                    }
                }

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater;
                inflater = LayoutInflater.from(context);
                final View dialogView = inflater.inflate(R.layout.dialog_show_wifi_list, null);
                dialogBuilder.setView(dialogView);
                alertDialog_wifi_list = dialogBuilder.create();
                alertDialog_wifi_list.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                alertDialog_wifi_list.show();

                RecyclerView recycler_view_wifi_list = (RecyclerView) alertDialog_wifi_list.findViewById(R.id.recycler_view_wifi_list);
                LinearLayoutManager layout_manager = new LinearLayoutManager(context);
                recycler_view_wifi_list.setLayoutManager(layout_manager);
                ShowWifiDeviceListAdpter showWifiDeviceListAdpter = new ShowWifiDeviceListAdpter(SettingFragment.this, context, listSSID);
                recycler_view_wifi_list.setAdapter(showWifiDeviceListAdpter);

            }
        });

        iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, R.layout.spinner_row, roomNames);
        Log.e("roomNames contain : ", roomNames.toString());
        spinner_room.setAdapter(spinnerAdapter);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Save", "Device data");
                Toast.makeText(context, "If you entered wrong password then reset device", Toast.LENGTH_SHORT).show();

                validateInputAndSendDataToWifi(spinner_room.getSelectedItem().toString());
            }
        });

        btn_test.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.e("Test", "Device");

                String requestString = "*TST#";

//                CommonAsynTask asynTask = new CommonAsynTask( context,requestString , my_object , MethodSelection.TEST_DEVICE , Constants.IP);
//                if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/)
//                {
//                    asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                } else {
//                    asynTask.execute();
//                }


                CommonAsynTaskNew asynTask = new CommonAsynTaskNew( context,requestString , my_object , MethodSelection.TEST_DEVICE ,"192.168.4.1");
                if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/)
                {
                    asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    asynTask.execute();
                }



            }
        });
    }

    private void validateInputAndSendDataToWifi(String name) {
        boolean cancel = false;
        View focusView = null;

        Long roomId = null;
        String roomName = null;
        for (RoomDbModel roomDbModel : roomDbModelList) {

            if (roomDbModel.getrName().equalsIgnoreCase(name))
            {
                roomName = roomDbModel.getrName();
                roomId = roomDbModel.getId();
            }
        }

        Log.e("selected rName & rId", roomName + "  " + roomId + "");

        String ssid = tv_router_name.getText().toString();
        String password = et_password.getText().toString();

        if (TextUtils.isEmpty(ssid)) {
            tv_router_name.setError("please select wifi device");
            focusView = tv_router_name;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            et_password.setError("Enter Password");
            focusView = et_password ;
            cancel = true ;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            String newSSID = '"' + ssid + '"';
            String newPassword = '"' + password + '"';

            String requestString = "*SID," + roomId + "," + "" + newSSID + "," + newPassword + "#" ;
            String IP = "192.168.4.1" ;

            CommonAsynTaskNew asynTask = new CommonAsynTaskNew( context,requestString , my_object , MethodSelection.CONFIGURE_DEVICE ,IP);
            if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/)
            {
                asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                asynTask.execute();
            }
        }
    }

    private void inflateView(View rootview) {
        tv_add_device = (TextView) rootview.findViewById(R.id.tv_add_device);
        tv_synchronize = (TextView) rootview.findViewById(R.id.tv_synchronize);
        tv_notification = (TextView) rootview.findViewById(R.id.tv_notification);
        tv_add_edit_light_point = (TextView) rootview.findViewById(R.id.tv_add_edit_light_point);
        tv_add_room = (TextView) rootview.findViewById(R.id.tv_add_room);
        tv_edit_profile = (TextView) rootview.findViewById(R.id.tv_edit_profile);
        tv_change_password = (TextView) rootview.findViewById(R.id.tv_change_password);


        ll_add_device = (LinearLayout) rootview.findViewById(R.id.ll_add_device);
        ll_synchronize = (LinearLayout )rootview.findViewById( R.id.ll_synchronize );
        ll_notification = (LinearLayout) rootview.findViewById(R.id.ll_notification);
        ll_add_edit_light_point = (LinearLayout )rootview.findViewById( R.id.ll_add_edit_light_point );
        ll_add_room = (LinearLayout )rootview.findViewById( R.id.ll_add_room );
        ll_edit_profile = (LinearLayout )rootview.findViewById( R.id.ll_edit_profile );
        ll_change_password = (LinearLayout )rootview.findViewById( R.id.ll_change_password );
    }

    //     Read all On room
    private List<Rooms> getAllONRoom() {
//        Log.e("inside","getAllRoom");

        //  order by id
//        return new Select().from(Rooms.class).orderBy("id ASC").execute();

        // access if room is on
        return new Select().from(Rooms.class).where("RoomOnOff = ?", 1).orderBy("id ASC").execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();
    }


    //     Read all Data
    private List<Rooms> getAllRoom() {
//        Log.e("inside","getAllRoom");

        //  order by id
//        return new Select().from(Rooms.class).orderBy("id ASC").execute();

        // access if room is on
        return new Select().from(Rooms.class).orderBy("id ASC").execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();
    }

    public void seletedDeviceName(String selectedDeviceName) {
        this.selectedDeviceName = selectedDeviceName;
        tv_router_name.setText(selectedDeviceName);
        alertDialog_wifi_list.cancel();
    }


    private void configureDeviceResponse(String response) {

        Toast.makeText(context, "Device is Connected to router successfully", Toast.LENGTH_SHORT).show();
        alertDialog.cancel() ;
    }

    private void testDeviceResponse(String response) {

        ll_main_dialog.setVisibility(View.VISIBLE);
        btn_test.setVisibility(View.GONE);

        if( response != null )
        {
            if( response.equals("*OK#")) {
                Toast.makeText(context, "Response is OK", Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(context, "Response is not OK", Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Toast.makeText(context,"Response is null",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void getResponse(String response, MethodSelection interface_method, String IP)
    {
        try {

            Log.e(TAG, "getResponse: "+response );

            switch ( interface_method ) {

                case CONFIGURE_DEVICE:
                    this.configureDeviceResponse(response);
                    break;

                case TEST_DEVICE :
                    this.testDeviceResponse(response);
                    break;
            }
        }catch (Exception e) {

            Log.e( "in getResponse" , e.getMessage() );
        }


    }
}