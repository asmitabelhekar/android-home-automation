package com.example.lenovo.activeandroid3.activity.v1.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.v1.adapter.ModeFragmentAdapter;
import com.example.lenovo.activeandroid3.activity.v1.adapter.RoomFragmentAdapter;
import com.example.lenovo.activeandroid3.dbModel.ModesActivityDbModel;
import com.example.lenovo.activeandroid3.dbModel.RoomDbModel;
import com.example.lenovo.activeandroid3.model.Mode;
import com.example.lenovo.activeandroid3.util.ItemOffsetDecoration;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 17/5/18.
 */

public class ModeFragments extends Fragment
{
    String TAG = "ModeFragment" ;
    RecyclerView recyclerView;
    ModeFragments my_object = this;
    Context context ;

    public ModeFragmentAdapter adapter;
    LinearLayoutManager layout_manager;

    // No Data available
    FlexboxLayout flexbox_no_data_available ;
    ImageView iv_no_data_image ;
    TextView tv_no_data_title ;

    public List<Mode> modeList = new ArrayList<>() ;
    ArrayList<ModesActivityDbModel> list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        Log.e(TAG ,"onCreate");
        context = getActivity() ;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState ) {

        View rootview = inflater.inflate( R.layout.fragment_room,container , false ) ;

        try
        {
//            Log.e(TAG,"onCreateView" ) ;
            this.recyclerView = ( RecyclerView ) rootview.findViewById( R.id.recycler_view ) ;
            FloatingActionButton fab_check_in = ( FloatingActionButton )rootview.findViewById( R.id.fab_check_in ) ;
            fab_check_in.setVisibility(View.GONE);

            flexbox_no_data_available =( FlexboxLayout )rootview.findViewById( R.id.v2_no_data_present_xml );
            flexbox_no_data_available.setVisibility( View.GONE ) ;
            iv_no_data_image = (ImageView)rootview.findViewById( R.id.v2_no_data_image ) ;
            tv_no_data_title=(TextView)rootview.findViewById( R.id.v2_tv_no_data_title ) ;

            layout_manager = new LinearLayoutManager( getActivity() ) ;
            this.recyclerView.setLayoutManager( layout_manager );

            GridLayoutManager gridLayoutManager =  new GridLayoutManager( context, 2 ) ;
            recyclerView.setHasFixedSize( true ) ;
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setLayoutManager(gridLayoutManager) ; // set LayoutManager to RecyclerView


            adapter = new ModeFragmentAdapter(  context ) ;
            this.recyclerView.setAdapter( adapter ) ;

        }catch (Exception e){
            Log.e(TAG + "Bind",e.getMessage());
        }
        return rootview;
    }

    public void makeAdapterListNull()
    {
        this.adapter.setListNull();
    }

    public void onLoadData()
    {
//        Log.e(TAG , "onLoadData") ;
        modeList = getAllModes() ;
//        Log.e("modeList size ", modeList.size()+"" ) ;
        setToDbModel( modeList ) ;
    }

    private void setToDbModel(List<Mode> modeList)
    {
        list = new ArrayList<>();

        for( int i = 0 ; i< modeList.size() ; i++ )
        {
//            Log.e("Modename is",modeList.get( i ).ModeName );
//            Log.e("ModeImage is",modeList.get( i ).ModeImage+"" );
//            Log.e("ModeIsOn is",modeList.get( i ).isOn+"" );
//            Log.e("ModeCreatedAt is",modeList.get( i ).CreatedAt+"" );
//            Log.e("ModeUpdatedAt is",modeList.get( i ).Updatedat+"" );

            list.add(new ModesActivityDbModel(
                     modeList.get(i).getId() ,
                     modeList.get( i).ModeName ,
                     modeList.get(i).CreatedAt ,
                     modeList.get(i).Updatedat  ,
                     modeList.get(i).isOn ,
                     modeList.get(i).ModeImage )) ;
        }
        adapter.addItem( list ) ;
    }

    // Read all Modes
    private List<Mode> getAllModes()
    {
//        Log.e("inside","getAll");

        //  order by id
        return new Select().from(Mode.class).orderBy("id ASC").execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();

    }
}
