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

/**
 * Created by anway on 13/8/18.
 */

class EditImageSelectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    RoomInsideDialogAdapter roomInsideDialogAdapter ;
    Context context ;
    AlertDialog alertDialog ;
    int[] buttonOffImagearray ;


    public EditImageSelectorAdapter(RoomInsideDialogAdapter roomInsideDialogAdapter, Context context, AlertDialog alertDialog, int[] buttonOffImagearray)
    {
        this.alertDialog = alertDialog ;
        this.context = context;
        this.roomInsideDialogAdapter = roomInsideDialogAdapter;
        this.buttonOffImagearray = buttonOffImagearray ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new EditImageSelectorAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_v1_edit_image_selector , parent , false ));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        if( holder instanceof EditImageSelectorAdapter.MyViewHolder )
        {
            Log.e("image  value", "onBindViewHolder: "+ buttonOffImagearray[position]+"" );
            Glide.with(context).load( buttonOffImagearray[ position ] ).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ((MyViewHolder) holder).iv_button_image.setBackground(drawable);
                    }
                }
            });

            ((MyViewHolder) holder).ll_main_image_selector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("position is ", position+"" ) ;
                    roomInsideDialogAdapter.selectedImagePosition( position );
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return buttonOffImagearray.length ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_button_image;
        LinearLayout ll_main_image_selector;
        public MyViewHolder( View view )
        {
            super( view ) ;
            iv_button_image = ( ImageView)view.findViewById( R.id.iv_button_image );
            ll_main_image_selector= (LinearLayout)view.findViewById( R.id.ll_main_image_selector );
        }
    }
}
