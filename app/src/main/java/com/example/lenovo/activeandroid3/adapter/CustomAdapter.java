package com.example.lenovo.activeandroid3.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.lenovo.activeandroid3.activity.AdminMainActivity;
import com.example.lenovo.activeandroid3.activity.MainActivity;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.SwitchBoardActivity;
import com.example.lenovo.activeandroid3.activity.TabActivity;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.util.Conversions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lenovo on 27/11/17.
 */

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public   ArrayList<RoomDbModel> roomListCustomAdapter ;
    int[] image ;
    Context context ;
    MainActivity activity = null ;
    AdminMainActivity adminActivity = null ;
    boolean  hide_edit_delete_linear_layout ;
    int position = -1 ;
    String delete_edit_btn_flag ;

    public CustomAdapter(Context context , ArrayList<RoomDbModel> roomNames , int[] image , MainActivity activity , boolean hide_edit_delete_linear_layout , String delete_edit_btn_flag  )
    {
        this.roomListCustomAdapter = roomNames ;
        this.activity = activity ;
        this.image = image ;
        this.delete_edit_btn_flag = delete_edit_btn_flag ;
        this.context = context ;
        this.hide_edit_delete_linear_layout = hide_edit_delete_linear_layout ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType )
    {
        return new CustomAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_custom_adapter , parent , false ));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position )
    {
        if( holder instanceof CustomAdapter.MyViewHolder )
        {

            final RoomDbModel singleItem = (RoomDbModel) roomListCustomAdapter.get( position ) ;

            if(  Conversions.checkString( singleItem.getRoomHomePageImage() ) )
            {
                Log.e("image is  null","in adapter") ;
                Glide.with(context).load( R.drawable.home1 ).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //Log.e("after ","set flash screen img");
                            ((MyViewHolder) holder).iv_room.setBackground(drawable);
                        }
                    }
                });
            }else {

                Log.e("image is not  null","in adapter") ;

                Glide.with(context).load( singleItem.getRoomHomePageImage() ).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //Log.e("after ","set flash screen img");
                            ((MyViewHolder) holder).iv_room.setBackground(drawable);
                        }
                    }
                });

            }
            (( MyViewHolder ) holder ).name.setText( singleItem.getrName() ) ;

            if( hide_edit_delete_linear_layout )
            {
                ((MyViewHolder) holder).ll_edit_delete_btn.setVisibility( View.GONE );

            }else {
                ((MyViewHolder) holder).ll_edit_delete_btn.setVisibility( View.VISIBLE );
            }

            ((MyViewHolder) holder).iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    deleteRoom( position ) ;


                }
            });

            ((MyViewHolder) holder).ll_main.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {

//                    Intent intent= new Intent( context , SwitchBoardActivity.class ) ;
//                    intent.putExtra("roomId", String.valueOf( singleItem.getId() ) );
//                    intent.putExtra("delete_edit_btn_flag", delete_edit_btn_flag );
//                    context.startActivity( intent ) ;
//
                    Intent intent1= new Intent( context , TabActivity.class ) ;
                    intent1.putExtra("roomId", String.valueOf( singleItem.getId() ) );
                    intent1.putExtra("delete_edit_btn_flag", delete_edit_btn_flag );
                    context.startActivity( intent1 ) ;
                    activity.overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_right ) ;

                }
            });
        }
    }

//    //    On scrolled
//    public void setRecyclerView(RecyclerView mView , final FloatingActionButton floatingActionButton  )
//    {
//        mView.addOnScrollListener(new RecyclerView.OnScrollListener()
//        {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
//            {
//
//                super.onScrolled(recyclerView, dx, dy);
//
//                // it will gide the fab button when scroll
//                if ( dy > 0 )
//                    floatingActionButton .hide();
//                else if (dy < 0)
//                    floatingActionButton.show();
//
//            }
//        });
//    }



    public void deleteRoom(int position)
    {
        try
        {
//            this.position = position ;
//
//             RoomDbModel temp = roomList.get( position) ;
//             Long id = temp.getId() ;
//            Log.e("id to be delete","aa");
//
//            Rooms.delete( Rooms.class , id ) ;


            roomListCustomAdapter.remove(this.position);
//            Toast.makeText(context,"Room Deleted Succesfully",Toast.LENGTH_LONG).show();
            notifyItemRemoved(position);
            notifyDataSetChanged();
//            this.position = -1;


        }catch ( Exception e )
        {
            Log.e("excep while delete room", e.getMessage() );
        }
    }

    @Override
    public int getItemCount()
    {
        return roomListCustomAdapter.size() ;
    }

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name ;
        LinearLayout ll_main,   ll_edit_delete_btn ;
        public ImageView  iv_edit , iv_delete , iv_room ;
        public MyViewHolder( View view )
        {
            super( view ) ;
            name = ( TextView ) view.findViewById( R.id.name ) ;
            iv_room =(ImageView )view.findViewById( R.id.iv_room ) ;
            iv_edit = ( ImageView )view.findViewById( R.id.iv_edit ) ;
            iv_delete = ( ImageView )view.findViewById( R.id.iv_delete );
            ll_main = ( LinearLayout )view.findViewById( R.id.ll_main ) ;
            ll_edit_delete_btn = ( LinearLayout )view.findViewById( R.id.ll_edit_delete_btn ) ;
        }
    }

    public static String convertTimeStampToDateString( Long timestamp )
    {
        try {
            Date start_date = new Date( timestamp ) ;
            return DateFormat.format("dd MMM, yyyy", start_date ).toString() ;
        }catch ( Exception e ) {

            return null;
        }
    }

    public static String convertTimeStampToDateForUpdate( Long timestamp )
    {
        try {
            Date start_date = new Date( timestamp );
            return DateFormat.format("dd/MM/yyyy", start_date ).toString();
        }catch ( Exception e ) {

            return null;
        }
    }


}
