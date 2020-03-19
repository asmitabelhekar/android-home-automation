package com.example.lenovo.activeandroid3.activity.v1;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by akshay on 27/5/18.
 */

public class WrapContentLinearLayoutManager extends LinearLayoutManager
{
    public WrapContentLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("probe", "handle a IOOBE of RecyclerView in WrapContentLinearLayoutManager");
        }
    }
}
