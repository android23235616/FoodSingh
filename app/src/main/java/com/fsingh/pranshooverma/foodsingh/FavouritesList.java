package com.fsingh.pranshooverma.foodsingh;

import android.view.Menu;

import java.util.List;

/**
 * Created by Tanmay on 21-10-2017.
 */

public class FavouritesList {

    List<MenuItems> fl;
    public FavouritesList(List<MenuItems> fl){

        this.fl = fl;

    }

    public List<MenuItems> getFavouriteList(){
        return fl;
    }

}
