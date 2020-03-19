package com.example.lenovo.activeandroid3.activity.v1.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.v1.WrapContentGridLayoutManager;
import com.example.lenovo.activeandroid3.activity.v1.activity.V1MainActivity;
import com.example.lenovo.activeandroid3.activity.v1.adapter.AddRoomDialogAdapter;
import com.example.lenovo.activeandroid3.activity.v1.adapter.RoomFragmentAdapter;
import com.example.lenovo.activeandroid3.activity.v1.adapter.RoomInsideDialogAdapter;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.model.SwitchButton;
import com.example.lenovo.activeandroid3.util.ItemOffsetDecoration;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 17/5/18.
 */

public class RoomFragment extends Fragment
{
    String TAG = "RoomFragment" ;
    RecyclerView recyclerView;

    RoomFragment my_object = this;
    public RoomFragmentAdapter adapter;
    LinearLayoutManager layout_manager;
    Context context ;
    AlertDialog  alertDialog ;

    boolean atLeastOneButtonOfRoomIsOn ;

    // No Data available
    FlexboxLayout flexbox_no_data_available ;
    ImageView iv_no_data_image ;
    TextView tv_no_data_title ;

    public List<Rooms> roomList = new ArrayList<>();
    ArrayList<RoomDbModel> list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        Log.e(TAG ,"onCreate");
        context = getActivity() ;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState )
    {

        View rootview = inflater.inflate( R.layout.fragment_room , container , false ) ;

        try
        {
//            Log.e(TAG ,"onCreateView");
            this.recyclerView = ( RecyclerView ) rootview.findViewById( R.id.recycler_view ) ;
            FloatingActionButton fab_check_in = ( FloatingActionButton )rootview.findViewById( R.id.fab_check_in );

            flexbox_no_data_available =( FlexboxLayout )rootview.findViewById( R.id.v2_no_data_present_xml );
            flexbox_no_data_available.setVisibility( View.GONE ) ;
            iv_no_data_image = ( ImageView )rootview.findViewById( R.id.v2_no_data_image ) ;
            tv_no_data_title=(TextView)rootview.findViewById( R.id.v2_tv_no_data_title ) ;

            layout_manager = new LinearLayoutManager( getActivity() ) ;

            this.recyclerView.setLayoutManager( layout_manager );

//            GridLayoutManager gridLayoutManager =  new GridLayoutManager( context, 2 ) ;
            recyclerView.setHasFixedSize( true ) ;
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
            recyclerView.addItemDecoration(itemDecoration);
//            recyclerView.setLayoutManager(gridLayoutManager) ; // set LayoutManager to RecyclerView

            recyclerView.setLayoutManager(new WrapContentGridLayoutManager(context, 2));

            adapter = new RoomFragmentAdapter(   context  , my_object ) ;
            this.recyclerView.setAdapter( adapter ) ;


            fab_check_in.setVisibility(  View.GONE );

            fab_check_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    addRoomDialog() ;
                }
            });

            onLoadData() ;

        }catch (Exception e){
            Log.e(TAG + "Bind",e.getMessage());
        }
        return rootview;
    }

    public void makeAdapterListNull()
    {
        this.adapter.setListNull();
    }

    //  if isOnMainScreenOrDialog == 1 then show on main screen , if  isOnMainScreenOrDialog == 2 then show on dialog
    public void onLoadData()
    {
        Log.e(TAG,"onLoadData") ;
        roomList.clear();
        roomList =  getAllRoom() ;
        for (Rooms rr: roomList)
        {
//            Log.e("Room data : " , "id name on/off  "+ rr.getId()+"  "+ rr.Name +"  "+ rr.RoomOnOff ) ;
        }
        setToDbModel( roomList ) ;
    }

    // Read all Data
    private List<Rooms> getAllRoom()
    {
        return new Select().from(Rooms.class).where("RoomOnOff = ?", 1).orderBy("id ASC").execute();
    }

    // set data to main screen
    private void setToDbModel(List<Rooms> roomList  )
    {
        list = new ArrayList<>( );
        for ( int i = 0 ; i < roomList.size() ; i++ )
        {
            List<SwitchButton> buttonList =  getAllButtonOfRoom( roomList.get( i ).getId() ) ;
            atLeastOneButtonOfRoomIsOn =false ;

            for ( SwitchButton button : buttonList )
            {
                if( button.is_on )
                    atLeastOneButtonOfRoomIsOn = true ;
            }

            list.add(new RoomDbModel
                    (
                            roomList.get(i).getId() ,
                            roomList.get(i).Name,
                            roomList.get(i).CreatedAt,
                            roomList.get(i).Updatedat,
                            roomList.get(i).RoomHomePageImage,
                            roomList.get(i).AddRoomImage,
                            roomList.get(i).RoomOnOff ,
                            atLeastOneButtonOfRoomIsOn
                    ));
        }
        adapter.addItem(list);
    }

    public   List<SwitchButton> getAllButtonOfRoom(Long roomId)
    {
        // access by RoomId
        return new Select().from( SwitchButton.class ).where("RoomId = ?", roomId ).execute() ;
    }

    public void testMethod()
    {
        Log.e(TAG, "testMethod: " );
    }

    //    private void addRoomDialog()
//    {
//
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme);
//        LayoutInflater inflater;
//        inflater = LayoutInflater.from(context);
//        final View dialogView = inflater.inflate(R.layout.v1_add_room, null);
//        dialogBuilder.setView(dialogView);
//        alertDialog = dialogBuilder.create();
//        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
//        alertDialog.show();
//
//        this.recycler_view_add_room_dialog = (RecyclerView) dialogView.findViewById(R.id.recycler_view_add_room_dialog );
//        LinearLayout ll_tv_save = (LinearLayout)dialogView.findViewById(R.id.ll_tv_save );
//        ImageView in_back_arrow = (ImageView)dialogView.findViewById( R.id.in_back_arrow );
//        in_back_arrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                alertDialog.cancel();
//            }
//        });
//        layout_manager = new LinearLayoutManager(context);
//        this.recycler_view_add_room_dialog.setLayoutManager(layout_manager);
//        addRoomDialogAdapter = new AddRoomDialogAdapter( this ,context  , alertDialog );
//        this.recycler_view_add_room_dialog.setAdapter( addRoomDialogAdapter );
//
//
//
//        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recycler_view_add_room_dialog.getContext(),
//                DividerItemDecoration.VERTICAL);
//        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider);
//        horizontalDecoration.setDrawable(horizontalDivider);
//        recycler_view_add_room_dialog.addItemDecoration(horizontalDecoration);
//
//        // show all rooms
//        onLoadData( 2 );
//
//
//        ll_tv_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                Log.e("click on","save") ;
//                addRoomDialogAdapter.updateRoomDatabase();
//            }
//        });
//
//
////        for (RoomDbModel roomDbModel : list )
////        {
////            Log.e("rName rImage rImageAdd ", roomDbModel.getrName()+"  "+ roomDbModel.getRoomHomePageImage()+"  "+roomDbModel.getAddRommImage()  );
////
////
////        }
//
//    }

}
