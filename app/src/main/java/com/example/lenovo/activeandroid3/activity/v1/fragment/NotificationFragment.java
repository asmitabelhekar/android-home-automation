package com.example.lenovo.activeandroid3.activity.v1.fragment;

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
import android.widget.TextView;

import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.v1.WrapContentGridLayoutManager;
import com.example.lenovo.activeandroid3.activity.v1.adapter.NotificationFragmentAdapter;
import com.example.lenovo.activeandroid3.activity.v1.adapter.RoomFragmentAdapter;
import com.example.lenovo.activeandroid3.util.ItemOffsetDecoration;
import com.google.android.flexbox.FlexboxLayout;

/**
 * Created by anway on 29/10/18.
 */

public class NotificationFragment extends Fragment {

    Context context;
    SharedPreferences sharedPref;
    NotificationFragment myObject = this;
    NotificationFragmentAdapter adapter;
    String TAG = "NotificationFragment ";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        context = getActivity();
    }

    RecyclerView recyclerView ;
    FlexboxLayout flexbox_no_data_available ;
    ImageView iv_no_data_image;
    TextView tv_no_data_title;
    LinearLayoutManager layout_manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View rootview = inflater.inflate(R.layout.fragment_room, container, false);
        try
        {
            this.recyclerView = (RecyclerView) rootview.findViewById( R.id.recycler_view ) ;

            FloatingActionButton fab_check_in = ( FloatingActionButton )rootview.findViewById( R.id.fab_check_in );
            fab_check_in.setVisibility(  View.GONE );

            flexbox_no_data_available =(FlexboxLayout)rootview.findViewById( R.id.v2_no_data_present_xml );
            flexbox_no_data_available.setVisibility( View.GONE ) ;
            iv_no_data_image = (ImageView)rootview.findViewById( R.id.v2_no_data_image ) ;
            tv_no_data_title=(TextView)rootview.findViewById( R.id.v2_tv_no_data_title ) ;
            tv_no_data_title.setText("No data available");

            layout_manager = new LinearLayoutManager( getActivity() ) ;
            this.recyclerView.setLayoutManager( layout_manager );

            recyclerView.setHasFixedSize( true ) ;
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
            recyclerView.addItemDecoration(itemDecoration);

            adapter = new NotificationFragmentAdapter(context, myObject) ;
            this.recyclerView.setAdapter( adapter ) ;


        }catch (Exception e){
            Log.e(TAG + "Bind",e.getMessage());
        }
        return rootview;
    }
}
