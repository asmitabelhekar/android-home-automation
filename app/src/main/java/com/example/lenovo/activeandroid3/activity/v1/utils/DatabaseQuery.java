package com.example.lenovo.activeandroid3.activity.v1.utils;

import android.widget.Button;

import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.model.Mode;
import com.example.lenovo.activeandroid3.model.SwitchButton;

import java.util.List;

/**
 * Created by anway on 22/1/19.
 */

public class DatabaseQuery
{
    // Read all Modes
    private List<SwitchButton> getAllONButtons(boolean status)
    {


        //  order by id
        return new Select().from(SwitchButton.class).where("is_on = ?", status).execute();

        // return data in random order
//        return new Select().from(Rooms.class).orderBy("RANDOM()") .execute();

    }



    public static List<SwitchButton> getAllButtons()
    {
        return new Select().from(SwitchButton.class).execute();
    }
}
