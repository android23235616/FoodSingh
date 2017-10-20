package com.fsingh.pranshooverma.foodsingh;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;

public class NotificationActivity extends AppCompatActivity {

    Gson gson;
    NotificationLists notificationLists;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
        recyclerView = (RecyclerView)findViewById(R.id.notificationRecycler);

        String tempJson = sharedPreferences.getString(constants.foodsinghNotif,"");

        if(tempJson.equals("")){
            Display("You Have No Notification");
        }else{
            notificationLists = gson.fromJson(tempJson,NotificationLists.class);
            notificationAdapter = new NotificationAdapter(notificationLists.getNotification(),this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(notificationAdapter);
            recyclerView.setNestedScrollingEnabled(true);
            notificationAdapter.notifyDataSetChanged();

        }


    }
    private void Display(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

}


