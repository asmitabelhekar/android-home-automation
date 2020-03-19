package com.example.lenovo.activeandroid3.activity.v1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.v1.activity.DemoModeActivity;
import com.example.lenovo.activeandroid3.dbModel.ModelNewDbModel;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by akshay on 30/5/18.
 */

public class DemoModeAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<ModelNewDbModel> roomModeList ;
    DemoModeActivity demoModeActivity ;

    LayoutInflater inflter;

    Gson gson ;
    ArrayList<SwitchButtonDbModel> buttonList ;
    ArrayList<DemoModeAdapterInside> array_adapter = null ;

    public DemoModeAdapter(Context context, ArrayList<ModelNewDbModel> roomModeList, DemoModeActivity demoModeActivity)
    {
        this.context =context;
        this.roomModeList = roomModeList;
        this.demoModeActivity = demoModeActivity ;

        inflter = (LayoutInflater.from( context ) ) ;

        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
        buttonList = new ArrayList<>();
        array_adapter = new ArrayList<>();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent)
    {
        try {
            view = inflter.inflate(R.layout.demo_item_mode_adapter, null);

            if( i == 0) {

                TextView tv_room_name = (TextView) view.findViewById(R.id.tv_room_name);
                ListView listView = (ListView) view.findViewById(R.id.listView);

                final ModelNewDbModel singleItem = (ModelNewDbModel) roomModeList.get(i);

                tv_room_name.setText(singleItem.getRoomName());

                String switchButtonStringArray = singleItem.getSwitchButtonArray();

                JSONArray my_result_set = new JSONArray(switchButtonStringArray);

                Type list_type = new TypeToken<ArrayList<SwitchButtonDbModel>>() {
                }.getType();
                buttonList = gson.fromJson(String.valueOf(my_result_set), list_type);
                Log.e("buttonList size ", buttonList.size() + "");

                ArrayList<SwitchButtonDbModel> objectArrayList = new ArrayList<>();

                for (int j = 0; j < buttonList.size(); j++) {

                    SwitchButtonDbModel switchButtonDbModel = buttonList.get(j);
//                    Log.e("roomDbModel "+i , switchButtonDbModel.getId() +"  "+switchButtonDbModel.getsButtonName() +"  "+switchButtonDbModel.getRoomId() );

                    objectArrayList.add(switchButtonDbModel);

                }

//                DemoModeAdapterInside demoModeAdapterInside = new DemoModeAdapterInside(context, objectArrayList, DemoModeAdapter.this);
//                EditModeDialogAdapterInside aa = new EditModeDialogAdapterInside(context ,objectArrayList , null );
//                listView.setAdapter(aa);

//                array_adapter.add(demoModeAdapterInside);
            }

            return view ;
        }catch ( Exception e )
        {
            Log.e("excep in ","DemoModeAdapter "+e.getMessage() );
        }

        return null ;
    }


    @Override
    public int getCount()
    {
        return 1 ;
    }

    @Override
    public Object getItem( int i ) {
        return  roomModeList.get( i );
    }

    @Override
    public long getItemId( int i )
    {
        return i;
    }



}
