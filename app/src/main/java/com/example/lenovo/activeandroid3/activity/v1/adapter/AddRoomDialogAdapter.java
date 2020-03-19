package com.example.lenovo.activeandroid3.activity.v1.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.v1.fragment.RoomFragment;
import com.example.lenovo.activeandroid3.activity.v1.fragment.SettingFragment;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.util.Conversions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by akshay on 25/5/18.
 */

public class AddRoomDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    ArrayList<RoomDbModel> roomDbModelList ;
//    RoomFragment roomFragment ;
    SettingFragment settingFragment;
    Context context ;
    AlertDialog  alertDialog ;


//    public AddRoomDialogAdapter(RoomFragment roomFragment , Context context , AlertDialog alertDialog )
//    {
//        roomDbModelList = new ArrayList<>();
//        this.roomFragment =roomFragment ;
//        this.context =context ;
//        this.alertDialog =alertDialog ;
//    }

    public AddRoomDialogAdapter(SettingFragment settingFragment, Context context, AlertDialog alertDialog)
    {
        roomDbModelList = new ArrayList<>();
        this.settingFragment = settingFragment ;
        this.context =context ;
        this.alertDialog =alertDialog ;
    }


    public void setDataToAdapter(ArrayList<RoomDbModel> roomDbModel )
    {

        Log.e("in","setDataToAdapter") ;


        roomDbModelList.addAll( roomDbModel );
        Log.e("size",roomDbModelList.size()+"") ;
        notifyItemRangeChanged( 0,roomDbModelList.size() );
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new AddRoomDialogAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_add_room_dialog, parent, false ) );


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if( holder instanceof AddRoomDialogAdapter.MyViewHolder )
        {
            Log.e("inside","onBindViewHolder") ;
            final RoomDbModel singleItem = (RoomDbModel) roomDbModelList.get( position ) ;

            ((MyViewHolder) holder).tv_add_room_name.setText(singleItem.getrName()  ) ;

            if(  Conversions.checkString( singleItem.getRoomHomePageImage()  ) )
            {
//                Log.e("image is  null","in adapter") ;
                Glide.with(context ).load( R.drawable.home1 ).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //Log.e("after ","set flash screen img");
                            ((MyViewHolder) holder).iv_add_room_image.setBackground( drawable );
                        }
                    }
                });
            }else {
//                Log.e("image is not null","in adapter") ;
                Glide.with(context).load( Integer.parseInt( singleItem.getRoomHomePageImage() )   ).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //Log.e("after ","set flash screen img");
                            ((MyViewHolder) holder).iv_add_room_image.setBackground(drawable);
                        }
                    }
                });
            }

//            Log.e("RoomOnOff" ,singleItem.getrName() +"  "+singleItem.getRoomOnOff() ) ;

            if( singleItem.getRoomOnOff() == 1 )
            {
                Glide.with(context ).load( R.drawable.click ).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //Log.e("after ","set flash screen img");
                            ((MyViewHolder) holder).iv_add_room_checkbox.setBackground( drawable );
                        }
                    }
                });

            }else {

                Glide.with(context ).load( R.drawable.unclick ).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //Log.e("after ","set flash screen img");
                            ((MyViewHolder) holder).iv_add_room_checkbox.setBackground( drawable );
                        }
                    }
                });
            }

            ((MyViewHolder) holder).iv_add_room_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("position : " , position +"") ;
                    addRoomSelectedRoom( position );
                }
            });
        }else {
            Log.e("outside","onBind");
        }
    }

    private void addRoomSelectedRoom(int position)
    {
       RoomDbModel roomDbModel = roomDbModelList.get( position ) ;
        int currentStatusOfRoom = roomDbModel.getRoomOnOff() ;
        if( currentStatusOfRoom == 1 )
        {
            roomDbModel.setRoomOnOff( 0 );
        }else
        {
            roomDbModel.setRoomOnOff( 1 );
        }
        notifyItemChanged( position );
    }

    public void updateRoomDatabase()
    {
        Log.e("inside","updateRoomDatabase method") ;

        ActiveAndroid.beginTransaction() ;
        try {
            for (int i = 0; i < roomDbModelList.size() ; i++ )
            {
                try
                {
                    final RoomDbModel roomDbModel = roomDbModelList.get( i ) ;
                    final Long roomId = roomDbModel.getId() ;
                    final Rooms room = readSingle( roomId ) ;
                    room.Name = roomDbModel.getrName() ;
                    room.RoomHomePageImage = roomDbModel.getRoomHomePageImage() ;
                    room.AddRoomImage= roomDbModel.getAddRommImage() ;
                    room.RoomOnOff = roomDbModel.getRoomOnOff() ;
                    room.CreatedAt = roomDbModel.getrCreatedAt() ;
                    room.Updatedat = roomDbModel.getrUpdatedAt();
                    room.save() ;

//                    roomFragment.
                }catch ( Exception e )
                {
                    Log.e("excep while addRoom",e.getMessage() );
                }
            }
            ActiveAndroid.setTransactionSuccessful() ;
        } finally {
            ActiveAndroid.endTransaction();
//            new RoomFragmentAdapter().roomList.clear() ;

           alertDialog.cancel();
        }
    }

    private Rooms readSingle(Long id)
    {
        return new Select()
                .from(Rooms.class)
                .where("id = ?", id)
                .executeSingle();
    }

    @Override
    public int getItemCount() {
        return roomDbModelList.size() ;
    }

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_add_room_name ;
        public ImageView iv_add_room_image , iv_add_room_checkbox;
        public MyViewHolder(View view)
        {
            super(view);
            iv_add_room_image = ( ImageView)view.findViewById( R.id.iv_add_room_image );
            tv_add_room_name   = (TextView) view.findViewById(R.id.tv_add_room_name);
            iv_add_room_checkbox = (ImageView)view.findViewById(R.id.iv_add_room_checkbox) ;
        }
    }
}
