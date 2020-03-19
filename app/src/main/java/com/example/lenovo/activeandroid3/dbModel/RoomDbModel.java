package com.example.lenovo.activeandroid3.dbModel;

import java.sql.Timestamp;

/**
 * Created by lenovo on 27/11/17.
 */

public  class RoomDbModel
{
    private Long id ;
    private String rName ;
    private Long rCreatedAt ;
    private Long rUpdatedAt ;
    private  String roomHomePageImage ;
    private String addRommImage ;
    private int roomOnOff ;
    private boolean atLeastOneButtonOfRoomIsOn ;

    public String getRoomHomePageImage() {
        return roomHomePageImage;
    }

    public void setRoomHomePageImage(String roomHomePageImage) {
        this.roomHomePageImage = roomHomePageImage;
    }

    public RoomDbModel(Long id, String rName, Long rCreatedAt, Long rUpdatedAt , String roomImage , String addRommImage ,int roomOnOff, boolean atLeastOneButtonOfRoomIsOn) {
        this.id = id;
        this.rName = rName;
        this.rCreatedAt = rCreatedAt;
        this.rUpdatedAt = rUpdatedAt ;
        this.roomHomePageImage = roomImage ;
        this.addRommImage = addRommImage ;
        this.roomOnOff =roomOnOff ;
        this.atLeastOneButtonOfRoomIsOn = atLeastOneButtonOfRoomIsOn;
    }


    public int getRoomOnOff() {
        return roomOnOff;
    }

    public void setRoomOnOff(int roomOnOff) {
        this.roomOnOff = roomOnOff;
    }

    public String getAddRommImage() {
        return addRommImage;
    }

    public void setAddRommImage(String addRommImage) {
        this.addRommImage = addRommImage;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public Long getrCreatedAt() {
        return rCreatedAt;
    }

    public void setrCreatedAt(Long rCreatedAt) {
        this.rCreatedAt = rCreatedAt;
    }

    public Long getrUpdatedAt() {
        return rUpdatedAt;
    }

    public void setrUpdatedAt( Long rUpdatedAt) {
        this.rUpdatedAt = rUpdatedAt;
    }

    public boolean isAtLeastOneButtonOfRoomIsOn() {
        return atLeastOneButtonOfRoomIsOn;
    }

    public void setAtLeastOneButtonOfRoomIsOn(boolean atLeastOneButtonOfRoomIsOn) {
        this.atLeastOneButtonOfRoomIsOn = atLeastOneButtonOfRoomIsOn;
    }
}
