package com.example.lenovo.activeandroid3.dbModel;

/**
 * Created by akshay on 28/5/18.
 */

public class ModelNewDbModel
{
    private Long roomId ;
    private String roomName ;
    private Long rCreatedAt ;
    private Long rUpdatedAt ;
    private  String roomHomePageImage ;
    private String addRommImage ;
    private int roomOnOff ;
    private String switchButtonArray ;

    public ModelNewDbModel(Long roomId, String roomName, Long rCreatedAt, Long rUpdatedAt , String roomImage ,
                           String addRommImage ,int roomOnOff , String switchButtonArray) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.rCreatedAt = rCreatedAt;
        this.rUpdatedAt = rUpdatedAt ;
        this.roomHomePageImage = roomImage ;
        this.addRommImage = addRommImage ;
        this.roomOnOff =roomOnOff ;
        this.switchButtonArray = switchButtonArray ;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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

    public void setrUpdatedAt(Long rUpdatedAt) {
        this.rUpdatedAt = rUpdatedAt;
    }

    public String getRoomHomePageImage() {
        return roomHomePageImage;
    }

    public void setRoomHomePageImage(String roomHomePageImage) {
        this.roomHomePageImage = roomHomePageImage;
    }

    public String getAddRommImage() {
        return addRommImage;
    }

    public void setAddRommImage(String addRommImage) {
        this.addRommImage = addRommImage;
    }

    public int getRoomOnOff() {
        return roomOnOff;
    }

    public void setRoomOnOff(int roomOnOff) {
        this.roomOnOff = roomOnOff;
    }

    public String getSwitchButtonArray() {
        return switchButtonArray;
    }

    public void setSwitchButtonArray(String switchButtonArray) {
        this.switchButtonArray = switchButtonArray;
    }
}
