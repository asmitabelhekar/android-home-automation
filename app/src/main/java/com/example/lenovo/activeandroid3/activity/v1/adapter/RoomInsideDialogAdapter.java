package com.example.lenovo.activeandroid3.activity.v1.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.ResponseInterfaceNew;
import com.example.lenovo.activeandroid3.activity.v1.WrapContentGridLayoutManager;
import com.example.lenovo.activeandroid3.activity.v1.activity.V1MainActivity;
import com.example.lenovo.activeandroid3.activity.v1.asyntask.ButtonNetworkCall;
import com.example.lenovo.activeandroid3.activity.v1.interfaces.ButtonInterface;
import com.example.lenovo.activeandroid3.activity.v1.utils.MethodSelection;
import com.example.lenovo.activeandroid3.activity.v1.utils.ResponseInterface;
import com.example.lenovo.activeandroid3.activity.v1.utils.ThreadUtil;
import com.example.lenovo.activeandroid3.asyntask.CommonAsynTaskNew;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.model.Mode;
import com.example.lenovo.activeandroid3.model.SwitchBoard;
import com.example.lenovo.activeandroid3.model.SwitchButton;
import com.example.lenovo.activeandroid3.util.Constants;
import com.example.lenovo.activeandroid3.util.ItemOffsetDecoration;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

/**
 * Created by akshay on 26/5/18.
 */

public class RoomInsideDialogAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ButtonInterface , ResponseInterfaceNew
{
    RoomFragmentAdapter roomFragmentAdapter ;
    Context context ;
    AlertDialog alertDialog ;
    RoomInsideDialogAdapter myObject = this ;
    public ArrayList<SwitchButtonDbModel>  roomButtonList ;
    String TAG = "RoomInsideDialogAdapter" ;
    int position = -1 ;

    ImageView iv_button_image ;
    int imageValue ;

    int[] buttonOffImagearray = { R.drawable.light , R.drawable.spotlight ,
            R.drawable.lamp ,R.drawable.tube ,R.drawable.fan };
    int roomPosition ;

    Socket   client;
    InputStream inputStream;
    PrintStream    printStream;
    RoomInsideDialogAdapter my_object = this;

    public RoomInsideDialogAdapter(RoomFragmentAdapter roomFragmentAdapter, Context context, AlertDialog alertDialog , int roomPosition )
    {
        Log.e(TAG, "Construtor " );
        this.context = context ;
        this.roomButtonList = new ArrayList<>() ;
        this.roomFragmentAdapter =roomFragmentAdapter ;
        this.alertDialog =alertDialog ;
        this.roomPosition = roomPosition ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType )
    {
        return new RoomInsideDialogAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_room_inside_dialog , parent , false ));
    }

    @Override
    public int getItemCount() {
        return roomButtonList.size() ;
    }

    public void addItem(ArrayList<SwitchButtonDbModel> switchButtonList)
    {
        roomButtonList.clear();
        roomButtonList.addAll( switchButtonList );
        notifyItemRangeChanged(0,roomButtonList.size() ) ;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position )
    {

        if( holder instanceof RoomInsideDialogAdapter.MyViewHolder )
        {
            final SwitchButtonDbModel singleItem = (SwitchButtonDbModel) roomButtonList.get( position ) ;

            ((MyViewHolder) holder).tv_button_name.setText( singleItem.getsButtonName() ) ;

            if( singleItem.getOn() )
            {
                Glide.with(context).load( singleItem.getOnImage() ).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ((MyViewHolder) holder).iv_button_image.setBackground(drawable);
                        }
                    }
                });
            }else {
                Glide.with(context).load( singleItem.getOffImage() ).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ((MyViewHolder) holder).iv_button_image.setBackground(drawable);
                        }
                    }
                });
            }

            ((MyViewHolder) holder).ll_inside.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    changeButtonStatus(position);
                }
            });

            ((MyViewHolder) holder).ll_inside.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    Log.e("inside","on long click listener") ;
                    editButton(position );
                    return true;
                }
            });
        }
    }

    private void editButton(final int position)
    {
        this.position = -1 ;
        this.position = position ;
        final SwitchButtonDbModel switchButtonDbModel = roomButtonList.get(position);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme);
        LayoutInflater inflater;
        inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.v1_edit_button, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog  alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.show();

        ImageView iv_back = (ImageView)dialogView.findViewById(R.id.iv_back );
        TextView tv_title = (TextView)dialogView.findViewById(R.id.tv_title);
        TextView tv_anyText = (TextView)dialogView.findViewById(R.id.tv_anyText);

        tv_title.setText("Edit Button");
        tv_anyText.setText("Save");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {

                alertDialog.cancel();
            }
        });

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.e(TAG, "onCancel: " );
            }
        });
        iv_button_image = ( ImageView)dialogView.findViewById(R.id.iv_button_image );
        final EditText et_button_name = ( EditText )dialogView.findViewById( R.id.et_button_name );

        et_button_name.setText(switchButtonDbModel.getsButtonName() ) ;
        et_button_name.setSelection(et_button_name.getText().length());
        imageValue = switchButtonDbModel.getOffImage();
        Glide.with(context).load(switchButtonDbModel.getOffImage() ).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    iv_button_image.setBackground(drawable);
                }
            }
        });

        RecyclerView rv_eis = (RecyclerView)dialogView.findViewById(R.id.recycler_view_edit_image_selector);
        LinearLayoutManager layout_manager = new LinearLayoutManager( context );
        rv_eis.setLayoutManager(layout_manager);

        rv_eis.setHasFixedSize( true ) ;
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
        rv_eis.addItemDecoration(itemDecoration);
        rv_eis.setLayoutManager(new WrapContentGridLayoutManager(context, 2));

        EditImageSelectorAdapter editImageSelectorAdapter = new EditImageSelectorAdapter( RoomInsideDialogAdapter.this ,context  , alertDialog , buttonOffImagearray );
        rv_eis.setAdapter( editImageSelectorAdapter );

        tv_anyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String newButtonName = et_button_name.getText().toString();
                Log.e(TAG, "onClick imageValue : "+ imageValue+"");
                Log.e(TAG, "onClick newButtonName :  "+newButtonName);

                SwitchButtonDbModel switchButton = roomButtonList.get(position);
                SwitchButton button = readSingleButton(switchButton.getId());

                // update database
                button.is_on = switchButton.getOn();
                button.OffImage = imageValue;
                button.SwitchButtonName = newButtonName;
                button.OnImage = switchButton.getOnImage();
                button.RoomId = switchButton.getRoomId();
                button.SwitchButtonCreatedAt=switchButton.getsButtonCreatedAt();
                button.SwitchButtonUpdatedat = Calendar.getInstance().getTime().getTime() ;
                button.save();

                // update arrayList object
                switchButton.setsButtonName( newButtonName);
                switchButton.setOffImage(imageValue);

                notifyItemChanged( position ) ;

                alertDialog.cancel();

            }
        });

    }

    public void selectedImagePosition(int position)
    {
        Log.e( TAG, "selectedImagePosition: "+buttonOffImagearray[position] +"" );
        imageValue = buttonOffImagearray[position] ;
        Glide.with( context ).load( imageValue ).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                    iv_button_image.setBackground(drawable) ;
                }
            }
        });
    }

    SwitchBoard getBoardById(Long boardId )
    {
        return new Select().from(SwitchBoard.class).where("id = ?", boardId ).executeSingle();
    }



    private void changeButtonStatus(int position)
    {
        this.position = position;
//        Log.e("position" , position+"" );
        SwitchButtonDbModel model = roomButtonList.get(position);
        Log.e("INFO : ButtonId : ", model.getId() + " Button Name: "+model.getsButtonName() +"  Buttom Status: "+model.getOn() +" roomId:"+model.getRoomId()+"  position:"+position);

        String roomId = String.valueOf( model.getRoomId());

        Long boardId = model.getsBoardId();
        SwitchBoard switchBoard = getBoardById(boardId) ;

        // isRoomOpenFlag is true then only update the arrayList object of RoomInsideDialogAdapter class.
        ((V1MainActivity)context).setIsRoomOpenFlag(true) ;


         //  if currentStatusOfButton is true then action is 1
         // bcoz electronics side person consider 1 as OFF  and  0 as ON
        // action : 0 = ON , action : 1 = OFF
        int action =  ( !roomButtonList.get( position ).getOn() ? 0 : 1 );

        if( switchBoard.IP != null )
        {
            Log.e("IP" , switchBoard.IP ) ;
            int new_position= this.position;

            String requestString =  "*ACT," + roomId + "," + ++new_position + "," + action + "#" ;

            CommonAsynTaskNew asynTask = new CommonAsynTaskNew( context,requestString , my_object , MethodSelection.BUTTON_CLICK ,switchBoard.IP);
            if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/)
            {
                asynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                asynTask.execute();
            }
        }else
        {
            Toast.makeText(context, "Please restart App", Toast.LENGTH_SHORT).show();
        }
    }

    private SwitchButton readSingleButton(Long id)
    {
        return new Select()
                .from(SwitchButton.class)
                .where("id = ?", id)
                .executeSingle();
    }


    private List<Mode> getAllMode() {
        // access by id
        return new Select().from(Mode.class).orderBy("id ASC").execute();
    }

    @Override
    public void buttonClickResponse( String respons )
    {
        String TAG2 = "buttonClickResponse" ;
//        Log.e(TAG2 , "response contain : "+ respons  );

        SwitchButtonDbModel switchButtonDbModel = roomButtonList.get( position ) ;

        String finalResponse =  getFinalString( respons , String.valueOf(switchButtonDbModel.getId() ) );
        ((V1MainActivity)context).broadcastToAllMobileDevices( finalResponse ) ;
    }

    private void offMode()
    {
        /**
         *   if any button status is change while mode is on  then first off that mode
         */
        if( roomFragmentAdapter.isModeDataNeedToGet )
        {
//            Log.e("get Mode","Data from DB");
            List<Mode> allMode = getAllMode();
            for( int i=0 ; i< allMode.size() ; i++ )
            {
                Mode mode = allMode.get(i);
                mode.isOn=false;
                mode.save();
            }
            roomFragmentAdapter.isModeDataNeedToGet = false;
        }
    }

    private void updateArrayListAfterResponse()
    {
        SwitchButtonDbModel switchButtonDbModel = roomButtonList.get( position ) ;
        if(  switchButtonDbModel.getOn() )
        {
            switchButtonDbModel.setOn( false ) ;
        }else
        {
            switchButtonDbModel.setOn( true ) ;
        }

        notifyItemChanged( position );
        this.position = -1 ;
    }

    private void updateDbAfterResponse()
    {
        SwitchButtonDbModel switchButtonDbModel = roomButtonList.get( position ) ;

        // update db after response
        Long buttonId = switchButtonDbModel.getId();
        final SwitchButton switchButton = readSingleButton(buttonId  ) ;
        switchButton.OffImage = switchButtonDbModel.getOffImage() ;
        switchButton.OnImage = switchButtonDbModel.getOnImage() ;
        switchButton.RoomId = switchButtonDbModel.getRoomId() ;
        switchButton.SwitchButtonName = switchButtonDbModel.getsButtonName() ;
        switchButton.is_on = switchButtonDbModel.getOn();
        switchButton.SwitchButtonCreatedAt = switchButtonDbModel.getsButtonCreatedAt();
        switchButton.SwitchButtonUpdatedat = switchButtonDbModel.getsButtonUpdatedAt();
        switchButton.SwitchBoardId = switchButtonDbModel.getsBoardId() ;
        switchButton.save();
    }

    private String getFinalString(String respons, String buttonId)
    {
        // final string should be like = "ButtonStatus,1,2,3,4,5"
        String data1 = respons.substring(respons.indexOf(",")+ 1 );
        String roomId = data1.substring(0, data1.indexOf(",")) ;
//        Log.e(TAG, "Data1: "+data1 +", roomId:"+roomId  );
        String data2 = data1.substring(data1.indexOf(",")+ 1 );
        String switchPosition = data2.substring(0, data2.indexOf(",")) ;
//        Log.e(TAG, "Data2: "+data2 +", switchNumber: "+switchPosition  );
        String data3 = data2.substring(data2.indexOf(",")+ 1 );
        String action = data3.substring(0, data3.indexOf("#")) ;
//        Log.e(TAG,  "data3 : "+ data3 +" action: "+action ) ;
        String finalResponse = "ButtonStatus,"+roomId+","+roomPosition+","+buttonId+","+ switchPosition +","+action ;
        Log.e(TAG,  "finalResponse : " +finalResponse ) ;
        return  finalResponse ;
    }

    @Override
    public void getResponse(String response, MethodSelection interface_method, String IP) {

        try {

            Log.e(TAG, "getResponse: "+response );

            switch ( interface_method ) {

                case BUTTON_CLICK :
                    this.buttonClickResponse(response);
                    break;

            }

        }catch (Exception e) {

            Log.e( "in getResponse" , e.getMessage() );
        }



    }

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_button_name ;
        LinearLayout ll_inside  ;
        public ImageView  iv_button_image ;
        public MyViewHolder( View view )
        {
            super( view ) ;
            tv_button_name = ( TextView ) view.findViewById( R.id.tv_button_name ) ;
            iv_button_image =(ImageView )view.findViewById( R.id.iv_button_image ) ;
            ll_inside = ( LinearLayout )view.findViewById( R.id.ll_inside );
        }
    }

    private void testMethodNew()
    {
        String TAG1 = "ThreadTwoForIdAndStatus" ;

                try {
                    // test device ip and port
                    Socket client = new Socket("192.168.4.1", Constants.PORT);
                    client.setSoTimeout(ThreadUtil.timeout );
                    Log.e(TAG1, " Socket created");

                    Scanner scanner = new Scanner(client.getInputStream());
                    Log.e(TAG1, " scanner created ");

                    PrintStream printStream = new PrintStream(client.getOutputStream());
                    Log.e(TAG1, " printStream created ");

//                    printStream.println("*TST#");
//                    Log.e(TAG1, " after printing :  *TST# ");

                    printStream.println("*ID?#") ;
                    Log.e(TAG1, " after printing :  *ID# ");


                    String response = scanner.nextLine();
                    Log.e(TAG1, " TEST Request Response is  " + response);

                    printStream.close();
                    scanner.close();
                    client.close();
                } catch (Exception e) {
                    Log.e(TAG1, "exception in testMethod: " + e.getMessage());
                }
    }

}
