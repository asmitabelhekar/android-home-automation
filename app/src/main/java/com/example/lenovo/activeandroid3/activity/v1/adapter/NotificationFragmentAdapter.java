package com.example.lenovo.activeandroid3.activity.v1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.v1.fragment.NotificationFragment;
import com.google.android.flexbox.FlexboxLayout;

/**
 * Created by anway on 29/10/18.
 */

public class NotificationFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    String TAG = "RoomFragmentAdapter" ;
    Context context ;
    NotificationFragment myObject ;

    public NotificationFragmentAdapter(Context context, NotificationFragment myObject) {
    this.context=context;
    this.myObject=myObject;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationFragmentAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_notification_fragment_adapter , parent , false ));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if( holder instanceof RoomFragmentAdapter.MyViewHolder )
        {

        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name ;
        LinearLayout ll_main,   ll_edit_delete_btn ;
        public ImageView iv_edit , iv_delete , iv_room ;
        FlexboxLayout single_grid_item ;
        public MyViewHolder( View view )
        {
            super( view ) ;
            name = ( TextView ) view.findViewById( R.id.name ) ;
            iv_room =(ImageView )view.findViewById( R.id.iv_room ) ;
            ll_main = ( LinearLayout)view.findViewById( R.id.ll_main );
            single_grid_item = ( FlexboxLayout )view.findViewById( R.id.single_grid_item ) ;
        }
    }

}
