package com.example.lenovo.activeandroid3.dbModel;

/**
 * Created by lenovo on 6/12/17.
 */

public class ModesActivityDbModel
{
    private Long modeId ;
    private String modeName ;
    private Long modeCreatedAt ;
    private Long modeUpdatedAt ;
    private boolean isOn ;
    private int modeImage ;

    public ModesActivityDbModel(Long modeId, String modeName, Long modeCreatedAt, Long modeUpdatedAt , boolean isOn , int modeImage) {
        this.modeId = modeId;
        this.modeName = modeName;
        this.modeCreatedAt = modeCreatedAt;
        this.modeUpdatedAt = modeUpdatedAt;
        this.isOn = isOn ;
        this.modeImage = modeImage ;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public Long getModeId() {
        return modeId;
    }

    public void setModeId(Long modeId) {
        this.modeId = modeId;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public Long getModeCreatedAt() {
        return modeCreatedAt;
    }

    public void setModeCreatedAt(Long modeCreatedAt) {
        this.modeCreatedAt = modeCreatedAt;
    }

    public Long getModeUpdatedAt() {
        return modeUpdatedAt;
    }

    public void setModeUpdatedAt(Long modeUpdatedAt) {
        this.modeUpdatedAt = modeUpdatedAt;
    }

    public int getModeImage() {
        return modeImage;
    }

    public void setModeImage(int modeImage) {
        this.modeImage = modeImage;
    }
}
