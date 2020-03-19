package com.example.lenovo.activeandroid3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.fragments.SingleFragment;
import com.example.lenovo.activeandroid3.model.SwitchButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lenovo on 8/12/17.
 */

public class SingleFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context ;
    SingleFragment singleFragment ;
    ArrayList<SwitchButtonDbModel> switchButtonList ;
    String  delete_edit_btn_flag ;
    int position = -1 ;


    public SingleFragmentAdapter( Context context , SingleFragment singleFragment , String delete_edit_btn_flag  )
    {
        this.context =context ;
        this.singleFragment = singleFragment ;
        switchButtonList = new ArrayList<SwitchButtonDbModel>() ;
        this.delete_edit_btn_flag = delete_edit_btn_flag ;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType )
    {
        return new SingleFragmentAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_switch_button_for_fragment , parent , false ));
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder , final int position )
    {
        if( holder instanceof SingleFragmentAdapter.MyViewHolder )
        {
            final SwitchButtonDbModel singleItem = ( SwitchButtonDbModel ) switchButtonList.get( position ) ;

//            if( delete_edit_btn_flag.equals("1") )
//            {
//                (( SingleFragmentAdapter.MyViewHolder ) holder ).ll_edit_delete_btn.setVisibility( View.GONE ) ;
//                (( SingleFragmentAdapter.MyViewHolder ) holder ).switch1.setVisibility( View.VISIBLE ) ;
//            }else {
//                (( SingleFragmentAdapter.MyViewHolder ) holder ).ll_edit_delete_btn.setVisibility( View.VISIBLE ) ;
//                (( SingleFragmentAdapter.MyViewHolder ) holder ).switch1.setVisibility( View.GONE ) ;
//            }

            boolean status =  singleItem.getOn() ;
            ( ( SingleFragmentAdapter.MyViewHolder ) holder ).switch1.setChecked( status );


            ( ( SingleFragmentAdapter.MyViewHolder ) holder ).name.setText( singleItem.getsButtonName() ) ;


            ( ( SingleFragmentAdapter.MyViewHolder ) holder ).switch1.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged( CompoundButton buttonView, boolean isChecked )
                {

                    updateStatus( position , isChecked ) ;
                }
            });

        }

    }

    private void updateStatus( int position , boolean status )
    {
        Log.e("inside","update status") ;
        this.position = position ;

        final SwitchButtonDbModel ddd = switchButtonList.get( position ) ;
        final Long id =   ddd.getId();
        final SwitchButton dd =    readSingle( id ) ;

//        Log.e("ddd.getsButtonName()",ddd.getsButtonName());
//        Log.e("created at",ddd.getsButtonUpdatedAt()+"");

        dd.SwitchButtonName = ddd.getsButtonName() ;
        Date timestamp = Calendar.getInstance().getTime() ;
        dd.SwitchButtonCreatedAt = ddd.getsButtonCreatedAt() ;
        dd.SwitchButtonUpdatedat = timestamp.getTime()  ;
        dd.is_on = status ;
        dd.SwitchBoardId = ddd.getsBoardId() ;
        updateData( dd ) ;
    }

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
        notifyItemChanged( position ) ;
        notifyDataSetChanged();
        this.position = -1;
    }


    private SwitchButton readSingle(Long id)
    {
        return new Select().from(SwitchButton.class).where("id = ?", id).executeSingle();
    }




    public void addItemMore( ArrayList<SwitchButtonDbModel> lst )
    {
        switchButtonList.clear() ;
        switchButtonList.addAll( lst ) ;
        notifyDataSetChanged() ;
    }

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name ;
        LinearLayout ll_main , ll_edit_delete_btn ;
        public ImageView iv_edit ,iv_delete  ;
        Switch switch1 ;
//        SwitchCompat switch1 ;
        public MyViewHolder( View view )
        {
            super( view ) ;

            name = ( TextView ) view.findViewById( R.id.tv_name ) ;
//            iv_edit =(ImageView )view.findViewById( R.id.iv_edit ) ;
//            iv_delete = ( ImageView )view.findViewById( R.id.iv_delete ) ;
            switch1 = ( Switch )view.findViewById( R.id.switch1 );
//            switch1 = (SwitchCompat)view.findViewById( R.id.switch1 );
            ll_main = ( LinearLayout )view.findViewById( R.id.ll_main ) ;
//            ll_edit_delete_btn = ( LinearLayout )view.findViewById( R.id.ll_edit_delete_btn );
        }
    }

    @Override
    public int getItemCount()
    {
        return switchButtonList.size() ;
    }
}
