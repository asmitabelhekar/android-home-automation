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
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.adapter.CustomAdapter;
import com.example.lenovo.activeandroid3.asyntask.NetworkSniffAsynTask;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
//import com.example.lenovo.activeandroid3.fragments.CusomeBackgroundFragment;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.model.Mode;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.model.SwitchButton;
import com.example.lenovo.activeandroid3.util.Conversions;
import com.example.lenovo.activeandroid3.util.ItemOffsetDecoration;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{


    // name kiran kokate
    private static final String TAG = "MainActivity";
    Rooms room ;
    Long id;
    public List<Rooms> roomList = new ArrayList<>();
    RecyclerView recyclerView ;
    String[] roomNamesArray = {"Hall", "Bed 1" ,"Bed 2","Kitchen"} ;
        int[]  roomImageArray = { R.drawable.home1  , R.drawable.home1 , R.drawable.home1 , R.drawable.home1 } ;
    ArrayList<RoomDbModel> list;
    CustomAdapter customAdapter ;
    Context context ;
    String isTableCreated = "" ;
    DrawerLayout drawer ;
    Button btn_on_off_toolbar ;
    FrameLayout frame ;

    // if this flag is "1" then  hide edit and delete button , if "0" then show it
    String delete_edit_btn_flag = "1" ;
    RelativeLayout relativeLayout ;
    private int PICK_IMAGE_REQUEST = 1;
    String image_Path ;
    private Uri filePath ;

    NavigationView   navigationView ;
    View  nav_header ;


    int[] covers = new int[]
            {
                    R.drawable.nature4,
                    R.drawable.nature1,
                    R.drawable.nature2,
                    R.drawable.nature3 } ;
    SharedPreferences  sharedPref ;
    String email , homeName  ;
    Toolbar toolbar ;
    FloatingActionButton fab ;
    public List<SwitchButton> switchButtonList = new ArrayList<>() ;
//    LinearLayoutManager linearLayoutManager ;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate( savedInstanceState ) ;
        setContentView( R.layout.activity_main ) ;
        toolbar = (Toolbar) findViewById( R.id.toolbar ) ;
        setSupportActionBar(toolbar ) ;

        toolbar.setTitle("Home Automation") ;

        drawer = (DrawerLayout) findViewById( R.id.drawer_layout ) ;
        btn_on_off_toolbar = ( Button )findViewById( R.id.btn_on_off_toolbar ) ;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) ;
        drawer.addDrawerListener( toggle ) ;
        toggle.syncState() ;

        context = this ;
        frame = ( FrameLayout )findViewById( R.id.frame ) ;
        frame.setVisibility(View.GONE ) ;
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout ) ;
        navigationView = ( NavigationView ) findViewById( R.id.nav_view ) ;
        recyclerView = (RecyclerView) findViewById( R.id.recyclerView ) ;

        btn_on_off_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("click","btn") ;
                allButtonOff( );
            }
        });

//        callNetworkSniffAsynTask() ;
        setIconToNavigationDrawerElements() ;

        navigationView.setNavigationItemSelectedListener( this ) ;

        getSharedPreferencesVariable();

        // this will set icon and text to navigation drawer.
        setIconToNavigationDrawerElements();

        nav_header = navigationView.getHeaderView(0);
        loadHeader();


        GridLayoutManager gridLayoutManager = new GridLayoutManager( getApplicationContext(), 2 ) ;
//        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL) ; // set Horizontal Orientation
        recyclerView.setHasFixedSize( true ) ;
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(gridLayoutManager) ; // set LayoutManager to RecyclerView


        if( sharedPref.contains( "image_Path") )
        {
            // get path from sharedPreferrence and set it to the  layout
            getPathFromSharedPreference() ;
        }

        room = new Rooms() ;

        // checkString return true if string is null  , it will return false if string is not null
        if( checkString( isTableCreated ) )
        {
//            Log.e("yes","null"+isTableCreated );

            addRoom() ;
            roomList = getAll() ;
            setToDbModel( roomList ) ;
        }else {
//            Log.e("yes","not null"+isTableCreated );
            roomList = getAll() ;
            getAllButtonStates() ;
            setToDbModel( roomList ) ;
        }


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab.setVisibility( View.GONE ) ;

    }

    private void allButtonOff()
    {
//        new Update(SwitchButton.class ).set("is_on = ?" , "false" ).where("is_on = ?" , "true" ).execute() ;
//        getAllButtonStates();

        List<SwitchButton>  switchButton =  new  Select().from( SwitchButton.class).where("is_on = ?", true ).execute() ;
        List<Mode>  mmd =   new Select().from(Mode.class).where("isOn = ?" ,true ).execute() ;

        for( int i = 0 ; i< mmd.size() ; i++ )
        {
             Mode mode2 = mmd.get( i ) ;
            mode2.isOn = false ;
            mode2.save() ;
        }

        for( int i= 0 ; i < switchButton.size() ; i++ )
        {
            SwitchButton btn = switchButton.get( i) ;
            btn.is_on = false ;
            btn.save() ;
        }
        getAllButtonStates();
    }

    private void getAllButtonStates()
    {

        switchButtonList = getAllButton() ;
        desideHideShow( switchButtonList ) ;
    }

    private List<SwitchButton> getAllButton()
    {
        //  order by id
//        return new Select().from( SwitchButton.class ).orderBy("id ASC").execute() ;

        // access by id
//        return new Select().from(SwitchButton.class).where("id = ?", roomId ).execute();


        // access by RoomId
        return new Select().from( SwitchButton.class ).where("is_on = ?" , true ).execute() ;

        // return data in random order
//        return new Select().from(SwitchButton.class).orderBy("RANDOM()") .execute();

    }

//    private void callNetworkSniffAsynTask()
//    {
//        NetworkSniffAsynTask asyn = new NetworkSniffAsynTask(context );
//        asyn.execute() ;
//    }

    private void getSharedPreferencesVariable()
    {
        sharedPref = PreferenceManager.getDefaultSharedPreferences( context ) ;
        isTableCreated = sharedPref.getString("isTableCreated","" );
        email = sharedPref.getString("email","");
        homeName = sharedPref.getString("homeName","");
    }

    private void loadHeader()
    {

        ImageView imageView_header = ( ImageView )nav_header.findViewById( R.id.drawer_imageView );
        Glide.with( this ).load( R.drawable.flat_white )
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy( DiskCacheStrategy.ALL )
                .into(imageView_header );

        TextView drawerEmail =  ( TextView ) nav_header.findViewById( R.id.drawer_email );
        TextView drawer_home_name = ( TextView ) nav_header.findViewById( R.id.drawer_home_name );

        if( email != null  && !email.equals("null") && !email.equals("") ) {

            drawerEmail.setText( email );
        } else  {

            drawerEmail.setVisibility( View.GONE );
        }

        if( homeName != null  && !homeName.equals("null") && !homeName.equals("") ) {

            drawer_home_name.setText( homeName );
        } else  {

            drawer_home_name.setVisibility( View.GONE );
        }

    }

    private void setIconToNavigationDrawerElements()
    {

/**
 *    This line is used to make first item in drawer chakable , when we open app
 */
        //navigationView.getMenu().getItem(1).setChecked(true);
        Menu menu = navigationView.getMenu() ;

        MenuItem nav_notices_item  = menu.findItem( R.id.nav_modes )  ;
        nav_notices_item.setIcon(R.drawable.nav_icon_mode ) ;

        MenuItem nav_meetings_item  = menu.findItem( R.id.nav_setting ) ;
        nav_meetings_item.setIcon(R.drawable.nav_icon_settings ) ;

        MenuItem nav_assets_item  = menu.findItem( R.id.nav_backimage ) ;
        nav_assets_item.setIcon(R.drawable.nav_icon_background ) ;

    }

    @Override
    protected void onResume()
    {
//        Log.e("inside","onResume") ;
        super.onResume() ;
//        boolean ss = sharedPref.contains( "image_Path" ) ;
//        Log.e("ss",ss+"") ;

        recyclerView.setVisibility( View.VISIBLE ) ;
        frame.setVisibility( View.GONE ) ;
        if( sharedPref.contains( "image_Path" ) )
        {
            // get path from sharedPreferrence and set it to the  layout
            getPathFromSharedPreference() ;
        }

        roomList = getAll() ;
        switchButtonList  =  getAllButton() ;
        desideHideShow( switchButtonList );
        setToDbModel( roomList ) ;
        delete_edit_btn_flag = "1" ;
    }

    private void desideHideShow(List<SwitchButton> switchButtonList)
    {
        Log.e("switchButtonList size",switchButtonList.size()+"" );
        if( switchButtonList.size() != 0 )
        {
            btn_on_off_toolbar.setVisibility( View.VISIBLE );
        }else {
            btn_on_off_toolbar.setVisibility( View.GONE );
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


    // checkString return true if string is null  , it will return false if string is not null
    public boolean checkString( String str )
    {
        return str != null && (  str.equals("null") ||  str.equals("NaN") || str.equals("undefined") || str.equals("") || str.isEmpty() );
    }

//
//    @Override
//    public boolean onNavigationItemSelected( @NonNull MenuItem item )
//    {
//        drawer.closeDrawers() ;
//        final int id = item.getItemId() ;
//        if (id == R.id.nav_modes )
//        {
//            if ( ! item.isChecked() )
//            {
//                Intent intent= new Intent( context , ModesActivity.class ) ;
//                context.startActivity( intent ) ;
//            }
//        }else if ( id == R.id.nav_setting )
//        {
//            if( ! item.isChecked() )
//            {
//                Intent i = new Intent( context , AdminMainActivity.class ) ;
//                startActivity( i ) ;
//            }
//
//        }else  if( id == R.id.nav_backimage )
//        {
//            if( ! item.isChecked() )
//            {
//
//
//                Intent intent = new Intent() ;
//                intent.setType("image/*") ;
//                intent.setAction( Intent.ACTION_GET_CONTENT ) ;
//                startActivityForResult( Intent.createChooser( intent, "Select Picture"), PICK_IMAGE_REQUEST ) ;
//            }
//        }else  if( id == R.id.nav_roomImage )
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
//
//        return false;
//    }


    @Override
    public boolean onNavigationItemSelected( @NonNull MenuItem item )
    {
        drawer.closeDrawers() ;
        final int id = item.getItemId() ;
        if (id == R.id.nav_modes )
        {
            if ( ! item.isChecked() )
            {
                Intent intent= new Intent( context , ModesActivity.class ) ;
                context.startActivity( intent ) ;
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_right ) ;
            }
        }else if ( id == R.id.nav_setting )
        {
            if( ! item.isChecked() )
            {
                Intent i = new Intent( context , AdminMainActivity.class ) ;
                startActivity( i ) ;
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_right ) ;
            }

        }else  if( id == R.id.nav_backimage )
        {
            if( ! item.isChecked() )
            {


                Intent intent = new Intent() ;
                intent.setType("image/*") ;
                intent.setAction( Intent.ACTION_GET_CONTENT ) ;
                startActivityForResult( Intent.createChooser( intent, "Select Picture"), PICK_IMAGE_REQUEST ) ;
            }
        }
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

        return false;
    }



    private void setToDbModel(List<Rooms> roomList )
    {
        list = new ArrayList<>( );
        for ( int i = 0 ; i < roomList.size() ; i++ )
        {
//            try
//            {
//                Log.e("name in set",roomList.get( i ).RoomImage ) ;
//            }catch ( Exception e )
//            {
//                Log.e("null image","in Room table") ;
//            }



            list.add(new RoomDbModel
                    (
                            roomList.get(i).getId() ,
                            roomList.get( i ).Name ,
                            roomList.get( i ).CreatedAt ,
                            roomList.get( i ).Updatedat ,
                            roomList.get( i).RoomHomePageImage,
                            roomList.get(i).AddRoomImage,
                            roomList.get( i).RoomOnOff,
                            roomList.get( i).AtLeastOneButtonOfRoomIsOn
                    ));
        }


//        Log.e("list size", list.size()+"" );

        // here false is for hinding  the ll_edit_delete_btn

        customAdapter = new CustomAdapter( MainActivity.this, list , covers , MainActivity.this ,  true , delete_edit_btn_flag ) ;
//        customAdapter.setRecyclerView(recyclerView , fab );
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

//        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager() ;


//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(customAdapter);
//        recyclerView.setVerticalScrollBarEnabled(false);
//        recyclerView.setHorizontalScrollBarEnabled(false);
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.stopNestedScroll();
//        recyclerView.stopScroll();

    }


    private void addRoom()
    {
        ActiveAndroid.beginTransaction() ;
        try {
            for (int i = 0; i < roomNamesArray.length ; i++ )
            {
                try
                {
//                    Log.e("int to string", String.valueOf( roomImageArray[ i ] ) ) ;
                    Date currentTime = Calendar.getInstance().getTime() ;
                    Rooms rooms = new Rooms() ;
                    rooms.Name = roomNamesArray[i] ;
                    rooms.RoomHomePageImage = String.valueOf(R.drawable.hall_room) ;
                    rooms.CreatedAt = currentTime.getTime() ;
                    rooms.Updatedat = currentTime.getTime() ;
                    rooms.save() ;
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
        editor.putString("isTableCreated", "1" ) ;
        editor.commit() ;
    }

    private void showList( List<Rooms> roomList )
    {
        for ( int i = 0 ; i < roomList.size() ; i++ )
        {
//            Log.e("id is "+roomList.get( i ).getId(),"Name is "+roomList.get( i ).Name  );
        }
    }

    private void insertData( Rooms room )
    {
        id = room.save() ;
        recreate() ;
    }

    // Read all Data
    private List<Rooms> getAll()
    {
//        Log.e("inside","getAll");

        //  order by id
        return new Select().from(Rooms.class).orderBy("id ASC").execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
//        Log.e("inside","onBackPressed"+TAG ) ;

        setUnchackedAllDrawerOptions() ;

        onResume() ;

    }

    private void setUnchackedAllDrawerOptions()
    {
        // this code is for -  when i come back to main activity i want to make
        //  visibility of all navigation drawer element as "flase"
        // hence i check "isChecked"  if "true"   then make it "false"
        Menu drawer_menu = navigationView.getMenu() ;
        MenuItem menuItem ;
        menuItem = drawer_menu.findItem( R.id.nav_modes ) ;
        if( menuItem.isChecked() )
        {
            menuItem.setChecked( false ) ;
        }
    }



    private void changeFragment(Fragment fragment )
    {
        try {

            Log.e("inside on","changeFragment") ;

            getSupportFragmentManager().beginTransaction()
                    .add(  R.id.frame , fragment )
                    .addToBackStack( null )
                    .commit() ;
        }
        catch (Exception e)
        {

        }
    }

    @Override
    protected void onActivityResult( int requestCode , int resultCode , Intent data )
    {
        super.onActivityResult( requestCode, resultCode, data ) ;

        // if the result is Gallery Image
        if ( requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            image_Path = getPath1( filePath ) ;
            try
            {

                Log.e("add to ","sharedPreff image_Path ") ;
                Log.e("image_Path" ,image_Path );
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("image_Path", image_Path ) ;
                editor.commit();
                editor.apply();

            } catch ( Exception e ) {
                e.printStackTrace() ;
                Log.e("Error", e.getMessage() ) ;
            }
        }

    }

    public String getPath1 ( Uri uri )
    {
        try {

            String path;
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if ( cursor == null ) { // Source is Dropbox or other similar local file path

                path = uri.getPath();
            } else {

                cursor = context.getContentResolver().query( uri, null, null, null, null );
                if ( cursor != null ) {
                    cursor.moveToFirst();
                }
                String document_id = null;
                if ( cursor != null ) {
                    document_id = cursor.getString(0);
                }
                if ( document_id != null ) {
                    document_id = document_id.substring( document_id.lastIndexOf(":") + 1 );
                }
                if ( cursor != null ) {
                    cursor.close();
                }

                cursor = context.getContentResolver().query(
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
                if ( cursor != null ) {
                    cursor.moveToFirst();
                }
                path = cursor.getString( cursor.getColumnIndex( MediaStore.Images.Media.DATA ) );
                //Log.e(" Gallery path", path);

                cursor.close();
            }
            return path;

        } catch ( Exception e ) {
            Log.e("A", e.getMessage() );
            return null;
        }
    }



}

