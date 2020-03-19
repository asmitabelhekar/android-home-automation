package com.example.lenovo.activeandroid3.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.adapter.AddRoomImageActivityAdapter;
import com.example.lenovo.activeandroid3.adapter.CustomAdapter;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.model.Rooms;

import java.util.ArrayList;
import java.util.List;

public class AddRoomImageActivity extends AppCompatActivity {

    Toolbar toolbar ;
    RecyclerView recyclerView ;
    Context context ;
    public List<Rooms> roomList = new ArrayList<>();
    ArrayList<RoomDbModel> list;
    LinearLayoutManager layout_manager ;
    AddRoomImageActivityAdapter adapter ;
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState ) ;
        setContentView( R.layout.activity_add_room_image ) ;
        toolbar = (Toolbar) findViewById( R.id.toolbar ) ;
        setSupportActionBar(toolbar ) ;

        toolbar.setTitle("Room Image") ;
        context = this ;
        recyclerView = (RecyclerView) findViewById( R.id.recyclerView ) ;
        layout_manager = new LinearLayoutManager( context ) ;
        recyclerView.setLayoutManager( layout_manager );



        adapter = new AddRoomImageActivityAdapter( context   , AddRoomImageActivity.this );
        recyclerView.setAdapter(adapter);


        roomList = getAll() ;
        setToDbModel( roomList ) ;


    }

    /*Back Button*/
    public boolean onOptionsItemSelected(MenuItem item)
    {
//        Log.e("inisde","onOptionsItemSelected"+TAG );
        int id = item.getItemId() ;
        if ( id == android.R.id.home ) {
            finish() ;
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        super.onActivityResult( requestCode, resultCode, data );

        adapter.onActivityResult( requestCode,resultCode,data );

////        Log.e("inside","onActivityResult Fragment");
//
//        if ( imageFrom == 0 )
//        {
////            Log.e("inside","if");
//            imageFrom = 1;
//
//            // if the result is capturing Image
//            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//
//                if (resultCode == RESULT_OK) {
//
//                    launchUploadActivity(true);
//                } else if (resultCode == RESULT_CANCELED) {
//
//                    // user cancelled Image capture
//                    Toast.makeText(context,
//                            "U cancelled image capture", Toast.LENGTH_SHORT)
//                            .show();
//                } else {
//                    // failed to capture image
//                    Toast.makeText(context,
//                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
//                            .show();
//                }
//            }
//
//            // if the result is Gallery Image
//            if ( requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//                filePath = data.getData();
//                image_Path = getPath1( filePath );
//                try {
//                    imageview2.setVisibility( View.VISIBLE );
//                    Glide.with( context ).load( filePath ).into( imageview2 );
//                } catch ( Exception e ) {
//                    e.printStackTrace();
//                    Log.e("Error", e.getMessage());
//                }
//            }
//        }else
//            adapter.onActivityResult( requestCode,resultCode,data );

    }



    private void setToDbModel(List<Rooms> roomList )
    {
        list = new ArrayList<>( ) ;
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
                            roomList.get( i).RoomOnOff ,
                            roomList.get( i).AtLeastOneButtonOfRoomIsOn
                    ));
        }


        Log.e("list size", list.size()+"" );

        adapter.addItemMore(list);

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

}
