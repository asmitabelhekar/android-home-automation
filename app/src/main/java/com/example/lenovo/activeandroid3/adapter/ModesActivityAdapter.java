package com.example.lenovo.activeandroid3.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.ModesActivity;
import com.example.lenovo.activeandroid3.activity.ModesDetailActivity;
import com.example.lenovo.activeandroid3.dbModel.ModesActivityDbModel;
import com.example.lenovo.activeandroid3.model.Mode;
import com.example.lenovo.activeandroid3.model.ModeOnOf;
import com.example.lenovo.activeandroid3.model.SwitchButton;
import com.example.lenovo.activeandroid3.util.MyBounceInterpolator;
import com.google.android.flexbox.FlexboxLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by lenovo on 6/12/17.
 */

public class ModesActivityAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    ArrayList<ModesActivityDbModel> list ;
    List<ModeOnOf> listModeOnOf = new ArrayList<>() ;
    List<SwitchButton> buttonList = new ArrayList<>() ;
    Context context ;
    ModesActivity activity ;
    int position =  -1 ;
    ArrayList<String> arrayButtonId ;
    SharedPreferences sharedPref ;
    String positionOFCheckedItem = "" ;



    public ModesActivityAdapter(Context con , ModesActivity modesActivity   )
    {
        list = new ArrayList<ModesActivityDbModel>();
        context = con ;
        activity = modesActivity  ;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
//        this.delete_edit_btn_flag = delete_edit_btn_flag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType )
    {
        return new ModesActivityAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_modes, parent , false ));
    }

    @Override
    public int getItemCount() {
        return  list.size() ;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position )
    {


        if( holder instanceof ModesActivityAdapter.MyViewHolder )
        {
            final ModesActivityDbModel singleItem = (ModesActivityDbModel) list.get( position ) ;

            Log.e("inside","bind" +singleItem.isOn() +" "+position );
            ( ( ModesActivityAdapter.MyViewHolder) holder).tv_buttonName.setText( singleItem.getModeName() ) ;

            // ((MyViewHolder) holder).switch_mode.setChecked( singleItem.isOn() );

            if( singleItem.isOn() )
            {
                ( ( MyViewHolder ) holder ).flexLayout_modes.setBackgroundColor( ContextCompat.getColor( context, R.color.blue_btn_bg_color ) ) ;
//                root.setBackgroundColor( ContextCompat.getColor(getActivity(), R.color.white));

            }else
            {
                ( ( MyViewHolder ) holder ).flexLayout_modes.setBackgroundColor( ContextCompat.getColor( context, R.color.button_text_color ) ) ;

            }

            ( ( MyViewHolder) holder ).flexLayout_modes.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View view )
                {

                    if( ! singleItem.isOn() )
                    {
                        Intent intent= new Intent( context , ModesDetailActivity.class ) ;
                        intent.putExtra("modeId", String.valueOf( singleItem.getModeId() ) ) ;
                        context.startActivity( intent ) ;
                        activity.overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_right ) ;

                    } else
                    {
                        new SweetAlertDialog( context, SweetAlertDialog.WARNING_TYPE )
                                .setTitleText("Please disable mode ..!")
                                .setContentText( "Please disable "+ singleItem.getModeName()+" "+"mode first to edit it's setting" )
                                .setConfirmClickListener( new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick( SweetAlertDialog sweetAlertDialog) {

                                        sweetAlertDialog.cancel() ;
                                    }
                                })
                                .show() ;
                    }
                }
            });


//            ( ( MyViewHolder ) holder ).switch_mode.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
//            {
//                public void onCheckedChanged( CompoundButton buttonView, boolean isChecked )
//                {
//
//                    updateModeStatus(position, isChecked) ;
//                }
//            } ) ;

            ((MyViewHolder) holder).btn_on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {




                    updateModeStatus( position, true ) ;



                }
            });


            ((MyViewHolder) holder).btn_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {


                    updateModeStatus( position , false ) ;
                }
            });
        }
    }


    private void updateModeStatus( int position , boolean status )
    {

        Log.e("in updateModeStatus" , status+"" ) ;
        this.position = position ;
        final ModesActivityDbModel ddd = list.get( position ) ;

        if( status )
        {
            AllModeOff() ;
            AllButtonOff() ;
            Long updatedId =  selectedModeOn( ddd ) ;
            Mode  updatedId1 =  updateUi( updatedId ) ;
            AllButtonOfSelectedModeOn( updatedId1 ) ;

            List<SwitchButton> modeList = getAllButton() ;
            for( int i = 0 ; i < modeList.size() ; i++ )
            {
                SwitchButton modeObject = modeList.get( i ) ;
                Log.e("button name status", modeObject.SwitchButtonName+"  "+modeObject.is_on+"" ) ;
            }

            notifyItemChanged( position ) ;
            notifyDataSetChanged() ;
            this.position = -1 ;
        }
        else
        {
            Log.e("inside else","status false") ;

            Long updatedId = selectedModeOff( ddd ) ;
            Mode  updatedId1 =  updateUi( updatedId ) ;
            AllButtonOfSelectedModeOff( updatedId1 ) ;

            notifyItemChanged( position ) ;
            notifyDataSetChanged() ;
            this.position = -1 ;

            List<SwitchButton> modeList = getAllButton() ;
            for( int i = 0 ; i < modeList.size() ; i++ )
            {
                SwitchButton modeObject = modeList.get(i) ;
                Log.e("button name status ", modeObject.SwitchButtonName+"  "+modeObject.is_on+"" ) ;
            }

        }




    }

    private void AllButtonOfSelectedModeOn( Mode updatedId1 )
    {
//        Log.e("inside","AllButtonOfSelectedModeOn");
        listModeOnOf = getDataFromModeOnOfTable( updatedId1.getId() ) ;
        createArrayOfButtonId( listModeOnOf ) ;

        buttonList = fetchButtonData() ;

        for (int k = 0 ; k < buttonList.size() ; k++ )
        {
            SwitchButton buttonObject = buttonList.get( k ) ;
//            Log.e("BName BID, ISON",buttonObject.SwitchButtonName+"  "+buttonObject.getId()+" "+buttonObject.is_on ) ;
            if (arrayButtonId.contains( String.valueOf( buttonObject.getId() ) ) ) {
//                Log.e("ArrayId is","Present") ;
                buttonObject.is_on = updatedId1.isOn ;
                Long ii = buttonObject.save() ;
                SwitchButton sb = readSingleButton(ii);
//                Log.e("name id" , sb.SwitchButtonName +"  "+sb.is_on ) ;
            } else {
                buttonObject.is_on = false;
                Long ii = buttonObject.save();
//                Log.e("ArrayId is","Not Present") ;
            }
        }
    }

    private void AllButtonOfSelectedModeOff( Mode updatedId1 )
    {
//        Log.e("inside","AllButtonOfSelectedModeOn");
        listModeOnOf = getDataFromModeOnOfTable( updatedId1.getId() ) ;
        createArrayOfButtonId( listModeOnOf ) ;

        buttonList = fetchButtonData() ;

        for (int k = 0 ; k < buttonList.size() ; k++ )
        {
            SwitchButton buttonObject = buttonList.get( k ) ;
//            Log.e("BName BID, ISON",buttonObject.SwitchButtonName+"  "+buttonObject.getId()+" "+buttonObject.is_on ) ;
            if (arrayButtonId.contains( String.valueOf( buttonObject.getId() ) ) ) {
//                Log.e("ArrayId is","Present") ;
                buttonObject.is_on = updatedId1.isOn ;
                Long ii = buttonObject.save();
                SwitchButton sb = readSingleButton(ii);
//                Log.e("name id" , sb.SwitchButtonName +"  "+sb.is_on ) ;
            } else {
                buttonObject.is_on = false;
                Long ii = buttonObject.save();
//                Log.e("ArrayId is","Not Present") ;
            }
        }
    }

    private Mode updateUi(Long updatedId)
    {
        Mode updatedId1 = readSingle( updatedId ) ;
        list.get( position ).setModeName( updatedId1.ModeName ) ;
        list.get( position ).setModeCreatedAt( updatedId1.CreatedAt ) ;
        list.get( position ).setModeUpdatedAt( updatedId1.Updatedat ) ;
        list.get( position ).setOn( updatedId1.isOn ) ;
        return updatedId1 ;

    }

    private Long selectedModeOn( ModesActivityDbModel ddd )
    {
        final Long id =   ddd.getModeId() ;
        final Mode dd =    readSingle( id ) ;
        dd.ModeName = ddd.getModeName() ;
        Date timestamp = Calendar.getInstance().getTime() ;
        dd.CreatedAt = ddd.getModeCreatedAt() ;
        dd.Updatedat = timestamp.getTime() ;
        dd.isOn = true ;
        Long updatedId   = dd.save() ;
        return  updatedId ;
    }

    private Long selectedModeOff( ModesActivityDbModel ddd )
    {
        final Long id =   ddd.getModeId() ;
        final Mode dd =    readSingle( id ) ;
        dd.ModeName = ddd.getModeName() ;
        Date timestamp = Calendar.getInstance().getTime() ;
        dd.CreatedAt = ddd.getModeCreatedAt() ;
        dd.Updatedat = timestamp.getTime() ;
        dd.isOn = false ;
        Long updatedId   = dd.save() ;
        return  updatedId ;
    }


    private void AllButtonOff()
    {
        List<SwitchButton> switchButtonList =  getAllButton() ;
        for( int i = 0 ; i < switchButtonList.size() ; i++ )
        {
            SwitchButton buttonObject = switchButtonList.get(i) ;
//            Log.e("button status",buttonObject.is_on+"" ) ;
            buttonObject.is_on = false ;
            buttonObject.save() ;
        }

    }

    public List<SwitchButton> getAllButton()
    {
        //  order by id
        return new Select().from(SwitchButton.class).orderBy("id ASC").execute();

        // access by id
//        return new Select().from(SwitchButton.class).where("id = ?", roomId ).execute();


        // access by RoomId
//            return new Select().from(SwitchButton.class).where("SwitchBoardId = ?", switchBoardId ).execute();

        // return data in random order
//        return new Select().from(SwitchButton.class).orderBy("RANDOM()") .execute();

    }

    private void AllModeOff()
    {
        List<Mode> modeList = getAllMode() ;

        for( int i = 0 ; i < modeList.size() ; i++ )
        {
//            ModesActivityDbModel temp  = list.get( 0 ) ;
            list.get( i ).setOn( false ) ;

            Mode modeObject = modeList.get(i) ;
            modeObject.isOn = false ;
            modeObject.save() ;
        }
    }

    private void updateData( Mode r )
    {
        try {
            Log.e("inside","update data") ;
            Long id = r.save() ;

            Mode dd = readSingle( id ) ;
            list.get( position ).setModeName( dd.ModeName ) ;
            list.get( position ).setModeCreatedAt( dd.CreatedAt ) ;
            list.get( position ).setModeUpdatedAt( dd.Updatedat ) ;
            list.get( position ).setOn( dd.isOn ) ;


            listModeOnOf = getDataFromModeOnOfTable( dd.getId() ) ;
            createArrayOfButtonId( listModeOnOf ) ;

            buttonList = fetchButtonData() ;

            for (int k = 0 ; k < buttonList.size() ; k++ )
            {
                SwitchButton buttonObject = buttonList.get( k ) ;
//            Log.e("BName BID, ISON",buttonObject.SwitchButtonName+"  "+buttonObject.getId()+" "+buttonObject.is_on ) ;
                if (arrayButtonId.contains( String.valueOf( buttonObject.getId() ) ) ) {
//                Log.e("ArrayId is","Present") ;
                    buttonObject.is_on = dd.isOn;
                    Long ii = buttonObject.save();
                    SwitchButton sb = readSingleButton(ii);
//                Log.e("name id" , sb.SwitchButtonName +"  "+sb.is_on ) ;
                } else {
                    buttonObject.is_on = false;
                    Long ii = buttonObject.save();
//                Log.e("ArrayId is","Not Present") ;
                }
            }

            notifyItemChanged(position);
            notifyDataSetChanged();
            this.position = -1;

        }catch ( Exception e)
        {
            Log.e("excep inside updatedata",e.getMessage());
        }
    }

    //    // Read all Data
    private List<Mode> getAllMode()
    {
//        Log.e("inside","getAll");

        //  order by id
        return new Select().from(Mode.class).orderBy("id ASC").execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();

    }

    private List<SwitchButton> fetchButtonData( )
    {
        //  order by id
        return new Select().from( SwitchButton.class ).orderBy("id ASC").execute() ;

        // access by id
//        return new Select().from(SwitchButton.class).where("id = ?", roomId ).execute();


        // access by boardId
//        return new Select().from( SwitchButton.class).where("SwitchBoardId = ?", sbId ).execute() ;

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();
    }


    private List<ModeOnOf> getDataFromModeOnOfTable(Long id )
    {
        //  order by id
        return new Select().from(ModeOnOf.class).where("ModeId = ?" , id ).orderBy("id ASC").execute() ;

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();
    }

    private void createArrayOfButtonId(List<ModeOnOf> listModeOnOf)
    {
        arrayButtonId = new ArrayList<>();
//        Log.e("inside","createArrayOfButtonId") ;
        for( int i = 0 ; i< listModeOnOf.size() ; i++ )
        {
            ModeOnOf temp  = listModeOnOf.get( i ) ;
//            Log.e("ModeOnOff button name", temp.ButtonName ) ;
            arrayButtonId.add( temp.ButtonId ) ;

        }
    }




    private Mode readSingle(Long id)
    {
        return new Select().from(Mode.class).where("id = ?", id).executeSingle() ;
    }

    private SwitchButton readSingleButton(Long id)
    {
        return new Select().from(SwitchButton.class).where("id = ?", id).executeSingle() ;
    }

    public void addItemMore( ArrayList<ModesActivityDbModel> lst )
    {
        list.clear() ;
        list.addAll( lst ) ;
        notifyDataSetChanged() ;
    }

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        //        LinearLayout ll_main , ll_edit_delete_btn ;
//        public ImageView iv_edit ,iv_delete  ;
//        Switch switch_mode ;
        TextView tv_roomName , tv_buttonName ;
        FlexboxLayout flexLayout_modes ;
        Button btn_on , btn_off ;

        public MyViewHolder( View view )
        {
            super( view ) ;

//            tv_roomName = ( TextView ) view.findViewById( R.id.tv_roomName ) ;
            tv_buttonName = ( TextView ) view.findViewById( R.id.tv_buttonName ) ;
            flexLayout_modes  = ( FlexboxLayout)view.findViewById( R.id.flexLayout_modes );
//            switch_mode = (Switch)view.findViewById( R.id.switch_mode );
            btn_on = ( Button )view.findViewById( R.id.btn_on );
            btn_off = ( Button )view.findViewById( R.id.btn_off );




//            iv_edit =(ImageView )view.findViewById( R.id.iv_edit ) ;
//            iv_delete = ( ImageView )view.findViewById( R.id.iv_delete ) ;
//            ll_main = ( LinearLayout )view.findViewById( R.id.ll_main ) ;
//            ll_edit_delete_btn = ( LinearLayout )view.findViewById( R.id.ll_edit_delete_btn );
        }
    }




//    private void updateData( Mode r )
//    {
//        try {
//            Log.e("inside","update data") ;
//            Long id = r.save() ;
//
//
//
//            Mode dd = readSingle( id ) ;
//            list.get( position ).setModeName( dd.ModeName ) ;
//            list.get( position ).setModeCreatedAt( dd.CreatedAt ) ;
//            list.get( position ).setModeUpdatedAt( dd.Updatedat ) ;
//            list.get( position ).setOn( dd.isOn ) ;
//
//
//            listModeOnOf = getDataFromModeOnOfTable( dd.getId() ) ;
//            createArrayOfButtonId( listModeOnOf ) ;
//
//            buttonList = fetchButtonData() ;
//
//            for (int k = 0 ; k < buttonList.size() ; k++ )
//            {
//                SwitchButton buttonObject = buttonList.get( k ) ;
////            Log.e("BName BID, ISON",buttonObject.SwitchButtonName+"  "+buttonObject.getId()+" "+buttonObject.is_on ) ;
//                if (arrayButtonId.contains( String.valueOf( buttonObject.getId() ) ) ) {
////                Log.e("ArrayId is","Present") ;
//                    buttonObject.is_on = dd.isOn;
//                    Long ii = buttonObject.save();
//                    SwitchButton sb = readSingleButton(ii);
////                Log.e("name id" , sb.SwitchButtonName +"  "+sb.is_on ) ;
//                } else {
//                    buttonObject.is_on = false;
//                    Long ii = buttonObject.save();
////                Log.e("ArrayId is","Not Present") ;
//                }
//            }
//
//            notifyItemChanged(position);
//            notifyDataSetChanged();
//            this.position = -1;
//
//        }catch ( Exception e)
//        {
//            Log.e("excep inside updatedata",e.getMessage());
//        }
//    }


//    private void updateModeStatusOff(int position, boolean isChecked)
//    {
//        this.position = position ;
//        final ModesActivityDbModel ddd = list.get( position ) ;
//        Log.e("in updateModeStatusOff" , isChecked+"" ) ;
//        Long updatedId = selectedModeOff( ddd ) ;
//        Mode  updatedId1 =  updateUi( updatedId ) ;
//        AllButtonOfSelectedModeOff( updatedId1 );
//        List<SwitchButton> modeList = getAllButton() ;
//        for( int i = 0 ; i < modeList.size() ; i++ )
//        {
//            SwitchButton modeObject = modeList.get(i) ;
//            Log.e("button name status ", modeObject.SwitchButtonName+"  "+modeObject.is_on+"" ) ;
//        }
//
////        notifyItemChanged( position ) ;
//        notifyDataSetChanged() ;
//        this.position = -1 ;
//
//    }



//    textView.setOnClickClickListener(new View.OnClickListener() {
//    private boolean stateChanged;
//    public void onClick(View view) {
//        if(stateChanged) {
//            // reset background to default;
//            textView.setBackgroundDrawable(circleOffDrawable);
//        } else {
//            textView.setBackgroundDrawable(circleOnDrawable);
//        }
//        stateChanged = !stateChanged;
//    }
//});





}
