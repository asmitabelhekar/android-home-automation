package com.example.lenovo.activeandroid3.dbModel;

/**
 * Created by anway on 21/8/18.
 */

public class ModeNetworkCallDbModel
{
    private Long buttonId ;
    private int switchNumber  ;
    private Long switchBoardId  ;
    private  String IP ;
    private int action ;
    private Long roomId ;

    public ModeNetworkCallDbModel( Long roomId ,Long buttonId , int switchNumber, Long switchBoardId, String IP, int action) {
        this.buttonId = buttonId;
        this.switchNumber = switchNumber;
        this.switchBoardId = switchBoardId;
        this.IP = IP;
        this.action = action;
        this.roomId= roomId ;
    }

    public Long getButtonId() {
        return buttonId;
    }

    public void setButtonId(Long buttonId) {
        this.buttonId = buttonId;
    }

    public int getSwitchNumber() {
        return switchNumber;
    }

    public void setSwitchNumber(int switchNumber) {
        this.switchNumber = switchNumber;
    }

    public Long getSwitchBoardId() {
        return switchBoardId;
    }

    public void setSwitchBoardId(Long switchBoardId) {
        this.switchBoardId = switchBoardId;
    }

    public String getIP() {
        return IP;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }


}
