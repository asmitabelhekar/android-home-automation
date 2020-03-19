package com.example.lenovo.activeandroid3.activity.v1.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.dbModel.ModelNewDbModel;
import com.example.lenovo.activeandroid3.dbModel.ModesActivityDbModel;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.model.ModeOnOf;
import com.example.lenovo.activeandroid3.model.SwitchButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 28/5/18.
 */

public class EditModeDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    ModeFragmentAdapter modeFragmentAdapter;
    Context context ;
    AlertDialog alertDialog_edit_mode ;
    ArrayList<SwitchButtonDbModel> buttonList ;
    ArrayList<RoomDbModel>  roomList ;
    ArrayList<ModelNewDbModel> roomModeList ;
    Gson gson ;
    EditModeDialogAdapterInside editModeDialogAdapterInside ;
    ArrayList<EditModeDialogAdapterInside> array_adapter = null ;

    public  Long modeId ;
    public ArrayList<String> arrayButtonId ;
    List<ModeOnOf> listModeOnOf = new ArrayList<>() ;

    public EditModeDialogAdapter(ModeFragmentAdapter modeFragmentAdapter, Context context, AlertDialog alertDialog_edit_mode, Long modeId)
    {
        this.context =context ;
        this.modeFragmentAdapter =modeFragmentAdapter ;
        this.alertDialog_edit_mode =alertDialog_edit_mode ;
        array_adapter = new ArrayList<>();
        buttonList = new ArrayList<>();
        roomList = new ArrayList<>();
        roomModeList= new ArrayList<>() ;
        this.modeId = modeId ;

        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();

        listModeOnOf  = getDataFromModeOnOfTable( );
//        Log.e("listModeOnOf size ",listModeOnOf.size()+"" );
        createArrayOfButtonId( listModeOnOf );
    }

    private List<ModeOnOf> getDataFromModeOnOfTable()
    {
        //  order by id
        return new Select().from( ModeOnOf.class).where("ModeId = ?", modeId ).execute() ;
    }

    private void createArrayOfButtonId(List<ModeOnOf> listModeOnOf)
    {
//        Log.e("=================","======ModeOnOffData ==============");
        arrayButtonId = new ArrayList<>();
        for( int i = 0 ; i< listModeOnOf.size() ; i++ )
        {
            ModeOnOf temp  = listModeOnOf.get( i ) ;
//            Log.e("bbb","  "+temp.ButtonId +"  "+temp.ButtonName +"  "+temp.ModeId );
            arrayButtonId.add( temp.ButtonId ) ;
        }
//        Log.e("=================","=====ModeOnOffData==============");
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new EditModeDialogAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_edit_mode, parent , false ));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if( holder instanceof EditModeDialogAdapter.MyViewHolder )
        {
            try
            {
                final ModelNewDbModel singleItem = (ModelNewDbModel) roomModeList.get(position);

                    ((MyViewHolder) holder).tv_room_name.setText(singleItem.getRoomName());

                    String switchButtonStringArray = singleItem.getSwitchButtonArray() ;

//                    Log.e(singleItem.getRoomName() , "switchButtonStringArray "+ switchButtonStringArray );

                    JSONArray my_result_set  = new JSONArray( switchButtonStringArray );

                    Type list_type = new TypeToken<ArrayList<SwitchButtonDbModel>>() {}.getType();
                    buttonList = gson.fromJson(String.valueOf(my_result_set), list_type);

                    ArrayList<SwitchButtonDbModel> objectArrayList = new ArrayList<>();

                    for( int i=0 ; i < buttonList.size() ; i++ )
                    {
                        SwitchButtonDbModel switchButtonDbModel =  buttonList.get( i );
//                      Log.e("roomDbModel "+i , switchButtonDbModel.getId() +"  "+switchButtonDbModel.getsButtonName() +"  "+switchButtonDbModel.getRoomId() );

                        objectArrayList.add( switchButtonDbModel);

                    }

                    editModeDialogAdapterInside = new EditModeDialogAdapterInside( context , objectArrayList , EditModeDialogAdapter.this ) ;
                    ((MyViewHolder) holder).recycler_view_inside_adapter.setAdapter( editModeDialogAdapterInside );
                    editModeDialogAdapterInside.notifyDataSetChanged();

                    array_adapter.add( editModeDialogAdapterInside ) ;

            }catch (Exception e )
            {
                Log.e("excep in ","EditModeDialogAdapter "+e.getMessage() ) ;
            }
        }
    }

    public void addItem(ArrayList<ModelNewDbModel> roomModeListData)
    {
        roomModeList.addAll(roomModeListData ) ;
        notifyDataSetChanged();
    }

    public void changeOnOffFlagOfButton(int position, boolean onOffFlag, int roomPositionWithButton, int buttonPositionInsideRoom  )
    {
        ModelNewDbModel modelNewDbModel =   roomModeList.get( roomPositionWithButton ) ;
        Log.e("Room Name : ",modelNewDbModel.getRoomName() );

        String buttonArray = modelNewDbModel.getSwitchButtonArray() ;

        Log.e("old buttonArray ",buttonArray );

        ArrayList<SwitchButtonDbModel>  switchButtonDbModelList = new ArrayList<>() ;
        ArrayList<SwitchButtonDbModel>  addInThisArrayList = new ArrayList<>() ;
        Type list_type = new TypeToken<ArrayList<SwitchButtonDbModel>>() {}.getType();
        switchButtonDbModelList = gson.fromJson(String.valueOf(buttonArray), list_type);

        for(  int i = 0 ; i < switchButtonDbModelList.size() ; i++ )
        {
            SwitchButtonDbModel dBModel  =  switchButtonDbModelList.get( i ) ;
            if( buttonPositionInsideRoom == i )
            {
                dBModel.setOn(onOffFlag);
            }
            addInThisArrayList.add( dBModel ) ;
        }

        String addInThisArrayListString =  gson.toJson( addInThisArrayList , ArrayList.class) ;
        Log.e("new",addInThisArrayListString );

        modelNewDbModel.setSwitchButtonArray( addInThisArrayListString );

        Log.e("after set",modelNewDbModel.getSwitchButtonArray()  );
        Log.e("arrayButtonId size",arrayButtonId.size()+"" );

        Log.e("============","===========" );
        for( int i =0 ;  i < arrayButtonId.size() ; i++ )
        {
            Log.e("arrayButtonId : ",arrayButtonId.get( i) );
        }
        Log.e("============","===========" );
//
//
//        SwitchButtonDbModel dbModel = switchButtonDbModelList.get( position ) ;
//        dbModel.setOn( onOffFlag ) ;
    }

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_room_name ;
//        public ListView listView ;

        RecyclerView recycler_view_inside_adapter;


                LinearLayout ll_main  ;
//        public ImageView iv_button_image  ,iv_delete_button ;
        public MyViewHolder( View view )
        {
            super( view ) ;

            tv_room_name = ( TextView ) view.findViewById( R.id.tv_room_name ) ;
//            listView = (ListView)view.findViewById( R.id.listView );
            recycler_view_inside_adapter = ( RecyclerView )view.findViewById( R.id.recycler_view_inside_adapter );
            LinearLayoutManager    layout_manager = new LinearLayoutManager(context);
            this.recycler_view_inside_adapter.setLayoutManager(layout_manager);

            layout_manager.scrollToPosition( 0);

            DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recycler_view_inside_adapter.getContext()  ,
                    DividerItemDecoration.VERTICAL );
            Drawable horizontalDivider = ContextCompat.getDrawable(context , R.drawable.horizontal_divider);
            horizontalDecoration.setDrawable(horizontalDivider);
            recycler_view_inside_adapter.addItemDecoration(horizontalDecoration);


//            iv_button_image =(ImageView )view.findViewById( R.id.iv_button_image ) ;
//            iv_delete_button =(ImageView )view.findViewById( R.id.iv_delete_button ) ;
//            ll_main = ( LinearLayout )view.findViewById( R.id.ll_main );
        }
    }


    @Override
    public int getItemCount()
    {
        return roomModeList.size() ;
    }
}
