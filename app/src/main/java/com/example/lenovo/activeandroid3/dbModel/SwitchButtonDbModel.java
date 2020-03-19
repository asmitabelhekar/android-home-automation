package com.example.lenovo.activeandroid3.dbModel;

/**
 * Created by lenovo on 28/11/17.
 */

public class SwitchButtonDbModel
{
    private Long id ;
    private String sButtonName ;
    private  Long roomId ;
    private int onImage ;
    private int offImage ;

    // button vr click kelyavr room chi position bhetal nhavti. mhanun pratek button la roomposition set keli.
    private  int roomPositionWithButton ;

    // card vr chya button vr click kel tr tya card vr te button kiti position la aahe he samjav mhanun ha flag use kela aahe.
    private  int buttonPositionInsideRoom ;
    public SwitchButtonDbModel(Long id, String sButtonName, Long sBoardId, Long sButtonCreatedAt, Long sButtonUpdatedAt,
                                Boolean isOn, Long roomId ,
                                int onImage , int offImage, int roomPositionWithButton , int buttonPositionInsideRoom ) {
        this.id = id;
        this.sButtonName = sButtonName;
        this.sBoardId = sBoardId;
        this.sButtonCreatedAt = sButtonCreatedAt;
        this.sButtonUpdatedAt = sButtonUpdatedAt;
        this.isOn = isOn;
        this.roomId =roomId ;
        this.onImage = onImage ;
        this.offImage = offImage ;
        this.roomPositionWithButton  = roomPositionWithButton;
        this.buttonPositionInsideRoom = buttonPositionInsideRoom ;
    }

    public int getRoomPositionWithButton() {
        return roomPositionWithButton;
    }

    public void setRoomPositionWithButton(int roomPositionWithButton) {
        this.roomPositionWithButton = roomPositionWithButton;
    }

    public int getButtonPositionInsideRoom() {
        return buttonPositionInsideRoom;
    }

    public void setButtonPositionInsideRoom(int buttonPositionInsideRoom) {
        this.buttonPositionInsideRoom = buttonPositionInsideRoom;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public int getOnImage() {
        return onImage;
    }

    public void setOnImage(int onImage) {
        this.onImage = onImage;
    }

    public int getOffImage() {
        return offImage;
    }

    public void setOffImage(int offImage) {
        this.offImage = offImage;
    }

    private Long sBoardId ;
    private Long sButtonCreatedAt ;
    private Long sButtonUpdatedAt ;
    private  Boolean isOn ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getOn() {
        return isOn;
    }

    public void setOn(Boolean on) {
        isOn = on;
    }


    public String getsButtonName() {
        return sButtonName;

    }

    public void setsButtonName(String sButtonName) {
        this.sButtonName = sButtonName;
    }

    public Long getsBoardId() {
        return sBoardId;
    }

    public void setsBoardId(Long sBoardId) {
        this.sBoardId = sBoardId;
    }

    public Long getsButtonCreatedAt() {
        return sButtonCreatedAt;
    }

    public void setsButtonCreatedAt(Long sButtonCreatedAt) {
        this.sButtonCreatedAt = sButtonCreatedAt;
    }

    public Long getsButtonUpdatedAt() {
        return sButtonUpdatedAt;
    }

    public void setsButtonUpdatedAt(Long sButtonUpdatedAt) {
        this.sButtonUpdatedAt = sButtonUpdatedAt;
    }
}
