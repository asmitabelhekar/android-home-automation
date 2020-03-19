package com.example.lenovo.activeandroid3.activity.v1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.v1.fragment.SettingFragment;

import java.util.ArrayList;

/**
 * Created by anway on 30/7/18.
 */

public class ShowWifiDeviceListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    String TAG = "ShowWDLAdpter" ;
    SettingFragment settingFragment ;
    Context context ;
    ArrayList<String> listSSID ;

    public ShowWifiDeviceListAdpter( SettingFragment settingFragment, Context context, ArrayList<String> listSSID)
    {
        this.settingFragment = settingFragment ;
        this.context = context ;
        this.listSSID =listSSID ;
//        Log.e("in","constructor") ;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ShowWifiDeviceListAdpter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_dialog_show_wifi_list , parent , false ));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

//        if( holder instanceof ShowWifiDeviceListAdpter.MyViewHolder )
//        {
//            Log.e("in","onBindViewHolder") ;

            String ssid =  listSSID.get( position ) ;

            ((MyViewHolder) holder).tv_wifi_name.setText( ssid ) ;
            ((MyViewHolder) holder).ll_wifi_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((MyViewHolder) holder).iv_click_unclick_device.setBackgroundResource( R.drawable.click) ;
                    String selectedDeviceName  =  listSSID.get( position ) ;
//                    Log.e("selectedDeviceName", selectedDeviceName ) ;
                    settingFragment.seletedDeviceName( selectedDeviceName );

                }
            });
//        Log.e("ssid in binder : ",ssid) ;
//        }
    }

    @Override
    public int getItemCount() {
         return listSSID.size() ;
    }

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_wifi_name ;
        public LinearLayout ll_wifi_name ;
        public ImageView iv_click_unclick_device;
        public MyViewHolder( View view )
        {
            super( view ) ;
            tv_wifi_name = ( TextView ) view.findViewById( R.id.tv_wifi_name ) ;
            ll_wifi_name = (LinearLayout)view.findViewById( R.id.ll_wifi_name );
            iv_click_unclick_device = ( ImageView )view.findViewById( R.id.iv_click_unclick_device );
        }
    }
}
