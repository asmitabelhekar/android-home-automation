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
import com.example.lenovo.activeandroid3.adapter.SwitchButtonActivityAdapter;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.model.SwitchButton;
import com.example.lenovo.activeandroid3.util.Conversions;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SwitchButtonActivity extends AppCompatActivity
{
    Context context ;
    String TAG = "SwitchButtonActivity" ;
    String  delete_edit_btn_flag  ;
    Long switchBoardId ;
    SwitchButtonActivityAdapter adapter ;
    RecyclerView recyclerView ;
    FloatingActionButton fab ;
    ArrayList<SwitchButtonDbModel> list;
    public List<SwitchButton> switchButtonList = new ArrayList<>() ;
    AlertDialog alertDialog ;
    //    int position ;
//    int images[] = { R.drawable.nature1 , R.drawable.nature2 , R.drawable.nature3 , R.drawable.nature4 } ;
    RelativeLayout relativeLayout ;
    SharedPreferences sharedPref ;

    private Uri filePath ;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState ) ;
        setContentView( R.layout.activity_switch_button ) ;
        Toolbar toolbar = ( Toolbar ) findViewById( R.id.toolbar ) ;
        setSupportActionBar( toolbar );
        toolbar.setTitle("Switch Button");

        ActiveAndroid.initialize( this ) ;
        context = this ;
        sharedPref = PreferenceManager.getDefaultSharedPreferences( context ) ;

        getIntentData( ) ;
        initialization( ) ;



        switchButtonList = getAll() ;
        setToDbModel( switchButtonList ) ;


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

                addSwitchButton() ;
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

    }

    private void getPathFromSharedPreference()
    {
//        Log.e("inside","getPathFromSharedPreference") ;
        filePath  =  Uri.parse( sharedPref.getString("image_Path", "" ) ) ;
        File f = new File(getRealPathFromURI( filePath ) ) ;
        Drawable d = Drawable.createFromPath( f.getAbsolutePath() ) ;
        relativeLayout.setBackground( d ) ;
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//
////        Log.e("inside","onResume") ;
//        switchButtonList = getAll() ;
//        setToDbModel( switchButtonList ) ;
//
//    }

    /*Back Button*/
    public boolean onOptionsItemSelected( MenuItem item )
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


        if ( id == android.R.id.home )
        {
            finish();
            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave ) ;

            return true ;
        }
        return super.onOptionsItemSelected( item ) ;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_enter , R.anim.animation_leave ) ;
    }



    private void addSwitchButton()
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
        add_flat_staff_title.setText("Add Switch Button");

        final EditText tv_service_name = (EditText) dialogView.findViewById(R.id.tv_service_name);

        Button cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button submit = (Button) dialogView.findViewById(R.id.submit);
        submit.setText("Add");

        final TextInputLayout textLayoutinput_service_name = (TextInputLayout) dialogView.findViewById(R.id.textLayoutinput_service_name);
        textLayoutinput_service_name.setHint("Enter Button Name ");

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

                String tv_service_name_text = tv_service_name.getText().toString().trim();

                if (TextUtils.isEmpty(tv_service_name_text)) {
                    tv_service_name.setError("Enter Button Name");
                    focusView = tv_service_name;
                    cancel = true;
                }

                if ( cancel ) {
                    focusView.requestFocus() ;
                } else
                {

                    addSwitchBoardDynamic( Conversions.makeFirstLeterCap( tv_service_name_text ) ) ;

                }
            }
        });
    }

    private void addSwitchBoardDynamic(String tv_service_name_text)
    {
        alertDialog.cancel();
        Date timestamp = Calendar.getInstance().getTime() ;
        SwitchButton switchButton  = new SwitchButton() ;
        switchButton.SwitchButtonName=  tv_service_name_text ;
        switchButton.SwitchButtonCreatedAt = timestamp.getTime() ;
        switchButton.SwitchButtonUpdatedat = timestamp.getTime() ;
        switchButton.SwitchBoardId = switchBoardId ;
        switchButton.is_on = false ;
        insertData( switchButton) ;
    }

    private void insertData(SwitchButton switchButton)
    {
        Long  id = switchButton.save() ;

        SwitchButton dd =  readSingle( id )  ;
        adapter.addOneMoreItem( dd );

    }

    private SwitchButton readSingle( Long id )
    {
        return new Select()
                .from(SwitchButton.class )
                .where("id = ?", id )
                .executeSingle() ;

    }

    private void setToDbModel( List<SwitchButton> switchButtonList )
    {
//        Log.e("switchButtonsize in set",switchButtonList.size()+"" ) ;
        list = new ArrayList<>() ;
        for ( int i = 0; i < switchButtonList.size() ; i++ )
        {

//            Log.e("BoardName ,id, boardId",switchButtonList.get( i ).SwitchButtonName +" "+switchButtonList.get(i).getId()+""+switchButtonList.get(i).SwitchBoardId  );
            list.add(new SwitchButtonDbModel(
                    switchButtonList.get(i).getId()  ,
                    switchButtonList.get(i).SwitchButtonName ,
                    switchButtonList.get( i).SwitchBoardId ,
                    switchButtonList.get(i).SwitchButtonCreatedAt ,
                    switchButtonList.get(i).SwitchButtonUpdatedat ,
                    switchButtonList.get(i).is_on,
                    switchButtonList.get(i).RoomId,
                    switchButtonList.get(i).OnImage,
                    switchButtonList.get(i).OffImage,
                    1 , // this flag is used only for editmode dialog
                    1 // card vr room madhe button kiti position la aahe he samjav mhanun ha flag set kela aahe.pn ethe ha fakt dummy flag aahe.ethe yach use nahi.
                        // Actucally ha flag fakt ModeFragmentAdapter madhech use kela aahe.


            ));
        }

        adapter.addItemMore( list ) ;
    }

    private void getIntentData()
    {
        Intent intent = getIntent() ;
//        switchBoardId = intent.getStringExtra("switchBoardId") ;
        delete_edit_btn_flag = intent.getStringExtra("delete_edit_btn_flag") ;

//        Log.e("switchBoardId ","in getIntentData"+switchBoardId ) ;
    }


//    private void setToDbModel(List<SwitchButton> switchButtonList)
//    {
//
//        Log.e("ssize size setDbModel",switchButtonList.size()+"" ) ;
////        RoomDbModel db = new RoomDbModel() ;
//        list = new ArrayList<>();
//        for (int i = 0; i < switchButtonList.size(); i++)
//        {
////            db.setrName( roomList.get( i).Name ) ;
//            Log.e("BoardName in set",switchButtonList.get( i ).BoardName );
////            db.setId( String.valueOf( roomList.get(i).getId() ) ) ;
//            Log.e("id in set",switchButtonList.get(i).getId()+"" );
////            db.setrCreatedAt( roomList.get( i ).CreatedAt ) ;
////            db.setrUpdatedAt( roomList.get( i ).Updatedat ) ;
////            list.add( db );
////            Log.e("id is "+roomList.get( i ).getId(),"Name is "+roomList.get( i ).Name  );
//
//            list.add(new SwitchBoardDbModel(
//                    String.valueOf( switchButtonList.get(i).getId() ) ,
//                    switchButtonList.get(i).BoardName,
//                    switchButtonList.get( i).RoomId ,
//                    switchButtonList.get(i).BoardCreatedAt,
//                    switchButtonList.get(i).BoardUpdatedat
//            ));
//        }
//
//        adapter.addItemMore( list ) ;
//
//    }

    private List<SwitchButton> getAll()
    {
        //  order by id
//        return new Select().from( SwitchBoard.class ).orderBy("id ASC").execute() ;

        // access by id
//        return new Select().from(SwitchBoard.class).where("id = ?", roomId ).execute();


        // access by RoomId
        return new Select().from( SwitchButton.class ).where("SwitchBoardId = ?", switchBoardId ).execute() ;

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();

    }

    private void initialization()
    {
        //        et_room_name = ( EditText )findViewById( R.id.etGetRoom );
//        btn_get = ( Button)findViewById( R.id.btn_get );
//        btnAddBoard =  ( Button )findViewById( R.id.btnAddRoom );

        recyclerView = (RecyclerView) findViewById( R.id.recyclerView ) ;
         fab = ( FloatingActionButton ) findViewById( R.id.fab ) ;
        relativeLayout = ( RelativeLayout ) findViewById( R.id.relative_layout ) ;
        LinearLayoutManager layout_manager = new LinearLayoutManager( this ) ;
        recyclerView.setLayoutManager(layout_manager);
        adapter = new SwitchButtonActivityAdapter( this  , SwitchButtonActivity.this , delete_edit_btn_flag );
        adapter.setRecyclerView( recyclerView , fab ) ;
        recyclerView.setLayoutManager(layout_manager);
        recyclerView.setAdapter( adapter ) ;

    }

}