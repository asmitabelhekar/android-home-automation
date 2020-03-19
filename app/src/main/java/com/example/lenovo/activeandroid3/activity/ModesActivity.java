package com.example.lenovo.activeandroid3.activity ;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.adapter.ModesActivityAdapter;
import com.example.lenovo.activeandroid3.dbModel.ModesActivityDbModel;
import com.example.lenovo.activeandroid3.dbModel.ModesDetailActivityDbModel;
import com.example.lenovo.activeandroid3.model.Mode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ModesActivity extends AppCompatActivity {

    String[] modeNamesArray = {"Day", "Evening" ,"Night"} ;
    public List<Mode> modeList = new ArrayList<>() ;
    Context context ;
    SharedPreferences sharedPref ;
    RecyclerView  recyclerView ;
    ModesActivityAdapter adapter ;
    ArrayList<ModesActivityDbModel> list ;
    //    DemoView demo ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modes_detail ) ;
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar ) ;
        setSupportActionBar(toolbar) ;
        toolbar.setTitle("Modes") ;

        context = this ;
        getDataFromIntent() ;
        initialization( ) ;

        getSharedPreferencesVariable() ;

        setDataToAdapter();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab.setVisibility(View.GONE );
    }



    private void setDataToAdapter()
    {
        list = new ArrayList<>();

        for( int i = 0 ; i< modeList.size() ; i++ )
        {
//            Log.e("Modename is",modeList.get( i ).ModeName );
            boolean b = modeList.get( i).isOn ;

            list.add(new ModesActivityDbModel(modeList.get(i).getId() , modeList.get( i).ModeName , modeList.get(i).CreatedAt , modeList.get(i).Updatedat  , modeList.get(i).isOn , 2130837632 ) ) ;
        }

        adapter.addItemMore( list ) ;
    }

    //    // Read all Data
    private List<Mode> getAll()
    {
//        Log.e("inside","getAll");

        //  order by id
        return new Select().from(Mode.class).orderBy("id ASC").execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();

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

    private void getSharedPreferencesVariable()
    {
        sharedPref = PreferenceManager.getDefaultSharedPreferences( context ) ;


        if ( sharedPref.contains("isModesCreated") )
        {
            Log.e("modes created","already") ;
            modeList = getAll() ;


        }else {
            Log.e("modes Not created","already") ;
            createModes();

            modeList = getAll() ;


        }

        Log.e("modeList size", modeList.size()+"") ;
    }

    private void getDataFromIntent()
    {
        Intent intent = getIntent() ;
//        switchBoardId = intent.getStringExtra("switchBoardId") ;
//        delete_edit_btn_flag = intent.getStringExtra("delete_edit_btn_flag") ;


    }

    private void initialization()
    {
        //        et_room_name = ( EditText )findViewById( R.id.etGetRoom );
//        btn_get = ( Button)findViewById( R.id.btn_get );
//        btnAddBoard =  ( Button )findViewById( R.id.btnAddRoom );

        recyclerView = (RecyclerView) findViewById( R.id.recyclerView ) ;
        LinearLayoutManager layout_manager = new LinearLayoutManager( this ) ;
        recyclerView.setLayoutManager( layout_manager ) ;
        adapter = new ModesActivityAdapter( this  , ModesActivity.this  );

        recyclerView.setAdapter( adapter ) ;

    }


    /*Back Button*/
    public boolean onOptionsItemSelected(MenuItem item)
    {
//        Log.e("inisde","onOptionsItemSelected"+TAG );

        int id = item.getItemId() ;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings )
        {
//            return true;
            Log.e("click on","item") ;
            Intent i = new Intent( context , MainActivity.class );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity( i ) ;
//            finish();
            overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_right ) ;
        }

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave ) ;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave ) ;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("inside","OnResume") ;

        modeList = getAll() ;
        setDataToAdapter();

    }








}
