package com.example.lenovo.activeandroid3.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.fragments.SingleFragment;
import com.example.lenovo.activeandroid3.model.SwitchButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lenovo on 20/12/17.
 */

public class SingleFragmentListAdapter extends BaseAdapter
{
    Context context ;
    SingleFragment singleFragment ;
    ArrayList<SwitchButtonDbModel> switchButtonList ;
    String delete_edit_btn_flag ;
    LayoutInflater inflater ;
    int position ;
    String TAG2 = "SingleFragmentListAdapter" ;
    String statusString = "" ;


    public SingleFragmentListAdapter( Context context , SingleFragment singleFragment , String delete_edit_btn_flag  )
    {
        this.context =context ;
        this.singleFragment = singleFragment ;
        switchButtonList = new ArrayList<SwitchButtonDbModel>() ;
        this.delete_edit_btn_flag = delete_edit_btn_flag ;
        this.inflater = LayoutInflater.from(context);

    }

    public void addItemMore( ArrayList<SwitchButtonDbModel> lst )
    {
        switchButtonList.clear() ;
        switchButtonList.addAll( lst ) ;
        notifyDataSetChanged() ;
    }


    @Override
    public int getCount()
    {
//        Log.e("switchButtonList ", "size"+ switchButtonList.size() );
        return switchButtonList.size() ;
    }

    @Override
    public Object getItem(int i) {
        return switchButtonList.get( i );
    }

    @Override
    public long getItemId(int i ) {
        return  i ;
    }

    @Override
    public View getView(final int i, View view , ViewGroup viewGroup )
    {
        view = inflater.inflate( R.layout.item_switch_button_for_fragment , null ) ;
        TextView   name = ( TextView ) view.findViewById( R.id.tv_name ) ;
        Switch switch1 = (Switch)view.findViewById( R.id.switch1 );
        LinearLayout  ll_main = (LinearLayout)view.findViewById( R.id.ll_main ) ;


        name.setText( switchButtonList.get( i ).getsButtonName() ) ;
        switch1.setChecked( switchButtonList.get(i ).getOn() ) ;

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked ) {

                updateStatus( i , isChecked ) ;
            }
        });

        return view ;
    }

    private void updateStatus(int position , boolean status )
    {
        final String TAG2 = "updateStatus" ;
        Log.e("inside","update status") ;
        this.position = position ;



        final SwitchButtonDbModel ddd = switchButtonList.get( position ) ;
        final Long id =   ddd.getId() ;
        final SwitchButton dd =    readSingle( id ) ;

        dd.SwitchButtonName = ddd.getsButtonName() ;
        Date timestamp = Calendar.getInstance().getTime() ;
        dd.SwitchButtonCreatedAt = ddd.getsButtonCreatedAt() ;
        dd.SwitchButtonUpdatedat = timestamp.getTime()  ;
        dd.is_on = status ;
        dd.SwitchBoardId = ddd.getsBoardId() ;


        sendDataToWifi( ddd.getsButtonName() , status ) ;


        updateData( dd ) ;

    }

    private void sendDataToWifi(final String btnName , final boolean status   )
    {
        final String TAG2 = "sendDataToWifi" ;

        new Thread(new Runnable() {

            @Override
            public void run()
            {
//                try {
//                    Log.e("inside" ,"run method" ) ;
//                    Log.e("before" ,"socket create" ) ;
//                    Socket  client = new Socket("192.168.1.101", 8888) ;
//                    Log.e("after socket" ,"Created" ) ;
//
//
//                    if( status )
//                    {
//                        statusString  = "ON" ;
//                    }else {
//                        statusString  = "OFF" ;
//                    }
//                    Log.e("client" ,client.toString() ) ;
//                    OutputStreamWriter printwriter = new OutputStreamWriter( client
//                            .getOutputStream(), "ISO-8859-1") ;
//                    printwriter.write( "You "+statusString +" "+ btnName ) ;
//
////                    Toast.makeText(context, printwriter.toString(), Toast.LENGTH_SHORT).show();
//
//                    Log.e("printWriter" ,printwriter.toString() ) ;
//                    printwriter.flush() ;
//                    printwriter.close() ;
//                    client.close() ;
//                }
//                catch ( UnknownHostException e )
//                {
//                    Log.e( TAG2 ,"UnknownHostException "+e.getMessage() ) ;
//                    e.printStackTrace();
//                } catch (IOException e)
//                {
//                    Log.e( TAG2 ,"IOException "+e.getMessage() ) ;
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }


                try {
                    Log.e( "before" ,"socket creation" ) ;
                    Socket s = new Socket("192.168.4.1",8888);
                    Log.e( "after" ,"socket creation" ) ;

                    OutputStreamWriter printwriter = new OutputStreamWriter( s
                            .getOutputStream(), "ISO-8859-1") ;
//                    printwriter.write( "You "+statusString +" "+ btnName ) ;
                    printwriter.write("*ACT,0,3,0#");

//                    if( status )
//                    {
//                        statusString  = "ON" ;
//                    }else {
//                        statusString  = "OFF" ;
//                    }

                    Log.e("printWriter" ,printwriter.toString() ) ;
                    printwriter.flush() ;
                    printwriter.close() ;
                    s.close() ;
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

    }

//    private void sendDataToWifi(final String btnName , final boolean status   )
//    {
//        final String TAG2 = "sendDataToWifi" ;
//
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                try {
//                    Log.e("inside" ,"run method" ) ;
//                    Socket  client = new Socket("192.168.4.1", Integer.parseInt("8888") ) ;
//
//                    if( status )
//                    {
//                         statusString  = "*ACT,0,3,0#" ;
//                    }else {
//                        statusString  = "*ACT,0,3,1#" ;
//                    }
//                    Log.e("client" ,client.toString() ) ;
//                    OutputStreamWriter printwriter = new OutputStreamWriter( client
//                            .getOutputStream(), "ISO-8859-1") ;
////                    printwriter.write( "You "+statusString +" "+ btnName ) ;
//                    printwriter.write( statusString  ) ;
//
//                    Toast.makeText(context, printwriter.toString(), Toast.LENGTH_SHORT).show();
//
////                    Toast(this,printwriter.toString(),Toast.LENGTH_SHORT);
//
//                    Log.e("printWriter" ,printwriter.toString() ) ;
//                    printwriter.flush() ;
//                    printwriter.close() ;
//                    client.close() ;
//                }
//                catch ( UnknownHostException e )
//                {
//                    Log.e( TAG2 ,"UnknownHostException "+e.getMessage() ) ;
//                    e.printStackTrace();
//                } catch (IOException e)
//                {
//                    Log.e( TAG2 ,"IOException "+e.getMessage() ) ;
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }


    private void updateData( SwitchButton r )
    {
        Log.e("inside","updateData") ;
        Long   id = r.save() ;
        SwitchButton dd  = readSingle( id ) ;
        Log.e("After Update  ", "Button name id isOn"+dd.SwitchButtonName +" " +dd.getId()+""+dd.is_on ) ;

        switchButtonList.get( position ).setsButtonName( dd.SwitchButtonName ) ;
        switchButtonList.get( position ).setsButtonCreatedAt( dd.SwitchButtonCreatedAt ) ;
        switchButtonList.get( position ).setsButtonUpdatedAt( dd.SwitchButtonUpdatedat ) ;
        switchButtonList.get( position ).setOn( dd.is_on);
        switchButtonList.get( position ).setsBoardId( dd.SwitchBoardId );
//        notifyItemChanged( position ) ;
        notifyDataSetChanged();
        this.position = -1;
    }


    private SwitchButton readSingle( Long id )
    {
        return new Select().from(SwitchButton.class).where("id = ?", id ).executeSingle();
    }
}
