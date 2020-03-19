package com.example.lenovo.activeandroid3.activity.v1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;

import java.util.ArrayList;

/**
 * Created by akshay on 30/5/18.
 */

class DemoModeAdapterInside extends BaseAdapter
{
    Context context ;
    ArrayList<SwitchButtonDbModel> objectArrayList;
    DemoModeAdapter demoModeAdapter ;
    LayoutInflater inflter;

    LinearLayout linearLayout ;
    View view ;


    public DemoModeAdapterInside(Context context, ArrayList<SwitchButtonDbModel> objectArrayList, DemoModeAdapter demoModeAdapter)
    {
        this.context =context;
        this.objectArrayList = objectArrayList ;
        Log.e("objectArrayList inside",objectArrayList.size()+"") ;
        this.demoModeAdapter = demoModeAdapter ;
        inflter = (LayoutInflater.from( context ) ) ;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent)
    {
        view = inflter.inflate( R.layout.demo_item_mode_inside_adapter, null  );


        TextView tv_btn_name_inside  = (TextView) view.findViewById( R.id.tv_btn_name_inside );
        tv_btn_name_inside.setText( objectArrayList.get( i).getsButtonName() ) ;
        Log.e("Bname bid ", objectArrayList.get( i).getsButtonName() +"  "+objectArrayList.get(i).getId()  );

        return view ;
    }


    @Override
    public int getCount()
    {
        return objectArrayList.size() ;
    }

    @Override
    public Object getItem( int i ) {
        return  objectArrayList.get( i );
    }

    @Override
    public long getItemId( int i )
    {
        return i;
    }




}


