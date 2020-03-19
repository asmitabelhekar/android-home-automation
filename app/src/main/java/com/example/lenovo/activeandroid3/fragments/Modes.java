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
import android.widget.Button;

import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.activity.MainActivity;
import com.google.android.flexbox.FlexboxLayout;

/**
 * Created by lenovo on 5/12/17.
 */

public class Modes extends Fragment
{

    private static final String TAG = "Modes";
    Context context ;
    RecyclerView recyclerView ;
    LinearLayoutManager   layout_manager ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.e("In onCreate", "Modes");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        auth_token = sharedPref.getString("auth_token", "");
//        society_id = sharedPref.getString("society_id", "");
//        id = sharedPref.getString("id", "");
//        flat_id = sharedPref.getString("flat_id", "");
//        role_id = sharedPref.getString("roll","" ) ;
        context = getActivity() ;
//        onLoadMore( 0 );
    }

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
//        Log.e("Inside onCreateView", "Modes" ) ;

        View rootview = inflater.inflate( R.layout.fragment_modes , container , false ) ;

        try {


            Button btn_frag = ( Button )rootview.findViewById( R.id.btn_frag ) ;
//            btn_frag.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    new MainActivity().openAnotherFragment();
//
//                }
//            });


//            recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view);
//               layout_manager = new LinearLayoutManager(getActivity());
//            recyclerView.setLayoutManager(layout_manager);
//            FloatingActionButton fab_emergency = (FloatingActionButton) rootview.findViewById( R.id.fab_emergency );

//            v2_no_data_present_xml = (FlexboxLayout)rootview.findViewById( R.id.v2_no_data_present_xml );
//            v2_no_data_present_xml.setVisibility( View.GONE ) ;

//            adapter = new EmergencyServicesAdapter( getContext() , this.auth_token  ,this );
//            adapter.setRecyclerView(recyclerView);
//            adapter.setMoreLoading(false);
//            adapter.setLinearLayoutManager(this.layout_manager);
//
            //recyclerView.setAdapter( adapter ) ;

//            if( role_id.equals("2") )
//            {
//                fab_emergency.setVisibility( View.VISIBLE );
//            }else {
//                fab_emergency.setVisibility( View.GONE ) ;
//            }
//
//
//            fab_emergency.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view)
//                {
//
//                    add_services();
//
//                }
//            });

        }catch (Exception e)
        {
            Log.e(TAG + "Bind",e.getMessage());}


        return rootview;
    }

}
