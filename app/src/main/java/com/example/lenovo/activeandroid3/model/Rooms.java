package com.example.lenovo.activeandroid3.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by lenovo on 27/11/17.
 */
@Table( name = "Rooms" )
public class Rooms extends Model
{
    @Column( name = "CreatedAt" )
    public Long CreatedAt ;

    @Column( name = "Updatedat")
    public Long Updatedat ;

    @Column(name = "Name")
    public String Name ;

    @Column(name = "RoomHomePageImage")
    public String RoomHomePageImage ;

    @Column(name = "AddRoomImage")
    public String AddRoomImage ;

    // if on = 1 , off = 0
    @Column(name = "RoomOnOff")
    public int RoomOnOff ;

    @Column(name = "AtLeastOneButtonOfRoomIsOn")
    public  boolean AtLeastOneButtonOfRoomIsOn ;



    // This method is optional, does not affect the foreign key creation.
    public List<SwitchBoard> getBoards() {
        return getMany(SwitchBoard.class, "Rooms");
    }



}
