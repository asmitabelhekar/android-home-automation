package com.example.lenovo.activeandroid3.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.adapter.ModesDetailActivityAdapter;
import com.example.lenovo.activeandroid3.dbModel.ModesDetailActivityDbModel;
import com.example.lenovo.activeandroid3.model.ModeOnOf;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.model.SwitchBoard;
import com.example.lenovo.activeandroid3.model.SwitchButton;

import java.util.ArrayList;
import java.util.List;

public class ModesDetailActivity extends AppCompatActivity
{

    String TAG = "ModesDetailActivity" ;
    List<Rooms> roomList = new ArrayList<>() ;
    List<SwitchBoard> boardList = new ArrayList<>() ;
    List<SwitchButton> buttonList = new ArrayList<>() ;
    List<ModeOnOf> listModeOnOf = new ArrayList<>() ;

    ArrayList<ModesDetailActivityDbModel> list ;
    RecyclerView  recyclerView ;
    ModesDetailActivityAdapter adapter ;
    Context context ;
    SharedPreferences sharedPref ;
    String modeId ;
    ArrayList<String> arrayButtonId ;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState ) ;
        setContentView( R.layout.activity_modes_detail )  ;
        Toolbar toolbar = ( Toolbar ) findViewById( R.id.toolbar ) ;
        setSupportActionBar( toolbar );
        toolbar.setTitle("Modes Detail") ;

        ActiveAndroid.initialize( this ) ;

        context = this ;
        getDataFromIntent() ;
        listModeOnOf  = getDataFromModeOnOfTable( );
        Log.e("listModeOnOf size ",listModeOnOf.size()+"" );
        createArrayOfButtonId( listModeOnOf );
        initialization( ) ;

        sharedPref = PreferenceManager.getDefaultSharedPreferences( context ) ;
        getData() ;
    }
    private void initialization()
    {
        //        et_room_name = ( EditText )findViewById( R.id.etGetRoom );
//        btn_get = ( Button)findViewById( R.id.btn_get );
//        btnAddBoard =  ( Button )findViewById( R.id.btnAddRoom );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab.setVisibility( View.GONE ) ;

        recyclerView = (RecyclerView) findViewById( R.id.recyclerView ) ;
        LinearLayoutManager layout_manager = new LinearLayoutManager( this ) ;
        recyclerView.setLayoutManager(layout_manager);

        adapter = new ModesDetailActivityAdapter( this  , ModesDetailActivity.this , modeId  , arrayButtonId  );
//        adapter.setRecyclerView( recycler_view12 ) ;
        recyclerView.setLayoutManager(layout_manager);
//        adapter.setMoreLoading(false) ;
//        adapter.setLinearLayoutManager(layout_manager);

        recyclerView.setAdapter( adapter ) ;
    }

    private void getData()
    {

//        Log.e("inside" ,"getData") ;
        list = new ArrayList<>();

        // fetch Room Data
        roomList = fetchRoomData() ;
//        Log.e("roomList size",roomList.size()+"" ) ;
        for( int i = 0 ; i < roomList.size() ; i++ )
        {
            Rooms room =  roomList.get( i ) ;

            boardList =  fetchBoardData(room.getId() ) ;

            for( int j = 0 ; j < boardList.size() ; j++ )
            {
                SwitchBoard switchBoard =   boardList.get( j ) ;

                buttonList  =  fetchButtonData( switchBoard.getId() ) ;
                for( int k = 0 ; k < buttonList.size() ; k++ )
                {
                    SwitchButton buttonObject = buttonList.get( k ) ;
//                    Log.e("rName  BName BId ButtN",rName+"  "+boardName+"  "+sbId+"  "+switchButtonName+"  "+switchButtonId ) ;

                    list.add(new ModesDetailActivityDbModel( room.Name , room.getId() , switchBoard.BoardName , switchBoard.getId() , buttonObject.SwitchButtonName , buttonObject.getId() ) ) ;
                }
            }
        }
        adapter.addItemMore( list ) ;
    }


    private void createArrayOfButtonId(List<ModeOnOf> listModeOnOf)
    {
        arrayButtonId = new ArrayList<>();
//        Log.e("inside","createArrayOfButtonId") ;
        for( int i = 0 ; i< listModeOnOf.size() ; i++ )
        {
            ModeOnOf temp  = listModeOnOf.get( i ) ;
//            Log.e("buttonId", temp.ButtonId ) ;
            arrayButtonId.add( temp.ButtonId ) ;
        }

    }

    private List<ModeOnOf> getDataFromModeOnOfTable()
    {
        //  order by id
        return new Select().from( ModeOnOf.class).where("ModeId = ?", modeId ).execute() ;
    }

    private void getDataFromIntent()
    {
        Intent intent = getIntent() ;
        modeId = intent.getStringExtra("modeId") ;
        Log.e("mode id is", modeId ) ;
//        delete_edit_btn_flag = intent.getStringExtra("delete_edit_btn_flag") ;

    }


//    /*Back Button*/
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
////        Log.e("inisde","onOptionsItemSelected"+TAG );
//        int id = item.getItemId() ;
//        if ( id == android.R.id.home ) {
//            finish() ;
//            return true ;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /*Back Button*/
    public boolean onOptionsItemSelected(MenuItem item)
    {
//        Log.e("inisde","onOptionsItemSelected"+TAG );
        int id = item.getItemId() ;
        if ( id == android.R.id.home ) {
            finish() ;
            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave ) ;
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave ) ;
    }

    private List<SwitchButton> fetchButtonData(Long sbId)
    {
        //  order by id
//        return new Select().from( SwitchBoard.class ).orderBy("id ASC").execute() ;

        // access by id
//        return new Select().from(SwitchBoard.class).where("id = ?", roomId ).execute();


        // access by boardId
        return new Select().from( SwitchButton.class).where("SwitchBoardId = ?", sbId ).execute() ;

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();
    }

    private List<SwitchBoard> fetchBoardData(Long rId)
    {
//        Log.e("inside","fetchBoardData rId is"+rId ) ;

        //  order by id
//        return new Select().from( SwitchBoard.class ).orderBy("id ASC").execute() ;

        // access by id
//        return new Select().from(SwitchBoard.class).where("id = ?", roomId ).execute();


        // access bu RoomId
        return new Select().from(SwitchBoard.class).where("RoomId = ?", rId ).execute() ;

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();
    }

    private List<Rooms> fetchRoomData()
    {
        //  order by id
        return new Select().from(Rooms.class).orderBy("id ASC").execute() ;

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();
    }



}
