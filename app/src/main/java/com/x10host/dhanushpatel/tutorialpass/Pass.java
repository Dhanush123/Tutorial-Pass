package com.x10host.dhanushpatel.tutorialpass;

import com.orm.SugarRecord;

/**
 * Created by Dhanush on 3/7/2015.
 */
public class Pass extends SugarRecord<Pass> {
    String info;

    public Pass(){ super(); }

    public Pass(String info){
        this.info = info;
    }
    public String passReturn()
    {
        return info;
    }
}