package com.example.lenovo.activeandroid3.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.adapter.SwitchBoardActivityAdapter;
import com.example.lenovo.activeandroid3.dbModel.SwitchBoardDbModel;
import com.example.lenovo.activeandroid3.model.SwitchBoard;
import com.example.lenovo.activeandroid3.model.SwitchButton;
import com.example.lenovo.activeandroid3.util.Conversions;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SwitchBoardActivity extends AppCompatActivity {

    EditText et_room_name ;
//    SwitchBoard switchBoard ;

    SwitchBoardActivityAdapter adapter ;
    ArrayList<SwitchBoardDbModel> list;
    public List<SwitchBoard> switchList = new ArrayList<>() ;
    RecyclerView recyclerView ;

    int images[] = {R.drawable.nature1, R.drawable.nature2, R.drawable.nature3, R.drawable.nature4 };
    RelativeLayout relativeLayout ;

    Long id ;
    Button btn_get , btnAddBoard ;
    Context context ;
    String TAG = "SwitchBoardActivity" ;
    String roomId = "" ,delete_edit_btn_flag  ;

    AlertDialog  alertDialog;
    SharedPreferences sharedPref ;
    Uri filePath ;
    FloatingActionButton fab ;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState ) ;
        setContentView( R.layout.activity_switch_board ) ;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Switch Boards");

        ActiveAndroid.initialize( this ) ;
        context = this ;
        sharedPref = PreferenceManager.getDefaultSharedPreferences( context ) ;

        getIntentData( ) ;
        initialization( ) ;




        switchList = getAll() ;
        setToDbModel( switchList ) ;


        if( sharedPref.contains( "image_Path" ) )
        {
            // get path from sharedPreferrence and set it to the  layout
            getPathFromSharedPreference();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if(delete_edit_btn_flag.equals("1") )
        {
            fab.setVisibility( View.GONE ) ;
        }else {
            fab.setVisibility( View.VISIBLE ) ;
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addSwitchBoard() ;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if( sharedPref.contains( "image_Path" ) )
        {
            // get path from sharedPreferrence and set it to the  layout
            getPathFromSharedPreference();
        }

        switchList = getAll() ;
        setToDbModel( switchList ) ;

    }

    private void getPathFromSharedPreference()
    {
        filePath  =  Uri.parse( sharedPref.getString("image_Path", "" ) ) ;
        File f = new File(getRealPathFromURI( filePath ) ) ;
        Drawable d = Drawable.createFromPath( f.getAbsolutePath() ) ;
        relativeLayout.setBackground(d);
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2 , menu);

        MenuItem ii = menu.getItem( 0 );
        ii.setTitle("Home") ;
        return true;
    }


//    /*Back Button*/
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
////        Log.e("inisde","onOptionsItemSelected"+TAG );
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void addSwitchBoard()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme);
        LayoutInflater inflater;
        inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.add_room, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.show();


        ImageView in_back_arrow = (ImageView) dialogView.findViewById(R.id.in_back_arrow);
        in_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        Glide.with(context).load(R.drawable.back_arrow)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(in_back_arrow);
        TextView add_flat_staff_title = (TextView) dialogView.findViewById(R.id.add_flat_staff_title);
        add_flat_staff_title.setText("Add Switch Board");

        final EditText tv_service_name = (EditText) dialogView.findViewById(R.id.tv_service_name);

        Button cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button submit = (Button) dialogView.findViewById(R.id.submit);
        submit.setText("Add");

        final TextInputLayout textLayoutinput_service_name = (TextInputLayout) dialogView.findViewById(R.id.textLayoutinput_service_name);
        textLayoutinput_service_name.setHint("Enter Board Name ");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textLayoutinput_service_name.setError(null);

                boolean cancel = false;
                View focusView = null;

                String tv_service_name_text =tv_service_name.getText().toString().trim()  ;

                if (TextUtils.isEmpty(tv_service_name_text)) {
                    tv_service_name.setError("Enter Board Name");
                    focusView = tv_service_name;
                    cancel = true;
                }

                if ( cancel ) {
                    focusView.requestFocus() ;
                } else
                {

                    addSwitchBoardDynamic(  Conversions.makeFirstLeterCap( tv_service_name_text ) );

//                    SwitchBoard r = new SwitchBoard() ;
//                    r.BoardName = tv_service_name_text ;
//                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//                    r.BoardUpdatedat = timestamp;
//                    r.BoardCreatedAt = timestamp;
//                    Long id = r.save();
//                    alertDialog.cancel();
//                    Rooms dd =  readSingle( id )  ;
//                    customAdapter.addOneMoreItem( dd );
                }

            }
        });

    }

    private void setToDbModel(List<SwitchBoard> switchList )
    {
//        Log.e("ssize size setDbModel",switchList.size()+"" ) ;
        list = new ArrayList<>() ;
        for (int i = 0; i < switchList.size(); i++)
        {

//            Log.e("BoardName and id",switchList.get( i ).BoardName +" "+switchList.get(i).getId()+""  );
            list.add(new SwitchBoardDbModel(
                    switchList.get(i).getId()  ,
                    switchList.get(i).BoardName,
                    switchList.get( i).RoomId ,
                    switchList.get(i).BoardCreatedAt,
                    switchList.get(i).BoardUpdatedat
            ));
        }

        adapter.addItemMore( list ) ;
    }


    private void showList( List<SwitchBoard> switchList )
    {
        //Log.e("inside","showList") ;
//        Log.e("roomList size",switchList.size()+"" );
//        ArrayList<String> al = new ArrayList<>() ;
        for ( int i = 0 ; i < switchList.size() ; i++ )
        {
//            Log.e("id is "+ switchList.get( i ).getId() ,"Name is "+switchList.get( i ).BoardName+"room id"+switchList.get(i).RoomId ) ;

        }
    }

    // Read all Data
    private List<SwitchBoard> getAll()
    {
//        Log.e("inside","getAll roomid is"+roomId ) ;


        //  order by id
//        return new Select().from( SwitchBoard.class ).orderBy("id ASC").execute() ;

        // access by id
//        return new Select().from(SwitchBoard.class).where("id = ?", roomId ).execute();


        // access bu RoomId
        return new Select().from(SwitchBoard.class).where("RoomId = ?", roomId ).execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();

    }


    private void initialization()
    {

//        et_room_name = ( EditText )findViewById( R.id.etGetRoom );
//        btn_get = ( Button)findViewById( R.id.btn_get );
//        btnAddBoard =  ( Button )findViewById( R.id.btnAddRoom );

        recyclerView = (RecyclerView) findViewById( R.id.recyclerView ) ;
        fab = (FloatingActionButton) findViewById(R.id.fab);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        LinearLayoutManager layout_manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout_manager);
        adapter = new SwitchBoardActivityAdapter( context  , SwitchBoardActivity.this , delete_edit_btn_flag  );
        recyclerView.setLayoutManager(layout_manager);
        adapter.setRecyclerView( recyclerView , fab ) ;
        recyclerView.setAdapter(adapter ) ;


    }

    private void getIntentData()
    {
        Intent intent = getIntent() ;
        intent.getStringExtra("roomId");
        roomId = intent.getStringExtra("roomId") ;
        delete_edit_btn_flag = intent.getStringExtra("delete_edit_btn_flag") ;

//        Log.e("room id","in getIntentData"+roomId ) ;
    }

    private void addSwitchBoardDynamic( String name_text )
    {
        alertDialog.cancel() ;
        Date currentTime = Calendar.getInstance().getTime() ;
        SwitchBoard sb = new SwitchBoard() ;
        sb.BoardName =  name_text ;
        sb.BoardCreatedAt = currentTime.getTime() ;
        sb.BoardUpdatedat = currentTime.getTime() ;
        sb.RoomId = roomId ;
//        insertData( switchBoard) ;
        id = sb.save() ;
        SwitchBoard dd =  readSingle( id )  ;

        addDefaultButtons( dd.getId() ) ;
        adapter.addOneMoreItem( dd ) ;


    }

    private void addDefaultButtons(Long boardId )
    {
        ActiveAndroid.beginTransaction() ;
        try
        {
            for (int i = 1; i < 9 ; i++ )
            {

                String buttonName = "Button"+i ;
                Date timestamp = Calendar.getInstance().getTime();
                SwitchButton switchButton = new SwitchButton();
                switchButton.SwitchButtonName = buttonName ;
                switchButton.SwitchButtonCreatedAt = timestamp.getTime() ;
                switchButton.SwitchButtonUpdatedat = timestamp.getTime() ;
                switchButton.SwitchBoardId = boardId;
                switchButton.is_on = false ;
                switchButton.save() ;
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    private SwitchBoard readSingle( Long id )
    {
        return new Select().from(SwitchBoard.class).where("id = ?", id).executeSingle();
    }

}