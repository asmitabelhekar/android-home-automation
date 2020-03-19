package com.example.lenovo.activeandroid3.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.AdminMainActivity;
import com.example.lenovo.activeandroid3.activity.MainActivity;
import com.example.lenovo.activeandroid3.activity.SwitchBoardActivity;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.util.Conversions;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by lenovo on 28/11/17.
 */

public class AdminMainActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    ArrayList<RoomDbModel> roomList ;
    int[] image ;
    Context context ;
    AlertDialog   alertDialog ;
    MainActivity activity = null ;
    AdminMainActivity adminActivity = null ;
    boolean  hide_edit_delete_linear_layout ;
    int position = -1 ;
    String delete_edit_btn_flag ;

    //Uri to store the image uri
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 2;
    String image_Path = "" ;
    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100 ;
    ImageView imageview2 ,iv_camera , iv_gallery  ;

    boolean   isClickForImage  = false ;




    public AdminMainActivityAdapter(AdminMainActivity adminActivity , Context context , ArrayList<RoomDbModel> roomNames , int[] image , boolean hide_edit_delete_linear_layout , String delete_edit_btn_flag )
    {
//        Log.e("roomNames size", roomNames.size()+"" );

        this.roomList = new ArrayList<>() ;
        this.adminActivity = adminActivity ;
        this.hide_edit_delete_linear_layout = hide_edit_delete_linear_layout ;
        this.image = image ;
        this.context = context;
        this.delete_edit_btn_flag  = delete_edit_btn_flag ;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType )
    {
        return new AdminMainActivityAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_custom_adapter , parent , false ));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position )
    {
        if( holder instanceof AdminMainActivityAdapter.MyViewHolder )
        {

            final RoomDbModel singleItem = (RoomDbModel) roomList.get( position ) ;

//            Log.e("inside","onBind"+singleItem.getrName()+" "+singleItem.getId() );

//            Log.e("flag is in","AdminMainActivityAdapter"+ delete_edit_btn_flag );

//            ((AdminMainActivityAdapter.MyViewHolder) holder).image.setImageResource( image[ position ] ) ;
            ((AdminMainActivityAdapter.MyViewHolder) holder ).name.setText( singleItem.getrName() ) ;

            if( hide_edit_delete_linear_layout )
            {
                ((AdminMainActivityAdapter.MyViewHolder) holder).ll_edit_delete_btn.setVisibility( View.GONE );

            }else {
                ((AdminMainActivityAdapter.MyViewHolder) holder).ll_edit_delete_btn.setVisibility( View.VISIBLE );
            }

            ((AdminMainActivityAdapter.MyViewHolder) holder).iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    deleteRoom( position ) ;


                }
            });

            if(  Conversions.checkString( singleItem.getRoomHomePageImage() ) )
            {
//                Log.e("image is  null","in adapter") ;
                Glide.with(context).load( R.drawable.home1 ).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //Log.e("after ","set flash screen img");
                            ((AdminMainActivityAdapter.MyViewHolder) holder).iv_room.setBackground(drawable);
                        }
                    }
                });
            }else {

//                Log.e("image is not  null","in adapter") ;

                Glide.with(context).load( singleItem.getRoomHomePageImage() ).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //Log.e("after ","set flash screen img");
                            ((AdminMainActivityAdapter.MyViewHolder) holder).iv_room.setBackground(drawable);
                        }
                    }
                });
            }


            ((MyViewHolder) holder).iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    updateRoom( position );
                }
            });

            ( ( AdminMainActivityAdapter.MyViewHolder ) holder ).ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent( context , SwitchBoardActivity.class ) ;
                    intent.putExtra("roomId", String.valueOf( singleItem.getId() ) ) ;
                    intent.putExtra("delete_edit_btn_flag", delete_edit_btn_flag ) ;
                    context.startActivity( intent ) ;
                    adminActivity.overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_right ) ;

                }
            });
        }
    }

    //    On scrolled
    public void setRecyclerView( RecyclerView mView , final FloatingActionButton floatingActionButton  )
    {
        mView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {

                super.onScrolled(recyclerView, dx, dy);

                // it will gide the fab button when scroll
                if ( dy > 0 )
                    floatingActionButton .hide();
                else if (dy < 0)
                    floatingActionButton.show();

            }
        });
    }



    private void updateRoom( int position )
    {
        try {
            this.position = position ;

            final RoomDbModel ddd = roomList.get( position ) ;
            final Long id = ddd.getId() ;
            final Rooms dd = readSingle( id ) ;
            isClickForImage = false ;


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme) ;
            LayoutInflater inflater ;
            inflater = LayoutInflater.from(context ) ;
            final View dialogView = inflater.inflate(R.layout.add_room_with_image, null);
            dialogBuilder.setView( dialogView ) ;
            alertDialog = dialogBuilder.create() ;
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            alertDialog.show() ;


            ImageView in_back_arrow = (ImageView) dialogView.findViewById( R.id.in_back_arrow ) ;
            in_back_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialog.cancel();
                }
            });

            Glide.with(context).load(R.drawable.back_arrow)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(in_back_arrow);
            TextView add_flat_staff_title = (TextView) dialogView.findViewById(R.id.add_flat_staff_title);
            add_flat_staff_title.setText("Update Room");

            final EditText tv_service_name = (EditText) dialogView.findViewById(R.id.tv_service_name);

            tv_service_name.setText(dd.Name);
            tv_service_name.setSelection(tv_service_name.getText().length());


            iv_camera = ( ImageView ) dialogView.findViewById( R.id.iv_camera ) ;
            iv_gallery = ( ImageView ) dialogView.findViewById( R.id.iv_gallery ) ;
            imageview2 = ( ImageView ) dialogView.findViewById( R.id.imageview2 ) ;
            imageview2.setVisibility( View.VISIBLE );

//        Glide.with( context ).load( R.drawable.camera_icon )
//                .thumbnail(0.5f)
//                .crossFade()
//                .diskCacheStrategy( DiskCacheStrategy.ALL )
//                .into(  iv_camera) ;
//
//        Glide.with( context ).load( R.drawable.gallery_icon )
//                .thumbnail(0.5f)
//                .crossFade()
//                .diskCacheStrategy( DiskCacheStrategy.ALL )
//                .into(  iv_gallery ) ;

            if(  Conversions.checkString( ddd.getRoomHomePageImage()  ) )
            {
                Log.e("image is  null","in adapter") ;
                Glide.with(adminActivity).load( R.drawable.home1 ).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
                        {
                            //Log.e("after ","set flash screen img");
                            imageview2.setBackground( drawable ) ;

                        }
                    }
                });
            }else {

                Log.e("image is not  null","in adapter") ;

                Glide.with(context).load( ddd.getRoomHomePageImage() ).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //Log.e("after ","set flash screen img");
                            imageview2.setBackground( drawable ) ;
                        }
                    }
                });

            }


            iv_camera.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    // Checking camera availability
                    if (!isDeviceSupportCamera()) {
                        Toast.makeText(adminActivity,
                                "Sorry! Your device doesn't support camera",
                                Toast.LENGTH_LONG).show();
                    } else {

                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);
                        adminActivity.startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                    }
                }
            });


            iv_gallery.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Log.e("click","on gallery") ;
                    Intent intent = new Intent() ;
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    adminActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
            });


            Button cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
            Button submit = (Button) dialogView.findViewById(R.id.submit);
            submit.setText("Update");

            final TextInputLayout textLayoutinput_service_name = (TextInputLayout) dialogView.findViewById(R.id.textLayoutinput_service_name);
            textLayoutinput_service_name.setHint("Enter Room Name");

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.cancel();
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    textLayoutinput_service_name.setError(null) ;

                    boolean cancel = false ;
                    View focusView = null ;

                    String tv_service_name_text = tv_service_name.getText().toString().trim();

                    if (TextUtils.isEmpty(tv_service_name_text)) {
                        tv_service_name.setError("Enter Room Name");
                        focusView = tv_service_name;
                        cancel = true ;
                    }

                    if (cancel )
                    {
                        focusView.requestFocus() ;
                    } else
                    {

                        if( isClickForImage )
                        {
                            dd.Name = tv_service_name_text;
                            Date currentTime = Calendar.getInstance().getTime();
                            dd.CreatedAt = ddd.getrCreatedAt();
                            dd.Updatedat = currentTime.getTime();
                            dd.RoomHomePageImage = image_Path;
                            updateData( dd );

                        }else
                        {
                            dd.Name = tv_service_name_text;
                            Date currentTime = Calendar.getInstance().getTime() ;
                            dd.CreatedAt = ddd.getrCreatedAt();
                            dd.Updatedat = currentTime.getTime();
                            dd.RoomHomePageImage = ddd.getRoomHomePageImage() ;
                            updateData(dd);
                        }
                    }
                }
            });

        }catch ( Exception e )
        {
            Log.e("excep in","updateRoom"+e.getMessage() ) ;
            e.printStackTrace();
        }
    }

    private void updateData( Rooms r )
    {
        alertDialog.cancel() ;
        Long   id = r.save() ;
//        adminActivity.recreate();

        Rooms dd  = readSingle( id ) ;
        roomList.get( position ).setrName( dd.Name );
        roomList.get( position ).setrCreatedAt( dd.CreatedAt ) ;
        roomList.get( position ).setrUpdatedAt( dd.Updatedat ) ;
        roomList.get( position ).setRoomHomePageImage( dd.RoomHomePageImage ); ;

        notifyItemChanged(position );
        this.position = -1;

//
//        roomList.set( position, roomList.get( position)).setrName( dd.Name ) ;
//        roomList.set( position, roomList.get( position)).setrCreatedAt(dd.CreatedAt ) ;
//        roomList.set( position, roomList.get( position)).setrUpdatedAt( dd.Updatedat ) ;
//
//
//
//
////        roomList.set( position, new RoomDbModel( dd.getId() , dd.Name ,dd.CreatedAt ,dd.Updatedat ) );
//        notifyItemChanged(position);
//        this.position = -1;

    }

    private void deleteRoom(final int position)
    {
//        Log.e("inside","deleteRoom");
        this.position = position ;
        try
        {

            new SweetAlertDialog(context , SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("You want to delete this room !")
                    .setCancelText("No,cancel it !")
                    .setConfirmText("Yes,delete it !")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog)
                        {
//                            Log.e("delete","this") ;
                            RoomDbModel temp = roomList.get( position) ;
                            Long id = temp.getId() ;
                            Rooms.delete( Rooms.class , id ) ;
                            roomList.remove( position ) ;
                            Toast.makeText( context ,"Room Deleted Succesfully",Toast.LENGTH_SHORT ).show( ) ;
                            sweetAlertDialog.cancel();
                            notifyItemRemoved( position ) ;
                            notifyDataSetChanged() ;

                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();


//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder( context ) ;
//            LayoutInflater inflater ;
//            inflater = LayoutInflater.from(context ) ;
//            final View dialogView = inflater.inflate( R.layout.confirmation_dialog, null ) ;
//            dialogBuilder.setView( dialogView ) ;
//            alertDialog = dialogBuilder.create();
//            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme ;
//            alertDialog.show() ;
//
//            TextView btn_cancel = ( TextView ) dialogView.findViewById( R.id.dialog_cancel ) ;
//            TextView submit = (TextView) dialogView.findViewById( R.id.dialog_ok ) ;
//            btn_cancel.setOnClickListener( new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    alertDialog.cancel();
//
//                }
//            });
//
//            submit.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View view) {
//
//                    RoomDbModel temp = roomList.get( position) ;
//                    Long id = temp.getId() ;
//
//                    Rooms.delete( Rooms.class , id ) ;
//                    alertDialog.cancel();
//                    roomList.remove( position ) ;
//                    Toast.makeText( context ,"Room Deleted Succesfully",Toast.LENGTH_LONG ).show( ) ;
//                    notifyItemRemoved( position ) ;
//                    notifyDataSetChanged() ;
//                }
//            });

        }catch ( Exception e )
        {
            Log.e("excep while delete room", e.getMessage() );
        }

    }

    @Override
    public int getItemCount()
    {
        return roomList.size() ;
    }

    public void addOneMoreItem(Rooms dd)
    {
        roomList.add( 0 , new RoomDbModel( dd.getId() , dd.Name , dd.CreatedAt ,dd.Updatedat , dd.RoomHomePageImage, dd.AddRoomImage , dd.RoomOnOff , dd.AtLeastOneButtonOfRoomIsOn) ) ;
        notifyItemRangeChanged( 0 , roomList.size() ) ;
    }

    public void addItemMore( ArrayList<RoomDbModel> lst )
    {
        roomList.clear() ;
        roomList.addAll( lst ) ;
        notifyDataSetChanged() ;
    }

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name ;
        LinearLayout  ll_edit_delete_btn , ll_main ;
        public ImageView   iv_edit , iv_delete, iv_room  ;
        public MyViewHolder( View view )
        {
            super( view ) ;
            name = ( TextView ) view.findViewById( R.id.name ) ;
            iv_room =(ImageView )view.findViewById( R.id.iv_room ) ;
            iv_edit = ( ImageView )view.findViewById( R.id.iv_edit ) ;
            iv_delete = ( ImageView )view.findViewById( R.id.iv_delete ) ;
            ll_main = ( LinearLayout )view.findViewById( R.id.ll_main ) ;
            ll_edit_delete_btn = ( LinearLayout )view.findViewById( R.id.ll_edit_delete_btn ) ;
        }
    }

    private Rooms readSingle(Long id)
    {
        return new Select()
                .from(Rooms.class)
                .where("id = ?", id)
                .executeSingle();

    }

    private boolean isDeviceSupportCamera() {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public Uri getOutputMediaFileUri(int type) {


        Log.e("checking path", Uri.fromFile(getOutputMediaFile(type)).toString());
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type)
    {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                com.example.lenovo.activeandroid3.activity.Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("check", "Oops! Failed create "
                        +  com.example.lenovo.activeandroid3.activity.Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {

            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");

        } else {
            return null;
        }

        return mediaFile;
    }

    public void launchUploadActivity(boolean isImage)
    {
        try {
            Log.e("inside","launchUploadActivity") ;

//            imageview2.setVisibility(View.VISIBLE);
            Glide.with(context).load( fileUri ).into( imageview2 ) ;
            image_Path = getPath1(fileUri);
        } catch (Exception e) {

            e.printStackTrace();
            Log.e("Error", e.getMessage());
        }
    }

    public String getPath1 (Uri uri)
    {
        try {

            String path;
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path

                path = uri.getPath();
            } else {


                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                }
                String document_id = null;
                if (cursor != null) {
                    document_id = cursor.getString(0);
                }
                if (document_id != null) {
                    document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
                }
                if (cursor != null) {
                    cursor.close();
                }

                cursor = context.getContentResolver().query(
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                }
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                //Log.e(" Gallery path", path);

                cursor.close();
            }
            return path;

        } catch (Exception e) {
            Log.e("A", e.getMessage());
            return null;
        }
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        isClickForImage = true ;
        Log.e("inside","onActivityResult Adapter");
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK) {

                launchUploadActivity( true ) ;
            } else if( resultCode == RESULT_CANCELED ) {

                // user cancelled Image capture
                Toast.makeText( context,
                        "U cancelled image capture", Toast.LENGTH_SHORT)
                        .show() ;
            } else {
                // failed to capture image
                Toast.makeText( context,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show() ;
            }
        }

        // if the result is Gallery Image
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.e("filePath is", filePath.toString() );

            image_Path=getPath1(filePath);
            Log.e("image_Path is", image_Path );

            try
            {

                //imageview2.setVisibility(View.VISIBLE);
                Glide.with(context).load( filePath ).into( imageview2 ) ;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error", e.getMessage());
            }
        }
    }






}
