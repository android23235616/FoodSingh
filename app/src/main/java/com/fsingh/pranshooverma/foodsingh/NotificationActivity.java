package com.fsingh.pranshooverma.foodsingh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.Collections;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    Gson gson;
    NotificationLists notificationLists;
    SharedPreferences sharedPreferences;

    BroadcastReceiver broadcastReceiver;
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
        getSupportActionBar().setTitle("");
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
            List<NotificationItem> shallowCopy = notificationLists.getNotification().subList(0, notificationLists.getNotification().size());
            Collections.reverse(shallowCopy);
            notificationAdapter = new NotificationAdapter(shallowCopy,this);
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
                localdatabase.notifmount.setVisibility(View.INVISIBLE);
                edit.apply();
                Intent i = new Intent();
                i.setAction(constants.menu2BroadcastReceiver);
                i.putExtra("data",0);
                sendBroadcast(i);
                recreate();

            }
        });


        setUpReceiver();

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NotificationActivity.this, "Pressed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUpReceiver() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(constants.broadCastReceiverNotification);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UpdateUi();
                    }
                },1000);
                Log.i("checkerbroadacast", "called");

            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void UpdateUi(){


        //recreate();
        String tempJson = sharedPreferences.getString(constants.foodsinghNotif,"");

        if(tempJson.equals("")){
            Display("You Have No Notification");

        }else{
            notificationLists = gson.fromJson(tempJson,NotificationLists.class);
            List<NotificationItem> shallowCopy = notificationLists.getNotification().subList(0, notificationLists.getNotification().size());
            Collections.reverse(shallowCopy);
            notificationAdapter = new NotificationAdapter(shallowCopy,this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(notificationAdapter);

            notificationAdapter.notifyDataSetChanged();

        }
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

}


