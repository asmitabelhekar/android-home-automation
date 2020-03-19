package com.example.lenovo.activeandroid3.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.AddRoomImageActivity;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.model.Rooms;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by lenovo on 11/12/17.
 */

public class AddRoomImageActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context;
    AddRoomImageActivity addRoomImageActivity ;
    private ArrayList<RoomDbModel> list;

    public static final int MEDIA_TYPE_IMAGE = 2 ;
    private int PICK_IMAGE_REQUEST = 1 ;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100 ;
    int STORAGE_PERMISSION_CODE = 123 ;
    //Uri to store the image uri
    private Uri fileUri ;
    String image_Path ;
    private Uri filePath;

    Long created_at , updated_at ;
    String rName ;
    Rooms temp ;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        return new AddRoomImageActivityAdapter.MyViewHolder(LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_select_image_for_room , parent , false ));


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        if( holder instanceof AddRoomImageActivityAdapter.MyViewHolder)
        {

            final RoomDbModel singleItem = ( RoomDbModel ) list.get( position ) ;

            ((MyViewHolder) holder).rName.setText( singleItem.getrName() ) ;

            ((MyViewHolder) holder).iv_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    // Checking camera availability
                    if (!isDeviceSupportCamera())
                    {
                        Toast.makeText(context,
                                "Sorry! Your device doesn't support camera",
                                Toast.LENGTH_LONG).show();
                    } else
                    {

                        getSelectedData( position );






                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUri = getOutputMediaFileUri( MEDIA_TYPE_IMAGE );
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);
                        addRoomImageActivity.startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

//                        Log.e("finally","come here after getting from camera") ;
//                        Log.e("image_path contain",image_Path ) ;
//                        launchUploadActivity() call last here we get final image path in string format

                    }


                }
            });


            ((MyViewHolder) holder).iv_gallery.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    getSelectedData( position );

                    Intent intent = new Intent();
                    intent.setType("image/*") ;
                    intent.setAction( Intent.ACTION_GET_CONTENT ) ;
                    addRoomImageActivity.startActivityForResult( Intent.createChooser( intent, "Select Picture"), PICK_IMAGE_REQUEST );

//                    Log.e("finally","come here after getting from gallery") ;
//                    Log.e("image_path contain",image_Path ) ;

                    // onActivityResult() contain final result in image_Path variale in string format.

                }
            });


        }

    }

    private void getSelectedData(int position)
    {
       RoomDbModel singleItem =  list.get( position );


        created_at =  singleItem.getrCreatedAt() ;
        updated_at = singleItem.getrUpdatedAt() ;
        rName = singleItem.getrName() ;

        temp  =  readSingle( singleItem.getId() ) ;

    }


    //Requesting permission
    private void requestStoragePermission()
    {
        if ( ContextCompat.checkSelfPermission( context, Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED )
            return;

        if( ActivityCompat.shouldShowRequestPermissionRationale( (Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE ) )
        {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions( ( Activity ) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


//    //This method will be called when the user will tap on allow or deny
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        //Checking the request code of our request
//        if (requestCode == STORAGE_PERMISSION_CODE)
//        {
////            Log.e("inside ","onRequestPermissionsResult");
//
//            //If permission is granted
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //Displaying a toast
//                Toast.makeText(context, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
//            } else {
//                //Displaying another toast if permission is not granted
//                Toast.makeText(context, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }


    private boolean isDeviceSupportCamera() {
        if( context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA )  ) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public Uri getOutputMediaFileUri( int type ) {


//        Log.e("checking path", Uri.fromFile(getOutputMediaFile(type)).toString());
        return Uri.fromFile( getOutputMediaFile( type ) );
    }

    private static File getOutputMediaFile(int type )
    {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES ),
                com.example.lenovo.activeandroid3.activity.Config.IMAGE_DIRECTORY_NAME );

        // Create the storage directory if it does not exist
        if ( !mediaStorageDir.exists() ) {
            if ( !mediaStorageDir.mkdirs() ) {
                Log.d("check", "Oops! Failed create "
                        + com.example.lenovo.activeandroid3.activity.Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        File mediaFile;
        if ( type == MEDIA_TYPE_IMAGE ) {

            mediaFile = new File( mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg" );

        } else
        {
            return null;
        }

        return mediaFile;
    }





    public AddRoomImageActivityAdapter( Context context, AddRoomImageActivity addRoomImageActivity )
    {
        this.list = new ArrayList<RoomDbModel>() ;
        this.context = context ;
        this.addRoomImageActivity = addRoomImageActivity ;
        //Requesting storage permission
        requestStoragePermission();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.e("inside","onActivityResult Adapter");
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
        {

            if ( resultCode == RESULT_OK )  {

                launchUploadActivity( true ) ;
            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(context,
                        "U cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(context,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        // if the result is Gallery Image
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.e("filePath is", filePath.toString() );

            image_Path = getPath1(filePath);
            Log.e("image_Path is", image_Path ) ;

            setImageAfterResult( image_Path ) ;

        }

    }

    private void setImageAfterResult( String image_path )
    {
        temp.Name = rName ;
        temp.CreatedAt = created_at ;
        temp.Updatedat = updated_at ;
        temp.RoomHomePageImage = image_path ;
        Long   id = temp.save() ;

        new SweetAlertDialog( context, SweetAlertDialog.SUCCESS_TYPE )
                .setTitleText("Image added for"+ rName + "!" )
                .setContentText("Success...")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.cancel();
                    }
                })
                .show();




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



    public void launchUploadActivity(boolean isImage)
    {
        try {

//            imageview2.setVisibility(View.VISIBLE);
            image_Path = getPath1(fileUri);
            Log.e("image_path contain", image_Path ) ;

            setImageAfterResult( image_Path );



        } catch (Exception e) {

            e.printStackTrace();
            Log.e("Error", e.getMessage());
        }
    }



    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        public TextView rName ;
        public ImageView iv_gallery , iv_camera ;

        public MyViewHolder( View view )
        {
            super( view ) ;
            rName = ( TextView ) view.findViewById( R.id.rName );
            iv_camera = ( ImageView ) view.findViewById( R.id.iv_camera ) ;
            iv_gallery = ( ImageView ) view.findViewById( R.id.iv_gallery ) ;

        }
    }

    //    Add new items in existing arraylist
    public void addItemMore( List<RoomDbModel> lst )
    {
        list.clear() ;
        list.addAll( lst ) ;
        notifyItemRangeChanged( 0,list.size() ) ;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private Rooms readSingle(Long id)
    {
        return new Select()
                .from(Rooms.class)
                .where("id = ?", id)
                .executeSingle();

    }

}
