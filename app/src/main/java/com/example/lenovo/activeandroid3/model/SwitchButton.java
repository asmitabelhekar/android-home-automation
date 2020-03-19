package com.example.lenovo.activeandroid3.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.sql.Timestamp;

/**
 * Created by lenovo on 27/11/17.
 */
@Table( name = "SwitchButton" )
public class SwitchButton extends Model
{
    @Column( name = "SwitchButtonCreatedAt")
    public Long SwitchButtonCreatedAt ;

    @Column( name = "SwitchButtonUpdatedat")
    public Long SwitchButtonUpdatedat ;

    @Column(name = "SwitchButtonName")
    public String SwitchButtonName ;

    @Column(name = "SwitchBoardId" )
    public Long SwitchBoardId ;

    @Column( name = "is_on" )
    public boolean is_on ;

    @Column( name = "RoomId")
    public  Long RoomId ;

    @Column(name = "OnImage" )
    public int OnImage ;

    @Column(name = "OffImage" )
    public int OffImage ;

    @Column(name = "SwitchButtonPosition" )
    public int SwitchButtonPosition;

}
