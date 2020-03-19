package com.example.lenovo.activeandroid3.activity;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.R;
import com.example.lenovo.activeandroid3.adapter.SwitchBoardActivityAdapter;
import com.example.lenovo.activeandroid3.dbModel.SwitchBoardDbModel;
import com.example.lenovo.activeandroid3.fragments.SingleFragment;
import com.example.lenovo.activeandroid3.model.SwitchBoard;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity
{

    Toolbar toolbar ;
    Context context;
    ViewPager mViewPager ;
    TabLayout tabLayout ;
    String  roomId , delete_edit_btn_flag ;
    FlexboxLayout  flexbox_no_data_available ;


    // Fragment List
    private final List<Fragment> mFragmentList = new ArrayList<>();
    // Title List
    public final List<String> mFragmentTitleList = new ArrayList<>();
    public final List<String> mSwitchBoardIdList = new ArrayList<>() ;
    private ViewPagerAdapter pagerAdapter;

    SwitchBoardActivityAdapter adapter ;
    ArrayList<SwitchBoardDbModel> list;
    public List<SwitchBoard> switchList = new ArrayList<>() ;
    RecyclerView recyclerView ;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate( savedInstanceState ) ;
        setContentView( R.layout.activity_tab ) ;
        toolbar = ( Toolbar ) findViewById( R.id.toolbar ) ;
        setSupportActionBar( toolbar ) ;
        context = this ;

        getIntentData( ) ;

        switchList  =  getAllBoardDataOfRoomId( roomId ) ;
        Log.e("switchList size", switchList.size()+"" );
        createFragmenttitleList( switchList ) ;

        mViewPager = ( ViewPager ) findViewById( R.id.container ) ;
        tabLayout = ( TabLayout ) findViewById( R.id.tabs ) ;


//        // edited , you can use multiple titles and one Fragment
        for (int i = 0; i < mFragmentTitleList.size() ; i++ ) {
            mFragmentList.add( new SingleFragment() ) ;
        }


        setupViewPager(mViewPager) ;


        tabLayout.setupWithViewPager( mViewPager ) ;
        // Tab ViewPager setting
        mViewPager.setOffscreenPageLimit( mFragmentList.size() ) ;
        tabLayout.setupWithViewPager( mViewPager ) ;
        tabLayout.setTabMode( TabLayout.MODE_SCROLLABLE ) ;
        tabLayout.setTabGravity( TabLayout.GRAVITY_FILL ) ;
        tabLayout.setTabsFromPagerAdapter( pagerAdapter ) ;

        flexbox_no_data_available =( FlexboxLayout )findViewById( R.id.v2_no_data_present_xml ) ;
        flexbox_no_data_available.setVisibility( View.GONE ) ;
        ImageView  iv_no_data_image = ( ImageView )findViewById( R.id.v2_no_data_image ) ;
        TextView tv_no_data_title = ( TextView )findViewById( R.id.v2_tv_no_data_title ) ;
        tv_no_data_title.setText("No Board Available...!");

        if( switchList.size() == 0 )
        {
            tabLayout.setVisibility(View.GONE);
            flexbox_no_data_available.setVisibility( View.VISIBLE );

        }


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels )
            {

            }

            @Override
            public void onPageSelected( int position )
            {
                try {

                    SingleFragment frag12 =  ( SingleFragment ) mViewPager.getAdapter()
                            .instantiateItem( mViewPager, mViewPager.getCurrentItem() ) ;

                }catch ( Exception e )
                {
                    Log.e("excep addOnPageChange",e.getMessage() );
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2 , menu);
        return true ;

    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId() ;

        //noinspection SimplifiableIfStatement
        if ( id == R.id.action_settings )
        {
//            return true;
            Log.e("click on","item") ;
            Intent i = new Intent( context , AdminMainActivity.class );
            startActivity( i ) ;
            finish();
            overridePendingTransition( R.anim.slide_in_right , R.anim.slide_out_right ) ;
        }

        if ( id == android.R.id.home )
        {
            finish();
//            overridePendingTransition(R.anim.slide_right, R.anim.slide_left );
            overridePendingTransition( R.anim.animation_enter, R.anim.animation_leave ) ;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave ) ;
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.e("inside","onResume TabActivity") ;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_activity2 , menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings)
//        {
////            return true;
//            Log.e("click on","item") ;
//            Intent i = new Intent( context , AdminMainActivity.class ) ;
//            startActivity( i ) ;
//            finish();
//        }
//
//        if (id == android.R.id.home )
//        {
//            finish();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    /*Back Button*/
//    public boolean onOptionsItemSelected( MenuItem item )
//    {
////        Log.e("inisde","onOptionsItemSelected"+TAG );
//        int id = item.getItemId() ;
//
//        return super.onOptionsItemSelected( item ) ;
//    }



    private void createFragmenttitleList(List<SwitchBoard> switchList)
    {

        for( int i= 0 ; i < switchList.size() ; i++ )
        {
            mFragmentTitleList.add( switchList.get( i ).BoardName  ) ;
            mSwitchBoardIdList.add( String.valueOf( switchList.get( i ).getId()  ) ) ;

        }
    }


    // Read all Data
    private List<SwitchBoard> getAllBoardDataOfRoomId( String roomId )
    {
//        Log.e("inside","getAll roomid is"+roomId ) ;


        //  order by id
//        return new Select().from( SwitchBoard.class ).orderBy("id ASC").execute() ;

        // access by id
//        return new Select().from(SwitchBoard.class).where("id = ?", roomId ).execute();


        // access bu RoomId
        return new Select().from(SwitchBoard.class).where("RoomId = ?", roomId ).execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();

    }



    private void setupViewPager(ViewPager viewPager)
    {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager() , mFragmentTitleList ) ;
        viewPager.setAdapter( pagerAdapter ) ;
    }

    private void getIntentData()
    {

        Intent intent = getIntent() ;
        intent.getStringExtra("roomId");
        roomId = intent.getStringExtra("roomId") ;
        delete_edit_btn_flag = intent.getStringExtra("delete_edit_btn_flag") ;

//        Log.e("room id","in getIntentData"+roomId ) ;
    }


    /**
     * ViewPagerAdapter setting
     */
    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        //        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

//        public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titleLists) {
//            super(fm);
//            this.mFragmentList = fragments;
//            this.mFragmentTitleList = titleLists;
//        }

        public ViewPagerAdapter(FragmentManager fm,  List<String> titleLists) {
            super(fm);

            this.mFragmentTitleList = titleLists;
        }

        @Override
        public Fragment getItem( int position )
        {
            Log.e("inside","getItem") ;


            return SingleFragment.newInstance( mSwitchBoardIdList.get( position ) ) ;
//            SingleFragment fragment = new SingleFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("switchBoardId", "1");
//            fragment.setArguments(bundle);
//            return fragment ;

        }

        @Override
        public int getCount() {
            return mFragmentTitleList == null ? 0 : mFragmentTitleList.size();
        }

        @Override
        public CharSequence getPageTitle( int position )
        {
            Log.e("inside","getPageTitle");
            return mFragmentTitleList.get(position);
        }
    }





}
