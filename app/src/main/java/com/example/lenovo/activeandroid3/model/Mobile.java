package com.example.lenovo.activeandroid3.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by anway on 17/8/18.
 */

@Table( name = "Mobile" )
public class Mobile extends Model
{
    @Column( name = "CreatedAt" )
    public Long CreatedAt ;

    @Column( name = "Updatedat")
    public Long Updatedat ;

    @Column(name = "MobileName")
    public String MobileName ;

    @Column(name = "IP")
    public String IP ;
}
