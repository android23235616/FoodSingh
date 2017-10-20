package com.fsingh.pranshooverma.foodsingh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
        recyclerView = (RecyclerView)findViewById(R.id.notificationRecycler);
        gson = new Gson();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        TextView cartitemcount1;

        MenuItem menuItem=menu.findItem(R.id.action_cart);
        View actionView= MenuItemCompat.getActionView(menuItem);
        cartitemcount1=(TextView) actionView.findViewById(R.id.cart_badge);

        cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ssd=new Intent(getApplicationContext(),cart.class);
                startActivity(ssd);
            }
        });

        return true;
    }
    private void Display(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

}


