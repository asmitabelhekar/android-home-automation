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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.SwitchBoardActivity;
import com.example.lenovo.activeandroid3.activity.SwitchButtonActivity;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.dbModel.SwitchBoardDbModel;
import com.example.lenovo.activeandroid3.model.Rooms;
import com.example.lenovo.activeandroid3.model.SwitchBoard;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by lenovo on 27/11/17.
 */

public class SwitchBoardActivityAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context ;
    SwitchBoardActivity activity ;
    ArrayList<SwitchBoardDbModel> switchBoardList ;
    int position = -1 ;
    AlertDialog alertDialog ;
    String delete_edit_btn_flag ;

    public SwitchBoardActivityAdapter( Context context,  SwitchBoardActivity switchBoardActivity1 , String delete_edit_btn_flag )
    {
        this.switchBoardList = new ArrayList<SwitchBoardDbModel>();
        this.context = context;
        this.activity = switchBoardActivity1 ;
        this.delete_edit_btn_flag = delete_edit_btn_flag ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType )
    {
        return new SwitchBoardActivityAdapter.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_switch_board , parent , false ));
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, final int position ) {

        if( holder instanceof SwitchBoardActivityAdapter.MyViewHolder )
        {
            final SwitchBoardDbModel singleItem = ( SwitchBoardDbModel ) switchBoardList.get( position ) ;

//            Log.e("Roomid name id", singleItem.getsBRoomId()+" "+singleItem.getsBName() +" " +singleItem.getId()+"" );
//            Log.e("flag is in","SwitchBoardActivityAdapter"+ delete_edit_btn_flag );

            if( delete_edit_btn_flag.equals("1") )
            {
                ( ( SwitchBoardActivityAdapter.MyViewHolder ) holder ).ll_edit_delete_btn.setVisibility( View.GONE );
            }else {
                ( ( SwitchBoardActivityAdapter.MyViewHolder ) holder ).ll_edit_delete_btn.setVisibility( View.VISIBLE );
            }

            ( ( SwitchBoardActivityAdapter.MyViewHolder ) holder ).name.setText( singleItem.getsBName() ) ;

            ((MyViewHolder) holder).iv_edit.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View view ) {

                    updateSwitchBoard( position ) ;
                }
            });


            ( ( MyViewHolder ) holder ).iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View view )
                {
                    deleteRoom( position ) ;
                }
            });

            ( ( SwitchBoardActivityAdapter.MyViewHolder ) holder ).ll_main.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick( View view ) {

                    Intent intent= new Intent( activity , SwitchButtonActivity.class ) ;
                    intent.putExtra("switchBoardId", String.valueOf( singleItem.getId() )  ) ;
                    intent.putExtra("delete_edit_btn_flag", delete_edit_btn_flag  );
                    activity.startActivity( intent ) ;
                    activity.overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_right ) ;
                }
            });
        }
    }

    private void deleteRoom( final int position )
    {
        this.position = position ;

        try
        {
            new SweetAlertDialog(activity , SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("You want to delete this board !")
                    .setCancelText("No,cancel it !")
                    .setConfirmText("Yes,delete it !")
                    .showCancelButton( true )
                    .setConfirmClickListener( new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick( SweetAlertDialog sweetAlertDialog )
                        {

                            SwitchBoardDbModel temp = switchBoardList.get( position) ;
                            Long id = temp.getId() ;

                            SwitchBoard.delete( SwitchBoard.class , id ) ;
                            switchBoardList.remove( position ) ;
                            sweetAlertDialog.cancel() ;

                            Toast.makeText( activity ,"Switch Board Deleted Succesfully", Toast.LENGTH_SHORT ).show( ) ;
                            notifyItemRemoved( position ) ;
                            notifyDataSetChanged() ;

                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick( SweetAlertDialog sDialog ) {
                            sDialog.cancel() ;
                        }
                    })
                    .show();


        }catch ( Exception e )
        {
            Log.e("exe while delete board",e.getMessage() );
        }



//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder( activity ) ;
//        LayoutInflater inflater ;
//        inflater = LayoutInflater.from(activity ) ;
//        final View dialogView = inflater.inflate( R.layout.confirmation_dialog, null ) ;
//        dialogBuilder.setView( dialogView ) ;
//        alertDialog = dialogBuilder.create() ;
//        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme ;
//        alertDialog.show() ;
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
//                SwitchBoardDbModel temp = switchBoardList.get( position) ;
//                Long id = temp.getId() ;
//                alertDialog.cancel();
//
//                SwitchBoard.delete( SwitchBoard.class , id ) ;
//
//                switchBoardList.remove( position ) ;
//
//                Toast.makeText( activity ,"Switch Board Deleted Succesfully",Toast.LENGTH_LONG ).show( ) ;
//                notifyItemRemoved( position ) ;
//                notifyDataSetChanged() ;
//
//            }
//        });

    }

    private void updateSwitchBoard(int position)
    {
//        Log.e("inside","updateSwitchBoard") ;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder( activity, R.style.AppTheme ) ;
        LayoutInflater inflater ;
        inflater = LayoutInflater.from( activity);
        final View dialogView = inflater.inflate(R.layout.add_room , null );
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.show();


        this.position = position ;
        final SwitchBoardDbModel ddd = switchBoardList.get( position ) ;
        final Long id =   ddd.getId() ;
        final SwitchBoard dd =    readSingle( id ) ;

//        Log.e("after 1","updateSwitchBoard") ;

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
        add_flat_staff_title.setText("Update Switch");

        final EditText tv_service_name = (EditText) dialogView.findViewById(R.id.tv_service_name);

        tv_service_name.setText( dd.BoardName ) ;
        tv_service_name.setSelection( tv_service_name.getText().length() ) ;

        Button cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button submit = (Button) dialogView.findViewById(R.id.submit);
        submit.setText("Update");

        final TextInputLayout textLayoutinput_service_name = (TextInputLayout) dialogView.findViewById(R.id.textLayoutinput_service_name);
        textLayoutinput_service_name.setHint("Enter Switch Board Name");

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
                    tv_service_name.setError("Enter Switch Board Name");
                    focusView = tv_service_name;
                    cancel = true;
                }

                if ( cancel ) {
                    focusView.requestFocus() ;
                } else
                {
                    Date currentTime = Calendar.getInstance().getTime() ;
                    dd.BoardName = tv_service_name_text ;
                    dd.BoardCreatedAt = ddd.getsBCreatedAt() ;
                    dd.BoardUpdatedat = currentTime.getTime() ;
                    updateData( dd ) ;

                }

            }
        });

    }

    private SwitchBoard readSingle(Long id)
    {
        return new Select()
                .from(SwitchBoard.class)
                .where("id = ?", id)
                .executeSingle();

    }

    private void updateData( SwitchBoard r )
    {
        alertDialog.cancel();
        Long   id = r.save() ;

        SwitchBoard dd  = readSingle( id ) ;
        switchBoardList.get( position ).setsBName( dd.BoardName );
        switchBoardList.get( position ).setsBCreatedAt( dd.BoardCreatedAt ) ;
        switchBoardList.get( position ).setsBUpdatedAt( dd.BoardUpdatedat );
        notifyItemChanged( position ) ;
        this.position = -1;

    }


    public void addItemMore(ArrayList<SwitchBoardDbModel> lst)
    {
        switchBoardList.clear();
        switchBoardList.addAll( lst ) ;
        notifyDataSetChanged();
    }

    public void addOneMoreItem(SwitchBoard dd)
    {
        switchBoardList.add( 0, new SwitchBoardDbModel( dd.getId() , dd.BoardName ,dd.RoomId ,dd.BoardCreatedAt , dd.BoardUpdatedat ) );
        notifyItemRangeChanged( 0 , switchBoardList.size() ) ;
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

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name ;
        LinearLayout ll_main , ll_edit_delete_btn ;
        public ImageView iv_edit ,iv_delete  ;
        public MyViewHolder( View view )
        {
            super( view );
            name = ( TextView ) view.findViewById( R.id.tv_name );
            iv_edit =(ImageView )view.findViewById( R.id.iv_edit );
            iv_delete = ( ImageView )view.findViewById( R.id.iv_delete );
            ll_main = ( LinearLayout )view.findViewById( R.id.ll_main );
            ll_edit_delete_btn = ( LinearLayout )view.findViewById( R.id.ll_edit_delete_btn );
        }
    }

    @Override
    public int getItemCount() {
        return switchBoardList.size() ;
    }
}
