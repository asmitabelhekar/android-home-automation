package com.example.lenovo.activeandroid3.activity.v1.activity;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.v1.adapter.DemoModeAdapter;
import com.example.lenovo.activeandroid3.dbModel.ModelNewDbModel;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.model.Mode;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.model.SwitchButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;


public class DemoModeActivity extends AppCompatActivity {

    Context context ;
    List<Mode> modeList ;
    List<Rooms> roomList ;
    ArrayList<ModelNewDbModel> roomModeList ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState ) ;
        setContentView( R.layout.activity_mode_demo ) ;
         modeList = getAllMode() ;
         roomList = getAllRoom() ;
        Log.e("roomList : ",roomList.size()+"") ;
        Log.e("modeList : ",modeList.size()+"") ;

        context = this ;
        FloatingActionButton fab = ( FloatingActionButton)findViewById( R.id.fab );
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();
            }
        });
    }

    private void openDialog()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme);
        LayoutInflater inflater;
        inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.demo_mode_dialog, null);
        dialogBuilder.setView(dialogView);

        AlertDialog  alertDialog_edit_mode = dialogBuilder.create();
        alertDialog_edit_mode.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog_edit_mode.show();

//        inflateDialogToolbarForAddLight(dialogView ) ;



        ListView listview_demo_mode = (ListView) dialogView.findViewById(R.id.listview_demo_mode );

        createData();


        DemoModeAdapter demoModeAdapter = new DemoModeAdapter( context , roomModeList , DemoModeActivity.this   );
        listview_demo_mode.setAdapter( demoModeAdapter );



//        editModeDialogAdapter.addItem( roomModeList) ;

    }

    private void createData()
    {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        roomModeList = new ArrayList<>();

        for( int i= 0 ;i < roomList.size(); i++ )
        {
            Rooms room = roomList.get( i ) ;

            ArrayList<SwitchButtonDbModel>  switchButtonDbModelList= new ArrayList<>();
            List<SwitchButton> buttonList  =   getAllButtonOfRoom( room.getId() ) ;

            for( int j=0 ; j < buttonList.size() ; j++ )
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
                        1 , // this flag is used only for editmode dialog
                        1 // card vr room madhe button kiti position la aahe he samjav mhanun ha flag set kela aahe.pn ethe ha fakt dummy flag aahe.ethe yach use nahi.
                        // Actucally ha flag fakt ModeFragmentAdapter madhech use kela aahe.
                );
                switchButtonDbModelList.add( switchButtonDbModel );
            }

            String switchButtonDbModelListString =  gson.toJson( switchButtonDbModelList , ArrayList.class) ;
            ModelNewDbModel modelNewDbModel =  new ModelNewDbModel( room.getId() ,room.Name ,room.CreatedAt,room.Updatedat,room.RoomHomePageImage ,room.AddRoomImage,room.RoomOnOff , switchButtonDbModelListString) ;
            roomModeList.add( modelNewDbModel );
        }
        for( int i= 0 ; i< roomModeList.size() ;i++ )
        {
            ModelNewDbModel modelNewDbModel =  roomModeList.get(i) ;
            Log.e( "btnString" ,modelNewDbModel.getRoomId()+"  "+modelNewDbModel.getRoomName() +"  "+modelNewDbModel.getSwitchButtonArray() );

        }
    }

    //    // Read all Data
    private List<Mode> getAllMode()
    {
        return new Select().from(Mode.class).orderBy("id ASC").execute();
    }

    // Read all Data
    private List<Rooms> getAllRoom()
    {
        return new Select().from(Rooms.class).orderBy("id ASC").execute();
    }

    private List<SwitchButton> getAllButtonOfRoom(Long roomId)
    {
        // access by RoomId
        return new Select().from( SwitchButton.class ).where("RoomId = ?", roomId ).execute() ;
    }


}
