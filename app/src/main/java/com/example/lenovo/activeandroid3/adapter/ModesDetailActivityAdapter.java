package com.example.lenovo.activeandroid3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.ModesDetailActivity;
import com.example.lenovo.activeandroid3.dbModel.ModesDetailActivityDbModel;
import com.example.lenovo.activeandroid3.model.ModeOnOf;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.model.SwitchBoard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 6/12/17.
 */

public class ModesDetailActivityAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    ArrayList<ModesDetailActivityDbModel> list ;
    Context context ;
    ModesDetailActivity activity ;
    String modeId ;
    public List<ModeOnOf> onList = new ArrayList<>() ;
    ArrayList<String>  arrayButtonId ;

    public ModesDetailActivityAdapter(Context con , ModesDetailActivity modesActivity  , String modeId  , ArrayList<String>  arrayButtonId )
    {
        list = new ArrayList<ModesDetailActivityDbModel>();
        context = con ;
        activity = modesActivity  ;
        this.modeId = modeId ;
        this.arrayButtonId = arrayButtonId ;

//        this.delete_edit_btn_flag = delete_edit_btn_flag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType )
    {
        return new ModesDetailActivityAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_modes_detail, parent , false ));
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, final int position ) {

        if( holder instanceof ModesDetailActivityAdapter.MyViewHolder )
        {
            final ModesDetailActivityDbModel singleItem = (ModesDetailActivityDbModel) list.get( position ) ;


            ( ( MyViewHolder ) holder ).tv_buttonName.setText( singleItem.getRoonName()+"  "+singleItem.getBoardName()+"  "+singleItem.getButtonName() ) ;

            String id =   String.valueOf( singleItem.getButtonId()  )  ;
//            Log.e("ButtonId is ",id ) ;

            boolean isPresent  = arrayButtonId.contains( id ) ;


            if( isPresent )
            {
//                Log.e("ButtonId","present") ;
                ( ( MyViewHolder ) holder ).switch1.setChecked( true ) ;

            }else {
//                Log.e("ButtonId","Not present") ;
                ( ( MyViewHolder ) holder ).switch1.setChecked( false ) ;
            }

            ( ( MyViewHolder ) holder ).switch1.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged( CompoundButton compoundButton , boolean  isChecked ) {

                    addOrRemoveEnter( position , isChecked );
                }
            });

//            ((ModesDetailActivityAdapter.MyViewHolder) holder).switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked )
//                {
//
//                    addOrRemoveEnter( position , isChecked );
//                }
//            });
        }
    }


    private void addOrRemoveEnter( int position ,  boolean isChecked )
    {
        ModesDetailActivityDbModel temp  = list.get(position  ) ;
        if( isChecked )
        {
            Log.e("add","entry") ;

            Date currentTime = Calendar.getInstance().getTime() ;
            ModeOnOf modeOnOf = new ModeOnOf() ;
            modeOnOf.CreatedAt = currentTime.getTime() ;
            modeOnOf.Updatedat = currentTime.getTime() ;
            modeOnOf.ModeId = modeId ;
            modeOnOf.ButtonName = temp.getButtonName() ;
            modeOnOf.ButtonId = String.valueOf( temp.getButtonId() )  ;
            Long  id = modeOnOf.save() ;

//            onList  =  getAll() ;
//            printData( onList ) ;

        }else
        {
            Log.e("remove","entry") ;
            new Delete().from( ModeOnOf.class ).where("ButtonId = ?", temp.getButtonId() ).execute() ;

//            onList  =  getAll() ;
//            printData( onList ) ;
        }
    }

//    private void printData( List<ModeOnOf> onList )
//    {
//        for ( int i = 0 ; i < onList.size() ; i++ )
//        {
//            Log.e("Buttonid is "+onList.get( i ).ButtonId,"buttonName is "+onList.get( i ).ButtonName  );
//        }
//    }

    public void addItemMore( ArrayList<ModesDetailActivityDbModel> lst )
    {
        list.clear() ;
        list.addAll( lst ) ;
        notifyDataSetChanged() ;
    }

    // Read all Data
    private List<ModeOnOf> getAll()
    {
//        Log.e("inside","getAll roomid is"+roomId ) ;


        //  order by id
//        return new Select().from( SwitchBoard.class ).orderBy("id ASC").execute() ;

        // access by id
//        return new Select().from(SwitchBoard.class).where("id = ?", roomId ).execute();


        // access bu RoomId
        return new Select().from(ModeOnOf.class).where("ModeId = ?", modeId ).execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();

    }


//    public void addOneMoreItem( SwitchButton dd )
//    {
//        list.add( 0 , new ModesDetailActivityDbModel( dd.getId() , dd.SwitchButtonName ,dd.SwitchBoardId ,dd.SwitchButtonCreatedAt , dd.SwitchButtonUpdatedat , dd.is_on  ) );
//        notifyItemRangeChanged( 0 , list.size() ) ;
//    }

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        //        Switch switch1 ;
        CheckBox switch1 ;
        TextView  tv_buttonName ;

        public MyViewHolder( View view )
        {
            super( view ) ;

            tv_buttonName = ( TextView ) view.findViewById( R.id.tv_buttonName ) ;
            switch1 = (CheckBox)view.findViewById( R.id.switch1 );
//            switch1 = (Switch)view.findViewById( R.id.switch1 );

        }
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }



}
