package com.fsingh.pranshooverma.foodsingh;

import android.app.Notification;

/**
 * Created by Tanmay on 20-10-2017.
 */

public class NotificationItem {

    private String body,title, img, url, activity, notificationType, time;

    public NotificationItem(String body, String title, String img, String url, String activity, String notificationType,String time){

        this.body = body;
        this.title = title;
        this.time = time;
        this.img = img;
        this.url = url;
        this.activity = activity;
        this.notificationType = notificationType;

    }

    public String getBody(){
        return body;
    }

    public String getTitle(){
        return title;
    }

    public String getImg(){
        return img;
    }

    public String getActivity(){
        return activity;
    }

    public String getUrl(){
        return url;
    }

    public String getNotificationType(){
        return notificationType;
    }

    public String getTime(){
        return time;
    }

}
