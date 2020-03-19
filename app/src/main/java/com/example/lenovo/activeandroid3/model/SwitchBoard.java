package com.example.lenovo.activeandroid3.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by lenovo on 27/11/17.
 */
@Table( name = "SwitchBoard" )
public class SwitchBoard extends Model
{


    @Column(name = "BoardName")
    public String BoardName ;

    @Column(name = "RoomId")
    public String RoomId ;

    @Column(name = "IP")
    public String IP ;

    @Column(name = "Rooms")
    public Rooms rooms;

    // This method is optional, does not affect the foreign key creation.
    public List<SwitchButton> getButtons() {
        return getMany(SwitchButton.class, "SwitchBoard");
    }

    @Column(name = "BoardCreatedAt")
    public Long BoardCreatedAt ;

    @Column(name = "BoardUpdatedat")
    public Long BoardUpdatedat ;

}
