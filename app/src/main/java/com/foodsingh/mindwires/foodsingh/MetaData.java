package com.foodsingh.mindwires.foodsingh;

/**
 * Created by Tanmay on 14-10-2017.
 */

public class MetaData {

    private String Discount, Latest_version, service, Min_Order, msg_api;

    public MetaData(String Discount, String Latest_Version, String service, String Min_Order, String msg_api){

        this.Discount = Discount;
        this.Latest_version = Latest_Version;
        this.service = service;
        this.Min_Order = Min_Order;
        this.msg_api = msg_api;


    }

    public String getDiscount(){
        return Discount;
    }

    public String getLatest_version(){
        return Latest_version;
    }

    public String getservice(){
        return service;
    }

    public String getMin_Order(){
        return Min_Order;
    }

    public String getMsg_api(){
        return msg_api;
    }
}
