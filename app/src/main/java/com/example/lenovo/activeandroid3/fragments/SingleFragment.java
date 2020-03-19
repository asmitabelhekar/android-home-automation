package com.example.lenovo.activeandroid3.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.SwitchButtonActivity;
import com.example.lenovo.activeandroid3.activity.TabActivity;
import com.example.lenovo.activeandroid3.adapter.SingleFragmentAdapter;
import com.example.lenovo.activeandroid3.adapter.SingleFragmentListAdapter;
import com.example.lenovo.activeandroid3.adapter.SwitchButtonActivityAdapter;
import com.example.lenovo.activeandroid3.dbModel.SwitchButtonDbModel;
import com.example.lenovo.activeandroid3.model.SwitchButton;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 8/12/17.
 */

public class SingleFragment extends Fragment
{
    Context context ;
    TabActivity tabActivity ;
    public static final String BOARDID = "BOARDID";
    private String boardId;
    public List<SwitchButton> switchButtonList = new ArrayList<>() ;
//    RecyclerView recyclerView ;
//SingleFragmentAdapter adapter ;


    ListView listview ;
    SingleFragmentListAdapter adapter ;

    LinearLayoutManager layout_manager ;
    String TAG1 = "SingleFragment" ;

    ArrayList<SwitchButtonDbModel> list ;
    FlexboxLayout flexbox_no_data_available ;
    ImageView iv_no_data_image ;
    TextView  tv_no_data_title ;

    public SingleFragment()
    {
//        Log.e("SingleFragment","default constructor");
    }

    public static SingleFragment newInstance( String  boardId ) {
        Bundle args = new Bundle();
        args.putString( BOARDID, boardId ) ;
        SingleFragment fragment = new SingleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate( @Nullable Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        boardId = getArguments().getString(BOARDID);
        Log.e("SingleFragment mPage","inside fragments mPage"+ boardId ) ;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( getActivity() ) ;
        context = getActivity() ;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState ) {

//        String switchBoardId = getArguments().getString("switchBoardId") ;
//        Log.e("switchBoardId", switchBoardId);
        View rootview = inflater.inflate( R.layout.fragment_single,container , false ) ;
//        Log.e("SingleFragment","onCreateView" ) ;

//        TextView tv= ( TextView) rootview.findViewById( R.id.tv );
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Log.e("inside","click"+boardId ) ;
//            }
//        });




//        tv.setText( switchButtonList.get( 0).SwitchButtonName + switchButtonList.get(1).SwitchButtonName ) ;

        initialization( rootview ) ;

//        this.recyclerView = (RecyclerView) rootview.findViewById( R.id.recycler_view ) ;
//        layout_manager = new LinearLayoutManager( getActivity() ) ;
//        this.recyclerView.setLayoutManager( layout_manager );
//        adapter = new SingleFragmentAdapter( getContext()  , this , "1") ;
//        adapter.setRecyclerView(recyclerView);

        flexbox_no_data_available =(FlexboxLayout)rootview.findViewById( R.id.v2_no_data_present_xml );
        flexbox_no_data_available.setVisibility( View.GONE ) ;
        iv_no_data_image = (ImageView)rootview.findViewById( R.id.v2_no_data_image ) ;
        tv_no_data_title = (TextView)rootview.findViewById( R.id.v2_tv_no_data_title ) ;


        switchButtonList = getButtonId( boardId );
        setToDbModel( switchButtonList ) ;




        return rootview;
    }

    private void setToDbModel( List<SwitchButton> switchButtonList )
    {
//        Log.e("switchButtonsize in set",switchButtonList.size()+"" ) ;
        list = new ArrayList<>() ;

        if( switchButtonList.size() > 0 ) {
            for (int i = 0; i < switchButtonList.size(); i++) {

//            Log.e("ButtonName ,id, boardId",switchButtonList.get( i ).SwitchButtonName +" "+switchButtonList.get(i).getId()+""+switchButtonList.get(i).SwitchBoardId  );
                list.add(new SwitchButtonDbModel(
                        switchButtonList.get(i).getId(),
                        switchButtonList.get(i).SwitchButtonName,
                        switchButtonList.get(i).SwitchBoardId,
                        switchButtonList.get(i).SwitchButtonCreatedAt,
                        switchButtonList.get(i).SwitchButtonUpdatedat,
                        switchButtonList.get(i).is_on,
                        switchButtonList.get(i).RoomId,
                        switchButtonList.get(i).OnImage,
                        switchButtonList.get(i).OffImage,
                        1 , // this flag is used only for editmode dialog
                        1 // card vr room madhe button kiti position la aahe he samjav mhanun ha flag set kela aahe.pn ethe ha fakt dummy flag aahe.ethe yach use nahi.
                        // Actucally ha flag fakt ModeFragmentAdapter madhech use kela aahe.
                ));
            }

            adapter.addItemMore(list);
        }else {
            flexbox_no_data_available.setVisibility( View.VISIBLE );
        }
    }


    private List<SwitchButton> getButtonId(String boardId)
    {
        //  order by id
//        return new Select().from( SwitchBoard.class ).orderBy("id ASC").execute() ;

        // access by id
//        return new Select().from(SwitchBoard.class).where("id = ?", roomId ).execute();


        // access by RoomId
        return new Select().from(SwitchButton.class).where("SwitchBoardId = ?", boardId ).execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();

    }

//    public void sendData(TabActivity tabActivity)
//    {
//        Log.e("inside","sendData") ;
//        this.tabActivity  = tabActivity ;
//
//
//    }


    private void initialization( View rootView )
    {

//        recyclerView = (RecyclerView)rootView.findViewById( R.id.recycler_view ) ;
//        layout_manager = new LinearLayoutManager( context ) ;
//        recyclerView.setLayoutManager(layout_manager);
//        adapter = new SingleFragmentAdapter( context  , SingleFragment.this , "1" );
//        recyclerView.setLayoutManager(layout_manager);
//        recyclerView.setAdapter( adapter ) ;




        listview = (ListView)rootView.findViewById( R.id.listview ) ;
        adapter = new SingleFragmentListAdapter( context  , SingleFragment.this , "1" );
        listview.setAdapter( adapter ) ;

    }

}
