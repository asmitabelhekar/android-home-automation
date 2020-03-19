package com.example.lenovo.activeandroid3.dbModel;

import java.sql.Timestamp;

/**
 * Created by lenovo on 27/11/17.
 */

public class SwitchBoardDbModel
{
    private Long id ;
    private String sBName ;
    private String sBRoomId ;
    private Long sBCreatedAt ;
    private Long sBUpdatedAt ;

    public SwitchBoardDbModel(Long id, String sBName, String sBRoomId, Long sBCreatedAt, Long sBUpdatedAt) {
        this.id = id;
        this.sBName = sBName;
        this.sBRoomId = sBRoomId;
        this.sBCreatedAt = sBCreatedAt;
        this.sBUpdatedAt = sBUpdatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getsBName() {
        return sBName;
    }

    public void setsBName(String sBName) {
        this.sBName = sBName;
    }

    public String getsBRoomId() {
        return sBRoomId;
    }

    public void setsBRoomId(String sBRoomId) {
        this.sBRoomId = sBRoomId;
    }

    public Long getsBCreatedAt() {
        return sBCreatedAt;
    }

    public void setsBCreatedAt(Long sBCreatedAt) {
        this.sBCreatedAt = sBCreatedAt;
    }

    public Long getsBUpdatedAt() {
        return sBUpdatedAt;
    }

    public void setsBUpdatedAt(Long sBUpdatedAt) {
        this.sBUpdatedAt = sBUpdatedAt;
    }
}
