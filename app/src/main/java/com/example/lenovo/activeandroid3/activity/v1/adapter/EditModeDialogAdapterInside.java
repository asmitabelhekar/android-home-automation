package com.example.lenovo.activeandroid3.activity.v1.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.dbModel.ModesDetailActivityDbModel;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.model.ModeOnOf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by akshay on 28/5/18.
 */

public  class EditModeDialogAdapterInside extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context ;
    ArrayList<SwitchButtonDbModel> objectArrayList ;
    EditModeDialogAdapter editModeDialogAdapter ;

    public EditModeDialogAdapterInside( Context context, ArrayList<SwitchButtonDbModel> objectArrayList, EditModeDialogAdapter editModeDialogAdapter )
    {
        this.context = context ;
        this.objectArrayList = objectArrayList ;
//        Log.e("object size insideAdap",objectArrayList.size()+"" ) ;
        this.editModeDialogAdapter = editModeDialogAdapter ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        return new EditModeDialogAdapterInside.MyViewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_edit_mode_inside, parent , false ));
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position )
    {
        if( holder instanceof EditModeDialogAdapterInside.MyViewHolder )
        {
            try
            {
                final SwitchButtonDbModel singleItem = ( SwitchButtonDbModel ) objectArrayList.get( position );

                ( ( EditModeDialogAdapterInside.MyViewHolder ) holder ).tv_btn_name_inside.setText( singleItem.getsButtonName() );

                Glide.with(context).load(  singleItem.getOffImage() ).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //Log.e("after ","set flash screen img");
                            ((MyViewHolder) holder).iv_add_room_image.setBackground(drawable);
                        }
                    }
                });

                String buttonId =  String.valueOf( singleItem.getId() ) ;
                boolean  isButtonIdContainInModeOnOf =   editModeDialogAdapter.arrayButtonId.contains( buttonId  ) ;
                Log.e("isButtonIdContainInMode",isButtonIdContainInModeOnOf+"") ;

                if( isButtonIdContainInModeOnOf )
                {
                    Glide.with(context).load(  R.drawable.click ).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                //Log.e("after ","set flash screen img");
                                ((EditModeDialogAdapterInside.MyViewHolder) holder).iv_edit_mode_inside_click.setBackground(drawable);
                            }
                        }
                    });
                }else {

                    Glide.with(context).load(  R.drawable.unclick ).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                //Log.e("after ","set flash screen img");
                                ((EditModeDialogAdapterInside.MyViewHolder) holder).iv_edit_mode_inside_click.setBackground(drawable);
                            }
                        }
                    });
                }

                ((MyViewHolder) holder).ll_inside.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        SwitchButtonDbModel singleItem = (SwitchButtonDbModel) objectArrayList.get(position);
                        Log.e("click on Button ", singleItem.getsButtonName());
                        Log.e("position:", position+"  "+" room position  "+singleItem.getRoomPositionWithButton()+"  buttonPosition "+singleItem.getButtonPositionInsideRoom()) ;

                        if( editModeDialogAdapter.arrayButtonId.contains( String.valueOf( singleItem.getId() ) ) )
                        {
                            new Delete().from( ModeOnOf.class ).where("ButtonId = ?", String.valueOf( singleItem.getId() )  ).execute() ;
                            editModeDialogAdapter.arrayButtonId.remove( String.valueOf( singleItem.getId() ) );

                            singleItem.setOn( false ) ;
                            editModeDialogAdapter.changeOnOffFlagOfButton( position  , false  , singleItem.getRoomPositionWithButton(), singleItem.getButtonPositionInsideRoom() ) ;


                            Glide.with(context).load(R.drawable.unclick).asBitmap().into(new SimpleTarget<Bitmap>()
                            {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
                                {
                                    Drawable drawable = new BitmapDrawable(resource);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        ((EditModeDialogAdapterInside.MyViewHolder) holder).iv_edit_mode_inside_click.setBackground( drawable ) ;
                                    }
                                }
                            });
                        }else
                        {
                            Date currentTime = Calendar.getInstance().getTime() ;
                            ModeOnOf modeOnOf = new ModeOnOf() ;
                            modeOnOf.CreatedAt = currentTime.getTime() ;
                            modeOnOf.Updatedat = currentTime.getTime() ;
                            modeOnOf.ModeId =  String.valueOf( editModeDialogAdapter.modeId );
                            modeOnOf.ButtonName = singleItem.getsButtonName()  ;
                            modeOnOf.ButtonId = String.valueOf( singleItem.getId()  )  ;
                            Long  id = modeOnOf.save() ;

                            editModeDialogAdapter.arrayButtonId.add( String.valueOf( singleItem.getId() )) ;

                            singleItem.setOn( true ) ;
                            editModeDialogAdapter.changeOnOffFlagOfButton( position  , true  , singleItem.getRoomPositionWithButton()  , singleItem.getButtonPositionInsideRoom() ) ;

                            Glide.with(context).load(R.drawable.click).asBitmap().into( new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    Drawable drawable = new BitmapDrawable(resource);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        //Log.e("after ","set flash screen img");
                                        ((EditModeDialogAdapterInside.MyViewHolder) holder).iv_edit_mode_inside_click.setBackground(drawable);
                                    }
                                }
                            });
                        }
                    }
                });
            }catch (Exception e )
            {
                //   Log.e("excep in","EditModeDialogAdapterInside "+e.getMessage() );
            }
        }
    }

//    private void addOrRemoveEnter( int position ,  boolean isChecked )
//    {
//        ModesDetailActivityDbModel temp  = list.get(position  ) ;
//        if( isChecked )
//        {
//            Log.e("add","entry") ;
//
//            Date currentTime = Calendar.getInstance().getTime() ;
//            ModeOnOf modeOnOf = new ModeOnOf() ;
//            modeOnOf.CreatedAt = currentTime.getTime() ;
//            modeOnOf.Updatedat = currentTime.getTime() ;
//            modeOnOf.ModeId = modeId ;
//            modeOnOf.ButtonName = temp.getButtonName() ;
//            modeOnOf.ButtonId = String.valueOf( temp.getButtonId() )  ;
//            Long  id = modeOnOf.save() ;
//        }else
//        {
//            Log.e("remove","entry") ;
//            new Delete().from( ModeOnOf.class ).where("ButtonId = ?", temp.getButtonId() ).execute() ;
//
////            onList  =  getAll() ;
////            printData( onList ) ;
//        }
//    }

    //    Classes
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_btn_name_inside ;
        public ImageView iv_add_room_image , iv_edit_mode_inside_click  ;
        public LinearLayout ll_inside ;
        public MyViewHolder( View view )
        {
            super( view ) ;
            tv_btn_name_inside = ( TextView ) view.findViewById( R.id.tv_btn_name_inside ) ;
            iv_add_room_image = (ImageView)view.findViewById( R.id.iv_add_room_image );
            iv_edit_mode_inside_click = ( ImageView )view.findViewById( R.id.iv_edit_mode_inside_click );
            ll_inside = ( LinearLayout )view.findViewById( R.id.ll_inside );
        }
    }



    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }
}

//==============================================================================================================

//class EditModeDialogAdapterInside extends BaseAdapter


//{
//    Context context ;
//    ArrayList<SwitchButtonDbModel> objectArrayList;
//    EditModeDialogAdapter editModeDialogAdapter ;
//    LayoutInflater inflter;
//
//    LinearLayout linearLayout ;
//
//
//    // radio adapter
//    LinearLayout  listmainLayout ;
//    RadioGroup rg ;
//    RadioButton[] rb ;
//    CircleImageView[]  circleImageViewArray ;
//
//

//    public EditModeDialogAdapterInside(Context context, ArrayList<SwitchButtonDbModel> objectArrayList, EditModeDialogAdapter editModeDialogAdapter)
//    {
//        this.context =context;
//        this.objectArrayList = objectArrayList ;
//        Log.e("object size insideAdap",objectArrayList.size()+"" );
//        this.editModeDialogAdapter = editModeDialogAdapter ;
//        inflter = (LayoutInflater.from( context ) ) ;
//    }
//
//
//    @Override
//    public View getView(final int i , View view , ViewGroup viewGroup )
//    {
//
//        Log.e("inside listview ","adapter" ) ;
//
//
////        view = inflter.inflate( R.layout.item_edit_mode_inside, null  );
////        listmainLayout = ( LinearLayout ) view.findViewById( R.id.ll );
//
//
////        TextView tv_btn_name_inside  = (TextView) view.findViewById( R.id.tv_btn_name_inside );
////        tv_btn_name_inside.setText( objectArrayList.get( i).getsButtonName() ) ;
////        Log.e("Bname ", objectArrayList.get( i).getsButtonName() +"   "+ objectArrayList.get(i).getRoomId() );
//
////        tv_btn_name_inside.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Log.e("click on" ,objectArrayList.get( i ).getsButtonName() + "  "+ objectArrayList.get(i).getId() );
////            }
////        });
////        return view ;
//
//
//        view = inflter.inflate( R.layout.item_radio_button_adapter, null  );
//        listmainLayout = ( LinearLayout ) view.findViewById( R.id.ll ) ;
//
//        final SwitchButtonDbModel singleItem = ( SwitchButtonDbModel ) objectArrayList.get( i ) ;
//        createRadioButton( i , view ) ;
//
//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int id = linearLayout.getId() ;
//                Log.e("id : ", id+"" ) ;
//            }
//        });
//                return view;
//    }
//
//    private void createRadioButton(int position , View view )
//    {
//        linearLayout = new LinearLayout(context);
//        linearLayout.setOrientation( LinearLayout.VERTICAL ); //or RadioGroup.VERTICAL
//        linearLayout.setPadding(10,6,0,0 );
//
//        for( int i = 0 ; i < objectArrayList.size(); i++ )
//        {
//            LinearLayout linear = new LinearLayout( context );
//            linear.setOrientation( LinearLayout.HORIZONTAL );
//
//            SwitchButtonDbModel singleItem  =  objectArrayList.get(i ) ;
//
//            linear.setId( singleItem.getId().intValue() );
//
////            Log.e("set id : ", singleItem.getId().intValue() +"" ) ;
////            Log.e("get id : ",linearLayout.getId()+"" ) ;
////            Log.e("get id : ", ((SwitchButtonDbModel)linearLayout.getTag()).getId()+"" ) ;
//            final CircleImageView   circleImageView = new CircleImageView( context ) ;
////            Log.e("in for","1") ;
//            circleImageView.setPadding(5,10,0,0 ) ;
////            Log.e("in for","2 : "+ singleItem.getOffImage() ) ;
//            Glide.with(context).load( singleItem.getOffImage() ).asBitmap().into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    Drawable drawable = new BitmapDrawable(resource);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//                    {
//                        circleImageView.setBackground( drawable);
//                    }
//                }
//            });
////            Log.e("in for","3") ;
//            linear.addView( circleImageView) ;
//
//            circleImageView.getLayoutParams().height = 100;
//            circleImageView.getLayoutParams().width = 100;
//
//            TextView t  = new TextView( context ) ;
//            t.setText( singleItem.getsButtonName() ) ;
//            t.setTextSize(TypedValue.COMPLEX_UNIT_PX,  context.getResources().getDimension(R.dimen.tv_title ));
//            t.setPadding( 15,0,0,0 ) ;
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
////            params.weight = 1.0f;
//            params.gravity = Gravity.CENTER;
//            t.setLayoutParams(params);
//
//            linear.addView( t );
//
//
//            //  image for on/off mode
//         final CircleImageView   circleImageViewOnOffMode = new CircleImageView( context ) ;
//            circleImageView.setPadding(0,0,200,0 ) ;
//
//            circleImageViewOnOffMode.setId( singleItem.getId().intValue() ) ;
//            Glide.with(context).load( R.drawable.unclick ).asBitmap().into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    Drawable drawable = new BitmapDrawable(resource);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//                    {
//                        circleImageViewOnOffMode.setBackground( drawable);
//                    }
//                }
//            });
//            LinearLayout.LayoutParams iv_click_unclick_params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.weight = 1.0f;
//            iv_click_unclick_params.gravity = Gravity.CENTER;
//            iv_click_unclick_params.rightMargin = 20;
//            circleImageViewOnOffMode.setLayoutParams(iv_click_unclick_params);
//
//            linear.addView(circleImageViewOnOffMode );
//
//            circleImageViewOnOffMode.getLayoutParams().height = 40;
//            circleImageViewOnOffMode.getLayoutParams().width = 40;
//
//
//            linearLayout.addView(linear );
//
//        }
//        listmainLayout.addView(linearLayout);//you add the whole RadioGroup to the layout
//    }
//
////    private void createRadioButton(int position )
////    {
////        rg = new RadioGroup( context ); //create the RadioGroup
////        rb = new RadioButton[ objectArrayList.size() ];
////        rg.setOrientation( RadioGroup.VERTICAL ); //or RadioGroup.VERTICAL
////        rg.setPadding(10,10,0,0 );
////
////        SwitchButtonDbModel temp  =  objectArrayList.get(position ) ;
////
////        for( int i = 0; i < objectArrayList.size(); i++ )
////        {
////            rb[i]  = new RadioButton( context ) ;
////            rb[i].setPadding(10,0,0,0 );
////
////            rb[ i ].setText( objectArrayList.get( i ).getsButtonName() ) ;
////            String is_selected =  "1" ;
////            if( is_selected.equals("1") )
////            {
////                rb[ i ].setChecked( true ) ;
////            }
////
////
////            rb[ i ].setId( i ) ;
////            rb[i].setTextSize(15 ) ;
////            rg.addView( rb[i] );
////
////            TextView t  = new TextView( context ) ;
////
////            t.setText( "10" + "   "+"30 %" ) ;
////            t.setGravity( Gravity.CENTER );
////            t.setTextSize(15 ) ;
////            t.setPadding( 0,0,0,0 ) ;
////            t.setGravity( Gravity.CENTER ) ;
////            t.setTextColor( Color.GREEN ) ;
////            rg.addView( t );
////        }
////        listmainLayout.addView(rg);//you add the whole RadioGroup to the layout
////    }
//
//
//    @Override
//    public int getCount()
//    {
//        return objectArrayList.size() ;
//    }
//
//    @Override
//    public Object getItem( int i ) {
//        return  objectArrayList.get( i );
//    }
//
//    @Override
//    public long getItemId( int i )
//    {
//        return i;
//    }
//}
