package com.example.lenovo.activeandroid3.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by lenovo on 6/12/17.
 */
@Table( name = "ModeOnOf" )
public class ModeOnOf extends Model
{

    @Column( name = "CreatedAt" )
    public Long CreatedAt ;

    @Column( name = "Updatedat")
    public Long Updatedat ;

    @Column( name = "ModeId" )
    public String ModeId ;

    @Column( name = "ButtonName" )
    public String ButtonName ;

    @Column( name = "ButtonId" )
    public String ButtonId ;





}
