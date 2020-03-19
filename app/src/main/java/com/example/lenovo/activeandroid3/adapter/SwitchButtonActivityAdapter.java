package com.example.lenovo.activeandroid3.adapter;

import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.SwitchBoardActivity;
import com.example.lenovo.activeandroid3.activity.SwitchButtonActivity;
import com.example.lenovo.activeandroid3.dbModel.SwitchBoardDbModel;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.model.SwitchBoard;
import com.example.lenovo.activeandroid3.model.SwitchButton;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by lenovo on 29/11/17.
 */

public class SwitchButtonActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context ;
    SwitchButtonActivity activity ;
    ArrayList<SwitchButtonDbModel> switchButtonList ;
    int position = -1 ;
    AlertDialog alertDialog ;
    String delete_edit_btn_flag ;

    public SwitchButtonActivityAdapter( Context context , SwitchButtonActivity switchButtonActivity , String delete_edit_btn_flag  )
    {
        switchButtonList = new ArrayList<SwitchButtonDbModel>();
        this.context = context;
        this.activity = switchButtonActivity  ;
        this.delete_edit_btn_flag = delete_edit_btn_flag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType )
    {
        return new SwitchButtonActivityAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_switch_button , parent , false ));
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, final int position ) {

        if( holder instanceof SwitchButtonActivityAdapter.MyViewHolder )
        {
            final SwitchButtonDbModel singleItem = (SwitchButtonDbModel) switchButtonList.get( position ) ;

//            Log.e("flag is in","SwitchButtonActivityAdapter"+ delete_edit_btn_flag );

//           Timestamp ss =  singleItem.getsButtonCreatedAt() ;
//            Log.e("ss",ss+"" );

            if( delete_edit_btn_flag.equals("1"))
            {
                ((MyViewHolder) holder).ll_edit_delete_btn.setVisibility( View.GONE ) ;
                ((MyViewHolder) holder).switch1.setVisibility( View.VISIBLE ) ;
            }else {
                ((MyViewHolder) holder).ll_edit_delete_btn.setVisibility( View.VISIBLE ) ;
                ((MyViewHolder) holder).switch1.setVisibility(View.GONE);
            }

            boolean status =  singleItem.getOn() ;
            ((MyViewHolder) holder).switch1.setChecked( status ) ;


            ((MyViewHolder) holder).switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked )
                {
                    Log.e("inside","change state of button");
//                    updateStatus( position , isChecked ) ;
                }
            });


//            ((MyViewHolder) holder).switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked )
//                {
//                    updateStatus( position , isChecked );
//                }
//            });


            Log.e("BordId name id isOn", singleItem.getsBoardId()+" "+singleItem.getsButtonName() +" " +singleItem.getId()+""+singleItem.getOn() ) ;
            ((SwitchButtonActivityAdapter.MyViewHolder) holder ).name.setText( singleItem.getsButtonName() ) ;

            ((SwitchButtonActivityAdapter.MyViewHolder) holder).iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    updateSwitchBoard( position );
                }
            });

            ((SwitchButtonActivityAdapter.MyViewHolder) holder).iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View view )
                {
                    deleteButton( position ) ;
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


//    private void updateStatus( int position , boolean status )
//    {
//        this.position = position ;
//        Log.e("inside","updateStatus") ;
//
//        final SwitchButtonDbModel ddd = switchButtonList.get( position ) ;
//        final Long id =   ddd.getId() ;
//        final SwitchButton dd =    readSingle( id ) ;
//
////        Log.e("ddd.getsButtonName()",ddd.getsButtonName());
////        Log.e("created at",ddd.getsButtonUpdatedAt()+"");
//
//
//        dd.SwitchButtonName = ddd.getsButtonName() ;
//        Date timestamp = Calendar.getInstance().getTime() ;
//        dd.SwitchButtonCreatedAt = ddd.getsButtonCreatedAt() ;
//        dd.SwitchButtonUpdatedat = timestamp.getTime() ;
//        dd.is_on = status ;
//        dd.SwitchBoardId = ddd.getsBoardId() ;
//        updateData( dd ) ;
//    }

    private void deleteButton( int position )
    {

        Log.e("inside","deleteButton") ;

        try
        {
            this.position = position ;


            new SweetAlertDialog(activity , SweetAlertDialog.WARNING_TYPE )
                    .setTitleText("Are you sure?")
                    .setContentText("You want to delete this button !")
                    .setCancelText("No,cancel it !")
                    .setConfirmText("Yes,delete it !")
                    .showCancelButton( true )
                    .setConfirmClickListener( new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog)
                        {

                            deleteIt( sweetAlertDialog ) ;
//                            SwitchButtonDbModel temp = switchButtonList.get( this.position ) ;
//                            Long id = temp.getId() ;
//                            SwitchButton.delete( SwitchButton.class , id ) ;
//                            switchButtonList.remove( this.position ) ;
//                            sweetAlertDialog.cancel();
//                            Toast.makeText( activity ,"Switch Button Deleted Succesfully",Toast.LENGTH_SHORT ).show( ) ;
//                            notifyItemRemoved( this.position ) ;
//                            notifyDataSetChanged() ;

                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();

        }catch ( Exception e )
        {
            Log.e("exe while delete button",e.getMessage() );
        }

//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder( activity ) ;
//        LayoutInflater inflater ;
//        inflater = LayoutInflater.from(activity ) ;
//        final View dialogView = inflater.inflate( R.layout.confirmation_dialog, null ) ;
//        dialogBuilder.setView( dialogView ) ;
//        alertDialog = dialogBuilder.create() ;
//        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme ;
//        alertDialog.show() ;
////        Log.e("inside","delete") ;
//
//        TextView btn_cancel = ( TextView ) dialogView.findViewById( R.id.dialog_cancel ) ;
//        TextView submit = ( TextView ) dialogView.findViewById( R.id.dialog_ok ) ;
//        btn_cancel.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                alertDialog.cancel() ;
//
//            }
//        });
//
//        submit.setOnClickListener( new View.OnClickListener()
//        {
//            @Override
//            public void onClick( View view ) {
//
//                SwitchButtonDbModel temp = switchButtonList.get( position) ;
//                Long id = temp.getId() ;
//                alertDialog.cancel();
//
//                SwitchButton.delete( SwitchButton.class , id ) ;
//
//                switchButtonList.remove( position ) ;
////            new CustomAdapter().roomListCustomAdapter.remove( this.position );
////            new CustomAdapter().notifyItemRemoved( position ) ;
////            new CustomAdapter().notifyDataSetChanged();
//
//                Toast.makeText( activity ,"Switch Button Deleted Succesfully",Toast.LENGTH_LONG ).show( ) ;
//                notifyItemRemoved( position ) ;
//                notifyDataSetChanged() ;
//
//            }
//        });

    }

    private void deleteIt( SweetAlertDialog sweetAlertDialog )
    {
        Log.e("inside","delete it method") ;
        SwitchButtonDbModel temp = switchButtonList.get( this.position ) ;
        Long id = temp.getId() ;
        SwitchButton.delete( SwitchButton.class , id ) ;
        switchButtonList.remove( this.position ) ;
        sweetAlertDialog.cancel();
        Toast.makeText( activity ,"Switch Button Deleted Succesfully",Toast.LENGTH_SHORT ).show( ) ;
        notifyItemRemoved( this.position ) ;
        notifyDataSetChanged() ;
        this.position = -1 ;
    }


    private void updateSwitchBoard( int position )
    {
        Log.e("inside","updateSwitchBoard") ;
        this.position = position ;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder( activity, R.style.AppTheme ) ;
        LayoutInflater inflater ;
        inflater = LayoutInflater.from( activity);
        final View dialogView = inflater.inflate(R.layout.add_room , null );
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.show();

        final SwitchButtonDbModel ddd = switchButtonList.get( position ) ;
        final Long id =   ddd.getId() ;
        final SwitchButton dd =    readSingle( id ) ;

        ImageView in_back_arrow = (ImageView) dialogView.findViewById(R.id.in_back_arrow);
        in_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.cancel();
            }
        });

        Glide.with( activity ).load( R.drawable.back_arrow )
                .thumbnail( 0.5f )
                .crossFade()
                .diskCacheStrategy( DiskCacheStrategy.ALL )
                .into( in_back_arrow )  ;
        TextView add_flat_staff_title = (TextView) dialogView.findViewById(R.id.add_flat_staff_title);
        add_flat_staff_title.setText("Update Button");

        final EditText tv_service_name = (EditText) dialogView.findViewById(R.id.tv_service_name);

        tv_service_name.setText( dd.SwitchButtonName ) ;
        tv_service_name.setSelection( tv_service_name.getText().length() ) ;

        Button cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button submit = (Button) dialogView.findViewById(R.id.submit);
        submit.setText("Update");

        final TextInputLayout textLayoutinput_service_name = (TextInputLayout) dialogView.findViewById(R.id.textLayoutinput_service_name);
        textLayoutinput_service_name.setHint("Enter Button Name");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textLayoutinput_service_name.setError(null);

                boolean cancel = false;
                View focusView = null;

                String tv_service_name_text = tv_service_name.getText().toString().trim();

                if (TextUtils.isEmpty(tv_service_name_text)) {
                    tv_service_name.setError("Enter Button Name");
                    focusView = tv_service_name;
                    cancel = true;
                }

                if ( cancel ) {
                    focusView.requestFocus() ;
                } else
                {
                    dd.SwitchButtonName = tv_service_name_text ;
                    Date timestamp = Calendar.getInstance().getTime() ;
                    dd.SwitchButtonCreatedAt = ddd.getsButtonCreatedAt() ;
                    dd.SwitchButtonUpdatedat = timestamp.getTime() ;
                    dd.is_on = ddd.getOn() ;
                    dd.SwitchBoardId = ddd.getsBoardId() ;
                    alertDialog.cancel();
                    updateData( dd ) ;
                }
            }
        });
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

    public void addOneMoreItem( SwitchButton dd )
    {
        switchButtonList.add( 0 ,
                new SwitchButtonDbModel( dd.getId() ,
                        dd.SwitchButtonName ,dd.
                        SwitchBoardId ,
                        dd.SwitchButtonCreatedAt ,
                        dd.SwitchButtonUpdatedat ,
                        dd.is_on  ,
                        dd.RoomId,
                        dd.OnImage,
                        dd.OffImage , 1 , 1 // card vr room madhe button kiti position la aahe he samjav mhanun ha flag set kela aahe.pn ethe ha fakt dummy flag aahe.ethe yach use nahi.
                        // Actucally ha flag fakt ModeFragmentAdapter madhech use kela aahe.
                        )
                        );
        notifyItemRangeChanged( 0 , switchButtonList.size() ) ;
    }

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name ;
        LinearLayout ll_main , ll_edit_delete_btn ;
        public ImageView iv_edit ,iv_delete  ;
        CheckBox switch1 ;
        public MyViewHolder( View view )
        {
            super( view ) ;

            name = ( TextView ) view.findViewById( R.id.tv_name ) ;
            iv_edit =(ImageView )view.findViewById( R.id.iv_edit ) ;
            iv_delete = ( ImageView )view.findViewById( R.id.iv_delete ) ;
//            switch1 = ( Switch )view.findViewById( R.id.switch1 );
            switch1 = (CheckBox)view.findViewById( R.id.switch1 );
            ll_main = ( LinearLayout )view.findViewById( R.id.ll_main ) ;
            ll_edit_delete_btn = ( LinearLayout )view.findViewById( R.id.ll_edit_delete_btn );
        }
    }

    @Override
    public int getItemCount() {
        return switchButtonList.size() ;
    }


}
