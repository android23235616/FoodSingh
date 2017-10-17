package com.fsingh.pranshooverma.foodsingh;

/**
 * Created by Tanmay on 17-10-2017.
 */

public class FoodItem {

    private String id, item, amount, address, date;

    public FoodItem(String id, String item , String amount, String address, String date){
        this.id = id;
        this.item = item;
        this.amount = amount;
        this.address = address;
        this.date = date;
    }

    public String getId(){
        return id;
    }

    public String getItem(){
        return item;
    }
}
