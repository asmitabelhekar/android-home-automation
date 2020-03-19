package com.example.lenovo.activeandroid3.dbModel;

/**
 * Created by lenovo on 6/12/17.
 */

public class ModesDetailActivityDbModel
{
    private String roonName ;
    private Long  roomId ;
    private String boardName ;
    private  Long boardId ;
    private String buttonName ;
    private Long buttonId ;

    public ModesDetailActivityDbModel(String roonName, Long roomId, String boardName, Long boardId, String buttonName, Long buttonId) {
        this.roonName = roonName;
        this.roomId = roomId;
        this.boardName = boardName;
        this.boardId = boardId;
        this.buttonName = buttonName;
        this.buttonId = buttonId;
    }

    public String getRoonName() {
        return roonName;
    }

    public void setRoonName(String roonName) {
        this.roonName = roonName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public Long getButtonId() {
        return buttonId;
    }

    public void setButtonId(Long buttonId) {
        this.buttonId = buttonId;
    }
}
