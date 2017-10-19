package com.fsingh.pranshooverma.foodsingh;

import android.app.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanmay on 20-10-2017.
 */

public class NotificationLists {
    private List<String> notification = new ArrayList<>();

    public NotificationLists(List<String> notificaation){

        notification = notificaation;

    }

    public List<String> getNotification(){
        return notification;
    }

}
