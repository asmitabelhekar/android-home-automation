package com.example.lenovo.activeandroid3.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.adapter.AdminMainActivityAdapter;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.util.Conversions;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminMainActivity extends AppCompatActivity {

    //    Rooms room ;
    String TAG = "AdminMainActivity" ;
    Long id ;
    public List<Rooms> roomList = new ArrayList<>() ;
    RecyclerView recyclerView ;
    String[] roomNamesArray = {"Hall", "Bed 1" ,"Bed 2","Kitchen"} ;
    ArrayList<RoomDbModel> list ;
    Context context ;
    AlertDialog  alertDialog ;
    String delete_edit_btn_flag = "0" ;
    Uri filePath ;


    //Uri to store the image uri
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 2;
    String image_Path = "" ;
    //    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100 ;
    ImageView imageview2 ,iv_camera , iv_gallery  ;

    boolean clickFromActivity = false ;





    //    int position ;
//    int images[] = { R.drawable.nature1 , R.drawable.nature2 , R.drawable.nature3 , R.drawable.nature4 } ;
    RelativeLayout relativeLayout ;
    SharedPreferences sharedPref ;

    int[] covers = new int[]
            {
                    R.drawable.nature4,
                    R.drawable.nature1,
                    R.drawable.nature2,
                    R.drawable.nature3 };

    AdminMainActivityAdapter customAdapter ;
    FloatingActionButton fab ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo ) ;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        toolbar.setTitle("Home Automation");


         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addSingleRoom() ;
            }
        });

        context = this ;
        sharedPref = PreferenceManager.getDefaultSharedPreferences( context );
        if( sharedPref.contains( "image_Path" ) )
        {
            // get path from sharedPreferrence and set it to the  layout
            getPathFromSharedPreference() ;
        }

        recyclerView = (RecyclerView) findViewById( R.id.recyclerView ) ;
        GridLayoutManager gridLayoutManager = new GridLayoutManager( getApplicationContext() , 2 ) ;
//        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL) ; // set Horizontal Orientation
        recyclerView.setHasFixedSize( true ) ;
        recyclerView.setLayoutManager(gridLayoutManager) ; // set LayoutManager to RecyclerView

        customAdapter = new AdminMainActivityAdapter( AdminMainActivity.this , this , list , covers , false  , delete_edit_btn_flag ) ;
        customAdapter.setRecyclerView( recyclerView , fab ) ;
        recyclerView.setAdapter( customAdapter ) ; // set the Adapter to RecyclerView

        roomList = getAll() ;
        setToDbModel( roomList ) ;

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Log.e("inside","onResume AdminMainActivity") ;



        if( sharedPref.contains( "image_Path" ) )
        {
            // get path from sharedPreferrence and set it to the  layout
            getPathFromSharedPreference();
        }

        roomList = getAll() ;
        setToDbModel( roomList ) ;
        delete_edit_btn_flag = "0" ;
    }

    private void getPathFromSharedPreference()
    {
        filePath  =  Uri.parse( sharedPref.getString("image_Path", "" ) ) ;
        File f = new File(getRealPathFromURI( filePath ) ) ;
        Drawable d = Drawable.createFromPath( f.getAbsolutePath() ) ;
        relativeLayout.setBackground( d ) ;
    }

    private String getRealPathFromURI( Uri contentURI )
    {
        Cursor cursor = getContentResolver().query( contentURI, null, null, null, null ) ;
        if ( cursor == null ) { // Source is Dropbox or other similar local file path
            return contentURI.getPath() ;
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA ) ;
            return cursor.getString( idx ) ;
        }
    }


    /*Back Button*/
    public boolean onOptionsItemSelected( MenuItem item )
    {
//        Log.e("inisde","onOptionsItemSelected"+TAG );
        int id = item.getItemId() ;
        if ( id == android.R.id.home ) {
            finish() ;
            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave ) ;
            return true ;
        }
        return super.onOptionsItemSelected( item ) ;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave ) ;
    }


//
//    /*Back Button*/
//    public boolean onOptionsItemSelected( MenuItem item )
//    {
////        Log.e("inisde","onOptionsItemSelected"+TAG );
//        int id = item.getItemId() ;
//        if ( id == android.R.id.home ) {
//            finish() ;
//            return true ;
//        }
//        return super.onOptionsItemSelected( item ) ;
//    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );

        if( clickFromActivity )
        {
            // if the result is capturing Image
            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

                if (resultCode == RESULT_OK) {

                    launchUploadActivity(true);
                } else if (resultCode == RESULT_CANCELED) {

                    // user cancelled Image capture
                    Toast.makeText(context,
                            "U cancelled image capture", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to capture image
                    Toast.makeText(context,
                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            // if the result is Gallery Image
            if ( requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                image_Path = getPath1( filePath );
                try {
                    imageview2.setVisibility( View.VISIBLE );
                    Glide.with( context ).load( filePath ).into( imageview2 );
                } catch ( Exception e ) {
                    e.printStackTrace();
                    Log.e("Error", e.getMessage());
                }
            }


        }else {
            customAdapter.onActivityResult(requestCode, resultCode, data);
        }
    }



    private void addSingleRoom() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder( context, R.style.AppTheme ) ;
        LayoutInflater inflater ;
        inflater = LayoutInflater.from( context ) ;
        final View dialogView = inflater.inflate( R.layout.add_room_with_image ,  null ) ;
        dialogBuilder.setView( dialogView ) ;
        alertDialog = dialogBuilder.create() ;
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.show() ;

        clickFromActivity = false ;




        ImageView in_back_arrow = ( ImageView ) dialogView.findViewById( R.id.in_back_arrow ) ;
        in_back_arrow.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        Glide.with( context ).load( R.drawable.back_arrow )
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy( DiskCacheStrategy.ALL )
                .into( in_back_arrow ) ;
        TextView add_flat_staff_title = ( TextView ) dialogView.findViewById( R.id.add_flat_staff_title ) ;
        add_flat_staff_title.setText("Add Room");

        iv_camera = ( ImageView ) dialogView.findViewById( R.id.iv_camera ) ;
        iv_gallery = ( ImageView ) dialogView.findViewById( R.id.iv_gallery ) ;
        imageview2 = ( ImageView ) dialogView.findViewById( R.id.imageview2 ) ;

        iv_camera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clickFromActivity = true ;
                // Checking camera availability
                if (!isDeviceSupportCamera()) {
                    Toast.makeText(context ,
                            "Sorry! Your device doesn't support camera",
                            Toast.LENGTH_LONG).show();
                } else {

                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                }
            }
        });


        iv_gallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clickFromActivity = true ;
                Log.e("click","on gallery") ;
                Intent intent = new Intent() ;
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        final EditText tv_service_name = (EditText) dialogView.findViewById(R.id.tv_service_name);

        Button cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button submit = (Button) dialogView.findViewById(R.id.submit);
        submit.setText("Add");

        final TextInputLayout textLayoutinput_service_name = (TextInputLayout) dialogView.findViewById(R.id.textLayoutinput_service_name);
        textLayoutinput_service_name.setHint("Enter Room Name");

        cancel.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textLayoutinput_service_name.setError(null);

                boolean cancel = false ;
                View focusView = null ;

                String tv_service_name_text = tv_service_name.getText().toString().trim() ;

                if (TextUtils.isEmpty(tv_service_name_text)) {
                    tv_service_name.setError("Enter Room Name");
                    focusView = tv_service_name;
                    cancel = true;
                }

                if ( cancel ) {
                    focusView.requestFocus() ;
                } else
                {
                    Rooms r = new Rooms() ;
                    r.Name = Conversions.makeFirstLeterCap( tv_service_name_text  ) ;

                    Date currentTime = Calendar.getInstance().getTime() ;
                    r.CreatedAt = currentTime.getTime() ;
                    r.Updatedat = currentTime.getTime()  ;
                    r.RoomHomePageImage = image_Path ;
                    Long id = r.save();
                    clickFromActivity = false ;
                    alertDialog.cancel();
                    Rooms dd =  readSingle( id )  ;
                    customAdapter.addOneMoreItem( dd );
                }

            }
        });
    }

    private boolean isDeviceSupportCamera() {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public Uri getOutputMediaFileUri(int type) {


        Log.e("checking path", Uri.fromFile(getOutputMediaFile(type)).toString());
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type)
    {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                com.example.lenovo.activeandroid3.activity.Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("check", "Oops! Failed create "
                        +  com.example.lenovo.activeandroid3.activity.Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {

            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");

        } else {
            return null;
        }

        return mediaFile;
    }

    public void launchUploadActivity(boolean isImage)
    {
        try {
            Log.e("inside","launchUploadActivity") ;

            imageview2.setVisibility(View.VISIBLE ) ;
            Glide.with(context).load( fileUri ).into( imageview2 ) ;
            image_Path = getPath1(fileUri);
        } catch (Exception e) {

            e.printStackTrace();
            Log.e("Error", e.getMessage());
        }
    }

    public String getPath1 (Uri uri)
    {
        try {

            String path;
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path

                path = uri.getPath();
            } else {


                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                }
                String document_id = null;
                if (cursor != null) {
                    document_id = cursor.getString(0);
                }
                if (document_id != null) {
                    document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
                }
                if (cursor != null) {
                    cursor.close();
                }

                cursor = context.getContentResolver().query(
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                }
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                //Log.e(" Gallery path", path);

                cursor.close();
            }
            return path;

        } catch (Exception e) {
            Log.e("A", e.getMessage());
            return null;
        }
    }





    private Rooms readSingle( Long id )
    {
        return new Select().from(Rooms.class).where("id = ?", id).executeSingle();
    }

    private void setToDbModel(List<Rooms> roomList)
    {
        list = new ArrayList<>( );
        for ( int i = 0 ; i < roomList.size() ; i++ )
        {
            Log.e("name in set",roomList.get( i ).Name );
            list.add(new RoomDbModel(
                    roomList.get(i).getId() ,
                    roomList.get( i ).Name,
                    roomList.get( i ).CreatedAt ,
                    roomList.get( i ).Updatedat ,
                    roomList.get( i).RoomHomePageImage,
                    roomList.get( i).AddRoomImage,
                    roomList.get( i).RoomOnOff,
                    roomList.get( i).AtLeastOneButtonOfRoomIsOn
            ));
        }

        customAdapter.addItemMore( list );



//        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
//        layoutManager.setFlexWrap(FlexWrap.WRAP);
//        layoutManager.setFlexDirection(FlexDirection.ROW);
//        layoutManager.setAlignItems(AlignItems.CENTER);
//        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
//
//        // layoutManager.setAlignContent(AlignContent.CENTER);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(customAdapter);
//        recyclerView.setVerticalScrollBarEnabled(false);
//        recyclerView.setHorizontalScrollBarEnabled(false);
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.stopNestedScroll();
//        recyclerView.stopScroll();

    }

    private List<Rooms> getAll()
    {

        //  order by id
        return new Select().from(Rooms.class).orderBy("id ASC").execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();

    }

//    private void addRoom()
//    {
//        ActiveAndroid.beginTransaction();
//        try {
//
//            for ( int i = 0; i < roomNamesArray.length ; i++ ) {
//                long timestamp = new Timestamp(System.currentTimeMillis() * 1000 ).getTime() ;
//                Rooms rooms = new Rooms();
//                rooms.Name = roomNamesArray[i];
//                rooms.CreatedAt = timestamp ;
//                rooms.Updatedat = timestamp;
//                rooms.save();
//            }
//            ActiveAndroid.setTransactionSuccessful();
//        } finally {
//            ActiveAndroid.endTransaction();
//        }
//    }






}