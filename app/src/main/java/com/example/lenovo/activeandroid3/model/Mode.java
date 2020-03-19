package com.example.lenovo.activeandroid3.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by lenovo on 6/12/17.
 */
@Table( name = "Mode" )
public class Mode  extends Model
{
    @Column( name = "CreatedAt" )
    public Long CreatedAt ;

    @Column( name = "Updatedat")
    public Long Updatedat ;

    @Column(name = "ModeName")
    public String ModeName ;

    @Column(name = "ModeImage")
    public int ModeImage ;

    @Column(name = "isOn")
    public boolean isOn ;



}
