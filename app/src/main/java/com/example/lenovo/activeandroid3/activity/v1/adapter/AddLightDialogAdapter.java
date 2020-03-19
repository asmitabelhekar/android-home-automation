package com.example.lenovo.activeandroid3.activity.v1.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.model.SwitchButton;

import java.util.ArrayList;

/**
 * Created by akshay on 27/5/18.
 */

public class AddLightDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context ;
    RoomFragmentAdapter roomFragmentAdapter ;
    AlertDialog alertDialog_add_light ;
    ArrayList<SwitchButtonDbModel>  aLDAButtonList ;

    public AddLightDialogAdapter(RoomFragmentAdapter roomFragmentAdapter, Context context, AlertDialog alertDialog_add_light)
    {
        this.context = context ;
        this.aLDAButtonList = new ArrayList<>() ;
        this.roomFragmentAdapter =roomFragmentAdapter ;
        this.alertDialog_add_light = alertDialog_add_light ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new AddLightDialogAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_add_light, parent , false ));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        if( holder instanceof AddLightDialogAdapter.MyViewHolder )
        {
            final SwitchButtonDbModel singleItem = (SwitchButtonDbModel) aLDAButtonList.get(position);

            ((AddLightDialogAdapter.MyViewHolder) holder).tv_button_name.setText( singleItem.getsButtonName() ) ;

            Glide.with(context).load( singleItem.getOffImage() ).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ((AddLightDialogAdapter.MyViewHolder) holder).iv_button_image.setBackground(drawable);
                    }
                }
            });

            Glide.with(context).load( R.drawable.delete_new ).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ((MyViewHolder) holder).iv_delete_button.setBackground(drawable);
                    }
                }
            });

            ((MyViewHolder) holder).iv_delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    deleteButton( position );
                }
            });
        }
    }

    private void deleteButton( int position )
    {

        Log.e("click on","delete button position : "+position ) ;
        SwitchButtonDbModel switchButtonDbModel =  aLDAButtonList.get( position ) ;
        Log.e("BName",switchButtonDbModel.getsButtonName() );
        roomFragmentAdapter.isButtonAddedOrDeletedFlag = true ;

        aLDAButtonList.remove( position ) ;

        SwitchButton.delete(SwitchButton.class ,switchButtonDbModel.getId() ) ;

        notifyDataSetChanged() ;
    }

    public void addItem(ArrayList<SwitchButtonDbModel> switchButtonList)
    {
        aLDAButtonList.addAll( switchButtonList );
        notifyItemRangeChanged(0,aLDAButtonList.size() ) ;
    }

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_button_name ;
        LinearLayout ll_inside  ;
        public ImageView iv_button_image  ,iv_delete_button ;
        public MyViewHolder( View view )
        {
            super( view ) ;
            tv_button_name = ( TextView ) view.findViewById( R.id.tv_button_name ) ;
            iv_button_image =(ImageView )view.findViewById( R.id.iv_button_image ) ;
            iv_delete_button =(ImageView )view.findViewById( R.id.iv_delete_button ) ;
            ll_inside = ( LinearLayout )view.findViewById( R.id.ll_inside );
        }
    }


    @Override
    public int getItemCount() {
        return aLDAButtonList.size();
    }
}
