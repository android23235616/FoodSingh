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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    Gson gson;
    NotificationLists notificationLists;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    android.support.design.widget.FloatingActionButton floatingActionButton;
    TextView clear;
   // static TextView localdatabase.no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RemoveTop();
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        clear = (TextView)findViewById(R.id.text);
        floatingActionButton = (android.support.design.widget.FloatingActionButton)findViewById(R.id.new_clear);


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
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(notificationAdapter);

            notificationAdapter.notifyDataSetChanged();

        }

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //edit.putInt(constants.notifAmount,0);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString(constants.foodsinghNotif,"");
                edit.putInt(constants.notifAmount,0);
                localdatabase.notifications = 0;
                edit.apply();
                recreate();

            }
        });


    }

    private void RemoveTop(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem=menu.findItem(R.id.action_cart);
        View actionView= MenuItemCompat.getActionView(menuItem);
        TextView cartitemcount1=(TextView) actionView.findViewById(R.id.cart_badge);

        cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
        ImageView cart = (ImageView)actionView.findViewById(R.id.cartimage);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ssd=new Intent(getApplicationContext(),cart.class);
                startActivity(ssd);
            }
        });

        ImageView notif = (ImageView)actionView.findViewById(R.id.notif);

        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotificationActivity.this, NotificationActivity.class));
            }
        });

         localdatabase.notifmount = (TextView)actionView.findViewById(R.id.notification_badge);
        if(localdatabase.notifications==0){
            localdatabase.notifmount.setVisibility(View.INVISIBLE);
        }else {
            localdatabase.notifmount.setVisibility(View.VISIBLE);
            localdatabase.notifmount.setText(localdatabase.notifications+"");
        }

        return true;
    }
    private void Display(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

}


